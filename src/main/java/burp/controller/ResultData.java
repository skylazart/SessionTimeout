package burp.controller;

import javafx.beans.property.SimpleStringProperty;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/7/17.
 */
public class ResultData {
    private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("hh:mm:ss.SSS dd MMM yyyy");
    private final SimpleStringProperty time;
    private final SimpleStringProperty msg;
    private final SimpleStringProperty status;
    private final SimpleStringProperty rawMsg;

    public ResultData(String msg) {
        this(msg, "-", "-");
    }

    public ResultData(String msg, String status, String rawMsg) {
        this.time = new SimpleStringProperty(fmt.print(new DateTime()));
        this.msg = new SimpleStringProperty(msg);
        this.status = new SimpleStringProperty(status);
        this.rawMsg = new SimpleStringProperty(rawMsg);
    }

    public String getTime() {
        return time.get();
    }

    public String timeProperty() {
        return time.get();
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getMsg() {
        return msg.get();
    }

    public String msgProperty() {
        return msg.get();
    }

    public void setMsg(String msg) {
        this.msg.set(msg);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public String getRawMsg() {
        return rawMsg.get();
    }

    public SimpleStringProperty rawMsgProperty() {
        return rawMsg;
    }
}
