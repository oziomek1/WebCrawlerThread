package main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class URLQueue implements thread.QueueInterface {

	LinkedList evenQueue;
	LinkedList oddQueue;
	Set gatheredLinks;
	Set processedLinks;
	
	int maxElements;
	
	String filenamePrefix;
	
	public URLQueue() {
		evenQueue = new LinkedList();
		oddQueue = new LinkedList();
		gatheredLinks = new HashSet();
		processedLinks = new HashSet();
		maxElements = -1;
		filenamePrefix = "";
	}
	
	public URLQueue(int maxElements, String filenamePrefix) {
		evenQueue = new LinkedList();
		oddQueue = new LinkedList();
		gatheredLinks = new HashSet();
		processedLinks = new HashSet();
		this.maxElements = maxElements;
		this.filenamePrefix = filenamePrefix;
	}
	
	public void setFilenamePrefix(String filenamePrefix) {
		this.filenamePrefix = filenamePrefix;
	}
	
	public String getFilenamePrefix() {
		return filenamePrefix;
	}
	
	@Override
	public Set getGatheredElements() {
		return gatheredLinks;
	}

	@Override
	public Set getProcessedElements() {
		return processedLinks;
	}

	@Override
	public int getQueueSize(int level) {
		if(level % 2 == 0) {
			return evenQueue.size();
		} else {
			return oddQueue.size();
		}
	}

	@Override
	public int getProcessedSize() {
		return processedLinks.size();
	}

	@Override
	public int getGatheredSize() {
		return gatheredLinks.size();
	}

	@Override
	public void setMaxElements(int elements) {
		this.maxElements = elements;
	}

	@Override
	public synchronized Object pop(int level) {
		String s;
		
		if(level % 2 == 0) {
			if(evenQueue.size() == 0) {
				return null;
			} else {
				s = (String) evenQueue.removeFirst();
			}
		} else {
			if(oddQueue.size() == 0) {
				return null;
			} else {
				s = (String) oddQueue.removeFirst();
			}
		}
		
		try {
			URL url = new URL(s);
			processedLinks.add(s);
			return url;
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@Override
	public synchronized boolean push(Object url, int level) {

		if(maxElements != -1 && maxElements <= gatheredLinks.size())
			return false;
		String s = ((URL) url).toString();
		if(gatheredLinks.add(s)) {
			if(level % 2 == 0)
				evenQueue.add(s);
			else
				oddQueue.add(s);
			return true;
		} else
			return false;
	}

	@Override
	public void clear() {
		evenQueue.clear();
		oddQueue.clear();
	}
	
}
