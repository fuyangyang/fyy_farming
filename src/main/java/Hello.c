#include "fyy_jna_Hello.h"

JNIEXPORT void JNICALL Java_fyy_jna_Hello_sayHello(JNIEnv *env, jobject jobj){
  printf("Hello World!\n");
}
