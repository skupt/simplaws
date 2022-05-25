package com.example.simplaws.dao;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Component
public class AdjustedDynamoDbClient {
    private DynamoDbClient dynamoDbClient;
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public AdjustedDynamoDbClient() {
        createDynamoDbClient();
        createDynamoDbEnhancedClient();
    }

    public DynamoDbClient getDynamoDbClient() {
        if (dynamoDbClient == null) createDynamoDbClient();
        return dynamoDbClient;
    }

    public DynamoDbEnhancedClient getDynamoDbEnhancedClient() {
        if (dynamoDbEnhancedClient == null) createDynamoDbEnhancedClient();
        return dynamoDbEnhancedClient;
    }

    public void closeDynamoDbClient() {
        dynamoDbClient.close();
    }

    private void createDynamoDbClient() {
        Region region = Region.US_EAST_1;
        dynamoDbClient = DynamoDbClient.builder()
                .region(region)
                .build();
    }

    private void createDynamoDbEnhancedClient() {
        if (dynamoDbClient == null) createDynamoDbClient();
        dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }
}
