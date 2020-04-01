
import java.io.File;

import org.opencv.core.*;
import org.opencv.highgui.*;

public class Activite3 {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat m = Highgui.imread("p0.jpg", Highgui.CV_LOAD_IMAGE_COLOR);
		MaBibliothequeTraitementImage.afficheImage("Image originale", m);
		MaBibliothequeTraitementImage.afficheImage("Image HSV",
				MaBibliothequeTraitementImage.transformeBGRversHSV(LectureImage("p0.jpg")));

	}

	public static Mat LectureImage(String fichier) {
		File f = new File(fichier);
		Mat m = Highgui.imread(f.getPath());
		return m;

	}
}