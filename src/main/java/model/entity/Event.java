package model.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Событие
 */
@Getter
@Setter
public class Event {
    private String id;
    private int X;
    private int Y;
    private int time;
    private Map<String, String> params;
}
