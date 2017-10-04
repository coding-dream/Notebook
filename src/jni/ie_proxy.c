#include <stdio.h>
#include <windows.h>
#include <conio.h>
#include "application_util_IEProxys.h"
#define PROXY_REG_ITEM "Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings"

char global_proxy_server[256] = {0};

JNIEXPORT jstring JNICALL Java_application_util_IEProxys_setProxy
  (JNIEnv * env, jclass clazz, jbyteArray j_proxy_array,jint flag){
	JNIEnv J = *env;
	char * c_proxy_array;
	int proxy_length;
	proxy_length = J->GetArrayLength(env,j_proxy_array);

	c_proxy_array = (char *) malloc(sizeof(char) * proxy_length);
	memset(c_proxy_array, 0, sizeof(jbyte) * proxy_length);

	J->GetByteArrayRegion(env,j_proxy_array, 0, proxy_length, c_proxy_array);

	strncpy(global_proxy_server, c_proxy_array, sizeof(global_proxy_server));
	free(c_proxy_array);
	setProxy(flag,J);

	char *result = "success";
	return (*env)->NewStringUTF(env, result);
}

void setProxy(int flag,JNIEnv * env){
	JNIEnv J = *env;
	HKEY hKey = NULL;
	LONG lret = RegOpenKeyEx(HKEY_CURRENT_USER,PROXY_REG_ITEM,NULL,KEY_WRITE|KEY_SET_VALUE,&hKey);
	if(hKey == NULL || lret != ERROR_SUCCESS){
		J->ThrowNew(env, J->FindClass(env, "java/lang/RuntimeException"), "Proxy Setting Failed");
	}

	if(flag == 1){
		// enable or disable
		lret = RegSetValueEx(hKey,"ProxyServer",NULL,REG_SZ,(BYTE *) global_proxy_server,sizeof(global_proxy_server));
        DWORD dwenable = 1;
        lret = RegSetValueEx(hKey,"ProxyEnable",NULL,REG_DWORD,(LPBYTE) & dwenable,sizeof(dwenable));

	}else{
		DWORD dwenable = 0;
		lret = RegSetValueEx(hKey,"ProxyEnable",NULL,REG_DWORD,(LPBYTE) & dwenable,sizeof(dwenable));
	}
	RegCloseKey(hKey);
}
