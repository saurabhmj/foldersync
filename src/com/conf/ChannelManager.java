package com.conf;

import java.util.HashMap;
import java.util.Map;

import com.jcraft.jsch.ChannelSftp;

/**
 * @author saurabh jogalekar 
 * 				Class for managing SFTP sessions and channels.
 */
public class ChannelManager {

	private Map<String, JSCHSession> channels;
	private static ChannelManager chMan = null;

	private ChannelManager() {
		channels = new HashMap<>();
	}

	public static ChannelManager getInstance() {
		if (chMan == null) {
			chMan = new ChannelManager();
			return chMan;
		}
		return chMan;
	}

	public String getChannelToken(String username, String password, String host, int port) {
		return username + "::" + password + "::" + host + "::" + port;
	}

	public ChannelSftp getChannel(String token) {
		if (!channels.containsKey(token)) {
			String[] spToken = token.split("::");
			channels.put(token, new JSCHSession(spToken[0], spToken[1], spToken[2], Integer.parseInt(spToken[3])));
		}

		return channels.get(token).getSftpchannel();
	}

	public void closeChannels() {
		for (String s : channels.keySet())
			channels.get(s).closeSession();

	}

}
