#include "jni.h"
/* Header for class application_util_IEProxys */

#ifndef _Included_application_util_IEProxys
#define _Included_application_util_IEProxys
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     application_util_IEProxys
 * Method:    setProxy
 * Signature: ([BI)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_application_util_IEProxys_setProxy
  (JNIEnv *, jclass, jbyteArray, jint);

#ifdef __cplusplus
}
#endif
#endif
