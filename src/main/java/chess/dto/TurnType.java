package chess.dto;

import chess.domain.Team;
import java.util.Arrays;

public enum TurnType {

    BLACK(Team.BLACK),
    WHITE(Team.WHITE),
    ;

    private final Team team;

    TurnType(Team team) {
        this.team = team;
    }

    public static TurnType from(Team team) {
        return Arrays.stream(TurnType.values())
                .filter(teamType -> teamType.team == team)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 턴 타입이 없습니다."));
    }

    public Team getTeam() {
        return team;
    }
}
