package chess.view;

import chess.domain.Team;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.dto.BoardDto;
import chess.dto.PieceDto;
import chess.dto.PieceType;
import chess.dto.ProgressStatus;
import chess.dto.StatusDto;
import java.util.List;
import java.util.Map;

public class OutputView {

    private static final List<Rank> RANK_ORDER = List.of(
            Rank.EIGHT, Rank.SEVEN, Rank.SIX, Rank.FIVE, Rank.FOUR, Rank.THREE, Rank.TWO, Rank.ONE);
    private static final List<File> FILE_ORDER = List.of(
            File.A, File.B, File.C, File.D, File.E, File.F, File.G, File.H);
    private static final List<Team> TEAM_ORDER = List.of(Team.WHITE, Team.BLACK);
    private static final Map<PieceType, String> PIECE_DISPLAY = Map.of(
            PieceType.KING, "K", PieceType.QUEEN, "Q", PieceType.KNIGHT, "N", PieceType.BISHOP, "B",
            PieceType.ROOK, "R", PieceType.PAWN, "P", PieceType.EMPTY, ".");
    private static final String ERROR_PREFIX = "[ERROR] ";

    public void printStartGame() {
        System.out.println("""
                > 체스 게임을 시작합니다.
                > 게임 시작 : start
                > 게임 종료 : end
                > 게임 이동 : move source위치 target위치 - 예. move b2 b3
                > 팀 별 기물 점수 조회 : status
                """);
    }

    public void printCurrentTurn(Team turn) {
        if (turn.isBlack()) {
            System.out.println("현재 검은 팀 차례입니다.");
            return;
        }
        System.out.println("현재 흰 팀 차례입니다.");
    }

    public void printBoard(BoardDto board) {
        System.out.println();
        for (Rank rank : RANK_ORDER) {
            printBoardOneLine(board, rank);
        }
    }

    private void printBoardOneLine(BoardDto board, Rank rank) {
        for (File file : FILE_ORDER) {
            PieceDto piece = board.getPieceDto(new Position(file, rank));
            printPiece(piece);
        }
        System.out.println();
    }

    private void printPiece(PieceDto piece) {
        if (piece.isBlack()) {
            printBlackPiece(piece.pieceType());
            return;
        }
        printWhitePiece(piece.pieceType());
    }

    private void printBlackPiece(PieceType type) {
        String display = PIECE_DISPLAY.get(type);
        System.out.print(display.toUpperCase());
    }

    private void printWhitePiece(PieceType type) {
        String display = PIECE_DISPLAY.get(type);
        System.out.print(display.toLowerCase());
    }

    public void printStatus(StatusDto status) {
        System.out.println();
        TEAM_ORDER.forEach(team -> printStatus(team, status.getScore(team)));
    }

    private void printStatus(Team team, double score) {
        if (team.isBlack()) {
            System.out.printf("검정 팀 : %.1f%n", score);
            return;
        }
        System.out.printf("하양 팀 : %.1f%n", score);
    }

    public void printWinnerMessage(ProgressStatus status) {
        if (status.isContinue()) {
            throw new IllegalArgumentException("우승자가 결정되지 않았습니다.");
        }

        if (status == ProgressStatus.BLACK_WIN) {
            System.out.println("검정 팀이 승리했습니다.");
            return;
        }
        System.out.println("하양 팀이 승리했습니다.");
    }

    public void printExceptionMessage(Exception exception) {
        System.out.println(ERROR_PREFIX + exception.getMessage());
    }
}
