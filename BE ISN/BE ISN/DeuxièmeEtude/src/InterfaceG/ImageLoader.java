package InterfaceG;


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

public class ImageLoader {
	Mat imageOriginale;
	public ImageLoader(String source) {
		System.loadLibrary("opencv_java2413");
		imageOriginale=Highgui.imread(source,Highgui.CV_LOAD_IMAGE_COLOR);
		System.out.println(imageOriginale);
	}
	public void AfficherImage(JPanel panel, String ex) {
		MatOfByte matOfByte=new MatOfByte();
		Highgui.imencode(ex,imageOriginale,matOfByte);

		byte[] byteArray=matOfByte.toArray();
		BufferedImage bufImage=null;
		try{
			InputStream in=new ByteArrayInputStream(byteArray);
			bufImage=ImageIO.read(in);
			Image scaled = bufImage.getScaledInstance(panel.getWidth(), panel.getHeight(),Image.SCALE_SMOOTH);
			panel.add(new JLabel(new ImageIcon(scaled)));
			panel.updateUI();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public Mat getImage() {
		return imageOriginale;
	}
}
