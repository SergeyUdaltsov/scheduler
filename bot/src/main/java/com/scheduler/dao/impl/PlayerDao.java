package com.scheduler.dao.impl;

import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.scheduler.dao.IDynamoDbFactory;
import com.scheduler.dao.IPlayerDao;
import com.scheduler.model.Player;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
public class PlayerDao extends BaseDao<Player> implements IPlayerDao {

    public PlayerDao(IDynamoDbFactory dynamoDbFactory) {
        super(dynamoDbFactory, Player.class);
    }

    @Override
    public List<Player> getPlayersByYears(List<Integer> years) {
        ScanSpec scanSpec = new ScanSpec()
                .withFilterExpression("contains(:years, y)")
                .withValueMap(new ValueMap().withList(":years", years));
        return super.findAll(scanSpec);
    }

    @Override
    public List<Player> getPlayersByYear(int year) {
        QuerySpec querySpec = new QuerySpec()
                .withHashKey(Player.YEAR_FIELD, year);
        return super.findAllByIndexQueryObject(Player.YEAR_INDEX, querySpec);
    }

    @Override
    public List<Player> getAllPlayers() {
        return super.getAllEntities();
    }

    @Override
    public Player getPLayer(String name) {
        Player player = new Player(name);
        return super.getEntityByQueryObject(player);
    }
}
