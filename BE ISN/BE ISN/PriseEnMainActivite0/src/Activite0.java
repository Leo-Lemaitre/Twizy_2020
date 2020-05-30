package src;

import org.opencv.core.Core;

public class Activite0 {
	static {
		try {
			System.load("C:/opencv/build/x64/vc14/bin/opencv_ffmpeg2413_64.dll");
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load.\n" + e);
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	MaBibliothequeTraitementImage.ObjectifARealiser("video1.avi");
	}
}