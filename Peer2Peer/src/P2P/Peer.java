package P2P;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Peer {

	//Number of recent file
	static int nfile = 0;	
	
	public static void main(String[] args) {
		try {
			
				InetAddress IP =  InetAddress.getLocalHost();
		
				Socket peerSocket = new Socket(IP, 3500);
				
				DataInputStream inn = new DataInputStream(peerSocket.getInputStream());
				DataOutputStream out = new DataOutputStream(peerSocket.getOutputStream());
				Scanner scanner = new Scanner(System.in);


				//User name
				System.out.println(inn.readUTF());
				out.writeUTF(scanner.nextLine());
				
				//Password
				System.out.println(inn.readUTF());
				out.writeUTF(scanner.nextLine());
				
				//Successful or not
				System.out.println(inn.readUTF());
				
				//Port
				int port = inn.readInt();
				
				
				Thread server = new ServerConnection(port);
				server.start();
				
				
				//Number of file
				int file_nums[] = new int[4];
				file_nums[0] = -1;
				file_nums[1] = -1;
				file_nums[2] = -1;
				file_nums[3] = -1;
				
				
				
				
				
				//Receiving file number
				file_nums[0] = inn.readInt() - 48;
				
				
				
				//Creating a file
				//char number_of_file = '1';
				File peerfile = new File("F:\\JavaFolder\\PeerJava" + file_nums[0] + ".txt");
				PrintWriter inpeerfile = new PrintWriter(peerfile);
				//nfile++;
				
				
				
				
				//Receiving a file from the tracker
				for (int i = 0; i < 5; i++) {
					
					String fileinfo = inn.readUTF();
	
					//Storing the info of the tracker in a file
					inpeerfile.println(fileinfo);
				
				}
				inpeerfile.close();
				
				System.out.println("File number " + file_nums[0] + " has been downloaded");
				
				
				
				//Receiving the track file
				File peertrackfile = new File("F:\\JavaFolder\\Peertrack.txt");
				PrintWriter inpeertrackfile = new PrintWriter(peertrackfile);

				
				for (int i = 0; i < 1; i++) {
				
					//Receiving a file from the tracker
					String trackfileinfo = inn.readUTF();

					//Storing the info of the tracker in a file
					inpeertrackfile.println(trackfileinfo);
					
				}
				inpeertrackfile.close();
				
				
				
				
				//Trying to download all the files
				while(true) {
				
					//Reading from the track file
					File peertracksfile = new File("F:\\JavaFolder\\Peertrack.txt");
					FileInputStream outpeertracksfile = new FileInputStream(peertracksfile);
					Scanner scanpeertrackfile = new Scanner(outpeertracksfile);
					
					//This variable to break from the loop
					int br = 0;
					
					if (scanpeertrackfile.hasNext()) {
					
						//Reading line by line from the track file
						int line = scanpeertrackfile.nextInt();
						
						
						for (int i = 0; i < 4; i++) {
							
							if (line == file_nums[i]) {
								br++;
							}
							
							else {
								
								file_nums[i+1] = line;
								
								int peerport = scanpeertrackfile.nextInt();
								
								System.out.println(peerport);
							
								//Connecting to another peer
								Socket peer = new Socket(IP, peerport);
								
								DataInputStream innpeer = new DataInputStream(peer.getInputStream());
								DataOutputStream outpeer = new DataOutputStream(peer.getOutputStream());
								
								//Hello from a peer
								System.out.println(innpeer.readUTF());
								
								File anpeerfile = new File("F:\\JavaFolder\\PeerJava" + file_nums[i+1] + ".txt");
								PrintWriter innpeerfile = new PrintWriter(anpeerfile);
								//nfile++;
								
								for (int l = 0; l < 5; l++) {
									
									String peerfileinfo = innpeer.readUTF();
					
									//Storing the info of the tracker in a file
									innpeerfile.println(peerfileinfo);
								
								}
								innpeerfile.close();	
							}
							}
						scanpeertrackfile.close();
						}
						
						
						
						if (br == 4) {
							break;
						}
							
					
					
					}
					/*
					
						*/
	
				
			} catch (IOException e) {
				System.out.println("400 Error");
			}
	}

	
	
	static class ServerConnection extends Thread {

		int port;

		public ServerConnection(int port) {
			this.port = port;
		}

		public void run() {
			try {

	                ServerSocket PeerSocket = new ServerSocket(port);
					System.out.println("The peer uploads on port " + port);
					
					while(true) {
						
						Socket clientSocket = PeerSocket.accept();
						System.out.println("A new client [" + clientSocket + "] is connected .");
						
						DataInputStream innpeer = new DataInputStream(clientSocket.getInputStream());
						DataOutputStream outpeer = new DataOutputStream(clientSocket.getOutputStream());
		
						outpeer.writeUTF("200 OK ");
						
						
						try{
							
							
							//Reading the peer file data
							File peerfile = new File("F:\\JavaFolder\\PeerJava2.txt");
							FileInputStream inpeerfile = new FileInputStream(peerfile);
							Scanner scanpeerfile = new Scanner(inpeerfile);
							
							while(scanpeerfile.hasNext()) {
							
								//Sending this peer file to another peer 
								outpeer.writeUTF(scanpeerfile.nextLine());
								
							}
							scanpeerfile.close();
							
						}catch(IOException e) {
							System.out.println("400 Error");
						}
						
						
				
				}
				
				
			} catch (IOException e) {
				
			}
		}
	}
}

