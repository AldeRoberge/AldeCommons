package alde.commons.util.autoComplete.jtextfield;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import org.apache.commons.lang3.StringUtils;

import alde.commons.util.HintTextField;

public class AutoCompleteJTextField extends HintTextField {

	List<AutoCompleteInputReceiver> inputReceivers = new ArrayList<AutoCompleteInputReceiver>();

	public AutoCompleteJTextField(CompletionService<?> c, final AutoCompleteInputReceiver receiver, String hint) {
		super(hint);

		inputReceivers.add(receiver);

		// Create the auto completing document model with a reference to the
		// service and the input field.
		Document autoCompleteDocument = new AutoCompleteDocument(c, this);

		// Set the auto completing document as the document model on our input
		// field.
		setDocument(autoCompleteDocument);

		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!StringUtils.isAllBlank(getText())) {
					for (AutoCompleteInputReceiver a : inputReceivers) {
						a.receive(getText());
						setText("");
					}
				}
			}
		});

	}

	public void addListener(AutoCompleteInputReceiver input) {
		inputReceivers.add(input);
	}

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
