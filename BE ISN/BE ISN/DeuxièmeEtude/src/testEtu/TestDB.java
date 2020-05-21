package testEtu;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opencv.features2d.KeyPoint;

import activeRecord.KeyPoints;
import activeRecord.PanneauxRef;
import activeRecord.Relation;

/**
 * Tests des differentes methodes de la la classe Keypoints
 * 
 * @author Leo Lemaitre
 *
 */
public class TestDB {

	// Les differentes variables que nous allons utiliser dans nos tests

	/**
	 * liste de Keypointss
	 */
	ArrayList<KeyPoints> listeKeypoints;

	ArrayList<Relation> listeRelations;

	/**
	 * liste de PanneauxRef
	 */
	ArrayList<PanneauxRef> listPanneauxRef;

	/**
	 * objet permettant de se connecter a la base de donnees
	 */
	Connection c1;

	/**
	 * les differents realisateurs
	 */
	PanneauxRef p1, p2, p3;

	/**
	 * les differents Keypointss
	 */
	KeyPoints f1, f2, f3, f4, f5;

	Relation r1, r2, r3;

	/**
	 * Ce qui sera execute avant chaques tests, cela initialisera les tables
	 * personne et Keypoints en inserant differents tuples
	 * 
	 * @throws SQLException
	 *             si il y a un probleme d'acces a la base de donnees
	 * @throws RealisateurAbsentException
	 *             si un Keypoints n'a pas de realisateur
	 */
	@Before
	public void avant() throws SQLException {

		KeyPoints.createTable();
		PanneauxRef.createTable();
		Relation.createTable();

		p1 = new PanneauxRef("110");
		p1.save();
		p2 = new PanneauxRef("30");
		p2.save();
		listPanneauxRef = (ArrayList<PanneauxRef>) PanneauxRef.findAll();
		float x, y, size, angle, response, octave;
		x = (float) 607.0;
		y = (float) 524.0;
		size = (float) 31.0;
		angle = (float) 196.014;
		response = (float) 0.09898;
		octave = (float) 0.0454;
		
		f1 = new KeyPoints(x, y, size, angle, response, octave);
		f1.save();
		/*
		 * f2 = new KeyPoints(510); f2.save(); f3=new KeyPoints(1000);
		 * f3.save();
		 */
		listeKeypoints = (ArrayList<KeyPoints>) KeyPoints.findAll();
		/*
		 * r1=new Relation(p1.getId(), f1.getIdKeyPoint()); r1.save(); r2=new
		 * Relation(p2.getId(), f2.getIdKeyPoint()); r2.save(); r3=new
		 * Relation(p1.getId(), f3.getIdKeyPoint()); r3.save(); listeRelations =
		 * (ArrayList<Relation>) Relation.findAll();
		 * 
		 * System.out.println("TEST"); listeKeypoints = (ArrayList<KeyPoints>)
		 * KeyPoints.findByNomPanneau("110");
		 */
	}

	/**
	 * Ce qui sera execute a la fin de chaques tests
	 * 
	 * @throws SQLException
	 *             si il y a un probleme d'acces a la base de donnees
	 */

	@After
	public void apres() throws SQLException {
		Relation.deleteTable();

		KeyPoints.deleteTable();
		PanneauxRef.deleteTable();
	}

	/**
	 * Test de la methode findAll, on verifie que la liste retournee contient
	 * tout les Keypointss ajoutes
	 * 
	 * @throws SQLException
	 *             si il y a un probleme d'acces a la base de donnees
	 */
	@Test
	public void test_findAll_nombreKeypoints() throws SQLException {
		KeyPoints keyPointDB = listeKeypoints.get(0);
		KeyPoint test = new KeyPoint(keyPointDB.getX(), keyPointDB.getY(), keyPointDB.getSize(), keyPointDB.getAngle(),
				keyPointDB.getResponse());
		System.out.println(test);
		assertEquals("il devrait y avoir 1 Keypoints dans la table Keypoints", 1, listeKeypoints.size());

	}

}
