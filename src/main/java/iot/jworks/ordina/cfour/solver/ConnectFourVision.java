package iot.jworks.ordina.cfour.solver;



import java.util.LinkedList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ConnectFourVision {

	private static final int NUM_THRESHOLDS = 2;
	private static final Size MAGIC_SIZE = new Size(622,457);


	public enum SupportedColors{
		RED, YELLOW, BLUE, MYBLUE;
	}

	private DisplayUtil displayUtil;


	public ConnectFourVision(DisplayUtil displayUtil) {
		this.displayUtil = displayUtil;
	}

	public DisplayUtil getDisplayUtil() {
		return displayUtil;
	}

	public void setDisplayUtil(DisplayUtil displayUtil) {
		this.displayUtil = displayUtil;
	}


	public  BoardInformation getBoard(Mat originalBoardImage) throws VisionException {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();	
		

			// Load the connect four original image
			//	Mat originalBoardImage = bufferedImageToMat(awtBufferedImage);
	
			// Resize the image to a more manageable size
			Imgproc.resize(originalBoardImage, originalBoardImage, MAGIC_SIZE);
	
			// Create a copy of the original image to use
			Mat img = originalBoardImage.clone();
	
			if (displayUtil != null) {
				displayUtil.showResult(img);
			}
	
			// Apply thresholding techniques the board image
			// Mat boardThreshold = generateBoardThreshold(img);
			Mat boardThreshold = performThresholdForColor(img,SupportedColors.BLUE);
			if (displayUtil != null) {
				displayUtil.showResult(boardThreshold);
			}
	
			// Generate a mask from the thresholded board image
			Mat projection = generateBoardProjection(boardThreshold.clone(), originalBoardImage, displayUtil);
			if (displayUtil != null) {
				displayUtil.showResult(projection);
			}
	
			// Find red tokens in the image
			LinkedList<Circle> redTokens = findTokens(projection, SupportedColors.RED);
			System.out.println("Found " + redTokens.size() + " red tokens.");
	
			// Find yellow tokens in the image
			LinkedList<Circle> yellowTokens = findTokens(projection, SupportedColors.YELLOW);
			System.out.println("Found " + yellowTokens.size() + " yellow tokens.");
	
			// Create and display debug image for how the computer sees the game board
			Mat computerView = buildComputerVisionImage(projection, redTokens, yellowTokens);
			if (displayUtil != null) {
				displayUtil.showResult(computerView);
			}
	
			// Verify the number of tokens
			int tokenDifference = redTokens.size() - yellowTokens.size();
			if (Math.abs(tokenDifference) > 1) {
				throw new VisionException("Invalid numbers of game pieces.");
			}
	
			// Calculate the supposed positions of the pieces
			Board board = calculateTokenPositions(projection.size(), redTokens,
					yellowTokens);
			board.display();


			// Find whose turn it is
			char userTurn = board.getUserTurn();


			// Display whose turn it is
			if(userTurn == Board.MARK_RED){
				System.out.println("It is Red's turn.");
			}else{
				System.out.println("It is Yellow's turn.");
			}
	


			stopWatch.stop();

			BoardInformation boardInformation = new BoardInformation(computerView, board);
			boardInformation.setProjection(projection);
			boardInformation.setComputerVisionCalculationDuration(stopWatch.getElapsedTime());
			return boardInformation;

	}




	private Mat buildComputerVisionImage(Mat projection, LinkedList<Circle> redTokens, LinkedList<Circle> yellowTokens) {
		Mat debugImage = new Mat(projection.size(),projection.type(),new Scalar(128,0,0));
		for(Circle circle : redTokens){
			Imgproc.circle(debugImage, circle.getCenter(), circle.getRadius(), new Scalar(0,0,255),-1);
		}
		for(Circle circle : yellowTokens){
			Imgproc.circle(debugImage, circle.getCenter(), circle.getRadius(), new Scalar(0,255,255),-1);
		}
		return debugImage;
	}


	// Returns a binary image of the board based on the specified color
	private Mat performThresholdForColor(Mat image, SupportedColors color) {
		Mat imageHSV = new Mat();


		Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_RGB2HSV);
		Mat threshold = new Mat();

		Scalar lower;
		Scalar upper;

		switch (color) {
			case BLUE:
				lower = new Scalar(0, 160, 60);
				upper = new Scalar(15, 255, 255);
				break;
			case MYBLUE:
				lower = new Scalar(100, 20, 60);
				upper = new Scalar(135, 255, 255);
				break;
			case RED:
				lower = new Scalar(120, 160, 60);
				upper = new Scalar(130, 255, 255);

			//	lower = new Scalar(110, 160, 60);
		//		upper = new Scalar(130, 255, 255);
				break;
			case YELLOW:
				lower = new Scalar(90, 160, 60);
				upper = new Scalar(100, 255, 255);
				break;

			default:
				throw new IllegalArgumentException("Unsupported color: " + color);
		}

		Core.inRange(imageHSV, lower, upper, threshold);

		return threshold;
	}


	public Mat drawBestMove(Solution solution){

		//we have best column, find best row!
		//starting from bottom at 0, moving up
		int row = 4;
		row = 5;
		//Starting from 1, counting towards the right
		int column = solution.getBestMove();
		column = 7;

		Mat projection = solution.getBoardInformation().getProjection();
		Size boardBounds = projection.size();

		final float BLOCK_WIDTH = (float) boardBounds.width / Board.COLUMNS;
		final float BLOCK_HEIGHT = (float) boardBounds.height / Board.ROWS;


		//average block height and block width to get radius
		int circleRadius = (int)(BLOCK_HEIGHT + BLOCK_WIDTH) / 4 * 2 / 3;

		//color orange
		//rgb(255,165,0)
		Scalar orange = new Scalar(0,165,255);

		//TODO validate
	//	double x = BLOCK_WIDTH * column - (BLOCK_WIDTH / 2);
	//	double y = BLOCK_HEIGHT * row + (BLOCK_HEIGHT /2);


	    //x is pointing right
		double x = BLOCK_WIDTH * column - (BLOCK_WIDTH / 2) ;

		//y is pointing down
		double y = (BLOCK_HEIGHT *  Board.ROWS) - (BLOCK_HEIGHT * row + (BLOCK_HEIGHT /2));

		

		Point center = new Point( x,y);
		Circle bestMoveCircle = new Circle(center, circleRadius);

		Mat solved = projection.clone();

		Imgproc.circle(solved, bestMoveCircle.getCenter(), bestMoveCircle.getRadius(), orange,-1);

		return solved;
	}

	private Board calculateTokenPositions(Size boardBounds,
			LinkedList<Circle> redTokens, LinkedList<Circle> yellowTokens) {
		Board result = new Board();
		final float BLOCK_WIDTH = (float) boardBounds.width / Board.COLUMNS;
		final float BLOCK_HEIGHT = (float) boardBounds.height / Board.ROWS;
		for (int row = 0; row < Board.ROWS; row++) {
			for (int col = 0; col < Board.COLUMNS; col++) {
				Point point = new Point(col * BLOCK_WIDTH + BLOCK_WIDTH / 2,
						(Board.ROWS - row - 1) * BLOCK_HEIGHT + BLOCK_HEIGHT
								/ 2);
				for (Circle token : redTokens) {
					if (token.containsPoint(point)) {
						result.set(col, Board.MARK_RED);
						break;
					}
				}
				for (Circle token : yellowTokens) {
					if (token.containsPoint(point)) {
						result.set(col, Board.MARK_BLACK);
						break;
					}
				}
			}
		}
		return result;
	}

	private LinkedList<Circle> findTokens(Mat sourceImage,
		SupportedColors tokenColor) {
		Mat boardThreshold = performThresholdForColor(sourceImage, tokenColor);
		LinkedList<MatOfPoint> contours = new LinkedList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(boardThreshold, contours, hierarchy,
				Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
		LinkedList<Circle> minCircles = new LinkedList<Circle>();
		for (int i = 0; i < contours.size(); i++) {
			double area = Imgproc.contourArea(contours.get(i));
			if (area > 200) {
				Point center = new Point();
				float[] radius = new float[1];
				Imgproc.minEnclosingCircle(new MatOfPoint2f(contours.get(i)
						.toArray()), center, radius);
				Circle circle = new Circle(center, (int) radius[0] + 5);
				minCircles.push(circle);
			}
		}
		return minCircles;
	}

	private Mat generateBoardProjection(Mat boardThreshold, Mat originalBoardImage, DisplayUtil ui)
			throws VisionException {

		// Find the polygon enclosing the blue connect four board
		LinkedList<MatOfPoint> contours = new LinkedList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(boardThreshold, contours, hierarchy,
				Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
		int maxContourIndex = 0;
		double maxArea = 0;
		for (int i = 0; i < contours.size(); i++) {
			double area = Imgproc.contourArea(contours.get(i));
			if (area > maxArea) {
				maxArea = area;
				maxContourIndex = i;
			}
		}

		// Calculate the percent of the image the detected blue area makes up
		double percentSpace = maxArea
				/ (boardThreshold.width() * boardThreshold.height()) * 100;
		System.out.println("The board occupies " + Math.round(percentSpace)
				+ "% of the image.");


        // Throw an exception if the detected blue area is too small
		if (percentSpace < 20) {
			throw new VisionException("A sufficiently large board could not be detected.");
		}


		return warpPerspective(boardThreshold, originalBoardImage, ui, contours, maxContourIndex);
	}



	private Mat warpPerspective(Mat boardThreshold, Mat originalBoardImage, DisplayUtil ui, LinkedList<MatOfPoint> contours,
								int maxContourIndex) throws VisionException{
		// Find possible border lines of the enclosing polygon
		Mat newImage = Mat.zeros(boardThreshold.size(), boardThreshold.type());
		Imgproc.drawContours(newImage, contours, maxContourIndex, new Scalar(
				255));
		Mat lines = new Mat();
		Imgproc.HoughLines(newImage, lines, 1, Math.PI / 180, 75);


		ui.showResult(newImage);
		ui.showResult(boardThreshold);

		LinkedList<Line> detectedLines = new LinkedList<Line>();
		System.out.println("Total cols: " + lines.rows());
		for (int i = 0; i < lines.rows(); i++) {
			double[] info = lines.get(i, 0);
			Line line = new Line(info[0], info[1]);
			Imgproc.clipLine(new Rect(0, 0, boardThreshold.width(), boardThreshold.height()), line.getPt1(), line.getPt2());
			detectedLines.push(line);
		}
		System.out.println("There are " + detectedLines.size()
				+ " lines that were detected.");
		if (ui != null) {
			Mat houghLinesImage = buildHoughLinesImage(originalBoardImage, contours, maxContourIndex, detectedLines);
			ui.showResult(houghLinesImage);
		}

		// Get the corners of the polygon and apply the transform
		Mat corners1 = calculateBorderFromLines(detectedLines);
		Size resultSize = originalBoardImage.size();
		double[] data1 = { 0, 0 };
		double[] data2 = { resultSize.width, 0 };
		double[] data3 = { resultSize.width, resultSize.height };
		double[] data4 = { 0, resultSize.height };
		Mat corners2 = new Mat(new Size(4, 1), CvType.CV_32FC2);
		corners2.put(0, 0, data1);
		corners2.put(0, 1, data2);
		corners2.put(0, 2, data3);
		corners2.put(0, 3, data4);
		Mat transform = Imgproc.getPerspectiveTransform(corners1, corners2);
		Mat dst = new Mat(resultSize, originalBoardImage.type());
		Imgproc.warpPerspective(originalBoardImage, dst, transform, dst.size());
		return dst;

	}


	private Mat buildHoughLinesImage(Mat originalBoardImage, LinkedList<MatOfPoint> contours, int maxContourIndex, LinkedList<Line> detectedLines) {
		Mat debugImage = originalBoardImage.clone();
		Imgproc.drawContours(debugImage, contours, maxContourIndex, new Scalar(
                0, 0, 255), 3);
		for (Line line : detectedLines) {
            Imgproc.line(debugImage, line.getPt1(), line.getPt2(), new Scalar(0,
                    255, 0), 3);
        }
		return debugImage;
	}


	// Given lists of horizontal and vertical lines on the border of the image,
	// determine the border rectangle of the image
	private Mat calculateBorderFromLines(LinkedList<Line> lines)
			throws VisionException {

		// Create a list of every intersection
		LinkedList<Point> points = new LinkedList<Point>();
		for (int i = 0; i < lines.size(); i++) {
			for (int j = i + 1; j < lines.size(); j++) {
				Line line1 = lines.get(i);
				Line line2 = lines.get(j);
				Point point = line1.getIntersectionPoint(line2);
				if (!line1.similarTheta(line2) && point != null) {
					points.add(point);
				}
			}
		}
		System.out.println(points.size() + " intersections detected.");

		// Find the average of all the points
		Point average = averagePoints(points);

		// Categorize the points
		LinkedList<Point> topLeftPoints = new LinkedList<Point>();
		LinkedList<Point> topRightPoints = new LinkedList<Point>();
		LinkedList<Point> bottomLeftPoints = new LinkedList<Point>();
		LinkedList<Point> bottomRightPoints = new LinkedList<Point>();
		for (Point point : points) {
			if (point.x < average.x) {
				if (point.y < average.y) {
					topLeftPoints.add(point);
				} else {
					bottomLeftPoints.add(point);
				}
			} else {
				if (point.y < average.y) {
					topRightPoints.add(point);
				} else {
					bottomRightPoints.add(point);
				}
			}
		}

		if (topLeftPoints.size() == 0 || topRightPoints.size() == 0
				|| bottomLeftPoints.size() == 0
				|| bottomRightPoints.size() == 0) {
			throw new VisionException(
					"Could not identify the corners of the game board.");
		}

		// Average the points in each of the categories
		Point pt1 = averagePoints(topLeftPoints);
		Point pt2 = averagePoints(topRightPoints);
		Point pt3 = averagePoints(bottomRightPoints);
		Point pt4 = averagePoints(bottomLeftPoints);
		Mat corners = new Mat(new Size(4, 1), CvType.CV_32FC2);

		// Return the matrix of the corners
		double[] data1 = { pt1.x, pt1.y };
		double[] data2 = { pt2.x, pt2.y };
		double[] data3 = { pt3.x, pt3.y };
		double[] data4 = { pt4.x, pt4.y };
		corners.put(0, 0, data1);
		corners.put(0, 1, data2);
		corners.put(0, 3, data4);
		corners.put(0, 2, data3);
		return corners;
	}

	private Point averagePoints(LinkedList<Point> points) {
		Point result = new Point(0, 0);
		for (Point point : points) {
			result.x += point.x;
			result.y += point.y;
		}
		result.x /= points.size();
		result.y /= points.size();
		return result;
	}

}
