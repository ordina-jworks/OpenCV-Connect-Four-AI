import org.opencv.core.Mat;

public class ConnectFourSolverImpl implements ConnectFourSolver {

    int bestMove = -1;
    private Board oldBoard;

    private ConnectFourVision connectFourVision;

    public ConnectFourSolverImpl() {
        connectFourVision = new ConnectFourVision();
    }

    @Override
    public Board getBoard(byte[] data, DisplayUtil ui) {
        return connectFourVision.getBoard(data, ui);
    }

    @Override
    public Board getBoard(Mat originalBoardImage) throws VisionException {
        return connectFourVision.getBoard(originalBoardImage);
    }

    @Override
    public Board getBoard(Mat originalBoardImage, DisplayUtil ui) throws VisionException {
        return connectFourVision.getBoard(originalBoardImage, ui);
    }

    @Override
    public int getBestMove(Board board) {
        if(oldBoard != null && oldBoard.equals(board)){
            System.out.println("Same board configuration. Returning last known bestmove "  + bestMove);
            return bestMove;
        }else{
            //new board detected
            oldBoard = board;
        }
        Minimax minimax = new Minimax(board, 10);
        bestMove = minimax.alphaBeta(board.getUserTurn()) + 1;
        return bestMove;
    }

    @Override
    public int getBestMove(Mat originalBoardImage) throws VisionException {
        return getBestMove(getBoard(originalBoardImage));
    }
}
