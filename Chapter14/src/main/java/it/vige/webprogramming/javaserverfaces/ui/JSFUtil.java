package it.vige.webprogramming.javaserverfaces.ui;

import static it.vige.rubia.Constants.ERROR;
import static it.vige.rubia.PortalUtil.getUser;
import static it.vige.rubia.auth.User.INFO_USER_LAST_LOGIN_DATE;
import static java.lang.Long.valueOf;
import static java.lang.Thread.currentThread;
import static java.util.ResourceBundle.getBundle;
import static javax.faces.context.FacesContext.getCurrentInstance;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import it.vige.rubia.auth.User;
import it.vige.rubia.auth.UserModule;
import it.vige.rubia.auth.UserProfileModule;

public class JSFUtil {

	public static String getRequestParameter(String name) {
		String parameter = null;

		Map<String, String> requestParameterMap = getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (requestParameterMap != null) {
			parameter = requestParameterMap.get(name);
		}

		return parameter;
	}

	public static boolean isRunningInPortal() {
		boolean isRunningInPortal = false;
		return isRunningInPortal;
	}

	public static String getContextPath() {
		String contextPath = "";

		contextPath = getCurrentInstance().getExternalContext().getRequestContextPath();

		return contextPath;
	}

	public static boolean isAnonymous() {
		boolean anonymous = true;

		String remoteUser = getCurrentInstance().getExternalContext().getRemoteUser();
		if (remoteUser != null && remoteUser.trim().length() > 0) {
			anonymous = false;
		}

		return anonymous;
	}

	public static String getComponentValue(String componentId) {
		String value = null;

		UIViewRoot root = getCurrentInstance().getViewRoot();
		UIComponent component = root.findComponent(componentId);

		if (component != null) {
			Object o = component.getValueExpression("value").getValue(getCurrentInstance().getELContext());
			value = (String) o;
		}

		return value;
	}

	public static void removeComponent(String componentId) {
		UIViewRoot root = getCurrentInstance().getViewRoot();
		UIComponent component = root.findComponent(componentId);

		if (component != null) {
			UIComponent parent = component.getParent();
			parent.getChildren().remove(component);
		}
	}

	public static String handleException(Exception e) {
		String genericNavState = ERROR;
		String msg = e.toString();
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
		getCurrentInstance().addMessage(ERROR, message);

		return genericNavState;
	}

	public static String getErrorMsg() {
		String errorMsg = null;

		Iterator<FacesMessage> msgs = getCurrentInstance().getMessages(ERROR);
		if (msgs != null) {
			if (msgs.hasNext()) {
				FacesMessage message = msgs.next();
				errorMsg = message.getDetail();
			}
		}

		return errorMsg;
	}

	public static boolean isErrorOccurred() {
		boolean errorOccurred = false;

		Iterator<FacesMessage> msgs = getCurrentInstance().getMessages(ERROR);
		if (msgs != null && msgs.hasNext()) {
			errorOccurred = true;
		}

		return errorOccurred;
	}

	public static void setMessage(String id, String msg) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
		getCurrentInstance().addMessage(id, message);
	}

	public static void setErrorMessage(String id, String msg) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
		getCurrentInstance().addMessage(id, message);
	}

	public static String getMessage(String id) {
		String msg = null;

		Iterator<FacesMessage> msgs = getCurrentInstance().getMessages(id);
		if (msgs != null) {
			if (msgs.hasNext()) {
				FacesMessage message = msgs.next();
				msg = message.getDetail();
			}
		}

		return msg;
	}

	public static String getBundleMessage(String bundleName, String messageKey) {
		String bundleMessage = null;

		FacesContext ctx = getCurrentInstance();
		UIViewRoot uiRoot = ctx.getViewRoot();
		Locale locale = uiRoot.getLocale();
		ClassLoader ldr = currentThread().getContextClassLoader();
		ResourceBundle bundle = getBundle(bundleName, locale, ldr);

		bundleMessage = bundle.getString(messageKey);

		return bundleMessage;
	}

	public static Locale getSelectedLocale() {
		return getCurrentInstance().getExternalContext().getRequestLocale();
	}

	public static Locale getDefaultLocale() {
		return getCurrentInstance().getApplication().getDefaultLocale();
	}

	public static Iterator<Locale> getSupportedLocales() {
		return getCurrentInstance().getApplication().getSupportedLocales();
	}

	public static Date getUserLastLoginDate(UserModule userModule, UserProfileModule userProfileModule) {
		try {
			User user = getUser(userModule);
			if (user == null) {
				return null;
			}
			Object property = userProfileModule.getProperty(user, INFO_USER_LAST_LOGIN_DATE);
			if (property != null) {
				long time = 0;
				try {
					time = valueOf(property.toString());
				} catch (NumberFormatException ex) {

				}
				Date date;
				if (time == 0) {
					DateFormat sdfForLastLoginDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",
							getDefaultLocale());
					date = sdfForLastLoginDate.parse(property.toString());
				} else
					date = new Date(time);
				return date;
			}
		} catch (Exception e) {
			JSFUtil.handleException(e);
		}
		return null;
	}
}
