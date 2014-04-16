package org.skywing.code.wordcount;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.skywing.code.wordcount.util.MapUtil;

public class WordSelector
{
	private ConcurrentHashMap<String, Integer> counterMap;
	private CountDownLatch latch;
	
	public WordSelector(ConcurrentHashMap<String, Integer> counterMap, CountDownLatch latch)
	{
		this.counterMap = counterMap;
		this.latch = latch;
	}
	
	public void start()
	{
		System.out.println("WordSelector start ...");
		try
		{
			System.out.println("WordSelector await ...");
			this.latch.await();
			Map<String, Integer> sortedMap = MapUtil.sortMapByValue(this.counterMap, new Comparator<Entry<String, Integer>>()
			{
				@Override
				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2)
				{
					return o2.getValue() - o1.getValue();
				}
			});
			output(sortedMap);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private <K, V> void output(Map<K, V> map)
	{
		for (Entry<K, V> entry : map.entrySet())
		{
			System.out.println(entry.getKey() + "--" + entry.getValue());
		}
	}
}
