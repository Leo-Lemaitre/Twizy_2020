package src.activeRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe du patron active record correspondant à la table PanneauxRefs de notre
 * base de donnees
 * 
 * @author Leo Lemaitre
 *
 */
public class PanneauxRef {

	/**
	 * id du realisateur
	 */
	private int idPanneau;

	/**
	 * nom du realisateur
	 */
	private String nomPanneau;

	/**
	 * Construit un objet Personne en l'initialisant avec un nom donne, un
	 * prenom donne et un id de -1
	 * 
	 * @param n
	 *            correspond au nom du realisateur
	 * @param p
	 *            correspond au prenom du realisateur
	 */
	public PanneauxRef(String nomPanneau) {
		this.nomPanneau = nomPanneau;
		this.idPanneau = -1;
	}

	/**
	 * methode static, elle permet de connaitre toutes les PanneauxRefss de la
	 * base de donnees
	 * 
	 * @return retourne la liste des PanneauxRefss de la base de donnees
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 */
	public static ArrayList<PanneauxRef> findAll() throws SQLException {
		ArrayList<PanneauxRef> result = new ArrayList<PanneauxRef>();
		Connection connect = DBConnection.getInstance().getConnection();
		System.out.println("4) Recupere les panneaux de la table PanneauxRefs");
		String SQLPrep = "SELECT * FROM PanneauxRefs;";
		PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
		prep1.execute();
		ResultSet rs = prep1.getResultSet();
		// s'il y a un resultat
		PanneauxRef p;
		while (rs.next()) {
			String nom = rs.getString("nomPanneau");
			int id = rs.getInt("idPanneau");
			p = new PanneauxRef("nomPanneau");
			p.setId(id);
			result.add(p);
			System.out.println("  -> (" + id + ") " + nom);
		}
		System.out.println();
		return result;
	}

	@Override
	public String toString() {
		return "PanneauxRef [idPanneau=" + idPanneau + ", nomPanneau=" + nomPanneau + "]";
	}

	// On compare tous les attributs sauf les ids
	/**
	 * Methode qui compare deux objets PanneauxRefs en comparant leurs noms
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PanneauxRef other = (PanneauxRef) obj;
		if (nomPanneau == null) {
			if (other.nomPanneau != null)
				return false;
		} else if (!nomPanneau.equals(other.nomPanneau))
			return false;
		return true;
	}

	/**
	 * methode static, elle permet de connaitre la PanneauxRefs ayant un certain
	 * nom
	 * 
	 * @param nom
	 *            correspond au nom du panneau que l'on cherche
	 * @return retourne la PanneauxRefs de la base de donnees
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 */
	public static ArrayList<PanneauxRef> findByName(String nom) throws SQLException {
		Connection connect = DBConnection.getInstance().getConnection();
		System.out.println("7.1) Recupere les PanneauxRefs de la table PanneauxRefs ayant le nom " + nom);
		String SQLPrep = "SELECT * FROM PanneauxRefs WHERE nom=?";
		PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
		prep1.setString(1, nom);
		prep1.execute();
		ResultSet rs = prep1.getResultSet();
		ArrayList<PanneauxRef> list = new ArrayList<PanneauxRef>();
		PanneauxRef p = null;
		while (rs.next()) {
			String name = rs.getString("nomPanneau");
			int ids = rs.getInt("idPanneau");
			p = new PanneauxRef(name);
			p.setId(ids);
			list.add(p);
			System.out.println("  -> (" + ids + ") " + nom);
		}
		return list;
	}

	/**
	 * Methode qui permet de creer la table PanneauxRefs dans la base de donnees
	 * 
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 */
	public static void createTable() throws SQLException {
		Connection connect = DBConnection.getInstance().getConnection();
		String createString = "CREATE TABLE IF NOT EXISTS Twizy.PanneauxRefs ("
				+ " idPanneau INT NOT NULL AUTO_INCREMENT, nomPanneau VARCHAR(45) NULL,"
				+ " PRIMARY KEY (idPanneau))";
		Statement stmt = connect.createStatement();
		stmt.executeUpdate(createString);
		System.out.println("1) creation table PanneauxRefs\n");
	}

	/**
	 * Methode qui permet de supprimer la table PanneauxRefs dans la base de
	 * donnees
	 * 
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 */
	public static void deleteTable() throws SQLException {
		Connection connect = DBConnection.getInstance().getConnection();
		String drop = "DROP TABLE PanneauxRefs";
		Statement stmt = connect.createStatement();
		stmt.executeUpdate(drop);
		System.out.println("Supprime table PanneauxRefs");
	}



	/**
	 * Methode qui permet une mise a jour de la base de donnees en inserant ou
	 * modifiant un tuple
	 * 
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 */
	public void save() throws SQLException {
		if (this.getId() == -1) {
			this.saveNew();
		} else {
			this.update();
		}
	}

	/**
	 * methode qui permet la mise a jour d'un tuple dans la base de donnees
	 * 
	 * @throws SQLException
	 *             si il y a une erreur d'acces a la base
	 */
	private void update() throws SQLException {
		Connection connect = DBConnection.getInstance().getConnection();
		String SQLprep = "update Panneau set nomPanneau=?, prenom=? where id=" + this.idPanneau;
		PreparedStatement prep = connect.prepareStatement(SQLprep);
		prep.setString(1, this.nomPanneau);
		prep.execute();
		System.out.println("7) Met à jour les informations du Panneau " + this);
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
		String SQLPrep = "INSERT INTO PanneauxRefs (nomPanneau) VALUES (?);";
		PreparedStatement prep = connect.prepareStatement(SQLPrep, Statement.RETURN_GENERATED_KEYS);
		prep.setString(1, this.nomPanneau);
		prep.executeUpdate();

		// recuperation de la derniere ligne ajoutee (auto increment)
		// recupere le nouvel id
		int autoInc = -1;
		ResultSet rs = prep.getGeneratedKeys();
		if (rs.next()) {
			autoInc = rs.getInt(1);
		}
		this.setId(autoInc);
		System.out.print("  ->  id utilise lors de l'ajout : ");
		System.out.println(autoInc);
		System.out.println();

	}

	public int getId() {
		return idPanneau;
	}

	public void setId(int id) {
		this.idPanneau = id;
	}

	public String getNom() {
		return nomPanneau;
	}

	public void setNom(String nom) {
		this.nomPanneau = nom;
	}

}
