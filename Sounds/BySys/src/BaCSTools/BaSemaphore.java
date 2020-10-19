package BaCSTools;

public class BaSemaphore
{
	// Variables
	private boolean raised = false;
	
	// Methods
	public synchronized void raise () {	
		raised = true; 
	}
	
	public synchronized void waitUntilDropped () throws InterruptedException {
		while (raised) wait ();
	}
	
	public synchronized void drop () {
		raised = false;
		this.notify();
	}
} // BaSemaphore
