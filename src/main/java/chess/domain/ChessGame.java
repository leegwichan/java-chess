package chess.domain;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.ProgressStatus;
import java.util.Map;
import java.util.Optional;

public class ChessGame {

    private final Board board;
    private final GameStatus status;

    public ChessGame(Map<Position, Piece> board, Team currentTurn) {
        this.board = new Board(board);
        this.status = new GameStatus(currentTurn);
    }

    public ChessGame(Map<Position, Piece> board) {
        this(board, Team.WHITE);
    }

    public Optional<Piece> findPieceAt(Position position) {
        return board.findPieceAt(position);
    }

    public void move(Position start, Position end) {
        validate(start, end);
        boolean isKingCaptured = board.isOpponentKingExist(end, status.getCurrentTurn());
        board.move(start, end);
        if (isKingCaptured) {
            status.endGame();
            return;
        }
        status.nextTurn();
    }

    private void validate(Position start, Position end) {
        board.validate(start, end);

        Team pieceTeam = board.findPieceAt(start)
                .map(Piece::getTeam)
                .orElseThrow();
        status.validate(pieceTeam);
    }

    public Map<Team, Point> calculateTotalPoints() {
        return board.calculateTotalPoints();
    }

    public Team findCurrentTurn() {
        return status.getCurrentTurn();
    }

    public ProgressStatus findStatus() {
        return status.findStatus();
    }
}
