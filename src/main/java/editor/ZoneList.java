package editor;

import lombok.Getter;
import lombok.Setter;
import utils.JsonUtils;

import java.util.List;

/*
 * Список зон в редакторе
 * */
@Setter
@Getter
public class ZoneList {
    private List<ZoneInfo> zones;
    private int zonesCount;

    public ZoneList() {
        zones = JsonUtils.getZones();
        zonesCount = zones.size();
    }
}
