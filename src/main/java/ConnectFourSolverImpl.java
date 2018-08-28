import org.opencv.core.Mat;

public class ConnectFourSolverImpl implements ConnectFourSolver {

    int bestMove = -1;
    private Board oldBoard;

    private ConnectFourVision connectFourVision;

    private DisplayUtil displayUtil;

    public ConnectFourSolverImpl(DisplayUtil displayUtil) {
        this.displayUtil = displayUtil;
        connectFourVision = new ConnectFourVision(displayUtil);
    }

    public DisplayUtil getDisplayUtil() {
        return displayUtil;
    }

    public void setDisplayUtil(DisplayUtil displayUtil) {
        this.displayUtil = displayUtil;
        connectFourVision.setDisplayUtil(displayUtil);
    }

    public ConnectFourSolverImpl() {
        connectFourVision = new ConnectFourVision(null);
    }

    @Override
    public Board getBoard(byte[] data) {
        return connectFourVision.getBoard(data);
    }

    @Override
    public Board getBoard(Mat originalBoardImage) throws VisionException {
        return connectFourVision.getBoard(originalBoardImage);
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
