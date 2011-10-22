package net.xylophones.threadtest;

public class TestClass {
	
	private volatile Integer myInteger = new Integer(1);
	
	private volatile Integer myInteger2 = new Integer(2);

	private volatile long startTime = 0; 
	
	private volatile boolean running = true; 
	
	private volatile boolean changed = false; 

	public static void main(String args[]) {
		TestClass test = new TestClass();
		test.doTest();
	}
	
	private void doTest() {
		System.out.println("myInteger: " + System.identityHashCode(myInteger));
		System.out.println("myInteger2: " + System.identityHashCode(myInteger2));
		startTime = System.currentTimeMillis();
		
		Thread t1 = new Thread( new GetLock() );
		t1.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		
		synchronized (myInteger) {
			System.out.println("Main got lock on: " + System.identityHashCode(myInteger));

			while (startTime + 5000 > System.currentTimeMillis()) {
				if ( ! changed && (startTime + 2000) < System.currentTimeMillis() ) {
					System.out.println("changing reference");
					myInteger = myInteger2;
					System.out.println("myInteger: " + System.identityHashCode(myInteger));
					changed = true;
				}
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}
			
			System.out.println("Main releasing lock");
		}
		
		running = false;
		
		System.out.println("main complete");
	}
	
	private class GetLock implements Runnable {
		public void run() {
			while (running) {
				System.out.println("GetLock attempting to get lock");
				synchronized (myInteger) {
					System.out.println("GetLock synchronised on: " + System.identityHashCode(myInteger) + " - releasing");
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}				
			}
			
			System.out.println("GetLock complete");
		}
	}
}
