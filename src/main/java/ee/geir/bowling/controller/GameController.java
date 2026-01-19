package ee.geir.bowling.controller;

import ee.geir.bowling.entity.Game;
import ee.geir.bowling.entity.Player;
import ee.geir.bowling.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("games")
    public Game getGame() {
        return gameService.getGame();
    }

    @GetMapping("games/new")
    public Player startGame() {
        return gameService.startGame();
    }

    @GetMapping("games/throw")
    public Game throwBall() {
        return gameService.throwBall();
    }

    @DeleteMapping("games")
    public void resetGame() {
        gameService.resetGame();
    }

}
