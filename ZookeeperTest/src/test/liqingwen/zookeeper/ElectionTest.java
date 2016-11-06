package test.liqingwen.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

/**
 * Created by qingwenli on 2016/11/6.
 */
public class ElectionTest {

    public static void main(String... args) {
        Leader leader = new Leader();
        leader.createRooNote();

        // leader
        new Thread(new LeaderRunnable()).start();
        // leader
        new Thread(new LeaderRunnable()).start();
        // leader
        new Thread(new LeaderRunnable()).start();

    }
}
