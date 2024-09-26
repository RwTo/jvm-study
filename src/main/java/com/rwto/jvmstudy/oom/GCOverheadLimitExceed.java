package com.rwto.jvmstudy.oom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author renmw
 * @create 2024/4/6 22:07
 **/
public class GCOverheadLimitExceed {
    public static void main(String[] args) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            Map<String, Object> map = new HashMap<>();
            for (int j = 0; j < i; j++) {
                map.put(String.valueOf(j), j);
            }
            mapList.add(map);
        }
    }
}
