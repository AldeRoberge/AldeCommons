package alde.commons.console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import alde.commons.util.autoComplete.jtextfield.AutoCompleteInputReceiver;
import alde.commons.util.autoComplete.jtextfield.AutoCompleteMemoryJTextField;
import alde.commons.util.autoComplete.jtextfield.AutoCompleteService;
import alde.commons.util.math.LevenshteinDistance;

public class Console implements InputListener {

	public static Console console;

	private static ArrayList<ConsoleAction> actions = new ArrayList<>();

	static org.slf4j.Logger log = LoggerFactory.getLogger(Console.class);

	private static final AutoCompleteService autoComplete = new AutoCompleteService();
	private final static JPanel consoleInputPanel = new ConsoleInputPanel(autoComplete, getConsole());

	private Console() {
	}

	private static Console getConsole() {

		if (console == null) {
			console = new Console();

			Console.addListener(new HelpAction(actions));

		}

		return console;
	}

	public static JPanel getConsoleInputPanel() {
		return consoleInputPanel;
	}

	/**
	 * Receive input from the consoleInputPanel
	 */
	public void receive(String command) {

		boolean accepted = false;

		for (ConsoleAction t : actions) {

			for (String s : t.getKeywords()) {
				if (command.contains(s)) {
					accepted = true;
					t.accept(command);

					break;
				}
			}
		}

		if (!accepted) {
			log.error("No action found for input '" + command + "'" + getSuggestion(command));
		}

	}

	private String getSuggestion(String imput) {

		int closest = 99999;
		String suggestion = "";

		for (ConsoleAction c : actions) {

			for (String keyword : c.getKeywords()) {
				int distance = LevenshteinDistance.computeLevenshteinDistance(keyword, imput);

				if (distance < closest) {
					closest = distance;
					suggestion = keyword;
				}
			}

		}

		if (closest <= 3) {
			return ", did you mean '" + suggestion + "'?";
		} else {
			return ".";
		}

	}

	public static void addListener(ConsoleAction c) {

		Iterator<ConsoleAction> it = actions.iterator();
		while (it.hasNext()) {

			ConsoleAction otherAction = it.next();

			for (String keyword : c.getKeywords()) {

				for (String otherKeyword : otherAction.getKeywords()) {
					if (otherKeyword.equalsIgnoreCase(keyword)) {
						log.error("New keyword '" + c.toString() + "' overrides other action '" + c.toString() + "'.");
						it.remove();
					}
				}
			}
		}

		autoComplete.addData(c.getKeywords());
		actions.add(c);

	}

}

// A ConsoleAction that shows a list of ConsoleAction (Help)
class HelpAction extends ConsoleAction {

	static org.slf4j.Logger log = LoggerFactory.getLogger(HelpAction.class);

	List<ConsoleAction> consoleActions = new ArrayList<ConsoleAction>();

	public HelpAction(List<ConsoleAction> consoleActions) {
		super();
		this.consoleActions = consoleActions;
	}

	@Override
	public void accept(String command) {
		for (ConsoleAction c : consoleActions) {
			log.info(c.toString());
		}
	}

	@Override
	public String getDescription() {
		return "Shows the help function";
	}

	@Override
	public String[] getKeywords() {
		return new String[] { "help" };
	}

}

class ConsoleInputPanel extends JPanel {

	org.slf4j.Logger log = LoggerFactory.getLogger(ConsoleInputPanel.class);

	AutoCompleteMemoryJTextField inputField;

	public ConsoleInputPanel(AutoCompleteService s, final InputListener inputListener) {

		setLayout(new BorderLayout());

		inputField = new AutoCompleteMemoryJTextField(s, new AutoCompleteInputReceiver() {

			@Override
			public void receive(String input) {
				inputListener.receive(inputField.getText());
			}

		}, "Insert command here");

		inputField.setFont(getFont().deriveFont(Font.BOLD));
		inputField.setBackground(Color.WHITE);
		inputField.setForeground(new Color(29, 29, 29)); // Very dark gray
		inputField.setCaretColor(Color.WHITE);

		Border line = BorderFactory.createLineBorder(Color.DARK_GRAY);
		Border empty = new EmptyBorder(5, 5, 5, 0);
		CompoundBorder border = new CompoundBorder(line, empty);
		inputField.setBorder(border);

		setBorder(null);

		add(inputField, BorderLayout.CENTER);

	}

}

interface InputListener {
	public void receive(String s);
}
