package ee.geir.bowling.controller;

import ee.geir.bowling.dto.AddPlayerRequest;
import ee.geir.bowling.entity.Player;
import ee.geir.bowling.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("players")
    public List<Player> getPlayers(){
        return playerService.getAllPlayers();
    }

    @PostMapping("players")
    public List<Player> addPlayer(@RequestBody AddPlayerRequest request) {
        return playerService.addPlayer(request.getName());
    }

}
