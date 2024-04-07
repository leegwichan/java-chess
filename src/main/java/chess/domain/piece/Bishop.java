package chess.domain.piece;

import chess.domain.Point;
import chess.domain.Team;
import chess.domain.movement.MovementRule;
import chess.domain.movement.continuous.NorthEastMovement;
import chess.domain.movement.continuous.NorthWestMovement;
import chess.domain.movement.continuous.SouthEastMovement;
import chess.domain.movement.continuous.SouthWestMovement;
import java.util.List;

public final class Bishop extends Piece {

    private static final Point PIECE_POINT = new Point(3.0);
    private static final List<MovementRule> MOVEMENT_RULES = List.of(
            new NorthEastMovement(), new SouthEastMovement(), new NorthWestMovement(), new SouthWestMovement());

    public Bishop(Team team) {
        super(team, MOVEMENT_RULES);
    }


    @Override
    public Point getPoint(boolean isPawnOverlappedInFile) {
        return PIECE_POINT;
    }
}
