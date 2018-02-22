package cn.et;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

public class ZkTest {

	public static void main(String[] args) {
		String zkUrl = "localhost:2181";
		//新建zookeeper客户端                           url   断开连接超时    连接超时
		ZkClient zk = new ZkClient(zkUrl, 10000, 5000);
		if(!zk.exists("/user")){
			//创建一个永久节点
			zk.createPersistent("/user");
			//创建两个顺序节点   返回值为顺序节点的真实节点名                                                              创建模式.永久顺序节点
			String znodeName1 = zk.create("/user/ls", "girl", CreateMode.PERSISTENT_SEQUENTIAL);
			String znodeName2 = zk.create("/user/ls", "girl", CreateMode.PERSISTENT_SEQUENTIAL);
			//创建一个临时节点
			zk.createEphemeral("/user/zs", "boy");
			System.out.println(znodeName1 + "-----" + znodeName2);
		}
		//监控节点变化
		zk.subscribeDataChanges("/db", new IZkDataListener() {
			//db节点被删除时触发
			public void handleDataDeleted(String path) throws Exception {
				
			}
			//db节点的数据被修改时触发   只有使用java客户端修改才会触发
			public void handleDataChange(String path, Object arg0) throws Exception {
				System.out.println(path);
			}
		});
		zk.writeData("/db", "mysql");
		while(true){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
