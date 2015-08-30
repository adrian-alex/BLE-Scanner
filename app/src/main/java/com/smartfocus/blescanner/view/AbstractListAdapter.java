package com.smartfocus.blescanner.view;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adrian Antoci
 * @since 27/08/15
 */
public abstract class AbstractListAdapter<V, K extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<K> {

    protected List<V> mData = new ArrayList<>();

    @Override
    public abstract K onCreateViewHolder(ViewGroup viewGroup, int i);

    @Override
    public abstract void onBindViewHolder(K k, int i);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(final List<V> data) {
        mData = data;
        notifyDataSetChanged();
    }
}
