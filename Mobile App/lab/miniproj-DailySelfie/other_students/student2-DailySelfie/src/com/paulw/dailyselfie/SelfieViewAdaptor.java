package com.paulw.dailyselfie;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by paulweir on 11/16/15.
 */

public class SelfieViewAdaptor extends BaseAdapter {

    private ArrayList<SelfieRecord> list = new ArrayList<SelfieRecord>();
    private static LayoutInflater inflater = null;
    private Context mContext;

    public SelfieViewAdaptor(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = convertView;
        ViewHolder holder;

        SelfieRecord curr = list.get(position);

        if (null == convertView) {
            holder = new ViewHolder();
            newView = inflater.inflate(R.layout.selfie_list_view, parent, false);
            holder.selfieImage = (ImageView) newView.findViewById(R.id.selfie_preview);
            holder.date = (TextView) newView.findViewById(R.id.selfie_preview_name);
            newView.setTag(holder);

        } else {
            holder = (ViewHolder) newView.getTag();
        }

        holder.selfieImage.setImageBitmap(curr.getSelfiePreview());
        holder.date.setText(curr.getSelfieDateTime());
        holder.uriLoaction = Uri.fromFile(new File(curr.getSelfieFilePath()));

        return newView;
    }

    public void add(SelfieRecord listItem) {
        list.add(listItem);
        notifyDataSetChanged();
    }

    public ArrayList<SelfieRecord> getList() {
        return list;
    }

    public void removeAllViews() {
        list.clear();
        this.notifyDataSetChanged();
    }

    static class ViewHolder {
        Uri uriLoaction;
        ImageView selfieImage;
        TextView date;
    }
}
