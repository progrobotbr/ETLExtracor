package main;

import scheduler.scheduler;
import scheduler.bean.beanScheduler;

public class Main {

	public static void main(String[] args) {
		
		scheduler sched = new scheduler();
		sched.execute();
		
	}
	
	public static void start(String[] args) {
		
		scheduler sched = new scheduler();
		sched.execute();
		
	}
	
	public static void stop(String[] args){
		
		beanScheduler bs = beanScheduler.factory();
		bs.setState(beanScheduler.ISSTOP);
		bs.End();

	}

}
