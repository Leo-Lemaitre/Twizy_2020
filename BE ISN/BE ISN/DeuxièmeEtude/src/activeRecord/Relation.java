package src.activeRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe du patron active record correspondant à la table Relation de notre
 * base de donnees
 * 
 * @author Leo Lemaitre
 *
 */
public class Relation {

	private int idPanneau;
	private int idKeyPoint;

	/**
	 * Construit un objet Personne en l'initialisant avec un nom donne, un
	 * prenom donne et un id de -1
	 * 
	 * @param n
	 *            correspond au nom du realisateur
	 * @param p
	 *            correspond au prenom du realisateur
	 */
	public Relation(int idPanneau, int idKeyPoint) {
		this.idPanneau = idPanneau;
		this.idKeyPoint = idKeyPoint;
	}

	/**
	 * methode static, elle permet de connaitre toutes les Relations de la base
	 * de donnees
	 * 
	 * @return retourne la liste des Relations de la base de donnees
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 */
	public static ArrayList<Relation> findAll() throws SQLException {
		ArrayList<Relation> result = new ArrayList<Relation>();
		Connection connect = DBConnection.getInstance().getConnection();
		System.out.println("4) Recupere les Relation de la table Relation");
		String SQLPrep = "SELECT * FROM Relation;";
		PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
		prep1.execute();
		ResultSet rs = prep1.getResultSet();
		// s'il y a un resultat
		Relation p;
		while (rs.next()) {
			int idPanneau = rs.getInt("PanneauxRefs_idPanneau");
			int idKeyPoint = rs.getInt("KeyPoints_idKey_points");
			p = new Relation(idPanneau, idKeyPoint);
			result.add(p);
			System.out.println("  -> (" + idPanneau + ") " + idKeyPoint);
		}
		System.out.println();
		return result;
	}

	/**
	 * Methode qui permet de creer la table Relation dans la base de donnees
	 * 
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 */
	public static void createTable() throws SQLException {
		Connection connect = DBConnection.getInstance().getConnection();
		String createString = "CREATE TABLE IF NOT EXISTS Relation ( KeyPoints_idKey_points INT NOT NULL, "
				+ "PanneauxRefs_idPanneau INT NOT NULL, INDEX fk_Relation_KeyPoints_idx (KeyPoints_idKey_points ASC),"
				+ " INDEX fk_Relation_PanneauxRefs1_idx (PanneauxRefs_idPanneau ASC), PRIMARY KEY (KeyPoints_idKey_points, PanneauxRefs_idPanneau), "
				+ "CONSTRAINT fk_Relation_KeyPoints FOREIGN KEY (KeyPoints_idKey_points) REFERENCES Twizy.KeyPoints (idKeyPoints) ON DELETE NO ACTION "
				+ "ON UPDATE NO ACTION, CONSTRAINT fk_Relation_PanneauxRefs1 FOREIGN KEY (PanneauxRefs_idPanneau) REFERENCES Twizy.PanneauxRefs (idPanneau)"
				+ " ON DELETE NO ACTION ON UPDATE NO ACTION)";
		Statement stmt = connect.createStatement();
		stmt.executeUpdate(createString);
		System.out.println(" creation table Relation\n");
	}

	/**
	 * Methode qui permet de supprimer la table Relation dans la base de donnees
	 * 
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 */
	public static void deleteTable() throws SQLException {
		Connection connect = DBConnection.getInstance().getConnection();
		String drop = "DROP TABLE Relation";
		Statement stmt = connect.createStatement();
		stmt.executeUpdate(drop);
		System.out.println("Supprime table Relation");
	}

	/**
	 * Methode qui permet une mise a jour de la base de donnees en inserant ou
	 * modifiant un tuple
	 * 
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 */
	public void save() throws SQLException {

		this.saveNew();

	}

	/**
	 * methode qui permet l'insertion d'un tuple dans la base de donnees
	 * 
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 */
	private void saveNew() throws SQLException {
		Connection connect = DBConnection.getInstance().getConnection();
		String SQLPrep = "INSERT INTO Relation (PanneauxRefs_idPanneau,KeyPoints_idKey_points) VALUES (?,?);";
		PreparedStatement prep = connect.prepareStatement(SQLPrep, Statement.RETURN_GENERATED_KEYS);
		prep.setInt(1, this.idPanneau);
		prep.setInt(2, this.idKeyPoint);
		prep.executeUpdate();

	}

}
