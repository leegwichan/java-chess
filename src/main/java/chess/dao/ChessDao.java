package chess.dao;

import chess.domain.position.Position;
import chess.dto.TurnType;
import java.util.List;

public class ChessDao {

    private final ChessGameRepository chessGameRepository;
    private final PieceRepository pieceRepository;

    public ChessDao(ChessGameRepository chessGameRepository, PieceRepository pieceRepository) {
        this.chessGameRepository = chessGameRepository;
        this.pieceRepository = pieceRepository;
    }

    public boolean isExistSavingGame() {
        return chessGameRepository.isExistGame();
    }

    public TurnType findCurrentTurn() {
        return chessGameRepository.find();
    }

    public PieceEntities findAllPieces() {
        return pieceRepository.findAll();
    }

    public void saveBoard(PieceEntities pieces, TurnType currentTurn) {
        pieceRepository.saveAll(pieces);
        chessGameRepository.update(currentTurn);
    }

    public void saveMoving(PieceEntity piece, Position previous, TurnType currentTurn) {
        PieceEntity emptyPiece = PieceEntity.createEmptyPiece(previous);

        pieceRepository.update(piece);
        pieceRepository.update(emptyPiece);
        chessGameRepository.update(currentTurn);
    }

    public void deleteAll() {
        pieceRepository.deleteAll();
        chessGameRepository.deleteAll();
    }
}
