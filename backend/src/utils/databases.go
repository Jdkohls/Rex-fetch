package utils

import "database/sql"

var Client_db *sql.DB
var Recipe_db *sql.DB
var Client_recipe_db *sql.DB

//TODO: DEAL WITH PANICS BETTER

func OpenDatabases() (*sql.DB, *sql.DB, *sql.DB) {
	Client_db, err := sql.Open("postgres", "postgres://postgres:postgres@localhost:5432/client_db?sslmode=disable")
	if err != nil {
		panic("Failed opening DB")
	}
	Recipe_db, err = sql.Open("postgres", "postgres://postgres:postgres@localhost:5432/recipe_db?sslmode=disable")
	if err != nil {
		panic("Failed opening DB")
	}
	Client_recipe_db, err = sql.Open("postgres", "postgres://postgres:postgres@localhost:5432/client_recipe_db?sslmode=disable")
	if err != nil {
		panic("Failed opening DB")
	}
	return Client_db, Recipe_db, Client_recipe_db
}

func Open_client_db() (*sql.DB, error) {
	Client_db, err := sql.Open("postgres", "postgres://postgres:postgres@localhost:5432/client_db?sslmode=disable")
	if err != nil {
		panic("Failed opening DB")
	}
	return Client_db, err
}

func CloseDatabases() {
	Client_db.Close()
	Recipe_db.Close()
	Client_recipe_db.Close()
}
