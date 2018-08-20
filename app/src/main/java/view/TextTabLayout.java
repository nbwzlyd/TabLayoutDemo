package view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TextTabLayout extends CustomTabLayout {

    public TextTabLayout(Context context) {
        this(context,null);
    }

    public TextTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScrollListener(new TextScrollListener());
    }

    private class TextScrollListener extends ScrollListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        }

        @Override
        public void onPageSelected(int position) {
            scaleText(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    public void scaleText(int position) {
        for (int i = 0, count = mTabsContainer.getChildCount(); i < count; i++) {
            View child = mTabsContainer.getChildAt(i);

            if (child != null && child instanceof TextView) {
                child.setScaleX(i == position ? 1.2f : 1f);
                child.setScaleY(i == position ? 1.2f : 1f);
                ((TextView) child).setTypeface(Typeface.defaultFromStyle(i == position ? Typeface.BOLD : Typeface.NORMAL));
            }
        }
    }

    @Override
    protected void onDataSetReady() {
        scaleText(0);
    }
}
