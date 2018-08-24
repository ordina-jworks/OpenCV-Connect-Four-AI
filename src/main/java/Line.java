
import org.opencv.core.Point;
import org.opencv.core.Size;

public class Line {
	//extends Line2D.Double
	
	private static final double RANGE = Math.PI / 8;
	private double theta;

	
	private double x1,x2,y1,y2;


	public Line(double rho, double theta) {
		this.theta=theta;
		double a = Math.cos(theta);
		double b = Math.sin(theta);
		double x0 = a * rho;
		double y0 = b * rho;
		x1 = Math.round(x0 + 1000 * (-b));
		y1 = Math.round(y0 + 1000 * (a));
		x2 = Math.round(x0 - 1000 * (-b));
		y2 = Math.round(y0 - 1000 * (a));

	}


	public Point getPt1() {
		return new Point(x1, y1);
	}

	public Point getPt2() {
		return new Point(x2, y2);
	}



	public Boolean similarTheta(Line line){
		return (Math.abs(theta-line.theta)<Math.PI/8);
	}

	public Point getIntersectionPoint(Line line) {
		if (!intersectsLine(line))
			return null;
		double px = x1, py = y1, rx = x2 - px, ry = y2 - py;
		double qx = line.getX1(), qy = line.getY1(), sx = line.getX2() - qx, sy = line
				.getY2() - qy;

		double det = sx * ry - sy * rx;
		if (det == 0) {
			return null;
		} else {
			double z = (sx * (qy - py) + sy * (px - qx)) / det;
			if (z == 0 || z == 1)
				return null;
			return new Point((float) (px + z * rx), (float) (py + z * ry));
		}
	}

	public double getX1() {
		return x1;
	}

	public double getX2() {
		return x2;
	}

	public double getY1() {
		return y1;
	}

	public double getY2() {
		return y2;
	}


	public boolean intersectsLine(Line l) {
		return linesIntersect(l.getX1(), l.getY1(), l.getX2(), l.getY2(), getX1(), getY1(),
				getX2(), getY2());
	}


	/**
	 * Tells whether the two line segments cross.
	 *
	 * @param x1
	 *            the x coordinate of the starting point of the first segment.
	 * @param y1
	 *            the y coordinate of the starting point of the first segment.
	 * @param x2
	 *            the x coordinate of the end point of the first segment.
	 * @param y2
	 *            the y coordinate of the end point of the first segment.
	 * @param x3
	 *            the x coordinate of the starting point of the second segment.
	 * @param y3
	 *            the y coordinate of the starting point of the second segment.
	 * @param x4
	 *            the x coordinate of the end point of the second segment.
	 * @param y4
	 *            the y coordinate of the end point of the second segment.
	 * @return true, if the two line segments cross.
	 */
	public static boolean linesIntersect(double x1, double y1, double x2, double y2, double x3,
										 double y3, double x4, double y4) {
		/*
		 * A = (x2-x1, y2-y1) B = (x3-x1, y3-y1) C = (x4-x1, y4-y1) D = (x4-x3,
		 * y4-y3) = C-B E = (x1-x3, y1-y3) = -B F = (x2-x3, y2-y3) = A-B Result
		 * is ((AxB) (AxC) <=0) and ((DxE) (DxF) <= 0) DxE = (C-B)x(-B) =
		 * BxB-CxB = BxC DxF = (C-B)x(A-B) = CxA-CxB-BxA+BxB = AxB+BxC-AxC
		 */
		x2 -= x1; // A
		y2 -= y1;
		x3 -= x1; // B
		y3 -= y1;
		x4 -= x1; // C
		y4 -= y1;
		double AvB = x2 * y3 - x3 * y2;
		double AvC = x2 * y4 - x4 * y2;
		// Online
		if (AvB == 0.0 && AvC == 0.0) {
			if (x2 != 0.0) {
				return (x4 * x3 <= 0.0)
						|| ((x3 * x2 >= 0.0) && (x2 > 0.0 ? x3 <= x2 || x4 <= x2 : x3 >= x2
						|| x4 >= x2));
			}
			if (y2 != 0.0) {
				return (y4 * y3 <= 0.0)
						|| ((y3 * y2 >= 0.0) && (y2 > 0.0 ? y3 <= y2 || y4 <= y2 : y3 >= y2
						|| y4 >= y2));
			}
			return false;
		}
		double BvC = x3 * y4 - x4 * y3;
		return (AvB * AvC <= 0.0) && (BvC * (AvB + BvC - AvC) <= 0.0);
	}
}
