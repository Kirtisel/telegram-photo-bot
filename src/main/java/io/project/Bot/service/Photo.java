package io.project.Bot.service;

import java.io.File;
import java.io.Serializable;

public class Photo implements Serializable {
    private String caption;
    private String userSender;
    private File file;

    public Photo(File file, String userSender) {
        this.userSender = userSender;
        this.file = file;
    }

    public Photo(File file, String caption, String userSender) {
        this.caption = caption;
        this.userSender = userSender;
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public String getUserSender() {
        return userSender;
    }

    public String getCaption() {
        return caption;
    }
}
