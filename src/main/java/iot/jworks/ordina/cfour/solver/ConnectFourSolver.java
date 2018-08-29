package iot.jworks.ordina.cfour.solver;

import org.opencv.core.Mat;

public interface ConnectFourSolver {

    BoardInformation getBoard(Mat originalBoardImage) throws VisionException;

    Solution getBestMove(BoardInformation boardInformation);

    Solution getBestMove(Mat originalBoardImage) throws VisionException;


}
