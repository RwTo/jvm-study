package com.rwto.jvmstudy.oom;

/**
 * @author renmw
 * @create 2024/4/6 21:59
 **/
public class StackOverFlow {
    /**
     * stackoverflowError 栈空间溢出
     * */
    public static void main(String[] args) {
        stackOverflowError();
    }

    public static void stackOverflowError(){
        stackOverflowError();
    }

}
