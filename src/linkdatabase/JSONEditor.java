package linkdatabase;

import thread.*;

import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONEditor {
	
	public static void printToJSON(QueueInterface queue) {
		
		JSONObject obj = new JSONObject();
		
		int level = new ThreadController().getCurrentLevel();
		
		obj.put("level", level);
		
		JSONArray array = new JSONArray();
		for(int i = 0; i < queue.getQueueSize(level); i++) {
			array.add(queue.pop(level));
		}
		obj.put("links", array);
		
		try {
			FileWriter file = new FileWriter("links.json");
			file.write(obj.toJSONString());
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
