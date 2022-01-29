package com.scheduler.service;

import com.scheduler.model.Player;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
public interface IPlayerService {

    void save(Player player);

    void remove(Player player);

    List<Player> getPlayersByYear(int year);

    List<Player> getPlayersByYears(List<Integer> years);

    List<Player> getAllPlayers();

    Player getPlayer(String name);
}
