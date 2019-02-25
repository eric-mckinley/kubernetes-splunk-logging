package com.mckinleyit.microapp.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.json.classic.JsonLayout;

import java.util.Map;

public class AppJsonLayout extends JsonLayout {

    private String logType;

    public void setLogType(String logType) {
        this.logType = logType;
    }

    @Override
    protected void addCustomDataToJsonMap(Map<String, Object> map, ILoggingEvent event) {
        map.put("logType", logType);
    }
}

