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
        String jsonString = "{\"created_at\":\"Tue Mar 15 23:25:09 +0000 2016\",\"text\":\"RT @Ellie_Shaw123:I just wanna sleep for a week straight\",\"entities\":{\"urls\":[],\"user_mentions\":[{\"screen_name\":\"Ellie_Shaw123\",\"name\":\"Ellie\",\"id\":2149625945,\"id_str\":\"2149625945\",\"indices\":[3, 17]}]},\"timestamp_ms\":\"1458084309660\"}";
        assertEquals(new ArrayList<TweetUrl>(), TopLinks.getUrlFromTweet(jsonString));
    }

    @Test
    public void testGetUrlFromTweetSingleUrls() {
        String jsonString = "{\"created_at\":\"Wed Mar 16 16:53:41 +0000 2016\",\"text\":\"Test Tweet https:\\/\\/t.co\\/aI3PP1jZ24 https:\\/\\/t.co\\/MsCwv59HKx https:\\/\\/t.co\\/5K2fwzmAOf\",\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[],\"urls\":[{\"url\":\"https:\\/\\/t.co\\/aI3PP1jZ24\",\"expanded_url\":\"https:\\/\\/www.facebook.com\\/\",\"display_url\":\"facebook.com\",\"indices\":[11,34]}]}, \"timestamp_ms\":\"1458143621000\"}";

        Date timestamp = new Date(Long.parseLong("1458143621000"));
        ArrayList<TweetUrl> tweetUrls = new ArrayList<>();
        tweetUrls.add(new TweetUrl("https://www.facebook.com/", timestamp));

        assertEquals(tweetUrls, TopLinks.getUrlFromTweet(jsonString));
    }

    @Test
    public void testGetUrlFromTweetMultipleUrls() {
        String jsonString = "{\"created_at\":\"Wed Mar 16 16:53:41 +0000 2016\",\"text\":\"Test Tweet https:\\/\\/t.co\\/aI3PP1jZ24 https:\\/\\/t.co\\/MsCwv59HKx https:\\/\\/t.co\\/5K2fwzmAOf\",\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[],\"urls\":[{\"url\":\"https:\\/\\/t.co\\/aI3PP1jZ24\",\"expanded_url\":\"https:\\/\\/www.facebook.com\\/\",\"display_url\":\"facebook.com\",\"indices\":[11,34]},{\"url\":\"https:\\/\\/t.co\\/MsCwv59HKx\",\"expanded_url\":\"http:\\/\\/www.theverge.com\\/\",\"display_url\":\"theverge.com\",\"indices\":[35,58]},{\"url\":\"https:\\/\\/t.co\\/5K2fwzmAOf\",\"expanded_url\":\"https:\\/\\/www.youtube.com\\/\",\"display_url\":\"youtube.com\",\"indices\":[59,82]}]}, \"timestamp_ms\":\"1458143621000\"}";

        Date timestamp = new Date(Long.parseLong("1458143621000"));
        ArrayList<TweetUrl> tweetUrls = new ArrayList<>();
        tweetUrls.add(new TweetUrl("https://www.facebook.com/", timestamp));
        tweetUrls.add(new TweetUrl("http://www.theverge.com/", timestamp));
        tweetUrls.add(new TweetUrl("https://www.youtube.com/", timestamp));

        assertEquals(tweetUrls, TopLinks.getUrlFromTweet(jsonString));
    }


}
