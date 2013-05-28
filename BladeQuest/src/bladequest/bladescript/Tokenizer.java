package bladequest.bladescript;

public interface Tokenizer {
	public ScriptToken getNextToken();
	public String getExceptionInfo();
}
