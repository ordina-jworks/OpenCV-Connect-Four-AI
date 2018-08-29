import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import iot.jworks.ordina.cfour.solver.BoardInformation;
import iot.jworks.ordina.cfour.solver.Solution;
import iot.jworks.ordina.cfour.solver.DisplayUtil;
import iot.jworks.ordina.cfour.solver.VisionException;
import iot.jworks.ordina.cfour.solver.StopWatch;
import iot.jworks.ordina.cfour.solver.ConnectFourSolver;
import iot.jworks.ordina.cfour.solver.ConnectFourSolverImpl;

public class Driver {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	private static DisplayUtil debugDisplay = null;
	private static final boolean debug = true;

	private static ConnectFourSolver connectFourSolver;


	public static void main(String[] args) {
		// Load the OpenCV Library
	
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.print("Library: " + Core.NATIVE_LIBRARY_NAME) ;
		
	
		if(debug){
			debugDisplay = new DisplayUtilAWSImpl();
		}

		connectFourSolver = new ConnectFourSolverImpl(debugDisplay);
		//CameraBridgeViewBase	
		//https://gist.github.com/jayrambhia/5265868

		if (args.length == 1) {
			String fileName = args[0];
			System.out.println("Starting Connect Four Analysis for file: " + fileName);
			try {
				File file = new File(fileName);
				if (file.isDirectory()) {
					processDirectory(file);
				} else {
					processImage(file);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} 
			System.out.println("Done");
		}
	}

	private static void processDirectory(File dir) throws IOException{
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
		  for (File child : directoryListing) {
			 if(child.isFile()){
				 processImage(child);
			 }
		  }
		}
	}


	private static void processImage(File file) throws IOException {
			
		System.out.println("Trying to process file:  " + file.getName());
		Mat originalBoardImage = bufferedImageToMat(file);
		//Issues when not using bufferedImage. Odd.
		//Mat originalBoardImage = imageFileToMat(file);

		Solution solution = null;
		try {						
			StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			BoardInformation boardInformation = connectFourSolver.getBoard(originalBoardImage);
			
			solution = connectFourSolver.getBestMove(boardInformation);
		} catch (VisionException e) {			
			e.printStackTrace();
			
		}
		if(solution != null){
			System.out.println("Best Move:" + solution.getBestMove());
			if(debugDisplay != null){
				//debugDisplay.showResult(solution.)
			}
		}else{
			System.err.println("Failed for file: " + file.getName());
		}





		
	}
	private static byte[] getBytes(BufferedImage img) {
        return ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
    }

	private static Mat bufferedImageToMat(File file) throws IOException {
		BufferedImage awtBufferedImage = ImageIO.read(file);

		awtBufferedImage = ImageUtils.resizeMaxWidth(awtBufferedImage, 800);

		byte[] data = getBytes(awtBufferedImage);

				int height =  awtBufferedImage.getHeight();
				int width = awtBufferedImage.getWidth();
				System.out.println("Height: " + height + " Width: " + width);
		return byteArrayToMat(data, height, width );
	}


	private static Mat imageFileToMat(File file) throws IOException{		
			byte[] data = Files.readAllBytes(file.toPath());
			//TODO resolve image size.
			return byteArrayToMat(data, 881, 1200);				
	}

	private static Mat byteArrayToMat(byte[] data,  int height, int width) {
		Mat mat = new Mat(height, width, CvType.CV_8UC3);
		mat.put(0, 0, data);
		return mat;
	};


}
