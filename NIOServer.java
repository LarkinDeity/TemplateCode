package com.larkin.useful;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer {
	
	private Selector selector;
	
	public void initServer(int port)throws IOException{
		
		ServerSocketChannel serverChannel=ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		serverChannel.socket().bind(new InetSocketAddress(port));
		
		this.selector=Selector.open();
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);	
		
	}
	
	public void listen() throws IOException{
		System.out.println("服务器端启动成功");
		while(true){
			selector.select();
			
			Iterator ite =this.selector.selectedKeys().iterator();
			while(ite.hasNext()){
				SelectionKey key=(SelectionKey)ite.next();
				ite.remove();
				if(key.isAcceptable()){
					
					ServerSocketChannel server=(ServerSocketChannel)key.channel();

					SocketChannel channel=server.accept();
					channel.configureBlocking(false);
					channel.write(ByteBuffer.wrap(new String("客户端发送来的一条消息").getBytes()));
					
					channel.register(this.selector, SelectionKey.OP_READ);
					
				}else if(key.isReadable()){
					read(key);
				}
			}
		}
	}
	public void read(SelectionKey key)throws IOException{
		SocketChannel channel=(SocketChannel)key.channel();
		
		ByteBuffer buffer=ByteBuffer.allocate(10);
		channel.read(buffer);
		byte[] data =buffer.array();
		String msg=new String(data).trim();
		System.out.println("服务器端收到消息:"+msg);
		ByteBuffer outBuffer=ByteBuffer.wrap(msg.getBytes());
		channel.write(outBuffer);
	}
	
	public static void main(String[] args) throws IOException {  
        NIOServer server = new NIOServer();  
        server.initServer(8000);  
        server.listen();  
    }  

}
