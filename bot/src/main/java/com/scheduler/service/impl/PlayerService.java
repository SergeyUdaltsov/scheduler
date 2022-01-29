package com.scheduler.service.impl;

import com.scheduler.dao.IPlayerDao;
import com.scheduler.model.Player;
import com.scheduler.service.IPlayerService;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
public class PlayerService implements IPlayerService {

    private IPlayerDao playerDao;

    public PlayerService(IPlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    @Override
    public void save(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player can not be null");
        }
        playerDao.save(player);
    }

    @Override
    public List<Player> getPlayersByYears(List<Integer> years) {
        return playerDao.getPlayersByYears(years);
    }

    @Override
    public List<Player> getAllPlayers() {
        return playerDao.getAllPlayers();
    }

    @Override
    public void remove(Player player) {
        playerDao.remove(player);
    }

    @Override
    public List<Player> getPlayersByYear(int year) {
        return playerDao.getPlayersByYear(year);
    }

    @Override
    public Player getPlayer(String name) {
        return playerDao.getPLayer(name);
    }
}
