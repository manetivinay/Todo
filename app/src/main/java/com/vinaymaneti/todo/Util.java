package com.vinaymaneti.todo;

/**
 * Created by vinaymaneti on 9/28/16.
 */

public class Util {

    public static String getPriorityString(int priorityStatus) {
        if (PriorityStatusCode.HIGH.getCode() == priorityStatus) {
            return PriorityStatusCode.HIGH.getPriorityStatus();
        } else if (PriorityStatusCode.MEDIUM.getCode() == priorityStatus) {
            return PriorityStatusCode.MEDIUM.getPriorityStatus();
        } else {
            return PriorityStatusCode.LOW.getPriorityStatus();
        }
    }
}
