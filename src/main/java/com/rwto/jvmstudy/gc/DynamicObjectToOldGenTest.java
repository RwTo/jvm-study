package com.rwto.jvmstudy.gc;

import static com.rwto.jvmstudy.content.JVMContent._1MB;

/**
 * 动态对象年龄判定
 * JVM规定
 * 如果Survivor空间中相同年龄所有对象大小总超过Survivor空间的一半
 * 如果有对象的年龄大于或等于此年龄，则直接进入老年代
 * @author renmw
 * @create 2023/12/20 20:42
 **/
public class DynamicObjectToOldGenTest {
    /**
     * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+UseSerialGC -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=15 -XX:+PrintTenuringDistribution
     * 参数解释：{@link LongTimeObjectToOldGenTest}
     */
    //private static byte[] allocation =  new byte[_1MB * 1];静态对象也存在堆内存中
    public static void main(String[] args) {
        /**IDEA启动初始Eden区占用3M*/
        byte[] allocation1, allocation2, allocation3, allocation4;

        /**Eden区申请一个256K的内存空间，申请成功，Eden区占用3M+256K*/
        allocation1 = new byte[_1MB / 4];
        System.out.println("allocation1 256K");

        /**Eden区申请一个256K的内存空间，申请成功，Eden区占用3M+512K*/
        allocation2 = new byte[_1MB / 4];
        System.out.println("allocation2 256K");

        /**Eden区申请一个4M的内存空间，申请成功，Eden区占用7M+512K*/
        allocation3 = new byte[4 * _1MB];
        System.out.println("allocation3 4M");

        /**Eden区申请一个4M的内存空间，申请失败，
         * 第一次GC
         * allocation1小于1M，放入Survivor from区，年龄=1
         * allocation2小于1M，放入Survivor from区，年龄=1
         * allocation3大于1M，直接放入老年代,老年代 占用4M
         * Eden区清空，Survivor from区 占用1M，新生代占用内存1M
         * Eden区申请4M内存存放allocation3，新生代占用5M
         * */
        allocation4 = new byte[4 * _1MB];
        System.out.println("allocation4 4M");
        /*这里必须取消刚刚引用的4M对象，因为 接下来申请对象之前，这个对象经可达性分析是有效的，不会被丢弃*/
        //allocation4 = null;

        /**
         * Eden区申请一个4M的内存空间，申请失败
         * 第二次GC
         * Survivor 区都是年龄为1的 对象，超过Survivor区内存的一半，年龄>=1的对象（即全部），都进入老年代
         * allocation4 经过可达性分析，直接回收内存
         * 新生代内存清空，
         * Eden区再申请4M内存存放allocation4
         */
        allocation4 = new byte[4 * _1MB];
        System.out.println("allocation4 4M");
    }

    /**
     allocation1 256K
     allocation2 256K
     allocation3 4M
     [GC (Allocation Failure) [DefNew
     Desired survivor size 524288 bytes, new threshold 1 (max 15)
     - age   1:    1048576 bytes,    1048576 total
     : 7835K->1024K(9216K), 0.0032106 secs] 7835K->5543K(19456K), 0.0032513 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
     allocation4 4M
     [GC (Allocation Failure) [DefNew
     Desired survivor size 524288 bytes, new threshold 15 (max 15)
     - age   1:         72 bytes,         72 total
     : 5278K->0K(9216K), 0.0009190 secs] 9798K->5543K(19456K), 0.0009354 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
     allocation4 4M
     Heap
     def new generation   total 9216K, used 4178K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
     eden space 8192K,  51% used [0x00000000fec00000, 0x00000000ff014990, 0x00000000ff400000)
     from space 1024K,   0% used [0x00000000ff400000, 0x00000000ff400048, 0x00000000ff500000)
     to   space 1024K,   0% used [0x00000000ff500000, 0x00000000ff500000, 0x00000000ff600000)
     tenured generation   total 10240K, used 5543K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
     the space 10240K,  54% used [0x00000000ff600000, 0x00000000ffb69d40, 0x00000000ffb69e00, 0x0000000100000000)
     Metaspace       used 3131K, capacity 4556K, committed 4864K, reserved 1056768K
     class space    used 333K, capacity 392K, committed 512K, reserved 1048576K
     * */
}
