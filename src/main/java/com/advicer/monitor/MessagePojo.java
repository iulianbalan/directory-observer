package com.advicer.monitor;

public class MessagePojo {
	
	private String action;
	private String file;
	private String fullPath;
	
	public MessagePojo() {
		super();
	}
	
	public MessagePojo(String action, String path) {
		super();
		this.action = action;
		this.file = path;
	}

	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}
	
	

}
