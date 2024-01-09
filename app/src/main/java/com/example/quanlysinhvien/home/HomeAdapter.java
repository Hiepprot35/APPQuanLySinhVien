package com.example.quanlysinhvien.home;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlysinhvien.R;

import java.util.List;
import java.util.zip.Inflater;

public class HomeAdapter extends ArrayAdapter<Home> {
    Activity context;
    int IdLayout;
    List<Home> homelist;

    public HomeAdapter(@NonNull Activity context,  List<Home> homelist) {
        super(context, 0 ,homelist);
        this.context = context;
        this.homelist = homelist;
    }

    @NonNull
    @Nullable
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_home, parent, false);
        }
        Home myhome = homelist.get(position);
        ImageView img_manager = convertView.findViewById(R.id.img_item);
        img_manager.setImageResource(myhome.getImages());
        TextView name_manager = convertView.findViewById(R.id.txt_nameHome);
        name_manager.setText(myhome.getName_manager());
        return convertView;
    }

}
