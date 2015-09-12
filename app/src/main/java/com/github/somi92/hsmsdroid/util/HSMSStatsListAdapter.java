package com.github.somi92.hsmsdroid.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.somi92.hsmsdroid.R;
import com.github.somi92.hsmsdroid.domain.HSMSStatsEntity;
import java.util.List;

/**
 * Created by milos on 9/12/15.
 */
public class HSMSStatsListAdapter extends ArrayAdapter<HSMSStatsEntity> {

    private List<HSMSStatsEntity> mStatsEntities;

    public HSMSStatsListAdapter(Context context, List<HSMSStatsEntity> statsEntities) {
        super(context, 0, statsEntities);
        mStatsEntities = statsEntities;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HSMSStatsEntity hsmsStatsEntity = mStatsEntities.get(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.stats_list_item, parent, false);
        }

        TextView descBox = (TextView) convertView.findViewById(R.id.stats_description);
        TextView numBox = (TextView) convertView.findViewById(R.id.stats_num_box);
        TextView messagesBox = (TextView) convertView.findViewById(R.id.stats_messages);
        TextView amountBox = (TextView) convertView.findViewById(R.id.stats_amount);
        TextView priceBox = (TextView) convertView.findViewById(R.id.stats_price);

        descBox.setText(hsmsStatsEntity.getActionDesc());
        numBox.setText(hsmsStatsEntity.getActionNumber());
        messagesBox.setText("Broj poruka: " + hsmsStatsEntity.getNumberOfDonations() + "");
        priceBox.setText(hsmsStatsEntity.getActionPrice());

        String amount = (Integer.parseInt(hsmsStatsEntity.getActionPrice().split(" ")[0])
                * hsmsStatsEntity.getNumberOfDonations()) + "din";
        amountBox.setText("Ukupan iznos: " + amount);

        return convertView;
    }
}
