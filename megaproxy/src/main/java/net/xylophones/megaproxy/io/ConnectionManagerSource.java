package net.xylophones.megaproxy.io;

public class ConnectionManagerSource {
	
    private static ThreadLocal<ConnectionManager> threadLocal = new ThreadLocal<ConnectionManager>() {
        protected synchronized ConnectionManager initialValue() {
            return new ConnectionManager();
        }
    };
	
	public static ConnectionManager getConnectionManager() {
		return threadLocal.get();
	}
	
}
