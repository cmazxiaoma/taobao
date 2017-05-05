package com.xiaoma.taobao.Utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/2/11.
 */

public class UtilsLog {
    //开关
    private static final Boolean DEBUG=true;
    //TAG,统一TAG，便于信息过滤
    private static final String TAG="CAONIMA";

    //这只写4个等级
    /**有5个级别
     * D,是指在细粒度信息事件对调试应用程序非常有帮助
     * I，是指在粗细度级别上突出强调应用运行的过程
     * W，是表明会出现的错误的形式，会导致应用程序的退出
     * E，是虽然发生错误事件，但是不影响整个系统的运行
     * F，指出每个严重的错误事件将会导致应用程序的退出
     *
     */
    public static void d(String text){
        if(DEBUG){
            Log.d(TAG,text);

        }
    }

    public static void i(String text){
        if(DEBUG){
            Log.i(TAG,text);

        }
    }

    public static  void e(String text){
        if(DEBUG){
            Log.e(TAG,text);

        }
    }

    public static void w(String text){
        if(DEBUG){
            Log.w(TAG,text);
        }

    }

    public static void f(String text){
        if(DEBUG){
            Log.wtf(TAG,text);
        }
    }
}
