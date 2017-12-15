package thread;

public interface MessageReceiver {
	public void receiveMessage(Object message, int threadId);
	public void finished(int threadId);
	public void finishedAll();
}
