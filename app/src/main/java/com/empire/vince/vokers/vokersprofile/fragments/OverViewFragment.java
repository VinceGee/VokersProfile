package com.empire.vince.vokers.vokersprofile.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.empire.vince.vokers.vokersprofile.R;
import com.empire.vince.vokers.vokersprofile.adapter.OverviewAdapter;
import com.empire.vince.vokers.vokersprofile.models.BasicInfo;
import com.empire.vince.vokers.vokersprofile.models.CounterInfo;
import com.empire.vince.vokers.vokersprofile.tracker.AppInitializer;
import com.empire.vince.vokers.vokersprofile.util.NavHandlerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.getDrawableIdFromFileName;
import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.loadJSONFromAsset;
import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.dpToPx;
import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.getWindowSize;

/**
 * Created by VinceGee on 09/07/2016.
 */
public class OverViewFragment extends Fragment {

    private static NavHandlerListener navHandlerListener = null;
    private RecyclerView overviewRv = null;
    private AppBarLayout appBarLayout = null;
    private CollapsingToolbarLayout collapsingToolbar = null;
    private String companyIcon = null;
    private String parallaxBackgroundImg = null;
    private String companyName = null;
    private String companyTag = null;
    private List<CounterInfo> counterInfoList = null;
    private List<BasicInfo> basicInfoList = null;
    private RelativeLayout parallaxView;
    private CircleImageView companyLogoImgView;
    private TextView companyNameTv;
    private TextView companyTagTv;
    private TextView projectCompleteTv;
    private TextView projectQueueTv;
    private TextView projectWaitingTv;


    public OverViewFragment() {

    }

    public static OverViewFragment newInstance(NavHandlerListener listener) {
        OverViewFragment fragment = new OverViewFragment();
        navHandlerListener = listener;
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_over_view, container, false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbarOverview);
        toolbar.setNavigationIcon(R.drawable.ic_navigation);
        getOverViewData();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navHandlerListener.onNavOpenRequested();
            }
        });

        appBarLayout = (AppBarLayout)view.findViewById(R.id.appbar_container);
        appBarLayout.getLayoutParams().height = (int) (getWindowSize(getActivity()).y * 0.60);
        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_container);
        collapsingToolbar.setTitle(getString(R.string.company_name));
        collapsingToolbar.setTitleEnabled(true);
        ((LinearLayout)view.findViewById(R.id.counterView)).
                getLayoutParams().height = (int) (getWindowSize(getActivity()).y * 0.10);

        parallaxView = ((RelativeLayout)view.findViewById(R.id.parallaxView));
        companyLogoImgView = ((CircleImageView) view.findViewById(R.id.companyLogoImgView));
        companyNameTv =  ((TextView)view.findViewById(R.id.companyNameTv));
        companyTagTv =  ((TextView)view.findViewById(R.id.companyTagTv));
        projectCompleteTv =  ((TextView)view.findViewById(R.id.projectCompleteTv));
        projectQueueTv =  ((TextView)view.findViewById(R.id.projectQueueTv));
        projectWaitingTv =  ((TextView)view.findViewById(R.id.projectWaitingTv));

        overviewRv = ((RecyclerView)view.findViewById(R.id.overviewRv));
        overviewRv.addItemDecoration(new SpacesItemDecoration(20));
        overviewRv.setHasFixedSize(true);
        return view;
    }

    private void getOverViewData() {
        String jsonString = loadJSONFromAsset(getActivity(), "overview");
        try {
            JSONObject jsonObject = new JSONObject(jsonString).getJSONObject("overview");
            companyIcon = jsonObject.getString("company_icon");
            parallaxBackgroundImg = jsonObject.getString("background_image");
            companyName = jsonObject.getString("company_name");
            companyTag =  jsonObject.getString("company_tag");
            JSONArray counterInfoArray = jsonObject.getJSONArray("counter_info");
            counterInfoList = new ArrayList<>();
            for(int i=0; i<counterInfoArray.length();i++) {
                counterInfoList.add(new CounterInfo(counterInfoArray.getJSONObject(i).getString("number"),
                        counterInfoArray.getJSONObject(i).getString("text")));
            }
            JSONArray basicInfoArray = jsonObject.getJSONArray("basic_info");
            basicInfoList = new ArrayList<BasicInfo>();
            for(int i=0; i<basicInfoArray.length(); i++) {
                basicInfoList.add(new BasicInfo(basicInfoArray.getJSONObject(i).getString("image"),
                        basicInfoArray.getJSONObject(i).getString("title"),
                        basicInfoArray.getJSONObject(i).getString("body")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        AppInitializer.getInstance().trackScreenView("Overview Screen");
        getOverViewData();
        parallaxView.setBackgroundResource(
                getDrawableIdFromFileName(getActivity(),parallaxBackgroundImg));
        companyLogoImgView.setImageResource(
                getDrawableIdFromFileName(getActivity(),companyIcon));
        companyNameTv.setText(companyName);
        companyTagTv.setText(companyTag);
        projectCompleteTv.setText(getStyleText(counterInfoList.get(0)));
        projectQueueTv.setText(getStyleText(counterInfoList.get(1)));
        projectWaitingTv.setText(getStyleText(counterInfoList.get(2)));
        overviewRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        overviewRv.setAdapter(new OverviewAdapter(getActivity(),basicInfoList));
        super.onResume();

    }

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int verticalSpace;

        public SpacesItemDecoration(int verticalSpace) {
            this.verticalSpace = dpToPx(getActivity(),verticalSpace);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
//            outRect.left = 45;
            if(parent.getChildAdapterPosition(view) == 0) {
                outRect.top = verticalSpace;
                outRect.bottom = verticalSpace;
            } else if(parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount()-1) {
                outRect.bottom = verticalSpace;
            } else {
                outRect.bottom = verticalSpace;
            }
        }
    }

    private Spanned getStyleText(CounterInfo info) {
        return Html.fromHtml("<b>"+info.getNumber()+"</b><br><small>"+info.getText()+"</small>");
    }





}