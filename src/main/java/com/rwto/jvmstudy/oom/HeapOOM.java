package com.rwto.jvmstudy.oom;

import java.util.ArrayList;

/**
 * @author renmw
 * @create 2023/12/19 16:53
 **/
public class HeapOOM {
    /**
     * VM Args: -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=hprof/HeapOOM.hprof
     * 默认目录为 java_<pid>.hprof
     * */
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        while(true){
            list.add("123");
        }
    }
}
