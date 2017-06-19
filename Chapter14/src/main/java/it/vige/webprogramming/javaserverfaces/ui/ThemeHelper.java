package it.vige.webprogramming.javaserverfaces.ui;

import static it.vige.rubia.Constants.THEMENAME;
import static it.vige.webprogramming.javaserverfaces.ui.JSFUtil.getDefaultLocale;
import static it.vige.webprogramming.javaserverfaces.ui.JSFUtil.getSelectedLocale;
import static it.vige.webprogramming.javaserverfaces.ui.JSFUtil.getSupportedLocales;
import static it.vige.webprogramming.javaserverfaces.ui.JSFUtil.getUserLastLoginDate;
import static java.lang.Thread.currentThread;
import static java.util.ResourceBundle.getBundle;
import static javax.faces.context.FacesContext.getCurrentInstance;

import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.inject.Singleton;

import it.vige.rubia.Constants;
import it.vige.rubia.auth.UserModule;
import it.vige.rubia.auth.UserProfileModule;
import it.vige.rubia.model.Topic;
import it.vige.webprogramming.javaserverfaces.properties.TCCLXProperties;
import it.vige.webprogramming.javaserverfaces.theme.FolderType;
import it.vige.webprogramming.javaserverfaces.theme.ForumsTheme;

@Singleton
@Named
public class ThemeHelper {

	@EJB
	private UserModule userModule;

	@EJB
	private UserProfileModule userProfileModule;

	public static final int hotThreshold = 10;

	private Set<String> supportedLanguages;

	private ForumsTheme theme;

	public ThemeHelper() throws Exception {
		theme = new ForumsTheme();
		theme.setExtendedProperties(new TCCLXProperties(THEMENAME, "theme.properties"));
		theme.start();
		SortedSet<String> languages = new TreeSet<String>();
		Iterator<Locale> it = getSupportedLocales();
		while (it.hasNext()) {
			languages.add(it.next().getLanguage());
		}
		supportedLanguages = java.util.Collections.unmodifiableSortedSet(languages);
	}

	private boolean isSupportedLanguage(String language) {
		return supportedLanguages.contains(language);
	}

	public String getURL(String urlKey) {
		try {
			String url = null;

			url = (String) ForumsTheme.class.getField(urlKey).get(theme);
			int lastIndexOfSlash = url.lastIndexOf("/");
			String beginning = url.substring(0, lastIndexOfSlash);

			if (!beginning.endsWith("common")) {
				String language = getSelectedLocale().getLanguage();
				if (language.compareTo("") == 0 || !isSupportedLanguage(language)) {
					language = getDefaultLocale().getLanguage();
				}
				String end = url.substring(lastIndexOfSlash, url.length());
				url = beginning + "/" + language + end;
			}
			return url;
		} catch (Exception e) {
			return null;
		}
	}

	public String getResourceForumURL() {
		return theme.resourceForumURL;
	}

	public String getResourceForumNewURL() {
		return theme.resourceForumNewURL;
	}

	public String getResourceForumLockedURL() {
		return theme.resourceForumLockedURL;
	}

	public String getFolderTypeURL(Topic topic, boolean isAnonymous) {
		String folderTypeURL = getURL("resourceFolderURL");

		FolderType folderType = theme.getFolderType(topic.getType(), topic.getStatus(),
				topic.getReplies() >= hotThreshold);

		if (!isAnonymous) {
			Date lastPostDate = topic.getLastPostDate();
			Date lastLoginDate = getUserLastLoginDate(userModule, userProfileModule);

			if (lastPostDate == null || lastLoginDate == null || lastPostDate.compareTo(lastLoginDate) <= 0) {
				folderTypeURL = folderType.getFolder();
			} else {
				folderTypeURL = folderType.getFolderNew();
			}
		} else {
			folderTypeURL = folderType.getFolder();
		}

		return folderTypeURL;
	}

	public String getFolderType(Topic topic) {

		// Getting ResourceBundle with current Locale
		FacesContext ctx = getCurrentInstance();
		UIViewRoot uiRoot = ctx.getViewRoot();
		Locale locale = uiRoot.getLocale();
		ClassLoader ldr = currentThread().getContextClassLoader();
		ResourceBundle bundle = getBundle("ResourceJSF", locale, ldr);

		String topicType = null;

		int topicStatus = topic.getStatus();
		FolderType folderType = theme.getFolderType(topic.getType(), topicStatus, topic.getReplies() >= hotThreshold);

		try {
			if (topicStatus != Constants.TOPIC_MOVED) {
				try {
					topicType = bundle.getString(folderType.type);
				} catch (MissingResourceException e) {
					topicType = "";
				}
			} else {
				topicType = bundle.getString("Topic_Moved");
			}
		} catch (Exception e) {
			return "";
		}
		return topicType;
	}
}
