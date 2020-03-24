package com.mobstac.thehindubusinessline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobstac.thehindubusinessline.R;

import java.util.List;

/**
 * Created by 9920 on 10/31/2017.
 */

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private LayoutInflater inflater;
    private List<String> spinnerItems;

    public CustomSpinnerAdapter(Context context, int resource, List<String> spinnerItems) {
        super(context, resource, spinnerItems);
        this.spinnerItems = spinnerItems;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.spinner_dropdown, null);
        TextView type = (TextView) view.findViewById(R.id.spinner_title);
        type.setText(spinnerItems.get(i));
        return view;
    }
}