package com.empire.vince.vokers.vokersprofile.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.empire.vince.vokers.vokersprofile.R;
import com.empire.vince.vokers.vokersprofile.adapter.MethodologyAdapter;
import com.empire.vince.vokers.vokersprofile.models.Methodology;
import com.empire.vince.vokers.vokersprofile.tracker.AppInitializer;
import com.empire.vince.vokers.vokersprofile.util.NavHandlerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.getDrawableIdFromFileName;
import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.loadJSONFromAsset;
import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.dpToPx;
import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.getWindowSize;

/**
 * Created by VinceGee on 09/07/2016.
 */
public class MethodologyFragment extends Fragment {

    private static NavHandlerListener navHandlerListener = null;
    private RecyclerView methodologyRv = null;
    private AppBarLayout appBarLayout = null;
    private CollapsingToolbarLayout collapsingToolbar = null;
    private String companyIcon = null;
    private String parallaxBackgroundImg = null;
    private RelativeLayout parallaxView;
    private ImageView companyLogoImgView = null;
    private List<Methodology> methodologyList = null;


    public MethodologyFragment() {

    }

    public static MethodologyFragment newInstance(NavHandlerListener listener) {
        MethodologyFragment fragment = new MethodologyFragment();
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
        View view =  inflater.inflate(R.layout.fragment_methodology, container, false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbarMethodology);
        toolbar.setNavigationIcon(R.drawable.ic_navigation);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navHandlerListener.onNavOpenRequested();
            }
        });

        appBarLayout = (AppBarLayout)view.findViewById(R.id.appbar_container);
        appBarLayout.getLayoutParams().height = (int) (getWindowSize(getActivity()).y * 0.40);
        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_container);
        collapsingToolbar.setTitle(getString(R.string.nav_text_methodology));
        collapsingToolbar.setTitleEnabled(true);
        parallaxView = (RelativeLayout)view.findViewById(R.id.parallaxView);
        companyLogoImgView = (ImageView)view.findViewById(R.id.companyLogoImgView);
        companyLogoImgView.getLayoutParams().height = (int) (getWindowSize(getActivity()).y * 0.25);
        methodologyRv = ((RecyclerView)view.findViewById(R.id.methodologyRv));
        methodologyRv.addItemDecoration(new SpacesItemDecoration(20));
        methodologyRv.setHasFixedSize(true);
        methodologyRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private void getMethodologyData() {
        String jsonString = loadJSONFromAsset(getActivity(), "methodology");
        try {
            JSONObject jsonObject = new JSONObject(jsonString).getJSONObject("methodology");
            companyIcon = jsonObject.getString("company_icon");
            parallaxBackgroundImg = jsonObject.getString("background_image");
            JSONArray methodologyArray = jsonObject.getJSONArray("methodology_info");
            methodologyList = new ArrayList<Methodology>();
            for(int i=0; i< methodologyArray.length(); i++) {
                methodologyList.add(new Methodology(methodologyArray.getJSONObject(i).getString("image"),
                        methodologyArray.getJSONObject(i).getString("heading"),
                        methodologyArray.getJSONObject(i).getString("description")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int verticalSpace;

        public SpacesItemDecoration(int verticalSpace) {
            this.verticalSpace =  dpToPx(getActivity(),verticalSpace);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
//            outRect.left = 45;
            if(parent.getChildAdapterPosition(view) == 0) {
                outRect.top = verticalSpace ;
                outRect.bottom = verticalSpace;
            } else if(parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount()-1) {
                outRect.bottom = verticalSpace;
            } else {
                outRect.bottom = verticalSpace;
            }
        }
    }

    @Override
    public void onResume() {
        AppInitializer.getInstance().trackScreenView("Methodology Screen");
        getMethodologyData();
        parallaxView.setBackgroundResource(
                getDrawableIdFromFileName(getActivity(),parallaxBackgroundImg));
        companyLogoImgView.setImageResource(
                getDrawableIdFromFileName(getActivity(),companyIcon));
        methodologyRv.setAdapter(new MethodologyAdapter(getActivity(),methodologyList));
        super.onResume();
    }
}
