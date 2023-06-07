package search

import (
	"database/sql"
	"log"

	"git.ucsc.edu/kho32/reciperetreiver/-/tree/main/backend/src/utils"
	"github.com/lib/pq"
)

func Search_helper(db *sql.DB, ingredients []string, num_substitutes int) []utils.Recipe { // the goal

	query := "SELECT id, ingrediants FROM table" //check spelling
	rows, err := db.Query(query)
	if err != nil {
		//cry
	}

	var recipes []utils.Recipe

	var recipe_ingr []string
	//id is id
	var id int
	for rows.Next() {
		if err = rows.Scan(&id, pq.Array(&recipe_ingr)); err != nil {
			log.Fatal(err)
		}

		if detect_Match(ingredients, recipe_ingr, num_substitutes) {
			//current recipe being used
			var current_recipe utils.Recipe
			//fetch recipe from the database
			//blegh sql calls
			//hate this
			recipe_query := db.QueryRow("SELECT id, name, ingredients, prep_time, cook_time, total_time, image_url, instructions, notes FROM recipes WHERE id = $1", id)
			err = recipe_query.Scan(&current_recipe.ID, &current_recipe.Name, &current_recipe.Ingredients, &current_recipe.PrepTime,
				&current_recipe.CookTime, &current_recipe.TotalTime, &current_recipe.ImageURL, &current_recipe.Instructions, &current_recipe.Notes)
			if err != nil {
				//cry
			}
			recipes = append(recipes, current_recipe)
		}
	}

	return recipes
}

func detect_Match(fridge_list, recipe_list []string, maxDifferences int) bool {
	// Convert the fridge list to dict

	fridge_set := make(map[string]bool)
	//recipe_set := make(map[string]bool)
	for _, item := range fridge_list {
		fridge_set[item] = true
	}

	//we don't care if there are items in the fridge that aren't in the recipe, but we care visa versa.s
	differences := 0
	for _, item := range recipe_list {
		if !fridge_set[item] {
			differences++
		}
	}

	return differences <= maxDifferences
}
