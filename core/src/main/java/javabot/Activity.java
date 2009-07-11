package javabot;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

public class Activity implements Serializable {
    private String channel;
    private Date start;
    private Date end;
    private long count;
    private long total;

    public Activity(String chan, long chanCount, Date endDate, Date startDate, long totalRowCount) {
        channel = chan;
        count = chanCount;
        end = endDate;
        start = startDate;
        total = totalRowCount;
    }

    public String getChannel() {
        return channel;
    }

    public long getCount() {
        return count;
    }

    public Date getEnd() {
        return end;
    }

    public Date getStart() {
        return start;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(final long total) {
        this.total = total;
    }

    public String getPercent() {
        return new DecimalFormat("##0.00").format(100.0*count/total);
    }
}
