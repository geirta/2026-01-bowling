package ee.geir.bowling.repository;

import ee.geir.bowling.entity.Player;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class PlayerRepository {

    private List<Player> players = new ArrayList<>();

    public List<Player> addPlayer(Player player) {
        players.add(player);
        return players;
    }

    public void deleteAllPlayers() {
        players.clear();
    }


}
