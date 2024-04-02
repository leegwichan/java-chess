package chess.domain;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Board {

    private final Map<Position, Piece> board;

    public Board(Map<Position, Piece> board) {
        this.board = new HashMap<>(board);
    }

    public Optional<Piece> findPieceAt(Position position) {
        Piece piece = board.get(position);
        return Optional.ofNullable(piece);
    }

    public void move(Position start, Position end) {
        validate(start, end);
        Piece piece = board.get(start);
        List<Position> path = piece.findPath(start, end, isExistEnemyAtEnd(start, end));

        validateEmpty(path);
        movePiece(start, end);
    }

    private void validate(Position start, Position end) {
        if (isNotExistPiece(start)) {
            throw new IllegalArgumentException("해당 위치에 말이 없습니다.");
        }
        if (isExistSameTeam(start, end)) {
            throw new IllegalArgumentException("도착 지점에 같은 팀 말이 있어 이동이 불가능합니다.");
        }
    }

    private boolean isNotExistPiece(Position position) {
        return !isExistPiece(position);
    }

    private boolean isExistSameTeam(Position start, Position end) {
        Piece pieceAtStart = findPieceOrElseThrow(start);
        return findPieceAt(end).map(piece -> piece.isSameTeam(pieceAtStart))
                .orElse(false);
    }

    private boolean isExistEnemyAtEnd(Position start, Position end) {
        Piece pieceAtStart = findPieceOrElseThrow(start);
        return findPieceAt(end).map(piece -> !piece.isSameTeam(pieceAtStart))
                .orElse(false);
    }

    private void movePiece(Position start, Position end) {
        Piece movingPiece = findPieceAt(start).orElseThrow();

        board.remove(start);
        board.put(end, movingPiece);
    }

    private void validateEmpty(List<Position> path) {
        if (isBlocked(path)) {
            throw new IllegalArgumentException("다른 말이 있어 이동 불가능합니다.");
        }
    }

    private boolean isBlocked(List<Position> path) {
        return path.stream()
                .limit(path.size() - 1)
                .anyMatch(this::isExistPiece);
    }

    private Piece findPieceOrElseThrow(Position position) {
        return findPieceAt(position)
                .orElseThrow(() -> new IllegalArgumentException("해당 위치에 말이 없습니다."));
    }

    private boolean isExistPiece(Position position) {
        return board.containsKey(position);
    }
}
