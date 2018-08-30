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
    public BoardInformation getBoard(Mat originalBoardImage){
        try{
            BoardInformation boardInformation = connectFourVision.getBoard(originalBoardImage);
            return boardInformation;
        }catch(VisionException e){
            return new BoardInformation(e.getMessage());
        }

    }


    @Override
    public Solution getBestMove(BoardInformation boardInformation) {
        if(previousSolution != null && previousSolution.getBoardInformation() != null && previousSolution.getBoardInformation().getBoard() != null && previousSolution.getBoardInformation().getBoard().equals(boardInformation.getBoard())){
            System.out.println("Same board configuration. Returning last known bestmove "  + previousSolution.getBestMove());
            previousSolution.setAiSolverDuration(Math.abs(previousSolution.getAiSolverDuration()) * -1);
            return previousSolution;
        }

        if(boardInformation.getComputerVisionError() != null){
            return new Solution(boardInformation);
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
    public Solution getBestMove(Mat originalBoardImage){
        return getBestMove(getBoard(originalBoardImage));
    }

    @Override
    public Mat drawBestMove(Solution solution){
        return connectFourVision.drawBestMove(solution);
    }
}
