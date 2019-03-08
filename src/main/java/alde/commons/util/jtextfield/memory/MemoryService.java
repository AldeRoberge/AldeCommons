package alde.commons.util.jtextfield.memory;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Works with JTextField and JPasswordField
 */
public class MemoryService {

    /**
     * List of previous inputs
     */
    private List<String> previousInputs = new ArrayList<String>();

    /**
     * Used to navigate between previous inputs
     */
    private int currentIndex = 0;

    public void setField(JTextField memoryJTextField) {

        memoryJTextField.addActionListener(e -> {
            previousInputs.add(memoryJTextField.getText());
            currentIndex = previousInputs.size();
        });

        memoryJTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { // Left blank intentionally
            }

            @Override
            public void keyPressed(KeyEvent e) { // Left blank intentionally
            }

            @Override
            public void keyReleased(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    setIndex(+1);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    setIndex(-1);
                }
            }

            private void setIndex(int i) {

                currentIndex -= i;

                if (currentIndex < 0) { // Minimum
                    currentIndex = 0;
                }

                if (currentIndex > previousInputs.size() - 1) { // Maximum
                    currentIndex = previousInputs.size() - 1;
                }

                if (!(previousInputs.size() == 0)) {
                    memoryJTextField.setText(previousInputs.get(currentIndex));
                }

            }


        });


    }
}
