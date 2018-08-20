package view;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import adapter.TabAdapter;

/**
 * Created by lyd10892 on 2016/8/24
 * <p>
 * .
 */

public abstract class CustomTabLayout extends HorizontalScrollView {
    private Context mContext;
    protected LinearLayout mTabsContainer;
    private int mTabCount;
    private TabAdapter mTabAdapter;
    private LinearLayout.LayoutParams mDefaultLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);


    //下划线
    private Paint mPaint;
    private RectF mRectf;
    private float mRadius;
    private int underlineHeight = 8;
    private int underlineWidth;

    private AccelerateInterpolator mAccelerateInterpolator;
    private DecelerateInterpolator mDecelerateInterpolator;

    //
    private ViewPager mViewPager;
    private ScrollListener mScrollListener;
    private TabClickListener mTabClickListener;


    private DataSetObserver mObservable;


    public CustomTabLayout(Context context) {
        this(context, null);

    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        underlineHeight = dp2px(mContext,4);
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(mContext, 40)));
        setFillViewport(true);//如果不设置，weight属性无效
        init();
    }

    private void init() {

        mObservable = new DataSetObserver() {
            @Override
            public void onChanged() {
                notifyDataSetChanged();
            }

            @Override
            public void onInvalidated() {
            }
        };

        mTabsContainer = new LinearLayout(mContext);
        mTabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        mTabsContainer.setLayoutParams(mDefaultLayoutParams);
        mTabsContainer.setGravity(Gravity.CENTER);
        addView(mTabsContainer);


        mAccelerateInterpolator = new AccelerateInterpolator();
        mDecelerateInterpolator = new DecelerateInterpolator();

        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#F37664"));
        mPaint.setAntiAlias(true);
        underlineWidth = dp2px(mContext, 20);
        mRadius = underlineHeight/2;
        mRectf = new RectF();
    }

    public CustomTabLayout bindViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(new TabScrollListener());
        return this;
    }

    public CustomTabLayout setAdapter(TabAdapter tabAdapter) {
        if (mTabAdapter != null && mObservable != null) {
            mTabAdapter.unregisterDataSetObserver();
        }
        mTabAdapter = tabAdapter;
        mTabCount = mTabAdapter.getCount();
        mTabAdapter.registerDataSetObserver(mObservable);
        return this;
    }

    public void setOnTabClickListener(TabClickListener tabClickListener) {
        mTabClickListener = tabClickListener;
    }

    public void setScrollListener(ScrollListener listener) {
        mScrollListener = listener;
    }

    private void scrollToChild(int position, int offset) {
        if (mTabCount == 0) {
            return;
        }

        int newScrollX = 0;
        if (position >= 0 && position <= mTabCount - 1) {
            newScrollX = mTabsContainer.getChildAt(position).getLeft() + offset;
        }
        scrollTo(newScrollX, 0);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode() || mTabCount == 0) {
            return;
        }
        canvas.drawRoundRect(mRectf, mRadius, mRadius, mPaint);
    }

    boolean first;

    private class TabScrollListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(final int position, final float positionOffset, int positionOffsetPixels) {
            if (mScrollListener != null) {
                mScrollListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            if (first) {
                first = false;
                post(new Runnable() {
                    @Override
                    public void run() {
                        invalidateMaoMaoChongLine(position, positionOffset);
                    }
                });
            } else {
                invalidateMaoMaoChongLine(position, positionOffset);
            }


            if (position >= 0 && position <= mTabCount - 1 && mTabsContainer.getChildAt(position) != null) {
                int width = mTabsContainer.getChildAt(position).getWidth();
                scrollToChild(position, (int) (positionOffset * width));
            }


        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollListener != null) {
                mScrollListener.onPageSelected(position);
            }

            scrollToChild(position, 0);

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mScrollListener != null) {
                mScrollListener.onPageScrollStateChanged(state);
            }

        }
    }

    public interface TabClickListener {
        void onClick(View view, int position);
    }

    private void invalidateMaoMaoChongLine(int position, float positionOffset) {

        View curView = mTabsContainer.getChildAt(position);
        View nextView = mTabsContainer.getChildCount() - 1 > position ? mTabsContainer.getChildAt(position + 1) : curView;
        if (curView == null) {
            return;
        }
        mRectf.left = curView.getLeft() + curView.getMeasuredWidth() / 2 - underlineWidth / 2 + nextView.getWidth() * mAccelerateInterpolator.getInterpolation(positionOffset);
        mRectf.right = curView.getRight() - curView.getMeasuredWidth() / 2 + underlineWidth / 2 + nextView.getWidth() * mDecelerateInterpolator.getInterpolation(positionOffset);
        mRectf.top = getHeight() - underlineHeight * 1.5f;
        mRectf.bottom = getHeight();
        invalidate();
    }

    private void notifyDataSetChanged() {
        mTabsContainer.removeAllViews();
        mTabCount = mTabAdapter.getCount();
        for (int i = 0; i < mTabAdapter.getCount(); i++) {
            View childView = mTabAdapter.createItem(i);
            ViewGroup.LayoutParams params = childView.getLayoutParams();
            if (childView.getLayoutParams() == null) {
                params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                if (mTabCount > 5) {
                    params = new LinearLayout.LayoutParams((int) (getScreenWidth(mContext) / 5.5f), ViewGroup.LayoutParams.MATCH_PARENT);
                }
            }
            childView.setLayoutParams(params);
            mTabsContainer.addView(childView);
            final int finalI = i;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTabClickListener != null) {
                        mTabClickListener.onClick(v, finalI);
                    }
                    mViewPager.setCurrentItem(finalI, true);
                }
            });
        }
        onDataSetReady();
        invalidate();
    }

    public abstract class ScrollListener {


        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        public void onPageSelected(int position) {

        }

        public void onPageScrollStateChanged(int state) {

        }
    }

    //数据集合完毕
    protected void onDataSetReady(){

    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }
}
