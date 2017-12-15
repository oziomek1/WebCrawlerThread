package main;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class SaveUrl {
//	static Vector emails = new Vector();
	public static void saveUrl(URL url, Writer writer) 
		throws IOException {
		BufferedInputStream in = new BufferedInputStream(url.openStream());
		for(int i = in.read(); i != -1; i = in.read()) {
			writer.write(i);
		}
	}
	
	public static void saveURL(URL url, OutputStream os)
		throws IOException {
		InputStream in = url.openStream();
		byte[] buf = new byte[1048576];
		int n = in.read(buf);
		while (n != -1) {
			os.write(buf, 0, n);
			n = in.read(buf);
		}
	}
	
	public static String getURL(URL url) 
		throws IOException {
		StringWriter sw = new StringWriter();
		saveUrl(url, sw);
		return sw.toString();
	}
	
	public static void writeURLtoFile(URL url, String filename) 
		throws IOException {
//		FileWriter writer = new FileWriter(filename);
//		saveURL(url, writer);
//		writer.close();
		FileOutputStream os = new FileOutputStream(filename);
		saveURL(url, os);
		os.close();
	}
	
	public static Vector extractLinks(URL url) 
		throws IOException {
		return extractLinks(getURL(url));
	}
	
	public static Vector extractLinks(String rawPage) {
		return extractLinks(rawPage, rawPage.toLowerCase().replaceAll("\\s", " "));
	}
	
	public static Vector extractLinks(String rawPage, String page) {
		int index = 0;
		Vector links = new Vector();
		while((index = page.indexOf("<a ", index)) != -1) {
			
			if((index = page.indexOf("href", index)) == -1) break;
			if((index = page.indexOf("=", index)) == -1) break;
			String str = rawPage.substring(++index);
			StringTokenizer st = new StringTokenizer(str, "\t\n\r\"'?#");
			String strLink = st.nextToken();
			if( !links.contains(strLink)) links.add(strLink);
		}
		return links;
	}
	
	public static void getEmails(String page) {
		
		final String EMAIL_PATTERN = "[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";
		
		Pattern pEmail = Pattern.compile(EMAIL_PATTERN);
		
		Matcher mEmail;
		mEmail = pEmail.matcher(page);
		while(mEmail.find()) {
			if(Main.isUnique(mEmail.group()))
				continue;
			else {
				Main.addToVector(mEmail.group());
//				System.out.println(mEmail.group());
			}
		}
	}
	
	public static Map extractLinksWithText(URL url)
		throws IOException {
		return extractLinksWithText(getURL(url));
	}
	
	public static Map extractLinksWithText(String rawPage) {
		return extractLinksWithText(rawPage, rawPage.toLowerCase().replaceAll("\\s", " "));
	}
	
	public static Map extractLinksWithText(String rawPage, String page) {
		int index = 0;
		Map links = new HashMap();
		while((index = page.indexOf("<a ", index)) != -1) {
			
			int tag = page.indexOf(">", index);
			int endTag = page.indexOf("</a", index);
			if((index = page.indexOf("href", index)) == -1) break;
			if((index = page.indexOf("=", index)) == -1) break;
			String str = rawPage.substring(++index);
			StringTokenizer st = new StringTokenizer(str, "\t\n\r\"'?#");
			String strLink = st.nextToken();
			String strTxt = "";
			if(tag != -1 && tag +1 <= endTag) {
				strTxt = rawPage.substring(tag+1, endTag);
			}
			strTxt = strTxt.replaceAll("\\s+", " ");
			links.put(strLink, strTxt);
		}
		return links;
	}
	
	public static void main(String [] args) {
		try {
			if(args.length == 1) {
				URL url = new URL(args[0]);
				System.out.println("Content-type: " + url.openConnection().getContentType());
				
				Set links = extractLinksWithText(url).entrySet();
				Iterator it = links.iterator();
				while(it.hasNext()) {
					Map.Entry en = (Map.Entry) it.next();
					String str = (String) en.getKey();
					String strTxt = (String) en.getValue();
					System.out.println(str + " \"" + strTxt + "\" ");
				}
				return;
			} else if(args.length == 2) {
				writeURLtoFile(new URL(args[0]), args[1]);
				return;
			}
		} catch (Exception e) {
			System.err.println("An error");
			e.printStackTrace();
		}
		System.err.println("Usage: java SaveURL <url> [<file>]");
		System.err.println("Saves a URL to a file.");
		System.err.println("If no file is given, extracts hyperlinks on url to console.");
	}
	
}
