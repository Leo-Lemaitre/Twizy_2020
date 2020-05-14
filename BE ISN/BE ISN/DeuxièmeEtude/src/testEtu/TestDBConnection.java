package testEtu;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import activeRecord.DBConnection;

/**
 * Classe de test de la classe DBConnection
 * 
 * @author Leo Lemaitre
 *
 */
public class TestDBConnection {

	/**
	 * Objet DBConnection sur lequel nous allons effectuer les tests
	 */
	DBConnection db;

	/**
	 * Objet connection sur lequel nous allons effectuer les tests
	 */
	Connection c1;

	/**
	 * Ce qui sera effectue avant chaque test
	 * 
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 */
	@Before
	public void avant() throws SQLException {
		db = DBConnection.getInstance();
		c1 = db.getConnection();

	}

	/**
	 * Permet de verifier qu'il n'y a qu'un seul objet connection
	 * 
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 */
	@Test
	public void test_Unique() throws SQLException {
		Connection c2 = db.getConnection();
		assertEquals("Les deux connexions devraient être les mêmes", c1, c2);
	}

	/**
	 * Permet de verifier que l'objet connection est bien un objet de la classe
	 * java.sql.Connection
	 * 
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 */
	@Test
	public void test_Connexion_Valide() throws SQLException {
		boolean res = c1 instanceof java.sql.Connection;
		assertTrue("La connexion devrait etre du type java.sql.Connection", res);

	}

	/**
	 * Permet de tester le changement de nom de la base de donnees. Ici nous ne créons pas de nouvelle base de donnees, nous utilisons une base deja disponible
	 * 
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 */
	@Test
	public void test_changementNomBase() throws SQLException {
		DBConnection.setNomDB("mysql");
		DBConnection db2 = DBConnection.getInstance();
		assertTrue("L'ancien objet DBConnection devrait etre different du nouveau", db != db2);
		assertTrue("Le nom de la base de l'objet DBConnection devrait être mysql", db2.getDbName() == "mysql");

		Connection c2 = db.getConnection();
		assertNotEquals("Les deux connexions devraient être différentes", c1, c2);

	}

}
