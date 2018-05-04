package alde.commons.util.autoCompleteJTextField;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Polyvalent JTextField.
 * 
 * Includes :
 * 
 * - Temporary Hint (disappears after input)
 * - Suggestions (@see AutoCompleteService)
 * - Memory (press up and down to navigate trough previous inputs)
 * 
 * @author Alde
 *
 */
@SuppressWarnings("serial")
public class UtilityJTextField extends HintTextField {

	Logger log = LoggerFactory.getLogger(UtilityJTextField.class);

	/** List of previous inputs */
	private List<String> previousInputs = new ArrayList<String>();

	/** Used to navigate between previous inputs */
	private int currentIndex = 0;

	/** AutoCompleteService used by suggestions */
	AutoCompleteService autoCompleteService = new AutoCompleteService();

	/** Subscribers to inputs */
	List<StringReceiver> inputReceivers = new ArrayList<StringReceiver>();

	public void addReceiver(StringReceiver input) {
		inputReceivers.add(input);
	}

	public AutoCompleteService getCompletionService() {
		return autoCompleteService;
	}

	public void setCompletionService(AutoCompleteService autoCompleteService) {
		this.autoCompleteService = autoCompleteService;
	}

	/**
	 * @param hint can be set to empty string
	 * @param memory weither or not to remembers inputs
	 */
	public UtilityJTextField(String hint, boolean memory) {
		super(hint);

		// Create the auto completing document model with a reference to the
		// service and the input field.
		Document autoCompleteDocument = new AutoCompleteDocument(autoCompleteService, this);

		// Set the auto completing document as the document model on our input
		// field.
		setDocument(autoCompleteDocument);

		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!StringUtils.isAllBlank(getText())) {
					for (StringReceiver a : inputReceivers) {
						a.receive(getText());
						setText("");
					}
				}
			}
		});

		if (memory) {
			addReceiver(new StringReceiver() { // Save in memory when enter is pressed
				@Override
				public void receive(String input) {
					if (!StringUtils.isAllBlank(getText())) {
						previousInputs.add(getText());
						currentIndex = previousInputs.size();
					}
				}
			});

			addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) { // Left blank intentionally
				}

				@Override
				public void keyPressed(KeyEvent e) { // Left blank intentionally
				}

				@Override
				public void keyReleased(KeyEvent e) {
					handle(e);
				}

				private void handle(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_UP) {
						setIndex(+1);
					}

					if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						setIndex(-1);
					}
				}

				void setIndex(int i) {

					currentIndex -= i;

					if (currentIndex < 0) { // Minimum
						currentIndex = 0;
					}

					if (currentIndex > previousInputs.size() - 1) { // Maximum
						currentIndex = previousInputs.size() - 1;
					}

					if (!(previousInputs.size() == 0)) {
						setText(previousInputs.get(currentIndex));
					}

				}

			});
		}

	}

}

/**
 * Text field with temporary hint text
 * 
 * from https://stackoverflow.com/a/24571681
 * 
 * @Author Adam Gawne-Cain
 */
class HintTextField extends JTextField {

	boolean displayHintAgain = true;

	private final String hint;

	public HintTextField(String hint) {
		this.hint = hint;
		setEnabled(true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (displayHintAgain && getText().length() == 0) {
			int h = getHeight();
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			Insets ins = getInsets();
			FontMetrics fm = g.getFontMetrics();
			int c0 = getBackground().getRGB();
			int c1 = getForeground().getRGB();
			int m = 0xfefefefe;
			int c2 = ((c0 & m) >>> 1) + ((c1 & m) >>> 1);
			g.setColor(new Color(c2, true));
			g.drawString(hint, ins.left, h / 2 + fm.getAscent() / 2 - 2);
		} else {
			displayHintAgain = false;
		}
	}

}

/**
 * A service providing autocompletion support.
 * 
 * @see AutoCompleteDocument
 * 
 * @author Samuel Sjoberg, http://samuelsjoberg.com
 * @version 1.0.0
 * 
 * @param <T>
 *            the type to be returned by the service
 */
interface CompletionService<T> {

	/**
	 * Autocomplete the passed string. The method will return the matching
	 * object when one single object matches the search criteria. As long as
	 * multiple objects stored in the service matches, the method will return
	 * <code>null</code>.
	 * 
	 * @param startsWith
	 *            prefix string
	 * @return the matching object or <code>null</code> if multiple matches are
	 *         found.
	 */
	T autoComplete(String startsWith);
}



/**
 * A {@link Document} performing auto completion on the inserted text. This
 * document can be used on any {@link JTextComponent}.
 * <p>
 * The completion will only happen for inserts, that is, when characters are
 * typed. If characters are erased, no new completion is suggested until a new
 * character is typed.
 * 
 * @see CompletionService
 * 
 * @author Samuel Sjoberg, http://samuelsjoberg.com
 * @version 1.0.0
 */
class AutoCompleteDocument extends PlainDocument {

	/** Default serial version UID. */
	private static final long serialVersionUID = 1L;

	/** Completion service. */
	private CompletionService<?> completionService;

	/** The document owner. */
	private JTextComponent documentOwner;

	/**
	 * Create a new <code>AutoCompletionDocument</code>.
	 * 
	 * @param service
	 *            the service to use when searching for completions
	 * @param documentOwner
	 *            the document owner
	 */
	public AutoCompleteDocument(CompletionService<?> service, JTextComponent documentOwner) {
		this.completionService = service;
		this.documentOwner = documentOwner;
	}

	/**
	 * Look up the completion string.
	 * 
	 * @param str
	 *            the prefix string to complete
	 * @return the completion or <code>null</code> if completion was found.
	 */
	protected String complete(String str) {
		Object o = completionService.autoComplete(str);
		return o == null ? null : o.toString();
	}

	/** {@inheritDoc} */
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		if (str == null || str.length() == 0) {
			return;
		}

		String text = getText(0, offs); // Current text.
		String completion = complete(text + str);
		int length = offs + str.length();
		if (completion != null && text.length() > 0) {
			str = completion.substring(length - 1);
			super.insertString(offs, str, a);
			documentOwner.select(length, getLength());
		} else {
			super.insertString(offs, str, a);
		}
	}
}
