package org.skywing.code.wordcount.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A Map Tool Class
 * 
 * @author jwm
 * @date Apr 11, 2014
 * @since 1.6
 */
public class MapUtil
{
	/**
	 * Sort the map by value. Value which in map.Entry<K, V> must be comparable,
	 * that is value object must implement Comparable interface.
	 * 
	 * @param map
	 * @return sortedMap whose type is LinkedHashMap
	 */
	public static <K, V extends Comparable<V>> Map<K, V> sortMapByValue(Map<K, V> map)
	{
		Comparator<Entry<K, V>> comparator = new Comparator<Map.Entry<K,V>>()
		{
			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2)
			{
				return e1.getValue().compareTo(e2.getValue());
			}
		};
		return sortMapByValue(map, comparator);
	}
	
	/**
	 * Sort the map by value. a comparator must be supplied.
	 * 
	 * @param map
	 * @param comparator
	 * @return
	 */
	public static <K, V> Map<K, V> sortMapByValue(Map<K, V> map, Comparator<Entry<K, V>> comparator)
	{
		List<Entry<K, V>> list = new LinkedList<Entry<K, V>>(map.entrySet());
		Collections.sort(list, comparator);
		Map<K, V> sortedMap = new LinkedHashMap<K, V>();
		for (Entry<K, V> entry : list)
		{
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap.isEmpty() ? null : sortedMap;
	}
}
