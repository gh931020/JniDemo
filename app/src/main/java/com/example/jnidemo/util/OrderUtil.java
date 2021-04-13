package com.example.jnidemo.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : zhenjieZhang
 * @date : 2021/4/12
 * @apiNote : 指令处理util
 **/
public class OrderUtil {
    /**
     * 处理粘包,不完整指令
     * @param orders
     * @return
     */
    public static List<String[]> handleOrder(String[] orders){

        ArrayList<String[]> strings = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        boolean isAdd = false;
        //把数组转变为字符串形式
        for (int i = 0; i < orders.length; i++) {
            //判断当前字符是不是起始符,如果是起始符,开始添加进buffer
            if ("AA".equals(orders[i].toUpperCase())){
                stringBuffer.delete(0, stringBuffer.length());
                isAdd = true;
            }
            //判断当前字符是不是终止符,如果是,停止添加,isAdd设置为false,并把结果放入array
            if ("DA".equals(orders[i].toUpperCase())){
                if (isAdd){
                    stringBuffer.append(orders[i].toUpperCase());
                    strings.add(stringBuffer.toString().split(" "));
                    isAdd = false;
                }
            }

            if (isAdd){
                stringBuffer.append(orders[i].toUpperCase() + " ");
            }
        }
        return strings;
    }
}
