package iot.jworks.ordina.cfour.solver;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * BGR doesnt help keeping my sanity
 */
public class ColorHelper {

    public static Mat toRGB(Mat mat){
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2RGB);
        return mat;
    }

    public static Mat toBGR(Mat mat){
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2BGR);
        return mat;
    }


}
