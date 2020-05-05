package activeRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe du patron active record correspondant à la table KeyPoints de notre
 * base de donnees
 * 
 * @author Leo Lemaitre
 *
 */
public class KeyPoints {

	private int idKeyPoint;

	private int Keypoint;

	public KeyPoints(int k) {
		this.idKeyPoint = -1;
		this.Keypoint = k;
	}

	// POUR CREER UN OBJET A PARTIR DES RESULTATS D'UNEE REQUETE

	private KeyPoints(int id, int k) {
		this.idKeyPoint = id;
		this.Keypoint = k;
	}

	/**
	 * methode static, elle permet de connaitre tout les films de la base de
	 * donnees
	 */
	public static List<KeyPoints> findAll() throws SQLException {
		List<KeyPoints> result = new ArrayList<KeyPoints>();
		Connection connect = DBConnection.getInstance().getConnection();
		System.out.println(" Recupere les keyPoints de la table KeyPoints ");
		String SQLPrep = "SELECT * FROM KeyPoints;";
		PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
		prep1.execute();
		ResultSet rs = prep1.getResultSet();
		// s'il y a un resultat
		KeyPoints f;
		while (rs.next()) {
			int id = rs.getInt("idKeyPoints");
			int val = rs.getInt("Keypoint");
			f = new KeyPoints(id, val);
			result.add(f);
			System.out.println("  -> (" + id + ") " + val);
		}
		System.out.println();
		return result;
	}

	
	public static List<KeyPoints> findByNomPanneau(String nomPanneau) throws SQLException {
		List<KeyPoints> result = new ArrayList<KeyPoints>();
		Connection connect = DBConnection.getInstance().getConnection();
		String SQLPrep = "SELECT * FROM KeyPoints INNER JOIN Relation ON KeyPoints.idKeyPoints = Relation.KeyPoints_idKey_points INNER JOIN Panneauxrefs ON Relation.PanneauxRefs_idPanneau=Panneauxrefs.idPanneau where nomPanneau='"+nomPanneau+"';";
		PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
		prep1.execute();
		ResultSet rs = prep1.getResultSet();
		// s'il y a un resultat
		KeyPoints f;
		while (rs.next()) {
			int id = rs.getInt("idKeyPoints");
			int val = rs.getInt("Keypoint");
			f = new KeyPoints(id, val);
			result.add(f);
			System.out.println("  -> (" + id + ") " + val);
		}
		System.out.println();
		return result;
	}
	/**
	 * Methode qui permet de creer la table film dans la base de donnees
	 * 
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 */
	public static void createTable() throws SQLException {
		Connection connect = DBConnection.getInstance().getConnection();
		String createString = "CREATE TABLE IF NOT EXISTS KeyPoints ( " + "IDKeyPoints INTEGER  AUTO_INCREMENT, "
				+ "KeyPoint INT NOT NULL, " + "PRIMARY KEY (IDKeyPoints))";
		Statement stmt = connect.createStatement();
		stmt.executeUpdate(createString);
		System.out.println("creation table KeyPoints\n");
	}

	/**
	 * Methode qui permet de supprimer la table film dans la base de donnees
	 * 
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 */
	public static void deleteTable() throws SQLException {
		Connection connect = DBConnection.getInstance().getConnection();
		String drop = "DROP TABLE KeyPoints";
		Statement stmt = connect.createStatement();
		stmt.executeUpdate(drop);
		System.out.println(" Supprime table KeyPoints");
	}

	/**
	 * methode qui permet de supprimer l'objet qui appelle la methode de la base
	 * de donnees
	 * 
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 * @throws DeleteException
	 *             si le delete ne peut pas etre effectue : id egal a -1 ou trop
	 *             grand
	 */
	public void delete() throws SQLException, DeleteException {
		Connection connect = DBConnection.getInstance().getConnection();
		if (getIdKeyPoint() == -1 || getIdKeyPoint() > findAll().size()) {
			throw new DeleteException("Le keypoint n'est pas dans la table");
		}
		PreparedStatement prep = connect.prepareStatement("DELETE FROM keypoints WHERE id=?");
		prep.setInt(1, this.getIdKeyPoint());
		prep.execute();
		System.out.println("5) Suppression du keypoint" + this);
		this.setIdKeyPoint(-1);
	}

	/**
	 * Methode qui permet une mise a jour de la base de donnees en inserant ou
	 * modifiant un tuple
	 * 
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 * @throws RealisateurAbsentException
	 *             si le film n'a pas de realisateur
	 */
	public void save() throws SQLException{
		if (this.idKeyPoint == -1) {
			this.saveNew();
		} else {
			this.update();

		}
	}

	private void update() throws SQLException {
		Connection connect = DBConnection.getInstance().getConnection();
		String SQLprep = "update KEYPOINTS set keypoint=? where id=" + this.idKeyPoint;
		PreparedStatement prep = connect.prepareStatement(SQLprep);
		prep.setInt(1, this.Keypoint);
		prep.execute();
		System.out.println("Met à jour les informations du Keypoint " + this);
		System.out.println();

	}

	/**
	 * methode qui permet l'insertion d'un tuple dans la base de donnees
	 * 
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 */
	private void saveNew() throws SQLException {
		Connection connect = DBConnection.getInstance().getConnection();
		String SQLPrep = "INSERT INTO Keypoints (Keypoint) VALUES (?);";
		PreparedStatement prep = connect.prepareStatement(SQLPrep, Statement.RETURN_GENERATED_KEYS);
		prep.setInt(1, this.Keypoint);
		prep.executeUpdate();
		// recuperation de la derniere ligne ajoutee (auto increment)
		// recupere le nouvel id
		int autoInc = -1;
		ResultSet rs = prep.getGeneratedKeys();
		if (rs.next()) {
			autoInc = rs.getInt(1);
		}
		this.setIdKeyPoint(autoInc);
		System.out.print("  ->  id utilise lors de l'ajout : ");
		System.out.println(autoInc);
		System.out.println();

	}

	public int getIdKeyPoint() {
		return idKeyPoint;
	}

	public void setIdKeyPoint(int idKeyPoint) {
		this.idKeyPoint = idKeyPoint;
	}

	public int getKeypoint() {
		return Keypoint;
	}

	public void setKeypoint(int keypoint) {
		Keypoint = keypoint;
	}

}
