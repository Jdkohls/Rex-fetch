package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"time"

	"github.com/gorilla/mux"
)

type incomingIngr struct {
	Ingredients   []string `json:"ingredients"`
	PrepTime      int      `json:"prep time"`
	TotalMinutes  int      `json:"total minutes"`
	RecipeOptions int      `json:"recipe options"`
	Substitutes   int      `json:"substitutes"`
}

type outgoingRecipe struct {
	Ingredients []string `json:"ingredients"`
	PrepTime    int      `json:"prep time"`
	TotalTime   int      `json:"total time"`
	Steps       []string `json:"steps"`
}

func main() {
	router := mux.NewRouter()

	// Define your API endpoint
	router.HandleFunc("/recipes", handleRecipes).Methods("POST")

	// Start the server
	srv := &http.Server{
		Handler:      router,
		Addr:         "localhost:8000",
		WriteTimeout: 15 * time.Second,
		ReadTimeout:  15 * time.Second,
	}
	log.Fatal(srv.ListenAndServe())
}

func handleRecipes(w http.ResponseWriter, r *http.Request) {
	// Parse the incoming JSON data
	var incomingData incomingIngr
	err := json.NewDecoder(r.Body).Decode(&incomingData)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	// Make the API call to Spoonacular
	// Replace API_KEY with your Spoonacular API key
	apiKey := "API_KEY"
	url := fmt.Sprintf("https://api.spoonacular.com/recipes/complexSearch?apiKey=%s", apiKey)

	// Customize the API call based on your requirements using the incomingData

	// Example: Add ingredients to the API call
	for _, ingredient := range incomingData.Ingredients {
		url += fmt.Sprintf("&includeIngredients=%s", ingredient)
	}

	// Send the API request
	response, err := http.Get(url)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	defer response.Body.Close()

	// Parse the API response
	var responseData outgoingRecipe
	err = json.NewDecoder(response.Body).Decode(&responseData)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	// Convert the response data to JSON
	jsonData, err := json.Marshal(responseData)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	// Set the response headers and write the JSON data
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write(jsonData)
}
