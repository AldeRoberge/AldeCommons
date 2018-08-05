package alde.commons.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class HashMapUtil {

	public static <K, V> Map<K, V> sortByValue(Map<K, V> map) {
		List<Entry<K, V>> list = new LinkedList<>(map.entrySet());
		list.sort(new Comparator<Object>() {
			@Override
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
