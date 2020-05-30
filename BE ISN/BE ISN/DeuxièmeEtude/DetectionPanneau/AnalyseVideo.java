package DetectionPanneau;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import TraitementImage.*;


public class AnalyseVideo {
	
	static ArrayList<Integer> indexMax;
	static {
		try {
			System.load("C:/opencv/build/x64/vc14/bin/opencv_ffmpeg2413_64.dll");
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load.\n" + e);
			System.exit(1);
		}
	}

	static Mat imag = null;

	public static void main(String[] args) {
		int fr = 0;
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		JFrame jframe = new JFrame("Detection de panneaux sur un flux vidéo");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel vidpanel = new JLabel();
		jframe.setContentPane(vidpanel);
		jframe.setSize(720, 480);
		jframe.setVisible(true);

		Mat frame = new Mat();
		VideoCapture camera = new VideoCapture("C:\\Users\\Yahya\\OneDrive\\Documents\\video1.avi");
		Mat PanneauAAnalyser = null;
		//camera.read(frame);

		while (camera.read(frame)) {
			ImageIcon image = new ImageIcon(Mat2bufferedImage(frame));
			vidpanel.setIcon(image);
			vidpanel.repaint();
			fr++;
			if(fr >50) {	
				indexMax = DetectImage.detect(frame);
				if(indexMax.size()==0) {
				//	System.out.println("Aucun panneau n'est trouve");
				}else {
					fr = 0;
					for(int i=0;i<indexMax.size();i++) {
						switch(indexMax.get(i)){
						case 0:System.out.println("Panneau 30 détécté");break;
						case 1:System.out.println("Panneau 50 détécté");break;
						case 2:System.out.println("Panneau 70 détécté");break;
						case 3:System.out.println("Panneau 90 détécté");break;
						case 4:System.out.println("Panneau 110 détécté");break;
						case 5:System.out.println("Panneau interdiction de dépasser détécté");break;
						}
					}
				}
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return img;
	}

	public static int identifiepanneau(Mat objetrond){
		double [] scores=new double [6];
		int indexmax=-1;
		if (objetrond!=null){
			scores[0]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref30.jpg");
			scores[1]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref50.jpg");
			scores[2]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref70.jpg");
			scores[3]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref90.jpg");
			scores[4]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref110.jpg");
			scores[5]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"refdouble.jpg");

			double scoremax=scores[0];

			for(int j=1;j<scores.length;j++){
				if (scores[j]>scoremax){scoremax=scores[j];indexmax=j;}}	


		}
		return indexmax;
	}


}