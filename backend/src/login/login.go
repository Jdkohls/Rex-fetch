package login

import (
	"crypto/rand"
	"crypto/sha256"
	"crypto/subtle"
	"database/sql"
	"time"

	"git.ucsc.edu/kho32/reciperetreiver/-/tree/main/backend/src/utils"
	"github.com/golang-jwt/jwt"
)

func SecureCompare(given string, actual string) bool {
	if subtle.ConstantTimeEq(int32(len(given)), int32(len(actual))) == 1 {
		return subtle.ConstantTimeCompare([]byte(given), []byte(actual)) == 1
	} else {
		/* Securely compare actual to itself to keep constant time, but always return false */
		return subtle.ConstantTimeCompare([]byte(actual), []byte(actual)) == 1 && false
	}
}

func Login_helper(db *sql.DB, user utils.Client, username string) (bool, string, error) {
	var usernameOK bool = true

	if SecureCompare(user.Username, username) { //Constant time string comparison
		usernameOK = false
	}

	var pwd_hash []byte
	var userID string
	var salt []byte
	var secret []byte

	row := db.QueryRow("SELECT password, id, salt, secret FROM clients WHERE username = $1", username)
	err := row.Scan(&pwd_hash, &userID, &salt, &secret)
	if err != nil && err != sql.ErrNoRows {
		panic("We don't have error logging yet.")
		return false, "", err
	}

	hash := sha256.New()
	hash.Write([]byte(user.Password))
	hash.Write(salt)
	pwd_inputted := hash.Sum(nil)

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, jwt.MapClaims{
		"ID":           userID,
		"Timeout Time": time.Now().Unix() + 10000,
		"Username":     username,
		"admin":        false,
	})

	tokenString, err := token.SignedString(secret)

	//compare outputs of pwd_hash and pwd_inputted in constant time.
	if (subtle.ConstantTimeCompare(pwd_hash, pwd_inputted) == 1) && (usernameOK) {
		return true, tokenString, err
	} else {
		return false, "", err
	}
}

func Create_user_helper(db *sql.DB, client *utils.Client) (int, error) {
	salt := make([]byte, 64)
	rand.Read(salt)

	secret := make([]byte, 64)
	rand.Read(secret)

	hash := sha256.New()
	hash.Write([]byte(client.Password))
	hash.Write(salt)
	pwd_hash := hash.Sum(nil)

	query := "INSERT INTO clients(username, password, salt, secret) VALUES ($1, $2, $3, $4, $5, $6) RETURNING id" //Need to add allergens
	row := db.QueryRow(query, client.Username, pwd_hash, client, salt, secret)
	err := row.Scan(&client.ID)

	return client.ID, err

}
