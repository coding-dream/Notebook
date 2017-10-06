package application.util;

public class IEProxys {
	static{
		System.loadLibrary("ie_proxy");
	}

	public native static String setProxy(byte[] proxy,int flag);

	public static void main(String[] args) {
		String result = IEProxys.setProxy("192.168.0.1:8888".getBytes(), 1);
		IEProxys.setProxy("192.168.0.1:8888".getBytes(), 0);
	}
}
