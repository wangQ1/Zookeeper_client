package cn.et;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;

public class ZkExercise {
	private static Connection conn = null;
	public static Connection getConnection(ZkClient zk) throws ClassNotFoundException, SQLException{
		byte[] url = zk.readData("/db/url");
		byte[] driverClassName = zk.readData("/db/driverClassName");
		byte[] userid = zk.readData("/db/userid");
		byte[] password = zk.readData("/db/password");
		Class.forName(new String(driverClassName));
		conn = DriverManager.getConnection(new String(url), new String(userid), new String(password));
		return conn;
	}
	public static void main(String[] args) {
		String zkUrl = "localhost:2181";
		//新建zookeeper客户端                           url   断开连接超时    连接超时
		final ZkClient zk = new ZkClient(zkUrl, 10000, 5000, new BytesPushThroughSerializer());
		try {
			conn = getConnection(zk);
			System.out.println(conn);
			zk.subscribeDataChanges("/db", new IZkDataListener() {
				//db节点被删除时触发
				public void handleDataDeleted(String path) throws Exception {
					
				}
				//db节点的数据被修改时触发   只有使用java客户端修改才会触发
				public void handleDataChange(String path, Object arg0) throws Exception {
					conn = getConnection(zk);
					System.out.println(conn);
				}
			});
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		while(true){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
