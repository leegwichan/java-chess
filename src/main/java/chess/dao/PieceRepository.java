package chess.dao;

public interface PieceRepository {

    PieceEntities findAll();

    PieceEntities saveAll(PieceEntities pieces);

    void update(PieceEntity piece);

    void deleteAll();
}
