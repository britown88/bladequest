package bladequest.scripting;

public class ParserException extends Exception {

	public String what;
	ParserException(String what)
	{
		this.what = what;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1491086446057386403L;

}
