
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class MaBibliothequeTraitementImage {
	//Contient toutes les méthodes necessaires à la transformation des images


	//Methode qui permet de transformer une matrice intialement au  format BGR au format HSV
	public static Mat transformeBGRversHSV(Mat matriceBGR){
		Mat matriceHSV=new Mat(matriceBGR.height(),matriceBGR.cols(),matriceBGR.type());
		Imgproc.cvtColor(matriceBGR,matriceHSV,Imgproc.COLOR_BGR2HSV);
		return matriceHSV;

	}

	//Methode qui convertit une matrice avec 3 cannaux en un vecteur de 3 matrices monocanal (un canal par couleur)
	public static Vector<Mat> splitHSVChannels(Mat input) {
		Vector<Mat> channels = new Vector<Mat>(); 
		Core.split(input, channels);
		return channels;
	}

	//Methode qui permet d'afficher une image sur un panel
	public static void afficheImage(String title, Mat img){
		MatOfByte matOfByte=new MatOfByte();
		Highgui.imencode(".png",img,matOfByte);
		byte[] byteArray=matOfByte.toArray();
		BufferedImage bufImage=null;
		try{
			InputStream in=new ByteArrayInputStream(byteArray);
			bufImage=ImageIO.read(in);
			JFrame frame=new JFrame();
			frame.setTitle(title);
			frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
			frame.pack();
			frame.setVisible(true);

		}
		catch(Exception e){
			e.printStackTrace();
		}


	}

	//Methode qui permet de saturer les couleurs rouges à partir de 3 seuils
	public static Mat seuillage(Mat input, int seuilRougeOrange, int seuilRougeViolet,int seuilSaturation){
		Vector<Mat> channels = splitHSVChannels(input);
		//création d'un seuil 
		Scalar rougeviolet = new Scalar(seuilRougeViolet);
		Scalar rougeorange = new Scalar(seuilRougeOrange);
		Scalar seuilsaturation = new Scalar(seuilSaturation);
		
		//Création d'une matrice
		Mat rouges=new Mat();
		Mat rougeOrange=new Mat();
		Mat rougeViolet=new Mat();
		Mat Saturation=new Mat();
		//Comparaison et saturation des pixels dont la composante rouge est plus grande que le seuil rougeViolet
		Core.compare(channels.get(0), rougeviolet, rougeViolet, Core.CMP_GT);
		Core.compare(channels.get(0), rougeorange, rougeOrange, Core.CMP_GT);
		Core.bitwise_not(rougeOrange, rougeOrange);
		Core.compare(channels.get(1), seuilsaturation, Saturation, Core.CMP_GT);
		
		Core.bitwise_or(rougeOrange, rougeViolet, rouges);
		Core.bitwise_and(rouges, Saturation, rouges);

		//image saturée à retourner
		return rouges;



	}


	//Methode d'exemple qui permet de saturer les couleurs rouges à partir d'un seul seuil 
	public static Mat seuillage_exemple(Mat input, int seuilRougeViolet){
		// Decomposition en 3 cannaux HSV
		Vector<Mat> channels = splitHSVChannels(input);
		//création d'un seuil 
		Scalar rougeviolet = new Scalar(seuilRougeViolet);
		//Création d'une matrice
		Mat rouges=new Mat();
		//Comparaison et saturation des pixels dont la composante rouge est plus grande que le seuil rougeViolet
		Core.compare(channels.get(0), rougeviolet, rouges, Core.CMP_GT);
		//image saturée à retourner
		return rouges;

	}

	public static double tauxDeSimilitude(Mat objetrond, String string) {
		// TODO Auto-generated method stub
		return 0;
	}



}

