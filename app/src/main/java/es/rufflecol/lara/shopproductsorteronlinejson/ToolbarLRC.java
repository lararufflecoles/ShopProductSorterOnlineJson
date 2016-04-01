package es.rufflecol.lara.shopproductsorteronlinejson;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

public class ToolbarLRC extends Toolbar {

    public ToolbarLRC(Context context) {
        super(context);
        initialise(context);
    }

    public ToolbarLRC(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialise(context);
    }

    public ToolbarLRC(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialise(context);
    }

    private void initialise(Context context) {
        setTitleTextAppearance(context, R.style.ToolbarAppearance);
    }
}