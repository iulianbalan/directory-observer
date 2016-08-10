package com.advicer.monitor.events;

public enum DirectoryObserverEventsConstants {

    PATH("p", "path"), CREATION("C"), DELETION("D"), MODIFICATION("M");

    //Making private fields final to avoid evil code.
    private final String flag;
    private final String arg;

    //Every constructor has to initialize all final fields not yet initialized
    DirectoryObserverEventsConstants(String flag) {
        this.flag = flag;
        this.arg = null;
    }

    DirectoryObserverEventsConstants(String flag, String arg) {
        this.flag = flag;
        this.arg = arg;
    }

    public String getFlag() {
        return this.flag;
    }

    public String getArg() {
        return this.arg;
    }

}
