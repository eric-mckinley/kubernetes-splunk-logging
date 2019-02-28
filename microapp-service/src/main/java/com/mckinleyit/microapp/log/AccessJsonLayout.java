package com.mckinleyit.microapp.log;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.classic.pattern.ThrowableHandlingConverter;
import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.contrib.json.JsonLayoutBase;

import java.util.LinkedHashMap;
import java.util.Map;

public class AccessJsonLayout extends JsonLayoutBase<IAccessEvent> {
    protected boolean includeLevel = true;
    protected boolean includeThreadName = true;
    protected boolean includeMDC = true;
    protected boolean includeLoggerName = true;
    protected boolean includeFormattedMessage = true;
    protected boolean includeMessage;
    protected boolean includeException = true;
    protected boolean includeContextName = true;

    private String logType;

    private ThrowableHandlingConverter throwableProxyConverter = new ThrowableProxyConverter();

    public AccessJsonLayout() {
    }

    public void start() {
        this.throwableProxyConverter.start();
        super.start();
    }

    public void stop() {
        super.stop();
        this.throwableProxyConverter.stop();
    }

    protected Map toJsonMap(IAccessEvent var1) {
        LinkedHashMap var2 = new LinkedHashMap();
        this.addTimestamp("timestamp", this.includeTimestamp, var1.getTimeStamp(), var2);
        this.add("logType", true, logType, var2);
        this.add("thread", this.includeThreadName, var1.getThreadName(), var2);
        this.add("contentlegnth", true, String.valueOf(var1.getContentLength()), var2);
        this.add("method", true, var1.getMethod(), var2);
        this.add("protocol", true, var1.getProtocol(), var2);
        this.add("uri", true, var1.getRequestURI(), var2);
        this.add("url", true, var1.getRequestURL(), var2);
        this.add("queryString", true, var1.getQueryString(), var2);
        this.add("remoteUser", true, var1.getRemoteUser(), var2);
        this.add("remoteHost", true, var1.getRemoteHost(), var2);
        var2.put("headers", var1.getRequestHeaderMap());
        var2.put("status", var1.getStatusCode());
        return var2;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public boolean isIncludeLevel() {
        return this.includeLevel;
    }

    public void setIncludeLevel(boolean var1) {
        this.includeLevel = var1;
    }

    public boolean isIncludeLoggerName() {
        return this.includeLoggerName;
    }

    public void setIncludeLoggerName(boolean var1) {
        this.includeLoggerName = var1;
    }

    public boolean isIncludeFormattedMessage() {
        return this.includeFormattedMessage;
    }

    public void setIncludeFormattedMessage(boolean var1) {
        this.includeFormattedMessage = var1;
    }

    public boolean isIncludeMessage() {
        return this.includeMessage;
    }

    public void setIncludeMessage(boolean var1) {
        this.includeMessage = var1;
    }

    public boolean isIncludeMDC() {
        return this.includeMDC;
    }

    public void setIncludeMDC(boolean var1) {
        this.includeMDC = var1;
    }

    public boolean isIncludeThreadName() {
        return this.includeThreadName;
    }

    public void setIncludeThreadName(boolean var1) {
        this.includeThreadName = var1;
    }

    public boolean isIncludeException() {
        return this.includeException;
    }

    public void setIncludeException(boolean var1) {
        this.includeException = var1;
    }

    public boolean isIncludeContextName() {
        return this.includeContextName;
    }

    public void setIncludeContextName(boolean var1) {
        this.includeContextName = var1;
    }

    public ThrowableHandlingConverter getThrowableProxyConverter() {
        return this.throwableProxyConverter;
    }

    public void setThrowableProxyConverter(ThrowableHandlingConverter var1) {
        this.throwableProxyConverter = var1;
    }
}

