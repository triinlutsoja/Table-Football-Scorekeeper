# Table-Football-Scorekeeper API

A simple REST API for managing players, teams, and games in a table football scorekeeping system.
Built with Java, Maven, and SQLite.

## 🚀 Features
- Manage Players (add, update, delete, list)
- Manage Teams (singles & doubles)
- Manage Games (track scores & winners)
- Built-in validation & error handling 
- Lightweight persistence with SQLite

## 🛠️ Technologies
- Java 17+
- Maven (build & dependency management)
- SQLite (lightweight embedded database)
- JUnit 5 + Mockito (unit testing)
- Docker (containerized deployment)

## 📦 Getting started
- Install Docker (if not already installed)
- Pull the image: 
```bash
docker pull triinlutsoja/football-api:latest
```
- Run the container:
```bash
docker run -p 8080:8080 triinlutsoja/football-api:latest
```
- (Optional) To persist data, you can mount a local folder for the SQLite database:
```bash
docker run -p 8080:8080 -e DB_PATH=/app/data/table_football_db.db -v /path/on/host:/app/data triinlutsoja/football-api:latest
```
⚠️ Replace /path/on/host with the full path on your computer where you want the database stored. The folder will 
contain your *.db file and preserve data between runs.
- Stop the container: 
```bash
docker stop <container_id_or_name>
```
⚠️ Replace <container_id_or_name> with the container name or id.

## 📖 Usage

The API runs by default on http://localhost:8080. You can interact with it using tools like Postman, curl, or the 
frontend app.

### Example Endpoints

#### Players

**POST /players**
- Request Body (JSON):
```json
{
    "name": "Jaanus"
}
```
- Response: `201 Created`
- Returns the created player.
- Error: `400 Bad Request` if input is invalid.

**GET /players/{id}**
- Response: `200 OK`
- Returns a player with the matching id.
- Error: `404 Not Found` if player is not found.

**GET /players**
- Response: `200 OK`
- Returns a list of all players.

**PUT /players/{id}**
- Request Body (JSON):
```json
{
    "name": "Jaanus"
}
```
- Response: `200 OK`
- Returns the updated player.
- Error: `400 Bad Request` if input is invalid.
- Error: `404 Not Found` if player is not found.

**DELETE /players/{id}**
- Response: `204 No Content`
- Error: `404 Not Found` if the player is not found.

**GET /players/by-name**
- Request Params: `?name=John Smith`
- Response: `200 OK`
- Returns a list  of all players where the name matches.


#### Teams

**POST /teams**
- Request Body (JSON):
```json
{
    "player1Id": 1, 
    "player2Id": 2
}
```
- Response: `201 Created`
- Returns the created team.
- Error: `400 Bad Request` if input is invalid.

**GET /teams/{id}**
- Response: `200 OK`
- Returns a team with the matching id.
- Error: `404 Not Found` if team is not found.

**GET /teams**
- Response: `200 OK`
- Returns a list of all teams.

**PUT /teams/{id}**
- Request Body (JSON):
```json
{
  "player1Id": 1,
  "player2Id": 2
}
```
- Response: `200 OK`
- Returns the updated team.
- Error: `400 Bad Request` if input is invalid.
- Error: `404 Not Found` if team is not found.

**DELETE /teams/{id}**
- Response: `204 No Content`
- Error: `404 Not Found` if the team is not found.


#### Games

**POST /games**
- Request Body (JSON):
```json
{
  "greyId": 2,
  "blackId": 9,
  "scoreGrey": 8,
  "scoreBlack": 7
}
```
- Response: `201 Created`
- Returns the created game.
- Error: `400 Bad Request` if input is invalid.

**GET /games/{id}**
- Response: `200 OK`
- Returns a game with the matching id.
- Error: `404 Not Found` if game is not found.

**GET /games**
- Response: `200 OK`
- Returns a list of all games.

**PUT /games/{id}**
- Request Body (JSON):
```json
{
  "greyId": 4,
  "blackId": 3,
  "scoreGrey": 3,
  "scoreBlack": 8,
  "timestamp": "2025-12-25T11:39:42"
}
```
- Response: `200 OK`
- Returns the updated game.
- Error: `400 Bad Request` if input is invalid.
- Error: `404 Not Found` if game is not found.

**DELETE /games/{id}**
- Response: `204 No Content`
- Error: `404 Not Found` if the game is not found.


#### Statistics

**GET /stats**
- Response: `200 OK`
- Returns a list of all players and the total number of their victories in a descending order.


## Frontend
A simple web frontend is included for interacting with the API: http://localhost:8080/index.html .

