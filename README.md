Mobile App: Ingredient Tracker
Introduction
Ingredient Tracker is a mobile app that helps users keep track of ingredients in their fridge, suggests recipes based on those ingredients, and helps them keep track of what groceries they need to buy. The app is built using Kotlin for the frontend and Go lang for the backend, with two databases - one for user login and another for recipes. The app also integrates with a third-party API to fetch additional recipe suggestions.

Features
The Ingredient Tracker app has the following features:

Inventory Management: Users can add and remove ingredients from their fridge and keep track of the expiry date of each ingredient. This helps users avoid wastage and plan meals based on the ingredients they have.

Recipe Suggestions: Based on the ingredients added to the inventory, the app suggests recipes that the user can make using those ingredients. The app integrates with a third-party API to fetch additional recipe suggestions.

Grocery List: Users can add ingredients they need to buy to a grocery list within the app. This helps users stay organized and ensure they have all the ingredients they need for the recipes they plan to make.

Technologies Used
The following technologies were used to build the Ingredient Tracker app:

Kotlin for the frontend
Go lang for the backend
SQLite database for user login
MySQL database for recipe management
Spoonacular API for recipe suggestions
Installation
To install the Ingredient Tracker app, follow these steps:

Clone the repository from GitHub: git clone https://github.com/Jdkohls/Rex-fetch.git
Install the necessary dependencies for the frontend and backend using the respective package managers.
Configure the database settings for the backend in the backend/go/config.go file.
Run the backend using the command go run backend/go/main.go.
Run the frontend using the respective command provided by the package manager.
Usage
To use the Ingredient Tracker app, follow these steps:

Register an account or log in with an existing account.
Add ingredients to your fridge using the "Add Ingredient" button.
View suggested recipes based on the ingredients you have in your fridge.
Add ingredients you need to buy to the grocery list.
Check off items on your grocery list as you purchase them.
Contributors
The following team members contributed to the development of the Ingredient Tracker app:

Jackson Kohls (Backend Developer)
Venky Nandapurkar (Frontend Developer)
Kim Ho (Database Administrator)
Kevin Martin Yosifov (UI/UX Designer)
Prithvi Arunshankar

Conclusion
The Ingredient Tracker app is a powerful tool that helps users keep track of their ingredients, plan meals, and stay organized when grocery shopping. With its intuitive interface and powerful features, the app is sure to become a valuable addition to any kitchen.
