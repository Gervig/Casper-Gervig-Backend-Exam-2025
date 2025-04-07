# Casper Gervig Backend Exam 2025
CPH-business email : cph-cg201@cphbusiness.dk

# Purpose
Backend system for an e-commerce platform offering ski instructor services. The platform should manage ski instructor details, and the ski lessons that they lead. This system will include managing instructor profiles, their lesson schedules, and client bookings.

# Choices
### IDs
* I chose to use Long for IDs instead of integers. For the purpose of this exercise integers would have been fine, but I'm used to Long.
### DAO classes
* The task said that the DAO classes should take DTOs and return DTOs, I chose to use entities here and mapped the entities to DTOs in the controller. Given more time I would have done it the other way.
### Location (longitude & latitude)
* In the SkiLesson class we were asked to add a Location that contained longitude and latitude. I chose to simply add those to the SkiLesson directly. Given more time I would have made a one-to-one relation between Location and SkiLesson.
### addInstructorToSkiLesson method
* The addInstructorToSkiLesson method was supposed to return void, but I let it return a SkiLesson, so I can test that the updated lesson actually gets updated in my http request.
# Setup

### Config properties

To run this project first create a resources folder in the src/main/java directory. In this folder create a "config.properties" file with the following lines:

* DB_NAME=insert your own database name here
* DB_USERNAME=postgres
* DB_PASSWORD=postgres
* SECRET_KEY="insert your own secret key here, ideally 30 signs or more"
* ISSUER="insert your name"
* TOKEN_EXPIRE_TIME=1800000

### First run

* When running the program for the first time, ensure that a Postgres database is created using the values from the config.properties file.
* There will also be a few pops ups asking to enable annotations, say yes to these.

# App Endpoint Table

| Endpoint                                              | Method | Description                                              |
|:------------------------------------------------------|:-------|:---------------------------------------------------------|
| api/skilessons                                        | GET    | Get all ski lessons.                                     |
| api/skilessons/{id}                                   | GET    | Get a ski lesson by its id.                              |
| api/skilessons                                        | POST   | Create a new ski lesson. Add instructor later.           |
| api/skilessons/{id}                                   | PUT    | Update information about a ski lesson.                   |
| api/skilessons/{id}                                   | DELETE | Delete a ski lesson.                                     |
| api/skilessons/{lessonId}/instructors/{instructorId}	 | PUT    | Add an existing instructor to an existing ski lesson.    |
| api/skilessons/populate	                              | POST   | Populate the database with ski lessons and instructors.  |

# HTTP responses

**api/skilessons/populate (POST)**
- Returns an empty response body and a status code 200. This will populate the database with 4 Ski lessons and 3 instructors.

**api/skilessons (GET)**
- Returns a 4 Ski lessons, first 2 has instructor with ID 1, 3rd has instructor with ID 2 and 4th has no instructor (null)

**api/skilessons/1 (GET)**
- Returns the Ski lesson with ID 1 (Whistler Blackcomb, Canada).
  
**api/skilessons/42 (GET)**
- Returns with a message saying the lesson could not be found and a status code 404, because there is no ski lesson with ID 42.

**api/skilessons (POST)**
- Returns a newly posted ski lesson (Cortina d'Ampezzo, Italy).

**api/skilessons/1 (PUT)**
- Returns with a newly updated ski lesson with ID 1, that now has an updated price of 780.00

**api/skilessons/42 (PUT)**
- Returns with a message saying the lesson could not be found and a status code 404, because there is no ski lesson with ID 42.

**api/skilessons/1 (DELTE)**
- Returns with an empty body and a status code of 204, if run again it will give a message saying no lesson with that ID and a status code of 404. Because that lesson was just deleted.

**api/skilessons/42 (DELTE)**
- Returns a message saying no lesson with that ID and a status code of 404. Because there is no lesson with ID 42.

**api/skilessons/2/instructors/3 (PUT)**
- Returns a newly updated lesson with a new instructor with ID 3

**api/skilessons/2/instructors/42 (PUT)**
- Return with a message saying instructor lesson could not be found and a status code 404. Because there is no instructor with ID 42.

# Theoretical questions
**num** : question 
* answer

# Security Endpoint Table

| Endpoint                         | Method | Secured      | Description                     |
|:---------------------------------|:-------|:------------:|:--------------------------------|
| api/auth/register                | POST   | ❌          | Create a new user               |
| api/auth/login                   | POST   | ❌          | Auth a user, return JWT token   |
| api/secured/demo                 | GET    | ✅          | Tests a users token after login |

❌ = Not secured

✅ = User secured

🔒 = Admin secured