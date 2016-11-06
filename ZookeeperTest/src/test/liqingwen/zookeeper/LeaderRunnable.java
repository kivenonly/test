package test.liqingwen.zookeeper;

import org.apache.zookeeper.*;

import java.util.*;

/**
 * Created by qingwenli on 2016/11/6.
 */
public class LeaderRunnable extends Leader implements Runnable {
    public void run() {
        try {
            String str = zooKeeper.create(RootNote + "/leader", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            final String currentNoteName = str.substring(str.lastIndexOf("/") + 1);

            // get the root path all children
            List<String> children = zooKeeper.getChildren(RootNote, null);

            // random sleep
            Thread.sleep(new Random().nextInt(10) * 10000);
            // check the note SN value.
            if (children == null || children.isEmpty()) {
                // I am the leader. the root note data is the leader node name.
                zooKeeper.setData(RootNote, currentNoteName.getBytes(), automicInteger.getAndAdd(1));
                return;
            }

            // check the minimum note
            int current = Integer.valueOf(currentNoteName.substring(6));

            List<Integer> noteSNArray = new ArrayList<Integer>();
            noteSNArray.add(current);
            for (String note : children) {
                noteSNArray.add(Integer.valueOf(note.substring(6)));
            }

            // sort
            Collections.sort(noteSNArray);
            // get the min SN
            zooKeeper.setData(RootNote, ("/leader" + noteSNArray.get(0)).getBytes(), automicInteger.getAndAdd(1));

            // check the min SN is current note.if not register watcher to the maximum but smaller than self note.
            int index = 0;
            if (current != noteSNArray.get(0)) {
                for (int i=0; i<noteSNArray.size(); i++) {
                    if (noteSNArray.get(i) == current) {
                        index = i-1;
                        break;
                    }
                }
            }

            if (index != 0) {
                zooKeeper.exists("/leader" + noteSNArray.get(index), new Watcher() {
                    public void process(WatchedEvent watchedEvent) {
                        if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                            // set my self to the leader
                            try {
                                zooKeeper.setData(RootNote, currentNoteName.getBytes(), automicInteger.getAndAdd(1));
                            } catch (KeeperException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
