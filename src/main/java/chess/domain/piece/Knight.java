package chess.domain.piece;

import chess.domain.Point;
import chess.domain.Team;
import chess.domain.movement.MovementRule;
import chess.domain.movement.discrete.KnightMovement;
import java.util.List;

public final class Knight extends Piece {

    private static final List<MovementRule> MOVEMENT_RULES = List.of(new KnightMovement());
    private static final Point PIECE_POINT = new Point(2.5);

    public Knight(Team team) {
        super(team, MOVEMENT_RULES);
    }

    @Override
    public Point getPoint(boolean isPawnOverlappedIn) {
        return PIECE_POINT;
    }
}
