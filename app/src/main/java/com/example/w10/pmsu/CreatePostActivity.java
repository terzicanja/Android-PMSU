package com.example.w10.pmsu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import model.NavItem;
import model.Post;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.w10.pmsu.adapters.DrawerListAdapter;
import com.example.w10.pmsu.activities.ReviewerPreferenceActivity;
import com.example.w10.pmsu.dialogs.LocationDialog;
import com.example.w10.pmsu.fragments.MyFragment;
import com.example.w10.pmsu.service.PostService;
import com.example.w10.pmsu.service.ServiceUtils;
import com.example.w10.pmsu.service.TagService;
import com.example.w10.pmsu.service.UserService;
import com.example.w10.pmsu.tools.FragmentTransition;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreatePostActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private AlertDialog dialog;

    private EditText title_text;
    private EditText write_post;

    private PostService postService;
    private UserService userService;
    private TagService tagService;

    public static User user;

    private String synctime;
    private boolean allowSync;
    private String lookupRadius;

    private boolean allowReviewNotif;
    private boolean allowCommentedNotif;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        prepareMenu(mNavItems);

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.navList);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);

        // set a custom shadow that overlays the main content when the drawer opens
//        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setAdapter(adapter);

        // enable ActionBar app icon to behave as action to toggle nav drawer
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();


//        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//            getSupportActionBar().setDisplayShowHomeEnabled(false);
//        }

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setIcon(R.drawable.ic_menu);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setHomeButtonEnabled(true);
        }

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String ulogovani = sharedPreferences.getString("User", "");
        Toast.makeText(this, ulogovani,Toast.LENGTH_SHORT).show();

//        mDrawerToggle = new ActionBarDrawerToggle(
//                this,                  /* host Activity */
//                mDrawerLayout,         /* DrawerLayout object */
//                toolbar,  /* nav drawer image to replace 'Up' caret */
//                R.string.drawer_open,  /* "open drawer" description for accessibility */
//                R.string.drawer_close  /* "close drawer" description for accessibility */
//        ) {
//            public void onDrawerClosed(View view) {
////                getActionBar().setTitle(mTitle);
//                getSupportActionBar().setTitle(mTitle);
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//
//            public void onDrawerOpened(View drawerView) {
////                getActionBar().setTitle(mDrawerTitle);
//                getSupportActionBar().setTitle("iReviewer");
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//        };
//        mDrawerLayout.setDrawerListener(mDrawerToggle);

        title_text = findViewById(R.id.title_text);
        write_post = findViewById(R.id.write_post);

        postService = ServiceUtils.postService;
        userService = ServiceUtils.userService;
        tagService = ServiceUtils.tagService;


        Call<User> call = userService.getUserByUsername(ulogovani);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                Toast.makeText(getApplicationContext(), "Pronadjen je user sa imenom " + user.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        if (savedInstanceState == null) {
            selectItemFromDrawer(0);
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void consultPreferences(){
        synctime = sharedPreferences.getString(getString(R.string.pref_sync_list), "1");//1min
        allowSync = sharedPreferences.getBoolean(getString(R.string.pref_sync), false);

        lookupRadius = sharedPreferences.getString(getString(R.string.pref_radius), "1");//1km

        allowCommentedNotif = sharedPreferences.getBoolean(getString(R.string.notif_on_my_comment_key), false);
        allowReviewNotif = sharedPreferences.getBoolean(getString(R.string.notif_on_my_review_key), false);

    }

    private void showLocatonDialog(){
        if(dialog == null){
            dialog = new LocationDialog(CreatePostActivity.this).prepareDialog();
        }else{
            if(dialog.isShowing()){
                dialog.dismiss();
            }
        }

        dialog.show();
    }

    public void confirmPost(){
        Post post = new Post();
//        User user = new User();

        String title = title_text.getText().toString();
        String description = write_post.getText().toString();
        post.setTitle(title);
        post.setDescription(description);
        post.setAuthor(user);
        post.setLikes(0);
        post.setDislikes(0);
        Date date = Calendar.getInstance().getTime();
        post.setDate(date);

//        Toast.makeText(getApplicationContext(), "post created",Toast.LENGTH_SHORT).show();

        Call<Post> call = postService.savePost(post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Toast.makeText(getApplicationContext(), "Post created",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), PostsActivity.class);
                startActivity(i);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        showLocatonDialog();

        consultPreferences();
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
        inflater.inflate(R.menu.activity_create, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // The action bar home/up action should open or close the drawer.
//        // ActionBarDrawerToggle will take care of this.
////        if (mDrawerToggle.onOptionsItemSelected(item)) {
////            return true;
////        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.action_confirm:
                confirmPost();
                Intent in = new Intent(this, PostsActivity.class);
                startActivity(in);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void btnCreatePost(View view) {
        confirmPost();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemFromDrawer(position);
        }
    }

//    private void selectItemFromDrawer(int position) {
//        FragmentTransition.to(MyFragment.newInstance(), this, false);
//
//        mDrawerList.setItemChecked(position, true);
//        if(position != 5) // za sve osim za sync
//        {
//            setTitle(mNavItems.get(position).getmTitle());
//            /*getSupportActionBar().setTitle(mNavItems.get(position).getmTitle());
//            mTitle = ;*/
//        }
//        mDrawerLayout.closeDrawer(mDrawerPane);
//    }

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
//        if(position != 5)
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

        //nzm sta radi ova linija ispod
//        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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

//    public void btnStartPostsActivity(View view){
//        Intent i = new Intent(this, PostsActivity.class);
//        startActivity(i);
//    }
}
