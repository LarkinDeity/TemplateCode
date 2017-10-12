package com.tfl.templateCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	final static String ADDRESS="127.0.0.1";
	final static int PORT=8765;
	
	public static void main(String[] args){
		Socket socket=null;
		BufferedReader in=null;
		PrintWriter out=null;
		StringBuffer str=new StringBuffer();
		Scanner sc=new Scanner(System.in);
		
		try{
			socket=new Socket(ADDRESS,PORT);
			in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out=new PrintWriter(socket.getOutputStream(),true);
			
//			str=str.append(sc.nextLine());
//			out.println(str);
//			String response =in.readLine();
//			System.out.println("Client: " + response);
			out.println("���յ��ͻ��˵���������...");
			String response = in.readLine();
			System.out.println("Client: " + response);

		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(out != null){
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(socket != null){
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			socket = null;
		}
		
	}
	

}
