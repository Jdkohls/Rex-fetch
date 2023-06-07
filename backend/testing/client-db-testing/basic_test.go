package test

import (
	"database/sql"
	_ "github.com/lib/pq"
	"testing"
)

// this test creates a db named client_db, inserts a client, retrieves it, then deletes the client_db
func TestDatabase(t *testing.T) {
	// open database connection
	db, err := sql.Open("postgres", "user=postgres password=postgres dbname=postgres sslmode=disable")
	if err != nil {
		panic(err)
	}
	defer db.Close()

	//create a client database
	_, err = db.Exec("CREATE DATABASE client_db;")
	if err != nil {
		t.Fatal(err)
	}
	//connect to that db
	clientdb, err := sql.Open("postgres", "user=postgres password=postgres dbname=client_db sslmode=disable")
	if err != nil {
		panic(err)
	}

	//In client_db, create a table, insert a client, remove a client, drop the table.
	var str = "CREATE TABLE IF NOT EXISTS clients (id SERIAL PRIMARY KEY, username VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, ingredients TEXT, favorite_recipes INTEGER[],	CONSTRAINT unique_username UNIQUE (username));"
	_, err = clientdb.Exec(str)
	if err != nil {
		t.Fatal(err)
	}

	str = "INSERT INTO clients (username, password, ingredients, favorite_recipes) VALUES ('Kim', 'Ho', '[salt, pepper]', '{1, 2}');"
	_, err = clientdb.Exec(str)
	if err != nil {
		t.Fatal(err)
	}

	row := clientdb.QueryRow("SELECT username, password, ingredients FROM clients WHERE id=1")
	var ruser string
	var rpass string
	var ring string
	err = row.Scan(&ruser, &rpass, &ring)
	if err != nil || ruser != "Kim" || rpass != "Ho" || ring != "[salt, pepper]" {
		t.Error("Retrieved client not as expected.")
	}

	//close client database, then remove it
	clientdb.Close()
	_, err = db.Exec("DROP DATABASE client_db;")
	if err != nil {
		t.Fatal(err)
	}
}
