package thread;

import java.util.*;

public interface QueueInterface {
	public Set getGatheredElements();
	public Set getProcessedElements();
	public int getQueueSize(int level);
	public int getProcessedSize();
	public int getGatheredSize();
	public void setMaxElements(int elements);
	public Object pop(int level);
	public boolean push(Object task, int level);
	public void clear();
}
