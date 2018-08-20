package adapter;

import android.database.DataSetObserver;
import android.view.View;

public abstract class TabAdapter {

    private DataSetObserver mObserver;

    public void registerDataSetObserver(DataSetObserver observer){
        mObserver = observer;
    }
    public void unregisterDataSetObserver(){
        mObserver = null;
    }

    public int getCount() {
        return 0;
    }

    public View createItem(int position) {
        return null;
    }

    public void notifyDataSetChanged() {
        mObserver.onChanged();
    }
}