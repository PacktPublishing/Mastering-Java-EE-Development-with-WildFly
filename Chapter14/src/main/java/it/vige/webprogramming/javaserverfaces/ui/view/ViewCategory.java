package it.vige.webprogramming.javaserverfaces.ui.view;

import static it.vige.webprogramming.javaserverfaces.ui.ForumUtil.getParameter;
import static it.vige.webprogramming.javaserverfaces.ui.ForumUtil.truncate;
import static it.vige.webprogramming.javaserverfaces.ui.JSFUtil.getUserLastLoginDate;
import static it.vige.webprogramming.javaserverfaces.ui.JSFUtil.handleException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.auth.UserModule;
import it.vige.rubia.auth.UserProfileModule;
import it.vige.rubia.model.Category;
import it.vige.rubia.model.Forum;
import it.vige.rubia.model.Post;
import it.vige.webprogramming.javaserverfaces.auth.AuthorizationListener;
import it.vige.webprogramming.javaserverfaces.ui.BaseController;
import it.vige.webprogramming.javaserverfaces.ui.ThemeHelper;

@Named("category")
@RequestScoped
public class ViewCategory extends BaseController {

	private static final long serialVersionUID = 5349549910762005145L;
	@EJB
	private ForumsModule forumsModule;
	@Inject
	private ThemeHelper themeHelper;
	@EJB
	private UserModule userModule;
	@EJB
	private UserProfileModule userProfileModule;

	private Collection<Category> categories = null;
	private Map<Integer, Collection<Forum>> forums = null;
	private Map<Integer, String> forumImages = null;
	private Map<Integer, String> forumImageDescriptions = null;
	private Map<Object, Post> forumLastPosts = null;
	private boolean categorySelected = false;

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Collection<Category> getCategories() {
		if (categories == null) {
			categories = new ArrayList<Category>();
		}
		return categories;
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Map<Integer, Collection<Forum>> getForums() {
		if (forums == null) {
			forums = new HashMap<Integer, Collection<Forum>>();
		}
		return forums;
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Map<Integer, String> getForumImageDescriptions() {
		if (forumImageDescriptions == null) {
			forumImageDescriptions = new HashMap<Integer, String>();
		}
		return forumImageDescriptions;
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Map<Integer, String> getForumImages() {
		if (forumImages == null) {
			forumImages = new HashMap<Integer, String>();
		}
		return forumImages;
	}

	public Map<Object, Post> getForumLastPosts() {
		if (forumLastPosts == null) {
			forumLastPosts = new HashMap<Object, Post>();
		}
		return forumLastPosts;
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String getLastPostSubject(int id) {
		Post post = getForumLastPosts().get(id);
		if (post != null) {
			String subject = post.getMessage().getSubject();
			return truncate(subject, 25);
		} else
			return "";
	}

	public boolean isCategorySelected() {
		return categorySelected;
	}

	@PostConstruct
	public void execute() {
		try {
			int categoryId = -1;
			String c = getParameter(p_categoryId);
			if (c != null && c.trim().length() > 0) {
				categoryId = Integer.parseInt(c);
				categorySelected = true;
			}

			int forumInstanceId = 1;

			this.forumLastPosts = forumsModule.findLastPostsOfForums(forumInstanceId);

			if (categoryId == -1) {
				Collection<Category> cour = forumsModule.findCategoriesFetchForums(forumInstanceId);
				if (cour != null) {
					for (Category currentCategory : cour)
						processCategory(currentCategory);
				}
			} else {
				Category currentCategory = forumsModule.findCategoryById(categoryId);
				if (currentCategory != null) {
					processCategory(currentCategory);
				}
			}
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void processCategory(Category category) throws Exception {
		Date userLastLogin = getUserLastLoginDate(userModule, userProfileModule);
		if (category != null) {
			getCategories().add(category);

			Collection<Forum> forums = forumsModule.findForumsByCategory(category);
			Collection<Forum> categoryForums = new ArrayList<Forum>();
			for (Forum currentForum : forums) {
				categoryForums.add(currentForum);

				String folderImage = themeHelper.getResourceForumURL();
				String folderAlt = "No_new_posts"; // bundle key
				if (this.forumLastPosts != null && forumLastPosts.containsKey(currentForum.getId())) {
					Post lastPost = forumLastPosts.get(currentForum.getId());
					Date lastPostDate = lastPost.getCreateDate();
					if (lastPostDate != null && userLastLogin != null && lastPostDate.compareTo(userLastLogin) > 0) {
						folderAlt = "New_posts"; // bundle key
						folderImage = themeHelper.getResourceForumNewURL();
					}
				}
				if (currentForum.getStatus() == FORUM_LOCKED) {
					folderImage = themeHelper.getResourceForumLockedURL();
					folderAlt = "Forum_locked"; // bundle key
				}
				getForumImages().put(currentForum.getId(), folderImage);
				getForumImageDescriptions().put(currentForum.getId(), folderAlt);
			}
			getForums().put(category.getId(), categoryForums);
		}
	}
}
