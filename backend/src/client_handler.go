package main

import (
	"encoding/json"
	"net/http"

	"io"

	"git.ucsc.edu/kho32/reciperetreiver/-/tree/main/backend/src/login"
	"git.ucsc.edu/kho32/reciperetreiver/-/tree/main/backend/src/utils"
	"github.com/gorilla/mux"
)

func getClients(w http.ResponseWriter, r *http.Request) {
	rows, err := client_db.Query("SELECT id, username, password, ingredients, favorite_recipes FROM clients")
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	defer rows.Close()

	clients := []utils.Client{}

	for rows.Next() {
		client := utils.Client{}
		err := rows.Scan(&client.ID, &client.Username, &client.Password, &client.Ingredients, &client.FavoriteRecipes)
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
		clients = append(clients, client)
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(clients)
}

func getClient(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	clientID := params["id"]

	row := client_db.QueryRow("SELECT id, username, password, ingredients, favorite_recipes FROM clients WHERE id = $1", clientID)

	client := utils.Client{}
	err := row.Scan(&client.ID, &client.Username, &client.Password, &client.Ingredients, &client.FavoriteRecipes)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(client)
}

func createClient(w http.ResponseWriter, r *http.Request) {
	var client utils.Client
	err := json.NewDecoder(r.Body).Decode(&client)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	_, err = login.Create_user_helper(client_db, &client)

	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(client)
}

func updateClient(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	clientID := params["id"]

	var client utils.Client
	err := json.NewDecoder(r.Body).Decode(&client)
	if client.VerifyJWT() == false {
		http.Error(w, err.Error(), http.StatusUnauthorized)
		return
	}

	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	query := "UPDATE clients SET username=$1, password=$2, ingredients=$3, favorite_recipes=$4 WHERE id=$5"
	_, err = client_db.Exec(query, client.Username, client.Password, client.Ingredients, client.FavoriteRecipes, clientID)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)
}

func deleteClient(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	clientID := params["id"]

	var client utils.Client
	err := json.NewDecoder(r.Body).Decode(&client)

	if client.VerifyJWT() == false {
		http.Error(w, err.Error(), http.StatusUnauthorized)
		return
	}

	_, err = client_db.Exec("DELETE FROM clients WHERE id = $1", clientID)

	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)
}

func loginClient(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	clientUsername := params["username"]

	var user utils.Client
	err := json.NewDecoder(r.Body).Decode(&user)

	if err != nil {
		panic("error unmarshalling")
	}

	logged_in, token, err := login.Login_helper(utils.Client_db, user, clientUsername)

	if logged_in && err == nil {
		w.Header().Set("Content-Type", "text/plain; charset=utf-8")
		w.WriteHeader(http.StatusOK)
		io.WriteString(w, token)

		//input JWT into db
		_, _ = utils.Client_db.Exec("UPDATE clients SET jwt=$1 WHERE username = $2", token, clientUsername)

	} else {
		w.Header().Set("Content-Type", "text/plain; charset=utf-8")
		w.WriteHeader(http.StatusUnauthorized)
		io.WriteString(w, "Incorrect Username/Password\n")
	}
	return
}
