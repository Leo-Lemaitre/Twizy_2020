package activeRecord;

/**
 * Classe qui permet d'instancier des exceptions lorsqu'il y a une exception au
 * niveau d'une deletion
 * 
 * @author Leo Lemaitre
 *
 */
public class DeleteException extends Exception {

	/**
	 * constructeur faisant appel au contructeur de Exception
	 * 
	 * @param s
	 *            message d'erreur a afficher
	 */
	public DeleteException(String s) {
		super(s);
	}
}
