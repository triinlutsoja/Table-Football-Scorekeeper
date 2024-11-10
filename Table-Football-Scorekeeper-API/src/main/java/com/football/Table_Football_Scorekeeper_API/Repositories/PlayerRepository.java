package com.football.Table_Football_Scorekeeper_API.Repositories;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    /*
    Inherited methods:

    save(Player player)
	findById(Long id)
	findAll()
	deleteById(Long id)
	delete(Player player)
    */

    // Custom query methods

    List<Player> getPlayersByName(String name);



}
