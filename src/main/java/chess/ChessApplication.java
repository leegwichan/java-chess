package chess;

import chess.dao.ChessDao;
import chess.dao.ChessGameRepository;
import chess.dao.ConnectionManager;
import chess.dao.MysqlChessGameRepository;
import chess.dao.MysqlPieceRepository;
import chess.dao.PieceRepository;
import chess.view.InputView;
import chess.view.OutputView;

public class ChessApplication {

    public static void main(String[] args) {
        ConnectionManager connectionManager = new ConnectionManager();
        ChessService chessService = createChessService(connectionManager);
        OutputView outputView = new OutputView();
        InputView inputView = new InputView();
        ChessController chessController = new ChessController(inputView, outputView, chessService);

        try {
            chessController.run();
        } finally {
            connectionManager.closeConnection();
        }
    }

    private static ChessService createChessService(ConnectionManager connectionManager) {
        ChessGameRepository chessGameRepository = new MysqlChessGameRepository(connectionManager);
        PieceRepository pieceRepository = new MysqlPieceRepository(connectionManager);
        ChessDao chessDao = new ChessDao(chessGameRepository, pieceRepository);
        return new ChessService(chessDao);
    }
}
