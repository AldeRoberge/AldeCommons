package alde.commons.util.autoCompleteJTextField;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteService implements CompletionService<String> {

	private List<String> data = new ArrayList<String>();

	/* String */
	private void addData(String data) {
		if (!this.data.contains(data)) {
			this.data.add(data);
		}
	}

	private void removeData(String data) {
		this.data.remove(data);
	}

	/* Array */
	public void addData(String[] data) {
		for (String k : data) {
			addData(k);
		}
	}

	public void removeData(String[] data) {
		for (String k : data) {
			removeData(k);
		}
	}

	/* List */
	public void addData(List<String> dataList) {
		for (String data : dataList) {
			addData(data);
		}
	}

	public void removeData(List<String> dataList) {
		for (String data : dataList) {
			removeData(data);
		}
	}

	/* Set */
	public void setData(List<String> data) {
		this.data = data;
	}

	public void setData(String data) {
		this.data.clear();
		addData(data);
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