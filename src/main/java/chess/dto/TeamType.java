package chess.dto;

import chess.domain.Team;
import java.util.Arrays;

public enum TeamType {

    BLACK(Team.BLACK),
    WHITE(Team.WHITE),
    EMPTY(null),
    ;

    private final Team team;

    TeamType(Team team) {
        this.team = team;
    }

    public static TeamType from(Team team) {
        return Arrays.stream(TeamType.values())
                .filter(type -> type.team == team)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 타입이 없습니다."));
    }

    public static TeamType getEmptyType() {
        return EMPTY;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isBlackTeam() {
        return this == BLACK;
    }
}
