package src.activeRecord;

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

	private float x, y, size, angle, response, octave;

	public KeyPoints(float x, float y,  float size, float angle, float response, float octave) {
		this.idKeyPoint = -1;
		this.x = x;
		this.y=y;
		this.size=size;
		this.angle=angle;
		this.response=response;
		this.octave=octave;
	}

	// POUR CREER UN OBJET A PARTIR DES RESULTATS D'UNEE REQUETE

	private KeyPoints(int id, float x, float y,  float size, float angle, float response, float octave) {
		this.idKeyPoint = id;
		this.x = x;
		this.y=y;
		this.size=size;
		this.angle=angle;
		this.response=response;
		this.octave=octave;
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
			float x = rs.getFloat("x");
			float y = rs.getFloat("y");
			float size = rs.getFloat("size");
			float angle = rs.getFloat("angle");
			float response = rs.getFloat("response");
			float octave = rs.getFloat("octave");
			f = new KeyPoints(id, x, y, size, angle, response, octave);
			result.add(f);
			System.out.println("  -> (" + id + ") " +"Coordonnées: "+ x+";"+y +" size: "+size+" angle: "+angle+" response: "+response+" octave: "+octave);
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
			float x = rs.getFloat("x");
			float y = rs.getFloat("y");
			float size = rs.getFloat("size");
			float angle = rs.getFloat("angle");
			float response = rs.getFloat("response");
			float octave = rs.getFloat("octave");
			f = new KeyPoints(x, y, size, angle, response, octave);
			result.add(f);
			System.out.println("  -> (" + id + ") " +"Coordonnées: "+ x+";"+y +" size: "+size+" angle: "+angle+" response: "+response+" octave: "+octave);
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
				+ "x float NULL, " + "y float NULL, "+"size float NULL, "+ "angle float NULL, "+ 
				"response float NULL, "+"octave float NULL, "+"PRIMARY KEY (IDKeyPoints))";
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
		prep.setFloat(1, this.x);
		prep.setFloat(2, this.y);
		prep.setFloat(3, this.size);
		prep.setFloat(4, this.angle);
		prep.setFloat(5, this.response);
		prep.setFloat(6, this.octave);
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
		String SQLPrep = "INSERT INTO Keypoints (x,y,size,angle,response,octave) VALUES (?,?,?,?,?,?);";
		PreparedStatement prep = connect.prepareStatement(SQLPrep, Statement.RETURN_GENERATED_KEYS);
		prep.setFloat(1, this.x);
		prep.setFloat(2, this.y);
		prep.setFloat(3, this.size);
		prep.setFloat(4, this.angle);
		prep.setFloat(5, this.response);
		prep.setFloat(6, this.octave);
		prep.executeUpdate();
		// recuperation de la derniere ligne ajoutee (auto increment)
		// recupere le nouvel id
		int autoInc = -1;
		ResultSet rs = prep.getGeneratedKeys();
		if (rs.next()) {
			autoInc = rs.getInt(1);
		}
		this.setIdKeyPoint(autoInc);
		System.out.print("  ->  id utilise lors de l'ajout de KeyPoint: ");
		System.out.println(autoInc);
		System.out.println();

	}

	public int getIdKeyPoint() {
		return idKeyPoint;
	}

	public void setIdKeyPoint(int idKeyPoint) {
		this.idKeyPoint = idKeyPoint;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getResponse() {
		return response;
	}

	public void setResponse(float response) {
		this.response = response;
	}

	public float getOctave() {
		return octave;
	}

	public void setOctave(float octave) {
		this.octave = octave;
	}


}
