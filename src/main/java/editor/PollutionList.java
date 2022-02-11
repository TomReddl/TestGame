package editor;

import lombok.Getter;
import lombok.Setter;
import utils.JsonUtils;

import java.util.List;

/*
 * Список загрязнений в редакторе
 * */
@Setter
@Getter
public class PollutionList {
    private List<PollutionInfo> pollutions;
    private int pollutionsCount;

    public PollutionList() {
        pollutions = JsonUtils.getPollutions();
        pollutionsCount = pollutions.size();
    }
}
