package com.example.tair;

import com.taobao.tair.impl.DefaultTairManager;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        DefaultTairManager  defaultTairManager = new DefaultTairManager();
        List<String> cs = new ArrayList<String>();
        cs.add("192.168.1.100:5198");
        defaultTairManager.setConfigServerList(cs);
        defaultTairManager.setGroupName("group_1");
        defaultTairManager.init();

        defaultTairManager.put(1, "key", "value");

    }
}
