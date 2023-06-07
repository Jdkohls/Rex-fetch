# Server Design

A documentation of server and backend design in general.

## Server

The HTTP server will be using a mux (HTTP server multiplexer) to account for scalability in the future.

https://github.com/gorilla/mux

## Logging

Uber-go/zap should be used for all logging, as it utilizes structured logging.

https://github.com/uber-go/zap

## Testing

Testing should use the testify library; Testify has an asser package, a require package, and a mock package.

https://github.com/stretchr/testify

Assert and require assert, or require, the contiditions to be met.

The mock package allows mock objects to be stored in memory when writing tests.

Mockery can also be used for mocking

https://github.com/vektra/mockery

# Formats

## Incoming recipe
Parsed JSON from the front end for a recipe will be in the `incoming_ingr` struct, which will have the following structure:

```go
 type incoming_ingr struct {
 	Ingredients   []string `json:"ingredients"` 
	PrepTime      int      `json:"prep time"`
	TotalMinutes  int      `json:"total minutes"`
	RecipeOptions int      `json:"recipe options"`
	Substitutes   int      `json:"substitutes"`
 }
```

## Outgoing recipe
JSON to the front end for a recipe will be utilizing the `outgoing_recipe` struct, which will have the following structure

```go
 type outgoing_recipe struct {
	Ingredients []string `json:"ingredients"`
	PrepTime    int      `json:"prep time"`
	TotalTime   int      `json:"total time"`
	Steps       []string `json:"steps"`
 }
 ```

 ## Incoming user information

 Parsed JSON from the front end for user information will 

 ```go
 
 ```