import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;

public class TestConection {

	public TestConection() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		ChannelSftp channelsftp = null;
		Channel channel= null;
		Session session= null;
		try {
		
			JSch jsch = new JSch();
			//session = jsch.getSession("jogal002", "csel-kh4250-13.cselabs.umn.edu", 22);
			session = jsch.getSession("saurabhjogalekar1217", "e.cloudxlab.com", 22);
			session.setPassword("VZASFWXL");
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			
			System.out.println("Connected");
			
			
			channel = session.openChannel("sftp");
			channel.connect();
			System.out.println("Channel connected");
			
			
			channelsftp = (ChannelSftp) channel;
			System.out.println();
			channelsftp.cd("/home/saurabhjogalekar1217/spark");
			
			
			OutputStream op = channelsftp.put("one.txt");
			System.out.println("put");
			
			InputStream ip = new FileInputStream(new File("F:\\Study\\MS\\SC_SRC\\one.txt"));
			
			byte[] buffer = new byte[1000];
			
			ip.read(buffer);
			op.write(buffer);
			op.close();
			System.out.println("Written");
			/*Vector<LsEntry> v = channelsftp.ls(channelsftp.pwd());
			csel-kh4250-13.cselabs.umn.edu
			for(LsEntry attr : v) {
				System.out.println("-------------"+attr.getAttrs().toString());
				
			}*/
			
			/*//System.out.println(channelsftp.ls(channelsftp.pwd()).toString());;
			SftpATTRS attr = channelsftp.lstat("test.sh");
			System.out.println(attr.toString());
			*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			channelsftp.exit();
			channel.disconnect();
			session.disconnect();
		}
		
		
		
		
	}

}
