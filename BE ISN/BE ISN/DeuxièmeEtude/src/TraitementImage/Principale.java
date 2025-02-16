package TraitementImage;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.*;
import org.opencv.highgui.*;

import activeRecord.KeyPoints;
public class Principale {

	public static void main(String[] args) throws SQLException
	{
		//Ouverture le l'image et saturation des rouges
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat m=Highgui.imread("p1.jpg",Highgui.CV_LOAD_IMAGE_COLOR);
		MaBibliothequeTraitementImageEtendue.afficheImage("Image test�e", m);
		Mat transformee=MaBibliothequeTraitementImageEtendue.transformeBGRversHSV(m);
		//la methode seuillage est ici extraite de l'archivage jar du meme nom 
		Mat saturee=MaBibliothequeTraitementImage.seuillage(transformee, 6, 170, 110);
		Mat objetrond = null;
		

		//Cr�ation d'une liste des contours � partir de l'image satur�e
		List<MatOfPoint> ListeContours= MaBibliothequeTraitementImageEtendue .ExtractContours(saturee);
		
		//A DECOMMENTER SI JAMAIS CREE
		//MaBibliothequeTraitementImageEtendue.creerDB();
		
		int i=0;
		double [] scores=new double [6];
		//Pour tous les contours de la liste
		for (MatOfPoint contour: ListeContours  ){
			i++;
			objetrond=MaBibliothequeTraitementImageEtendue.DetectForm(m,contour);

			if (objetrond!=null){
				MaBibliothequeTraitementImage.afficheImage("Objet rond det�ct�", objetrond);
				//*************
				Mat transformeerond=MaBibliothequeTraitementImageEtendue.transformeBGRversHSV(objetrond);
				//
				Mat satureerond=MaBibliothequeTraitementImage.seuillage(transformeerond, 115, 239, 110);
				MaBibliothequeTraitementImage.afficheImage("Objet rond det�ct�saturee", satureerond);
				MaBibliothequeTraitementImageEtendue.ExtractKeypoint();
		    //	List<MatOfPoint> ListeContoursrond= MaBibliothequeTraitementImageEtendue .ExtractContours(satureerond);
			//	List<Point> objetronddescriptor= MaBibliothequeTraitementImageEtendue.ExtractDesciptor(satureerond2);
		    	//*************
		    	
				scores[0]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref30.jpg");
				scores[1]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref50.jpg");
				scores[2]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref70.jpg");
				scores[3]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref90.jpg");
				scores[4]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref110.jpg");
				scores[5]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"refdouble.jpg");


				//recherche de l'index du maximum et affichage du panneau detect�
				double scoremax=-1;
				int indexmax=0;
				for(int j=0;j<scores.length;j++){
					if (scores[j]>scoremax){scoremax=scores[j];indexmax=j;}}	
				if(scoremax<0){System.out.println("Aucun Panneau d�t�ct�");}
				else{switch(indexmax){
				case -1:;break;
				case 0:System.out.println("Panneau 30 d�t�ct�");break;
				case 1:System.out.println("Panneau 50 d�t�ct�");break;
				case 2:System.out.println("Panneau 70 d�t�ct�");break;
				case 3:System.out.println("Panneau 90 d�t�ct�");break;
				case 4:System.out.println("Panneau 110 d�t�ct�");break;
				case 5:System.out.println("Panneau interdiction de d�passer d�t�ct�");break;
				}}

			}
		}	
		//MaBibliothequeTraitementImageEtendue.ExtractKeypoint();
		//KeyPoints.findByNomPanneau("ref30.jpg");

	}
}