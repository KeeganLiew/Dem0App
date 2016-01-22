package com.keegan.experiment.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.keegan.experiment.INTENT;
import com.keegan.experiment.R;
import com.keegan.experiment.fragments.SmsReceiverFragment;
import com.keegan.experiment.fragments.UnderConstructionFragment;
import com.keegan.experiment.utilities.DisplayPictureUtil;
import com.keegan.experiment.utilities.GalleryUtil;
import com.keegan.experiment.services.SMSReceiver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName().toString();
    private final int GALLERY_ACTIVITY_CODE = 200;
    private final int RESULT_CROP = 400;

    DrawerLayout mDrawerLayout;
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
    Switch mySwitch;
    Button login_Button;

    public static String username = "noName";
    String password;
    Activity mActivity;
    public static Context mContext;
    Toast exitToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_activity);

        initViewObjects();

        mActivity = this;
        mContext = getApplicationContext();

        ComponentName smsReceiverComponent = new ComponentName(mContext, SMSReceiver.class);

        exitToast = Toast.makeText(mActivity, "Press again to exit.", Toast.LENGTH_LONG);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        updateUsername(sharedPreferences.getString("Username", ""));

        //sms stuff
        int status = mContext.getPackageManager().getComponentEnabledSetting(smsReceiverComponent);
        if (status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            Log.d(TAG, "receiver is enabled");
        } else if (status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            Log.d(TAG, "receiver is disabled");
        }
        //Disable
        mContext.getPackageManager().setComponentEnabledSetting(smsReceiverComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        //Enable
        mContext.getPackageManager().setComponentEnabledSetting(smsReceiverComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    //TODO TO USE
    public void Logout() {
        clear_pref();
        //otherstuff
    }

    public void clear_pref() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static Context getAppContext() {
        return MainActivity.mContext;
    }

    private void initViewObjects() {
        ACTIVITY_FRAGMENTS_LAYOUT = (NestedScrollView) findViewById(R.id.main_activity_fragment_layout);
        main_activity_home_layout = (NestedScrollView) findViewById(R.id.main_activity_home_layout);

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //drawers
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, R.string.hello_world, R.string.hello_world);
        mDrawerLayout.setDrawerListener(drawerToggle);

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
                Fragment mFragment;

                closeDrawer();
                hideKeyboard(mActivity);
                FragmentManager fragmentManager = mActivity.getFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                switch (id) {
                    case R.id.nav_drawer_home:
                        closeFragmentLayout();
                        break;
                    case R.id.nav_drawer_sms_service:
                        mFragment = new SmsReceiverFragment();
                        startFragment(mFragment);
                        break;
                    case R.id.nav_drawer_development:
                        mFragment = new UnderConstructionFragment();
                        startFragment(mFragment);
                        break;
                    case R.id.nav_drawer_contact:
                        mFragment = new UnderConstructionFragment();
                        startFragment(mFragment);
                        break;
                    case R.id.nav_drawer_about:
                        mFragment = new UnderConstructionFragment();
                        startFragment(mFragment);
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

        //Switch
        mySwitch = (Switch) findViewById(R.id.smsToggleSwitch);

        //set the switch to ON
        mySwitch.setChecked(true);
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    Log.d(TAG, "Switch is currently ON");
                    //smsToggle = true;
                } else {
                    Log.d(TAG, "Switch is currently OFF");
                    //smsToggle = false;
                }

            }
        });

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

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    private void startFragment(Fragment mFragment) {
        if (mFragment != null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.FragmentContainer, mFragment);
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            ft.addToBackStack(null);
            ft.commit();
            setFunctionLayout(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        DisplayPictureUtil.loadImageFromStorage(nav_display_picture, "/data/data/com.keegan.experiment/app_imageDir");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                Log.d(TAG, "received intent: " + intent.getAction());

                if (INTENT.FRAGMENT_ITEM_CANCELLED.equalsName(action)) {
                    closeFragmentLayout();
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(INTENT.FRAGMENT_ITEM_CANCELLED.toString()));

    }

    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
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

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    NestedScrollView ACTIVITY_FRAGMENTS_LAYOUT;
    NestedScrollView main_activity_home_layout;

    @Override
    public void onBackPressed() {
        Log.d(TAG, "Frag count: " + getFragmentManager().getBackStackEntryCount());
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            closeDrawer();
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            closeCurrentFragment();
        } else if (getFragmentManager().getBackStackEntryCount() == 1) {
            closeFragmentLayout();
        } else {
            if (exitToast.getView().isShown()) {
                this.finish();
            } else {
                exitToast.show();
            }
        }
    }

    public void closeFragmentLayout() {
        getFragmentManager().popBackStack();
        setFunctionLayout(View.GONE);
    }

    private void setFunctionLayout(int status) {
        if (status == View.VISIBLE) {
            ACTIVITY_FRAGMENTS_LAYOUT.setVisibility(View.VISIBLE);
            main_activity_home_layout.setVisibility(View.GONE);
        } else {
            ACTIVITY_FRAGMENTS_LAYOUT.setVisibility(View.GONE);
            main_activity_home_layout.setVisibility(View.VISIBLE);
        }
    }

    public void closeCurrentFragment() {
        getFragmentManager().popBackStack();
    }

}


