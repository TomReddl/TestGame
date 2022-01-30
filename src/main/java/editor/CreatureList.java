package editor;

import editor.NPCInfo;
import lombok.Getter;
import lombok.Setter;
import utils.JsonUtils;

import java.util.List;

/*
 * Список существ в редакторе
 * */
@Setter
@Getter
public class CreatureList {
    private List<NPCInfo> creatures;
    private int creaturesCount;

    public CreatureList() {
        creatures = JsonUtils.getCreatures();
        creaturesCount = creatures.size();
    }
}
