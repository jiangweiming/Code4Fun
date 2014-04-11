package org.skywing.code.wordcount;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.io.IOUtils;

/**
 * 读取文件，将待处理的文件行放入阻塞队列
 * @author jwm
 * @date Apr 11, 2014
 * @since 1.6
 */
public class WordFileReader
{
	private String filePath;
	private BlockingQueue<String> blockingQueue;
	private volatile boolean done = false;
	
	
	public WordFileReader(String filePath, BlockingQueue<String> blockingQueue)
	{
		this.filePath = filePath;
		this.blockingQueue = blockingQueue;
	}
	
	public void start()
	{
		System.out.println("WordFileReader start ...");
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				this.blockingQueue.put(line);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			IOUtils.closeQuietly(reader);
			this.done = true;
		}
		System.out.println("WordFileReader exit ...");
	}

	public boolean isDone()
	{
		return done;
	}
}
