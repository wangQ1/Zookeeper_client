package cn.et;

import org.I0Itec.zkclient.ZkClient;

public class ZkAAA {
	public static void main(String[] args) {
		String zkUrl = "localhost:2181";
		//新建zookeeper客户端                           url   断开连接超时    连接超时
		ZkClient zk = new ZkClient(zkUrl, 10000, 5000);
		zk.writeData("/db", "mysql");
	}
}
