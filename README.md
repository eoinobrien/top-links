#Top Links

Stores the links from the tweets that were passed in to the system. The top of which can be fetched using a curl request.

This is a Tech Test for [Eagle Alpha](www.eaglealpha.com)

Currently gets tweets from Twitter Streaming API, but will be changed to the Eagle Alpha in time.

## Prerequisites
1. A local instance of Elasticsearch
2. Gradle


## To Run

    export TWITTER_OAUTH='[Twitter Streaming API Authorization header]'
e.g.

    export TWITTER_OAUTH='OAuth oauth_consumer_key="abj3jUbPgjgyxvENqQQ9Q", oauth_nonce="3d74a50056d5e00eecc8768951c97403", oauth_signature="EhSFoHZhJX3eB1IbJFXAiKsOV1U%3D", oauth_signature_method="HMAC-SHA1", oauth_timestamp="1458521487", oauth_token="113698616-IubDrbdhvRVYqG7mUakQqKwx4DUzTvwC693D4syb", oauth_version="1.0"'
    
then: 
    
    gradle run

## To Get Top N Results in last x Hours
Change `6h` to the number of hours to get the total of.
Change the value of `"size" : 10` to change the number of returned elements. Note: The higher this number the more accurate the values will be.

    curl -XPOST http://localhost:9200/top-links/_search -d '
     {
       "size": 0,
       "query": {
         "range": {
           "timestamp": {
             "gte": "now-6h/s",
             "lt": "now/s"
           }
         }
       },
       "aggregations": {
         "urls": {
           "terms": {
             "field": "url",
             "size" : 10
           }
         }
       }
    }

