package com.gamezgalaxy.GGS.util.logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Logger {
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	private String filepath;

	private Object owner;

	private PrintWriter out;

	private Queue<String> queue = new LinkedList<String>();

	private Thread runner;

	private boolean Running;

	/**
	 * Create a new instance of Logger
	 * @param filepath The filepath of the log file
	 * @param owner The class that extends Logger
	 */
	public Logger(String filepath, Object owner) {
		this.filepath = filepath;
		this.owner = owner;
	}
	/**
	 * Create a new instance of Logger
	 * @param filepath The filepath of the log file
	 */
	public Logger(String filepath) {
		this(filepath, null);
	}
	/**
	 * Create a new instance of Logger
	 */
	public Logger() {
		
	}
	
	/**
	 * Set the owner
	 * @param obj
	 */
	public void setParent(Object obj) {
		owner = obj;
	}

	/**
	 * Start writing logs
	 * @throws FileNotFoundException
	 */
	public void Start(boolean changefilename) throws FileNotFoundException {
		if (changefilename) {
			try {
				ChangeFilePath(filepath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Running = true;
		runner = new Log();
		runner.start();
	}
	
	public String getCurrentDate(Calendar cal) {
		return dateFormat.format(cal.getTime());
	}

	/**
	 * Stop writing logs
	 * This code will block until all logs have been written to the file
	 * @throws InterruptedException
	 */
	public void Stop() throws InterruptedException {
		Running = false;
		runner.join();
		out.close();
	}
	
	/**
	 * Add a log to the logger
	 * @param message The message to add
	 * @throws Exception
	 */
	public void Log(String message) {
		synchronized(queue) {
			if (!Running)
				return;
			queue.add(message);
		}
	}

	/**
	 * Change the filepath of the log file
	 * @param newpath The new filepath
	 * @throws IOException
	 */
	public void ChangeFilePath(String filename) throws IOException {
		this.filepath = filename;
		if (out != null)
			out.close();
		if (!new File(this.filepath).exists()) {
			new File(this.filepath).createNewFile();
		}
		out = new PrintWriter(filepath);
	}
	public void ChangeFilePath(String directory, String filename) throws IOException {
		this.filepath = directory + File.separator + filename;
		if (out != null)
			out.close();
		if (!new File(directory, filename).exists())
			new File(directory, filename).createNewFile();
		out = new PrintWriter(filepath);
	}
	
	private class Log extends Thread {

		@Override
		public void run() {
			Calendar cal = Calendar.getInstance();
			Iterator it = null;
			while (Running) {
				synchronized(queue) {
					it=queue.iterator();
					if (it == null)
						continue;
					while (it.hasNext()) {
						cal = Calendar.getInstance();
						String message = (String)it.next();
						String date = dateFormat.format(cal.getTime());
						String finalmessage = "[" + date + "] " + message;
						if (owner != null && owner instanceof LogInterface) {
							if (message.contains("ERROR"))
								((LogInterface)owner).onError(finalmessage);
							else
								((LogInterface)owner).onLog(finalmessage);
						}
						if (out != null)
							out.println(finalmessage);
					}
					queue.clear();
					out.flush();
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
