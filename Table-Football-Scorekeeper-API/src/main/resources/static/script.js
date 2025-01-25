let greyScore = 0;
let blackScore = 0;
let startTime;
let timerInterval;

// Handle form submission for adding a player
document.getElementById("add-player-form").addEventListener("submit", async (e) => {
  e.preventDefault(); // Prevent the form from refreshing the page
  const playerName = document.getElementById("new-player-name").value.trim();

  if (!playerName) {
    alert("Please enter a player name.");
    return;
  }

  try {
    const response = await fetch("http://localhost:8080/players", {
      method: "POST",
      headers: {"Content-Type": "application/json"},
      body: JSON.stringify({name: playerName}),
    });

    if (!response.ok) {
      throw new Error(`Failed to add player: ${response.status}`);
    }

    const newPlayer = await response.json();
    alert(`Player added successfully: ${newPlayer.name}`);
    document.getElementById("new-player-name").value = ""; // Clear the input field

    // Refresh player list
    fetchPlayers();
  } catch (error) {
    console.error(error.message);
  }
});

// Fetch players from the backend
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
    populatePlayerDropdown("black-player-select", players);
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

// Validate player selection
const validatePlayers = () => {
  const grey = document.getElementById("grey-player-select").value;
  const black = document.getElementById("black-player-select").value;

  if (grey && black && grey === black) {
    alert("A player cannot play against themselves!");
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
  alert(`Game Over! Grey: ${greyScore}, Black: ${blackScore}`);
  const grey = document.getElementById("grey-player-select").value;
  const black = document.getElementById("black-player-select").value;

  if (!grey || !black) {
    alert("Select players first!");
    return;
  }

  if (!validatePlayers()) {
    return; // Don't proceed if validation fails
  }

  const requestBody = {
    greyId: grey,
    blackId: black,
    scoreGrey: greyScore,
    scoreBlack: blackScore,
  };

  console.log("Sending game data to the backend:", requestBody); // Debug request body

  fetch("http://localhost:8080/games", {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify(requestBody),
  })
      .then((res) => {
        console.log("Response status:", res.status); // Log response status
        console.log("Response headers:", [...res.headers.entries()]); // Log all headers
        if (!res.ok) {
          throw new Error(`Error saving game: ${res.status}`);
        }
        return res.json();
      })
      .then((data) => console.log("Game saved successfully:", data)) // Log success response
      .catch((err) => console.error("Error saving game:", err)); // Log error
};

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
  document.getElementById("start-game-button").addEventListener("click", () => {
    if (validatePlayers()) {
      startGameTimer();  // Start the timer when the "Start Game" button is clicked
    }
  });

  // Add event listener to end the game
  document.getElementById("end-game").addEventListener("click", () => {
      endGame(); // End the game when the button is clicked
    });
});