package chess.domain;

import chess.dto.ProgressStatus;

public class GameStatus {

    private Team currentTurn;
    private boolean isProgress;

    public GameStatus(Team currentTurn) {
        this.currentTurn = currentTurn;
        this.isProgress = true;
    }

    public void validate(Team currentTeam) {
        if (!isProgressGame()) {
            throw new IllegalStateException("게임이 이미 종료되었습니다.");
        }
        if (isDifferentTurn(currentTeam)) {
            throw new IllegalArgumentException("해당 팀의 차례가 아닙니다.");
        }
    }

    private boolean isDifferentTurn(Team currentTeam) {
        return currentTurn != currentTeam;
    }

    public void nextTurn() {
        currentTurn = currentTurn.nextTurn();
    }

    public void endGame() {
        isProgress = false;
    }

    public ProgressStatus findStatus() {
        if (isProgressGame()) {
            return ProgressStatus.PROGRESS;
        }
        if (getCurrentTurn().isBlack()) {
            return ProgressStatus.BLACK_WIN;
        }
        return ProgressStatus.WHITE_WIN;
    }

    public Team getCurrentTurn() {
        return currentTurn;
    }

    public boolean isProgressGame() {
        return isProgress;
    }
}
