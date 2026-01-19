package ee.geir.bowling.service;

import ee.geir.bowling.entity.Game;
import ee.geir.bowling.entity.Player;
import ee.geir.bowling.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private Game game;

    public List<Player> getAllPlayers() {
        return playerRepository.getPlayers();
    }

    public List<Player> addPlayer(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Player name cannot be empty");
        }
        Player newPlayer = new Player();
        newPlayer.setName(name);
        List<Player> playerList = playerRepository.addPlayer(newPlayer);
        game.setPlayers(playerList);
        return playerList;
    }

}
