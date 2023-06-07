package utils

type incoming_message interface {
	verifyJWT() bool
}

func (user Client) VerifyJWT() bool {
	var userJWT string
	row := Client_db.QueryRow("SELECT JWT FROM clients WHERE id = $1", user.ID)
	err := row.Scan(&userJWT)
	if err != nil {
		return false
	}

	return user.JWT == userJWT
}

func (user ClientRecipe) VerifyJWT() bool {
	var userJWT string
	row := Client_db.QueryRow("SELECT JWT FROM clients WHERE id = $1", user.ID)
	err := row.Scan(&userJWT)
	if err != nil {
		return false
	}

	return user.JWT == userJWT
}
