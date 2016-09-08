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
import com.empire.vince.vokers.vokersprofile.adapter.ClientsAdapter;
import com.empire.vince.vokers.vokersprofile.models.Project;
import com.empire.vince.vokers.vokersprofile.models.Tag;
import com.empire.vince.vokers.vokersprofile.tracker.AppInitializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.getDrawableIdFromFileName;
import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.loadJSONFromAsset;
import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.dpToPx;

/**
 * Created by VinceGee on 09/07/2016.
 */
public class ClientsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private RecyclerView rvClients;
    private List<Project> projectList;
    private List<Tag> tagList;


    public ClientsFragment() {

    }

    public static ClientsFragment newInstance(String param) {
        ClientsFragment fragment = new ClientsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clients, container, false);
        rvClients = (RecyclerView) view.findViewById(R.id.rvClients);
        rvClients.setHasFixedSize(true);
        rvClients.addItemDecoration(new SpacesItemDecoration(10,10,5));
        rvClients.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int leftSpaceing;
        private int rightSpaceing;
        private int bottomSpacing;

        public SpacesItemDecoration(int leftSpaceing, int rightSpaceing, int bottomSpacing) {
            this.leftSpaceing = dpToPx(getActivity(),leftSpaceing);
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
        AppInitializer.getInstance().trackScreenView("Clients Screen");
        getClientData();
        rvClients.setAdapter(new ClientsAdapter(getActivity(),projectList));
        super.onResume();
    }

    private void getClientData() {
        String jsonString = loadJSONFromAsset(getActivity(), "client");
        try {
            JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("clients");
            projectList = new ArrayList<Project>();
            for(int i=0; i<jsonArray.length();i++) {
                Project project = new Project();
                String clientName = jsonArray.getJSONObject(i).getString("client_name");
                String delegateName = jsonArray.getJSONObject(i).getString("delegate_name");
                String countryName =  jsonArray.getJSONObject(i).getString("country");
                String image = jsonArray.getJSONObject(i).getString("image");
                JSONArray infoArray= jsonArray.getJSONObject(i).getJSONArray("project_info");
                tagList = new ArrayList<Tag>();
                for(int j=0; j< infoArray.length(); j++) {
                    Tag tag = new Tag();
                    tag.setPlatform(infoArray.getJSONObject(j).getString("platform"));
                    tag.setUrl(infoArray.getJSONObject(j).getString("url"));
                    tagList.add(tag);
                }
                project.setClientName(clientName);
                project.setDelegateName(delegateName);
                project.setCountryName(countryName);
                project.setImage(getDrawableIdFromFileName(getActivity(),image));
                project.setTagList(tagList);
                project.setIsExpanded(0);
                projectList.add(project);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
