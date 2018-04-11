package util.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WriteToFile {

	private String file;

	public WriteToFile(String file) {
		this.file = file;
	}

	private boolean isAlreadyRunning;

	private List<String> bufferedText = new ArrayList<>();

	public void write(final String text) {

		if (!isAlreadyRunning) {

			new Thread(new Runnable() {
				public void run() {
					isAlreadyRunning = true;

					try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {

						bw.write(text);
						bw.newLine();

						Iterator<String> it = bufferedText.iterator();
						while (it.hasNext()) {
							bw.write(it.next());
							bw.newLine();
							it.remove();
						}

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					isAlreadyRunning = false;
				}
			}).start();

		} else {
			bufferedText.add(text);
		}
	}

}
