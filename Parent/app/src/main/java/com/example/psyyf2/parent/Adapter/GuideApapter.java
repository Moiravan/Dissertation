package com.example.psyyf2.parent.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.psyyf2.parent.R;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

/**
 * Created by moiravan on 2018/4/12.
 */

public class GuideApapter extends StaticPagerAdapter {
    //load images
    private int[] imgs = {R.drawable.guide01p, R.drawable.guide02p, R.drawable.guide03p, R.drawable.guide04p,
    };

    @Override
    public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
            }

    @Override
    public int getCount()
    {
        return imgs.length;

    }


}
