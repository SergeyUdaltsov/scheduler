package com.scheduler.utils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.scheduler.model.CommandType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 4/23/2021
 */
public class CommandTypeConverter implements DynamoDBTypeConverter<List<String>, List<CommandType>> {

    @Override
    public List<String> convert(List<CommandType> types) {
        return types.stream().map(CommandType::name).collect(Collectors.toList());
    }

    @Override
    public List<CommandType> unconvert(List<String> types) {
        return types.stream().map(CommandType::fromValue).collect(Collectors.toList());
    }
}
