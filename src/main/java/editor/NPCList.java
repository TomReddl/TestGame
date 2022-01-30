package editor;

import editor.NPCInfo;
import lombok.Getter;
import lombok.Setter;
import utils.JsonUtils;

import java.util.List;

/*
 * Список NPC в редакторе
 * */
@Setter
@Getter
public class NPCList {
    private List<NPCInfo> npc;
    private int npcCount;

    public NPCList() {
        npc = JsonUtils.getNPC();
        npcCount = npc.size();
    }
}
