package chess.dao;

import chess.dto.TurnType;

public interface ChessGameRepository {

    boolean isExistGame();

    TurnType find();

    void update(TurnType turn);

    void deleteAll();
}
