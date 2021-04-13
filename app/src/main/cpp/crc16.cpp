//
// Created by hex on 2016/9/18.
//

#include <tgmath.h>
#include "crc16.h"
//#include "common_log.h"
/**
 *
 * @param puchMsg 需要处理的数据
 * @param usDataLen 数据长度
 * @return
 */
unsigned short getCRC16(unsigned char *puchMsg, int usDataLen) {
    unsigned char uchCRCHi = 0xFF;
    unsigned char uchCRCLo = 0xFF;
    unsigned short uIndex;
    while (usDataLen--) {
        //与当前位值进行异或运算,得到Crc表角标
        uIndex = uchCRCLo ^ *puchMsg++;
        //从表中取出高位值
        uchCRCLo = uchCRCHi ^ auchCRCHi[uIndex];
        //从crc低位表中取出对应的值
        uchCRCHi = auchCRCLo[uIndex];
    }
//    高8位和低8位进行合并得到最后的CRC校验值
    return (((unsigned short) (uchCRCHi) << 8) | uchCRCLo);
}

/**
 * 
 * @param puchMsg
 * @return
 */
unsigned short toCRC16(unsigned char *puchMsg) {
    unsigned char uchCRCHi = 0xFF;
    unsigned char uchCRCLo = 0xFF;
    int usDataLen = *(puchMsg + 3) + 3;
    unsigned short uIndex;
    puchMsg++;
    while (usDataLen--) {
        uIndex = uchCRCLo ^ *puchMsg++;
        uchCRCLo = uchCRCHi ^ auchCRCHi[uIndex];
        uchCRCHi = auchCRCLo[uIndex];
    }
    return (((unsigned short) (uchCRCHi) << 8) | uchCRCLo);
}

/**
 * 检验数据
 * @param puchMsg 收到的数据信息
 * @param begin 数据开始位
 * @param usDataLen 数据长度
 * @return 数据是否正确
 */
bool checkCRC16(unsigned char *puchMsg, int begin, int usDataLen) {
    unsigned char uchCRCHi = 0xFF;
    unsigned char uchCRCLo = 0xFF;
    unsigned short uIndex;
    //LOGI("----%d", usDataLen);
    begin++;
    while (usDataLen--) {
        uIndex = uchCRCLo ^ puchMsg[begin++];
        uchCRCLo = uchCRCHi ^ auchCRCHi[uIndex];
        uchCRCHi = auchCRCLo[uIndex];
    }
    //LOGI("----%x", uchCRCLo);
    //LOGI("----%x", uchCRCHi);
    //log_arr_Rdata(puchMsg, 11);
    return (uchCRCHi == puchMsg[begin + usDataLen + 1]) &&
           (uchCRCLo == puchMsg[begin + usDataLen + 2]);
}

/* 将十六进制中的字符装换为对应的整数 */

int hexchtoi(char hexch) {
    char phexch[] = "ABCDEF";

    char qhexch[] = "abcdef";

    int i;

    for (i = 0; i < 6; i++) {
        if ((hexch == phexch[i]) || (hexch == qhexch[i]))
            break;
    }

    if (i >= 6) {
        return 0; /* 非十六进制字符 */
    }
    return 10 + i;
}

int htoi(char s[]) {
    int n = 0;
    /*有n位*/
    int valu = 1;
    /*是否有效*/
    int i = 0, j;
    int answer = 0;
    /* 有效性检查 */
    if ((s[0] == '0') && ((s[1] == 'x') || (s[1] == 'X'))) {
        i += 2;
    }

    while ((s[i] != '\n')) {

        if ((s[i] < '0') && (s[i] > '9')) {
            if (hexchtoi(s[i]) == 0) {
                valu = 0;
                break;
            }
        }
        n++;
        i++;
    }
    if (valu != 0) {
        for (j = 0; j < n; j++) {
            answer += ((int) pow(16, j) * hexchtoi(s[i - j - 1]));
        }
    } else {
        answer = -1;
    }
    return answer;
}