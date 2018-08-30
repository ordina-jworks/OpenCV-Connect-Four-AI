package iot.jworks.ordina.cfour.solver;

import org.opencv.core.Mat;

public interface ConnectFourSolver {

    BoardInformation getBoard(Mat originalBoardImage);

    Solution getBestMove(BoardInformation boardInformation);

    Solution getBestMove(Mat originalBoardImage);

    Mat drawBestMove(Solution solution);


}
