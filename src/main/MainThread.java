package main;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import thread.*;

public class MainThread extends ControllableThread {
	public void process(Object o) {
		try {
			URL pageURL = (URL) o;
            String mimetype = pageURL.openConnection().getContentType();

            if (!mimetype.startsWith("text")) return;
			String rawPage = SaveUrl.getURL(pageURL);

            String smallPage = rawPage.toLowerCase().replaceAll("\\s", " ");
	/*		if (smallPage.indexOf("<definitions") != -1) {
				String filename = pageURL.getPath();

                int serviceId = tc.getUniqueNumber();
                filename = ((URLQueue) queue).getFilenamePrefix() + serviceId +
                    "-" + filename.substring(filename.lastIndexOf('/') + 1);

                if (!filename.toLowerCase().endsWith(".wsdl"))
                    filename += ".wsdl";
				FileWriter fw = new FileWriter(filename);
				fw.write(rawPage);
				fw.close();

                URL homepage = new URL(pageURL.getProtocol(),
                    pageURL.getHost(),
                    "");
                filename = ((URLQueue) queue).getFilenamePrefix() + serviceId +
                    "-" + homepage.getHost() + ".html";
                SaveUrl.writeURLtoFile(homepage, filename);
			} else {
	*/			Vector links = SaveUrl.extractLinks(rawPage, smallPage);

				SaveUrl.getEmails(smallPage);
				
	/*			try {
					FileWriter fw = new FileWriter("emails.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter pw = new PrintWriter(bw);
					for(int i = 0; i < emails.size(); i++) {
						System.out.println(emails.get(i));
						pw.println((String)emails.get(i)); 
					} 
				} catch (IOException e) {
					e.printStackTrace();
				} */
				
				for (int n = 0; n < links.size(); n++) {
					try {
						URL link = new URL(pageURL, (String) links.elementAt(n));
//						System.out.println(links.elementAt(n));
						queue.push(link, level + 1);
					} catch (MalformedURLException e) {

					}
				}
			//}
		} catch (Exception e) {
			return;
		}
	}
}
