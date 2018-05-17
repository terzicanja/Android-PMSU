package com.example.w10.pmsu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.w10.pmsu.adapters.DrawerListAdapter;
import com.example.w10.pmsu.fragments.CommentsFragment;
import com.example.w10.pmsu.fragments.ReadPostFragment;

import java.util.ArrayList;
import java.util.List;

import model.NavItem;

public class ReadPostActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private AlertDialog dialog;
    private SharedPreferences sharedPreferences;

    private String synctime;
    private boolean allowSync;
    private String lookupRadius;
    private boolean allowReviewNotif;
    private boolean allowCommentedNotif;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_post);

        prepareMenu(mNavItems);

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.navList);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setOnItemClickListener(new ReadPostActivity.DrawerItemClickListener());
        mDrawerList.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.read_post_toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setIcon(R.drawable.ic_menu);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setHomeButtonEnabled(true);
        }

        if (savedInstanceState == null) {
            selectItemFromDrawer(0);
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);




        Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        ImageView im = (ImageView) findViewById(R.id.image);
        im.setImageBitmap(b);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ReadPostFragment(), "NEWS");
        adapter.addFragment(new CommentsFragment(), "COMMENTS");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void consultPreferences(){
        synctime = sharedPreferences.getString(getString(R.string.pref_sync_list), "1");//1min
        allowSync = sharedPreferences.getBoolean(getString(R.string.pref_sync), false);

        lookupRadius = sharedPreferences.getString(getString(R.string.pref_radius), "1");//1km

        allowCommentedNotif = sharedPreferences.getBoolean(getString(R.string.notif_on_my_comment_key), false);
        allowReviewNotif = sharedPreferences.getBoolean(getString(R.string.notif_on_my_review_key), false);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void prepareMenu(ArrayList<NavItem> mNavItems ){
        mNavItems.add(new NavItem(getString(R.string.home), getString(R.string.home_long), R.drawable.ic_menu));
        mNavItems.add(new NavItem(getString(R.string.create), getString(R.string.create_long), R.drawable.ic_menu));
        mNavItems.add(new NavItem(getString(R.string.settings), getString(R.string.settings_long), R.drawable.ic_settings_black_24dp));
        mNavItems.add(new NavItem(getString(R.string.about), getString(R.string.about_long), R.drawable.ic_menu));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_read, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemFromDrawer(position);
        }
    }

    private void selectItemFromDrawer(int position) {
        if(position == 0){
//            FragmentTransition.to(MyFragment.newInstance(), this, false);
//            Intent preference = new Intent(this, PostsActivity.class);
//            startActivity(preference);
        }else if(position == 1){
//            Intent preference = new Intent(this, CreatePostActivity.class);
            Intent preference = new Intent(this, PostsActivity.class);
            startActivity(preference);
        }else if(position == 2){
//            Intent preference = new Intent(this, SettingsActivity.class);
            Intent preference = new Intent(this, CreatePostActivity.class);
            startActivity(preference);
        }else if(position == 3){
            Intent preference = new Intent(this, SettingsActivity.class);
            startActivity(preference);
        }else if(position == 4){
            //..
        }else{
            Log.e("DRAWER", "Nesto van opsega!");
        }

        mDrawerList.setItemChecked(position, true);
//        if(position != 5) // za sve osim za sync
//        {
//            setTitle(mNavItems.get(position).getmTitle());
//        }
        setTitle(mNavItems.get(position).getmTitle());
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.


        //nzm sta radi ova linija ispod
//        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        RadioButton rb = (RadioButton) findViewById(view.getId());
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.btn_like:
                if (checked)
                    Toast.makeText(this, "nesto se desilo",Toast.LENGTH_SHORT).show();
                // Pirates are the best
                break;
            case R.id.btn_dislike:
                if (checked)
                    Toast.makeText(this, "nestoooooooooo",Toast.LENGTH_SHORT).show();
                // Ninjas rule
                break;
        }
    }


}
