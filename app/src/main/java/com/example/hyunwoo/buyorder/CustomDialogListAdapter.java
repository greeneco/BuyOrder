package com.example.hyunwoo.buyorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hyunwoo on 2018-02-22.
 */

public class CustomDialogListAdapter extends BaseAdapter {
    public ArrayList<BuyViewItem> buylist1 = new ArrayList<>();
    private CustomDialog activity;

    CustomDialogListAdapter(CustomDialog activity) {
        this.activity = activity;
    }

    public CustomDialogListAdapter(MainActivity activity) {
    }

    @Override
    public int getCount() {
        return buylist1.size();
    }

    @Override
    public Object getItem(int position) {
        return buylist1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.popupitem, parent, false);
        }

        TextView popitemmarket = (TextView) convertView.findViewById(R.id.popitemmarket);
        TextView popitemorigin = (TextView) convertView.findViewById(R.id.popitemorigin);
        TextView popitemkg = (TextView) convertView.findViewById(R.id.popitemkg);
        TextView popitemprice = (TextView) convertView.findViewById(R.id.popitemprice);
        TextView popitemorder = (TextView) convertView.findViewById(R.id.popitemorder);

        BuyViewItem buyViewItem = buylist1.get(position);

        popitemmarket.setText(buyViewItem.getBuycom1());
        popitemorigin.setText(buyViewItem.getOrigin1());
        popitemkg.setText(Double.toString(buyViewItem.getKG1()));
        popitemprice.setText(Integer.toString(buyViewItem.getPrice1()));
        popitemorder.setText(buyViewItem.getOrder1());

        return convertView;
    }

    public void addItem(String buycom, String origin, Double KG, int price, String order) {
        BuyViewItem item = new BuyViewItem();

        item.setBuycom1(buycom);
        item.setOrigin1(origin);
        item.setKG1(KG);
        item.setPrice1(price);
        item.setOrder1(order);
        buylist1.add(item);
    }
}
