package com.eoinobrien;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;


/**
 * Created by eoin on 16/03/16.
 */
public class TopLinksTest {
    @Test
    public void testGetUrlFromEmptyTweet() {
        String jsonString = "{}";
        assertEquals(new ArrayList<TopLinks>(), TopLinks.getUrlFromTweet(jsonString));
    }

    @Test
    public void testGetUrlFromNoUrls() {
        String jsonString = "{\"_id\": 712263505198620681,\"updated_at\": \"2016-03-22T13:03:51.062Z\",\"created_at\": \"2016-03-22T13:03:47.000Z\",\"timestamp_ms\": 1458651827512,\"text\": \"RT @FHFA: U.S. house prices up 0.5% in January; up 6.0% YoY.\\n#breaking #housing\"}\n";
        assertEquals(new ArrayList<TweetUrl>(), TopLinks.getUrlFromTweet(jsonString));
    }

    @Test
    public void testGetUrlFromTweetSingleUrls() {
        String jsonString = "{\"_id\": 712263505198620681,\"updated_at\": \"2016-03-22T13:03:51.062Z\",\"created_at\": \"2016-03-22T13:03:47.000Z\",\"timestamp_ms\": 1458651827512,\"text\": \"RT @FHFA: U.S. house prices up 0.5% in January; up 6.0% YoY. https://t.co/6kq2tRBaIB\\n#breaking #housing https://t.co/Von5eVhvJO\",\"entities\": {\"urls\": [{\"expandedUrl\": \"https://www.facebook.com/\",\"url\": \"https://t.co/6kq2tRBaIB\",\"displayUrl\": \"facebook.com\"}]}}";

        Date timestamp = new Date(Long.parseLong("1458651827512"));
        ArrayList<TweetUrl> tweetUrls = new ArrayList<>();
        tweetUrls.add(new TweetUrl("https://www.facebook.com/", timestamp));

        assertEquals(tweetUrls, TopLinks.getUrlFromTweet(jsonString));
    }

    @Test
    public void testGetUrlFromTweetMultipleUrls() {
        String jsonString = "{\"_id\": 712263505198620681,\"updated_at\": \"2016-03-22T13:03:51.062Z\",\"created_at\": \"2016-03-22T13:03:47.000Z\",\"timestamp_ms\": 1458651827512,\"text\": \"RT @FHFA: U.S. house prices up 0.5% in January; up 6.0% YoY. https://t.co/6kq2tRBaIB\\n#breaking #housing https://t.co/Von5eVhvJO\",\"entities\": {\"urls\": [{\"expandedUrl\": \"https://www.facebook.com/\",\"url\": \"https://t.co/6kq2tRBaIB\",\"displayUrl\": \"facebook.com\"},{\"expandedUrl\": \"http://www.theverge.com/\",\"url\": \"https://t.co/6kq2tRBaIB\",\"displayUrl\": \"theverge.com\"},{\"expandedUrl\": \"https://www.youtube.com/\",\"url\": \"https://t.co/6kq2tRBaIB\",\"displayUrl\": \"youtube.com\"}]}}";

        Date timestamp = new Date(Long.parseLong("1458651827512"));
        ArrayList<TweetUrl> tweetUrls = new ArrayList<>();
        tweetUrls.add(new TweetUrl("https://www.facebook.com/", timestamp));
        tweetUrls.add(new TweetUrl("http://www.theverge.com/", timestamp));
        tweetUrls.add(new TweetUrl("https://www.youtube.com/", timestamp));

        assertEquals(tweetUrls, TopLinks.getUrlFromTweet(jsonString));
    }


}
