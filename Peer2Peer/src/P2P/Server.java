package P2P;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

//import P2P.Pear.ClientConnection;

public class Server {
	
	public static void main(String[] args){

		try {
			
			//Giving the racker a port number
			ServerSocket TrackerSocket = new ServerSocket(3500);
			System.out.println("Server is booted up");

			//Making a Thread to the Tracker
			while (true) {
				
				Socket clientSocket = TrackerSocket.accept();
				System.out.println("A new client [" + clientSocket + "] is connected .");

				Thread client = new ClientConnection(clientSocket);

				client.start();
			}
			
			
			
		} 
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	static class ClientConnection extends Thread {

		final private Socket clientSocket;

		public ClientConnection(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}
	
		
		public void run() {
			try {

				//Storing the user names of the peers
				String usernames[] = new String[8];
				usernames[0] = "Peer1";
				usernames[1] = "Peer2";
				usernames[2] = "Peer3";
				usernames[3] = "Peer4";
				usernames[4] = "Peer5";
				usernames[5] = "Peer6";
				usernames[6] = "Peer7";
				usernames[7] = "Peer8";

				//Storing the password of each peer account
				String pass[] = new String[8];
				pass[0] = "123";
				pass[1] = "456";
				pass[2] = "789";
				pass[3] = "147";
				pass[4] = "258";
				pass[5] = "369";
				pass[6] = "321";
				pass[7] = "654";
				
				
				//Storing each peer port number
				int ports[] = new int[8];
				ports[0] = 4000;
				ports[1] = 4001;
				ports[2] = 4002;
				ports[3] = 4003;
				ports[4] = 4004;
				ports[5] = 4005;
				ports[6] = 4006;
				ports[7] = 4007;
				
				
				//To write and read with the peers
				DataInputStream inn = new DataInputStream (clientSocket.getInputStream());
				DataOutputStream out = new DataOutputStream (clientSocket.getOutputStream());
				Scanner scanner = new Scanner(System.in);
				
				
				
				//Asking for the user name of the peer to log in
				out.writeUTF("Enter UserName: ");
				String username = inn.readUTF();
				
				
				//Comparing the entered user name with the existing ones
				boolean log = true;
				int account = -1;
				for (int i = 0; i < 8; i++) {
					
					if (username.equals(usernames[i])) {
						log = false;
						account = i;
						break; 
					}
				}
				
				
				//Asking for the password and compare it with the account password
				if (!log) {
					
					out.writeUTF("Enter your PassWord: ");
					String password = inn.readUTF();
					
					if (pass[account].equals(password)) {
						out.writeUTF("200 OK");
						
						//Sending a port number to a peer
						out.writeInt(ports[account]);
						
						//Giving a peer a specific file
						char file_num = 0;
						if (account == 0 || account == 4) {
							file_num = '1';
						}
						
						if (account == 1 || account == 5) {
							file_num = '2';
						}
						
						if (account == 2 || account == 6) {
							file_num = '3';
						}
						
						if (account == 3 || account == 7) {
							file_num = '4';
						}
						
						
						//To read from a file
						File file = new File("E:\\JavaFolder\\Java" + file_num + ".txt");
						FileInputStream infile = new FileInputStream(file);
						Scanner scanfile = new Scanner(infile);
						
						//Sending file number
						out.writeInt(file_num);
						
						
						//Sending a file for a peer
						while(scanfile.hasNext()) {
							
							out.writeUTF(scanfile.nextLine());
							
						}
						scanfile.close();
						
						
						
						
						
						
						//Storing the file names of the peers and the peers ports
						File trackfile = new File("E:\\JavaFolder\\tracks.txt");
						FileWriter intrackfile = new FileWriter(trackfile, true);
						
						intrackfile.write(file_num + " " + ports[account] + " ");
						intrackfile.close();
						
						
						
						
						//Sending the track file to the peer just logged in
						File tracksfile = new File("E:\\JavaFolder\\tracks.txt");
						FileInputStream streamtrackfile = new FileInputStream(tracksfile);
						Scanner scantrackfile = new Scanner(streamtrackfile);
						
						while(scantrackfile.hasNext()) {
							
							out.writeUTF(scantrackfile.nextLine());
							
						}
						scantrackfile.close();
					}
					
					else {
						out.writeUTF("Wrong Passowrd");
					}
					
					
					
					
					
					
						
				}
				
				
				else {
					out.writeUTF("There is no such Account");
				}
				
			} catch (IOException e) {
				
			}
		}

	private static int length(String[] usernames) {
		// TODO Auto-generated method stub
		return 0;
	}

}
}
