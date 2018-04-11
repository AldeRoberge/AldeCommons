package util;

import java.util.ArrayList;
import java.util.Collections;

public class RandomIDGen {

	private int amountOfIDs = -1;
	private ArrayList<Integer> eventIds = new ArrayList<Integer>();

	public RandomIDGen(int amountOfIDs) {
		this.amountOfIDs = amountOfIDs;
		populate();
	}

	public void populate() {
		for (int i = 0; i < amountOfIDs; i++) {
			eventIds.add(i);
		}

		Collections.shuffle(eventIds);
	}

	public int getNextID() {
		int i = -1;

		if (eventIds.size() == 0 || eventIds.get(0) == null) {
			populate();
		}

		i = eventIds.get(0);
		eventIds.remove(0);

		return i;

	}

}
