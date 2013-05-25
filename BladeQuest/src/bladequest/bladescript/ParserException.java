package bladequest.bladescript;

public class ParserException extends Exception {

	public String whatStr;
	ParserException(String whatStr)
	{
		this.whatStr = whatStr;
	}
	public String what()
	{
		return this.whatStr;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1491086446057386403L;

}
