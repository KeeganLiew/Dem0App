package com.keegan.experiment.activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.keegan.experiment.R;
import com.keegan.experiment.utilities.DisplayPictureUtil;
import com.keegan.experiment.utilities.GalleryUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName().toString();
    private final int GALLERY_ACTIVITY_CODE = 200;
    private final int RESULT_CROP = 400;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;

    FloatingActionsMenu fabBtn;
    //FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    Toolbar toolbar;
    TabLayout tabLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    NavigationView navigation;

    EditText username_EditText;
    EditText password_EditText;
    TextView nav_username;
    ImageView nav_display_picture;
    ImageView background_image;

    Button login_Button;

    public static String username = "noName";
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_activity);

        initInstances();
    }

    private void initInstances() {
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //drawers
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //float button
        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        //fabBtn = (FloatingActionButton) findViewById(R.id.fabBtn);
        /*fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "fabBtn onClick");
                Snackbar.make(rootLayout, "Hello. I am Snackbar!!", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
            }
        });*/

        fabBtn = (FloatingActionsMenu) findViewById(R.id.fabBtn);
        Log.d(TAG, "fabBtn made");
        final View actionB = findViewById(R.id.action_b);
        com.getbase.floatingactionbutton.FloatingActionButton actionC = new com.getbase.floatingactionbutton.FloatingActionButton(getBaseContext());
        actionC.setTitle("Hide/Show Action above");
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });
        fabBtn.addButton(actionC);

        //tabs
        /*tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 4"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 5"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 6"));*/

        //Header toolbar
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("Hello " + username);

        //nav draw
        navigation = (NavigationView) findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navItem1:
                        break;
                    case R.id.navItem2:
                        break;
                    case R.id.navItem3:
                        break;
                }
                return false;
            }
        });

        //EditText
        username_EditText = (EditText) findViewById(R.id.usernameEditText);
        password_EditText = (EditText) findViewById(R.id.passwordEditText);

        //Buttons
        login_Button = (Button) findViewById(R.id.loginButton);

        //TextView
        nav_username = (TextView) navigation.getHeaderView(0).findViewById(R.id.nav_username);

        //ImageView
        nav_display_picture = (ImageView) navigation.getHeaderView(0).findViewById(R.id.nav_display_picture);
        background_image = (ImageView) findViewById(R.id.background_image);

        //Listerners
        login_Button.setOnClickListener(this);
        nav_display_picture.setOnClickListener(this);
        nav_username.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "Long pressed background_image");
                //pop up dialog with cancel
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        DisplayPictureUtil.loadImageFromStorage(nav_display_picture, "/data/data/com.keegan.experiment/app_imageDir");
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void updateUsername(String title) {
        collapsingToolbarLayout.setTitle("Hello " + title);
        nav_username.setText(title);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                username = username_EditText.getText().toString();
                password = password_EditText.getText().toString();
                Log.d(TAG, "Username is: " + username);
                Log.d(TAG, "Password is: " + password);
                updateUsername(username);
                Snackbar.make(rootLayout, "Hello " + username, Snackbar.LENGTH_INDEFINITE)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
                hideKeyBoard();
                break;
            case R.id.nav_display_picture:
                imageMethod();
                break;
        }
    }

    public void imageMethod() {
        Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
        startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String picturePath = data.getStringExtra("picturePath");
                //perform Crop on the Image Selected from Gallery
                Intent cropIntent = DisplayPictureUtil.performCrop(picturePath);

                if (cropIntent != null) {
                    startActivityForResult(cropIntent, RESULT_CROP);
                } else {
                    String errorMessage = "your device doesn't support the crop action!";
                    Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }

        if (requestCode == RESULT_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap selectedBitmap = extras.getParcelable("data");
                // Set The Bitmap Data To ImageView

                selectedBitmap = DisplayPictureUtil.performCircleCrop(selectedBitmap);
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                DisplayPictureUtil.saveToInternalSorage(cw, selectedBitmap);
                nav_display_picture.setImageBitmap(selectedBitmap);
                nav_display_picture.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }

}


