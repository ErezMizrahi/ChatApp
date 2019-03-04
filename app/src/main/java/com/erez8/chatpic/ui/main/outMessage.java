package com.erez8.chatpic.ui.main;

public class outMessage {

    private String outMsg;
    private String date;

    public outMessage(String inMsg, String date) {
        this.outMsg = inMsg;
        this.date = date;
    }

    public String getOutMsg() {
        return outMsg;
    }

    public void setOutMsg(String outMsg) {
        this.outMsg = outMsg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
