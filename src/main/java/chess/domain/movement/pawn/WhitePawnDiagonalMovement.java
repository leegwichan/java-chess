package chess.domain.movement.pawn;

public final class WhitePawnDiagonalMovement extends PawnDiagonalMovement {

    private static final int TO_NORTH_ONE_BLOCK = 1;
    private static final int ONE_BLOCK = 1;

    @Override
    protected boolean isMovable(int rankDifference, int fileDifference) {
        return (rankDifference == TO_NORTH_ONE_BLOCK && Math.abs(fileDifference) == ONE_BLOCK);
    }
}
