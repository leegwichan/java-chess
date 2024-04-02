package chess;

import chess.domain.Team;
import chess.domain.position.Position;
import chess.dto.PieceDto;
import chess.dto.ProgressStatus;
import chess.view.GameCommand;
import chess.view.InputView;
import chess.view.OutputView;
import java.util.Map;

public class ChessController {

    private static final InputView INPUT_VIEW = new InputView();
    private static final OutputView OUTPUT_VIEW = new OutputView();

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    public void run() {
        try {
            startGame();
            play();
        } catch (IllegalArgumentException exception) {
            OUTPUT_VIEW.printExceptionMessage(exception);
        }
    }

    private void startGame() {
        OUTPUT_VIEW.printStartGame();
        GameCommand command = INPUT_VIEW.readCommand();
        if (command.isStart()) {
            initializeBoard();
            showCurrentTeam();
            showBoard();
            return;
        }
        throw new IllegalArgumentException("아직 게임을 시작하지 않았습니다.");
    }

    private void initializeBoard() {
        chessService.inititalizeBoard();
    }

    private void showCurrentTeam() {
        Team turn = chessService.findCurrentTurn();
        OUTPUT_VIEW.printCurrentTurn(turn);
    }

    private void play() {
        ProgressStatus status;
        do {
            status = processTurn();
        } while (status.isContinue());
        showResult(status);
    }

    private ProgressStatus processTurn() {
        GameCommand command = INPUT_VIEW.readCommand();
        if (command.isStart()) {
            throw new IllegalArgumentException("이미 게임을 시작했습니다.");
        }
        if (command.isMove()) {
            return executeMove();
        }
        if (command.isStatus()) {
            return executeStatus();
        }
        return ProgressStatus.END_GAME;
    }

    private ProgressStatus executeMove() {
        Position start = INPUT_VIEW.readPosition();
        Position end = INPUT_VIEW.readPosition();
        chessService.moveTo(start, end);

        showBoard();
        return chessService.findStatus();
    }

    private ProgressStatus executeStatus() {
        Map<Team, Double> statusDto = chessService.calculatePiecePoints();
        OUTPUT_VIEW.printStatus(statusDto);
        return ProgressStatus.PROGRESS;
    }

    private void showResult(ProgressStatus status) {
        if (status.isInputEndCommand()) {
            return;
        }
        OUTPUT_VIEW.printWinnerMessage(status);
    }

    private void showBoard() {
        Map<Position, PieceDto> boardDto = chessService.findTotalBoard();
        OUTPUT_VIEW.printBoard(boardDto);
    }
}
