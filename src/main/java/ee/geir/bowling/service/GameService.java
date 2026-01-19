package ee.geir.bowling.service;

import ee.geir.bowling.entity.Game;
import ee.geir.bowling.entity.Player;
import ee.geir.bowling.entity.Status;
import ee.geir.bowling.repository.PlayerRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GameService {

    @Autowired
    private PlayerRepository playerRepository;
    private int playerIndex = 0;
    private final Random random = new Random();
    private Player player;
    @Getter
    @Autowired
    private Game game;

    public Player startGame() {
        playerIndex = 0;
        game.setGameStatus(Status.STARTED);
        game.setFrame(0);
        game.setPins(10);
        player = getNextBaller();
        game.setCurrentPlayer(player);
        return player;
    }

    public void resetGame() {
        game.setPlayers(new ArrayList<>());
        game.setGameStatus(Status.NOT_STARTED);
        playerRepository.deleteAllPlayers();
    }

    public Player getNextBaller() {
        if (playerIndex >= game.getPlayers().size()) {
            playerIndex = 0;
            if (game.getFrame() == 9) {
                game.setGameStatus(Status.FINISHED);
                return null;
            }
            game.setFrame(game.getFrame() + 1);
        }
        Player nextPlayer = playerRepository.getPlayers().get(playerIndex);
        game.setCurrentPlayer(nextPlayer);
        game.setPins(10);
        return nextPlayer;
    }

    public Game throwBall() {
        Player currentPlayer = game.getCurrentPlayer();
        int frame = game.getFrame();

        List<Integer> frameResults = getFrameResults(currentPlayer, frame);

        int pinsKnocked = random.nextInt(game.getPins() + 1);
        frameResults.add(pinsKnocked);
        game.setPins(game.getPins() - pinsKnocked);
        calculateScore(currentPlayer);

        if (frame < 9) {
            if (game.getPins() == 0 || frameResults.size() == 2) {
                advancePlayer();
            }
            return game;
        }

        int frameTotal = frameResults.stream().mapToInt(Integer::intValue).sum();

        if (frameResults.size() == 3) {
            // kui 3 viset tehtud, saada j2rgmine viskama
            advancePlayer();
        } else if (game.getPins() == 0 && frameResults.size() <= 2) {
            // juhul kui 2 viske jooksul k6ik maas, siis 1 uus vise
            game.setPins(10);
        } else if (game.getPins() != 0 && frameResults.size() == 2 && frameTotal < 10) {
            // 2 viset tehtud ja pole pinnid maas ning total pole ka 10, j2rgmine viskama
            advancePlayer();
        }

        return game;
    }

    private void advancePlayer() {
        playerIndex++;
        player = getNextBaller();
    }

    private List<Integer> getFrameResults(Player player, int frame) {
        List<List<Integer>> results = player.getFrames();
        while (results.size() <= frame && results.size() < 10) {
            results.add(new ArrayList<>());
        }
        return results.get(frame);
    }

    public void calculateScore(Player player) {
        // spare / = 10 + next 1 ball
        // strike X = 10 + next 2 balls
        List<List<Integer>> frames = player.getFrames();
        List<Integer> playerScores = player.getFrameScores();
        playerScores.clear();

        for (int i = 0; i < frames.size() && i < 10; i++) {
            List<Integer> frame = frames.get(i);
            int frameTotal = frame.stream().mapToInt(Integer::intValue).sum();
            int frameThrows = frame.size();

            if (i == 9) {
                if (frameThrows == 2 && frameTotal < 10 || frameThrows ==3) {
                    playerScores.add(frameTotal);
                }else {
                    playerScores.add(null);
                }
                continue;
            }

            if (frameThrows == 2 && frameTotal < 10) { // OPEN
                playerScores.add(frameTotal);
            } else if (frameThrows == 2 && frameTotal == 10) { // SPARE
                Integer bonus = getNextThrowPoints(frames, i);
                playerScores.add(bonus == null ? null : 10 + bonus);
            } else if (frameThrows == 1 && frameTotal == 10) { // STRIKE
                Integer bonus = getNextTwoThrowsPoints(frames, i);
                playerScores.add(bonus == null ? null : 10 + bonus);
            } else {
                playerScores.add(null);
            }
        }
    }

    private Integer getNextThrowPoints(List<List<Integer>> frames, int index) {
        if (index+1 >= frames.size())
            return null;
        if (frames.get(index+1).isEmpty())
            return null;
        return frames.get(index+1).get(0);
    }

    private Integer getNextTwoThrowsPoints(List<List<Integer>> frames, int index) {
        List<Integer> balls = new ArrayList<>();
        for (int i = index + 1; i < frames.size() && balls.size() < 2; i++) {
            balls.addAll(frames.get(i));
        }
        return balls.size() < 2 ? null : balls.get(0) + balls.get(1);
    }

}
