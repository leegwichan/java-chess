package chess;

import chess.dao.ChessDao;
import chess.dao.PieceEntities;
import chess.dao.PieceEntity;
import chess.domain.ChessGame;
import chess.domain.ChessGameFactory;
import chess.domain.Point;
import chess.domain.Team;
import chess.domain.position.Position;
import chess.dto.PieceDto;
import chess.dto.ProgressStatus;
import chess.dto.StatusDto;
import chess.dto.TurnType;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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
        PieceEntity movedPiece = findPieceToEntity(end);
        TurnType turnType = TurnType.from(chessGame.findCurrentTurn());
        chessDao.saveMoving(movedPiece, start, turnType);
    }

    public StatusDto executeStatus() {
        Map<Team, Point> status = chessGame.calculateTotalPoints();
        return StatusDto.from(status);
    }

    public Map<Position, PieceDto> findTotalBoard() {
        return Position.ALL_POSITIONS.stream()
                .map(this::toResultEntry)
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    private Entry<Position, PieceDto> toResultEntry(Position position) {
        PieceDto pieceDto = chessGame.findPieceAt(position)
                .map(PieceDto::from)
                .orElse(PieceDto.createEmptyPiece());
        return Map.entry(position, pieceDto);
    }

    private PieceEntity findPieceToEntity(Position position) {
        return chessGame.findPieceAt(position)
                .map(piece -> new PieceEntity(position, piece))
                .orElse(PieceEntity.createEmptyPiece(position));
    }


    public Team findCurrentTurn() {
        return chessGame.findCurrentTurn();
    }

    public ProgressStatus findStatus() {
        return chessGame.findStatus();
    }
}
