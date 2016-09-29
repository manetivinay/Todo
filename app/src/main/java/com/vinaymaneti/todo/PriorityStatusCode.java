package com.vinaymaneti.todo;

/**
 * Created by vinaymaneti on 9/28/16.
 */

public enum PriorityStatusCode {

    HIGH(1, "high"),
    MEDIUM(2, "medium"),
    LOW(3, "low");

    private final int code;
    private final String priorityStatus;

    PriorityStatusCode(int code, String priorityStatus) {
        this.code = code;
        this.priorityStatus = priorityStatus;
    }


    public int getCode() {
        return code;
    }

    public String getPriorityStatus() {
        return priorityStatus;
    }

    @Override
    public String toString() {
        return code + ":" + priorityStatus;
    }
}
