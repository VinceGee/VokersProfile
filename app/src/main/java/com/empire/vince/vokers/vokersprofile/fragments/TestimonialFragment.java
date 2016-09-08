package com.empire.vince.vokers.vokersprofile.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.empire.vince.vokers.vokersprofile.R;
import com.empire.vince.vokers.vokersprofile.adapter.TestimonialAdapter;
import com.empire.vince.vokers.vokersprofile.models.Project;
import com.empire.vince.vokers.vokersprofile.tracker.AppInitializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.loadJSONFromAsset;
import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.dpToPx;


/**
 * Created by VinceGee on 09/07/2016.
 */
public class TestimonialFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private RecyclerView rvTestimonial;
    private static final int ROW_SPAN = 3;
    private List<Project> projectList;

    public TestimonialFragment() {

    }

    public static TestimonialFragment newInstance(String param) {
        TestimonialFragment fragment = new TestimonialFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_testimonial, container, false);
        rvTestimonial = (RecyclerView) view.findViewById(R.id.rvTestimonial);
        rvTestimonial.setHasFixedSize(true);
        rvTestimonial.addItemDecoration(new SpacesItemDecoration(10,10,5));
        rvTestimonial.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int leftSpaceing;
        private int rightSpaceing;
        private int bottomSpacing;

        public SpacesItemDecoration(int leftSpaceing, int rightSpaceing, int bottomSpacing) {
            this.leftSpaceing =  dpToPx(getActivity(),leftSpaceing);
            this.rightSpaceing = dpToPx(getActivity(),rightSpaceing);
            this.bottomSpacing = dpToPx(getActivity(),bottomSpacing);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = leftSpaceing;
            outRect.right = rightSpaceing;
            if(parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount()-1) {
                outRect.bottom = bottomSpacing;
            }
        }
    }

    @Override
    public void onResume() {
        AppInitializer.getInstance().trackScreenView("Testimonial Screen");
        getTestimonialData();
        rvTestimonial.setAdapter(new TestimonialAdapter(getActivity(),projectList));
        super.onResume();
    }

    private void getTestimonialData() {
        String jsonString = loadJSONFromAsset(getActivity(), "testimonial");
        try {
            JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("testimonials");
            projectList = new ArrayList<Project>();
            for(int i=0; i<jsonArray.length();i++) {
                Project project = new Project();
                String delegateName = jsonArray.getJSONObject(i).getString("delegate_name");
                String delegateDesignation = jsonArray.getJSONObject(i).
                        getString("delegate_designation");
                String delegateImg = jsonArray.getJSONObject(i).
                        getString("delegate_image");
                String referenceUrl = jsonArray.getJSONObject(i).
                        getString("ref_url");
                String comment = jsonArray.getJSONObject(i).
                        getString("comment");
                project.setDelegateName(delegateName);
                project.setDelegateDesignation(delegateDesignation);
                project.setDelegateImg(delegateImg);
                project.setReferenceUrl(referenceUrl);
                project.setComment(comment);
                projectList.add(project);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
