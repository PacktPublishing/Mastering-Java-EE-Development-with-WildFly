package it.vige.webprogramming.javaserverfaces.format.render.bbcodehtml;

import java.util.HashSet;
import java.util.Set;

public class ToHTMLConfig {

	public static final String TPL_THEME_CODE_B_CLOSE = "b_close";
	public static final String TPL_THEME_CODE_B_OPEN = "b_open";
	public static final String TPL_THEME_CODE_CODE_CLOSE = "code_close";
	public static final String TPL_THEME_CODE_CODE_OPEN = "code_open";
	public static final String TPL_THEME_CODE_COLOR_CLOSE = "color_close";
	public static final String TPL_THEME_CODE_COLOR_OPEN = "color_open";
	public static final String TPL_THEME_CODE_EMAIL = "email";
	public static final String TPL_THEME_CODE_I_CLOSE = "i_close";
	public static final String TPL_THEME_CODE_I_OPEN = "i_open";
	public static final String TPL_THEME_CODE_IMG = "img";
	public static final String TPL_THEME_CODE_LISTITEM = "listitem";
	public static final String TPL_THEME_CODE_OLIST_CLOSE = "olist_close";
	public static final String TPL_THEME_CODE_OLIST_OPEN = "olist_open";
	public static final String TPL_THEME_CODE_QUOTE_CLOSE = "quote_close";
	public static final String TPL_THEME_CODE_QUOTE_OPEN = "quote_open";
	public static final String TPL_THEME_CODE_QUOTE_USERNAME_OPEN = "quote_username_open";
	public static final String TPL_THEME_CODE_SIZE_CLOSE = "size_close";
	public static final String TPL_THEME_CODE_SIZE_OPEN = "size_open";
	public static final String TPL_THEME_CODE_U_CLOSE = "u_close";
	public static final String TPL_THEME_CODE_U_OPEN = "u_open";
	public static final String TPL_THEME_CODE_ULIST_CLOSE = "ulist_close";
	public static final String TPL_THEME_CODE_ULIST_OPEN = "ulist_open";
	public static final String TPL_THEME_CODE_URL = "url";

	public static final int FILTER_MODE_ALWAYS_PRINT = 0;
	public static final int FILTER_MODE_NEVER_PRINT = 1;
	public static final int FILTER_MODE_PRINT_IF_NOT_IN_TAGS = 2;
	public static final int FILTER_MODE_PRINT_IF_IN_TAGS = 3;

	public static final int OUTPUT_MODE_REMOVE = 0;
	public static final int OUTPUT_MODE_DISPLAY = 1;

	private Set<String> tags = new HashSet<String>();
	private int filterMode = FILTER_MODE_ALWAYS_PRINT;
	private int ouputMode = OUTPUT_MODE_REMOVE;
	private int maxTextWidth = 100;

	public ToHTMLConfig() {
	}

	public int getMaxTextWidth() {
		return maxTextWidth;
	}

	public void setMaxTextWidth(int maxTextWidth) {
		this.maxTextWidth = maxTextWidth;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public Set<String> getTags() {
		return tags;
	}

	public int getFilterMode() {
		return filterMode;
	}

	public void setFilterMode(int filterMode) {
		this.filterMode = filterMode;
	}

	public int getOuputMode() {
		return ouputMode;
	}

	public void setOuputMode(int ouputMode) {
		this.ouputMode = ouputMode;
	}

	public boolean print(String tag) {
		switch (filterMode) {
		case FILTER_MODE_NEVER_PRINT:
			return false;
		case FILTER_MODE_PRINT_IF_NOT_IN_TAGS:
			return !tags.contains(tag);
		case FILTER_MODE_PRINT_IF_IN_TAGS:
			return tags.contains(tag);
		case FILTER_MODE_ALWAYS_PRINT:
			return true;
		}
		return false;
	}
}
