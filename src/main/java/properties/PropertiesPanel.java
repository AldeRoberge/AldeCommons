package properties;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class PropertiesPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public PropertiesPanel(ArrayList<Property> properties) {
		setLayout(new BorderLayout(0, 0));

		JScrollPane propertyScrollPane = new JScrollPane();
		add(propertyScrollPane, BorderLayout.CENTER);

		JPanel propertyPanel = new JPanel();
		propertyScrollPane.setViewportView(propertyPanel);

		propertyPanel.setLayout(new BoxLayout(propertyPanel, BoxLayout.Y_AXIS));

		for (Property p : properties) {
			propertyPanel.add(p.getEditPropertyPanel());
		}

	}

}
