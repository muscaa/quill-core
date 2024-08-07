package quill.core.pkg.info;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import quill.core.QException;

public class PackageInfoReader {
	
    protected final Map<String, PackageInfoValue> properties = new HashMap<>();
    
    public PackageInfoReader(BufferedReader br) throws QException {
        try {
        	String line;
			while ((line = readLine(br)) != null) {
				if (line.isEmpty()) continue;
				
			    line = line.trim();
			    
			    if (line.isEmpty()) continue;
			    if (line.startsWith("#")) continue;
			    
			    int comment = line.indexOf('#');
			    if (comment != -1) {
			        line = line.substring(0, comment).trim();
			    }
			    
			    int space = line.indexOf(' ');
			    if (space == -1) throw new QException("\"" + line + "\" is not a valid property!");
			    
			    String key = line.substring(0, space);
			    String value = line.substring(space + 1).trim();
			    
			    properties.put(key, new PackageInfoValue(value));
			}
		} catch (IOException e) {
			throw new QException(e);
		}
    }
    
    public PackageInfoValue optional(String key) {
        return optional(key, null);
    }
    
    public PackageInfoValue optional(String key, String defaultValue) {
        return properties.containsKey(key) ? properties.get(key) : new PackageInfoValue(defaultValue);
    }
    
    public PackageInfoValue required(String key, String exception) throws QException {
        return required(key, new QException(exception));
    }
    
    public PackageInfoValue required(String key, QException exception) throws QException {
        if (!properties.containsKey(key)) throw exception;
        return properties.get(key);
    }
    
    private static String readLine(BufferedReader br) throws IOException {
    	String line = br.readLine();
    	if (line == null) return null;
    	
        line = line.trim();
        
        int comment = line.indexOf('#');
        if (comment != -1) {
            line = line.substring(0, comment).trim();
        }
        
        if (line.isEmpty()) return "";
        
        if (line.endsWith("\\")) {
        	String next = readLine(br);
        	if (next == null) next = "";
        	
        	return line.substring(0, line.length() - 1) + next;
        }
    	
    	return line.trim();
    }
}
