package chess;

import chess.dao.ChessDao;
import chess.dao.PieceEntities;
import chess.dao.PieceEntity;
import chess.domain.ChessGame;
import chess.domain.ChessGameFactory;
import chess.domain.Point;
import chess.domain.Team;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.BoardDto;
import chess.dto.ProgressStatus;
import chess.dto.StatusDto;
import chess.dto.TurnType;
import java.util.Map;

public class ChessService {

    private ChessGame chessGame;
    private final ChessDao chessDao;

    public ChessService(ChessDao chessDao) {
        this.chessDao = chessDao;
    }

    public void initializeBoard() {
        validateInitState();
        if (chessDao.isExistSavingGame()) {
            PieceEntities pieceEntities = chessDao.findAllPieces();
            TurnType turn = chessDao.findCurrentTurn();
            chessGame = pieceEntities.toBoard(turn);
            return;
        }
        chessGame = ChessGameFactory.createInitBoard();
        saveBoard();
    }

    private void validateInitState() {
        if (chessGame != null) {
            throw new IllegalStateException("보드가 이미 초기화 되었습니다.");
        }
    }

    private void saveBoard() {
        PieceEntities entities = PieceEntities.from(chessGame);
        TurnType turn = TurnType.from(chessGame.findCurrentTurn());
        chessDao.saveBoard(entities, turn);
    }

    public void executeMove(Position start, Position end) {
        chessGame.move(start, end);
        ProgressStatus status = chessGame.findStatus();

        if (status.isContinue()) {
            saveMoving(start, end);
            return;
        }
        chessDao.deleteAll();
    }

    private void saveMoving(Position start, Position end) {
        Piece piece = chessGame.findPieceAt(end)
                .orElseThrow(() -> new IllegalArgumentException("끝 위치에 말이 없습니다."));
        PieceEntity movedPiece = new PieceEntity(end, piece);
        TurnType turnType = TurnType.from(chessGame.findCurrentTurn());
        chessDao.saveMoving(movedPiece, start, turnType);
    }

    public StatusDto executeStatus() {
        Map<Team, Point> status = chessGame.calculateTotalPoints();
        return StatusDto.from(status);
    }

    public BoardDto findTotalBoard() {
        return BoardDto.from(chessGame);
    }

    public Team findCurrentTurn() {
        return chessGame.findCurrentTurn();
    }

    public ProgressStatus findStatus() {
        return chessGame.findStatus();
    }
}
