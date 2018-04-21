package alde.commons.console;

import java.awt.BorderLayout;
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

public class Console implements ImputListener {

	public static Console console;

	private static ArrayList<ConsoleAction> actions = new ArrayList<>();

	static org.slf4j.Logger log = LoggerFactory.getLogger(Console.class);

	private static final AutoCompleteService autoComplete = new AutoCompleteService();
	private final static JPanel consoleImputPanel = new ConsoleInputPanel(autoComplete, getConsole());

	private Console() {
	}

	public static final Console getConsole() {

		if (console == null) {
			console = new Console();

			Console.addListener(new HelpAction(actions));

		}

		return console;
	}

	public static JPanel getConsoleImputPanel() {
		return consoleImputPanel;
	}

	/**
	 * Receive input from the consoleImputPanel
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
			log.error("No action found for imput '" + command + "'.");
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

	AutoCompleteMemoryJTextField imputField;

	public ConsoleInputPanel(AutoCompleteService s, final ImputListener imputListener) {

		setLayout(new BorderLayout());

		imputField = new AutoCompleteMemoryJTextField(s);

		imputField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!StringUtils.isAllBlank(imputField.getText())) {

					imputListener.receive(imputField.getText());

					imputField.remember(imputField.getText());
					imputField.setText("");

				}

			}
		});

		imputField.setFont(getFont().deriveFont(Font.BOLD));

		add(imputField, BorderLayout.CENTER);

	}

}

interface ImputListener {
	public void receive(String s);
}
