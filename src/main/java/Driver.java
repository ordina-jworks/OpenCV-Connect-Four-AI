
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

import org.opencv.core.Core;

public class Driver {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {

		if (args.length == 1) {
			System.out.println("Starting Connect Four Analysis");
			try {
				File file = new File(args[0]);
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
		BufferedImage image = ImageIO.read(file);		
		int bestMove = -1;
		try {
			bestMove = ConnectFourVision.getMoveForImage(image, new DisplayUtilAWSImpl());
		} catch (VisionException e) {			
			e.printStackTrace();
			System.err.println("Failed for file: " + file.getName());
		}
		System.out.println("<BEST_MOVE>" + bestMove + "</BEST_MOVE>");
	}

}
