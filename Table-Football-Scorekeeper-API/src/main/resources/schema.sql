PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS player (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS team (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    player1Id INTEGER NOT NULL,
    player2Id INTEGER,
    FOREIGN KEY (player1Id) REFERENCES player(id),
    FOREIGN KEY (player2Id) REFERENCES player(id)
);

CREATE TABLE IF NOT EXISTS game (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    timestamp TEXT NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    greyId INTEGER NOT NULL,
    blackId INTEGER NOT NULL,
    scoreGrey INTEGER NOT NULL,
    scoreBlack INTEGER NOT NULL,
    FOREIGN KEY (greyId) REFERENCES team(id),
    FOREIGN KEY (blackId) REFERENCES team(id)
);

-- Indexes for faster lookups
CREATE INDEX IF NOT EXISTS idx_team_player1 ON team(player1Id);
CREATE INDEX IF NOT EXISTS idx_team_player2 ON team(player2Id);
CREATE INDEX IF NOT EXISTS idx_game_greyId ON game(greyId);
CREATE INDEX IF NOT EXISTS idx_game_blackId ON game(blackId);