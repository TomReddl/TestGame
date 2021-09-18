package entity;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class NPCList {
    private List<NPCInfo> npc = new ArrayList<>();
    private int npcCount;

    public NPCList() {
        npcCount = Objects.requireNonNull(new File("src/main/resources/graphics/characters").list()).length;
        for (int i = 0; i < npcCount; i++) {
            npc.add(new NPCInfo());
            npc.get(i).setImageId(i);
        }
    }
}
