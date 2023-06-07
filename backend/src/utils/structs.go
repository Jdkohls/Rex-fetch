package utils

type ClientRecipe struct {
	ID       int    `json:"id"`
	ClientID int    `json:"client_id"`
	RecipeID int    `json:"recipe_id"`
	JWT      string `json:"JWT,omitempty"`
}

type Client struct {
	ID              int      `json:"id,omitempty"`
	Username        string   `json:"username,omitempty"`
	Password        string   `json:"password,omitempty"`
	Ingredients     string   `json:"ingredients,omitempty"`
	FavoriteRecipes []string `json:"favorite_recipes,omitempty"`
	JWT             string   `json:"JWT,omitempty"`
}

type Recipe struct {
	ID           int      `json:"id"`
	Name         string   `json:"name"`
	Ingredients  []string `json:"ingredients"`
	PrepTime     string   `json:"prep_time"`
	CookTime     string   `json:"cook_time"`
	TotalTime    string   `json:"total_time"`
	ImageURL     string   `json:"image_url"`
	Instructions []string `json:"instructions"`
	Notes        []string `json:"notes"`
}
