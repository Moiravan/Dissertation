package com.example.psyyf2.dissertation.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.psyyf2.dissertation.R;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

/**
 * Created by moiravan on 2018/4/12.
 */

public class GuideAdapter extends StaticPagerAdapter {
    //images in guide page
    private int[] imgs = {R.drawable.guide01, R.drawable.guide02, R.drawable.guide03, R.drawable.guide04,};

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
