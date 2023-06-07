-- 5 sample clients
INSERT INTO clients (username, password, ingredients, favorite_recipes) VALUES ('Kim', 'Ho', '["salt", "pepper"]', '[1, 2]');
INSERT INTO clients (username, password, ingredients, favorite_recipes) VALUES ('Jackson', 'Kohls', '["onion", "garlic"]', '[3]');
INSERT INTO clients (username, password, ingredients, favorite_recipes) VALUES ('Venky', 'Nandapurkar', '["paprika", "cumin"]', '[4, 5, 6]');
INSERT INTO clients (username, password, ingredients, favorite_recipes) VALUES ('Prithvi', 'Arunshankar', '["ginger", "turmeric"]', '[1, 5]');
INSERT INTO clients (username, password, ingredients, favorite_recipes) VALUES ('Kevin', 'Yosifov', '["basil", "thyme"]', '[2, 4]');

--- 6 sample recipes
INSERT INTO recipes (name, ingredients, prep_time, cook_time, total_time, image_url, instructions, notes) VALUES ('Spaghetti Bolognese', 'spaghetti, tomato sauce, ground beef, onion, garlic, olive oil, salt, pepper', '00:15:00', '00:30:00', '00:45:00', 'https://www.example.com/spaghetti-bolognese.jpg', '1. Heat olive oil in a large skillet over medium heat. 2. Add chopped onion and garlic, and cook until softened. 3. Add ground beef and cook until browned. 4. Stir in tomato sauce and let simmer for 10-15 minutes. 5. Cook spaghetti according to package instructions. 6. Serve the spaghetti with the sauce on top. Enjoy!', 'This recipe is great for a quick and easy dinner.');
INSERT INTO recipes (name, ingredients, prep_time, cook_time, total_time, image_url, instructions, notes) VALUES ('Chicken Alfredo', 'fettuccine, chicken breast, heavy cream, butter, garlic, parmesan cheese, salt, pepper', '00:10:00', '00:20:00', '00:30:00', 'https://www.example.com/chicken-alfredo.jpg', '1. Cook fettuccine according to package instructions. 2. Cut chicken breast into bite-sized pieces. 3. Heat butter in a large skillet over medium-high heat. 4. Add chopped garlic and cook until fragrant. 5. Add chicken and cook until browned on all sides. 6. Pour in heavy cream and stir to combine. 7. Reduce heat to low and let simmer for a few minutes. 8. Stir in grated parmesan cheese until melted and smooth. 9. Season with salt and pepper to taste. 10. Serve the sauce over the cooked fettuccine. Enjoy!', 'This recipe is creamy and delicious.');
INSERT INTO recipes (name, ingredients, prep_time, cook_time, total_time, image_url, instructions, notes) VALUES ('Beef Stroganoff', 'beef tenderloin, egg noodles, sour cream, beef broth, onion, garlic, olive oil, salt, pepper', '00:15:00', '00:25:00', '00:40:00', 'https://www.example.com/beef-stroganoff.jpg', '1. Cook egg noodles according to package instructions. 2. Cut beef tenderloin into thin strips. 3. Heat olive oil in a large skillet over medium-high heat. 4. Add chopped onion and garlic, and cook until softened. 5. Add beef and cook until browned on all sides. 6. Pour in beef broth and let simmer for a few minutes. 7. Stir in sour cream until melted and smooth. 8. Season with salt and pepper to taste. 9. Serve the beef stroganoff over the cooked egg noodles. Enjoy!', 'This recipe is hearty and satisfying.');
INSERT INTO recipes (name, ingredients, prep_time, cook_time, total_time, image_url, instructions, notes) VALUES ('Honey Mustard Chicken', 'chicken breasts, dijon mustard, honey, garlic, paprika, thyme, salt, pepper', '00:10:00', '00:25:00', '00:35:00', 'https://www.example.com/honey-mustard-chicken.jpg', '1. Preheat the oven to 400°F (200°C). 2. In a small bowl, whisk together dijon mustard, honey, minced garlic, paprika, thyme, salt, and pepper. 3. Place chicken breasts in a baking dish and pour the mustard mixture over the chicken, coating well. 4. Bake for 25 minutes or until the chicken is cooked through. 5. Serve with your choice of sides. Enjoy!', 'This recipe is sweet and savory.');
INSERT INTO recipes (name, ingredients, prep_time, cook_time, total_time, image_url, instructions, notes) VALUES ('Tomato Soup', 'tomatoes, chicken broth, onion, garlic, butter, heavy cream, salt, pepper', '00:15:00', '00:30:00', '00:45:00', 'https://www.example.com/tomato-soup.jpg', '1. Melt butter in a large pot over medium heat. 2. Add chopped onion and garlic, and cook until softened. 3. Add chopped tomatoes and chicken broth, and bring to a simmer. 4. Let the soup simmer for 20-25 minutes. 5. Using an immersion blender, blend the soup until smooth. 6. Stir in heavy cream and season with salt and pepper to taste. 7. Serve with croutons or your choice of toppings. Enjoy!', 'This recipe is comforting and perfect for a chilly day.');
INSERT INTO recipes (name, ingredients, prep_time, cook_time, total_time, image_url, instructions, notes) VALUES ('Vegetable Fried Rice', 'rice, carrots, peas, corn, onion, garlic, soy sauce, sesame oil', '00:10:00', '00:20:00', '00:30:00', 'https://www.example.com/fried-rice.jpg', '1. Cook rice according to package instructions. 2. Heat sesame oil in a large skillet or wok over medium-high heat. 3. Add chopped onion and garlic, and cook until softened. 4. Add chopped carrots, peas, and corn, and cook until the vegetables are tender. 5. Add cooked rice to the skillet and stir to combine. 6. Pour in soy sauce and stir to coat everything evenly. 7. Serve hot. Enjoy!', 'This recipe is a quick and easy vegetarian meal.');
