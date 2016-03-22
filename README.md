#Top Links

[![Build Status](https://travis-ci.org/eoinobrien/top-links.svg?branch=master)](https://travis-ci.org/eoinobrien/top-links)

Stores the links from the tweets that were passed in to the system. The top of which can be fetched using a curl request.

This is a Tech Test for [Eagle Alpha](www.eaglealpha.com)

Takes tweets from a a special [Eagle Alpha API](https://bitbucket.org/eaglealpha/streaming-api/wiki/Home), which puts each new tweet on a new line. This will not work unmodified on either the official Twitter API or the default Eagle Alpha API. 

## Prerequisites
1. A local instance of Elasticsearch 1.7
2. Gradle


## To Run
Add your Eagle Alpha API key as an environmental variable.

    export EAGLE_ALPHA_API='[Eagle Alpha API Key]'
    
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

