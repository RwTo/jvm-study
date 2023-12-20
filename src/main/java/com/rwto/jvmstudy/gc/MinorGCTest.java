package com.rwto.jvmstudy.gc;

import com.rwto.jvmstudy.content.JVMContent;

/**
 * 使用 java -XX:+PrintCommandLineFlags -version 命令查看当前虚拟机信息
 * 例：jdk8
 * -XX:InitialHeapSize=263987904 -XX:MaxHeapSize=4223806464 -XX:+PrintCommandLineFlags -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseParallelGC
 * java version "1.8.0_101"
 * Java(TM) SE Runtime Environment (build 1.8.0_101-b13)
 * Java HotSpot(TM) 64-Bit Server VM (build 25.101-b13, mixed mode)
 * @author renmw
 * @create 2023/12/20 19:29
 **/
public class MinorGCTest {
    /**
     * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+UseSerialGC -XX:+PrintGCDetails -XX:SurvivorRatio=8
     * 解释：-Xms20M： 初始堆 20M
     *      -Xmx20M： 最大堆 20M
     *      -Xmn10M： 新生代 10M
     *      -XX:+UseSerialGC： 使用 Serial 和 SerialOld 垃圾收集器
     *      -XX:SurvivorRatio=8： Eden:Survivor = 8:1 默认也是 8
     *      -XX:+PrintGCDetails： 打印GC详细信息，程序结束后打印堆空间内存信息
     *      -verbose:gc：在控制台输出GC情况
     *      -Xloggc: filepath  将GC日志输出到指定文件中
     *
     */

    public static void main(String[] args) {
        /**根据上述配置，堆内存最大和初始都是20M，年轻代10M，其中Eden区为8M 两个Survivor区为1M 老年代10M*/
        //注意：如果使用IDE编译器，堆内存会有其他额外的内容，因为编译器在运行java时会带有其他jvm参数，会占用一部分内存
        /**我这里使用IDEA进行测试，执行初始状态堆里就占有3M内存*/
        byte[] allocation1, allocation2, allocation3;
        /**Eden区申请一块2M内存，申请成功，Eden占用 5M*/
        allocation1 = new byte[2 * JVMContent._1MB];
        System.out.println("allocation1 2MB");
        /**Eden区申请一块2M内存，申请成功，Eden占用 7M*/
        allocation2 = new byte[2 * JVMContent._1MB];
        System.out.println("allocation2 2MB");
        /**Eden区申请一块2M内存，申请失败！
         * 发生MinorGC Eden区可以保留的数据保存到 From Survivor区
         * 此时 Survivor区只有1M，放不下，由老年代进行空间分配担保，将存活的对象存到老年代，老年代占用2*2=4M内存
         * Eden 重新申请 2M内存
         * */
        allocation3 = new byte[2 * JVMContent._1MB]; // 出现一次Minor GC
        System.out.println("allocation3 2MB");
    }

    /**
    打印信息分析
    allocation1 2MB
    allocation2 2MB
    [GC (Allocation Failure) [DefNew: 7323K->935K(9216K), 0.0030587 secs] 7323K->5031K(19456K), 0.0031079 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
    allocation3 2MB
    Heap
     def new generation   total 9216K, used 3387K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
      eden space 8192K,  29% used [0x00000000fec00000, 0x00000000fee65190, 0x00000000ff400000)
      from space 1024K,  91% used [0x00000000ff500000, 0x00000000ff5e9c78, 0x00000000ff600000)
      to   space 1024K,   0% used [0x00000000ff400000, 0x00000000ff400000, 0x00000000ff500000)
     tenured generation   total 10240K, used 4096K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
       the space 10240K,  40% used [0x00000000ff600000, 0x00000000ffa00020, 0x00000000ffa00200, 0x0000000100000000)
     Metaspace       used 3131K, capacity 4556K, committed 4864K, reserved 1056768K
      class space    used 333K, capacity 392K, committed 512K, reserved 1048576K
    * */
}

/**
 * 空间分配担保
 * 在MinorGC之前，JVM会检查老年代最大可用的连续空间 是否大于新生代所有对象的总空间，或者历次晋升的平均大小
 * 如果成立，则进行Minor GC，如果不成立，则进行Full GC
 *
 * */
