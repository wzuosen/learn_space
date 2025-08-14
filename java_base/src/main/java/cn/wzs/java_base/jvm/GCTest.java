package cn.wzs.java_base.jvm;

import java.util.ArrayList;
import java.util.List;

public class GCTest {


    public static void main(String[] args) {
        List<GCTest> objs = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            objs.add(new GCTest());
        }
    }

}
