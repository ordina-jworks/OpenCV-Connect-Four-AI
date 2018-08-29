package iot.jworks.ordina.cfour.solver;

import org.opencv.core.Mat;

public class ConnectFourSolverImpl implements ConnectFourSolver {


    private Solution previousSolution;


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
    public BoardInformation getBoard(Mat originalBoardImage) throws VisionException {
        return connectFourVision.getBoard(originalBoardImage);
    }


    @Override
    public Solution getBestMove(BoardInformation boardInformation) {
        if(previousSolution != null && previousSolution.getBoardInformation() != null && previousSolution.getBoardInformation().getBoard() != null && previousSolution.getBoardInformation().getBoard().equals(boardInformation.getBoard())){
            System.out.println("Same board configuration. Returning last known bestmove "  + previousSolution.getBestMove());
            return previousSolution;
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Minimax minimax = new Minimax(boardInformation.getBoard(), 10);
        int bestMove = minimax.alphaBeta(boardInformation.getBoard().getUserTurn()) + 1;
        Solution solution = new Solution(bestMove, boardInformation);
        previousSolution = solution;
        stopWatch.stop();
        solution.setAiSolverDuration(stopWatch.getElapsedTime());
        return solution;
    }

    @Override
    public Solution getBestMove(Mat originalBoardImage) throws VisionException {
        return getBestMove(getBoard(originalBoardImage));
    }
}
