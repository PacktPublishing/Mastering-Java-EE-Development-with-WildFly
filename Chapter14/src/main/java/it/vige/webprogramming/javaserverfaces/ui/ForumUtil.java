package it.vige.webprogramming.javaserverfaces.ui;

import static it.vige.webprogramming.javaserverfaces.format.render.bbcodehtml.ToHTMLConfig.FILTER_MODE_ALWAYS_PRINT;
import static it.vige.webprogramming.javaserverfaces.format.render.bbcodehtml.ToHTMLConfig.FILTER_MODE_NEVER_PRINT;
import static it.vige.webprogramming.javaserverfaces.format.render.bbcodehtml.ToHTMLConfig.OUTPUT_MODE_REMOVE;
import static it.vige.webprogramming.javaserverfaces.ui.JSFUtil.getRequestParameter;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Thread.currentThread;
import static java.util.ResourceBundle.getBundle;
import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.StringWriter;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import it.vige.webprogramming.javaserverfaces.format.render.bbcodehtml.ToHTMLConfig;
import it.vige.webprogramming.javaserverfaces.format.render.bbcodehtml.ToHTMLRenderer;

public class ForumUtil {

	public final static String TRUNCATE = "...";

	public static String getParameter(String param) {
		String parameter = null;

		parameter = getRequestParameter(param);

		return parameter;
	}

	public static String nullIfEmptyString(String string) {
		if (string == null || string.trim().length() == 0) {
			return null;
		}
		return string.trim();
	}

	public static String formatMessage(String text, boolean allowHTML) {

		try {
			Object req = getCurrentInstance().getExternalContext().getRequest();

			if (allowHTML) {
				getToHTMLRenderer(req).getConfig().setFilterMode(FILTER_MODE_ALWAYS_PRINT);
				getToHTMLRenderer(req).getConfig().setOuputMode(OUTPUT_MODE_REMOVE);
				getToHTMLRenderer(req).getConfig().setMaxTextWidth(MAX_VALUE);
			} else {
				getToHTMLRenderer(req).getConfig().setFilterMode(FILTER_MODE_NEVER_PRINT);
				getToHTMLRenderer(req).getConfig().setOuputMode(OUTPUT_MODE_REMOVE);
				getToHTMLRenderer(req).getConfig().setMaxTextWidth(MAX_VALUE);
			}
			return formatTitle(req, text);
		} catch (Exception e) {
			return text;
		}

	}

	private static ToHTMLRenderer getToHTMLRenderer(Object req) {
		ToHTMLRenderer renderer = null;
		if (renderer == null) {

			FacesContext ctx = getCurrentInstance();
			UIViewRoot uiRoot = ctx.getViewRoot();
			Locale locale = uiRoot.getLocale();
			ClassLoader ldr = currentThread().getContextClassLoader();
			ResourceBundle bundle = getBundle("ResourceJSF", locale, ldr);

			ToHTMLConfig config = new ToHTMLConfig();
			renderer = new ToHTMLRenderer(config, bundle);
		}
		return renderer;
	}

	public static String formatTitle(Object req, String text) {

		StringWriter stringWriter = new StringWriter();
		getToHTMLRenderer(req).render(text.toCharArray(), 0, text.length());
		getToHTMLRenderer(req).getConfig().setMaxTextWidth(MAX_VALUE);
		return stringWriter.toString();

	}

	public static String truncate(String message, int length) {
		if (message != null) {
			if (message.length() >= length)
				return message.substring(0, length) + TRUNCATE;
		}
		return message;
	}

}
