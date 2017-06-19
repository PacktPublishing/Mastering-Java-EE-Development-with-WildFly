package it.vige.webprogramming.javaserverfaces.ui.view;

import static it.vige.webprogramming.javaserverfaces.ui.JSFUtil.handleException;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.model.Category;
import it.vige.rubia.model.Forum;
import it.vige.webprogramming.javaserverfaces.auth.AuthorizationListener;
import it.vige.webprogramming.javaserverfaces.ui.BaseController;

@Named("adminPanel")
public class ViewAdminPanel extends BaseController {

	private static final long serialVersionUID = 2921359509888139037L;

	@EJB
	private ForumsModule forumsModule;

	private List<Category> categories = null;
	private List<Forum> forums = null;

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public List<Category> getCategories() {
		if (categories != null) {
			return categories;
		}
		synchronized (this) {
			if (categories != null) {
				return categories;
			}
			try {
				int forumInstanceId = 1;

				categories = forumsModule.findCategoriesFetchForums(forumInstanceId);

				return categories;
			} catch (Exception e) {
				handleException(e);
			}
			return null;
		}
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public List<Forum> getForums() {
		if (forums != null) {
			return forums;
		}
		try {
			int forumInstanceId = 1;

			forums = forumsModule.findForums(forumInstanceId);

			return forums;
		} catch (Exception e) {
			handleException(e);
		}
		return null;
	}

}
