package org.skywing.code.wordcount;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class WordCountRunner
{
	public static void main(String[] args)
	{
		int taskNumber = 2;//equals #cpus
		LinkedBlockingQueue<String> blockingQueue = new LinkedBlockingQueue<String>();
		CountDownLatch latch = new CountDownLatch(taskNumber);
		
		WordFileReader reader = new WordFileReader("E:\\wordCount.txt", blockingQueue);
		WordSpliterExecutor spliter = new WordSpliterExecutor(blockingQueue, reader, latch, taskNumber);
		WordSelector selector = new WordSelector(spliter.getCounterMap(), latch);
		
		reader.start();
		spliter.start();
		selector.start();
	}
}
