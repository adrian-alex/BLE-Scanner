package com.smartfocus.blescanner.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartfocus.blescanner.R;

/**
 * @author Adrian Antoci
 * @since 27/08/15
 */
public class BasicListAdapter extends AbstractListAdapter<Entity, BasicListAdapter.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater mInflater;

    public BasicListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(
                mInflater.inflate(R.layout.recycle_item, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bind(mData.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTextName;
        private final TextView mTextMac;
        private final TextView mTextRSSI;

        public ViewHolder(View v) {
            super(v);
            mTextName = (TextView) v.findViewById(R.id.ITEM_textView_name);
            mTextMac = (TextView) v.findViewById(R.id.ITEM_textView_mac);
            mTextRSSI = (TextView) v.findViewById(R.id.ITEM_textView_rssi);
        }

        public void bind(Entity entity) {
            mTextName.setText(entity.getBleScan().getName());
            mTextMac.setText(entity.getBleScan().getMac());
            mTextRSSI.setText(String.valueOf(entity.getBleScan().getRssi()));
        }
    }
}
