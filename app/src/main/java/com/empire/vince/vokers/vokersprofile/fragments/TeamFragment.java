package com.empire.vince.vokers.vokersprofile.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.empire.vince.vokers.vokersprofile.R;
import com.empire.vince.vokers.vokersprofile.adapter.TeamAdapter;
import com.empire.vince.vokers.vokersprofile.models.Member;
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
public class TeamFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private RecyclerView rvTeam;
    private List<Member> team;
    private static final int ROW_SPAN = 3;


    public TeamFragment() {

    }

    public static TeamFragment newInstance(String param) {
        TeamFragment fragment = new TeamFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_team, container, false);
        rvTeam = (RecyclerView) view.findViewById(R.id.rvTeam);
        rvTeam.setHasFixedSize(true);
        rvTeam.addItemDecoration(new SpacesItemDecoration(20,8));
        rvTeam.setItemAnimator(new DefaultItemAnimator());
        rvTeam.setLayoutManager(new GridLayoutManager(getActivity(),ROW_SPAN));
        rvTeam.setAdapter(new TeamAdapter(getActivity(),getTeamData()));
        return view;
    }

    private List<Member> getTeamData() {
        String jsonString = loadJSONFromAsset(getActivity(), "team");
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray teamArray = jsonObject.getJSONArray("team");
            team = new ArrayList<Member>();
            for(int i=0; i< teamArray.length(); i++) {
                team.add(new Member(teamArray.getJSONObject(i).getString("name"),
                        teamArray.getJSONObject(i).getString("designation"),
                        teamArray.getJSONObject(i).getString("image")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(team!=null && team.size()>0) {
            return team;
        } else {
            return null;
        }
    }

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int verticalSpace;
        private int horizontalSpace;

        public SpacesItemDecoration(int verticalSpace, int horizontalSpace) {
            this.verticalSpace = dpToPx(getActivity(),verticalSpace);
            this.horizontalSpace = dpToPx(getActivity(), horizontalSpace);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.top = verticalSpace;
            outRect.left = horizontalSpace;
            outRect.right = horizontalSpace;
            outRect.bottom = verticalSpace;

            // Add top margin only for the first row to avoid double space between items
            if(parent.getChildAdapterPosition(view)% ROW_SPAN <= ROW_SPAN) {
                outRect.top = verticalSpace * 2;
            } else if(parent.getAdapter().getItemCount()%
                    parent.getChildAdapterPosition(view)<ROW_SPAN) {
                outRect.bottom = verticalSpace *2;
            }
        }
    }

    @Override
    public void onResume() {
        AppInitializer.getInstance().trackScreenView("Team Screen");
        super.onResume();
    }
}
