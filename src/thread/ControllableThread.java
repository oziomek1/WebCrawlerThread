package thread;

public abstract class ControllableThread extends Thread {
	protected int level;
	protected int id;
	protected QueueInterface queue;
	protected ThreadController tc;
	protected MessageReceiver mr;
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public void setQueue(QueueInterface queue) {
		this.queue = queue;
	}
	
	public void setThreadController(ThreadController tc) {
		this.tc = tc;
	}
	
	public void setMessageReceiver(MessageReceiver mr) {
		this.mr = mr;
	}
	
	public ControllableThread() {
		
	}
	
	public void run() {
		for(Object newTask = queue.pop(level); newTask != null; newTask = queue.pop(level)) {
			mr.receiveMessage(newTask, id);
			process(newTask);
			if(tc.getMaxThreads() > tc.getRunningThreads()) {
				try {
					tc.startThreads();
				} catch(Exception e) {
					System.out.println("[" + id + "] " + e.toString());
				}
			}
		}
		tc.finished(id);
	}
	
	public abstract void process(Object o);
}
