package com.empire.vince.vokers.vokersprofile.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.empire.vince.vokers.vokersprofile.R;
import com.empire.vince.vokers.vokersprofile.adapter.PortfolioAdapter;
import com.empire.vince.vokers.vokersprofile.fab.FloatingActionButton;
import com.empire.vince.vokers.vokersprofile.fab.FloatingActionMenu;
import com.empire.vince.vokers.vokersprofile.models.Project;
import com.empire.vince.vokers.vokersprofile.tracker.AppInitializer;
import com.empire.vince.vokers.vokersprofile.util.AITSRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.getDrawableIdFromFileName;
import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.loadJSONFromAsset;
import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.mailTo;

/**
 * Created by VinceGee on 09/07/2016.
 */
public class PortfolioFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final int ANIM_DURATION_FAB = 400;
    private AITSRecyclerView portfolioRecyclerView;
    private RelativeLayout noContentLayout;
    private List<Project> projectList;
    private List<String> platformList;
    private List<Project> filteredProjectList = new ArrayList<Project>();
    private PortfolioAdapter portfolioAdapter = null;
    private FloatingActionMenu fabMenu;

//    private List<FloatingActionButton> fabButtons = new ArrayList<>();


    public PortfolioFragment() {

    }

    public static PortfolioFragment newInstance(String param) {
        PortfolioFragment fragment = new PortfolioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setUpBackPressed();
        setHasOptionsMenu(true);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);
        portfolioRecyclerView = (AITSRecyclerView) view.findViewById(R.id.portfolioRecyclerView);
        noContentLayout = (RelativeLayout) view.findViewById(R.id.noContentLayout);
        portfolioRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setEmptyView(view);
        fabMenu = (FloatingActionMenu) view.findViewById(R.id.fabMenu);
        fabMenu.setDimColor(ContextCompat.getColor(getActivity(), R.color.fab_background_color));
        fabMenu.setMenuButtonColorNormal(ContextCompat.getColor(getActivity(), R.color.fab_color_normal));
        fabMenu.setMenuButtonColorPressed(ContextCompat.getColor(getActivity(), R.color.fab_color_pressed));
        fabMenu.setClosedOnTouchOutside(true);
//        fabMenu.setMenuButtonColorRipple(R.color.fab_color_normal);
        setFabMenus();
        return view;
    }

    private void setEmptyView(View view) {
        RelativeLayout noContentLayout = (RelativeLayout) view.findViewById(R.id.noContentLayout);
        ((Button) noContentLayout.findViewById(R.id.btnGetStarted)).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mailTo(getActivity(), getString(R.string.dev_mail));
                    }
                });
        portfolioRecyclerView.setEmptyView(noContentLayout);
    }


    private void setFabMenus() {
        String jsonString = loadJSONFromAsset(getActivity(), "filter");
        try {
            JSONObject jsonObject = new JSONObject(jsonString).getJSONObject("filter");
            int menuColor = Color.parseColor(jsonObject.getString("menu_color"));
            int menuPressedColor = Color.parseColor(jsonObject.getString("menu_pressed_color"));
            JSONArray jsonArray = jsonObject.getJSONArray("options");
            for(int i=0;i <jsonArray.length();i++) {
                final FloatingActionButton fabButton = new FloatingActionButton(getActivity());
                fabButton.setButtonSize(FloatingActionButton.SIZE_MINI);
                fabButton.setLabelText(jsonArray.getJSONObject(i).getString("title"));
                fabButton.setImageResource(getDrawableIdFromFileName
                        (getActivity(),jsonArray.getJSONObject(i).getString("image")));
                fabButton.setColorNormal(menuColor);
                fabButton.setColorPressed(menuPressedColor);
//            fabButton.setColorRipple(R.color.fab_color_normal);
                fabButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setFilter(fabButton.getLabelText());
                        fabMenu.toggle(true);
                    }
                });
                fabMenu.addMenuButton(fabButton);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setFilter(String labelText) {
        if (labelText.equalsIgnoreCase("All")) {
            portfolioAdapter.setFilterList(projectList);
            return;
        }
        filteredProjectList.removeAll(filteredProjectList);
        for (int i = 0; i < projectList.size(); i++) {
            List<String> platformList = projectList.get(i).getPlatformList();
            for (int j = 0; j < platformList.size(); j++) {
                if (labelText.equalsIgnoreCase(platformList.get(j))) {
                    filteredProjectList.add(projectList.get(i));
                    break;
                }
            }
        }
        portfolioAdapter.setFilterList(filteredProjectList);
        if (filteredProjectList.size() > 0) {
            portfolioRecyclerView.scrollToPosition(0);
        }
    }


    private void setUpBackPressed() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (fabMenu.isOpened()) {
                            fabMenu.toggle(true);
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
                return false;
            }
        });
    }


    @Override
    public void onResume() {
        AppInitializer.getInstance().trackScreenView("Portfolio Screen");
        getPortfolioData();
        portfolioAdapter = new PortfolioAdapter(getActivity(), projectList);
        portfolioRecyclerView.setAdapter(portfolioAdapter);
//        portfolioRecyclerView.setAdapter(new ScaleInAnimationAdapter(portfolioAdapter));
        if (portfolioRecyclerView.getAdapter().getItemCount() == 0) {
            noContentLayout.setVisibility(View.VISIBLE);
            fabMenu.setVisibility(View.GONE);
        }
        super.onResume();
    }

    private void getPortfolioData() {
        String jsonString = loadJSONFromAsset(getActivity(), "portfolio");
        try {
            JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("projects");
            projectList = new ArrayList<Project>();
            for (int i = 0; i < jsonArray.length(); i++) {
                Project project = new Project();
                String name = jsonArray.getJSONObject(i).getString("name");
                String image = jsonArray.getJSONObject(i).getString("image");
                JSONArray platforms = jsonArray.getJSONObject(i).getJSONArray("platforms");
                platformList = new ArrayList<String>();
                for (int j = 0; j < platforms.length(); j++) {
                    platformList.add(platforms.getString(j));
                }
                project.setTitle(name);
                project.setImage(getDrawableIdFromFileName(getActivity(), image));
                project.setPlatformList(platformList);
                project.setIsLiked(0);
                projectList.add(project);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}