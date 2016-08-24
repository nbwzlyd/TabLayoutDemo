package view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyd.tablayout.R;

/**
 * Created by lyd10892 on 2016/8/24
 * <p>
 * .
 */

public class TabLayout extends LinearLayout {
    private Context mContext;
    private LayoutInflater mInflater;
    private String[] mTitles;
    private OnTabSelectListener mListener;//点击回调
    private SparseArray<View> mTitleViews;//保存一下我们的textView;
    private SparseArray<View> mLineViews;//保存一下我们的下划线


    public TabLayout(Context context) {
        this(context, null);

    }

    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mTitleViews = new SparseArray<>();
        mLineViews = new SparseArray<>();
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public void setTabSelectListener(OnTabSelectListener listener) {
        mListener = listener;
    }

    public void setData(String[] titles) {
        mTitles = titles;
        layoutViews();

    }

    private void layoutViews() {

        if (mTitles != null && mTitles.length != 0) {
            for (int i = 0; i < mTitles.length; i++) {
                final View childView = mInflater.inflate(R.layout.tab_layout_item, this, false);
                final TextView titleView = (TextView) childView.findViewById(R.id.hotel_tab_tv_title);
                TextView tvLine = (TextView) childView.findViewById(R.id.tv_line);
                if (i == 0) {
                    titleView.setTextColor(mContext.getResources().getColor(R.color.main_green));
                    tvLine.setVisibility(VISIBLE);
                }
                titleView.setText(mTitles[i]);
                this.addView(childView);
                final int position = i;
                childView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onTabSelect(position);
                            setTabTitleColor(position);
                        }
                    }
                });

                mTitleViews.put(i, titleView);
                mLineViews.put(i, tvLine);
            }

        }


    }

    private void setTabTitleColor(int position) {

        if (mTitleViews != null && mTitleViews.size() != 0) {
            for (int key = 0; key < mTitleViews.size(); key++) {
                TextView titleView = (TextView) mTitleViews.get(key);
                TextView lineView = (TextView) mLineViews.get(key);
                titleView.setTextColor(position == key ? mContext.getResources().getColor(R.color.main_green) :
                        mContext.getResources().getColor(R.color.main_secondary));
                lineView.setVisibility(position == key ? VISIBLE : GONE);
            }
        }


    }

    public void setTabSelect(int position) {
        setTabTitleColor(position);
        if (mListener != null) {
            mListener.onTabSelect(position);
        }
    }


    public void setRedPointVisible(int position) {
        View childView = this.getChildAt(position);
        View billView = childView.findViewById(R.id.hotel_tab_tv_first_bill);
        billView.setVisibility(VISIBLE);

    }

    public void setRedPointGone(int position) {
        View childView = this.getChildAt(position);
        View billView = childView.findViewById(R.id.hotel_tab_tv_first_bill);
        billView.setVisibility(GONE);

    }

    public interface OnTabSelectListener {
        void onTabSelect(int position);
    }


}
