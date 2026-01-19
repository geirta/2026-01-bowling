package ee.geir.bowling.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    private String name;
    private List<List<Integer>> frames = new ArrayList<>(10);
    private List<Integer> frameScores = new ArrayList<>(10);

}
