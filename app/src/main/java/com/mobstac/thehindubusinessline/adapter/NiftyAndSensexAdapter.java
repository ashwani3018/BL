package com.mobstac.thehindubusinessline.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.model.BSEData;
import com.mobstac.thehindubusinessline.model.BSETopGainer;

import java.text.DecimalFormat;
import java.util.List;

public class NiftyAndSensexAdapter extends BLStockAdapter {
    private DecimalFormat df = new DecimalFormat("#.##");

    public NiftyAndSensexAdapter(Context context, int resource,
                                 int textViewResourceId, List<Object> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public NiftyAndSensexAdapter(Context context, int resource,
                                 int textViewResourceId, Object[] objects) {
        super(context, resource, textViewResourceId, objects);
    }


    public NiftyAndSensexAdapter(Context context, int resource,
                                 int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public NiftyAndSensexAdapter(Context context, int resource,
                                 List<Object> objects) {
        super(context, resource, objects);
    }


    public NiftyAndSensexAdapter(Context context, int resource,
                                 Object[] objects) {
        super(context, resource, objects);
    }

    public NiftyAndSensexAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //BSETopGainer.DataEntity object = (BSETopGainer.DataEntity) getItem(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.nifty_sensex_row, null);
            holder = new ViewHolder();
            holder.mChange = (TextView) convertView.findViewById(R.id.change_row_value);
            holder.mPrice = (TextView) convertView.findViewById(R.id.price_row_value);
            holder.mName = (TextView) convertView.findViewById(R.id.name_row_value);
            holder.mPercentageChange = (TextView) convertView.findViewById(R.id.percent_change_row_value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position) instanceof BSEData) {
            BSEData object = (BSEData) getItem(position);
            holder.mPrice.setText(object.getCp() + "");
            holder.mName.setText(object.getSnapShot().getScn());
            double changePrice = object.getCh();
            double fromatedPrice = Double.valueOf(df.format(changePrice));

            holder.mChange.setText(fromatedPrice + "");
            holder.mPercentageChange.setText("(" + object.getPer() + "%" + ")");
        } else {
            BSETopGainer.Data object = (BSETopGainer.Data) getItem(position);
            holder.mPrice.setText(object.getCp());
            holder.mName.setText(object.getName());
            try {
                double changePrice = Double.parseDouble(object.getChange());
                double fromatedPrice = Double.valueOf(df.format(changePrice));
                holder.mChange.setText(fromatedPrice + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.mPercentageChange.setText("(" + object.getPer() + "%" + ")");
        }

        if (position % 2 == 0) {
            convertView.setBackgroundResource(R.color.forex_row_background);
        } else {
            convertView.setBackgroundResource(R.color.forex_row_background_second);
        }

        return convertView;
    }

    class ViewHolder {
        public TextView mPrice, mName, mChange, mPercentageChange;
    }


}
