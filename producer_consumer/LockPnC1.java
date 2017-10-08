package com.model.producer_consumer;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public  class  LockPnC1<T> {
	final private LinkedList<T> l=new LinkedList<>();
	final private int MAX=10;
	private int count=0;
	ReentrantLock lock=new ReentrantLock();
	Condition consumer =lock.newCondition(); 
	Condition producer=lock.newCondition();
	
	public  void put(T t){
		
		
		
		try {
			lock.lock();
			while(l.size()==MAX){
				producer.await();
			}
			l.add(t);
			count++;
			consumer.signalAll();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			lock.unlock();
			
		}
		
		
		
	}
	
	public synchronized T get(){
		T t=null;
		try {
			lock.lock();
			while(l.size()==0){
				consumer.await();
			}
			t=l.removeFirst();
			count--;
			consumer.signalAll();
			return t;
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			lock.unlock();
			
		}
		return t;
		
	}
	
	public static void main(String[] args){
		LockPnC1<String> my=new LockPnC1<>();
		
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
	
	


