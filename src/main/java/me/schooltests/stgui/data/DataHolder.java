package me.schooltests.stgui.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHolder {
    private Map<String, Object> data = new HashMap<>();
    public <T> T get(String key, Class<T> clazz) {
        if (data.containsKey(key.toLowerCase())) {
            Object obj = data.get(key.toLowerCase());
            if (clazz.isAssignableFrom(obj.getClass())) {
                try {
                    return clazz.cast(obj);
                } catch (ClassCastException e) {
                    return null;
                }
            }
        }

        return null;
    }

    public <T> List<T> getList(String key, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        if (data.containsKey(key.toLowerCase())) {
            Object obj = data.get(key.toLowerCase());
            if (obj instanceof List) {
                try {
                    List<Object> objList = (List<Object>) obj;
                    for (Object item : objList) {
                        if (clazz.isAssignableFrom(item.getClass())) {
                            list.add(clazz.cast(item));
                        }
                    }
                } catch (ClassCastException e) {
                    return list;
                }
            }
        }

        return list;
    }

    public void set(String key, Object obj) {
        data.put(key.toLowerCase(), obj);
    }
}
