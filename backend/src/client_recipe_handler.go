package main

import (
	"encoding/json"
	"net/http"

	"git.ucsc.edu/kho32/reciperetreiver/-/tree/main/backend/src/utils"
	"github.com/gorilla/mux"
)

func getClientRecipes(w http.ResponseWriter, r *http.Request) {
	var clientRecipes []utils.ClientRecipe
	rows, err := client_recipe_db.Query("SELECT * FROM client_recipes")
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	defer rows.Close()

	for rows.Next() {
		var clientRecipe utils.ClientRecipe
		if err := rows.Scan(&clientRecipe.ID, &clientRecipe.ClientID, &clientRecipe.RecipeID); err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
		clientRecipes = append(clientRecipes, clientRecipe)
	}

	json.NewEncoder(w).Encode(clientRecipes)
}

func getClientRecipe(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	clientRecipeID := params["id"]

	var clientRecipe utils.ClientRecipe

	err := client_recipe_db.QueryRow("SELECT * FROM client_recipes WHERE id = $1", clientRecipeID).Scan(&clientRecipe.ID, &clientRecipe.ClientID, &clientRecipe.RecipeID)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	json.NewEncoder(w).Encode(clientRecipe)
}

// createClientRecipe creates a new client recipe.
func createClientRecipe(w http.ResponseWriter, r *http.Request) {
	// Parse request body.
	var clientRecipe utils.ClientRecipe
	err := json.NewDecoder(r.Body).Decode(&clientRecipe)

	if clientRecipe.VerifyJWT() == false {
		http.Error(w, err.Error(), http.StatusUnauthorized)
		return
	}

	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	// Insert new client recipe into database.
	_, err = client_recipe_db.Exec("INSERT INTO client_recipes (client_id, recipe_id) VALUES ($1, $2)",
		clientRecipe.ClientID, clientRecipe.RecipeID)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusCreated)
}

// updateClientRecipe updates an existing client recipe.
func updateClientRecipe(w http.ResponseWriter, r *http.Request) {
	// Parse request body.
	var clientRecipe utils.ClientRecipe
	err := json.NewDecoder(r.Body).Decode(&clientRecipe)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	if clientRecipe.VerifyJWT() == false {
		http.Error(w, err.Error(), http.StatusUnauthorized)
		return
	}

	// Update client recipe in database.
	_, err = client_recipe_db.Exec("UPDATE client_recipes SET client_id = $1, recipe_id = $2 WHERE id = $3",
		clientRecipe.ClientID, clientRecipe.RecipeID, clientRecipe.ID)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)
}

// deleteClientRecipe deletes an existing client recipe.
func deleteClientRecipe(w http.ResponseWriter, r *http.Request) {
	// Get client recipe ID from URL parameter.
	params := mux.Vars(r)
	clientRecipeID := params["id"]

	var clientRecipe utils.ClientRecipe
	err := json.NewDecoder(r.Body).Decode(&clientRecipe)
	if clientRecipe.VerifyJWT() == false {
		http.Error(w, err.Error(), http.StatusUnauthorized)
		return
	}

	// Delete client recipe from database.
	_, err = client_recipe_db.Exec("DELETE FROM client_recipes WHERE id = $1", clientRecipeID)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)
}
