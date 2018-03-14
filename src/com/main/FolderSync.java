package com.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.conf.ChannelManager;
import com.conf.DirectoryPair;
import com.conf.FSFile;
import com.conf.FileUtil;
import com.conf.LocalFSFile;
import com.conf.RemoteFSFile;
import com.conf.Scheme;
import com.conf.SyncUtil;

public class FolderSync {

	private static Scanner scanner = new Scanner( System.in );

	public static void main(String[] args) {

		try {

			DirectoryPair pair = new DirectoryPair();
			FSFile source = null, target=null;

			System.out.println("Please select the scheme for first folder (local/remote)");	
			String scheme = scanner.nextLine();
			System.out.println("Please enter the complete path for the folder");
			pair.setSourceDir(scanner.nextLine());

			if(scheme.toLowerCase().equals(Scheme.LOCAL.toString().toLowerCase())) {
				pair.setSourceScheme(Scheme.LOCAL);
				source = new LocalFSFile(pair.getSourceDir(), "");
			}else {
				pair.setSourceScheme(Scheme.REMOTE);
				System.out.println("Please enter the host name");
				pair.setSourceHost(scanner.nextLine());
				System.out.println("Please enter the port");
				pair.setSourcePort(scanner.nextInt());
				scanner.nextLine();
				System.out.println("Please enter the username");
				pair.setSourceUser(scanner.nextLine());
				System.out.println("Please enter the password");
				pair.setSourcePassword(scanner.nextLine());
				pair.setSourceToken(ChannelManager.getInstance().getChannelToken(pair.getSourceUser(), pair.getSourcePassword(), pair.getSourceHost(), pair.getSourcePort()));;
				source = new RemoteFSFile(pair.getSourceToken(), pair.getSourceDir(), "", "");
			}

			System.out.println("Please select the scheme for second folder (local/remote)");	
			scheme = scanner.nextLine();

			System.out.println("Please enter the complete path for the folder");
			pair.setTargetDir(scanner.nextLine());

			if(scheme.toLowerCase().equals(Scheme.LOCAL.toString().toLowerCase())) {
				pair.setTargetScheme(Scheme.LOCAL);
				target = new LocalFSFile(pair.getTargetDir(), "");

			}else {
				pair.setTargetScheme(Scheme.REMOTE);
				System.out.println("Please enter the host name");
				pair.setTrgHost(scanner.nextLine());
				System.out.println("Please enter the port");
				pair.setTrrgPort(scanner.nextInt());
				scanner.nextLine();
				System.out.println("Please enter the username");
				pair.setTrgUser(scanner.nextLine());
				System.out.println("Please enter the password");
				pair.setTrgPassword(scanner.nextLine());
				pair.setTargetToken(ChannelManager.getInstance().getChannelToken(pair.getTrgUser(), pair.getTrgPassword(), pair.getTrgHost(), pair.getTrrgPort()));;
				target = new RemoteFSFile(pair.getTargetToken(), pair.getTargetDir(), "", "");

			}
			//testing text here
			FileUtil.compareDirectories(source, target);
			heading();
			displayOnlyTarget(FileUtil.getToSrc());
			displayOnlySource(FileUtil.getToTrg());
			displayCollision(FileUtil.getCollision());

			if(FileUtil.getToSrc().size()>0) {
				System.out.println("Sync missing data on first source? (y/n)");
				if(scanner.nextLine().equals("y")) {
					System.out.print("Synchronizing.");
					SyncUtil sync = new SyncUtil();
					sync.setTrg(pair.getSourceDir());
					sync.setFiles(FileUtil.getToSrc());
					sync.setTargetScheme(pair.getSourceScheme());
					sync.setToken(pair.getSourceToken());

					sync.copyToTarget();

					System.out.println("Done!\n");
				}
			}

			if(FileUtil.getToTrg().size()>0) {
				System.out.println("Sync missing data on second source? (y/n)");
				if(scanner.nextLine().equals("y")) {
					System.out.print("Synchronizing.");
					SyncUtil sync = new SyncUtil();
					sync.setTrg(pair.getTargetDir());
					sync.setFiles(FileUtil.getToTrg());
					sync.setTargetScheme(pair.getTargetScheme());
					sync.setToken(pair.getTargetToken());
					sync.copyToTarget();
					System.out.println("Done!\n");
				}
			}

			if(FileUtil.getCollision().size()>0) {
				System.out.println("Resolve Conflicts? (y/n)");
				if(scanner.nextLine().equals("y")) {
					List<FSFile> files = new ArrayList<>();
					System.out.println("To first or second source?");
					if(scanner.nextLine().toLowerCase().equals("first")) {				
						for(int i=1; i<FileUtil.getCollision().size();i+=2)
							files.add(FileUtil.getCollision().get(i));
						System.out.print("Synchronizing.");
						SyncUtil sync = new SyncUtil();
						sync.setTrg(pair.getSourceDir());
						sync.setFiles(files);
						sync.setTargetScheme(pair.getSourceScheme());
						sync.setToken(pair.getSourceToken());

						sync.copyToTarget();
					}
					else {
						for(int i=0; i<FileUtil.getCollision().size();i+=2)
							files.add(FileUtil.getCollision().get(i));

						System.out.print("Synchronizing.");
						SyncUtil sync = new SyncUtil();
						sync.setTrg(pair.getTargetDir());
						sync.setFiles(files);
						sync.setTargetScheme(pair.getTargetScheme());
						sync.setToken(pair.getTargetToken());

						sync.copyToTarget();
					}


				}

				System.out.println("Done!\n");
			}



		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			ChannelManager.getInstance().closeChannels();
		}
	}

	private static void displayCollision(List<FSFile> collision) {
		for(int i=0;i<collision.size();i+=2) {
			System.out.format("|%10s%70s%10s|%10s%70s%10s|\n","", collision.get(i).getFile(),"","",collision.get(i+1).getFile(),"");
		}



	}

	private static void displayOnlySource(List<FSFile> toTrg) {
		for(FSFile file:toTrg) {
			System.out.format("|%10s%70s%10s|%10s%70s%10s|\n","", file.getFile(),"","","...","");
		}


	}

	private static void displayOnlyTarget(List<FSFile> toSrc) {
		for(FSFile file:toSrc) {
			System.out.format("|%10s%70s%10s|%10s%70s%10s|\n","", "...","","",file.getFile(),"");

		}

	}


	private static void heading() {
		System.out.println("___________________________________________________________________________________________________________________________________________________________________________________________\n");
		System.out.format("|%90s|%90s|\n", "","");
		System.out.format("|%10s%70s%10s|%10s%70s%10s|\n","","FIRST SOURCE ","","","SECOND SOURCE","");
		System.out.format("|%90s|%90s|\n", "","");
		System.out.println("___________________________________________________________________________________________________________________________________________________________________________________________\n");
	}

}
