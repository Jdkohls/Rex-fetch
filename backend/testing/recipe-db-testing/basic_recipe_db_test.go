package test

import (
	"bytes"
	"encoding/json"
	"fmt"
	_ "github.com/lib/pq"
	"net/http"
	"testing"
)

type Recipe struct {
	ID           int    `json:"id"`
	Name         string `json:"name"`
	Ingredients  string `json:"ingredients"`
	PrepTime     string `json:"prep_time"`
	CookTime     string `json:"cook_time"`
	TotalTime    string `json:"total_time"`
	ImageURL     string `json:"image_url"`
	Instructions string `json:"instructions"`
	Notes        string `json:"notes"`
}

func TestRecipeDB(t *testing.T) {
	//This test tests whether the functions in main.go that deal with the recipe database work as expected.
	//Prerequisites:
	//1. our server, defined in main.go, is running
	//2. our local postgres server has a database in it named 'recipe_db'. Edit main.go to connect to that
	// database in line 43 (the one that has the sql.Open() function call)
	//3. 'recipe_db' has a table in it named recipes--defined in schema.sql--in the backend/sql folder in our project

	//Test flow:
	//a. Create a recipe: send a POST request to '/recipes' endpoint
	//b. GET that recipe via the '/recipes/id' endpoint, verify it was correct.
	//c. GET all recipes via GET request on '/recipes' endpoint, verify that our sample recipe is in that list
	//d. Update that recipe via a PUT request on '/recipes/id' endpoint, then GET it again and check if correct
	//e. Delete that recipe via DELET request on '/recipes/id' endpoint, check for 200 OK status code.
	var err error

	sampleRecipe := `{"name":"egg salad","ingredients":"eggs, lettuce, bananas","prep_time":"00:10:00", "cook_time":"00:00:00",
		"total_time":"00:10:00", "image_url":"N/A", "instructions":"beat your eggs, then your children", "notes":"grass tastes bad"}`

	req, err := http.NewRequest("POST", "http://localhost:8080/recipes", bytes.NewBufferString(sampleRecipe))
	if err != nil {
		t.Fatal(err)
	}
	req.Header.Set("Content-Type", "application/json")
	resp, err := http.DefaultClient.Do(req) //send post request, creating this recipe in the database
	if err != nil {
		t.Fatal(err)
	}
	defer resp.Body.Close()
	if status := resp.StatusCode; status != http.StatusOK {
		t.Errorf("Create recipe request returned wrong status code: got %v want %v", status, http.StatusOK)
	}
	var returnedRecipe Recipe
	err = json.NewDecoder(resp.Body).Decode(&returnedRecipe)
	if err != nil {
		t.Error("Unable to decode createRecipe()'s response JSON")
	}

	//retrieve that recipe via a GET request
	url := fmt.Sprintf("http://localhost:8080/recipes/%d", returnedRecipe.ID)
	req2, err := http.NewRequest("GET", url, nil)
	if err != nil {
		t.Fatal(err)
	}
	resp2, err := http.DefaultClient.Do(req2) //send GET req
	if err != nil {
		t.Fatal(err)
	}
	defer resp2.Body.Close()
	if status2 := resp2.StatusCode; status2 != http.StatusOK {
		t.Errorf("Get recipe request returned wrong status code: got %v want %v", status2, http.StatusOK)
	}
	var returnedRecipe2 Recipe
	err = json.NewDecoder(resp2.Body).Decode(&returnedRecipe2)
	if err != nil {
		t.Error("Unable to decode getRecipe()'s response JSON")
	}
	if returnedRecipe2.Name != "egg salad" {
		t.Error("Retrieved recipe not as expected.")
	}

	//retrieve all recipes (should just be one in there), and verify egg salad is present
	req3, err := http.NewRequest("GET", "http://localhost:8080/recipes", nil)
	if err != nil {
		t.Fatal(err)
	}
	getallresp, err := http.DefaultClient.Do(req3) //send get request to server
	var recList [1]Recipe
	err = json.NewDecoder(getallresp.Body).Decode(&recList)
	if err != nil {
		t.Error("Unable to decode getrecipes()'s response JSON")
	}
	defer getallresp.Body.Close()
	if recList[0].Name != "egg salad" {
		t.Error("egg salad not found in response given by getRecipes().")
	}

	//update the recipe, and check if worked:
	updateRecipe := `{"name":"ovary salad","ingredients":"ovaries, lettuce, bananas","prep_time":"00:10:00", "cook_time":"00:00:00",
		"total_time":"00:10:00", "image_url":"N/A", "instructions":"beat your ovaries, then your children", "notes":"grass tastes bad"}`
	req, err = http.NewRequest("PUT", url, bytes.NewBufferString(updateRecipe))
	if err != nil {
		t.Fatal(err)
	}
	_, err = http.DefaultClient.Do(req) //send PUT request to server
	if err != nil {
		t.Error("updateRecipe() Error")
	}
	req, err = http.NewRequest("GET", url, nil)
	if err != nil {
		t.Fatal(err)
	}
	updatedResp, err := http.DefaultClient.Do(req) //send get request to server
	var returnedRecipe3 Recipe
	err = json.NewDecoder(updatedResp.Body).Decode(&returnedRecipe3)
	if err != nil {
		t.Error("Unable to decode updateRecipe()'s response JSON")
	}
	defer updatedResp.Body.Close()
	if returnedRecipe3.Name != "ovary salad" {
		t.Error("name did not get updated after updateClient(). Name: ", returnedRecipe3.Name)
	}

	//finally, delete that recipe from database
	req, err = http.NewRequest("DELETE", url, nil)
	if err != nil {
		t.Fatal(err)
	}
	delResp, err := http.DefaultClient.Do(req) //send get request to server
	//check return status code: should be 200
	if delResp.StatusCode != http.StatusOK {
		t.Errorf("Delete recipe request returned wrong status code: got %v want %v", delResp.StatusCode, http.StatusOK)
	}

}
