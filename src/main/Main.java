package main;
import java.net.URL;
import java.util.Vector;

import thread.*;

public class Main implements MessageReceiver {

	public static Database db = new Database();
	
	public Main(URLQueue q, int maxLevel, int maxThreads)
		throws InstantiationException, IllegalAccessException {
		new ThreadController(MainThread.class, maxThreads, 0, maxLevel, q, this);
	}

	@Override
	public void receiveMessage(Object message, int threadId) {
	//	System.out.println("[" + threadId + "] " + message.toString());
	}

	@Override
	public void finished(int threadId) {
	//	System.out.println("[" + threadId + "] finished");
	}

	@Override
	public void finishedAll() {
		System.out.println("All threads has finished");
	}

	static Vector emails = new Vector();
	static int counter = 0;
	static int counter2 = 0;
	
	public static boolean isUnique(String string) {
		return emails.contains(string);
	}
	
	public static void addToVector(String string) {
		emails.addElement(string);
		counter++;
		System.out.println(++counter2);
		if(counter > 1000) {
			Database.sendEmailsToDatabase(emails);
		}
	}
	
	
	public static void main(String[] args) {
		
		try {
			int maxLevel = 2;
			int maxThreads = 10;
	//		int maxDoc = -1;
			if(args.length >= 4) {
				maxThreads = Integer.parseInt(args[3]);
			}
			if(args.length >= 3) {
				maxLevel = Integer.parseInt(args[2]);
			}
			if (args.length >= 2) {
				URLQueue q = new URLQueue();
				q.setFilenamePrefix(args[1]);
				q.push(new URL(args[0]), 0);
				
//				Vector emails = new Vector();
				
				new Main(q, maxLevel, maxThreads);
				
			//	db.finalize();
				
				return;
			}
		} catch(Exception e) {
			System.err.println("Error occured");
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		System.err.println("Usage: java Main <url> <filenamePrefix> [<maxLevel> [<maxThreads>]]");
		System.err.println("Crawls the web for jpeg pictures and mpeg, avi or wmv movies.");
		System.err.println("-1 for either maxLevel or maxDoc means 'unlimited'.");
		
	}
}
