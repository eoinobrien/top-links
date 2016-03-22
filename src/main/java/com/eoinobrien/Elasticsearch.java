package com.eoinobrien;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 * Created by eoin on 20/03/16.
 */
public class Elasticsearch {
    private final Logger logger = LoggerFactory.getLogger(Elasticsearch.class);
    public BulkProcessor bulkProcessor;

    private String index;
    private Client client;

    public Elasticsearch(String index) {
        this.index = index;
        Node node = nodeBuilder().node();

        client = node.client();
        createIndex(this.index);
        setUpBulkProcessor();
    }

    private boolean createIndex(String name) {
        if(checkIfIndexExists(name)){
            return true;
        }

        logger.info("Creating index: " + index);
        CreateIndexResponse response = client.admin().indices().prepareCreate(name).execute().actionGet();

        return response.isAcknowledged();
    }

    public boolean checkIfIndexExists(String name){
        boolean indexExists = client.admin().indices().prepareExists(name).execute().actionGet().isExists();
        logger.info("Index " + index + " exists: " + indexExists);
        return indexExists;
    }

    public boolean addMappingToType(String type) {
        try {
            XContentBuilder mapping = jsonBuilder()
                    .startObject()
                    .startObject("properties")
                    .startObject("url")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("timestamp")
                    .field("type", "date")
                    .endObject()
                    .endObject()
                    .endObject();

            PutMappingResponse putMappingResponse = client.admin().indices()
                    .preparePutMapping(index)
                    .setType(type)
                    .setSource(mapping)
                    .execute().actionGet();

            return putMappingResponse.isAcknowledged();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setUpBulkProcessor() {
        bulkProcessor = BulkProcessor.builder(
                client,
                new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId,
                                           BulkRequest request) {
                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          BulkResponse response) {
                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          Throwable failure) {
                    }
                })
                .setBulkActions(10000)
                .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(1)
                .build();
    }

    public BulkProcessor addItemToBulkProcessor(TweetUrl tweetUrl, String type) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return bulkProcessor.add(new IndexRequest(index, type).source(mapper.writeValueAsBytes(tweetUrl)));
    }
}
