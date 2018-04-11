package alde.commons.util;

import java.util.*;
import java.util.Map.Entry;

public class HashMapUtil {

	public static <K, V> Map<K, V> sortByValue(Map<K, V> map) {
		List<Entry<K, V>> list = new LinkedList<>(map.entrySet());
		list.sort(new Comparator<Object>() {
			@SuppressWarnings("unchecked")
			public int compare(Object o1, Object o2) {
				return ((Comparable<V>) ((Entry<K, V>) (o2)).getValue()).compareTo(((Entry<K, V>) (o1)).getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<>();
		for (Iterator<Entry<K, V>> it = list.iterator(); it.hasNext();) {
			Map.Entry<K, V> entry = it.next();
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}

}
