package iot.jworks.ordina.cfour.solver;

import org.opencv.core.Mat;

public class Solution {

    private int bestMove = -1;

    private BoardInformation boardInformation;
    private long aiSolverDuration;

    public Solution(int bestMove, BoardInformation boardInformation) {
        this.bestMove = bestMove;
        this.boardInformation = boardInformation;
    }

    public Solution(BoardInformation boardInformation) {
        this.boardInformation = boardInformation;
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

    @Override
    public String toString() {
        return "Solution{" +
                "bestMove=" + bestMove +
                ", boardInformation=" + boardInformation +
                ", aiSolverDuration=" + aiSolverDuration +
                '}';
    }
}