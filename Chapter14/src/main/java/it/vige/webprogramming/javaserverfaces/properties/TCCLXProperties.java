package it.vige.webprogramming.javaserverfaces.properties;

import static it.vige.rubia.Constants.THEMENAME;
import static it.vige.webprogramming.javaserverfaces.ui.JSFUtil.getContextPath;
import static java.lang.Thread.currentThread;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class TCCLXProperties extends XProperties {
	private Map<Object, Object> props = new HashMap<Object, Object>();
	private Map<Object, Object> urls = new HashMap<Object, Object>();

	public TCCLXProperties(String base, String name) throws IOException {
		this(currentThread().getContextClassLoader(), base, name);
	}

	public TCCLXProperties(ClassLoader loader, String base, String name) throws IOException {
		InputStream in = null;
		try {
			// load the data
			in = loader.getResourceAsStream(base + "/" + name);

			// feed the properties
			Properties temp = new Properties();
			temp.load(in);
			props.putAll(temp);
			for (Iterator<Map.Entry<Object, Object>> i = props.entrySet().iterator(); i.hasNext();) {
				Map.Entry<Object, Object> entry = i.next();

				urls.put(entry.getKey(), getContextPath() + "/" + THEMENAME + "/" + entry.getValue());
			}
		} finally {
			in.close();
		}
	}

	public String getProperty(String name) {
		Object value = props.get(name);
		return (value != null) ? (String) value : "";
	}

	public String getResourceURL(String name) {
		Object value = urls.get(name);
		return (value != null) ? (String) value : "";
	}
}