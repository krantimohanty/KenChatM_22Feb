package com.kencloud.workchat.app_model;


public class GruopMessage {

    public String message;
    public String author;
    public String filename;
    public int type; // Text/Image/Audio/Video/Doc

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public GruopMessage() {
    }

    public GruopMessage(String message, String author, String filename, int type) {
        this.message = message;
        this.author = author;
        this.filename = filename;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public String getFilename(){
        return filename;
    }

    public int getType() { return type; }
}
