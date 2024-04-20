package controller.utils;

import model.entity.map.MapCellInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для работы с дополнительными параметрами сущностей
 */
public class ParamsUtils {

    public static boolean getBoolean(Map<String, String> params, String paramName) {
        if (params != null && paramName != null) {
            var paramValue = params.get(paramName.toLowerCase());
            if (paramValue == null) {
                paramValue = params.get(paramName);
            }
            return Boolean.parseBoolean(paramValue);
        }
        return false;
    }

    public static boolean getBoolean(MapCellInfo mapCellInfo, String paramName) {
        return getBoolean(mapCellInfo.getParams(), paramName.toLowerCase());
    }

    public static Integer getInteger(Map<String, String> params, String paramName) {
        if (params != null && paramName != null) {
            var paramValue = params.get(paramName.toLowerCase());
            if (paramValue == null) {
                paramValue = params.get(paramName);
            }
            return Integer.parseInt(paramValue);
        }
        return null;
    }

    public static Integer getInteger(MapCellInfo mapCellInfo, String paramName) {
        return getInteger(mapCellInfo.getParams(), paramName.toLowerCase());
    }

    public static String getString(Map<String, String> params, String paramName) {
        if (params != null && paramName != null) {
            var paramValue = params.get(paramName.toLowerCase());
            if (paramValue == null) {
                paramValue = params.get(paramName);
            }
            return paramValue;
        }
        return null;
    }

    public static String getString(MapCellInfo mapCellInfo, String paramName) {
        return getString(mapCellInfo.getParams(), paramName.toLowerCase());
    }

    public static void setParam(MapCellInfo mapCellInfo, String paramName, String value) {
        if (mapCellInfo.getParams() == null) {
            mapCellInfo.setParams(new HashMap<>());
        }
        mapCellInfo.getParams().put(paramName.toLowerCase(), value);
    }
}
