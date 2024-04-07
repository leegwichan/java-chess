package chess.domain.piece;

import chess.domain.Point;
import chess.domain.Team;
import chess.domain.movement.MovementRule;
import chess.domain.movement.discrete.KingMovement;
import java.util.List;

public final class King extends Piece {

    private static final List<MovementRule> MOVEMENT_RULES = List.of(new KingMovement());
    private static final Point PIECE_POINT = Point.ZERO;

    public King(Team team) {
        super(team, MOVEMENT_RULES);
    }

    @Override
    public Point getPoint(boolean isPawnOverlappedInFile) {
        return PIECE_POINT;
    }
}
