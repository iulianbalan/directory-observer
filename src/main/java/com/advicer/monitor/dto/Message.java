package com.advicer.monitor.dto;

/**
 * Class that provides a very basic model
 * of a message that is going to be serialized.
 * No need to comment all getters and setters
 *
 * @author Iulian Balan
 */
public class Message {

    private String action;
    private String file;
    private String fullPath;

    public Message() {
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
