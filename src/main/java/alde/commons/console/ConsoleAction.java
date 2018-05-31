package alde.commons.console;

/**
 * ConsoleAction defines an action to be performed when the user inputs text including a trigger
 * 
 * If the user types 'help' in Console's input panel, the HelpAction in Console will be triggered.
 * 
 * @see Console.HelpAction for an example
 *
 */
public abstract class ConsoleAction {

	/**
	 * Triggered : accept user input (contains trigger and arguments)
	 */
	public abstract void accept(String command);

	/**
	 * Returns a description of the action as a basic phrase that ends with a dot.
	 * 
	 */
	public abstract String getDescription();

	/**
	 * @return the possible keywords that can trigger this action
	 */
	public abstract String[] getKeywords();

	@Override
	public String toString() {
		return getKeywordsAsString() + " : " + getDescription();
	}

	public String getKeywordsAsString() {
		StringBuilder a = new StringBuilder();

		String keywords[] = getKeywords();

		for (int i = 0; i < keywords.length; i++) {

			if (i != keywords.length - 1) {
				a.append(keywords[i]).append(", ");
			} else {
				a.append(keywords[i]);
			}
		}

		return a.toString();

	}

}
