

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

public class DetectKeypoints {
	public static String DetectKeyPoint(Mat m) {
		Mat sroadSign = m;
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

		System.out.println("****Matrix of keypoints************");
		System.out.println(signKeypoints.toList().get(0));
		

		Mat objectDescriptor = new Mat(sObject.rows(), sObject.cols(), sObject.type());
		orbExtractor.compute(grayObject, objectKeypoints, objectDescriptor);

		Mat signDescriptor = new Mat(sroadSign.rows(), sroadSign.cols(), sroadSign.type());
		orbExtractor.compute(graySign, signKeypoints, signDescriptor);
		return (signKeypoints.toList().get(0).toString());
	}

}