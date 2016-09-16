package com.inter.aktiehq.app;

/**
 * Obsolete
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class AktiendetailFragment extends Fragment {


    public AktiendetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_beacondetail, container, false);


        // Die AktiendetailActivity wurde über einen Intent aufgerufen
        // Wir lesen aus dem empfangenen Intent die übermittelten Daten aus
        Intent empfangenerIntent = getActivity().getIntent();
        if (empfangenerIntent != null && empfangenerIntent.hasExtra(Intent.EXTRA_TEXT)) {
            String aktienInfo = empfangenerIntent.getStringExtra(Intent.EXTRA_TEXT);
            //((TextView) rootView.findViewById(R.id.aktiendetail_text)).setText(aktienInfo);

            BeacondetailView bv = new BeacondetailView(getActivity());

            RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.body);
            relativeLayout.addView(new BeacondetailView(getActivity()));


        }

        return rootView;
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        *//*
         *  Create the layout
         *//*
        MyRelativeLayout layout = new MyRelativeLayout(getActivity());
        layout.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        *//*
         *  Inflate your xml view
         *//*
        View rootView = inflater.inflate(R.layout.fragment_beacondetail, container, false);

        TextView txt1 = (TextView) rootView.findViewById(R.id.text1);

        TextView txt2 = (TextView) rootView.findViewById(R.id.text2);

        *//*txt1.startAnimation(fadeIn);
        txt2.startAnimation(fadeIn);

        fadeIn.setDuration(1400);
        fadeIn.setFillAfter(true);*//*

        *//*
         *  Add your view to the MyRelativeLayout you made, 'layout'
         *//*
        layout.addView(rootView);

        *//*
         *  Return the 'layout' instead of just the 'rootView'
         *//*
        return layout;
    }



    *//*
        *  Create a custom RelativeLayout and implement the inherited 'onDraw'
        *  method
        *//*
    class MyRelativeLayout extends RelativeLayout
    {
        public MyRelativeLayout(Context context)
        {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            *//*
             *  Draw your rectangle
             *//*
            Rect rectangle = new Rect(200, 56, 200, 112);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);

            canvas.drawRect(rectangle, paint);

            super.onDraw(canvas);
        }
    }*/

}