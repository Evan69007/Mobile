package main

import (
	"encoding/json"
	"fmt"
	// "io/ioutil"
	"log"
	"net/http"
	"time"
	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"strings"
	"errors"
	"gorm.io/gorm"
)

// Structures

type records struct {
	Records []surfSpot `json:"records"`
	Offset  string     `json:"offset"`
}

type surfSpot struct {
	ID          string `json:"ID"`
	Fields      fields `json:"fields"`
	CreatedTime string `json:"createdTime"`
}

type fields struct {
	SurfBreak               []string `json:"Surf Break"`
	DifficultyLevel         int      `json:"Difficulty Level"`
	Destination             string   `json:"Destination"`
	Geocode                 string   `json:"Geocode"`
	Rating                 	float64  `json:"Rating"`
	Influencers             []string `json:"Influencers"`
	MagicSeaweedLink        string   `json:"Magic Seaweed Link"`
	Photos                  []photo  `json:"Photos"`
	PeakSurfSeasonBegins    string   `json:"Peak Surf Season Begins"`
	PeakSurfSeasonEnds      string   `json:"Peak Surf Season Ends"`
	Address                 string   `json:"Address"`
	DestinationStateCountry string   `json:"Destination State/Country"`

}

type SurfSpotSummary struct {
	SurfBreak   []string `json:"Surf Break"`
	Photos      []photo  `json:"Photos"`
	Destination string   `json:"Destination"`
	Rating      float64  `json:"Rating"`
}

type Onespot struct {
	DifficultyLevel         int    `json:"Difficulty Level"`
	PeakSurfSeasonBegins    string `json:"Peak Surf Season Begins"`
	PeakSurfSeasonEnds      string `json:"Peak Surf Season Ends"`
	DestinationStateCountry string `json:"Destination State/Country"`
}

type photo struct {
	ID        string    `json:"id"`
	Url       string    `json:"url"`
	Filename  string    `json:"filename"`
	Size      int       `json:"size"`
	Types     string    `json:"type"`
	Thumbnail thumbnail `json:"thumbnails"`
}

type thumbnail struct {
	Small thumbnaildata `json:"small"`
	Large thumbnaildata `json:"large"`
	Full  thumbnaildata `json:"full"`
}

type thumbnaildata struct {
	Url    string `json:"url"`
	Width  int    `json:"width"`
	Height int    `json:"height"`
}

func parseDate(dateStr string) time.Time {
	t, _ := time.Parse("2006-01-02", dateStr)
	return t
}


// Handlers


func getAllSurfSpots(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	var spots []SurfSpot
	if err := db.Find(&spots).Error; err != nil {
		http.Error(w, "Error retrieving spots", http.StatusInternalServerError)
		return
	}
		// Define a struct for the wrapped record
	type Record struct {
		ID     string          `json:"id"`
		Fields SurfSpotSummary `json:"fields"`
	}

	var records []Record
	
	for _, spot := range spots {
		var Photo []photo
		Photo = append(Photo, photo{
			Url: spot.PhotoURL,
		})
		summary := SurfSpotSummary{
			SurfBreak:   strings.Split(spot.SurfBreak, ", "),
			Photos:      Photo,
			Destination: spot.Destination,
			Rating:    spot.Rating,
		}
		records = append(records, Record{
			ID:     spot.ID,
			Fields: summary,
		})
	}
	json.NewEncoder(w).Encode(map[string]interface{}{
		"records": records,
	})
}


func getOneSurfSpot(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	vars := mux.Vars(r)
	id := vars["id"]

	// Define a minimal fields struct for the response
	type FieldsResponse struct {
		DifficultyLevel         int    `json:"Difficulty Level"`
		PeakSurfSeasonBegins    string `json:"Peak Surf Season Begins"`
		PeakSurfSeasonEnds      string `json:"Peak Surf Season Ends"`
		DestinationStateCountry string `json:"Destination State/Country"`
	}

	// Define the full response structure
	type Record struct {
		Fields FieldsResponse `json:"fields"`
	}
	type Response struct {
		Records []Record `json:"records"`
	}

	var spot SurfSpot
	
	if err := db.First(&spot, "id = ?", id).Error; err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			http.Error(w, "Spot not found", http.StatusNotFound)
			return
		}
		http.Error(w, "DB error", http.StatusInternalServerError)
		return
	}else
	{
		layout := "2006-01-02"

		response := Response{
			Records: []Record{
				{
					Fields: FieldsResponse{
						DifficultyLevel:         spot.DifficultyLevel,
						PeakSurfSeasonBegins:    spot.PeakSurfSeasonBegins.Format(layout),
						PeakSurfSeasonEnds:      spot.PeakSurfSeasonEnds.Format(layout),
						DestinationStateCountry: spot.DestinationStateCountry,
					},
				},
			},
		}
		json.NewEncoder(w).Encode(response)
	}
}

func addARating(w http.ResponseWriter, r *http.Request) {
	fmt.Println("Adding a rating...")
	w.Header().Set("Content-Type", "application/json")
	type RatingPayload struct {
		Id     string  `json:"id"`
		Rating float64 `json:"rating"`
	}
	var payload RatingPayload
	if err := json.NewDecoder(r.Body).Decode(&payload); err != nil {
		http.Error(w, "Invalid JSON format", http.StatusBadRequest)
		return
	}

	var spot SurfSpot
	var id = payload.Id
	if err := db.First(&spot, "id = ?", id).Error; err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			http.Error(w, "Spot not found", http.StatusNotFound)
			return
		}
		http.Error(w, "DB error", http.StatusInternalServerError)
		return
	}else{
		var rating = (spot.Rating +  payload.Rating)/2
		db.Model(&spot).Update("rating", rating)
		json.NewEncoder(w).Encode(spot)
		return
	}
	
}

func updatesurfrating(w http.ResponseWriter, r *http.Request) {
	fmt.Println("Edtiting a rating...")
	w.Header().Set("Content-Type", "application/json")
	vars := mux.Vars(r)
	id := vars["id"]

	// Struct to decode the incoming JSON
	type RatingPayload struct {
		Rating float64 `json:"rating"`
	}
	var payload RatingPayload
	if err := json.NewDecoder(r.Body).Decode(&payload); err != nil {
		http.Error(w, "Invalid JSON format", http.StatusBadRequest)
		return
	}

	var spot SurfSpot

	
	if err := db.First(&spot, "id = ?", id).Error; err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			http.Error(w, "Spot not found", http.StatusNotFound)
			return
		}
		http.Error(w, "DB error", http.StatusInternalServerError)
		return
	}else{
		db.Model(&spot).Update("rating", payload.Rating)
		json.NewEncoder(w).Encode(spot)
		fmt.Println("Rating edited")
		return
	}	
}
		
func createSurfSpot(w http.ResponseWriter, r *http.Request) {
	var input SurfSpotInput
	if err := json.NewDecoder(r.Body).Decode(&input); err != nil {
		http.Error(w, "Invalid input: "+err.Error(), http.StatusBadRequest)
		return
	}

	layout := "2006-01-02"
	start, err1 := time.Parse(layout, input.PeakSurfSeasonBegins)
	end, err2 := time.Parse(layout, input.PeakSurfSeasonEnds)

	if err1 != nil || err2 != nil {
		http.Error(w, "Invalid date format", http.StatusBadRequest)
		return
	}

	spot := SurfSpot{
		ID:                     uuid.New().String(),
		Destination:            input.Destination,
		DestinationStateCountry: input.DestinationStateCountry,
		Address:                input.Address,
		SurfBreak:              input.SurfBreak,
		DifficultyLevel:        input.DifficultyLevel,
		Rating:                 input.Rating,
		Geocode:                input.Geocode,
		MagicSeaweedLink:       input.MagicSeaweedLink,
		Influencers:            input.Influencers,
		PhotoURL:               input.PhotoURL,
		PeakSurfSeasonBegins:   start,
		PeakSurfSeasonEnds:     end,
		CreatedTime:            time.Now(),
	}

	if err := db.Create(&spot).Error; err != nil {
		http.Error(w, "Failed to create", http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(spot)
}

// Main

func main() {
	router := mux.NewRouter().StrictSlash(true)

	router.HandleFunc("/api/spots", getAllSurfSpots).Methods("GET")
	router.HandleFunc("/api/spots/{id}", getOneSurfSpot).Methods("GET")
	router.HandleFunc("/api/spots/{id}", updatesurfrating).Methods("PUT")

	router.HandleFunc("/api/addspots", createSurfSpot).Methods("POST")
	router.HandleFunc("/api/spots", addARating).Methods("POST")

	database()
	fmt.Println("Server started at :8080")
	fmt.Println(time.Now().Format(time.RFC3339))
	log.Fatal(http.ListenAndServe("0.0.0.0:8080", router))
}



