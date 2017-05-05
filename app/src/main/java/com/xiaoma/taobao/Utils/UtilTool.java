package com.xiaoma.taobao.Utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Description: 沉梦昂志
 * Data：2017/3/27-10:14
 * Author: xiaoma
 */

public class UtilTool {

    public static void hideKeyboard(Context mcontext,ViewGroup view){
        view.requestFocus();
        InputMethodManager im= (InputMethodManager) mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
        try{
            im.hideSoftInputFromWindow(view.getWindowToken(),0);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void showKeyboard(Context mcontext,View view){
        InputMethodManager im= (InputMethodManager) mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(view,0);
    }

    public static void toast(Context mcontext,String text){
        Toast.makeText(mcontext,text, Toast.LENGTH_SHORT).show();
    }

}
