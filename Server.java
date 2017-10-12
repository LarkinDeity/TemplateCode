package com.tfl.templateCode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private static int port=8765;
	public static void main(String[] args){
		ServerSocket server=null;
		try {
			server=new ServerSocket(port);
			
			System.out.println("server start..");
			Socket socket=server.accept();
			new Thread(new ServerHandler(socket)).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(server != null){
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			server = null;
			
		}
		

		
	}

}
