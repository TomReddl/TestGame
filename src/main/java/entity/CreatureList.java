package entity;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class CreatureList {
    private List<NPCInfo> creatures = new ArrayList<>();
    private int creaturesCount;

    public CreatureList() {
        creaturesCount = Objects.requireNonNull(new File("src/main/resources/graphics/creatures").list()).length;
        for (int i = 0; i < creaturesCount; i++) {
            creatures.add(new NPCInfo());
            creatures.get(i).setImageId(i);
        }
    }
}
