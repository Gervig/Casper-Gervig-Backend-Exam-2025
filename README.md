# Casper Gervig Backend Exam 2025
CPH-business email : cph-cg201@cphbusiness.dk

# Purpose


# Setup

### Config properties

To run this project first create a resources folder in the src/main/java directory. In this folder create a "config.properties" file with the following lines:

* DB_NAME=insert your own database
* DB_USERNAME=postgres
* DB_PASSWORD=postgres
* SECRET_KEY="insert your own secret key here, ideally 30 signs or more"
* ISSUER="insert your name"
* TOKEN_EXPIRE_TIME=1800000

### First run

* When running the program for the first time, ensure that a Postgres database is created using the values from the config.properties file.
* There will also be a few pops ups asking to enable annotations, say yes to these.

# App Endpoint Table

| Endpoints | Method | Description |
|:----------|:-------|:------------|
| api/      |        |             |

# Theoretical questions
**num** : question 
* answer

# Security Endpoint Table

| Endpoints                         | Method | Secured      | Description                     |
|:----------------------------------|:-------|:------------:|:--------------------------------|
| api/auth/register                 | POST   | ❌          | Create a new user               |
| api/auth/login                    | POST   | ❌          | Auth a user, return JWT token   |
| api/secured/demo                  | GET    | ✅          | Tests a users token after login |

❌ = Not secured

✅ = User secured

🔒 = Admin secured