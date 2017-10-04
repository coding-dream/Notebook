package application.util;

public class IEProxys {
	static{
		System.loadLibrary("ie_proxy");
	}

	public native static String setProxy(byte[] proxy,int flag);

}
