package main

import (
	"encoding/json"
	"net/http"

	"git.ucsc.edu/kho32/reciperetreiver/-/tree/main/backend/src/utils"
	"github.com/gorilla/mux"
)

func getRecipes(w http.ResponseWriter, r *http.Request) {
	rows, err := recipe_db.Query("SELECT id, name, ingredients, prep_time, cook_time, total_time, image_url, instructions, notes FROM recipes")
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	defer rows.Close()

	recipes := []utils.Recipe{}
	for rows.Next() {
		recipe := utils.Recipe{}
		err := rows.Scan(&recipe.ID, &recipe.Name, &recipe.Ingredients, &recipe.PrepTime, &recipe.CookTime, &recipe.TotalTime, &recipe.ImageURL, &recipe.Instructions, &recipe.Notes)
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
		recipes = append(recipes, recipe)
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(recipes)
}

func getRecipe(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	recipeID := params["id"]

	row := recipe_db.QueryRow("SELECT id, name, ingredients, prep_time, cook_time, total_time, image_url, instructions, notes FROM recipes WHERE id = $1", recipeID)

	recipe := utils.Recipe{}
	err := row.Scan(&recipe.ID, &recipe.Name, &recipe.Ingredients, &recipe.PrepTime, &recipe.CookTime, &recipe.TotalTime, &recipe.ImageURL, &recipe.Instructions, &recipe.Notes)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(recipe)
}

func createRecipe(w http.ResponseWriter, r *http.Request) {
	var recipe utils.Recipe
	err := json.NewDecoder(r.Body).Decode(&recipe)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	query := "INSERT INTO recipes(name, ingredients, prep_time, cook_time, total_time, image_url, instructions, notes) VALUES($1, $2, $3, $4, $5, $6, $7, $8) RETURNING id"
	err = recipe_db.QueryRow(query, recipe.Name, recipe.Ingredients, recipe.PrepTime, recipe.CookTime, recipe.TotalTime, recipe.ImageURL, recipe.Instructions, recipe.Notes).Scan(&recipe.ID)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(recipe)
}

func updateRecipe(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	recipeID := params["id"]

	var recipe utils.Recipe
	err := json.NewDecoder(r.Body).Decode(&recipe)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	query := "UPDATE recipes SET name = $1, ingredients = $2, prep_time = $3, cook_time = $4, total_time = $5, image_url = $6, instructions = $7, notes = $8 WHERE id = $9"
	_, err = recipe_db.Exec(query, recipe.Name, recipe.Ingredients, recipe.PrepTime, recipe.CookTime, recipe.TotalTime, recipe.ImageURL, recipe.Instructions, recipe.Notes, recipeID)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)
}

func deleteRecipe(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	recipeID := params["id"]

	_, err := recipe_db.Exec("DELETE FROM client_recipes WHERE recipe_id = $1", recipeID)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	_, err = recipe_db.Exec("DELETE FROM recipes WHERE id = $1", recipeID)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)
}
