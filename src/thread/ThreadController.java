package thread;

import linkdatabase.JSONEditor;

public class ThreadController {
	int level;
	int maxLevel;
	int maxThreads;
	QueueInterface tasks;
	MessageReceiver receiver;
	Class threadClass;
	int counter;
	int nThreads;
	
	public ThreadController(Class threadClass, int maxThreads, int level, int maxLevel, QueueInterface tasks, MessageReceiver receiver) 
		throws InstantiationException, IllegalAccessException {
			this.threadClass = threadClass;
			this.maxLevel = maxLevel;
			this.level = level;
			this.maxThreads = maxThreads;
			this.tasks = tasks;
			this.receiver = receiver;
			this.counter = 0;
			this.nThreads = 0;
			startThreads();
	}
	
	public ThreadController() { }
	
	public synchronized int getUniqueNumber() {
		return counter++;
	}
	
	public synchronized void setMaxThreads(int maxThreads) 
		throws InstantiationException, IllegalAccessException {
		this.maxThreads = maxThreads;
		startThreads();
	}
	
	public int getMaxThreads() {
		return maxThreads;
	}
	
	public int getMaxLevel() {
		return maxLevel;
	}
	
	public int getRunningThreads() {
		return nThreads;
	}
	
	public int getCurrentLevel() {
		return level;
	}
	
	public synchronized void finished(int threadId) {
		nThreads--;
		receiver.finished(threadId);
		if(nThreads == 0) {
						
			level++;
			JSONEditor.printToJSON(tasks);
			
//			System.out.println("\n\n\nLEVEL " + (level-1) + " HAS ENDED\n\n\n");
			if(level > maxLevel) {
				receiver.finishedAll();
				return;
			}
			
			if(tasks.getQueueSize(level) == 0) {
				receiver.finishedAll();
				return;
			}
			try {
				startThreads();
			} catch (InstantiationException e) {
				
			} catch (IllegalAccessException e) {
				
			}
		}
	}
	
	public synchronized void startThreads()
		throws InstantiationException, IllegalAccessException {
			int m = maxThreads - nThreads;
			int ts = tasks.getQueueSize(level);
			if(ts < m || maxThreads == -1) {
				m = ts;
			}
			for(int i = 0; i < m; i++) {
				ControllableThread thread = (ControllableThread) threadClass.newInstance();
				thread.setThreadController(this);
				thread.setMessageReceiver(receiver);
				thread.setLevel(level);
				thread.setQueue(tasks);
				thread.setId(nThreads++);
				thread.start();
			}
	}
}
