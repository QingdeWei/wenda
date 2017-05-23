package com.nowcoder.async;

import java.util.List;

/**
 * Created by ZGH on 2017/5/16.
 */
public interface EventHandler {
    void doHandler(EventModel model);
    List<EventType> getSupportEventTypes();
}
