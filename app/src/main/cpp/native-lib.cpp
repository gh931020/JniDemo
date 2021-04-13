#include <jni.h>
#include <string>
#include <unistd.h>
#include "crc16.h"
#include<android/log.h>

//定义输出的TAG
const char *LOG_TAG = "LOG_NATIVE";

jboolean stop = false;
/**
 * 获取温度
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnidemo_activity_FirstActivity_temperatureFromJNI(JNIEnv *env, jobject thiz) {
    // 获取 MainActivity 的 jclass
    jclass mainActivityClass = env->GetObjectClass(thiz);
    const char *sig = "(D)V";
    jmethodID mainMethodId = env->GetMethodID(mainActivityClass, "setTemperature", sig);
    jdouble temp = 20.00;
    stop = false;
    while (!stop) {
        temp += 0.5;
        // 调用 Activity 方法更新UI
        env->CallVoidMethod(thiz, mainMethodId, temp);
        //模拟耗时操作,线程睡1s
        sleep(1);
    }
}
/**
 * 控制循环停止
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnidemo_activity_FirstActivity_stopFromJNI(JNIEnv *env, jobject thiz) {
    stop = true;
}
/**
 * 获取温度值
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnidemo_activity_SecondActivity_getTemp(JNIEnv *env, jobject thiz) {
    // 必须创建一个 全局变量 来公用参数 如果是局部变量方法出栈后 对象将被释放
//    jobject secondActivityObj = env->NewGlobalRef(thiz);

    // 获取 secondActivity 的 jclass
    jclass secondActivityClass = env->GetObjectClass(thiz);
    const char *sig = "(ID)V";
    jmethodID mainMethodId = env->GetMethodID(secondActivityClass, "setTemp", sig);
    jdouble temp = 20.00;
    stop = false;

    while (!stop) {
        temp += rand() % 20;
        temp += rand() % 10 / 10.0;
        // 调用 Activity 方法更新UI
        env->CallVoidMethod(thiz, mainMethodId, rand() % 8, temp);
        temp = 20.00;
        //模拟耗时操作,线程睡1s
        sleep(1);
    }

}
/**
 * 控制循环停止
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnidemo_activity_SecondActivity_stopFromJNI(JNIEnv *env, jobject thiz) {
    stop = true;
}
/**
 * 获取指令
 * 帧头：0xAA或0xBB。
 * 指令：两个字节，0x01~0x99; 由0~9字符组成，不包含英文字母。
 * 数据长度：1个字节，0x00~0xFF，值为数据长度n。
 * 数据：1个字节，0x00~0xff;
 * 数据：0x00~0xff。
 * CRC16:校验从指令开始计算，到最后一个数据结束，高位在前。
 * 结束符： 1字节，0xDA。
 * 波特率115200，无校验，1个停止位。
 */

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnidemo_activity_ThirdActivity_startReceive(JNIEnv *env, jobject thiz) {

    jclass thirdActivityClass = env->GetObjectClass(thiz);

    jmethodID methodId = env->GetMethodID(thirdActivityClass, "receiveOrder", "([C)V");

    jchar data[8] = {0xAA, 0xF2, 0xC6, 0xD6, 0xC5, 0x63};
    data[6] = getCRC16(reinterpret_cast<unsigned char *>(data),
                       strlen(reinterpret_cast<const char *const>(data)));
    data[7] = 0xDA;
    jcharArray array = env->NewCharArray(8);
    env->SetCharArrayRegion(array, 0, 8, data);
    env->CallVoidMethod(thiz, methodId, array);

    env->DeleteLocalRef(thirdActivityClass);
}
/**
 * 获取错误的指令
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnidemo_activity_ThirdActivity_getOrderData(JNIEnv *env, jobject thiz) {

    jclass thirdActivityClass = env->GetObjectClass(thiz);

    jmethodID methodId = env->GetMethodID(thirdActivityClass, "receiveOrder", "([C)V");
    //BB	05	01	Len	手柄类型 	插入/拔出	治疗头类型	其他功能	CRC16	DA
    jchar order[10] = {0xBB, 0x05, 0x01, 0x04, 0x01, 0x01, 0x02, 0x01, 0x8D2E, 0xDA};

    //解包
    //1-->判断包的完成性
    if (order[0] == 0xBB) {
        //获取数据长度
        jint length = order[3];
        //根据数据长度,获取包尾位置,判断包尾字符是不是DA
        if (order[length + 5] == 0xDA) {
            //2-->确定包完整, 校对CRC
            unsigned char temp[length + 3];
            for (int i = 1; i < length + 4; ++i) {
                temp[i - 1] = order[i];
            }
            //调用c方法,获取crc值
            jchar crc = getCRC16((unsigned char *) temp, length + 3);

            __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "CRC: %x", crc);

            if (crc == order[length + 4]) {
                //3-->取出数据区
                jchar data[length];
                for (int i = 4; i < length + 4; ++i) {
                    data[i - 4] = order[i];
                }
                //把jchar转换为charArray
                jcharArray array = env->NewCharArray(length);
                env->SetCharArrayRegion(array, 0, length, data);
                //4-->发送数据区给java
                env->CallVoidMethod(thiz, methodId, array);
            } else {
                __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "crc ->不一致,原:%1x, 计算: %2x",
                                    order[length + 4], crc);
            }
        } else {
            __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "包尾 ->解析错误");
        }
    } else {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "包头 ->解析错误");
    }
}

/**
 * 发送指令
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnidemo_activity_ThirdActivity_sendOrder(JNIEnv *env, jobject thiz, jcharArray chars, jint length) {
    //1-->将chars转换为char数组
    jchar *charData = env->GetCharArrayElements(chars, 0);
    //2-->定义存放数据的char数组,用于计算crc
    //添加指令数据
    unsigned char data[length + 3];
    data[0] = 0x05;
    data[1] = 0x01;
    //添加数据长度
    data[2] = length;
    //将charData中的数据添加进data
    for (int i = 3; i < length + 3; ++i) {
        data[i] = charData[i - 3];
    }
    //3-->调用c方法,获取crc值
    jchar crc = getCRC16((unsigned char *) data, length + 3);
    //将crc值添加进data
    data[length + 4] = crc;
    //4-->添加包头,包尾 1+2+1+4+1+1
    jchar compelte[length + 6];
    compelte[0] = 0xBB;
    for (int i = 1; i < length + 5; ++i) {
        compelte[i] = data[i - 1];
    }
    compelte[length + 5] = 0xDA;
    __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "sendOrder: %1x,%2x,%3x,%4x",
                        compelte[4], compelte[5], compelte[6], compelte[7]);
}
