package chess.domain;

import chess.domain.piece.Piece;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.dto.ProgressStatus;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChessGame {

    private final Board board;
    private GameStatus status;

    public ChessGame(Map<Position, Piece> board, Team currentTurn) {
        this.board = new Board(board);
        this.status = new GameStatus(currentTurn);
    }

    public ChessGame(Map<Position, Piece> board) {
        this(board, Team.WHITE);
    }

    public Optional<Piece> find(Position position) {
        return board.findPieceAt(position);
    }

    public ProgressStatus move(Position start, Position end) {
        validate(start, end);
        boolean isKingCaptured = board.isOpponentKingExist(end, status.getCurrentTurn());
        board.move(start, end);
        if (isKingCaptured) {
            status.endGame();
            return findFinishStatus(status.getCurrentTurn());
        }
        status.nextTurn();
        return ProgressStatus.PROGRESS;
    }

    public ProgressStatus findFinishStatus(Team currentTurn) {
        if (currentTurn == Team.BLACK) {
            return ProgressStatus.BLACK_WIN;
        }
        return ProgressStatus.WHITE_WIN;
    }

    private void validate(Position start, Position end) {
        board.validate(start, end);

        Team pieceTeam = board.findPieceAt(start)
                .map(Piece::getTeam)
                .orElseThrow();
        status.validateTurn(pieceTeam);
    }

    public Map<Team, Point> calculateTotalPoints() {
        return board.calculateTotalPoints();
    }

    public Team findCurrentTurn() {
        return status.getCurrentTurn();
    }
}
