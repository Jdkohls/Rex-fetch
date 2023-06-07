package testing

import (
	"database/sql"
	"fmt"
	"testing"

	"git.ucsc.edu/kho32/reciperetreiver/-/tree/main/backend/src/login"
	"git.ucsc.edu/kho32/reciperetreiver/-/tree/main/backend/src/utils"
	_ "github.com/lib/pq"
	"github.com/stretchr/testify/assert"
	_ "github.com/stretchr/testify/assert"
	_ "gopkg.in/DATA-DOG/go-sqlmock.v1"
)

func TestCreateUser(t *testing.T) {

	db, err := sql.Open("postgres", "user=postgres password=postgres dbname=postgres sslmode=disable password=b")
	defer db.Close()
	if err != nil {
		fmt.Println(err)
		panic(1)
	}

	client := utils.Client{Username: "Hello", Password: "pwd"}

	id, err := login.Create_user_helper(db, &client)

	username_row := db.QueryRow("SELECT id,username FROM clients WHERE id  = $1", id)

	assert.NotEqual(t, sql.ErrNoRows, username_row, "row shouldnt be empty")

	var username string
	var id2 int
	username_row.Scan(&id2, &username)

	assert.Equal(t, id, id2, "Not the same")

	fmt.Println(username)
	assert.Equal(t, "Hello", username, "Not the same")
}

func TestLogin_success(t *testing.T) {

	db, err := sql.Open("postgres", "user=postgres password=postgres dbname=postgres sslmode=disable password=b")
	db.Exec("TRUNCATE TABLE clients")

	defer db.Close()
	if err != nil {
		fmt.Println(err)
		panic(1)
	}

	client := utils.Client{Username: "Hello", Password: "pwd"}

	_, err = login.Create_user_helper(db, &client)

	res, _, _ := login.Login_helper(db, client, "Hello")

	assert.True(t, res, "not logged in")
}

func TestLogin_fail_username(t *testing.T) {

	db, err := sql.Open("postgres", "user=postgres password=postgres dbname=postgres sslmode=disable password=b")
	db.Exec("TRUNCATE TABLE clients")

	defer db.Close()
	if err != nil {
		fmt.Println(err)
		panic(1)
	}

	client := utils.Client{Username: "Hello", Password: "pwd"}

	_, err = login.Create_user_helper(db, &client)

	res, _, _ := login.Login_helper(db, client, "hey")

	assert.False(t, res, "not logged in")
}

func TestLogin_fail_pwd(t *testing.T) {

	db, err := sql.Open("postgres", "user=postgres password=postgres dbname=postgres sslmode=disable password=b")
	db.Exec("TRUNCATE TABLE clients")

	defer db.Close()
	if err != nil {
		fmt.Println(err)
		panic(1)
	}

	client := utils.Client{Username: "Hello", Password: "pwd"}

	_, err = login.Create_user_helper(db, &client)

	fake_client := utils.Client{Username: "Hello", Password: "evil"}

	res, _, _ := login.Login_helper(db, fake_client, "Hello")

	assert.False(t, res, "not logged in")
}

func TestLogin_fail_no_user(t *testing.T) {

	db, err := sql.Open("postgres", "user=postgres password=postgres dbname=postgres sslmode=disable password=b")
	db.Exec("TRUNCATE TABLE clients")

	defer db.Close()
	if err != nil {
		fmt.Println(err)
		panic(1)
	}

	client := utils.Client{Username: "hey", Password: "pwd"}

	_, err = login.Create_user_helper(db, &client)

	fake_client := utils.Client{Username: "Hello", Password: "pwd"}

	res, _, _ := login.Login_helper(db, fake_client, "Hello")

	assert.False(t, res, "not logged in")
}
