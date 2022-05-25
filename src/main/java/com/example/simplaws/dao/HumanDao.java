package com.example.simplaws.dao;

import com.example.simplaws.entities.Human;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class HumanDao implements DynamoDao<Long, Human> {
    public static final String TABLE_NAME = "humans";
    public static final String PARTITION_KEY = "id";

    private AdjustedDynamoDbClient adjustedDynamoDbClient;

    private static final Random random = new Random(System.nanoTime());

    public HumanDao() {
    }

    @Autowired
    public HumanDao(AdjustedDynamoDbClient adjustedDynamoDbClient) {
        this.adjustedDynamoDbClient = adjustedDynamoDbClient;
    }

    public void setAdjustedDynamoDbClient(AdjustedDynamoDbClient adjustedDynamoDbClient) {
        this.adjustedDynamoDbClient = adjustedDynamoDbClient;
    }

    @PostConstruct
    private String createTableIfAbsent() {
        String tableId = "";
        DynamoDbClient dynamoDbClient = adjustedDynamoDbClient.getDynamoDbClient();
        DescribeTableRequest request = DescribeTableRequest.builder()
                .tableName(HumanDao.TABLE_NAME)
                .build();

        TableDescription tableInfo = null;
        try {
            tableInfo = dynamoDbClient.describeTable(request).table();
        } catch (ResourceNotFoundException e) {
            log.warn(e.getMessage());
        }
        if (tableInfo == null) {
            tableId = createTableWithPartitionKey(HumanDao.TABLE_NAME, HumanDao.PARTITION_KEY);
        } else {
            tableId = tableInfo.tableId();
        }

        return tableId;
    }

    public Optional<Human> findById(Long partitionKeyVal) {
        DynamoDbTable<Human> custTable = adjustedDynamoDbClient.getDynamoDbEnhancedClient()
                .table(TABLE_NAME, TableSchema.fromBean(Human.class));
        Key key = Key.builder().partitionValue(partitionKeyVal).build();

        return Optional.of(custTable.getItem(r -> r.key(key)));
    }

    public void deleteById(Long partitionKeyVal) {

        HashMap<String, AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put(PARTITION_KEY, AttributeValue.builder().n(String.valueOf(partitionKeyVal)).build());

        DeleteItemRequest deleteReq = DeleteItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(keyToGet)
                .build();
        adjustedDynamoDbClient.getDynamoDbClient().deleteItem(deleteReq);
    }

    public Human save(Human changed) {
        if (changed.getId() == null) {
            changed.setId(random.nextLong());
        }
        DynamoDbTable<Human> custTable = adjustedDynamoDbClient.getDynamoDbEnhancedClient()
                .table(TABLE_NAME, TableSchema.fromBean(Human.class));

        return custTable.updateItem(changed);
    }

    public List<Human> findAll() {
        DynamoDbTable<Human> custTable = adjustedDynamoDbClient.getDynamoDbEnhancedClient()
                .table(TABLE_NAME, TableSchema.fromBean(Human.class));

        return custTable.scan().items().stream().collect(Collectors.toList());
    }

    public String createTableWithPartitionKey(String tableName, String partitionKey) {
        CreateTableRequest request = CreateTableRequest.builder()
                .attributeDefinitions(
                        AttributeDefinition.builder()
                                .attributeName(partitionKey)
                                .attributeType(ScalarAttributeType.N)
                                .build())
                .keySchema(
                        KeySchemaElement.builder()
                                .attributeName(partitionKey)
                                .keyType(KeyType.HASH)
                                .build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .tableName(tableName)
                .build();

        CreateTableResponse result = adjustedDynamoDbClient.getDynamoDbClient().createTable(request);

        return result.tableDescription().tableId();
    }

    private long createNewId() {
        return random.nextLong();
    }


}