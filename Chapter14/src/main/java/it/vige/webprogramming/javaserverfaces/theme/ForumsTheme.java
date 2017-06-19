package it.vige.webprogramming.javaserverfaces.theme;

import static it.vige.rubia.Constants.TOPIC_LOCKED;

import it.vige.rubia.model.TopicType;
import it.vige.webprogramming.javaserverfaces.properties.XProperties;

public class ForumsTheme {
	
	public FolderType getFolderType(TopicType type, int status, boolean hot) {
		switch (type) {
		case ADVICE:
			if (status == TOPIC_LOCKED) {
				return FOLDER_ANNOUNCE_LOCKED;
			} else {
				return FOLDER_ANNOUNCE;
			}

		case IMPORTANT:
			if (status == TOPIC_LOCKED) {
				return FOLDER_STICKY_LOCKED;
			} else {
				return FOLDER_STICKY;
			}

		default: {
			if (status == TOPIC_LOCKED) {
				return FOLDER_NORMAL_LOCKED;
			} else {
				if (hot) {
					return FOLDER_HOT;
				} else {
					return FOLDER_NORMAL;
				}
			}
		}
		}
	}

	public FolderType FOLDER_ANNOUNCE;
	public FolderType FOLDER_STICKY;
	public FolderType FOLDER_ANNOUNCE_LOCKED;
	public FolderType FOLDER_STICKY_LOCKED;
	public FolderType FOLDER_NORMAL_LOCKED;
	public FolderType FOLDER_NORMAL;
	public FolderType FOLDER_HOT;
	public String resourceForumURL;
	public String resourceForumNewURL;
	public String resourceForumNewBigURL;
	public String resourceForumLockedURL;
	public String resourceIconLatestReplyURL;
	public String resourceIconNewestReplyURL;
	public String resourceIconGotopostURL;
	public String resourceIconFeedURL;
	public String resourceFolderURL;
	public String resourceFolderNewURL;
	public String resourceFolderAnnounceURL;
	public String resourceFolderAnnounceNewURL;
	public String resourceFolderStickyURL;
	public String resourceFolderStickyNewURL;
	public String resourceFolderLockedURL;
	public String resourceFolderLockedNewURL;
	public String resourceFolderHotURL;
	public String resourceFolderHotNewURL;
	public String resourcePostLockedURL;
	public String resourcePollVotingBar;
	public String resourcePollVotingLBar;
	public String resourcePollVotingRBar;
	public String resourcePostNewURL;
	public String resourceTopicModeDeleteURL;
	public String resourceTopicModeDeleteDisaURL;
	public String resourceTopicModMoveURL;
	public String resourceTopicModLockURL;
	public String resourceTopicModUnlockURL;
	public String resourceTopicModSplitURL;
	public String resourceAdminNewCategoryURL;
	public String resourceAdminNewForumURL;
	public String resourceAdminArrowUpURL;
	public String resourceAdminArrowUpDisaURL;
	public String resourceAdminArrowDownURL;
	public String resourceAdminArrowDownDisaURL;
	public String resourceTopicUnWatchURL;
	public String resourceTopicWatchURL;
	public String resourceReplyNewURL;
	public String resourceReplyLockedURL;
	public String resourceIconProfileURL;
	public String resourceIconPMURL;
	public String resourceIconEmailURL;
	public String resourceIconWWWURL;
	public String resourceIconICQURL;
	public String resourceIconAIMURL;
	public String resourceIconMSNMURL;
	public String resourceIconSkypeURL;
	public String resourceIconYIMURL;
	public String resourceIconQuoteURL;
	public String resourceIconSearchURL;
	public String resourceIconEditURL;
	public String resourceIconRepostURL;
	public String resourceIconIPURL;
	public String resourceIconDelpostURL;
	public String resourceIconMinipostURL;
	public String resourceIconMinipostNewURL;
	public String resourceIconSubscribeURL;
	public String resourceIconUnSubscribeURL;
	public String resourceIconLockURL;
	public String resourceIconUnlockURL;
	public String resourceIconDialogWarningURL;
	public String resourceIconDialogErrorURL;
	public String resourceIconDialogQuestionURL;
	public String resourceIconModerateURL;
	public String resourceIconForumsLogoURL;
	public String resourceHeadStylesheetURL;
	private XProperties xprops;
	
	public void setExtendedProperties(XProperties xprops) {
		this.xprops = xprops;
	}

	public String getProperty(String name) {
		return xprops.getProperty(name);
	}

	public String getResourceURL(String name) {
		return xprops.getResourceURL(name);
	}

	public void start() throws Exception {
		resourceForumURL = xprops.getResourceURL("forum");
		resourceForumNewURL = xprops.getResourceURL("forum_new");
		resourceForumNewBigURL = xprops.getResourceURL("forum_new_big");
		resourceForumLockedURL = xprops.getResourceURL("forum_locked");
		resourceIconLatestReplyURL = xprops.getResourceURL("icon_latest_reply");
		resourceIconNewestReplyURL = xprops.getResourceURL("icon_newest_reply");
		resourceIconGotopostURL = xprops.getResourceURL("icon_gotopost");
		resourceIconFeedURL = xprops.getResourceURL("icon_feed");
		resourceFolderURL = xprops.getResourceURL("folder");
		resourceFolderNewURL = xprops.getResourceURL("folder_new");
		resourceFolderAnnounceURL = xprops.getResourceURL("folder_announce");
		resourceFolderAnnounceNewURL = xprops.getResourceURL("folder_announce_new");
		resourceFolderStickyURL = xprops.getResourceURL("folder_sticky");
		resourceFolderStickyNewURL = xprops.getResourceURL("folder_sticky_new");
		resourceFolderLockedURL = xprops.getResourceURL("folder_locked");
		resourceFolderLockedNewURL = xprops.getResourceURL("folder_locked_new");
		resourceFolderHotURL = xprops.getResourceURL("folder_hot");
		resourceFolderHotNewURL = xprops.getResourceURL("folder_hot_new");
		resourcePollVotingBar = xprops.getResourceURL("voting_graphic_0");
		resourcePollVotingLBar = xprops.getResourceURL("vote_lcap");
		resourcePollVotingRBar = xprops.getResourceURL("vote_rcap");
		resourcePostLockedURL = xprops.getResourceURL("post_locked");
		resourcePostNewURL = xprops.getResourceURL("post_new");
		resourceTopicModeDeleteURL = xprops.getResourceURL("topic_mod_delete");
		resourceTopicModeDeleteDisaURL = xprops.getResourceURL("topic_mod_delete_disa");
		resourceTopicModMoveURL = xprops.getResourceURL("topic_mod_move");
		resourceTopicModLockURL = xprops.getResourceURL("topic_mod_lock");
		resourceTopicModUnlockURL = xprops.getResourceURL("topic_mod_unlock");
		resourceTopicModSplitURL = xprops.getResourceURL("topic_mod_split");
		resourceAdminNewCategoryURL = xprops.getResourceURL("admin_newcategory");
		resourceAdminNewForumURL = xprops.getResourceURL("admin_newforum");
		resourceAdminArrowUpURL = xprops.getResourceURL("admin_arrowup");
		resourceAdminArrowUpDisaURL = xprops.getResourceURL("admin_arrowup_disa");
		resourceAdminArrowDownURL = xprops.getResourceURL("admin_arrowdown");
		resourceAdminArrowDownDisaURL = xprops.getResourceURL("admin_arrowdown_disa");
		resourceTopicUnWatchURL = xprops.getResourceURL("topic_un_watch");
		resourceTopicWatchURL = xprops.getResourceURL("topic_watch");
		resourceReplyNewURL = xprops.getResourceURL("reply_new");
		resourceReplyLockedURL = xprops.getResourceURL("reply_locked");
		resourceIconProfileURL = xprops.getResourceURL("icon_profile");
		resourceIconPMURL = xprops.getResourceURL("icon_pm");
		resourceIconEmailURL = xprops.getResourceURL("icon_email");
		resourceIconWWWURL = xprops.getResourceURL("icon_www");
		resourceIconICQURL = xprops.getResourceURL("icon_icq");
		resourceIconAIMURL = xprops.getResourceURL("icon_aim");
		resourceIconMSNMURL = xprops.getResourceURL("icon_msnm");
		resourceIconSkypeURL = xprops.getResourceURL("icon_skype");
		resourceIconYIMURL = xprops.getResourceURL("icon_yim");
		resourceIconQuoteURL = xprops.getResourceURL("icon_quote");
		resourceIconSearchURL = xprops.getResourceURL("icon_search");
		resourceIconEditURL = xprops.getResourceURL("icon_edit");
		resourceIconRepostURL = xprops.getResourceURL("icon_repost");
		resourceIconIPURL = xprops.getResourceURL("icon_ip");
		resourceIconDelpostURL = xprops.getResourceURL("icon_delpost");
		resourceIconMinipostURL = xprops.getResourceURL("icon_minipost");
		resourceIconMinipostNewURL = xprops.getResourceURL("icon_minipost_new");
		resourceIconSubscribeURL = xprops.getResourceURL("forum_subscribe");
		resourceIconUnSubscribeURL = xprops.getResourceURL("forum_unsubscribe");
		resourceIconUnlockURL = xprops.getResourceURL("unlock");
		resourceIconLockURL = xprops.getResourceURL("lock");
		resourceIconModerateURL = xprops.getResourceURL("moderate");
		resourceIconForumsLogoURL = xprops.getResourceURL("forums_logo");
		resourceIconDialogWarningURL = xprops.getResourceURL("icon_dialog_warning");
		resourceIconDialogQuestionURL = xprops.getResourceURL("icon_dialog_question");
		resourceIconDialogErrorURL = xprops.getResourceURL("icon_dialog_error");
		resourceHeadStylesheetURL = xprops.getResourceURL("head_stylesheet");

		FOLDER_ANNOUNCE = new FolderType(resourceFolderAnnounceURL, resourceFolderAnnounceNewURL, "Topic_Announcement");
		FOLDER_STICKY = new FolderType(resourceFolderStickyURL, resourceFolderStickyNewURL, "Topic_Sticky");
		FOLDER_ANNOUNCE_LOCKED = new FolderType(resourceFolderLockedURL, resourceFolderLockedNewURL,
				"Topic_Announcement");
		FOLDER_STICKY_LOCKED = new FolderType(resourceFolderLockedURL, resourceFolderLockedNewURL, "Topic_Sticky");
		FOLDER_NORMAL_LOCKED = new FolderType(resourceFolderLockedURL, resourceFolderLockedNewURL, "");
		FOLDER_HOT = new FolderType(resourceFolderHotURL, resourceFolderHotNewURL, "");
		FOLDER_NORMAL = new FolderType(resourceFolderURL, resourceForumNewURL, "");
	}
}
