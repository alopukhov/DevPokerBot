CREATE TABLE users (
  id INT NOT NULL PRIMARY KEY,
  nickname VARCHAR(40),
  reg_time TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE decks (
  id SMALLINT NOT NULL PRIMARY KEY,
  alias VARCHAR(100) NOT NULL,
  content JSON NOT NULL,
  CONSTRAINT unique_deck_alias UNIQUE(alias)
);

CREATE TABLE games (
  id BIGINT NOT NULL PRIMARY KEY,
  description VARCHAR(100),
  deck_id SMALLINT NOT NULL,
  owner_id INT NOT NULL,
  started_at TIMESTAMP WITH TIME ZONE NOT NULL,
  revealed_at TIMESTAMP WITH TIME ZONE,
  CONSTRAINT fk_games_deck_id FOREIGN KEY(deck_id) REFERENCES decks(id),
  CONSTRAINT fk_games_owner_id FOREIGN KEY(owner_id) REFERENCES users(id)
);
CREATE SEQUENCE games_seq START WITH 0 INCREMENT BY 50;

CREATE TABLE votes (
  id BIGINT NOT NULL PRIMARY KEY,
  game_id BIGINT NOT NULL,
  user_id INT NOT NULL,
  choice SMALLINT NOT NULL,
  voted_at TIMESTAMP WITH TIME ZONE NOT NULL,
  CONSTRAINT fk_votes_game_id FOREIGN KEY(game_id) REFERENCES games(id),
  CONSTRAINT fk_votes_user_id FOREIGN KEY(user_id) REFERENCES users(id),
  CONSTRAINT unique_user_vote UNIQUE(game_id,user_id)
);
CREATE SEQUENCE votes_seq START WITH 0 INCREMENT BY 50;

CREATE INDEX idx_votes_by_game ON votes(game_id, user_id);