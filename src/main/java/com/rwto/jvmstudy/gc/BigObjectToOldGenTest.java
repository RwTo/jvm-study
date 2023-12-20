package com.rwto.jvmstudy.gc;

import static com.rwto.jvmstudy.content.JVMContent._1MB;

/**
 * 大对象直接进入老年代
 * @author renmw
 * @create 2023/12/20 19:51
 **/
public class BigObjectToOldGenTest {

    /**
     * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+UseSerialGC -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=3145728
     * 解释：-XX:PretenureSizeThreshold=3145728 : 如果内存超过3M，直接分配到老年代，防止大对象重复进行GC，默认值为0，也就是所有创建都要经过新生代
     *         其他参数: {@link MinorGCTest}
     * 这里注意一点：
     *      -XX:PretenureSizeThreshold 只对 Serial垃圾收集器有效
     *      高版本的jdk 可能不是使用的Serial垃圾收集器，
     *      所以建议主动指定 使用 Serial垃圾收集器，即 -XX:+UseSerialGC
     *
     */
    public static void main(String[] args) {
        byte[] allocation;
        /**这里只申请了4M的内存，但超过了3M的阈值，会将此对象直接放在老年代里，所以老年代会有4M的空间占用，Eden区没有占用内存*/
        allocation = new byte[4 * _1MB];  //直接分配在老年代中
    }

    /**
     Heap
     def new generation   total 9216K, used 3391K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
     eden space 8192K,  41% used [0x00000000fec00000, 0x00000000fef4fd88, 0x00000000ff400000)
     from space 1024K,   0% used [0x00000000ff400000, 0x00000000ff400000, 0x00000000ff500000)
     to   space 1024K,   0% used [0x00000000ff500000, 0x00000000ff500000, 0x00000000ff600000)
     tenured generation   total 10240K, used 4096K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
     the space 10240K,  40% used [0x00000000ff600000, 0x00000000ffa00010, 0x00000000ffa00200, 0x0000000100000000)
     Metaspace       used 3128K, capacity 4556K, committed 4864K, reserved 1056768K
     class space    used 333K, capacity 392K, committed 512K, reserved 1048576K
     */
}
