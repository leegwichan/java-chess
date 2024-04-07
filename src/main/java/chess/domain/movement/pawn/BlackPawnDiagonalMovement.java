package chess.domain.movement.pawn;

public final class BlackPawnDiagonalMovement extends PawnDiagonalMovement {

    private static final int TO_SOUTH_ONE_BLOCK = -1;
    private static final int ONE_BLOCK = 1;

    @Override
    protected boolean isMovable(int rankDifference, int fileDifference) {
        return (rankDifference == TO_SOUTH_ONE_BLOCK && Math.abs(fileDifference) == ONE_BLOCK);
    }
}
