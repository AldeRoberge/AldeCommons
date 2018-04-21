package alde.commons.util.autoComplete.jtextfield;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteService implements CompletionService<String> {

	/** Our name data. */
	private List<String> data = new ArrayList<String>();

	public void setData(List<String> data) {
		this.data = data;
	}

	public void addData(String s) {
		this.data.add(s);
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (String o : data) {
			b.append(o).append("\n");
		}
		return b.toString();
	}

	@Override
	public String autoComplete(String startsWith) {

		if (startsWith == null) {
			new IllegalArgumentException("Auto complete data is null");

			return null;
		}

		// Naive implementation, but good enough for the sample
		String hit = null;
		for (String o : data) {
			if (o.startsWith(startsWith)) {
				// CompletionService contract states that we only
				// should return completion for unique hits.
				if (hit == null) {
					hit = o;
				} else {
					hit = null;
					break;
				}
			}
		}
		return hit;
	}

}