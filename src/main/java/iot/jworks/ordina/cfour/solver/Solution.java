package iot.jworks.ordina.cfour.solver;

import org.opencv.core.Mat;

public class Solution {

    private int bestMove = -1;

    private BoardInformation boardInformation;
    private long aiSolverDuration;

    private Mat drawSolved;

    public Solution(int bestMove, BoardInformation boardInformation) {
        this.bestMove = bestMove;
        this.boardInformation = boardInformation;
    }

    public Solution(BoardInformation boardInformation) {
        this.boardInformation = boardInformation;
    }


    public boolean hasVisionError(){
        return boardInformation.getComputerVisionError() != null;
    }

    public String getSolvedInformation(){
        return getCurrentPlayer() + " best move: " + getBestMove() + "| R:" + getBoardInformation().getBoard().getRedTokens() + " Y:"  + getBoardInformation().getBoard().getYellowTokens() +"| vision: " + getBoardInformation().getComputerVisionCalculationDuration() + " ai: " + getAiSolverDuration();
    }

    public int getBestMove() {
        return bestMove;
    }

    public void setBestMove(int bestMove) {
        this.bestMove = bestMove;
    }

    public BoardInformation getBoardInformation() {
        return boardInformation;
    }

    public void setBoardInformation(BoardInformation boardInformation) {
        this.boardInformation = boardInformation;
    }


    public long getAiSolverDuration() {
        return aiSolverDuration;
    }

    public void setAiSolverDuration(long aiSolverDuration) {
        this.aiSolverDuration = aiSolverDuration;
    }

    public Mat getDrawSolved() {
        return drawSolved;
    }

    public void setDrawSolved(Mat drawSolved) {
        this.drawSolved = drawSolved;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "bestMove=" + bestMove +
                ", boardInformation=" + boardInformation +
                ", aiSolverDuration=" + aiSolverDuration +
                '}';
    }

    public String getCurrentPlayer() {
        String player = "undefined";
        if(boardInformation != null && boardInformation.getBoard() != null){
            player = Board.MARK_RED == boardInformation.getBoard().getUserTurn() ? "Red" : "Yellow";
        }

        return player;
    }

}
