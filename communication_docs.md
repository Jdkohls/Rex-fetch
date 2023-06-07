# Communication documentation

For now, the HTTP protocol will be used for communication between the front and back end.

## Methods:

The primary methods used should be PUT and POST, and GET.

Both of these should contain necessary information within the body of the HTTP request.

### PUT

PUT is an HTTP method used to update data- this should be utilized for updating the user information.

PUT is used over POST because PUT is idempotent- if a change of user information is requested in the same way several times, then the change should always be the same.

### POST

POST is an HTTP method used for retreiving data- this should be used for retreiving recipies.

POST is being used over GET for two primary reasons:

- GET does not normally allow a message body; a message body is most convenient for our usages

- GET is a safe method; it does not change the server. POST is neither safe nor idempotent, it may change something in the server, and several calls of it may change the server in different ways.

### GET

GET is an HTTP method used to retrieve data- this should be used for retrieving user data from the server.

Get should be used only to retrieve information; such as what is currently in the user's fridge or settings.

## Endpoints

The main endpoints will be /recipe, /user/[identifier]/[action].

### /recipe

This will be used in the POST request for requesting the recipies, so it will be formatted like:

> POST localhost:3333/recipe

where the body of the message is the JSON formatted requirements for the recipe.

### /user/ID/action

This will be used when changing, adding, or removing information about the user from the server.

Similiarly to the /recipe command, it will use a JSON formatted request in the body.

User ID's will be, for now, 6 digit hexadecimal numbers: 

`[a-f0-9]{6}` 

in regex.

## Message Bodies

### /recipe

The JSON format for the message body will look something like:
```json
{
 "ingredients": ["banana", "sugar"],
 "allergens": ["nuts", "soy"],
 "prep time": 6,
 "total minutes": 15,
 "recipe options": 3,
 "missing ingredients" : 2, 
 "substitutes": 0
}
```

And the return value, if it's a OK request, will return a message body looking like:

```json
{
 "recipe name": "Banana chicken",
 "ingredients":["1 lbs chicken", "2 cups sugar","3 whole banananas","5 half onions"],
 "prep time": 3,
 "total time": 60,
 "steps":["cut the banananas in half","Dice the onions roughly","What does a rough dice mean??"]
}
```

### /recipe/UserID

The JSON format for the message body will look something like:
```json
{
 "ingredients": ["banana", "sugar"],
 "allergens": ["nuts", "soy"],
 "prep time": 6,
 "total minutes": 15,
 "recipe options": 3,
 "missing ingredients" : 2, 
 "substitutes": 0,
 "JWT" : "auiwhifjawoaibwnoufbgaw"
}
```

And the return value, if it's a OK request, will return a message body looking like:

```json
{
 "recipe name": "Banana chicken",
 "ingredients":["1 lbs chicken", "2 cups sugar","3 whole banananas","5 half onions"],
 "prep time": 3,
 "total time": 60,
 "steps":["cut the banananas in half","Dice the onions roughly","What does a rough dice mean??"]
}
```

### /user/Username/create

The JSON format for the message to backend will look like:
```json
{
    "Username":"I am user",
    "Password":"hello!",
}
```

The functionality of this is just to create the user- as such, the User ID should be unique.


### /user/{Username}/login

The JSON format for the message body will look like:
```json
{
    "Username":"I am user again, muahah",
    "Password":"This is my super secure password, i totally trust it!"
}
```

The response will just be a 200 HTTP response if true, with a message string of a valid JWT. If false, then you'll get an HTTP error header response- a 401 error.

The purpose of the JWT is to prove the user is logged in, and will be used in all future responses. Additionally, the JWT contains the user ID within it.

### /user/ID/user_update

The JSON format for the message body will look something like:

```json
{
    "Allergens":["bananas","fresh fruit","love"],
    "Fridge":["Bananas","canned fruit","seething hatred"],
    "Favorites":["A","B"],
    "Grocery":["Bananas","curiousity"]
    "rm Allergens":["nuts"]
    "rm Fridge":["Love"]
    "rm Favorites":["C"]
    "rm Grocery":["seething hatred"]
    "JWT": "abvubawbnmawopaisnbwa"
}
```

This is only used for updating the user information, by adding additional fields or removing them, this is not used for retreiving information.

The `rm` fields are used for removing the item from the user data.

Note: This should be idempotent, so `PUT` should be a valid HTTP method.

### /user/ID/get

The JSON format for the message body will look something like:

```json
{
    "Allergens":1,
    "Fridge":0,
    "Favorites":0,
    "Grocery":0,
    "JWT": "abvubawbnmawopaisnbwa"
}
```

This request should use front end requests of information, and should not change the data on the backend. As such, it should utilize the POST HTTP method- this is because GET traditionally does not have a message body.

The response will be 
```json
{
    "Allergens":["hello","how are you"]
}
```

Where the only fields that exist are the ones where the value is `1`.

