package iot.jworks.ordina.cfour.solver;

import org.opencv.core.Mat;

public class BoardInformation {

    private Mat computerView;

    private Mat projection;

    private Board board;

    private String computerVisionError;

    private long computerVisionCalculationDuration;

    private int redTokens;

    private int yellowTokens;


    public BoardInformation() {
    }

    public BoardInformation(Board board) {
        this.board = board;
    }

    public BoardInformation(Mat computerView, Board board) {
        this.computerView = computerView;
        this.board = board;
    }

    public BoardInformation(String computerVisionError) {
        this.computerVisionError = computerVisionError;
    }

    public Mat getComputerView() {
        return computerView;
    }

    public void setComputerView(Mat computerView) {
        this.computerView = computerView;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public String getComputerVisionError() {
        return computerVisionError;
    }

    public void setComputerVisionError(String computerVisionError) {
        this.computerVisionError = computerVisionError;
    }

    public long getComputerVisionCalculationDuration() {
        return computerVisionCalculationDuration;
    }

    public void setComputerVisionCalculationDuration(long computerVisionCalculationDuration) {
        this.computerVisionCalculationDuration = computerVisionCalculationDuration;
    }

    public Mat getProjection() {
        return projection;
    }

    public void setProjection(Mat projection) {
        this.projection = projection;
    }


    

    @Override
    public String toString() {
        return "BoardInformation{" +
                "board=" + board +
                ", computerVisionError='" + computerVisionError + '\'' +
                ", computerVisionCalculationDuration=" + computerVisionCalculationDuration +
                '}';
    }
}
