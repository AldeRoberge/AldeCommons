package alde.commons.console;

/**
 * ConsoleAction defines an action to be performed when the user inputs text including a trigger
 * 
 *If the user types 'help' in LoggerUI's ConsoleInputPanel, the HelpAction in Console will be triggered.
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
	 * Returns the description of the keyword
	 * 
	 * Used by the default Help console keyword
	 * 
	 * @return simple description of parameters
	 */
	public abstract String getDescription();

	/**
	 * Returns the keyword used to trigger this action
	 * 
	 * @return the keyword
	 */
	public abstract String getKeyword();

}
