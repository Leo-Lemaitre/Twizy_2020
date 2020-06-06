package activeRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

/**
 * Classe qui suit le patron Singleton, elle s'occupe de la connexion a la base
 * de donnee Le patron Singleton permet de n'avoir qu'une seule connexion a la
 * base
 * 
 * @author Leo Lemaitre
 *
 */
public class DBConnection {

	/**
	 * objet qui permet de se connecter a la base de donnees
	 */
	private Connection connect;

	/**
	 * instance du patron singleton
	 */
	private static DBConnection instance;

	/**
	 * nom d'utilisateur pour se connecter a la base de donnees
	 */
	private static String userName = "root";

	/**
	 * mot de passe pour se connecter a la base de donnees
	 */
	private static String password = "";

	/**
	 * nom du serveur de la base de donnees
	 */
	private static String serverName = "localhost";

	/**
	 * numero de port pour communiquer a la base de donnees
	 */
	private static String portNumber = "3306";

	/**
	 * nom de la base de donnees
	 */
	private static String dbName = "Twizy";

	/**
	 * constructeur prive qui initialise l'attribut connect avec les differents
	 * attributs de la classe
	 * 
	 * @throws SQLException
	 *             si il ya une erreur d'acces a la base
	 */
	private DBConnection() throws SQLException {
		// creation de la connection
		Properties connectionProps = new Properties();
		connectionProps.put("user", userName);
		Scanner sc = new Scanner(System.in);
		if (password.equals("")) {
			System.out.println("Entrez votre mot de passe MySQL :");
			password = sc.next();
		}
		connectionProps.put("password", password);
		String urlDB = "jdbc:mysql://" + serverName + ":";
		urlDB += portNumber + "/" + dbName;
		connect = DriverManager.getConnection(urlDB, connectionProps);
	}

	/**
	 * getter de l'attribut connection qui permet de se connecter a la base de
	 * donnees
	 * 
	 * @return retourne un objet connection pour se connecter a la base de
	 *         donnees
	 */
	public Connection getConnection() {
		try {
			return getInstance().connect;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Methode qui permet d'obtenir l'unique instance de DBConnection
	 * 
	 * @return retourne l'unique instance de DBConnection
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 */
	public static synchronized DBConnection getInstance() throws SQLException {
		if (instance == null)
			instance = new DBConnection();
		return instance;
	}

	/**
	 * methode qui permet de modifier le nom de la base de donnees, de plus elle
	 * remet a null l'unique instance de la classe
	 * 
	 * @param nomDB
	 *            Nouveau nom de la base de donnees
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 */
	public static void setNomDB(String nomDB) throws SQLException {
		dbName = nomDB;
		if (instance != null) {
			DBConnection.instance.connect.close();
		}

		DBConnection.instance = null;
	}

	/**
	 * Methode equals pour vérifier l'égalité entre un objet et l'objet
	 * DBConnection courant
	 * 
	 * @param obj
	 *            objet dont on veut vérifier l'égalité
	 * @return retourne vraie si les deux objets sont égaux
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DBConnection other = (DBConnection) obj;
		if (connect == null) {
			if (other.connect != null)
				return false;
		} else if (!connect.equals(other.connect))
			return false;
		return true;
	}

	public static String getDbName() {
		return dbName;
	}

	public static void setDbName(String dbName) {
		DBConnection.dbName = dbName;
	}

}
