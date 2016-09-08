package com.empire.vince.vokers.vokersprofile.fragments;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.empire.vince.vokers.vokersprofile.R;
import com.empire.vince.vokers.vokersprofile.tracker.AppInitializer;


import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.browseUrl;
import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.getUserEmail;
import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.isJellyBeanOrHigher;
import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.isLollipopOrHigher;
import static com.empire.vince.vokers.vokersprofile.util.ColorUtil.addAlpha;
import static com.empire.vince.vokers.vokersprofile.util.ColorUtil.getSocialButtonColorState;


/**
 * Created by VinceGee on 09/07/2016.
 */
public class SocialFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private RelativeLayout btnFB;
  //  private RelativeLayout btnLinkedIn;
    private RelativeLayout btnTwitter;
    private RelativeLayout btnYoutube;

    private String fbColor = "#3b5998";
 //   private String linkedInColor = "#077bb7";
    private String twitterColor = "#07b0ef";
    private String youtubeColor = "#ce2726";


    public SocialFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social, container, false);
        btnFB = (RelativeLayout) view.findViewById(R.id.btnFB);
        //btnLinkedIn = (RelativeLayout) view.findViewById(R.id.btnLinkedIn);
        btnTwitter = (RelativeLayout) view.findViewById(R.id.btnTwitter);
        btnYoutube = (RelativeLayout) view.findViewById(R.id.btnYoutube);

        btnFB.setOnClickListener(this);
//        btnLinkedIn.setOnClickListener(this);
        btnTwitter.setOnClickListener(this);
        btnYoutube.setOnClickListener(this);

        if (isJellyBeanOrHigher()) {
            btnFB.setBackground(getButtonShape(fbColor));
         //   btnLinkedIn.setBackground(getButtonShape(linkedInColor));
            btnTwitter.setBackground(getButtonShape(twitterColor));
            btnYoutube.setBackground(getButtonShape(youtubeColor));
        } else {
            btnFB.setBackgroundDrawable(getButtonShape(fbColor));
          //  btnLinkedIn.setBackgroundDrawable(getButtonShape(linkedInColor));
            btnTwitter.setBackgroundDrawable(getButtonShape(twitterColor));
            btnYoutube.setBackgroundDrawable(getButtonShape(youtubeColor));
        }

        return view;
    }

    private GradientDrawable getButtonShape(String color) {
        GradientDrawable shape;
        if (isLollipopOrHigher()) {
            shape = new GradientDrawable();
            shape.setCornerRadius(3);
            shape.setColor(getSocialButtonColorState(color));
        } else {
            shape = new GradientDrawable();
            shape.setColors(new int[]{Color.parseColor(color), Color.parseColor(addAlpha(color, 0.75f))});
            shape.setCornerRadius(3);
        }
        return shape;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFB:
                AppInitializer.getInstance().trackEvent(AppInitializer.EVENT_ACTION,
                        getUserEmail(getActivity())+" tapped on facebook button");
                browseUrl(getActivity(), getString(R.string.dev_fb));
                break;
            /*case R.id.btnLinkedIn:
                AppInitializer.getInstance().trackEvent(AppInitializer.EVENT_ACTION,
                        getUserEmail(getActivity())+" tapped on linked in button");
                browseUrl(getActivity(), getString(R.string.dev_linked_in));
                break;*/
            case R.id.btnTwitter:
                AppInitializer.getInstance().trackEvent(AppInitializer.EVENT_ACTION,
                        getUserEmail(getActivity())+" tapped on twitter button");
                browseUrl(getActivity(), getString(R.string.dev_twitter));
                break;
            case R.id.btnYoutube:
                AppInitializer.getInstance().trackEvent(AppInitializer.EVENT_ACTION,
                        getUserEmail(getActivity())+" tapped on youtube button");
                browseUrl(getActivity(), getString(R.string.dev_youtube));
                break;
        }
    }

    @Override
    public void onResume() {
        AppInitializer.getInstance().trackScreenView("Social Screen");
        super.onResume();
    }
}
