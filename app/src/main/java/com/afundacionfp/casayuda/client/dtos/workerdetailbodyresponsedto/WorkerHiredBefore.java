package com.afundacionfp.casayuda.client.dtos.workerdetailbodyresponsedto;

/**
 * Stores worker appointment history
 */
public class WorkerHiredBefore {
    private String datetime;
    private int hirerId;
    private boolean isAccepted;

    public WorkerHiredBefore(String date, int hirerId, boolean isAccepted ) {
        this.datetime = date;
        this.hirerId = hirerId;
        this.isAccepted = isAccepted;
    }

    public String getDatetime() {
        return datetime;
    }

    public int getHirerId() {
        return hirerId;
    }

    public boolean isAccepted() {
        return isAccepted;
    }
}
