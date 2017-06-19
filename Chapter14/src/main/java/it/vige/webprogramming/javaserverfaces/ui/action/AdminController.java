package it.vige.webprogramming.javaserverfaces.ui.action;

import static it.vige.webprogramming.javaserverfaces.ui.JSFUtil.getBundleMessage;
import static it.vige.webprogramming.javaserverfaces.ui.JSFUtil.handleException;
import static it.vige.webprogramming.javaserverfaces.ui.JSFUtil.setMessage;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.ModuleException;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.model.Category;
import it.vige.rubia.model.Forum;
import it.vige.rubia.model.ForumInstance;
import it.vige.webprogramming.javaserverfaces.auth.AuthorizationListener;
import it.vige.webprogramming.javaserverfaces.ui.BaseController;
import it.vige.webprogramming.javaserverfaces.ui.ForumUtil;

@Named
@RequestScoped
public class AdminController extends BaseController {

	private static final long serialVersionUID = -1977660655542809904L;
	@EJB
	private ForumsModule forumsModule;

	private static final int up = -15;
	private static final int down = 15;

	private String categoryName;

	private int selectedCategory = -1;

	private boolean editCategoryMode;
	private boolean addCategoryMode;

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(int selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public boolean isEditCategoryMode() {
		return editCategoryMode;
	}

	public boolean isAddCategoryMode() {
		return addCategoryMode;
	}

	public void cleanup() {
		categoryName = null;
		selectedCategory = -1;
		editCategoryMode = false;
		addCategoryMode = false;
	}

	@PostConstruct
	public void startService() {
		try {
			int categoryId = -1;
			String cour = ForumUtil.getParameter(p_categoryId);
			if (cour != null && cour.trim().length() > 0) {
				categoryId = Integer.parseInt(cour);
			}
			if (categoryId != -1) {
				Category category = null;
				try {
					category = forumsModule.findCategoryById(categoryId);
				} catch (ModuleException e) {
				}
				if (category != null) {
					categoryName = category.getTitle();
					selectedCategory = category.getId().intValue();
				}
			}

			int forumId = -1;
			String forumIdStr = ForumUtil.getParameter(p_forumId);
			if (forumIdStr != null && forumIdStr.trim().length() > 0) {
				forumId = Integer.parseInt(forumIdStr);
			}
			if (forumId != -1) {
				Forum forum = null;
				try {
					forum = forumsModule.findForumById(forumId);
				} catch (ModuleException e) {
				}
				if (forum != null) {
					selectedCategory = forum.getCategory().getId().intValue();
				}
			}

			String editCatStr = ForumUtil.getParameter(EDIT_CATEGORY);
			if (editCatStr != null && editCatStr.trim().length() > 0) {
				editCategoryMode = Boolean.valueOf(editCatStr).booleanValue();
			}

			String addCatStr = ForumUtil.getParameter(ADD_CATEGORY);
			if (addCatStr != null && addCatStr.trim().length() > 0) {
				addCategoryMode = Boolean.valueOf(addCatStr).booleanValue();
			}
		} catch (Exception e) {
			handleException(e);
		}
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String addCategory() {
		String navState = null;
		boolean success = false;
		try {
			int forumInstanceId = 1;

			ForumInstance forumInstance = forumsModule.findForumInstanceById(forumInstanceId);

			forumsModule.createCategory(categoryName, forumInstance);

			String start = getBundleMessage("ResourceJSF", "Category_created_0");
			String end = getBundleMessage("ResourceJSF", "Category_created_1");
			setMessage(FEEDBACK, start + " \"" + categoryName + "\" " + end);
			success = true;
		} catch (Exception e) {
			handleException(e);
		} finally {
			if (success) {
				cleanup();
			}
		}
		return navState;
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String editCategory() {
		String navState = null;
		boolean success = false;
		try {
			int categoryId = -1;
			String cour = ForumUtil.getParameter(p_categoryId);
			if (cour != null && cour.trim().length() > 0) {
				categoryId = Integer.parseInt(cour);
			}

			Category category = forumsModule.findCategoryById(categoryId);
			category.setTitle(categoryName);
			forumsModule.update(category);

			String start = getBundleMessage("ResourceJSF", "Category_updated_0");
			String end = getBundleMessage("ResourceJSF", "Category_updated_1");
			setMessage(FEEDBACK, start + " \"" + categoryName + "\" " + end);

			navState = "";
			success = true;
		} catch (Exception e) {
			handleException(e);
		} finally {
			if (success) {
				cleanup();
			}
		}
		return navState;
	}

	public String deleteCategory() {
		String navState = null;
		boolean success = false;
		try {
			int categoryId = -1;
			String cour = ForumUtil.getParameter(p_categoryId);
			if (cour != null && cour.trim().length() > 0) {
				categoryId = Integer.parseInt(cour);
			}

			Category source = forumsModule.findCategoryById(categoryId);

			if (selectedCategory != -1) {

				Category target = forumsModule.findCategoryById(selectedCategory);

				forumsModule.addAllForums(source, target);

			}

			forumsModule.removeCategory(source.getId());

			String start = getBundleMessage("ResourceJSF", "Category_deleted_0");
			String end = getBundleMessage("ResourceJSF", "Category_deleted_1");
			setMessage(FEEDBACK, start + " \"" + categoryName + "\" " + end);

			navState = DELETE_CATEGORY;
			success = true;
		} catch (Exception e) {
			handleException(e);
		} finally {
			if (success) {
				cleanup();
			}
		}
		return navState;
	}

	public String moveCategoryUp() {
		String navState = null;
		try {
			int categoryId = -1;
			String cour = ForumUtil.getParameter(p_categoryId);
			if (cour != null && cour.trim().length() > 0) {
				categoryId = Integer.parseInt(cour);
			}

			Category category = forumsModule.findCategoryById(categoryId);
			category.setOrder(category.getOrder() + up);
			forumsModule.update(category);

			int forumInstanceId = 1;

			Iterator<Category> categories = forumsModule.findCategories(forumInstanceId).iterator();

			for (int index = 10; categories.hasNext(); index += 10) {
				category = categories.next();
				category.setOrder(index);
				forumsModule.update(category);
			}
		} catch (Exception e) {
			handleException(e);
		}
		return navState;
	}

	public String moveCategoryDown() {
		String navState = null;
		try {
			int categoryId = -1;
			String cour = ForumUtil.getParameter(p_categoryId);
			if (cour != null && cour.trim().length() > 0) {
				categoryId = Integer.parseInt(cour);
			}

			Category category = forumsModule.findCategoryById(categoryId);
			category.setOrder(category.getOrder() + down);
			forumsModule.update(category);

			int forumInstanceId = 1;

			Iterator<Category> categories = forumsModule.findCategories(forumInstanceId).iterator();

			for (int index = 10; categories.hasNext(); index += 10) {
				category = categories.next();
				category.setOrder(index);
				forumsModule.update(category);
			}
		} catch (Exception e) {
			handleException(e);
		}
		return navState;
	}

	public String cancel() {
		cleanup();
		return "cancel";
	}
}
