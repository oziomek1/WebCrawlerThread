package thread;

import java.util.*;

public class ObjectQueue  implements QueueInterface {

	Object data;
	Set gatheredElements;
	Set processedElements;
	LinkedList queues[];
	int max;
	int numberOfQueues;
	
	public synchronized void setData(Object o) {
		this.data = o;
	}
	
	public synchronized Object getData() {
		return data;
	}
	
	public ObjectQueue(int max, int numberOfQueues) {
		this.numberOfQueues = numberOfQueues;
		this.max = max;
		queues = new LinkedList[numberOfQueues];
		for(int i = 0; i < numberOfQueues; i++) {
			queues[i] = new LinkedList();
		}
	}
	
	public ObjectQueue(int numberOfQueues) {
		this.numberOfQueues = numberOfQueues;
		this.max = -1;
		queues = new LinkedList[numberOfQueues];
		for(int i = 0; i < numberOfQueues; i++) {
			queues[i] = new LinkedList();
		}
	}

	public ObjectQueue() {
		this.numberOfQueues = 4;
		this.max = -1;
		queues = new LinkedList[numberOfQueues];
		for(int i = 0; i < numberOfQueues; i++) {
			queues[i] = new LinkedList();
		}
	}
	
	@Override
	public Set getGatheredElements() {
		return gatheredElements;
	}

	@Override
	public Set getProcessedElements() {
		return processedElements;
	}

	@Override
	public int getQueueSize(int level) {
		if(level < 0 || level >= numberOfQueues)
			return 0;
		else
			return queues[level].size();
	}

	@Override
	public int getProcessedSize() {
		return getProcessedElements().size();
	}

	@Override
	public int getGatheredSize() {
		return getGatheredElements().size();
	}

	@Override
	public void setMaxElements(int elements) {
		this.max = elements;
	}

	@Override
	public Object pop(int level) {
		if(level < 0 || level >= numberOfQueues)
			return null;
		else if (queues[level].size() == 0)
			return null;
		else
			return queues[level].removeFirst();
	}

	@Override
	public boolean push(Object task, int level) {
		if(this.max != -1 && this.max <= gatheredElements.size())
			return false;
		else if(level < 0 || level >= numberOfQueues)
			return false;
		queues[level].add(task);
		return true;
	}

	@Override
	public void clear() {
		for(int i = 0; i < numberOfQueues; i++)
			queues[i].clear();
	}

}
