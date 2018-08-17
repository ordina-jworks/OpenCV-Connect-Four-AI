
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class Driver {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}


	public static void main(String[] args) {

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

	private static void processDirectory(File file) {
		throw new RuntimeException("Not yet supported");
	}


	private static void processImage(File file) throws IOException {
			
		Mat originalBoardImage = bufferedImageToMat(file);
		//Issues when not using bufferedImage. Odd.
		//Mat originalBoardImage = imageFileToMat(file);

		int bestMove = -1;
		try {
			bestMove = ConnectFourVision.getMoveForImage(originalBoardImage, new DisplayUtilAWSImpl());
		} catch (VisionException e) {			
			e.printStackTrace();
			System.err.println("Failed for file: " + file.getName());
		}
		System.out.println("<BEST_MOVE>" + bestMove + "</BEST_MOVE>");
	}


	private static Mat bufferedImageToMat(File file) throws IOException {
		BufferedImage awtBufferedImage = ImageIO.read(file);		
		byte[] data = ((DataBufferByte) awtBufferedImage.getRaster().getDataBuffer())
				.getData();
				int height =  awtBufferedImage.getHeight();
				int width = awtBufferedImage.getWidth();
				System.out.println("Height: " + height + " Width: " + width);
		return byteArrayToMat(data, height, width );
	};


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
