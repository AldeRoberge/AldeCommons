package alde.commons.console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import alde.commons.util.autoComplete.jtextfield.AutoCompleteMemoryJTextField;
import alde.commons.util.autoComplete.jtextfield.AutoCompleteService;

public class Console implements InputListener {

	public static Console console;

	private static ArrayList<ConsoleAction> actions = new ArrayList<>();

	static org.slf4j.Logger log = LoggerFactory.getLogger(Console.class);

	private static final AutoCompleteService autoComplete = new AutoCompleteService();
	private final static JPanel consoleInputPanel = new ConsoleInputPanel(autoComplete, getConsole());

	private Console() {
	}

	public static final Console getConsole() {

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
	@Override
	public void receive(String command) {

		boolean accepted = false;

		for (ConsoleAction t : actions) {
			if (command.contains(t.getKeyword())) {
				accepted = true;
				t.accept(command);
			}
		}

		if (!accepted) {
			log.error("No action found for input '" + command + "'.");
		}

	}

	public static void addListener(ConsoleAction c) {

		Iterator<ConsoleAction> it = actions.iterator();
		while (it.hasNext()) {

			ConsoleAction otherAction = it.next();

			if (otherAction.getKeyword().equalsIgnoreCase(c.getKeyword())) {
				log.error("New keyword '" + c.getKeyword() + "' overrides other action with description "
						+ otherAction.getDescription());

				it.remove();
			}
		}

		autoComplete.addData(c.getKeyword());
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
			log.info(c.getKeyword() + " : " + c.getDescription());
		}
	}

	@Override
	public String getDescription() {
		return "Shows the help function";
	}

	@Override
	public String getKeyword() {
		return "help";
	}

}

class ConsoleInputPanel extends JPanel {

	org.slf4j.Logger log = LoggerFactory.getLogger(ConsoleInputPanel.class);

	Color backgroundColor = new Color(250, 170, 0);

	AutoCompleteMemoryJTextField inputField;

	public ConsoleInputPanel(AutoCompleteService s, final InputListener inputListener) {

		setLayout(new BorderLayout());

		inputField = new AutoCompleteMemoryJTextField(s);

		inputField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!StringUtils.isAllBlank(inputField.getText())) {

					inputListener.receive(inputField.getText());

					inputField.remember(inputField.getText());
					inputField.setText("");

				}

			}
		});

		inputField.setFont(getFont().deriveFont(Font.BOLD));
		inputField.setBackground(backgroundColor);
		inputField.setForeground(Color.WHITE);
		inputField.setCaretColor(Color.WHITE);

		add(inputField, BorderLayout.CENTER);

	}

}

interface InputListener {
	public void receive(String s);
}
