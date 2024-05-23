#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_tp_recyclertree_RecyclerTreeSampleActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */,
        jint type) {
    std::string hello = "sample key value sdcdsvdsvdv ";
    std::string decryption = "decryption.. ";


    if(type  == 1){
        return env->NewStringUTF(hello.c_str());
    }else if(type  == 2){
        return env->NewStringUTF(decryption.c_str());
    }
    return env->NewStringUTF(hello.c_str());
}





extern "C" JNIEXPORT jlong JNICALL
Java_com_tp_recyclertree_SecondActivity_initModel(JNIEnv* env,
                                                              jobject /* this */,
                                                              jfloat ratio) {
    return 199;
}




