package alde.commons.logger;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * Singleton UI for the global Logger
 * 
 * Fancy HTML visualization
 * 
 * 
 */
public class LoggerPanel extends JTextPane implements LoggerReceiver {

	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(LoggerPanel.class);

	public static LoggerPanel loggerPanel;

	private static final LoggerListener loggerListener = new LoggerListener();

	private static final String DEBUG = "DEBUG";
	private static final String INFO = "INFO";
	private static final String WARNING = "WARN";
	private static final String ERROR = "ERROR";

	private static Color BG_COLOR = new Color(39, 40, 34);
	private static Color LINE_NUMBER_COLOR = new Color(144, 144, 138);

	private static Color TAG_COLOR = new Color(250, 151, 32);
	private static Color DEBUG_COLOR = new Color(166, 226, 45);
	private static Color INFO_COLOR = new Color(96, 208, 239);
	private static Color WARNING_COLOR = TAG_COLOR;
	private static Color ERROR_COLOR = new Color(249, 39, 112);

	private static Color LINE_COLOR = new Color(248, 248, 242);

	//

	private HTMLDocument htmlDocument;
	private Element bodyElement;

	//

	public static LoggerPanel get() {
		if (loggerPanel == null) {
			loggerPanel = new LoggerPanel();
		}

		return loggerPanel;
	}

	private int currentLine = 0;

	private LoggerPanel() {

		loggerListener.addListener(this);

		// Based on http://www.java2s.com/Tutorials/Java/Swing_How_to/JTextPane/Style_JTextPane_with_HTML_CSS_and_StyleSheet.htm

		StyleSheet styleSheet = new StyleSheet();
		styleSheet.addRule(".console {font-family: Consolas,monaco,monospace;}");

		HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
		htmlEditorKit.setStyleSheet(styleSheet);
		htmlDocument = (HTMLDocument) htmlEditorKit.createDefaultDocument();

		setEditorKit(htmlEditorKit);
		setDocument(htmlDocument);

		setBackground(BG_COLOR);

		setContentType("text/html");
		setEditable(false);

		try {
			Element htmlElement = htmlDocument.getRootElements()[0];
			bodyElement = htmlElement.getElement(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clear() {
		setText("");
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

		String lineNumber = colorize("   " + currentLine + "  ", LINE_NUMBER_COLOR);

		String level = event.getLevel().levelStr;

		switch (level) {
		case DEBUG:
			level = colorize(level, DEBUG_COLOR);
			break;
		case INFO:
			level = colorize(level, INFO_COLOR);
			break;
		case WARNING:
			level = colorize(level, WARNING_COLOR);
			break;
		case ERROR:
			level = colorize(level, ERROR_COLOR);
			break;
		default:
			log.warn("Unknown logging level : '" + level + "'.");
			level = colorize(level, LINE_COLOR);
		}

		String name = colorize(event.getLoggerName(), TAG_COLOR);
		String line = colorize(event.getMessage(), LINE_COLOR);

		int lineInFile = event.getCallerData().length > 0 ? event.getCallerData()[0].getLineNumber() : 0;

		if (lineInFile != 0) {
			name += colorize(":" + Integer.toString(lineInFile), TAG_COLOR);
		}

		return lineNumber + "	" + level + " " + name + " " + line;
	}

	private String colorizeBackground(String text, Color color) {
		return "<span style=\"background-color:" + getColorAsHTML(color) + "\">" + text + "</span>";
	}

	private String colorize(String text, Color color) {
		return "<span style=\"color:" + getColorAsHTML(color) + "\">" + text + "</span>";
	}

	/**
	 * Converts Color to HTML rgb(255,0,0)
	 * @param color
	 * @return
	 */
	private String getColorAsHTML(Color color) {
		return "rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
	}

}

/**
 * Use this class to listen to global logger events.
 * 
 * Used by LoggerPanel.
 * 
 * Inspired by Florent Moisson
 */
class LoggerListener extends AppenderBase<ILoggingEvent> {

	private List<LoggerReceiver> loggerReceiverList = new ArrayList<>();

	public LoggerListener() {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		setContext(lc);
		start();

		lc.getLogger("ROOT").addAppender(this);
	}

	public void addListener(LoggerReceiver l) {
		loggerReceiverList.add(l);
	}

	@Override
	public void append(ILoggingEvent event) {
		for (LoggerReceiver l : loggerReceiverList) {
			l.receive(event);
		}
	}

}

interface LoggerReceiver {
	void receive(ILoggingEvent event);
}