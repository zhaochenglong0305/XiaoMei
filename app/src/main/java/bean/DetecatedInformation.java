package bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/3.
 */

public class DetecatedInformation implements Serializable {
    private static final long serialVersionUID = -3606515544154647003L;
    private String from;
    private String to;
    private String type;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DetecatedInformation{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
