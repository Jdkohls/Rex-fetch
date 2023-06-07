package main

import (
	"database/sql"
	"log"
	"net/http"

	_ "git.ucsc.edu/kho32/reciperetreiver/-/tree/main/backend/src/login"
	"git.ucsc.edu/kho32/reciperetreiver/-/tree/main/backend/src/utils"
	"github.com/gorilla/mux"
	_ "github.com/lib/pq"
)

var client_db *sql.DB
var recipe_db *sql.DB
var client_recipe_db *sql.DB

// To start the server, cd to '\src' directory, and type 'go run .' in terminal
// in the sql.Open() call, change username:password to whatever username and password you set up for
// your local postgres server (postgres:postgres for mine)
func main() {
	var err error
	client_db, recipe_db, client_recipe_db = utils.OpenDatabases()
	if err != nil {
		log.Fatal(err)
	}
	defer client_db.Close()
	defer recipe_db.Close()
	defer client_recipe_db.Close()

	r := mux.NewRouter()
	//r.HandleFunc("/clients", getClients).Methods("GET")
	r.HandleFunc("/clients/{id:[a-f0-9]{6}}", getClient).Methods("GET")

	r.HandleFunc("/clients", createClient).Methods("POST")
	r.HandleFunc("/clients/{id:[a-f0-9]{6}}", updateClient).Methods("PUT")
	r.HandleFunc("/clients/{id:[a-f0-9]{6}}", deleteClient).Methods("DELETE")

	r.HandleFunc("/recipes", getRecipes).Methods("GET")
	r.HandleFunc("/recipes/{id:[a-f0-9]{6}}", getRecipe).Methods("GET")

	r.HandleFunc("/recipes", createRecipe).Methods("POST")
	r.HandleFunc("/recipes/{id:[a-f0-9]{6}}", updateRecipe).Methods("PUT")
	r.HandleFunc("/recipes/{id:[a-f0-9]{6}}", deleteRecipe).Methods("DELETE")

	r.HandleFunc("/client_recipes", getClientRecipes).Methods("GET")
	r.HandleFunc("/client_recipes/{id:[a-f0-9]{6}}", getClientRecipe).Methods("GET")
	r.HandleFunc("/client_recipes", createClientRecipe).Methods("POST")
	r.HandleFunc("/client_recipes/{id:[a-f0-9]{6}}", updateClientRecipe).Methods("PUT")
	r.HandleFunc("/client_recipes/{id:[a-f0-9]{6}}", deleteClientRecipe).Methods("DELETE")

	r.NotFoundHandler = http.HandlerFunc(defaultHandler)

	log.Fatal(http.ListenAndServe(":8080", r))
}

func defaultHandler(w http.ResponseWriter, r *http.Request) {
	http.Error(w, "Not found", http.StatusNotFound)
}
