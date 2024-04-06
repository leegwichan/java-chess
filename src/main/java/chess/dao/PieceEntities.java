package chess.dao;

import chess.domain.ChessGame;
import chess.domain.Team;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.TurnType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PieceEntities {

    private final List<PieceEntity> pieceEntities;

    public PieceEntities(List<PieceEntity> pieceEntities) {
        this.pieceEntities = pieceEntities;
    }

    public static PieceEntities from(ChessGame chessGame) {
        List<PieceEntity> pieceEntities = toPieceEntities(chessGame);
        return new PieceEntities(pieceEntities);
    }

    private static List<PieceEntity> toPieceEntities(ChessGame chessGame) {
        return Position.ALL_POSITIONS.stream()
                .map(position -> findPieceToEntity(chessGame, position))
                .toList();
    }

    private static PieceEntity findPieceToEntity(ChessGame chessGame, Position position) {
        return chessGame.findPieceAt(position)
                .map(piece -> new PieceEntity(position, piece))
                .orElse(PieceEntity.createEmptyPiece(position));
    }

    public ChessGame toBoard(TurnType turnType) {
        Map<Position, Piece> board = pieceEntities.stream()
                .filter(PieceEntity::isExistPiece)
                .collect(Collectors.toMap(
                        PieceEntity::getPosition,
                        PieceEntity::toPiece
                ));
        Team turn = turnType.getTeam();
        return new ChessGame(board, turn);
    }
}
