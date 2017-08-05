package com.example.miguelsoler.mipedidos.Types;


import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/****************************************************************************************
 * CREDITOS:__________________________________________________________
 * |  * | * | 01 |  31/7/2017          |  Shadowns                  | @Miguelslr
 * |__________________________________________________________________
 *******************************************************************************************/
public class FontText extends android.support.v7.widget.AppCompatTextView{

    public FontText(Context context) {
        super(context);
        init();
    }

    public FontText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/deadstock.ttf");
            setTypeface(tf);
        }
    }
}
