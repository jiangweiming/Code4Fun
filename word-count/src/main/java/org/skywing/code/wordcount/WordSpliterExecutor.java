package org.skywing.code.wordcount;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WordSpliterExecutor
{
	private int taskNumber;
	private WordFileReader reader;
	private CountDownLatch latch;
	private BlockingQueue<String> blockingQueue;
	private ConcurrentHashMap<String, Integer> counterMap = new ConcurrentHashMap<String, Integer>();
	private ExecutorService threadPool = Executors.newCachedThreadPool();
	
	public WordSpliterExecutor(BlockingQueue<String> blockingQueue,
			WordFileReader reader, CountDownLatch latch, int taskNumber)
	{
		this.blockingQueue = blockingQueue;
		this.reader = reader;
		this.latch = latch;
		this.taskNumber = taskNumber;
	}
	
	public ConcurrentHashMap<String, Integer> getCounterMap()
	{
		return this.counterMap;
	}

	public void start()
	{
		System.out.println("WordSpliterExecutor start ...");
		for (int i = 0; i < this.taskNumber; i++)
		{
			this.threadPool.submit(new WordSpliterTask(this.reader, this.blockingQueue, this.counterMap, this.latch));
		}
	}
	
	public static class WordSpliterTask implements Runnable
	{
		private WordFileReader reader;
		private CountDownLatch latch;
		private BlockingQueue<String> blockingQueue;
		private ConcurrentHashMap<String, Integer> counterMap;
		private volatile boolean runFlag = true;
		
		public WordSpliterTask(WordFileReader reader, BlockingQueue<String> blockingQueue,
				ConcurrentHashMap<String, Integer> counterMap, CountDownLatch latch)
		{
			this.blockingQueue = blockingQueue;
			this.counterMap = counterMap;
			this.reader = reader;
			this.latch = latch;
		}
		
		public void stop()
		{
			this.runFlag = false;
		}
		
		@Override
		public void run()
		{
			System.out.println("Thread-[" + Thread.currentThread().getName() + "] start ...");
			try
			{
				while (this.runFlag)
				{
					String line = this.blockingQueue.poll(200, TimeUnit.MILLISECONDS);
					if (line != null)
					{
						String[] words = line.split(" ");
						for (String word : words)
						{
							System.out.println("counting word=[" + word + "]");
							//CAS乐观锁并发，不存在key-value对，致使出现死循环
							for (;;)
							{
								int count = this.counterMap.get(word) == null ? 0 : this.counterMap.get(word);
								int next = count + 1;
								if (this.counterMap.replace(word, count, next))
									break;
							}
							/*
							//不良并发
							synchronized (this.counterMap)
							{
								if (!this.counterMap.containsKey(word))
								{
									this.counterMap.put(word, 1);
								}
								else
								{
									int count = this.counterMap.get(word);
									this.counterMap.put(word, count + 1);
								}
							}
							*/
						}
					}
					else
					{
						TimeUnit.SECONDS.sleep(1);
					}
					if (this.reader.isDone() && this.blockingQueue.isEmpty())
					{
						this.latch.countDown();
						System.out.println("Thread-[" + Thread.currentThread().getName() + "] count down");
						this.stop();
					}
				}
				System.out.println("Thread-[" + Thread.currentThread().getName() + "] exit ...");
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
	}
}
