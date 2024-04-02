package chess.domain;

public class GameStatus {

    private Team currentTurn;
    private boolean isProgress;

    public GameStatus(Team currentTurn) {
        this.currentTurn = currentTurn;
        this.isProgress = true;
    }

    public void validateTurn(Team currentTeam) {
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

    public Team getCurrentTurn() {
        return currentTurn;
    }

    public boolean isProgressGame() {
        return isProgress;
    }
}
