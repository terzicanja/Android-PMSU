package com.example.w10.pmsu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.w10.pmsu.activities.ReviewerPreferenceActivity;
import com.example.w10.pmsu.adapters.DrawerListAdapter;
import com.example.w10.pmsu.adapters.PostAdapter;
import com.example.w10.pmsu.fragments.MyFragment;
import com.example.w10.pmsu.service.PostService;
import com.example.w10.pmsu.service.ServiceUtils;
import com.example.w10.pmsu.tools.FragmentTransition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import model.NavItem;
import model.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private AlertDialog dialog;
    private SharedPreferences sharedPreferences;
    private List<Post> posts;
    private Post post = new Post();
    private Post post1 = new Post();
    private Post post3 = new Post();
    private PostAdapter postAdapter;

    private String synctime;
    private boolean allowSync;
    private String lookupRadius;
    private boolean allowReviewNotif;
    private boolean allowCommentedNotif;
    private PostService postService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        prepareMenu(mNavItems);

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.navList);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setOnItemClickListener(new PostsActivity.DrawerItemClickListener());
        mDrawerList.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.post_toolbar);
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

        if (savedInstanceState == null) {
            selectItemFromDrawer(0);
        }

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        post.setDate(new Date(2018-1900, 7, 1, 10, 10));
        post1.setDate(new Date(2018-1900, 1, 1, 10, 10));
        post.setTitle("Eight things to expect at Google I/O 2018");
        post.setDescription("Google’s annual developer conference will be a jam-packed affair " +
                "about the future of Android, AI, and the smart home");
        post1.setTitle("Naslov2");
//        Post post3 = new Post();
        post3.setDate(new Date(2018-1900, 2, 1, 10, 10));
        post3.setTitle("Naslov3");
        post.setLikes(3);
        post1.setLikes(17);
        post3.setLikes(22);
        Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.img1);
        Bitmap bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_name);
        post.setPhoto(bmp1);
        post1.setPhoto(bmp2);


        post.getDate();
        post1.getDate();
        post.getTitle();
        post1.getTitle();
        post3.getDate();
        post3.getTitle();
//        post.getLikes();
//        post1.getLikes();
//        post3.getLikes();

//        posts.add(post);
//        posts.add(post1);
//        posts.add(post3);

        postAdapter = new PostAdapter(this, posts);
        final ListView listView = findViewById(R.id.post_list);
        postService = ServiceUtils.postService;

        Call call = postService.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                posts = response.body();
                postAdapter = new PostAdapter(getApplicationContext(), posts);
                listView.setAdapter(postAdapter);

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();

            }
        });


//        listView.setAdapter(postAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent startReadPost = new Intent(PostsActivity.this, ReadPostActivity.class);

                Pair<View, String> p1 = Pair.create(findViewById(R.id.image_view), "img_transition");
                Pair<View, String> p2 = Pair.create(findViewById(R.id.title_view), "naslov_transition");


                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(PostsActivity.this, p1, p2 );

                startActivity(startReadPost, optionsCompat.toBundle());
            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void consultPreferences(){
        String sortNews = sharedPreferences.getString("pref_sortNews", "");

//        if (sortNews.equals("oldest")){
//            newsByDate();
//        }
//
//        if (sortNews.equals("newest")){
//            newsByDateOld();
//        }
//
//        if (sortNews.equals("rating")){
//            newsByRating();
//        }


        synctime = sharedPreferences.getString(getString(R.string.pref_sync_list), "1");//1min
        allowSync = sharedPreferences.getBoolean(getString(R.string.pref_sync), false);

        lookupRadius = sharedPreferences.getString(getString(R.string.pref_radius), "1");//1km

        allowCommentedNotif = sharedPreferences.getBoolean(getString(R.string.notif_on_my_comment_key), false);
        allowReviewNotif = sharedPreferences.getBoolean(getString(R.string.notif_on_my_review_key), false);


        postAdapter.notifyDataSetChanged();

    }

    public void newsByDate(){
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post, Post t1) {
                return post.getDate().compareTo(t1.getDate());
            }
        });
    }

    public void newsByDateOld(){
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post, Post t1) {
                return t1.getDate().compareTo(post.getDate());
            }
        });
    }

    public void newsByRating(){
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post, Post t1) {
                return Integer.valueOf(t1.getLikes()).compareTo(post.getLikes());
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

//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        String syncConnPref = sharedPref.getString("pref_sortNews", "");
//
//        Toast.makeText(this, syncConnPref,Toast.LENGTH_SHORT).show();

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
        inflater.inflate(R.menu.activity_itemdetail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.action_add:
                Intent in = new Intent(this, CreatePostActivity.class);
                startActivity(in);
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


    public void btnStartCreatePostActivity(View view){
        Intent i = new Intent(this, CreatePostActivity.class);
        startActivity(i);
    }

    public void btnStartReadPostActivity(View view){
        Intent i = new Intent(this, ReadPostActivity.class);
        startActivity(i);
    }

    public void btnStartSettingsActivity(View view){
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    public void btnLogout(View view){
        sharedPreferences = getSharedPreferences("Login", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}
