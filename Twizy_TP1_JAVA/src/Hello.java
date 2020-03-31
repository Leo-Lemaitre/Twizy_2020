
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Hello {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
		System.out.println("mat = " + mat.dump());

		String filePath = "src/Images_partie_OpenCV/opencv.png";
		Mat m = LectureImage(filePath);
		System.out.println(m);

		for (int i = 0; i < m.height(); i++) {
			for (int j = 0; j < m.width(); j++) {
				double[] BGR = m.get(i, j);
				// Test si blanc
				if (BGR[0] == 255 && BGR[1] == 255 && BGR[2] == 255) {
					System.out.print('.');
				} else {
					System.out.print('+');
				}
			}
			System.out.println();
		}

		// Exercice 2
		filePath = "src/Images_partie_OpenCV/bgr.png";
		m = LectureImage(filePath);
		// ImShow("titre", m);
		Vector<Mat> channels = new Vector<Mat>();
		Core.split(m, channels);

		Mat dst = Mat.zeros(m.size(), m.type());
		Vector<Mat> chans = new Vector<Mat>();
		Mat empty = Mat.zeros(m.size(), CvType.CV_8UC1);
		for (int i = 0; i < channels.size(); i++) {
			// Exercice 2 solution 1
			// ImShow(Integer.toString(i), channels.get(i));
			chans.removeAllElements();
			for (int j = 0; j < channels.size(); j++) {
				if (j != i) {
					chans.add(empty);
				} else {
					chans.add(channels.get(i));

				}
			}
			Core.merge(chans, dst);
			// ImShow(Integer.toString(i), dst);

		}
		// EXERCICE 3

		filePath = "src/Images_partie_OpenCV/hsv.png";
		m = LectureImage(filePath);
		Mat output = Mat.zeros(m.size(), m.type());
		// Passage en HSV
		Imgproc.cvtColor(m, output, Imgproc.COLOR_BGR2HSV);
		// ImShow("hsv", output);
		channels = new Vector<Mat>();
		Core.split(output, channels);
		double hsv_values[][] = { { 1, 255, 255 }, { 179, 1, 255 }, { 179, 0, 1 } };
		for (int i = 0; i < 3; i++) {
			// ImShow(Integer.toString(i) + " HSv", channels.get(i));
			Mat chans2[] = new Mat[3];
			for (int j = 0; j < 3; j++) {
				empty = Mat.ones(m.size(), CvType.CV_8UC1);
				Mat comp = Mat.ones(m.size(), CvType.CV_8UC1);
				Scalar v = new Scalar(hsv_values[i][j]);
				Core.multiply(empty, v, comp);
				chans2[j] = comp;
			}
			chans2[i] = channels.get(i);
			dst = Mat.zeros(output.size(), output.type());
			Mat res = Mat.ones(dst.size(), dst.type());
			Core.merge(Arrays.asList(chans2), dst);
			Imgproc.cvtColor(dst, res, Imgproc.COLOR_HSV2BGR);
			// ImShow(Integer.toString(i) + " res", res);
		}
		// EXecerice 4 Seuillage sans lissage
		Mat m2 = LectureImage("src/Images_partie_OpenCV/circles.jpg");
		Mat hsv_image = Mat.zeros(m2.size(), m2.type());

		Imgproc.cvtColor(m2, hsv_image, Imgproc.COLOR_BGR2HSV);
		Mat threshold_img = new Mat();
		Mat threshold_img1 = new Mat();
		Mat threshold_img2 = new Mat();
		Core.inRange(hsv_image, new Scalar((216 * 179) / 360, 100, 100), new Scalar((236 * 179) / 360, 255, 255),
				threshold_img1);
		Core.inRange(hsv_image, new Scalar(160, 100, 100), new Scalar(179, 255, 255), threshold_img2);
		Core.bitwise_or(threshold_img1, threshold_img2, threshold_img);
		Imgproc.GaussianBlur(threshold_img, threshold_img, new Size(9, 9), 2, 2);
		// ImShow("Cercles rouge", threshold_img);

		// Filtre de Canny
		m = LectureImage("src/Images_partie_OpenCV/circles.jpg");
		ImShow("Cercles", m);
		hsv_image = Mat.zeros(m.size(), m.type());
		Imgproc.cvtColor(m, hsv_image, Imgproc.COLOR_BGR2HSV);
		// ImShow("HSV", hsv_image);
		threshold_img = DetecterFormesParLaCouleur(hsv_image);
		// ImShow("Seuillage",threshold_img);
		int thresh = 100;
		Mat canny_output = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		MatOfInt4 hierarchy = new MatOfInt4();
		Imgproc.Canny(threshold_img, canny_output, thresh, thresh * 2);
		Imgproc.findContours(canny_output, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		Mat drawing = Mat.zeros(canny_output.size(), CvType.CV_8UC3);
		Random rand = new Random();
		for (int i = 0; i < contours.size(); i++) {
			Scalar color = new Scalar(rand.nextInt(255 + 1), rand.nextInt(256), rand.nextInt(256));
			Imgproc.drawContours(drawing, contours, i, color, 1, 8, hierarchy, 0, new Point());
		}
		// ImShow("Contours",drawing);

		// PArtie 2

		m = LectureImage("src/Images_partie_OpenCV/circles.jpg");
		contours = DetecterCerclesParContour(threshold_img);

		MatOfPoint2f mat0fPoint2f = new MatOfPoint2f();
		float[] radius = new float[1];
		Point center = new Point();
		for (int c = 0; c < contours.size(); c++) {
			MatOfPoint contour = contours.get(c);
			double contourArea = Imgproc.contourArea(contour);
			mat0fPoint2f.fromList(contour.toList());
			Imgproc.minEnclosingCircle(mat0fPoint2f, center, radius);
			if ((contourArea / (Math.PI * radius[0] * radius[0])) >= 0.8) {
				Core.circle(m, center, (int) radius[0], new Scalar(0, 255, 0), 2);
			}
		}
		ImShow("Detecter cercles resultat", m);

	}

	private static Mat DetecterFormesParLaCouleur(Mat hsv_image) {
		Mat m2 = LectureImage("src/Images_partie_OpenCV/circles.jpg");
		Imgproc.cvtColor(m2, hsv_image, Imgproc.COLOR_BGR2HSV);
		Mat threshold_img = new Mat();
		Mat threshold_img1 = new Mat();
		Mat threshold_img2 = new Mat();
		Core.inRange(hsv_image, new Scalar((216 * 179) / 360, 100, 100), new Scalar((236 * 179) / 360, 255, 255),
				threshold_img1);
		Core.inRange(hsv_image, new Scalar(160, 100, 100), new Scalar(179, 255, 255), threshold_img2);
		Core.bitwise_or(threshold_img1, threshold_img2, threshold_img);
		Imgproc.GaussianBlur(threshold_img, threshold_img, new Size(9, 9), 2, 2);
		ImShow(" DetecterFormesParLaCouleur", threshold_img);
		return threshold_img;
	}

	public static List<MatOfPoint> DetecterCerclesParContour(Mat hsv_image) {

		int thresh = 100;
		Mat canny_output = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		MatOfInt4 hierarchy = new MatOfInt4();
		Imgproc.Canny(hsv_image, canny_output, thresh, thresh * 2);
		Imgproc.findContours(canny_output, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		/*
		 * Mat drawing = Mat.zeros(canny_output.size(), CvType.CV_8UC3); Random
		 * rand = new Random(); for (int i = 0; i < contours.size(); i++) {
		 * Scalar color = new Scalar(rand.nextInt(255 + 1), rand.nextInt(256),
		 * rand.nextInt(256)); Imgproc.drawContours(drawing, contours, i, color,
		 * 1, 8, hierarchy, 0, new Point()); }
		 */
		// ImShow(" DetecterCerclesParContour", drawing);
		return contours;

	}

	public static Mat LectureImage(String fichier) {
		return Highgui.imread(new File(fichier).getAbsolutePath());
	}

	public static void ImShow(String title, Mat img) {
		MatOfByte matOfByte = new MatOfByte();
		Highgui.imencode(".png", img, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		BufferedImage bufImage = null;
		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
			JFrame frame = new JFrame();
			frame.setTitle(title);
			frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
			frame.pack();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
