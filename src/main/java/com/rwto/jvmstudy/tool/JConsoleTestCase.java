package com.rwto.jvmstudy.tool;

import java.util.ArrayList;
import java.util.List;

public class JConsoleTestCase {

    /**
     * -Xms100m -Xmx100m -XX:+UseSerialGC
     * 内存占位符对象，一个OOMObject大约占64K
     * 远程连接配置：
     * -Dcom.sun.management.jmxremote  远程开启开关
     * -Dcom.sun.management.jmxremote.port=8999   jmx远程调用端口
     * -Dcom.sun.management.jmxremote.rmi.port=9999   指定rmi 端口
     *  JMX 和 RMI，是两种相关联的技术，JMX 使用 RMI 作为远程管理工具来管理和监控 Java 程序，RMI 为 JMX 提供了远程连接所需的远程调用和通信机制
     * -Dcom.sun.management.jmxremote.ssl=false  不为ssl连接
     * -Dcom.sun.management.jmxremote.authenticate=false 不开启验证
     -Dcom.sun.management.jmxremote
     -Dcom.sun.management.jmxremote.port=1111
     -Dcom.sun.management.jmxremote.rmi.port=2222
     -Dcom.sun.management.jmxremote.ssl=false
     -Dcom.sun.management.jmxremote.authenticate=false
     */
    static class OOMObject {
        public byte[] placeholder = new byte[64 * 1024];
    }

    public static void fillHeap(int num) throws InterruptedException {
        List<OOMObject> list = new ArrayList<OOMObject>();
        for (int i = 0; i < num; i++) {
            // 稍作延时，令监视曲线的变化更加明显
            Thread.sleep(50);
            list.add(new OOMObject());
        }
        System.gc();
    }

    public static void main(String[] args) throws Exception {
        fillHeap(1000);
        System.out.println("end");
        while(true){

        }
    }

}
