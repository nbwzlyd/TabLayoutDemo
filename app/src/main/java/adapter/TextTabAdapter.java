package adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TextTabAdapter extends TabAdapter {
    private ArrayList<String> mDatas = new ArrayList<>();
    private Context mContext;

    public TextTabAdapter(Context context){
        mContext = context;
    }
    public void bindData(ArrayList<String> data){
        mDatas = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public View createItem(int position) {
        TextView textView = new TextView(mContext);
        textView.setSingleLine();
        textView.setGravity(Gravity.CENTER);
        textView.setText(mDatas.get(position));
        return textView;
    }
}
