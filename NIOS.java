package com.tfl.templateCode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOS implements Runnable {
	
	private ByteBuffer readBuf=ByteBuffer.allocate(1024);
	private ByteBuffer writeBuf=ByteBuffer.allocate(1024);

	
	private Selector selector;
	
	public NIOS(int port){
		try {
			selector=Selector.open();
			ServerSocketChannel ssc=ServerSocketChannel.open();
			ssc.configureBlocking(false);
			//ssc.socket().setReuseAddress(true);
			ssc.bind(new InetSocketAddress(port));
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				//要让多路复用器去监听ServerSocket状态

				selector.select();
				Iterator<SelectionKey> it=selector.selectedKeys().iterator();
				
				while(it.hasNext()){
					SelectionKey key=it.next();
					it.remove();
					if(key.isValid()){
						if(key.isAcceptable()){
							//ServerSocketChannel server=(ServerSocketChannel)key.channel();
							accept(key);
						}
						if(key.isReadable()){
							read(key);
							
						}
						if(key.isWritable()){
							write(key);
						}
						
					}
					
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				
			}
		}
		
	}
	
	
	
	private void write(SelectionKey key) {
		// TODO Auto-generated method stub
		
		
		try {
			Object object=key.attachment();
			String attach=object!=null ? "server replay: "+object.toString() : "server replay: ";
			SocketChannel schannel=(SocketChannel) key.channel();
			schannel.write(ByteBuffer.wrap(attach.getBytes()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
//		SocketChannel sc=(SocketChannel)key.channel();
//		byte[] data=new byte[1024];
//		try {
//			System.in.read(data);
//			writeBuf.put(data);
//			sc.write(writeBuf);
//			writeBuf.clear();
			
			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
	}

	private void read(SelectionKey key) {
		// TODO Auto-generated method stub
		this.readBuf.clear();
		SocketChannel sc=(SocketChannel)key.channel();
		try {
			int index=sc.read(readBuf);
			if(index==-1){
				key.channel().close();
				key.cancel();
				return;
			}
			//对buffer进行复位
			readBuf.flip();
			byte[] bytes=new byte[readBuf.remaining()];
			readBuf.get(bytes);
			String body=new String(bytes).trim();
			System.out.println("服务器接收数据:"+body);
			readBuf.clear();
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void accept(SelectionKey key) {
		// TODO Auto-generated method stub
		
		try {
			ServerSocketChannel ssc=(ServerSocketChannel)key.channel();

			SocketChannel sc=ssc.accept();
			sc.configureBlocking(false);
			sc.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	public static void main(String[] args){
		
		new Thread(new NIOS(8765)).start();
		
	}

}
