CREATE DATABASE IF NOT EXISTS chess;

USE chess;

CREATE TABLE chess_game (
    turn VARCHAR(10) CHECK (turn IN ('BLACK', 'WHITE')) NOT NULL,
    PRIMARY KEY (turn)
);

CREATE TABLE pieces (
    board_file VARCHAR(2) CHECK (board_file IN ('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H')) NOT NULL,
    board_rank VARCHAR(2) CHECK (board_rank IN ('1', '2', '3', '4', '5', '6', '7', '8')) NOT NULL,
    type VARCHAR(10) CHECK (type IN ('KING', 'QUEEN', 'ROOK', 'BISHOP', 'KNIGHT', 'PAWN', 'EMPTY')) NOT NULL,
    team VARCHAR(10) CHECK (team IN ('BLACK', 'WHITE', 'EMPTY')) NOT NULL,
    PRIMARY KEY (board_rank, board_file)
);
