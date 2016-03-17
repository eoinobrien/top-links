package com.eoinobrien;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by eoin on 15/03/16.
 */
public class TopLinks {
    public static String streamFromAPI() {
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL("https://stream.twitter.com/1.1/statuses/sample.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "OAuth oauth_consumer_key=\"abj3jUbPgjgyxvENqQQ9Q\", oauth_nonce=\"c3a215e9f5c24ec6192a19a29880e963\", oauth_signature=\"7%2FScyqvW0%2FS8ZA9PuafCaIDQIxg%3D\", oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp=\"1458140201\", oauth_token=\"113698616-IubDrbdhvRVYqG7mUakQqKwx4DUzTvwC693D4syb\", oauth_version=\"1.0\"");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            ArrayList<TweetUrl> tweetUrls;
            while ((line = rd.readLine()) != null) {
                result.append(line);
                tweetUrls = getUrlFromTweet(line);

                System.out.println(tweetUrls);
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
        if (json.has("created_at") && json.getJSONObject("entities").has("urls")) {
            Date tweetTimestamp = new Date(json.getLong("timestamp_ms"));

            for (Object urlObj : json.getJSONObject("entities").getJSONArray("urls")) {
                tweetUrls.add(new TweetUrl(((JSONObject) urlObj).getString("expanded_url"), tweetTimestamp));
            }
        }
        return tweetUrls;
    }

    public static void main(String[] args) throws Exception {
        streamFromAPI();
    }
}
