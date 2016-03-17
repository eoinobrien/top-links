package com.eoinobrien;

import java.util.Date;

/**
 * Created by eoin on 16/03/16.
 */
public class TweetUrl {
    String url;
    Date timestamp;

    public TweetUrl(){}
    public TweetUrl(String url, Date timestamp){
        this.url = url;
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String toString() {
        return "TweetUrl{url:\"" + this.getUrl() + "\", timestamp: " + this.getTimestamp() + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TweetUrl){
            return this.getTimestamp().equals (((TweetUrl) obj).getTimestamp()) && this.getUrl().equals(((TweetUrl) obj).getUrl());
        }
        return false;
    }
}
