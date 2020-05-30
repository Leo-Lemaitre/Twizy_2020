package src;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class MaBibliothequeTraitementImage {
  public static Mat transformeBGRversHSV(Mat matriceBGR) {
    Mat matriceHSV = new Mat(matriceBGR.height(), matriceBGR.cols(), matriceBGR.type());
    Imgproc.cvtColor(matriceBGR, matriceHSV, 40);
    return matriceHSV;
  }
  
  public static Vector<Mat> splitHSVChannels(Mat input) {
    Vector<Mat> channels = new Vector<>();
    Core.split(input, channels);
    return channels;
  }
  
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
  
  public static Mat seuillage(Mat input, int seuilrougejaune, int seuilrougeviolet, int seuilsaturation) {
    Vector<Mat> channels = splitHSVChannels(input);
    Scalar rougejaune = new Scalar(seuilrougejaune);
    Scalar rougeviolet = new Scalar(seuilrougeviolet);
    Scalar gris = new Scalar(seuilsaturation);
    Mat souslesjaunes = new Mat();
    Mat surlesviolets = new Mat();
    Mat surlesgris = new Mat();
    Mat souslesjaunesOUsurlesviolets = new Mat();
    Mat souslesjaunesOUsurlesvioletsETsanslesgris = new Mat();
    Core.compare(channels.get(0), rougejaune, souslesjaunes, 3);
    Core.compare(channels.get(0), rougeviolet, surlesviolets, 1);
    Core.compare(channels.get(1), gris, surlesgris, 1);
    Core.bitwise_or(souslesjaunes, surlesviolets, souslesjaunesOUsurlesviolets);
    Core.bitwise_and(souslesjaunesOUsurlesviolets, surlesgris, souslesjaunesOUsurlesvioletsETsanslesgris);
    Imgproc.GaussianBlur(souslesjaunesOUsurlesvioletsETsanslesgris, souslesjaunesOUsurlesvioletsETsanslesgris, new Size(9.0D, 9.0D), 2.0D, 2.0D);
    return souslesjaunesOUsurlesvioletsETsanslesgris;
  }
  
  public static List<MatOfPoint> ExtractContours(Mat input) {
    int thresh = 100;
    Mat canny_output = new Mat();
    List<MatOfPoint> contours = new ArrayList<>();
    MatOfInt4 hierarchy = new MatOfInt4();
    Imgproc.Canny(input, canny_output, thresh, (thresh * 2));
    Imgproc.findContours(canny_output, contours, (Mat)hierarchy, 0, 2);
    Mat drawing = Mat.zeros(canny_output.size(), CvType.CV_8UC3);
    Random rand = new Random();
    for (int i = 0; i < contours.size(); i++) {
      Scalar color = new Scalar(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
      Imgproc.drawContours(drawing, contours, i, color, 1, 8, (Mat)hierarchy, 0, new Point());
    } 
    return contours;
  }
  
  public static Mat DetectForm(Mat img, MatOfPoint contour) {
    MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
    MatOfPoint2f approxCurve = new MatOfPoint2f();
    float[] radius = new float[1];
    Point center = new Point();
    Rect rect = Imgproc.boundingRect(contour);
    double contourArea = Imgproc.contourArea((Mat)contour);
    matOfPoint2f.fromList(contour.toList());
    Imgproc.minEnclosingCircle(matOfPoint2f, center, radius);
    if (contourArea / Math.PI * radius[0] * radius[0] >= 0.8D) {
      Core.circle(img, center, (int)radius[0], new Scalar(255.0D, 0.0D, 0.0D), 2);
      Core.rectangle(img, new Point(rect.x, rect.y), new Point((rect.x + rect.width), (rect.y + rect.height)), new Scalar(0.0D, 255.0D, 0.0D), 2);
      Mat tmp = img.submat(rect.y, rect.y + rect.height, rect.x, rect.x + rect.width);
      Mat sign = Mat.zeros(tmp.size(), tmp.type());
      tmp.copyTo(sign);
      return sign;
    } 
    Imgproc.approxPolyDP(matOfPoint2f, approxCurve, Imgproc.arcLength(matOfPoint2f, true) * 0.02D, true);
    long total = approxCurve.total();
    if (total == 3L) {
      Point[] pt = approxCurve.toArray();
      Core.line(img, pt[0], pt[1], new Scalar(255.0D, 0.0D, 0.0D), 2);
      Core.line(img, pt[1], pt[2], new Scalar(255.0D, 0.0D, 0.0D), 2);
      Core.line(img, pt[2], pt[0], new Scalar(255.0D, 0.0D, 0.0D), 2);
      Core.rectangle(img, new Point(rect.x, rect.y), new Point((rect.x + rect.width), (rect.y + rect.height)), new Scalar(0.0D, 255.0D, 0.0D), 2);
      Mat tmp = img.submat(rect.y, rect.y + rect.height, rect.x, rect.x + rect.width);
      Mat sign = Mat.zeros(tmp.size(), tmp.type());
      tmp.copyTo(sign);
      return sign;
    } 
    if (total >= 4L && total <= 6L) {
      List<Double> cos = new ArrayList<>();
      Point[] points = approxCurve.toArray();
      for (int j = 2; j < total + 1L; j++)
        cos.add(Double.valueOf(angle(points[(int)(j % total)], points[j - 2], points[j - 1]))); 
      Collections.sort(cos);
      Double minCos = cos.get(0);
      Double maxCos = cos.get(cos.size() - 1);
      boolean isRect = (total == 4L && minCos.doubleValue() >= -0.1D && maxCos.doubleValue() <= 0.3D);
      boolean isPolygon = !((total != 5L || minCos.doubleValue() < -0.34D || maxCos.doubleValue() > -0.27D) && (total != 6L || minCos.doubleValue() < -0.55D || maxCos.doubleValue() > -0.45D));
      if (isRect) {
        double ratio = Math.abs(1.0D - rect.width / rect.height);
        Core.rectangle(img, new Point(rect.x, rect.y), new Point((rect.x + rect.width), (rect.y + rect.height)), new Scalar(0.0D, 255.0D, 0.0D), 2);
        Mat tmp = img.submat(rect.y, rect.y + rect.height, rect.x, rect.x + rect.width);
        Mat sign = Mat.zeros(tmp.size(), tmp.type());
        tmp.copyTo(sign);
        return sign;
      } 
    } 
    return null;
  }
  
  public static double angle(Point a, Point b, Point c) {
    Point ab = new Point(b.x - a.x, b.y - a.y);
    Point cb = new Point(b.x - c.x, b.y - c.y);
    double dot = ab.x * cb.x + ab.y * cb.y;
    double cross = ab.x * cb.y - ab.y * cb.x;
    double alpha = Math.atan2(cross, dot);
    return Math.floor(alpha * 180.0D / Math.PI + 0.5D);
  }
  
  public static double tauxDeSimilitude(Mat object, String signfile) {
    Mat panneauref = Highgui.imread(signfile);
    Mat graySign = new Mat(panneauref.rows(), panneauref.cols(), panneauref.type());
    Imgproc.cvtColor(panneauref, graySign, 10);
    Core.normalize(graySign, graySign, 0.0D, 255.0D, 32);
    Mat signeNoirEtBlanc = new Mat();
    Core.compare(graySign, new Scalar(127.0D), signeNoirEtBlanc, 3);
    Mat grayObject = new Mat(panneauref.rows(), panneauref.cols(), panneauref.type());
    Imgproc.resize(object, object, graySign.size());
    Imgproc.cvtColor(object, grayObject, 10);
    Core.normalize(grayObject, grayObject, 0.0D, 255.0D, 32);
    Mat ObjetNoirEtBlanc = new Mat();
    Core.compare(grayObject, new Scalar(127.0D), ObjetNoirEtBlanc, 3);
    Mat masqueCirculaire = new Mat(ObjetNoirEtBlanc.size(), ObjetNoirEtBlanc.type());
    Core.circle(masqueCirculaire, new Point((grayObject.rows() / 2), (grayObject.cols() / 2)), (int)(grayObject.cols() * 0.2855D), new Scalar(255.0D), -1);
    Mat objetmasque = new Mat();
    Mat refmasquee = new Mat();
    Mat produit = new Mat();
    Core.bitwise_and(ObjetNoirEtBlanc, masqueCirculaire, objetmasque);
    Core.bitwise_and(signeNoirEtBlanc, masqueCirculaire, refmasquee);
    Core.bitwise_xor(objetmasque, refmasquee, produit);
    Core.bitwise_not(produit, produit);
    return Core.countNonZero(produit) / (produit.cols() * produit.rows());
  }
  
  public static int identifiepanneau(Mat objetrond) {
    double[] scores = new double[6];
    int indexmax = -1;
    if (objetrond != null) {
      scores[0] = tauxDeSimilitude(objetrond, "ref30.jpg");
      scores[1] = tauxDeSimilitude(objetrond, "ref50.jpg");
      scores[2] = tauxDeSimilitude(objetrond, "ref70.jpg");
      scores[3] = tauxDeSimilitude(objetrond, "ref90.jpg");
      scores[4] = tauxDeSimilitude(objetrond, "ref110.jpg");
      scores[5] = tauxDeSimilitude(objetrond, "refdouble.jpg");
      double scoremax = scores[0];
      for (int j = 1; j < scores.length; j++) {
        if (scores[j] > scoremax) {
          scoremax = scores[j];
          indexmax = j;
        } 
      } 
    } 
    return indexmax;
  }
  
  public static void ObjectifARealiser(String FichierVideo) {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    JFrame jframe = new JFrame("Detection de panneaux sur un flux vid");
    jframe.setDefaultCloseOperation(3);
    JLabel vidpanel = new JLabel();
    jframe.setContentPane(vidpanel);
    jframe.setSize(720, 480);
    jframe.setVisible(true);
    Mat frame = new Mat();
    VideoCapture camera = new VideoCapture(FichierVideo);
    Mat PanneauAAnalyser = null;
    int i = 0;
    int j = 0;
    int a = -1, b = -1;
    Size taille = new Size(0.0D, 0.0D);
    while (true) {
      if (camera.read(frame)) {
        j = 0;
        Mat transformee = transformeBGRversHSV(frame);
        Mat saturee = seuillage(transformee, 6, 170, 110);
        Mat objetrond = null;
        List<MatOfPoint> ListeContours = ExtractContours(saturee);
        for (MatOfPoint contour : ListeContours) {
          objetrond = DetectForm(frame, contour);
          if (objetrond != null) {
            j++;
            if (i > 15) {
              afficheImage("test", objetrond);
              PanneauAAnalyser = objetrond;
              b = a;
              a = identifiepanneau(PanneauAAnalyser);
              i = 0;
              break;
            } 
          } 
        } 
        if (j > 0)
          i++; 
        if (b != a) {
          switch (a) {
            case 0:
              System.out.println("Panneau 30 d");
              break;
            case 1:
              System.out.println("Panneau 50 d");
              break;
            case 2:
              System.out.println("Panneau 70 d");
              break;
            case 3:
              System.out.println("Panneau 90 d");
              break;
            case 4:
              System.out.println("Panneau 110 d");
              break;
            case 5:
              System.out.println("Panneau interdiction de dd");
              break;
          } 
          b = a;
        } 
        ImageIcon image = new ImageIcon(Mat2bufferedImage(frame));
        vidpanel.setIcon(image);
        vidpanel.repaint();
      } 
    } 
  }
  
  public static BufferedImage Mat2bufferedImage(Mat image) {
    MatOfByte bytemat = new MatOfByte();
    Highgui.imencode(".jpg", image, bytemat);
    byte[] bytes = bytemat.toArray();
    InputStream in = new ByteArrayInputStream(bytes);
    BufferedImage img = null;
    try {
      img = ImageIO.read(in);
    } catch (IOException e) {
      e.printStackTrace();
    } 
    return img;
  }
  
  public static int identifiepanneau1(Mat objetrond) {
    double[] scores = new double[6];
    int indexmax = -1;
    if (objetrond != null) {
      scores[0] = tauxDeSimilitude(objetrond, "ref30.jpg");
      scores[1] = tauxDeSimilitude(objetrond, "ref50.jpg");
      scores[2] = tauxDeSimilitude(objetrond, "ref70.jpg");
      scores[3] = tauxDeSimilitude(objetrond, "ref90.jpg");
      scores[4] = tauxDeSimilitude(objetrond, "ref110.jpg");
      scores[5] = tauxDeSimilitude(objetrond, "refdouble.jpg");
      double scoremax = scores[0];
      for (int j = 1; j < scores.length; j++) {
        if (scores[j] > scoremax) {
          scoremax = scores[j];
          indexmax = j;
        } 
      } 
    } 
    return indexmax;
  }
}
