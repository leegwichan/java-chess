package chess.dto;

import chess.domain.Point;
import chess.domain.Team;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class StatusDto {

    private final Map<Team, Double> status;

    private StatusDto(Map<Team, Double> status) {
        this.status = status;
    }

    public static StatusDto from(Map<Team, Point> statusResult) {
        Map<Team, Double> status = statusResult.entrySet().stream()
                .collect(Collectors.toMap(
                        Entry::getKey,
                        entry -> entry.getValue().toDouble()
                ));

        return new StatusDto(status);
    }

    public double getScore(Team team) {
        return status.get(team);
    }
}
