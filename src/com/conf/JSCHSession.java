package com.conf;

import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class JSCHSession {

	private Session session;
	private Channel channel;
	private ChannelSftp sftpchannel;

	/**
	 * @param username
	 * @param password
	 * @param host
	 * @param port
	 * 
	 *            Creates JSCH session to get sftpChannel.
	 */
	public JSCHSession(String username, String password, String host, int port) {
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(username, host, port);
			session.setPassword(password);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			sftpchannel = (ChannelSftp) channel;

		} catch (Exception e) {
			e.printStackTrace();
			closeSession();
		}

	}

	/**
	 * @return sftpChannel
	 */
	public ChannelSftp getSftpchannel() {
		return sftpchannel;
	}

	/**
	 * Closes session and sftpChannel.
	 */
	public void closeSession() {
		sftpchannel.exit();
		channel.disconnect();
		session.disconnect();
	}

}
