package com.model.producer_consumer;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public  class  SynPnC<T> {
	final private LinkedList<T> l=new LinkedList<>();
	final private int MAX=10;
	private int count=0;
	
	public synchronized void put(T t){
		while(l.size()==MAX){
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		l.add(t);
		++count;
		this.notifyAll();
		
		
		
	}
	
	public synchronized T get(){
		T t=null;
		while(l.size()==0){
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		t=l.removeFirst();
		count--;
		this.notifyAll();
		return t;
		
	}
	
	public static void main(String[] args){
		SynPnC<String> my=new SynPnC<>();
		
		for(int i=0;i<5;i++){
			new Thread(()->{
				for(int j=0;j<10;j++){
					//synchronized(my)
					{
					System.out.println("消费者："+Thread.currentThread().getName()+"消费产品:"+my.get());
					}
				}
				},"c"+i).start();
		}
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=0;i<2;i++){
			new Thread(()->{
				for(int j=0;j<25;j++){
					synchronized(my)
					{
					my.put(Thread.currentThread().getName()+" "+j);
					System.out.println("生产："+Thread.currentThread().getName()+" "+j);
					}
				}
				
			},"p"+i).start();
		}
		
	}
	
	

}
