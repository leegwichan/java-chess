package chess.dto;

import chess.domain.Team;
import java.util.Objects;

public enum ProgressStatus {

    WHITE_WIN(false),
    BLACK_WIN(false),
    PROGRESS(true),
    END_GAME(false),
    ;

    private final boolean isContinue;

    ProgressStatus(boolean isContinue) {
        this.isContinue = isContinue;
    }

    public static ProgressStatus winGame(Team team) {
        Objects.requireNonNull(team);

        if (team.isBlack()) {
            return BLACK_WIN;
        }
        return WHITE_WIN;
    }

    public static ProgressStatus progressGame() {
        return PROGRESS;
    }

    public static ProgressStatus endGame() {
        return END_GAME;
    }

    public boolean isContinue() {
        return isContinue;
    }

    public boolean isInputEndCommand() {
        return this == END_GAME;
    }
}
