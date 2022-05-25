package com.example.simplaws.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;


@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class Human {
    private Long id;
    private String name;
    private int age;
    private Gender gender;

    @DynamoDbPartitionKey
    public Long getId() {
        return id;
    }

    @DynamoDbAttribute("name")
    public String getName() {
        return name;
    }

    @DynamoDbAttribute("age")
    public int getAge() {
        return age;
    }

    @DynamoDbAttribute("gender")
    public Gender getGender() {
        return gender;
    }
}
