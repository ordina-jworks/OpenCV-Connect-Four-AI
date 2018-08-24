import org.opencv.core.Mat;

public interface ConnectFourSolver {


    Board getBoard(byte[] data, DisplayUtil ui);

    Board  getBoard(Mat originalBoardImage) throws VisionException;

    Board getBoard(Mat originalBoardImage, DisplayUtil ui) throws VisionException;

    int getBestMove(Board board);

    int getBestMove(Mat originalBoardImage) throws VisionException;


}
