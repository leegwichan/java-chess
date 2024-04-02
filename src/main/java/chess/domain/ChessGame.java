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

    private final Map<Position, Piece> board;
    private Team turn;

    public ChessGame(Map<Position, Piece> board, Team turn) {
        this.board = new HashMap<>(board);
        this.turn = turn;
    }

    public ChessGame(Map<Position, Piece> board) {
        this(board, Team.WHITE);
    }

    public Optional<Piece> find(Position position) {
        Piece piece = board.get(position);
        return Optional.ofNullable(piece);
    }

    public ProgressStatus move(Position start, Position end) {
        validate(start, end);
        Piece piece = board.get(start);
        List<Position> path = piece.findPath(start, end, isExistEnemy(end));

        validateEmpty(path);
        return movePiece(start, end);
    }

    private void validate(Position start, Position end) {
        if (isNotExistPiece(start)) {
            throw new IllegalArgumentException("해당 위치에 말이 없습니다.");
        }
        if (isDifferentTurn(start)) {
            throw new IllegalArgumentException("해당 팀의 차례가 아닙니다.");
        }
        if (isExistSameTeam(end)) {
            throw new IllegalArgumentException("도착 지점에 같은 팀 말이 있어 이동이 불가능합니다.");
        }
    }

    private boolean isDifferentTurn(Position position) {
        return find(position).map(piece -> !piece.isSameTeam(turn))
                .orElseThrow(() -> new IllegalArgumentException("해당 위치에 말이 없습니다."));
    }

    private boolean isExistSameTeam(Position position) {
        return find(position).map(piece -> piece.isSameTeam(turn))
                .orElse(false);
    }

    private boolean isExistEnemy(Position position) {
        return find(position).map(piece -> !piece.isSameTeam(turn))
                .orElse(false);
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

    private ProgressStatus movePiece(Position start, Position end) {
        Piece movingPiece = find(start).orElseThrow();
        ProgressStatus status = findStatus(end);

        board.remove(start);
        board.put(end, movingPiece);
        turn = turn.nextTurn();

        return status;
    }

    private ProgressStatus findStatus(Position end) {
        boolean isKingCaptured = find(end).map(Piece::isKing)
                .orElse(false);

        if (!isKingCaptured) {
            return ProgressStatus.PROGRESS;
        }
        if (turn.isBlack()) {
            return ProgressStatus.BLACK_WIN;
        }
        return ProgressStatus.WHITE_WIN;
    }

    private boolean isExistPiece(Position position) {
        return board.containsKey(position);
    }

    private boolean isNotExistPiece(Position position) {
        return !isExistPiece(position);
    }

    public Map<Team, Point> calculateTotalPoints() {
        return Arrays.stream(Team.values())
                .collect(Collectors.toMap(
                        team -> team,
                        this::calculateTotalPoints
                ));
    }

    private Point calculateTotalPoints(Team team) {
        return Arrays.stream(File.values())
                .map(file -> calculatePoints(team, file))
                .reduce(Point.ZERO, Point::add);
    }

    private Point calculatePoints(Team team, File file) {
        List<Piece> sameFilePieces = Arrays.stream(Rank.values())
                .map(rank -> new Position(file, rank))
                .map(this::find)
                .flatMap(Optional::stream)
                .filter(piece -> piece.isSameTeam(team))
                .toList();
        return calculatePoints(sameFilePieces);
    }

    private Point calculatePoints(List<Piece> teamPieces) {
        boolean isPawnOverlappedInFilePawn = isOverlappedPawn(teamPieces);
        return teamPieces.stream()
                .map(piece -> piece.getPoint(isPawnOverlappedInFilePawn))
                .reduce(Point.ZERO, Point::add);
    }

    private boolean isOverlappedPawn(List<Piece> pieces) {
        return pieces.stream()
                .filter(Piece::isPawn)
                .count() >= 2;
    }

    public Team findCurrentTurn() {
        return turn;
    }
}
