package test

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
	"testing"

	_ "github.com/lib/pq"
)

// Prerequisites:
// 1. Make sure the server that is defined in main.go is running on localhost, port 8080.
// 2. Make sure your local Postgres server has a database in it named "client_db", and that database has a "clients" table.
// 3. Make sure your local Postgres server also has a database in it named "recipe_db", which has tables "recipes" and "client_recipes"
// (Look at 'schema.sql' for what the databases need specifically)
// NOTE-- I have removed the "client_id" field from the client_recipes table here, because it was giving an error. Apparently,
// we can't just access another database's table from within a different database.

type Client struct {
	ID              int    `json:"id"`
	Username        string `json:"username"`
	Password        string `json:"password"`
	Ingredients     string `json:"ingredients"`
	FavoriteRecipes string `json:"favorite_recipes"`
}

func TestDatabaseFunctions(t *testing.T) {
	// This function does the following:
	// 1. Create a new client in our database using a POST request to the /clients endpoint (tests method "createClient()")
	// 2. Get all current client as an array of JSON objects (tests method "getClients()""), and check if correct
	// 3. Get one specific client using the /clients/<id> endpoint, where 'id' was returned by step 1 (this tests "getClient()")
	// 4. Update that specific client to now have the username "bobsballs", then GET it and check if updated (tests "updateClient()")
	// 5. Delete that client by sending a DELETE request to /clients/<id> endpoint (tests method "deleteClient()")
	var err error

	clientJSON := `{"username":"testuser","password":"testpass","ingredients":"flour, sugar","favorite_recipes":"{1}"}`
	req, err := http.NewRequest("POST", "http://localhost:8080/clients", bytes.NewBufferString(clientJSON))
	if err != nil {
		t.Fatal(err)
	}
	req.Header.Set("Content-Type", "application/json")

	//send a request to server to create client for us.
	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		t.Fatal(err)
	}
	defer resp.Body.Close()
	if status := resp.StatusCode; status != http.StatusOK {
		t.Errorf("Create Client request returned wrong status code: got %v want %v", status, http.StatusOK)
	}
	var createdClient Client
	err = json.NewDecoder(resp.Body).Decode(&createdClient)
	if err != nil {
		t.Error("Unable to decode createClient()'s response JSON")
	}

	//call getClients()--since there's only one client, it should return a list with the testuser client
	/*
		req, err = http.NewRequest("GET", "http://localhost:8080/clients", nil)
		if err != nil {
			t.Fatal(err)
		}
		getallresp, err := http.DefaultClient.Do(req) //send get request to server
		var clientList [1]Client
		err = json.NewDecoder(getallresp.Body).Decode(&clientList)
		if err != nil {
			t.Error("Unable to decode getClients()'s response JSON")
		}
		defer getallresp.Body.Close()
		if clientList[0].Username != "testuser" {
			t.Error("getClients() returned wrong list of users.")
		}*/

	//next call getClient()
	url := fmt.Sprintf("http://localhost:8080/clients/%d", createdClient.ID)
	req, err = http.NewRequest("GET", url, nil)
	if err != nil {
		t.Fatal(err)
	}
	resp2, err := http.DefaultClient.Do(req) //send get request to server
	var returnedClient Client
	err = json.NewDecoder(resp2.Body).Decode(&returnedClient)
	if err != nil {
		t.Error("Unable to decode getClient()'s response JSON")
	}
	defer resp2.Body.Close()

	//check if returned client was as expected.
	if returnedClient.Username != "testuser" {
		t.Errorf("Unexpected username. Expected: 'testuser', returned: %s", returnedClient.Username)
	}
	if returnedClient.Password != "testpass" {
		t.Errorf("Unexpected username. Expected: 'testpass', returned: %s", returnedClient.Password)
	}
	if returnedClient.Ingredients != "flour, sugar" {
		t.Errorf("Unexpected username. Expected: 'flour, sugar', returned: %s", returnedClient.Ingredients)
	}

	//update that client to have a new username.
	updateJSON := `{"username":"bobsballs","password":"testpass","ingredients":"flour, sugar","favorite_recipes":"{1}"}`
	req, err = http.NewRequest("PUT", url, bytes.NewBufferString(updateJSON))
	if err != nil {
		t.Fatal(err)
	}
	_, err = http.DefaultClient.Do(req) //send PUT request to server
	if err != nil {
		t.Error("updateClient() Error")
	}
	//check if testuser got changed to bobsballs
	req, err = http.NewRequest("GET", url, nil)
	if err != nil {
		t.Fatal(err)
	}
	updatedResp, err := http.DefaultClient.Do(req) //send get request to server
	var returnedClient2 Client
	err = json.NewDecoder(updatedResp.Body).Decode(&returnedClient2)
	if err != nil {
		t.Error("Unable to decode updateClient()'s response JSON")
	}
	defer updatedResp.Body.Close()
	if returnedClient2.Username != "bobsballs" {
		t.Error("username did not get updated after updateClient(). Username: ", returnedClient2.Username)
	}

	//next, we delete that client from db.
	url = fmt.Sprintf("http://localhost:8080/clients/%d", returnedClient.ID)
	req, err = http.NewRequest("DELETE", url, nil)
	if err != nil {
		t.Fatal(err)
	}
	resp3, err := http.DefaultClient.Do(req) //send get request to server
	//check return status code: should be 200
	if resp3.StatusCode != http.StatusOK {
		t.Errorf("Delete client request returned wrong status code: got %v want %v", resp3.StatusCode, http.StatusOK)
	}

	//Need to check if client actually got deleted. Right now if we send a get request to clients/id,
	//sql will panic and shut the server down so we need to deal with that with appropriate error handling.
}
