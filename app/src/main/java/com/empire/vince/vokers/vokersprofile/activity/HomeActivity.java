package com.empire.vince.vokers.vokersprofile.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.empire.vince.vokers.vokersprofile.R;
import com.empire.vince.vokers.vokersprofile.fragments.ClientsFragment;
import com.empire.vince.vokers.vokersprofile.fragments.FaqFragment;
import com.empire.vince.vokers.vokersprofile.fragments.HomeFragment;
import com.empire.vince.vokers.vokersprofile.fragments.MethodologyFragment;
import com.empire.vince.vokers.vokersprofile.fragments.OverViewFragment;
import com.empire.vince.vokers.vokersprofile.fragments.PortfolioFragment;
import com.empire.vince.vokers.vokersprofile.fragments.SocialFragment;
import com.empire.vince.vokers.vokersprofile.fragments.TeamFragment;
import com.empire.vince.vokers.vokersprofile.fragments.TestimonialFragment;
import com.empire.vince.vokers.vokersprofile.tracker.AppInitializer;
import com.empire.vince.vokers.vokersprofile.util.NavHandlerListener;
import static com.empire.vince.vokers.vokersprofile.util.ColorUtil.getNavIconColorState;
import static com.empire.vince.vokers.vokersprofile.util.ColorUtil.getNavTextColorState;
import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.browseUrl;
import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.getGooglePlayUrl;
import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.getUserEmail;
import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.mailTo;
import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.phoneCall;


/**
 * Created by VinceGee on 09/07/2016.
 */
public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        NavHandlerListener {

    private NavigationView navigationView = null;
    private NavigationMenuView navigationMenuView = null;
    private DrawerLayout drawer = null;
    private View headerView;
    private RelativeLayout navHeaderImgContainer;
    private RelativeLayout navActionPhone;
    private RelativeLayout navActionMail;
    private RelativeLayout navActionWeb;
    public NavHandlerListener navHandlerListener = null;
    private boolean isDoubleBackToExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
//        navigationView.getHeaderView(0).getLayoutParams().height = (int) (getWindowSize(this).y * 0.25);
        navigationView.setNavigationItemSelectedListener(this);
        if (navigationMenuView != null) {
            navigationMenuView.setVerticalScrollBarEnabled(false);
        }
        navigationView.setItemTextColor(getNavTextColorState());
        navigationView.setItemIconTintList(getNavIconColorState());
        headerView = navigationView.getHeaderView(0);
        navHeaderImgContainer = (RelativeLayout) headerView.findViewById(R.id.navHeaderImgContainer);
        navActionPhone = (RelativeLayout) headerView.findViewById(R.id.navActionPhone);
        navActionMail = (RelativeLayout) headerView.findViewById(R.id.navActionMail);
        navActionWeb = (RelativeLayout) headerView.findViewById(R.id.navActionWeb);
        navHeaderImgContainer.setOnClickListener(actionListener);
        navActionPhone.setOnClickListener(actionListener);
        navActionMail.setOnClickListener(actionListener);
        navActionWeb.setOnClickListener(actionListener);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_home));
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (navigationView.getMenu().findItem(itemId).isChecked()) {
            return true;
        }
        if (itemId != R.id.nav_rate) {
            if (itemId == R.id.nav_overview || itemId == R.id.nav_methodology) {
                hideActivityToolbar();
            } else {
                showActivityToolbar();
            }
        }

        if (itemId == R.id.nav_home) {
            showHomeScreen();
        } else if (itemId == R.id.nav_overview) {
            showOverViewScreen();
        } else if (itemId == R.id.nav_portfolio) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showPortfolioScreen();
                }
            }, 200);
        } else if (itemId == R.id.nav_team) {
            showTeamScreen();
        } else if (itemId == R.id.nav_client) {
            showClientScreen();
        } else if (itemId == R.id.nav_methodology) {
            showMethodologyScreen();
        } else if (itemId == R.id.nav_testimonial) {
            showTestimonialScreen();
        } else if (itemId == R.id.nav_social) {
            showSocialScreen();
        } else if (itemId == R.id.nav_faq) {
            showFaqScreen();
        } else if (itemId == R.id.nav_rate) {
            AppInitializer.getInstance().trackEvent(AppInitializer.EVENT_ACTION,
                    getUserEmail(HomeActivity.this)+" User tapped on '"+ getString(R.string.nav_text_rate)+"' option");
            gotoGooglePlay();
            return true;
        } else {

        }

        drawer.closeDrawer(GravityCompat.START);
        setTitle(navigationView.getMenu().findItem(itemId).getTitle());
        return true;
    }

    private void gotoGooglePlay() {
        browseUrl(HomeActivity.this, getGooglePlayUrl(HomeActivity.this));
        drawer.closeDrawer(GravityCompat.START);
    }

    private void showActivityToolbar() {
        if (!getSupportActionBar().isShowing()) {
            getSupportActionBar().show();
        }
    }

    private void hideActivityToolbar() {
        if (getSupportActionBar().isShowing()) {
            getSupportActionBar().hide();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isDoubleBackToExit) {
                super.onBackPressed();
                finish();
            }
            if (!isDoubleBackToExit) {
                Toast.makeText(this, getString(R.string.re_tap_text), Toast.LENGTH_SHORT).show();
            }
            this.isDoubleBackToExit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isDoubleBackToExit = false;
                }
            }, 2000);
        }
    }

    private void showHomeScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, HomeFragment.newInstance(" "))
                .commit();
    }

    private void showOverViewScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, OverViewFragment.newInstance(navHandlerListener))
                .commit();
    }

    private void showPortfolioScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, PortfolioFragment.newInstance(" "))
                .commit();
    }

    private void showTeamScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, TeamFragment.newInstance(" "))
                .commit();
    }

    private void showClientScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, ClientsFragment.newInstance(" "))
                .commit();
    }

    private void showMethodologyScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,
                        MethodologyFragment.newInstance(navHandlerListener))
                .commit();
    }

    private void showTestimonialScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new TestimonialFragment())
                .commit();
    }

    private void showSocialScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new SocialFragment())
                .commit();
    }

    private void showFaqScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new FaqFragment())
                .commit();
    }

    @Override
    protected void onResume() {
        AppInitializer.getInstance().trackScreenView("Home Activity");
        navHandlerListener = this;
        super.onResume();
    }

    @Override
    public void onNavOpenRequested() {
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    View.OnClickListener actionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.navHeaderImgContainer:
                    if (!navigationView.getMenu().findItem(R.id.nav_home).isChecked()) {
                        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_home));
                        navigationView.setCheckedItem(R.id.nav_home);
                    }
                    break;
                case R.id.navActionPhone:
                    AppInitializer.getInstance().trackEvent(AppInitializer.EVENT_ACTION,
                            getUserEmail(HomeActivity.this)+" tapped on phone call option");
                    phoneCall(HomeActivity.this, getString(R.string.dev_phone));
                    break;
                case R.id.navActionMail:
                    AppInitializer.getInstance().trackEvent(AppInitializer.EVENT_ACTION,
                            getUserEmail(HomeActivity.this)+" tapped on mail option");
                    mailTo(HomeActivity.this, getString(R.string.dev_mail));
                    break;
                case R.id.navActionWeb:
                    AppInitializer.getInstance().trackEvent(AppInitializer.EVENT_ACTION,
                            getUserEmail(HomeActivity.this)+" tapped on web option");
                    browseUrl(HomeActivity.this, getString(R.string.dev_web_page));
                    break;
            }

            drawer.closeDrawer(GravityCompat.START);
        }
    };

}
