let greyScore = 0;
let blackScore = 0;
let startTime;
let timerInterval;

// Fetch players from the backend
async function fetchPlayers() {
  try {
    const response = await fetch("http://localhost:8080/players"); // Correct backend endpoint
    if (!response.ok) {
      throw new Error(`Error fetching players: ${response.status}`);
    }
    const players = await response.json(); // Assuming the backend returns JSON
    populatePlayerDropdown("grey-player-select", players);
    populatePlayerDropdown("black-player-select", players);
  } catch (error) {
    console.error(error.message);
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

  if (greyScore === 8 || blackScore === 8) {
    endGame();
  }
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

  // Call validation function
  if (!validatePlayers()) {
    return; // Don't proceed if validation fails
  }

  fetch("http://localhost:8080/games", { // Correct endpoint for saving game
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      greyId: grey,
      blackId: black,
      scoreGrey: greyScore,
      scoreBlack: blackScore,
    }),
  })
    .then((res) => {
      if (!res.ok) {
        throw new Error(`Error saving game: ${res.status}`);
      }
      return res.json();
    })
    .then((data) => console.log("Game saved:", data))
    .catch((err) => console.error("Error saving game:", err));
};

// Initialize when page loads
document.addEventListener("DOMContentLoaded", () => {
  fetchPlayers();
});