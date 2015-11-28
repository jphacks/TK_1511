package tk11.jphacks.titech.controller.IconFont;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

import tk11.jphacks.titech.R;

/**
 * Created by hirokinaganuma on 15/11/28.
 */
public class CustomFontButton extends Button {
    private static final String TAG = "CustomFontButton";

    public CustomFontButton(Context context) {
        super(context);
    }

    public CustomFontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public CustomFontButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomFontButton);
        String customFont = a.getString(R.styleable.CustomFontButton_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context context, String asset) {
        Typeface tf = null;
        try {
            // ここでフォントファイル読み込み。
            // 読み込み済みならキャッシュから。
            tf = CachedTypefaces.get(context, asset);
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: " + e.getMessage());
            return false;
        }

        setTypeface(tf);
        return true;
    }
}