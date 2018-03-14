package com.conf;

/**
 * @author saurabh jogalekar 
 * 				A directory pair for source and target folders.
 */
public class DirectoryPair {

	private String sourceDir, targetDir;
	private Scheme sourceScheme, targetScheme;

	private String sourceHost, sourceUser, sourcePassword, sourceToken, targetToken;

	public String getSourceToken() {
		return sourceToken;
	}

	public void setSourceToken(String sourceToken) {
		this.sourceToken = sourceToken;
	}

	public String getTargetToken() {
		return targetToken;
	}

	public void setTargetToken(String targetToken) {
		this.targetToken = targetToken;
	}

	private String trgHost, trgUser, trgPassword;

	public String getSourcePassword() {
		return sourcePassword;
	}

	public void setSourcePassword(String sourcePassword) {
		this.sourcePassword = sourcePassword;
	}

	public String getTrgPassword() {
		return trgPassword;
	}

	public void setTrgPassword(String trgPassword) {
		this.trgPassword = trgPassword;
	}

	private int srcPort, trgPort;

	public DirectoryPair(String sourceDir, String targetDir) {
		super();
		this.sourceDir = sourceDir;
		this.targetDir = targetDir;
	}

	public String getSourceDir() {
		return sourceDir;
	}

	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}

	public String getTargetDir() {
		return targetDir;
	}

	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}

	public DirectoryPair() {
		// TODO Auto-generated constructor stub
	}

	public Scheme getSourceScheme() {
		return sourceScheme;
	}

	public void setSourceScheme(Scheme sourceScheme) {
		this.sourceScheme = sourceScheme;
	}

	public Scheme getTargetScheme() {
		return targetScheme;
	}

	public void setTargetScheme(Scheme targetScheme) {
		this.targetScheme = targetScheme;
	}

	public String getSourceHost() {
		return sourceHost;
	}

	public void setSourceHost(String sourceHost) {
		this.sourceHost = sourceHost;
	}

	public int getSourcePort() {
		return srcPort;
	}

	public void setSourcePort(int sourcePort) {
		this.srcPort = sourcePort;
	}

	public String getSourceUser() {
		return sourceUser;
	}

	public void setSourceUser(String sourceUser) {
		this.sourceUser = sourceUser;
	}

	public String getTrgHost() {
		return trgHost;
	}

	public void setTrgHost(String trgHost) {
		this.trgHost = trgHost;
	}

	public int getTrrgPort() {
		return trgPort;
	}

	public void setTrrgPort(int trrgPort) {
		this.trgPort = trrgPort;
	}

	public String getTrgUser() {
		return trgUser;
	}

	public void setTrgUser(String trgUser) {
		this.trgUser = trgUser;
	}

}
