import org.opencv.core.Mat;

public interface ConnectFourSolver {


    Board getBoard(byte[] data);

    Board  getBoard(Mat originalBoardImage) throws VisionException;

    int getBestMove(Board board);

    int getBestMove(Mat originalBoardImage) throws VisionException;


}
