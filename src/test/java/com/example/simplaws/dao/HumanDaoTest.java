package com.example.simplaws.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.TableDescription;

public class HumanDaoTest {

    private static AdjustedDynamoDbClient adjustedDynamoDbClient;
    private static HumanDao dao;

    @BeforeAll
    public static void init() {
        AdjustedDynamoDbClient dynamoDbClient = new AdjustedDynamoDbClient();
        adjustedDynamoDbClient = dynamoDbClient;
        dao = new HumanDao(new AdjustedDynamoDbClient());
    }

    @Test
    public void shouldCreateDBIfAbsent() {
        DynamoDbClient dynamoDbClient = adjustedDynamoDbClient.getDynamoDbClient();
        DescribeTableRequest request = DescribeTableRequest.builder()
                .tableName(HumanDao.TABLE_NAME)
                .build();

        TableDescription tableInfo = dynamoDbClient.describeTable(request).table();
        Assertions.assertNotNull(tableInfo);
    }

}

