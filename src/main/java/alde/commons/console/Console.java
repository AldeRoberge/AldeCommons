package alde.commons.console;

import alde.commons.util.jtextfield.UtilityJTextField;
import alde.commons.util.math.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Console extends UtilityJTextField {

    private static Logger log = LoggerFactory.getLogger(Console.class);

    /**
     * Hint on the JTextField (disappears on input)
     */
    private static String HINT = "Enter command here";

    /**
     * List of actions
     */
    private static final List<ConsoleAction> actions = new ArrayList<>();

    public Console() {
        super(HINT);

        setFont(getFont().deriveFont(Font.BOLD));
        setBackground(Color.WHITE);
        setForeground(new Color(29, 29, 29)); // Very dark gray
        setCaretColor(Color.WHITE);

        Border line = BorderFactory.createLineBorder(Color.DARK_GRAY);
        Border empty = new EmptyBorder(5, 5, 5, 0);
        CompoundBorder border = new CompoundBorder(line, empty);
        setBorder(border);

        addAction(new HelpAction(actions));

        /*
         * Receive input
         */
        addActionListener(actionEvent -> {

            String command = getText();

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

            /*
             * Use levenshtein to get the closest match to the user command.
             * If the closest match is too far away, do not suggest anything.
             */
            if (!accepted) {

                log.error("No action found for command '" + command + "'.");

                int closest = Integer.MAX_VALUE;
                String suggestion = "";

                for (ConsoleAction c : actions) {

                    for (String keyword : c.getKeywords()) {
                        int distance = LevenshteinDistance.computeLevenshteinDistance(keyword, command);

                        if (distance < closest) {
                            closest = distance;
                            suggestion = keyword;
                        }
                    }

                }

                if (closest <= 3) {
                    log.info("Did you mean '" + suggestion + "'?");
                }
            }
            
            setText("");
            
        });


    }

    /**
     * Use this static method to addAction listeners.
     * <p>
     * Console.addAction(ConsoleAction)
     * <p>
     * For an example of implementation, @see HelpAction
     */
    public void addAction(ConsoleAction c) {

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

        // Adds the keyWords of the action to the autoCompleteService
        addData(c.getKeywords());
        actions.add(c);

    }

}

/**
 * ConsoleAction that shows a list of all available ConsoleActions
 */
class HelpAction extends ConsoleAction {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(HelpAction.class);

    private List<ConsoleAction> consoleActions = new ArrayList<ConsoleAction>();

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
        return "Shows this message.";
    }

    @Override
    public String[] getKeywords() {
        return new String[]{"help"};
    }

}