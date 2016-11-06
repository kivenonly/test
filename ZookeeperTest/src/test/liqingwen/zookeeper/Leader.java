package test.liqingwen.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qingwenli on 2016/11/6.
 */
public class Leader {
    private static final String zkUrl = "10.221.73.89:2181";
    static String RootNote = "/elroot";
    static ZooKeeper zooKeeper;
    static AtomicInteger automicInteger = new AtomicInteger(0);

    public Leader() {
        try {
            System.out.println("Starting ZK:");
            zooKeeper = new ZooKeeper(zkUrl, 6000, null);
            System.out.println("Start ZK finished.");
        } catch (Exception e) {
            System.out.println("start zookeeper error:" + e);
            zooKeeper = null;
        }
    }

    public void createRooNote() {
        try {
            if (zooKeeper.exists(RootNote, false) == null) {
                zooKeeper.create(RootNote, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

}
