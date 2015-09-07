package com.github.somi92.hsmsdroid.util;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.somi92.hsmsdroid.R;
import com.github.somi92.hsmsdroid.domain.HSMSEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milos on 9/2/15.
 */
public class HSMSListAdapter extends ArrayAdapter<HSMSEntity> {

    private List<HSMSEntity> mEntities;

    public HSMSListAdapter(Context context, ArrayList<HSMSEntity> entities) {
        super(context, 0, entities);
        mEntities = entities;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HSMSEntity entity = mEntities.get(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView numBox = (TextView) convertView.findViewById(R.id.num_box);
        TextView description = (TextView) convertView.findViewById(R.id.description);
        TextView organisation = (TextView) convertView.findViewById(R.id.organisation);
        TextView priceBox = (TextView) convertView.findViewById(R.id.price);
        TextView websiteBox = (TextView) convertView.findViewById(R.id.website);

        numBox.setText(Html.fromHtml(entity.getNumber()));
        description.setText(Html.fromHtml(entity.getDesc()));
        organisation.setText(Html.fromHtml(entity.getOrganisation()));
        priceBox.setText(entity.getPrice());
        websiteBox.setText(Html.fromHtml(entity.getWeb()));

        return convertView;
    }
}
