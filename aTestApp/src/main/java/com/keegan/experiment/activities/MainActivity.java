package com.keegan.experiment.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.Behavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout.DrawerListener;
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
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.keegan.experiment.Global;
import com.keegan.experiment.Intents;
import com.keegan.experiment.R;
import com.keegan.experiment.fragments.Development;
import com.keegan.experiment.fragments.DeviceInfoFragment;
import com.keegan.experiment.fragments.SmsFragment;
import com.keegan.experiment.fragments.UnderConstructionFragment;
import com.keegan.experiment.utilities.ContactUtil;
import com.keegan.experiment.utilities.DisplayPictureUtil;
import com.keegan.experiment.utilities.GalleryUtil;
import com.keegan.experiment.customs.CustomCoordinatorLayout;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsFragment;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private final String TAG = getClass().getSimpleName();

    private static DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private NestedScrollView fragmentLayoutNSV;
    private NestedScrollView homeLayoutNSV;

    private AppBarLayout mAppBarLayout;
    private Behavior behavior;
    private static CustomCoordinatorLayout rootLayoutCCL;
    private Toolbar mToolbar;
    //private TabLayout tabLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private NavigationView navigationNV;

    private TextView navigationUsernameTV;
    private EditText usernameET;
    private EditText passwordET;
    private ImageView navigationDisplayPictureIV;
    private ImageView toolBarImageIV;
    private Button loginB;
    private FloatingActionsMenu fabMainButtonFAM;
    private FloatingActionButton buttonA;
    private FloatingActionButton buttonB;
    private FloatingActionButton buttonC;

    private static String username;
    private String password;

    private static Activity mActivity;
    private Context mContext;
    private Toast exitToast;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mContext = getApplicationContext();
        setContentView(R.layout.activity_main);

        viewObjectsInitializations();
        otherInitializations();
    }

    private void viewObjectsInitializations() {
        //layouts
        fragmentLayoutNSV = (NestedScrollView) findViewById(R.id.Activity_Main_NestedScrollView_FragmentLayout);
        homeLayoutNSV = (NestedScrollView) findViewById(R.id.Activity_Main_NestedScrollView_HomeLayout);
        rootLayoutCCL = (CustomCoordinatorLayout) findViewById(R.id.Activity_Main_CustomCoordinatorLayout_RootLayout);
        rootLayoutCCL.setAllowForScroll(true);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.Activity_Main_AppBarLayout);

        //toolbar
        mToolbar = (Toolbar) findViewById(R.id.Activity_Main_Toolbar);
        setSupportActionBar(mToolbar);

        //drawers
        mDrawerLayout = (DrawerLayout) findViewById(R.id.Activity_Main_DrawerLayout);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, R.string.hello_world, R.string.hello_world);
        mDrawerLayout.setDrawerListener(new mainDrawerListener());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //float menu & buttons
        fabMainButtonFAM = (FloatingActionsMenu) findViewById(R.id.Activity_Main_FloatingActionsMenu_FabMainButton);
        buttonA = (FloatingActionButton) findViewById(R.id.Activity_Main_FloatingActionButton_ButtonA);
        buttonB = (FloatingActionButton) findViewById(R.id.Activity_Main_FloatingActionButton_ButtonB);
        buttonC = (FloatingActionButton) findViewById(R.id.Activity_Main_FloatingActionButton_ButtonC);
        buttonC.setOnClickListener(this);

        //tabs
        /*tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 4"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 5"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 6"));*/

        //Header toolbar
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.Activity_Main_CollapsingToolbarLayout);
        setTitle("Hello " + username);

        //nav draw
        navigationNV = (NavigationView) findViewById(R.id.Activity_Main_NavigationView_Navigation);
        navigationNV.setNavigationItemSelectedListener(new mainNavigationItemListener());

        //EditText
        usernameET = (EditText) findViewById(R.id.Activity_Main_EditText_Username);
        passwordET = (EditText) findViewById(R.id.Activity_Main_EditText_Password);

        //Buttons
        loginB = (Button) findViewById(R.id.Activity_Main_Button_Login);
        loginB.setOnClickListener(this);

        //TextView
        navigationUsernameTV = (TextView) navigationNV.getHeaderView(0).findViewById(R.id.Navigation_TextView_Username);
        navigationUsernameTV.setOnLongClickListener(new userNameLongClickListener());

        //ImageView
        navigationDisplayPictureIV = (ImageView) navigationNV.getHeaderView(0).findViewById(R.id.Navigation_ImageView_DisplayPicture);
        navigationDisplayPictureIV.setOnClickListener(this);
        toolBarImageIV = (ImageView) findViewById(R.id.Activity_Main_ImageView_ToolbarImage);
    }

    private void otherInitializations() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = sharedPreferences.getString("Username", "noName");
        updateUsername(username);

        exitToast = Toast.makeText(mActivity, "Press again to exit.", Toast.LENGTH_LONG);
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
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        DisplayPictureUtil.loadImageFromStorage(navigationDisplayPictureIV, "/data/data/com.keegan.experiment/app_imageDir");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                Log.d(TAG, "Received Intent: " + intent.getAction());
                if (Intents.FRAGMENT_ITEM_CANCELLED.equalsName(action)) {
                    closeFragmentLayout();
                } else if (Intents.PICK_CONTACT.equalsName(action)) {
                    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    Log.d(TAG, "Sending Intent: " + contactPickerIntent.getAction());
                    startActivityForResult(contactPickerIntent, Global.CONTACT_PICKER_RESULT);
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(Intents.FRAGMENT_ITEM_CANCELLED.toString()));
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(Intents.PICK_CONTACT.toString()));
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "Fragment count: " + getFragmentManager().getBackStackEntryCount());
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            closeDrawer();
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            closeCurrentFragment();
        } else if (getFragmentManager().getBackStackEntryCount() == 1) {
            closeFragmentLayout();
        } else {
            if (exitToast.getView().isShown()) {
                exitToast.cancel();
                this.finish();
            } else {
                exitToast.show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Activity_Main_Button_Login:
                username = usernameET.getText().toString();
                password = passwordET.getText().toString();
                Log.d(TAG, "Username is: " + username);
                Log.d(TAG, "Password is: " + password);
                updateUsername(username);
                Snackbar usernameSB = Snackbar.make(rootLayoutCCL, "Hello " + username, Snackbar.LENGTH_INDEFINITE);
                usernameSB.setAction("Undo", new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                usernameSB.show();
                hideKeyboard(mActivity);
                break;
            case R.id.Navigation_ImageView_DisplayPicture:
                Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
                startActivityForResult(gallery_Intent, Global.GALLERY_ACTIVITY_CODE);
                break;
            case R.id.Activity_Main_FloatingActionButton_ButtonC:
                buttonB.setVisibility(buttonB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case (RESULT_OK):
                Toast.makeText(getBaseContext(), "SMS sent ", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "requestCode: " + requestCode);
                switch (requestCode) {
                    case Global.CONTACT_PICKER_RESULT:
                        String[] nameAndPhoneNumber = ContactUtil.searchForContactNumber(data, mContext);
                        Intent intent = new Intent(Intents.PICKED_CONTACT_INFO.toString());
                        intent.putExtra(Intents.PICKED_CONTACT_INFO_EXTRA_NAME.toString(), nameAndPhoneNumber[0]);
                        intent.putExtra(Intents.PICKED_CONTACT_INFO_EXTRA_PHONE_NUMBER.toString(), nameAndPhoneNumber[1]);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                        Log.d(TAG, "Sending Intent: " + intent.getAction() + " with extra: " + intent.getStringExtra(Intents.PICKED_CONTACT_INFO_EXTRA_PHONE_NUMBER.toString()));
                        break;
                    case Global.GALLERY_ACTIVITY_CODE:
                        String picturePath = data.getStringExtra("picturePath");
                        //perform Crop on the Image Selected from Gallery
                        Intent cropIntent = DisplayPictureUtil.performCrop(picturePath);

                        if (cropIntent != null) {
                            startActivityForResult(cropIntent, Global.RESULT_CROP);
                        } else {
                            String errorMessage = "your device doesn't support the crop action!";
                            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        break;
                    case Global.RESULT_CROP:
                        Bundle extras = data.getExtras();
                        Bitmap selectedBitmap = extras.getParcelable("data");
                        // Set The Bitmap Data To ImageView
                        selectedBitmap = DisplayPictureUtil.performCircleCrop(selectedBitmap);
                        ContextWrapper cw = new ContextWrapper(getApplicationContext());
                        DisplayPictureUtil.saveToInternalSorage(cw, selectedBitmap);
                        navigationDisplayPictureIV.setImageBitmap(selectedBitmap);
                        navigationDisplayPictureIV.setScaleType(ImageView.ScaleType.FIT_XY);
                        break;
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    //public methods
    public void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void clearSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void logout() {
        clearSharedPreferences();
        this.finish();
    }

    //getters and setters
    public static Activity getmActivity() {
        return mActivity;
    }

    public static String getUsername() {
        return username;
    }

    //listeners
    private class mainDrawerListener implements DrawerListener {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            drawerToggle.onDrawerSlide(drawerView, slideOffset);
            if (slideOffset * 100 > Global.hide_keyboard_login_drawer_percentage) {
                hideKeyboard(mActivity);
            }
        }

        @Override
        public void onDrawerOpened(View drawerView) {
        }

        @Override
        public void onDrawerClosed(View drawerView) {
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    }

    private class mainNavigationItemListener implements OnNavigationItemSelectedListener {

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
                    updateUsername(username);
                    break;
                case R.id.nav_drawer_sms_service:
                    mFragment = new SmsFragment();
                    startFragment(mFragment, getString(R.string.sms_service));
                    break;
                case R.id.nav_drawer_device_info:
                    mFragment = new DeviceInfoFragment();
                    startFragment(mFragment, getString(R.string.device_info));
                    break;
                case R.id.nav_drawer_development:
                    mFragment = new Development();
                    startFragment(mFragment, getString(R.string.development_log));
                    break;
                case R.id.nav_drawer_contact:
                    mFragment = new UnderConstructionFragment();
                    startFragment(mFragment, getString(R.string.contact));
                    break;
                case R.id.nav_drawer_about:
                    String description = getString(R.string.about_description_app);
                    String extra = getString(R.string.about_description_extra_info);
                    LibsFragment aboutPageFragment = new LibsBuilder()
                            //get the fragment
                            .withAboutIconShown(true)
                            .withAboutVersionShown(true)
                            .withAboutDescription(description)
                            .fragment();
                    startFragment(aboutPageFragment, getString(R.string.about));
                    break;
                case R.id.nav_drawer_feedback:
                    mFragment = new UnderConstructionFragment();
                    startFragment(mFragment, getString(R.string.feedback));
                    break;
                case R.id.nav_drawer_log_out:
                    logout();
                    break;
            }
            return false;
        }
    }

    private class userNameLongClickListener implements OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            Log.d(TAG, "Long pressed background_image");
            //pop up dialog with cancel
            return true;
        }
    }

    //public ui methods
    public static Snackbar createSnackBar(String message, int length) {
        Snackbar sb = Snackbar.make(rootLayoutCCL, message, length);
        sb.show();
        return sb;
    }

    public static void closeDrawer() {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Activity activity, EditText editText) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        }
    }

    //private ui methods
    private void updateUsername(String name) {
        updateTitle("Hello " + name);
        navigationUsernameTV.setText(name);
    }

    private void updateTitle(String title) {
        mCollapsingToolbarLayout.setTitle(title);
    }

    private void collapseToolbar() {
        LayoutParams params = (LayoutParams) mAppBarLayout.getLayoutParams();
        behavior = (Behavior) params.getBehavior();
        if (behavior != null) {
            behavior.onNestedFling(rootLayoutCCL, mAppBarLayout, null, 0, 10000, true);
        }
        rootLayoutCCL.setAllowForScroll(false);
    }

    private void expandToolbar() {
        LayoutParams params = (LayoutParams) mAppBarLayout.getLayoutParams();
        behavior = (Behavior) params.getBehavior();
        if (behavior != null) {
            behavior.setTopAndBottomOffset(0);
            behavior.onNestedPreScroll(rootLayoutCCL, mAppBarLayout, null, 0, 1, new int[2]);
        }
        rootLayoutCCL.setAllowForScroll(true);
    }

    private void startFragment(Fragment mFragment, String title) {
        if (mFragment != null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.Activity_Main_FragmentLayout_FragmentContainer, mFragment);
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            ft.addToBackStack(null);
            ft.commit();
            setFunctionLayout(View.VISIBLE);
            updateTitle(title);
        }
    }

    private void closeCurrentFragment() {
        getFragmentManager().popBackStack();
    }

    private void closeFragmentLayout() {
        getFragmentManager().popBackStack();
        setFunctionLayout(View.GONE);
        updateUsername(username);
    }

    private void setFunctionLayout(int status) {
        if (status == View.VISIBLE) {
            fragmentLayoutNSV.setVisibility(View.VISIBLE);
            homeLayoutNSV.setVisibility(View.GONE);
            fabMainButtonFAM.setVisibility(View.GONE);
            collapseToolbar();
        } else {
            fragmentLayoutNSV.setVisibility(View.GONE);
            homeLayoutNSV.setVisibility(View.VISIBLE);
            fabMainButtonFAM.setVisibility(View.VISIBLE);
            expandToolbar();
        }
    }

}


