#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_tp_recyclertree_RecyclerTreeSampleActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "sample key value ";
    return env->NewStringUTF(hello.c_str());
}