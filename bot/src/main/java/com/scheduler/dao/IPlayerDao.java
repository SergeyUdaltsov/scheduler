package com.scheduler.dao;

import com.scheduler.model.Player;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
public interface IPlayerDao {

    void save(Player player);

    void remove(Player player);

    List<Player> getPlayersByYear(int year);

    List<Player> getPlayersByYears(List<Integer> years);

    List<Player> getAllPlayers();

    Player getPLayer(String name);
}
