package br.com.diegosilva.buscalayoutlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;

/**
 * Created by Diego on 30/11/2015.
 */
public class UIUtils {

    public static int getThemeAttributeDimensionSize(Context context, int attr)
    {
        TypedArray a = null;
        try{
            a = context.getTheme().obtainStyledAttributes(new int[] { attr });
            return a.getDimensionPixelSize(0, 0);
        }finally{
            if(a != null){
                a.recycle();
            }
        }
    }
}
