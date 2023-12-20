package com.rwto.jvmstudy.gc;

import static com.rwto.jvmstudy.content.JVMContent._1MB;

/**
 * 长期存活的对象进入老年代
 * jvm 内部给每个对象做了一个标记，每经过一次GC，对象如果还存活，标记就+1
 * @author renmw
 * @create 2023/12/20 20:02
 **/
public class LongTimeObjectToOldGenTest {
    /**
     * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+UseSerialGC -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1 -XX:+PrintTenuringDistribution
     * 解释：-XX:MaxTenuringThreshold=1 : 如果存活标记达到1，下次GC，将此对象放入老年代，默认为15
     *      -XX:+PrintTenuringDistribution： JVM 在每次新生代GC时，打印出幸存区中对象的年龄分布
     *      其他参数: {@link MinorGCTest}
     *
     */
    public static void main(String[] args) {
        /**IDEA启动初始Eden区占用3M*/
        byte[] allocation1, allocation2, allocation3;
        /**Eden区申请一个256K的内存空间，申请成功，Eden区占用3M+256K*/
        allocation1 = new byte[_1MB / 4];
        System.out.println("allocation1 256K");
        /**Eden区申请一个4M的内存空间，申请成功，Eden区占用7M+256K*/
        allocation2 = new byte[4 * _1MB];
        System.out.println("allocation2 4M");
        /**Eden区申请一个4M的内存空间，申请失败，
         * 第一次GC
         * allocation1小于1M，放入Survivor from区，年龄+1
         * allocation2大于1M，直接放入老年代,老年代 占用4M
         * Eden区清空，Survivor from区 占用1M，新生代占用内存1M
         * Eden区申请4M内存存放allocation3，新生代占用5M
         * */
        allocation3 = new byte[4 * _1MB];
        System.out.println("allocation3 4M");
        allocation3 = null;// 刚刚allocation3指向的对象成游离态，下次GC会释放掉
        /**
         * Eden区申请一个4M的内存空间，申请失败
         * 第二次GC
         * 新生代 有age为1的对象——allocation1，进入老年代
         * allocation3 经过可达性分析，直接回收内存
         * 新生代内存清空，
         * Eden区再申请4M内存存放allocation3
         */
        allocation3 = new byte[4 * _1MB];
        System.out.println("allocation3 4M");
    }

    /**
     allocation1 256K
     allocation2 4M
     [GC (Allocation Failure) [DefNew
     Desired survivor size 524288 bytes, new threshold 1 (max 1)
     - age   1:    1048576 bytes,    1048576 total
     : 7579K->1024K(9216K), 0.0024393 secs] 7579K->5287K(19456K), 0.0024755 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
     allocation3 4M
     [GC (Allocation Failure) [DefNew
     Desired survivor size 524288 bytes, new threshold 1 (max 1)
     - age   1:         72 bytes,         72 total
     : 5442K->0K(9216K), 0.0007722 secs] 9705K->5286K(19456K), 0.0007847 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
     allocation1 4M
     Heap
     def new generation   total 9216K, used 4336K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
     eden space 8192K,  52% used [0x00000000fec00000, 0x00000000ff03bfc0, 0x00000000ff400000)
     from space 1024K,   0% used [0x00000000ff400000, 0x00000000ff400048, 0x00000000ff500000)
     to   space 1024K,   0% used [0x00000000ff500000, 0x00000000ff500000, 0x00000000ff600000)
     tenured generation   total 10240K, used 5286K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
     the space 10240K,  51% used [0x00000000ff600000, 0x00000000ffb29880, 0x00000000ffb29a00, 0x0000000100000000)
     Metaspace       used 3131K, capacity 4556K, committed 4864K, reserved 1056768K
     class space    used 333K, capacity 392K, committed 512K, reserved 1048576K
     Disconnected from the target VM, address: '127.0.0.1:64260', transport: 'socket'

     Process finished with exit code 0

     */
}
