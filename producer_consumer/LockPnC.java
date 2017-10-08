package com.model.producer_consumer;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

import java.util.concurrent.locks.ReentrantLock;

public class LockPnC<T>{
	private final LinkedList<T> list=new LinkedList<>();
	private final int MAX=10;
	private int count=0;
	ReentrantLock lock=new ReentrantLock();
	Condition producer=lock.newCondition();
	Condition consumer=lock.newCondition();
	
	public void put(T t){
		try{
			lock.lock();
			while(list.size()==MAX){
				producer.await();
			}
			list.add(t);
			count++;
			consumer.signalAll();
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}
	
	public T get(){
		T t=null;
		try{
			lock.lock();
			while(list.size()==0){
				consumer.await();
			}
			t=list.remove();
			count--;
			producer.signalAll();
			return t;
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
		return t;
	}
	
	public static void main(String[] args){
		LockPnC<String> m=new LockPnC<>();
		Object o=new Object();
		ReentrantLock lock=new ReentrantLock();
		
		for(int i=0;i<5;i++){
			new Thread(()->{
				for(int j=0;j<8;j++){
					synchronized(o)
					{
					System.out.println("消费者："+Thread.currentThread().getName()+"消费产品:"+m.get());
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
				for(int j=0;j<20;j++){
					//synchronized(o)
					{
						m.put(Thread.currentThread().getName()+' '+j);
						//System.out.println("produce product:"+Thread.currentThread().getName()+" "+j);
					}
				}
				
			},"p"+i).start();
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
}
