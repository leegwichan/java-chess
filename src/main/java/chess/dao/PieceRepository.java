package chess.dao;

import java.util.List;

public interface PieceRepository {

    PieceEntities findAll();

    PieceEntities saveAll(PieceEntities pieces);

    void update(PieceEntity piece);

    void deleteAll();
}
