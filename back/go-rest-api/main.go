package main

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"time"
	"github.com/google/uuid"
	"github.com/gorilla/mux"
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

// Data (initial)

var surfSpots = records{
	Records: []surfSpot{
		{
			ID: "rec5aF9TjMjBicXCK",
			Fields: fields{
				SurfBreak:        []string{"Reef Break"},
				DifficultyLevel:  4,
				Destination:      "Pipeline",
				Geocode:          "GeocodeDataHere",
				Rating:          4.5,
				Influencers:      []string{"recD1zp1pQYc8O7l2", "rec1ptbRPxhS8rRun"},
				MagicSeaweedLink: "https://magicseaweed.com/Pipeline-Backdoor-Surf-Report/616/",
				Photos: []photo{
					{
						ID:       "attf6yu03NAtCuv5L",
						Url:      "https://solidsurfhouse.com/wp-content/uploads/2023/09/Riding-the-Waves-The-10-Best-Surf-Spots-in-Mentawai.jpg",
						Filename: "thomas-ashlock-64485-unsplash.jpg",
						Size:     688397,
						Types:    "image/jpeg",
						Thumbnail: thumbnail{
							Small: thumbnaildata{
								Url:    "https://dl.airtable.com/yfKxR9ZQqiT7drKxpjdF_small_thomas-ashlock-64485-unsplash.jpg",
								Width:  52,
								Height: 36,
							},
							Large: thumbnaildata{
								Url:    "https://dl.airtable.com/cFfMuU8NQjaEskeC3B2h_large_thomas-ashlock-64485-unsplash.jpg",
								Width:  744,
								Height: 512,
							},
							Full: thumbnaildata{
								Url:    "https://dl.airtable.com/psynuQNmSvOTe3BWa0Fw_full_thomas-ashlock-64485-unsplash.jpg",
								Width:  2233,
								Height: 1536,
							},
						},
					}},
				PeakSurfSeasonBegins:    "2018-07-22",
				PeakSurfSeasonEnds:      "2018-08-31",
				Address:                 "Pipeline, Oahu, Hawaii",
				DestinationStateCountry: "Oahu, Hawaii",
			},
			CreatedTime: "2018-05-31T00:16:16.000Z",
		},
	},
	Offset: "121",
}

// Handlers

func homeLink(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]string{"message": "Welcome home!"})
}

func getSurfSpots(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(surfSpots)
}

func getAllSurfSpots(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	// Define a struct for the wrapped record
	type Record struct {
		ID     string          `json:"id"`
		Fields SurfSpotSummary `json:"fields"`
	}

	var records []Record
	for _, spot := range surfSpots.Records {
		summary := SurfSpotSummary{
			SurfBreak:   spot.Fields.SurfBreak,
			Photos:      spot.Fields.Photos,
			Destination: spot.Fields.Destination,
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
		Rating                  float64 `json:"Rating"`
	}

	// Define the full response structure
	type Record struct {
		Fields FieldsResponse `json:"fields"`
	}
	type Response struct {
		Records []Record `json:"records"`
	}

	for _, spot := range surfSpots.Records {
		if spot.ID == id {
			response := Response{
				Records: []Record{
					{
						Fields: FieldsResponse{
							DifficultyLevel:         spot.Fields.DifficultyLevel,
							PeakSurfSeasonBegins:    spot.Fields.PeakSurfSeasonBegins,
							PeakSurfSeasonEnds:      spot.Fields.PeakSurfSeasonEnds,
							DestinationStateCountry: spot.Fields.DestinationStateCountry,
							Rating:          		 spot.Fields.Rating,
						},
					},
				},
			}
			json.NewEncoder(w).Encode(response)
			return
		}
	}

	http.Error(w, "Surf spot not found", http.StatusNotFound)
}

func createSurfSpot(w http.ResponseWriter, r *http.Request) {

	fmt.Println("Creating a new surf spot...")
	w.Header().Set("Content-Type", "application/json")
	defer r.Body.Close()

	var newFields fields
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	var wrapper struct {
		Fields fields `json:"fields"`
	}

	if err := json.Unmarshal(reqBody, &wrapper); err != nil {
		http.Error(w, "Invalid JSON format", http.StatusBadRequest)
		return
	}

	newFields = wrapper.Fields
	newSpot := surfSpot{
		ID:          uuid.New().String(),
		Fields:      newFields,
		CreatedTime: time.Now().Format(time.RFC3339),
	}
	fmt.Println("New fields:", newFields)
	fmt.Println("New surf spot created:", newSpot)
	surfSpots.Records = append(surfSpots.Records, newSpot)
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(newSpot)
}



// Main

func main() {
	router := mux.NewRouter().StrictSlash(true)

	router.HandleFunc("/", homeLink)
	router.HandleFunc("/api/spots", getAllSurfSpots).Methods("GET")
	router.HandleFunc("/api/spots/{id}", getOneSurfSpot).Methods("GET")
	router.HandleFunc("/api/spots", createSurfSpot).Methods("POST")

	router.HandleFunc("/api/all/spots", getSurfSpots).Methods("GET")

	fmt.Println("Server started at :8080")
	fmt.Println(time.Now().Format(time.RFC3339))
	log.Fatal(http.ListenAndServe("0.0.0.0:8080", router))
}



