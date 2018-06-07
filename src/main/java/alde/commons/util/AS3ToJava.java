package alde.commons.util;

import alde.commons.util.text.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AS3ToJava extends StringUtils {

	private JFrame frame;

	JLabel infoButton;

	private static final boolean DEBUG = true;

	private int numberOfErrors = 0;
	private int numberOfLines = 0;

	private boolean hasAddedJavaImports = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					AS3ToJava window = new AS3ToJava();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AS3ToJava() {
		initialize();
	}

	private static void debug(String string) {
		if (DEBUG) {
			System.out.println(string);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		frame = new JFrame();

		try {
			frame.setIconImage(Toolkit.getDefaultToolkit()
					.getImage(AS3ToJava.class.getResource("/com/sun/javafx/scene/web/skin/Redo_16x16_JFX.png")));
		} catch (Exception e) {
			System.err.println("Error with icon image...");
		}

		frame.setTitle("AS3 to Java");

		frame.setBounds(100, 100, 948, 492);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setEnabled(false);
		splitPane.setDividerLocation(460);
		splitPane.setResizeWeight(0.5);
		panel.add(splitPane);

		JPanel leftPanel = new JPanel();
		splitPane.setLeftComponent(leftPanel);
		leftPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane leftScrollPane = new JScrollPane();
		leftPanel.add(leftScrollPane, BorderLayout.CENTER);

		JPanel rightPanel = new JPanel();
		splitPane.setRightComponent(rightPanel);
		rightPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane rightScrollPane = new JScrollPane();
		rightPanel.add(rightScrollPane, BorderLayout.CENTER);

		// Sync scroll between the two

		/**leftScrollPane.getHorizontalScrollBar().setModel(rightScrollPane.getHorizontalScrollBar().getModel());
		rightScrollPane.getVerticalScrollBar().setModel(leftScrollPane.getVerticalScrollBar().getModel());*/

		JTextArea rightTextArea = new JTextArea();
		rightTextArea.setEditable(false);
		rightScrollPane.setViewportView(rightTextArea);

		JTextArea leftTextArea = new JTextArea();
		leftTextArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				update();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				update();
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				update();
			}

			private void update() {
				rightTextArea.setText("");

				numberOfErrors = 0;
				numberOfLines = 0;

				for (String line : leftTextArea.getText().split(newLine)) {
					rightTextArea.append(staticParse(line) + newLine);
					updateInfo();

					System.out.println("Ohhh yeah");
				}
			}

		});
		leftScrollPane.setViewportView(leftTextArea);

		JPanel topLabelPanel = new JPanel();
		frame.getContentPane().add(topLabelPanel, BorderLayout.NORTH);
		topLabelPanel.setLayout(new BoxLayout(topLabelPanel, BoxLayout.X_AXIS));

		JPanel leftLabelPanel = new JPanel();
		topLabelPanel.add(leftLabelPanel);

		JLabel lblAS3 = new JLabel("AS3");
		leftLabelPanel.add(lblAS3);

		JPanel rightLabelPanel = new JPanel();
		topLabelPanel.add(rightLabelPanel);

		JLabel lblJava = new JLabel("Java");
		rightLabelPanel.add(lblJava);

		JPanel bottomPanel = new JPanel();
		panel.add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.setLayout(new BorderLayout(0, 0));

		infoButton = new JLabel("Lines : 0");
		bottomPanel.add(infoButton);
	}

	public void updateInfo() {
		infoButton.setText("Errors : " + numberOfErrors + ", Number of lines : " + numberOfLines);
	}

	public String staticParse(String line) {

		numberOfLines++;

		try {

			if (line.contains("package ")) { // remove package
				line = "";
			} else if (line.contains("import ")) { // remove imports

				line = "";

				if (!hasAddedJavaImports) {
					hasAddedJavaImports = true;

					line = "import java.util.ArrayList; " + newLine + " import java.util.List;";
				}
			} else {

				line = replace(line, "_:", ":");
				line = replace(line, "_;", ";");
				line = replace(line, "_ = ", " = ");
				line = replace(line, "_ ", " ");
				line = replace(line, "_.", ".");
				line = replace(line, "_(", "(");
				line = replace(line, "_)", ")");
				line = replace(line, "_[", "[");
				line = replace(line, "_]", "]");
				line = replace(line, "_,", ",");

				line = replace(line, " const ", " final ");
				line = replace(line, "uint", "int");
				line = replace(line, "Boolean", "boolean");

				line = replace(line, ".push(", ".add(");
				line = replace(line, "ByteArray", "byte[]");

				line = replace(line, "new String();", "\"\"");

				line = replace(line, "= NaN", "= 0");

				line = replace(line, "override ", " @Override ");

				if (line.contains("Vector.<")) {
					line = line.replace("Vector.<", "Vector<");
				}

				line = line.replace("Number", "double");
				line = line.replace("<Number>", "<Double>");

				line = replace(line, "<int>", "<Integer>");
				line = replace(line, "<float>", "<Float>");
				line = replace(line, "<double>", "<Double>");

				line = replace(line, "double.MAX_VALUE", "Double.MAX_VALUE");
				line = replace(line, "double.MIN_VALUE", "Double.MIN_VALUE");

				line = replace(line, "int.MAX_VALUE", "Integer.MAX_VALUE");
				line = replace(line, "int.MIN_VALUE", "Integer.MIN_VALUE");

				//

				line = line.replace(" is ", " instanceof ");

				//

				if (line.contains("for each") && line.contains(" in ")) {
					line = line.replace(" in ", " : ").replace("for each", "for");
				}
				
				line = line.replace("native ", " ");

				/*
				 * The 'in' keyword in Java does not exist. We'll use .contains instead
				 */
				if (line.contains(" in ")) {
					String dictionaryName = getFollowingWord(line, " in ");
					String varName = getPreviousWord(line, " in ");

					System.out.println(dictionaryName + ", " + varName);

					line = rotate(line, " in ", ".contains(");

					line = line.replace(varName, varName + ")");
				}

				//

				if (line.contains("<") && line.contains(">[")) { // Array declaration (switch on the haters)

					String type = line.substring(line.indexOf("<") + 1, line.indexOf(">"));

					line = replace(line, "[", "{");
					line = replace(line, "]", "}");

					line = replace(line, "<", "[");
					line = replace(line, ">", "]");

					if (line.contains("{")) {

						String squared = "[" + type + "]";

						line = replace(line, squared, ""); // Switch type before the [] (double[])
						line = replace(line, "{", type + "[]{");
					}

				}

				//dict = obj as Dictionary;
				//dict = (Dictionary) obj;

				if (line.contains(" as ")) {

					//bitmapData = obj as BitmapData;
					//bitmapData = obj as BitmapData;

					String type = getFollowingWord(line, " as ");

					System.out.println("Type : " + type);

					line = rotate(line, " as ", " "); // rotate type and object name and remove ' as '

					System.out.println(line);

					line = replace(line, type, "(" + type + ")"); // add parentheses

				}

				//Fields (public static var texture:BitmapData;)

				if (!line.contains("function") && line.contains(":") && !line.contains("{") && line.contains(";")
						&& !line.contains("?") && !line.contains("return")) { //? break this

					debug("Line : " + line + " treated as a field.");

					StringBuilder visibility = new StringBuilder();

					String nameAndType = "";

					String[] l2 = line.split(" ");

					boolean reachedNameAndType = false;

					for (String l : l2) {
						if (!l.contains(":") && !reachedNameAndType) {
							visibility.append(l).append(" ");
						} else {
							if (!reachedNameAndType) {
								reachedNameAndType = true;

								nameAndType = l;
							}
						}
					}

					String name = "";
					String type = "";

					name = nameAndType.substring(0, nameAndType.indexOf(":"));

					type = nameAndType.substring(nameAndType.indexOf(":") + 1);

					debug("Name  " + name + ", Type : " + type);

					if (type.contains(";")) {
						type = type.replace(";", "");
					}

					String restOfTheDeclaration = "";

					if (line.contains("=")) {
						restOfTheDeclaration = line.substring(line.indexOf(" = "));
					} else {
						restOfTheDeclaration = ";";
					}

					debug("FIELD : Visiblity : " + visibility + ", Type : " + type + ", Name : " + name + ", Rest : "
							+ restOfTheDeclaration);

					if (visibility.toString().contains("var ")) {
						visibility = new StringBuilder(visibility.toString().replace("var ", " "));
					}

					return visibility + type + " " + name + restOfTheDeclaration;

				}

				if (line.contains("function")) {
					line = treatFunction(line);
				}
			}
		} catch (Exception e) {
			numberOfErrors++;

			System.err.println("Error with line : " + line);
			e.printStackTrace();
		}

		return line;

	}

	// override public function drawShadow(graphicsData:Vector.<IGraphicsData>, camera:Camera, time:int) : void
	private String treatFunction(String line) {

		// Visibility

		String visibility = "";

		if (line.contains("public ")) {
			visibility = "public";
		} else if (line.contains("private ")) {
			visibility = "private";
		} else if (line.contains("protected ")) {
			visibility = "protected";
		}

		if (line.contains("static ")) {
			visibility = visibility + " static";
		}

		// Return type

		String returnType = "";

		if (!line.contains(") {") && line.contains("{")) { //is not constructor (has return type)
			if (line.contains("):")) {
				returnType = line.substring(line.indexOf("):") + 2, line.indexOf("{"));
			} else if (line.contains(") :")) {
				returnType = line.substring(line.indexOf(") :") + 3, line.indexOf("{"));
			}
		} else if (line.contains(" : ")) {
			returnType = line.substring(line.indexOf(" : ") + 3);
		}

		// Name

		String functionName = getInbetween(line, "function ", "(").replaceAll("\\s+$", ""); // get name and remove whitespace

		// Params

		List<Parameter> params = new ArrayList<>();
		boolean hasOptionalParams = false;

		if (!line.contains("()")) { // if it has parameters
			String paramLine = getInbetween(line, "(", ")");
			System.out.println("Param line : " + paramLine);

			for (String param : paramLine.split(", ")) {
				param = param.replace(",", "");

				Parameter parameter = new Parameter();

				parameter.name = getPreviousWord(param, ":");
				parameter.type = getFollowingWord(param, ":");

				if (param.contains(" = ")) {
					hasOptionalParams = true;
					parameter.isOptionalParameter = true;
					parameter.defaultValue = getPreviousWord(param, " = ");
				}

				params.add(parameter);

				System.out.println(parameter.toStringToString());
			}
		}

		System.out.println("Number of params found : " + params.size() + ", has optional params : " + hasOptionalParams);

		List<String> allParams = new ArrayList<>();
		List<String> onlyNonOptionalParams = new ArrayList<>();
		for (Parameter p : params) {

			allParams.add(p.toString());

			if (p.isOptionalParameter) {
				hasOptionalParams = true;
				onlyNonOptionalParams.add(p.toString());
			}
		}
		String nonOptionalParamLine = StringUtils.generateSeparatedStringFromStringList(onlyNonOptionalParams);

		System.out.println("Non optional param line : " + nonOptionalParamLine);

		String function = visibility + " " + returnType + " " + functionName;

		StringBuilder otherConstructorWithoutOptionals = new StringBuilder(function + " (" + nonOptionalParamLine + " ) { " + newLine + function + " ( ");

		for (Parameter p : params) {
			if (p.isOptionalParameter) {
				otherConstructorWithoutOptionals.append(p.defaultValue);
			} else {
				otherConstructorWithoutOptionals.append(p.name);
			}
		}
		otherConstructorWithoutOptionals.append(newLine + " }");

		String actualParamLine = StringUtils.generateSeparatedStringFromStringList(allParams);


		String curlyOrNot = line.contains("{") ? "{" : ""; // Sometimes the curly bracket is on the other line

		if (DEBUG) {
			System.out.println("visibility : " + visibility);
			System.out.println("returnType : " + returnType);
			System.out.println("name : " + functionName);
			System.out.println("actualParamLine : " + actualParamLine);
			System.out.println("curlyorNot : " + curlyOrNot);
		}

		String re = function + "(" + actualParamLine + ") " + " "
				+ curlyOrNot;

		if (hasOptionalParams) {
			re += otherConstructorWithoutOptionals;
		}

		return re;
	}

}


class Parameter {

	public String type; // int
	public String name; // size

	public boolean isOptionalParameter = false;
	public String defaultValue; // 0

	@Override
	public String toString() {
		return type + " " + name;
	}


	public String toStringToString() {
		return "Parameter{" +
				"type='" + type + '\'' +
				", name='" + name + '\'' +
				", isOptionalParameter=" + isOptionalParameter +
				", defaultValue='" + defaultValue + '\'' +
				'}';
	}

}