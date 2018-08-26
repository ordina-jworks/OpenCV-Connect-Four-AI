import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class DisplayUtilAWSImpl implements DisplayUtil{

	@Override
	public void showResult(Mat image) {
		Mat resizedImage = new Mat();
		Imgproc.resize(image, resizedImage,
				new Size(image.cols(), image.rows()));
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".jpg", resizedImage, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		showResult(byteArray);
		
		
	}

	@Override
	public void showResult(byte[] byteArray) {
		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			BufferedImage bufImage = ImageIO.read(in);
			JFrame frame = new JFrame();
			frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
			frame.pack();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}