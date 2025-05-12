package main

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"

	"github.com/gorilla/mux"
)

// Define the event structure
type event struct {
	ID          string `json:"ID"`
	Title       string `json:"Title"`
	Description string `json:"Description"`
}
type records struct {
	Records []surfSpot `json:"records"`
	Offset string `json:"offset"`
}
type surfSpot struct {
	ID          string `json:"ID"`
	Fields 	    fields `json:"fields"`
	CreatedTime string `json:"createdTime"`
}
type fields struct {
	SurfBreak []string `json:"Surf Break"`	
	DifficultyLevel int `json:"Difficulty Level"`
	Destination string `json:"Destination"`
	Geocode string `json:"Geocode"`
	Influencers []string `json:"Influencers"`
	MagicSeaweedLink string `json:"Magic Seaweed Link"`
	Photos []photo `json:"Photos"`
	PeakSurfSeasonBegins string `json:"Peak Surf Season Begins"`
	PeakSurfSeasonEnds string `json:"Peak Surf Season Ends"`
	Adress string `json:"Address"`
	DestinationStateCountry string `json:"Destination State/Country"` // DestinationStateCountry


}

type photo struct {
	ID string `json:"id"`
	Url string `json:"url"`
	Filename string `json:"filename"`
	Size int `json:"size"`
	Types string `json:"type"`
	Thumbnail thumbnail `json:"thumbnails"`
}
type thumbnail struct {
	Small thumbnaildata `json:"small"`
	Large thumbnaildata `json:"large"`
	Full thumbnaildata `json:"full"`
}
type thumbnaildata struct {
	Url string `json:"url"`
	Width int `json:"width"`
	Height int `json:"height"`
}



type allEvents []event
type allSurfSpots records
type allPhotos []photo
type allThumbnail thumbnail
type allThumbnaildata thumbnaildata
type allFields []fields


var events = allEvents{
	{
		ID:          "1",
		Title:       "Introduction to Golang",
		Description: "Come join us for a chance to learn how golang works and get to eventually try it out",
	},
}

var surfSpots = allSurfSpots{
	
	Records: []surfSpot{
		{
		ID:          "1",
		Fields: fields{
			SurfBreak: []string{"North Point", "South Point"},
			DifficultyLevel: 3,
			Destination: "North Shore",
			Geocode: "21.6, -158.1",
			Influencers: []string{"John Doe", "Jane Smith"},
			MagicSeaweedLink: "https://magicseaweed.com/",
			Photos: []photo{
				{
					ID: "1",
					Url: "https://example.com/photo1.jpg",
					Filename: "photo1.jpg",
					Size: 12345,
					Types: "image/jpeg",
					Thumbnail: thumbnail{
						Small: thumbnaildata{
							Url: "https://example.com/photo1_small.jpg",
							Width: 100,
							Height: 100,
						},
						Large: thumbnaildata{
							Url: "https://example.com/photo1_large.jpg",
							Width: 800,
							Height: 600,
						},
						Full: thumbnaildata{
							Url: "https://example.com/photo1_full.jpg",
							Width: 1920,
							Height: 1080,
						},
					},
				}},
			PeakSurfSeasonBegins: "2023-06-01",
			PeakSurfSeasonEnds: "2023-09-01",
			Adress: "123 Main St",
			DestinationStateCountry: "Hawaii",
		},
		CreatedTime: "2023-01-01T00:00:00Z",
	}},
	Offset: "121",
}

// Handlers
func homeLink(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "Welcome home!")
}

func createEvent(w http.ResponseWriter, r *http.Request) {
	var newEvent event
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		fmt.Fprintf(w, "Please provide event title and description")
		return
	}

	json.Unmarshal(reqBody, &newEvent)
	events = append(events, newEvent)
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(newEvent)
}

func getAllEvents(w http.ResponseWriter, r *http.Request) {
	json.NewEncoder(w).Encode(events)
}

func getAllSurfSpots(w http.ResponseWriter, r *http.Request) {
	json.NewEncoder(w).Encode(surfSpots)
}

// Main function
func main() {
	router := mux.NewRouter().StrictSlash(true)

	router.HandleFunc("/", homeLink)
	router.HandleFunc("/events", createEvent).Methods("POST")
	router.HandleFunc("/events", getAllEvents).Methods("GET")
	router.HandleFunc("/events/{id}", getOneEvent).Methods("GET")
	router.HandleFunc("/api/spots", getAllSurfSpots).Methods("GET")

	fmt.Println("Server started at :8080")
	log.Fatal(http.ListenAndServe(":8080", router))
}



func getOneEvent(w http.ResponseWriter, r *http.Request) {
	eventID := mux.Vars(r)["id"]

	for _, singleEvent := range events {
		if singleEvent.ID == eventID {
			json.NewEncoder(w).Encode(singleEvent)
		}
	}
}