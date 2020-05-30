package TraitementImage;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

public class DetectImage {
	
	public static ArrayList<Integer> detect(Mat m) {
		ArrayList<Integer> listPan = new ArrayList<Integer>();
		Mat transformee=MaBibliothequeTraitementImageEtendue.transformeBGRversHSV(m);
		//la methode seuillage est ici extraite de l'archivage jar du meme nom 
		Mat saturee=MaBibliothequeTraitementImage.seuillage(transformee, 6, 170, 110);
		Mat objetrond = null;
		

		//Création d'une liste des contours à partir de l'image saturée
		List<MatOfPoint> ListeContours= MaBibliothequeTraitementImageEtendue .ExtractContours(saturee);
		
		
		
		int i=0;
		double [] scores=new double [6];
		//Pour tous les contours de la liste
		for (MatOfPoint contour: ListeContours  ){
			i++;
			objetrond=MaBibliothequeTraitementImageEtendue.DetectForm(m,contour);

			if (objetrond!=null){
				//*************
				Mat transformeerond=MaBibliothequeTraitementImageEtendue.transformeBGRversHSV(objetrond);
				//
				Mat satureerond=MaBibliothequeTraitementImage.seuillage(transformeerond, 115, 239, 110);

			//	MaBibliothequeTraitementImageEtendue.ExtractKeypoint();
		    //	List<MatOfPoint> ListeContoursrond= MaBibliothequeTraitementImageEtendue .ExtractContours(satureerond);
			//	List<Point> objetronddescriptor= MaBibliothequeTraitementImageEtendue.ExtractDesciptor(satureerond2);
		    	//*************
		    	
				scores[0]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref30.jpg");
				scores[1]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref50.jpg");
				scores[2]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref70.jpg");
				scores[3]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref90.jpg");
				scores[4]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref110.jpg");
				scores[5]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"refdouble.jpg");


				//recherche de l'index du maximum et affichage du panneau detecté
				double scoremax=-1;
				int indexmax=0;
				for(int j=0;j<scores.length;j++){
					if (scores[j]>scoremax){scoremax=scores[j];indexmax=j;}}	
				if(scoremax>0){listPan.add(indexmax);}
				
				

			}
		}	
		return listPan;
	}
}
