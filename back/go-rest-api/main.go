package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"

	"github.com/gorilla/mux"
)

// Define the event structure
// type event struct {
// 	ID          string `json:"ID"`
// 	Title       string `json:"Title"`
// 	Description string `json:"Description"`
// }
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
	Address string `json:"Address"`
	DestinationStateCountry string `json:"Destination State/Country"` // DestinationStateCountry


}

type SurfSpotSummary struct {
    SurfBreak []string `json:"Surf Break"`
    Photos    []photo  `json:"Photos"`
    Address   string   `json:"Address"`
}

type Onespot struct {
	DifficultyLevel int `json:"Difficulty Level"`
	PeakSurfSeasonBegins string `json:"Peak Surf Season Begins"`
	PeakSurfSeasonEnds string `json:"Peak Surf Season Ends"`
	DestinationStateCountry string `json:"Destination State/Country"`
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


type allSurfSpots records
type allPhotos []photo
type allThumbnail thumbnail
type allThumbnaildata thumbnaildata
type allFields []fields


// var events = allEvents{
// 	{
// 		ID:          "1",
// 		Title:       "Introduction to Golang",
// 		Description: "Come join us for a chance to learn how golang works and get to eventually try it out",
// 	},
// }

var surfSpots = allSurfSpots{
	Records: []surfSpot{
		{
			ID: "rec5aF9TjMjBicXCK",
			Fields: fields{
				SurfBreak:           []string{"Reef Break"},
				DifficultyLevel:     4,
				Destination:         "Pipeline",
				Geocode:             ":grand_cercle_bleu: eyJpIjoiUGlwZWxpbmUsIE9haHUsIEhhd2FpaSIsIm8iOnsic3RhdHVzIjoiT0siLCJmb3JtYXR0ZWRBZGRyZXNzIjoiRWh1a2FpIEJlYWNoIFBhcmssIEhhbGVpd2EsIEhJIDk2NzEyLCBVbml...",
				Influencers:         []string{"recD1zp1pQYc8O7l2", "rec1ptbRPxhS8rRun"},
				MagicSeaweedLink:    "https://magicseaweed.com/Pipeline-Backdoor-Surf-Report/616/",
				Photos: []photo{
					{
						ID:       "attf6yu03NAtCuv5L",
						Url:      "https://v5.airtableusercontent.com/v3/u/40/40/1746180000000/q_gkT3iQi_H59TLXaPDg_w/9Cn-4XtmK73Yon9UqpEdJNJkV_hnE4RVFlon_HKR07sFeujcRhwmtfT6fASQYTLdQDycxqMYT5ZH0oU9fTIFSCzroC3w_ugwRasUTsQLa1Jg2Mwj9dY1FIHIeEzH1E8K/86RABu2oA7oED7NRQCPYJzepyhyzxrZ1_a8ur7Q6_2Q",
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
					},
				},
				PeakSurfSeasonBegins:     "2018-07-22",
				PeakSurfSeasonEnds:       "2018-08-31",
				Address:                   "Pipeline, Oahu, Hawaii",
				DestinationStateCountry:  "Oahu, Hawaii",
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

// func createEvent(w http.ResponseWriter, r *http.Request) {
// 		w.Header().Set("Content-Type", "application/json")

// 	var newEvent event
// 	reqBody, err := ioutil.ReadAll(r.Body)
// 	if err != nil {
// 		fmt.Fprintf(w, "Please provide event title and description")
// 		return
// 	}

// 	json.Unmarshal(reqBody, &newEvent)
// 	events = append(events, newEvent)
// 	w.WriteHeader(http.StatusCreated)
// 	json.NewEncoder(w).Encode(newEvent)
// }

// func getAllEvents(w http.ResponseWriter, r *http.Request) {
// 		w.Header().Set("Content-Type", "application/json")

// 	json.NewEncoder(w).Encode(events)
// }

func getAllSurfSpots(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	    var summarySpots []SurfSpotSummary
	for _, spot := range surfSpots.Records {
		summarySpots = append(summarySpots, SurfSpotSummary{
			SurfBreak: spot.Fields.SurfBreak,
			Photos:    spot.Fields.Photos,
			Address:   spot.Fields.Address,
		})
		
	}
	json.NewEncoder(w).Encode(map[string]interface{}{
		"records": summarySpots,
	})
}

func getOneSurfSpot(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	    var onesurfspot []Onespot
	for _, spot := range surfSpots.Records {
		onesurfspot = append(onesurfspot, Onespot{
			DifficultyLevel: spot.Fields.DifficultyLevel,
			PeakSurfSeasonBegins: spot.Fields.PeakSurfSeasonBegins,
			PeakSurfSeasonEnds: spot.Fields.PeakSurfSeasonEnds,
			DestinationStateCountry: spot.Fields.DestinationStateCountry,
		})
		
	}
	json.NewEncoder(w).Encode(onesurfspot)
}

// Main function
func main() {
	router := mux.NewRouter().StrictSlash(true)

	router.HandleFunc("/", homeLink)
	router.HandleFunc("/api/spots", getAllSurfSpots).Methods("GET")
	router.HandleFunc("/api/spots/{id}", getOneSurfSpot).Methods("GET")

	fmt.Println("Server started at :8080")
	log.Fatal(http.ListenAndServe("0.0.0.0:8080", router))


}



