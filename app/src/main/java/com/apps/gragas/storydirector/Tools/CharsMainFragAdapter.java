package com.apps.gragas.storydirector.Tools;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.gragas.storydirector.R;

import java.util.ArrayList;

public class CharsMainFragAdapter extends ArrayAdapter <String> {

    private final Activity context;
    private final ArrayList <String> charNames;
    private final Integer [] charIcons;

    public CharsMainFragAdapter (Activity context, ArrayList <String> charNames, Integer [] charIcons){
        super(context, R.layout.char_list_main_frag, charNames);

        this.charIcons=charIcons;
        this.context = context;
        this.charNames = charNames;
    }

    public View getView (int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rolView = inflater.inflate(R.layout.char_list_main_frag,null,true);

        TextView heroName = rolView.findViewById(R.id.tv_titleCharName);
        ImageView heroIcon = rolView.findViewById(R.id.iconHero_in_listMainFrag);


            heroIcon.setImageResource(charIcons[1]);
            heroName.setText(charNames.get(position));

        return rolView;
    };
}
