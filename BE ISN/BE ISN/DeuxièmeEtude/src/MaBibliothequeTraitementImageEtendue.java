
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import activeRecord.KeyPoints;
import activeRecord.PanneauxRef;
import activeRecord.Relation;

public class MaBibliothequeTraitementImageEtendue {
	// Contient toutes les méthodes necessaires à la transformation des images

	// Methode qui permet de transformer une matrice intialement au format BGR
	// au format HSV
	public static Mat transformeBGRversHSV(Mat matriceBGR) {
		Mat matriceHSV = new Mat(matriceBGR.height(), matriceBGR.cols(), matriceBGR.type());
		Imgproc.cvtColor(matriceBGR, matriceHSV, Imgproc.COLOR_BGR2HSV);
		return matriceHSV;

	}

	// Methode qui convertit une matrice avec 3 canaux en un vecteur de 3
	// matrices monocanal (un canal par couleur)
	public static Vector<Mat> splitHSVChannels(Mat input) {
		Vector<Mat> channels = new Vector<Mat>();
		Core.split(input, channels);
		return channels;
	}

	// Methode qui permet d'afficher une image sur un panel
	public static void afficheImage(String title, Mat img) {
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

	// Methode qui permet d'extraire les contours d'une image donnee
	public static List<MatOfPoint> ExtractContours(Mat input) {
		// Detecter les contours des formes trouvées
		int thresh = 100;
		Mat canny_output = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		MatOfInt4 hierarchy = new MatOfInt4();
		Imgproc.Canny(input, canny_output, thresh, thresh * 2);

		/// Find extreme outer contours
		Imgproc.findContours(canny_output, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

		Mat drawing = Mat.zeros(canny_output.size(), CvType.CV_8UC3);
		Random rand = new Random();
		for (int i = 0; i < contours.size(); i++) {
			Scalar color = new Scalar(rand.nextInt(255 - 0 + 1), rand.nextInt(255 - 0 + 1), rand.nextInt(255 - 0 + 1));
			Imgproc.drawContours(drawing, contours, i, color, 1, 8, hierarchy, 0, new Point());
		}
	//	afficheImage("Contours", drawing);

		return contours;
	}

	// ignorez cette fonction
	/*
	 * public static List<Point> ExtractDesciptor(Mat mat){ List<MatOfPoint>
	 * ListeContoursPointsInterets=
	 * MaBibliothequeTraitementImageEtendue.ExtractContours(mat); List<Point>
	 * lista = new ArrayList<>(); List<Point> listaEncours=new ArrayList<>();
	 * for(int k=0;k<ListeContoursPointsInterets.size();k++) {
	 * listaEncours.addAll(ListeContoursPointsInterets.get(k).toList());
	 * System.out.println(listaEncours); } return listaEncours; }
	 */

	// Methode qui permet de decouper et identifier les contours carrés,
	// triangulaires ou rectangulaires.
	// Renvoie null si aucun contour rond n'a été trouvé.
	// Renvoie une matrice carrée englobant un contour rond si un contour rond
	// a
	// été trouvé
	public static Mat DetectForm(Mat img, MatOfPoint contour) {
		MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
		MatOfPoint2f approxCurve = new MatOfPoint2f();
		float[] radius = new float[1];
		Point center = new Point();
		Rect rect = Imgproc.boundingRect(contour);
		double contourArea = Imgproc.contourArea(contour);

		matOfPoint2f.fromList(contour.toList());
		// Cherche le plus petit cercle entourant le contour
		Imgproc.minEnclosingCircle(matOfPoint2f, center, radius);
		// System.out.println(contourArea+" "+Math.PI*radius[0]*radius[0]);
		// on dit que c'est un cercle si l'aire occupé par le contour est à
		// supérieure à 80% de l'aire occupée par un cercle parfait
		if ((contourArea / (Math.PI * radius[0] * radius[0])) >= 0.8) {
			// System.out.println("Cercle");
			Core.circle(img, center, (int) radius[0], new Scalar(255, 0, 0), 2);
			Core.rectangle(img, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
					new Scalar(0, 255, 0), 2);
			Mat tmp = img.submat(rect.y, rect.y + rect.height, rect.x, rect.x + rect.width);
			Mat sign = Mat.zeros(tmp.size(), tmp.type());
			tmp.copyTo(sign);
			return sign;
		} else {

			Imgproc.approxPolyDP(matOfPoint2f, approxCurve, Imgproc.arcLength(matOfPoint2f, true) * 0.02, true);
			long total = approxCurve.total();
			if (total == 3) { // is triangle
				// System.out.println("Triangle");
				Point[] pt = approxCurve.toArray();
				Core.line(img, pt[0], pt[1], new Scalar(255, 0, 0), 2);
				Core.line(img, pt[1], pt[2], new Scalar(255, 0, 0), 2);
				Core.line(img, pt[2], pt[0], new Scalar(255, 0, 0), 2);
				Core.rectangle(img, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
						new Scalar(0, 255, 0), 2);
				Mat tmp = img.submat(rect.y, rect.y + rect.height, rect.x, rect.x + rect.width);
				Mat sign = Mat.zeros(tmp.size(), tmp.type());
				tmp.copyTo(sign);
				return null;
			}
			if (total >= 4 && total <= 6) {
				List<Double> cos = new ArrayList<>();
				Point[] points = approxCurve.toArray();
				for (int j = 2; j < total + 1; j++) {
					cos.add(angle(points[(int) (j % total)], points[j - 2], points[j - 1]));
				}
				Collections.sort(cos);
				Double minCos = cos.get(0);
				Double maxCos = cos.get(cos.size() - 1);
				boolean isRect = total == 4 && minCos >= -0.1 && maxCos <= 0.3;
				boolean isPolygon = (total == 5 && minCos >= -0.34 && maxCos <= -0.27)
						|| (total == 6 && minCos >= -0.55 && maxCos <= -0.45);
				if (isRect) {
					double ratio = Math.abs(1 - (double) rect.width / rect.height);
					// drawText(rect.tl(), ratio <= 0.02 ? "SQU" : "RECT");
					// System.out.println("Rectangle");
					Core.rectangle(img, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
							new Scalar(0, 255, 0), 2);
					Mat tmp = img.submat(rect.y, rect.y + rect.height, rect.x, rect.x + rect.width);
					Mat sign = Mat.zeros(tmp.size(), tmp.type());
					tmp.copyTo(sign);
					return null;
				}
				if (isPolygon) {
					// System.out.println("Polygon");
					// drawText(rect.tl(), "Polygon");
				}
			}
		}
		return null;

	}

	public static double angle(Point a, Point b, Point c) {
		Point ab = new Point(b.x - a.x, b.y - a.y);
		Point cb = new Point(b.x - c.x, b.y - c.y);
		double dot = (ab.x * cb.x + ab.y * cb.y); // dot product
		double cross = (ab.x * cb.y - ab.y * cb.x); // cross product
		double alpha = Math.atan2(cross, dot);
		return Math.floor(alpha * 180. / Math.PI + 0.5);
	}

	// methode à completer
	public static double Similitude(Mat object, String signfile) {

		// Conversion du signe de reference en niveaux de gris et normalisation
		Mat panneauref = Highgui.imread(signfile);
		Mat graySign = new Mat(panneauref.rows(), panneauref.cols(), panneauref.type());
		Imgproc.cvtColor(panneauref, graySign, Imgproc.COLOR_BGRA2GRAY);
		Core.normalize(graySign, graySign, 0, 255, Core.NORM_MINMAX);
		Mat signeNoirEtBlanc = new Mat();

		// Conversion du panneau extrait de l'image en gris et normalisation et
		// redimensionnement à la taille du panneau de réference
		Mat grayObject = new Mat(panneauref.rows(), panneauref.cols(), panneauref.type());
		Imgproc.resize(object, object, graySign.size());
		// afficheImage("Panneau extrait de l'image", object);
		Imgproc.cvtColor(object, grayObject, Imgproc.COLOR_BGRA2GRAY);
		Core.normalize(grayObject, grayObject, 0, 255, Core.NORM_MINMAX);
		Imgproc.resize(grayObject, grayObject, graySign.size());

		Core.normalize(graySign, graySign, 0, 255, Core.NORM_MINMAX);
		FeatureDetector orbDetector = FeatureDetector.create(FeatureDetector.GRID_MSER);
		DescriptorExtractor orbExtractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);

		MatOfKeyPoint objectKeypoints = new MatOfKeyPoint();
		orbDetector.detect(grayObject, objectKeypoints);

		MatOfKeyPoint signKeypoints = new MatOfKeyPoint();
		orbDetector.detect(graySign, signKeypoints);

		Mat objectDescriptor = new Mat(object.rows(), object.cols(), object.type());
		orbExtractor.compute(grayObject, objectKeypoints, objectDescriptor);

		Mat signDescriptor = new Mat(panneauref.rows(), panneauref.cols(), panneauref.type());
		orbExtractor.compute(graySign, signKeypoints, signDescriptor);

		List<MatOfDMatch> matchs = new ArrayList<MatOfDMatch>();
		int k = 3;
		DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
		// matcher.match(objectDescriptor, signDescriptor, matchs);
		matcher.knnMatch(objectDescriptor, signDescriptor, matchs, k);

		float ratioThresh = 0.7f;
		List<DMatch> listOfGoodMatches = new ArrayList<>();
		for (int i = 0; i < matchs.size(); i++) {
			if (matchs.get(i).rows() > 1) {
				DMatch[] matches = matchs.get(i).toArray();
				if (matches[0].distance < ratioThresh * matches[1].distance) {
					listOfGoodMatches.add(matches[0]);
				}
			}
		}
		MatOfDMatch goodMatches = new MatOfDMatch();
		goodMatches.fromList(listOfGoodMatches);
	//	System.out.println(goodMatches.dump());

		Mat matchedImage = new Mat(panneauref.rows(), panneauref.cols() * 2, panneauref.type());
		Features2d.drawMatches(object, objectKeypoints, panneauref, signKeypoints, goodMatches, matchedImage);

		return goodMatches.size().height;

	}
	/*
	 * public static void question_6() { // p33 du diapo open_cv Mat sroadSign =
	 * Highgui.imread("ref30.jpg");
	 * 
	 * Mat sObject = new Mat();
	 * 
	 * Imgproc.resize(sroadSign, sObject, sroadSign.size());
	 * 
	 * Mat grayObject = new Mat(sObject.rows(), sObject.cols(), sObject.type());
	 * Imgproc.cvtColor(sObject, grayObject, Imgproc.COLOR_BGRA2GRAY);
	 * 
	 * Core.normalize(grayObject, grayObject, 0, 255, Core.NORM_MINMAX);
	 * 
	 * Mat graySign = new Mat(sroadSign.rows(), sroadSign.cols(),
	 * sroadSign.type()); Imgproc.cvtColor(sroadSign, graySign,
	 * Imgproc.COLOR_BGRA2GRAY);
	 * 
	 * Core.normalize(graySign, graySign, 0, 255, Core.NORM_MINMAX);
	 * 
	 * FeatureDetector orbDetector =
	 * FeatureDetector.create(FeatureDetector.ORB); DescriptorExtractor
	 * orbExtractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
	 * 
	 * MatOfKeyPoint objectKeypoints = new MatOfKeyPoint();
	 * orbDetector.detect(grayObject, objectKeypoints);
	 * 
	 * MatOfKeyPoint signKeypoints = new MatOfKeyPoint();
	 * orbDetector.detect(graySign, signKeypoints);
	 * 
	 * Mat objectDescriptor = new Mat(sObject.rows(), sObject.cols(),
	 * sObject.type()); orbExtractor.compute(grayObject, objectKeypoints,
	 * objectDescriptor);
	 * 
	 * Mat signDescriptor = new Mat(sroadSign.rows(), sroadSign.cols(),
	 * sroadSign.type()); orbExtractor.compute(graySign, signKeypoints,
	 * signDescriptor);
	 * 
	 * MatOfDMatch matchs = new MatOfDMatch();
	 * 
	 * DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher .
	 * BRUTEFORCE) matcher .matchCobjectDescriptor, signDescriptor, matchs);
	 * 
	 * System. out.println(matchs.dump());
	 * 
	 * Mat matchedImage = new Mat(sroadSign.rows(), sroadSign.cols()*2,
	 * sroadSign.type()) Features2d.drawMatches(sObject, objectKeypoints,
	 * sroadSign, signKeypoints,
	 * 
	 * }
	 */

	public static void ExtractKeypoint() {
		Mat sroadSign = Highgui.imread("p10.jpg");
		Mat sObject = new Mat();
		Imgproc.resize(sroadSign, sObject, sroadSign.size());
		Mat grayObject = new Mat(sObject.rows(), sObject.cols(), sObject.type());
		Imgproc.cvtColor(sObject, grayObject, Imgproc.COLOR_BGRA2GRAY);
		Core.normalize(grayObject, grayObject, 0, 255, Core.NORM_MINMAX);
		Mat graySign = new Mat(sroadSign.rows(), sroadSign.cols(), sroadSign.type());
		Imgproc.cvtColor(sroadSign, graySign, Imgproc.COLOR_BGRA2GRAY);
		Core.normalize(graySign, graySign, 0, 255, Core.NORM_MINMAX);
		FeatureDetector orbDetector = FeatureDetector.create(FeatureDetector.ORB);
		DescriptorExtractor orbExtractor = DescriptorExtractor.create(DescriptorExtractor.ORB);

		MatOfKeyPoint objectKeypoints = new MatOfKeyPoint();
		orbDetector.detect(grayObject, objectKeypoints);

		// System.out.println(objectKeypoints.toList());

		MatOfKeyPoint signKeypoints = new MatOfKeyPoint();
		orbDetector.detect(graySign, signKeypoints);

		System.out.println("*****Matrix of keypoints*************");
		System.out.println(signKeypoints.toList().get(0));

		Mat objectDescriptor = new Mat(sObject.rows(), sObject.cols(), sObject.type());
		orbExtractor.compute(grayObject, objectKeypoints, objectDescriptor);

		Mat signDescriptor = new Mat(sroadSign.rows(), sroadSign.cols(), sroadSign.type());
		orbExtractor.compute(graySign, signKeypoints, signDescriptor);

		// Matrice des descripteurs
		// System.out.println("************Matrice des
		// descripteurs******************");
		// System.out.println(signDescriptor.dump());

		// System.out.println(signKeypoints.size());
		// System.out.println(signDescriptor.size());

		/*
		 * 
		 * //Le matching MatOfDMatch matchs = new MatOfDMatch();
		 * DescriptorMatcher matcher =
		 * DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
		 * matcher.match(objectDescriptor, signDescriptor,matchs);
		 * System.out.println(matchs.dump()); Mat matchedImage=new
		 * Mat(sroadSign.rows(),sroadSign.cols()*2,sroadSign.type());
		 * Features2d.drawMatches(sObject, objectKeypoints, sroadSign,
		 * signKeypoints, matchs, matchedImage);
		 * 
		 * 
		 */
	}

	public static void creerDB() throws SQLException {
		KeyPoints.createTable();
		PanneauxRef.createTable();
		Relation.createTable();
	}

	public static void remplirDBKeyPoints(String nomPanneau, List<KeyPoint> liste) throws SQLException {
		PanneauxRef pDB = new PanneauxRef(nomPanneau);
		pDB.save();
		Relation rDB;
		KeyPoints keyPointDB;
		float x, y, size, angle, response, octave;
		for (KeyPoint k : liste) {
			System.out.println(k);
			x = (float) k.pt.x;
			y = (float) k.pt.y;
			size = k.size;
			angle = k.angle;
			response = k.response;
			octave = k.octave;
			keyPointDB = new KeyPoints(x, y, size, angle, response, octave);
			keyPointDB.save();
			rDB = new Relation(pDB.getId(), keyPointDB.getIdKeyPoint());
			rDB.save();
		}

	}

	public static void supprimerDB() throws SQLException {
		Relation.deleteTable();
		KeyPoints.deleteTable();
		PanneauxRef.deleteTable();
	}

}
