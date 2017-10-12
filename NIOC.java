package com.tfl.templateCode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOC {
	
	private ByteBuffer readBuf=ByteBuffer.allocate(1024);
	private static Selector selector;
	
	public static void client(){
		SocketChannel channel=null;
		
		try {
			Selector selector=Selector.open();
			channel=SocketChannel.open();
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_CONNECT);
			channel.connect(new InetSocketAddress("127.0.0.1",8765));
			
			while(true){
				if(selector.select()>0){
					Iterator<SelectionKey> it=selector.selectedKeys().iterator();
					while(it.hasNext()){
						SelectionKey key=it.next();
						it.remove();
						
						SocketChannel ch=(SocketChannel)key.channel();
						if(key.isConnectable()){
							ch.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE, new Integer(1));
							ch.finishConnect();
						}
						if(key.isReadable()){
							key.attach(new Integer(1));
							ByteArrayOutputStream output=new ByteArrayOutputStream();
							ByteBuffer buffer=ByteBuffer.allocate(1024);
							int len=0;
							while((len=ch.read(buffer))!=0){
								buffer.flip();
								byte by[]=new byte[buffer.remaining()];
								buffer.get(by);
								output.write(by);
								buffer.clear();
							}
							System.out.println(new String(output.toByteArray()));
							output.close();
						}
						if(key.isWritable()){
							
							
							
							key.attach(new Integer(1));
							ch.write(ByteBuffer.wrap(("client say:hi").getBytes()));
							
						}
					}
				}
			}
			
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				channel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	
	public static void main(String[] args){
		client();
		
//		InetSocketAddress addr=new InetSocketAddress("127.0.0.1",8765);
//		//Selector selector=Selector.open();
//		
//		
//		ByteBuffer writeBuf=ByteBuffer.allocate(1024);
//		
//
//		
//		try {
//			SocketChannel sc=SocketChannel.open();
//			sc.connect(addr);
//			
//			while(true){
//				byte[] data=new byte[1024];
//				System.in.read(data);
//				
//				writeBuf.put(data);
//				writeBuf.flip();
//				sc.write(writeBuf);
//				
//				writeBuf.clear();
//				
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			
//		}

		
	}
	
	

}
