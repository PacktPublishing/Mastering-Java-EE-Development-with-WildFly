package it.vige.webprogramming.javaserverfaces.theme;

public class FolderType {

	public final String folder;
	public final String folderNew;
	public final String type;

	public FolderType(String folder, String folderNew, String type) {
		this.folder = folder;
		this.folderNew = folderNew;
		this.type = type;
	}

	public String getFolder() {
		return folder;
	}

	public String getFolderNew() {
		return folderNew;
	}

	public String getType() {
		return type;
	}
}