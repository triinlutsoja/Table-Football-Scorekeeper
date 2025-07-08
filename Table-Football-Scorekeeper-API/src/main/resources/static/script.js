let grey;
let grey2;
let black;
let black2;
let greyId;
let blackId;
let greyScore = 0;
let blackScore = 0;
let startTime;
let timerInterval;

// Handle form submission for adding a player
document.getElementById("add-player-form").addEventListener("submit", async (e) => {
  e.preventDefault(); // Prevent the form from refreshing the page

  const playerName = document.getElementById("new-player-name").value.trim();

  // Display error message if no name is entered
  if (!playerName) {
    displayMessage2("Please enter a player name.", "error");
    return;
  }

  try {
    // Send POST request to add the player
    const response = await fetch("http://localhost:8080/players", {
      method: "POST",
      headers: {"Content-Type": "application/json"},
      body: JSON.stringify({name: playerName}),
    });

    if (!response.ok) {
      throw new Error(`Failed to add player: ${response.status}`);
    }

    const newPlayer = await response.json();

    // Clear the input field and display success message
    document.getElementById("new-player-name").value = "";
    displayMessage2(`Player added successfully: ${newPlayer.name}`, "success");

    // Refresh player list
    fetchPlayers();

  } catch (error) {
    console.error("Error adding player:", error);
    displayMessage2("Failed to add player. Please try again.", "error");
  }
});

// Function to fetch players from the backend
async function fetchPlayers() {
  console.log("Fetching players...");
  try {
    const response = await fetch("http://localhost:8080/players");
    console.log("Response status:", response.status);
    console.log("Response headers:", [...response.headers.entries()]); // Log all headers

    if (!response.ok) {
      throw new Error(`Error fetching players: ${response.status}`);
    }

    const players = await response.json(); // Assuming the backend returns JSON
    console.log("Players fetched successfully:", players); // Debugging: Log players data
    populatePlayerDropdown("grey-player-select", players);
    populatePlayerDropdown("grey-player2-select", players);
    populatePlayerDropdown("black-player-select", players);
    populatePlayerDropdown("black-player2-select", players);
  } catch (error) {
    console.error("Error fetching players:", error.message);
  }
}

// Populate the dropdown list with players
function populatePlayerDropdown(dropdownId, players) {
  const dropdown = document.getElementById(dropdownId);
  dropdown.innerHTML = ""; // Clear any existing options

  // Add a default "Select a player" option
  const defaultOption = document.createElement("option");
  defaultOption.textContent = "Select a player";
  defaultOption.value = "";
  dropdown.appendChild(defaultOption);

  // Add players to the dropdown
  players.forEach((player) => {
    const option = document.createElement("option");
    option.textContent = player.name; // Assuming each player object has a "name" field
    option.value = player.id; // Assuming each player object has an "id" field
    dropdown.appendChild(option);
  });
}

// Validate teams before any backend requests
const validateTeams = () => {
  grey = Number(document.getElementById("grey-player-select").value) || null;
  grey2 = Number(document.getElementById("grey-player2-select").value || null);
  black = Number(document.getElementById("black-player-select").value) || null;
  black2 = Number(document.getElementById("black-player2-select").value) || null;

  if (grey && black && grey === black) {
    alert("A player cannot play against themselves!");
    return false;
  } else if (grey && grey2 && grey === grey2 || black && black2 && black === black2) {
    alert("A player cannot team up with themselves!");
    return false;
  } else if (grey === black || grey === black2 || black === grey2) {
    alert("A player cannot play on both teams at the same time!");
    return false;
  }

  return true;
};

// Start game timer
const startGameTimer = () => {
  startTime = new Date();
  timerInterval = setInterval(() => {
    const elapsed = Math.floor((new Date() - startTime) / 1000);
    const minutes = Math.floor(elapsed / 60);
    const seconds = elapsed % 60;
    document.getElementById("game-timer").textContent = `${minutes}:${seconds < 10 ? "0" : ""}${seconds}`;
  }, 1000);
};

// Stop game timer
const stopGameTimer = () => {
  clearInterval(timerInterval);
};

// Handle scoring
document.addEventListener("keydown", (e) => {
  if (e.key === "ArrowLeft") {
    greyScore = Math.min(8, greyScore + 1);
  } else if (e.key === "ArrowRight") {
    blackScore = Math.min(8, blackScore + 1);
  }
  updateScore();
});

// Update scoreboard
const updateScore = () => {
  document.getElementById("score-grey").textContent = greyScore;
  document.getElementById("score-black").textContent = blackScore;
};

// End game and send results to backend
const endGame = () => {
  stopGameTimer();

    // Prepare the data to send to the backend
  const requestBody = {
    greyId: greyId,
    blackId: blackId,
    scoreGrey: greyScore,
    scoreBlack: blackScore,
  };

  // Send data to the backend
  fetch("http://localhost:8080/games", {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify(requestBody),
  })
      .then((res) => {
        if (!res.ok) {
          throw new Error(`Error saving game: ${res.status}`);
        }
        return res.json();
      })
      .then((data) => {
      displayMessage(
      `Game Over! Grey: ${greyScore}, Black: ${blackScore}. Game saved successfully!`, "success"
      );
      resetGame();  // Reset scores and dropdowns
      })
      .catch((err) => {
        console.error("Error saving game:", err);
        displayMessage("Failed to save game. Please try again.", "error");
      });
};

// Function to display a message under the "Add" button
const displayMessage2 = (message, type) => {
  const messageElement = document.getElementById("add-player-message");

  if (type === "success") {
    messageElement.style.color = "green";
  } else if (type === "error") {
    messageElement.style.color = "red";
  }

  messageElement.textContent = message;
};

// Function to display a message under the "Start game" button
const displayMessage3 = (message, type) => {
  const messageElement = document.getElementById("start-game-error-message");

  if (type === "success") {
    messageElement.style.color = "green";
  } else if (type === "info") {
    messageElement.style.color = "grey";
  } else if (type === "error") {
    messageElement.style.color = "red";
  }

  messageElement.textContent = message;
};

// Function to display a message under the "End Game" button
const displayMessage = (message, type) => {
  const messageElement = document.getElementById("end-game-message");

  if (type === "success") {
    messageElement.style.color = "green";
  } else if (type === "error") {
    messageElement.style.color = "red";
  }

  messageElement.textContent = message;
};

// Function to reset the game
const resetGame = () => {
  greyScore = 0;
  blackScore = 0;
  updateScore();

  // Reset dropdown selections
  document.getElementById("grey-player-select").value = "";
  document.getElementById("black-player-select").value = "";
};

// Function to get existing teams ID
async function findTeamIdByPlayers(player1Id, player2Id) {
  try {

    console.log("These are IDs that findTeamIdByPlayers received: ", player1Id, player2Id); // Debug
    const response = await fetch("http://localhost:8080/teams");
    const allTeams = await response.json();
    console.log("These are all the teams received from backend: ", allTeams);  // Debug

    for (const team of allTeams) {
      const ids = [team.player1Id, team.player2Id];
      console.log("The current loop round is this: ", ids);
      if (ids.includes(player1Id) && ids.includes(player2Id)) {
        console.log("Yay! Found the team you were looking for: ", team.id);  // Debug
        return team.id;
      } else {
        console.log("No match between ", ids, " and ", player1Id, player2Id);
      }
    }
  } catch (error) {
    console.error("Error fetching teams:", error);
  }
  console.log("Team not found among existing teams.");
  return null;  
}

// Initialize when page loads
document.addEventListener("DOMContentLoaded", () => {
  fetchPlayers();
});

document.addEventListener('DOMContentLoaded', function () {
  console.log("JavaScript is running!");

  // Fetch the data from the backend API
  fetch('http://localhost:8080/stats')
      .then(response => {
        console.log("Fetching stats...");
        return response.json();
      })
      .then(data => {
        console.log(data); // Log the data received

        // Clear existing table rows
        const statsBody = document.getElementById('stats-body');
        statsBody.innerHTML = '';

        // Loop through each player and display their victories
        data.forEach(player => {
          console.log("Adding player to table:", player); // Log each player object

          const row = document.createElement('tr'); // Create a new table row

          // Create and append the player name cell
          const playerNameCell = document.createElement('td');
          playerNameCell.textContent = player.playerName;
          console.log("Player name cell:", player.playerName); // Log the player name
          row.appendChild(playerNameCell);

          // Create and append the victories cell
          const victoriesCell = document.createElement('td');
          victoriesCell.textContent = `${player.victoryCount}`; // Force victories to render as a string
          console.log("Victories cell:", player.victoryCount); // Log the victories count
          row.appendChild(victoriesCell);

          // Append the row to the table body
          statsBody.appendChild(row);
        });
      })
      .catch(error => {
        console.error('Error fetching stats:', error);
      });

  // Add event listener to start the game
  document.getElementById("start-game-button").addEventListener("click", async () => {
    
    if (validateTeams()) {
    
      // Send POST request to add the grey team
      try {
      const responseGrey = await fetch("http://localhost:8080/teams", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({player1Id: grey, player2Id: grey2}),
      });

      if (!responseGrey.ok) {
        throw new Error(`Failed to add grey team: ${responseGrey.status}`);
      }

      const newTeamGrey = await responseGrey.json();
      greyId = newTeamGrey.id;

      } catch (error) {
        console.warn("Grey team already exists or failed. Trying to fetch existing team...");
        greyId = await findTeamIdByPlayers(grey, grey2);
        
      }
      
      // Send POST request to add the black team
      try {
      const responseBlack = await fetch("http://localhost:8080/teams", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({player1Id: black, player2Id: black2}),
      });

      if (!responseBlack.ok) {
        throw new Error(`Failed to add black team: ${responseBlack.status}`);
      }

      const newTeamBlack = await responseBlack.json();
      blackId = newTeamBlack.id;

      } catch (error) {
        console.warn("Black team already exists or failed. Trying to fetch existing team...");
        blackId = await findTeamIdByPlayers(black, black2);
      }
      startGameTimer();  // Starts, if no errors caught with the catch blocks
    }
  });
  


  // Add event listener to end the game
  document.getElementById("end-game").addEventListener("click", () => {
      endGame(); // End the game when the button is clicked
    });
});