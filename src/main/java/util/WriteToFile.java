package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class WriteToFile {

	private String file;

	public WriteToFile(String file) {
		this.file = file;
	}

	private boolean isAlreadyRunning;
	private ArrayList<String> bufferedText = new ArrayList<>();

	public void write(final String text) {

		if (!isAlreadyRunning) {

			new Thread(new Runnable() {
				public void run() {
					isAlreadyRunning = true;

					BufferedWriter bw;
					try {
						// APPEND MODE SET HERE
						bw = new BufferedWriter(new FileWriter(file, true));
						bw.write(text);
						bw.newLine();

						Iterator<String> it = bufferedText.iterator();
						while (it.hasNext()) {
							bw.write(it.next());
							bw.newLine();
							it.remove();
						}

						try {
							bw.close();
						} catch (IOException ioe2) {
							// just ignore it
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
