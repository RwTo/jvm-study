# jstack: Java 堆栈跟踪工具

jstack（Stack Trace for Java）命令用于生成虚拟机当前时刻的线程快照（一般称为threaddump或者javacore文件）。

## jstack 命令格式
`jstack [option] vmid`

## jstack option 选项
![](../img-md/jstack-option.png)

## 举例
`jstack -l 3500`

![](../img-md/jstack-l.png)