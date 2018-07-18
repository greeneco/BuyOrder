package com.example.hyunwoo.buyorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hyunwoo on 2018-02-14.
 */
public class OrderListAdapter extends BaseAdapter {
    public ArrayList<OrderList> list = new ArrayList<>();

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override

    public Object getItem(int position) {
        return list.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.orderitem, parent, false);
        }

        TextView Textproductsub = (TextView) convertView.findViewById(R.id.Textproductsub);
        TextView Textoriginsub = (TextView) convertView.findViewById(R.id.Textoriginsub);
        TextView Textordersub = (TextView) convertView.findViewById(R.id.Textordersub);
        TextView Textbuysub = (TextView) convertView.findViewById(R.id.Textbuysub);
        TextView Textpricesub = (TextView) convertView.findViewById(R.id.Textpricesub);
        TextView Textrequestsub = (TextView) convertView.findViewById(R.id.Textrequestsub);

        OrderList orderViewItem = list.get(position);

        Textproductsub.setText(orderViewItem.getProduct());
        Textoriginsub.setText(orderViewItem.getOrigin());
        Textordersub.setText(orderViewItem.getOrderkg().toString());
        Textbuysub.setText(orderViewItem.getBuykg().toString());
        Textpricesub.setText(orderViewItem.getPrice().toString());
        Textrequestsub.setText(orderViewItem.getRequest());

        /*if (list.get(position).getOrderkg().equals(list.get(position).getBuykg())) {
            convertView.setBackgroundColor(0xFF99FF99);
        }*/
        return convertView;
    }

    public void addItem(String product, String origin, String orderkg, String buykg, String price, String request) {
        OrderList Item = new OrderList(product, origin, orderkg, buykg, price, request);

        Item.setProduct(product);
        Item.setOrigin(origin);
        Item.setOrderkg(orderkg);
        Item.setBuykg(buykg);
        Item.setPrice(price);
        Item.setRequest(request);

        list.add(Item);
    }
}
