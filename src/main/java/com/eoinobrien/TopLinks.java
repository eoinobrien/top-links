package com.eoinobrien;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by eoin on 15/03/16.
 */
public class TopLinks {
    private static final Logger logger = LoggerFactory.getLogger(TopLinks.class);

    private static String streamFromAPI(Elasticsearch es) {
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL("http://beta.api.eaglealpha.com/streams/dev/social_feeds.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", System.getenv("EAGLE_ALPHA_API"));

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            ArrayList<TweetUrl> tweetUrls;

            String date;
            String previousDate = "";
            String type = "links-0000";

            while ((line = rd.readLine()) != null) {
                result.append(line);
                tweetUrls = getUrlFromTweet(line);

                date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                if(!previousDate.equals(date)){
                    type = "links-" + date;
                    es.addMappingToType(type);
                    previousDate = date;
                }

                for (TweetUrl tweetUrl : tweetUrls) {
                    es.addItemToBulkProcessor(tweetUrl, type);
                }
            }
            rd.close();
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<TweetUrl> getUrlFromTweet(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        ArrayList<TweetUrl> tweetUrls = new ArrayList<>();
        if (json.has("created_at") && json.has("entities") && json.getJSONObject("entities").has("urls")) {
            Date tweetTimestamp = new Date(json.getLong("timestamp_ms"));

            for (Object urlObj : json.getJSONObject("entities").getJSONArray("urls")) {
                tweetUrls.add(new TweetUrl(((JSONObject) urlObj).getString("expandedUrl"), tweetTimestamp));
            }
        }
        return tweetUrls;
    }

    public static void main(String[] args) throws Exception {
        String indexName = "top-links";
        Elasticsearch es = new Elasticsearch(indexName);

        streamFromAPI(es);
    }
}
