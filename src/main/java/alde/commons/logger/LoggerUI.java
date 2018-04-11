package alde.commons.logger;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;

/**
 * Based on http://www.java2s.com/Tutorials/Java/Swing_How_to/JTextPane/Style_JTextPane_with_HTML_CSS_and_StyleSheet.htm
 */
public class LoggerUI extends JTextPane implements LoggerReceiver {

	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(LoggerUI.class);

	public static final LoggerListener loggerListener = new LoggerListener();

	private static final String DEBUG = "DEBUG";
	private static final String INFO = "INFO";
	private static final String WARNING = "WARN";
	private static final String ERROR = "ERROR";

	private static final Color backGroundColor = new Color(39, 40, 34);
	private static final Color lineNumberColor = new Color(144, 144, 138);

	private static final Color tagColor = new Color(250, 151, 32);
	private static final Color debugColor = new Color(166, 226, 45);
	private static final Color infoColor = new Color(96, 208, 239);
	private static final Color warningColor = tagColor;
	private static final Color errorColor = new Color(249, 39, 112);

	private static final Color lineColor = new Color(248, 248, 242);

	//

	private HTMLDocument htmlDocument;
	private Element bodyElement;

	//

	private int currentLine = 0;

	public LoggerUI() {
		loggerListener.addListener(this);

		StyleSheet styleSheet = new StyleSheet();
		styleSheet.addRule(".console {font-family: Consolas,monaco,monospace;}");

		HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
		htmlEditorKit.setStyleSheet(styleSheet);
		htmlDocument = (HTMLDocument) htmlEditorKit.createDefaultDocument();

		setEditorKit(htmlEditorKit);
		setDocument(htmlDocument);

		setBackground(backGroundColor);

		setContentType("text/html");
		setEditable(false);

		try {
			Element htmlElement = htmlDocument.getRootElements()[0];
			bodyElement = htmlElement.getElement(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void receive(ILoggingEvent event) {
		try {
			addContent(formatLogToColorizedHTML(event));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addContent(String content) throws Exception {
		Element contentElement = bodyElement.getElement(bodyElement.getElementCount() - 1);
		htmlDocument.insertBeforeEnd(contentElement, "<span class=console>" + content + "</span><br>");
	}

	private String formatLogToColorizedHTML(ILoggingEvent event) {
		currentLine++;

		String lineNumber = colorize(currentLine + " ", lineNumberColor);

		String level = event.getLevel().levelStr;

		switch (level) {
		case DEBUG:
			level = colorize(level, debugColor);
			break;
		case INFO:
			level = colorize(level, infoColor);
			break;
		case WARNING:
			level = colorize(level, warningColor);
			break;
		case ERROR:
			level = colorize(level, errorColor);
			break;
		default:
			log.warn("Unknown logging level : '" + level + "'.");
			level = colorize(level, lineColor);
		}

		String name = colorize(event.getLoggerName(), tagColor);
		String line = colorize(event.getMessage(), lineColor);

		return lineNumber + level + " " + name + " " + line;
	}

	private String colorize(String text, Color color) {
		String rgb = "rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")"; // Convert to html rgb(255,0,0)
		return "<span style=\"color:" + rgb + "\">" + text + "</span>";
	}

}
