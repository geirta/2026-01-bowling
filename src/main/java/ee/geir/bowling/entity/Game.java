package ee.geir.bowling.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Game {

    private Long id;
    private Status gameStatus;
    private Player currentPlayer;
    private int pins;
    private int frame;
    private List<Player> players;
}
