package main


import (
	 "fmt"
    "time" 
  "gorm.io/gorm"
  "gorm.io/driver/mysql"
//   "github.com/google/uuid"
)

var db *gorm.DB

type SurfSpotInput struct {
	Destination              string `json:"Destination"`
	DestinationStateCountry  string `json:"Destination State/Country"`
	Address                  string `json:"Address"`
	SurfBreak                string `json:"Surf Break"`
	DifficultyLevel          int    `json:"Difficulty Level"`
	Rating                   float64 `json:"Rating"`
	Geocode                  string `json:"Geocode"`
	MagicSeaweedLink         string `json:"Magic Seaweed Link"`
	Influencers              string `json:"Influencers"`
	PhotoURL                 string `json:"url"`
	PeakSurfSeasonBegins     string `json:"Peak Surf Season Begins"`
	PeakSurfSeasonEnds       string `json:"Peak Surf Season Ends"`
}

type SurfSpot struct {
	ID                     string   `json: "id" gorm:"primaryKey"`
	Destination string `json:"Destination"`
	DestinationStateCountry string `json: "Destination State/Country"`
	Address                string   `json: "Address"`
	SurfBreak              string   `json: "Surf Break" gorm:"type:varchar(100)"`
	DifficultyLevel        int    `json: "Difficulty Level"`
	Rating                 float64  `json: "Rating" gorm:"type:decimal(3,1)"`
	Geocode                string    `json: "Geocode"`// Or use custom Point type
	MagicSeaweedLink       string     `json: "Magic Seaweed Link" gorm:"type:text"` // if links are long
	Influencers            string    `json: "Influencers" gorm:"type:text"` // or json / array
	PhotoURL               string    `json: "url" gorm:"type:text"` // to support long URLs
	PeakSurfSeasonBegins   time.Time `json: "Peak Surf Season Begins" gorm:"type:date"`
	PeakSurfSeasonEnds     time.Time `json: "Peak Surf Season Ends" gorm:"type:date"`
	CreatedTime            time.Time `json: "Created Time" gorm:"type:datetime"`
}


func database() {
	dsn := "utilisateur:root@tcp(127.0.0.1:3306)/wavely?charset=utf8mb4&parseTime=True&loc=Local"
	var err error
	db, err = gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		fmt.Println("failed to connect database")
	
  }
  fmt.Println("✅ Connected to DB")

  // Migrate the schema
  db.AutoMigrate(&SurfSpot{})

//   ends, err := time.Parse("2006-01-02", "2025-12-01")
//   starts, err := time.Parse("2006-01-02", "2025-01-01")
//   // Create
//   db.Create(&SurfSpot{ID: uuid.New().String(), Destination: "Paris Plage", DestinationStateCountry:"France", SurfBreak:"Point break", DifficultyLevel:3, Rating:4.0, PhotoURL:"https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.gettyimages.fr%2Fphotos%2Fparis-plage&psig=AOvVaw0aXS_G7HEGvP7nLexWDJV_&ust=1747316595318000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCICpvNmLo40DFQAAAAAdAAAAABAE",
//   PeakSurfSeasonBegins: starts, PeakSurfSeasonEnds: ends})

//   // Read
//   var product Product
//   db.First(&product, 1) // find product with integer primary key
//   db.First(&product, "code = ?", "D42") // find product with code D42

//   // Update - update product's price to 200
//   db.Model(&product).Update("Price", 200)
//   // Update - update multiple fields
//   db.Model(&product).Updates(Product{Price: 200, Code: "F42"}) // non-zero fields
//   db.Model(&product).Updates(map[string]interface{}{"Price": 200, "Code": "F42"})

//   // Delete - delete product
//   db.Delete(&product, 1)
}

