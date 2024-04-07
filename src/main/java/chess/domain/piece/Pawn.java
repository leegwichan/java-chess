package chess.domain.piece;

import chess.domain.Point;
import chess.domain.Team;
import chess.domain.movement.MovementRule;
import chess.domain.movement.pawn.BlackPawnDefaultMovement;
import chess.domain.movement.pawn.BlackPawnDiagonalMovement;
import chess.domain.movement.pawn.BlackPawnFirstMovement;
import chess.domain.movement.pawn.WhitePawnDefaultMovement;
import chess.domain.movement.pawn.WhitePawnDiagonalMovement;
import chess.domain.movement.pawn.WhitePawnFirstMovement;
import java.util.List;

public final class Pawn extends Piece {

    private static final List<MovementRule> BLACK_MOVEMENT_RULES = List.of(
            new BlackPawnFirstMovement(), new BlackPawnDefaultMovement(), new BlackPawnDiagonalMovement());
    private static final List<MovementRule> WHITE_MOVEMENT_RULES = List.of(
            new WhitePawnFirstMovement(), new WhitePawnDefaultMovement(), new WhitePawnDiagonalMovement());
    private static final Point PIECE_BASIC_POINT = new Point(1.0);
    private static final Point PIECE_OVERLAPPED_POINT = new Point(0.5);


    public Pawn(Team team) {
        super(team, findMovementRule(team));
    }

    private static List<MovementRule> findMovementRule(Team team) {
        if (team.isBlack()) {
            return BLACK_MOVEMENT_RULES;
        }
        return WHITE_MOVEMENT_RULES;
    }

    @Override
    public Point getPoint(boolean isPawnOverlappedInFile) {
        if (isPawnOverlappedInFile) {
            return PIECE_OVERLAPPED_POINT;
        }
        return PIECE_BASIC_POINT;
    }
}
