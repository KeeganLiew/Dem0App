package com.keegan.experiment.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.Behavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.keegan.experiment.Global;
import com.keegan.experiment.Intents;
import com.keegan.experiment.R;
import com.keegan.experiment.fragments.ContactMe;
import com.keegan.experiment.fragments.DevelopmentLog;
import com.keegan.experiment.fragments.DeviceInfoFragment;
import com.keegan.experiment.fragments.SmsFragment;
import com.keegan.experiment.fragments.UnderConstructionFragment;
import com.keegan.experiment.utilities.ContactUtil;
import com.keegan.experiment.utilities.DisplayPictureUtil;
import com.keegan.experiment.utilities.GalleryUtil;
import com.keegan.experiment.customs.CustomCoordinatorLayout;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsFragment;

import java.io.File;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private final String TAG = MainActivity.class.getSimpleName();

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
    private boolean cropGalleryImage = false;

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
        Global.loadImageIntoImageView(mContext, navigationDisplayPictureIV, Global.profilePicImgName);
        toolBarImageIV = (ImageView) findViewById(R.id.Activity_Main_ImageView_ToolbarImage);
        toolBarImageIV.setOnLongClickListener(new toolBarImageLongClickListener());
        Global.loadImageIntoImageView(mContext, toolBarImageIV, Global.profileBgPicImgName);
    }

    private void otherInitializations() {
        username = Global.loadSavedPreferences(mActivity, Global.sharedPref_Username, Global.username_default);
        uiUpdateUsername(username);

        exitToast = Toast.makeText(mActivity, getString(R.string.toast_press_again_to_exit), Toast.LENGTH_LONG);
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
        Fragment mFragment;
        switch (item.getItemId()) {
            case R.id.main_menu_item_about:
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
            case R.id.main_menu_item_settings:
                mFragment = new UnderConstructionFragment();
                startFragment(mFragment, getString(R.string.settings));
                break;
            case R.id.main_menu_item_logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActivity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hide android keyboard
        uiUpdateUsername(username);
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
                //username
                final String previousUsername = Global.loadSavedPreferences(mActivity, Global.sharedPref_Username, Global.EMPTY_STRING);
                username = usernameET.getText().toString();
                Log.d(TAG, "Username is: " + username);
                Global.savePreferences(mActivity, Global.sharedPref_Username, username);
                uiUpdateUsername(username);
                Snackbar usernameSB = Snackbar.make(rootLayoutCCL, "Hello " + username, Snackbar.LENGTH_LONG);
                if (!previousUsername.equals(Global.EMPTY_STRING) && !previousUsername.equals(username)) {
                    usernameSB.setAction("Undo", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            username = previousUsername;
                            Global.savePreferences(mActivity, Global.sharedPref_Username, username);
                            uiUpdateUsername(username);
                        }
                    });
                    usernameSB.show();
                }
                //password
                final String previousPassword = Global.loadSavedPreferences(mActivity, Global.sharedPref_Password, Global.EMPTY_STRING);
                password = passwordET.getText().toString();
                Log.d(TAG, "Password is: " + password);
                Global.savePreferences(mActivity, Global.sharedPref_Password, password);
                Snackbar passwordSB = Snackbar.make(rootLayoutCCL, "Updated password to: " + password, Snackbar.LENGTH_SHORT);
                if (!previousPassword.equals(Global.EMPTY_STRING) && !previousPassword.equals(password)) {
                    passwordSB.setAction("Undo", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            password = previousPassword;
                            Global.savePreferences(mActivity, Global.sharedPref_Password, password);
                            uiUpdateUsername(password);
                        }
                    });
                    passwordSB.show();
                }

                hideKeyboard(mActivity);
                break;
            case R.id.Navigation_ImageView_DisplayPicture:
                Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
                startActivityForResult(gallery_Intent, Global.GALLERY_ACTIVITY_CODE);
                cropGalleryImage = true;
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
                        final String picturePath = data.getStringExtra("picturePath");
                        //perform Crop on the Image Selected from Gallery
                        if (cropGalleryImage) {
                            Intent cropIntent = DisplayPictureUtil.performCrop(picturePath, Global.display_picture_crop_size);

                            if (cropIntent != null) {
                                startActivityForResult(cropIntent, Global.RESULT_CROP);
                            } else {
                                Global.createAndShowToast(mActivity, getString(R.string.toast_error_message_crop_support), Toast.LENGTH_SHORT);
                            }
                            cropGalleryImage = false;
                        } else {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 2;
                            final Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
                            toolBarImageIV.setImageBitmap(bitmap);

                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                                    DisplayPictureUtil.saveToInternalStorage(cw, bitmap, Global.profileBgPicImgName);
                                }
                            });
                        }

                        break;
                    case Global.RESULT_CROP:
                        final ContextWrapper cw = new ContextWrapper(getApplicationContext());
                        //back up
                        DisplayPictureUtil.backUpDisplayPictureFromStorage(cw);
                        //crop result
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = extras.getParcelable("data");
                        //circle crop
                        Bitmap circleCroppedBitmap = DisplayPictureUtil.performCircleCrop(imageBitmap);
                        //save final to internal
                        DisplayPictureUtil.saveToInternalStorage(cw, circleCroppedBitmap, Global.profilePicImgName);

                        // Set The Bitmap Data To ImageView
                        navigationDisplayPictureIV.setImageBitmap(circleCroppedBitmap);
                        navigationDisplayPictureIV.setScaleType(ImageView.ScaleType.FIT_XY);

                        //undo if prev_profile_pic exist
                        Log.d(TAG, "undo if prev_profile_pic exist");
                        File directory = cw.getDir(Global.profileImgDirName, Context.MODE_PRIVATE);
                        final Bitmap prev_bitmapImage = DisplayPictureUtil.getDisplayPictureFromStorage(directory.getPath(), Global.prevProfileImgName);
                        Snackbar displayPicSB = Snackbar.make(rootLayoutCCL, "Revert display picture? ", Snackbar.LENGTH_INDEFINITE);
                        if (prev_bitmapImage != null) {
                            displayPicSB.setAction("Undo", new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DisplayPictureUtil.saveToInternalStorage(cw, prev_bitmapImage, Global.profilePicImgName);
                                    navigationDisplayPictureIV.setImageBitmap(prev_bitmapImage);
                                    navigationDisplayPictureIV.setScaleType(ImageView.ScaleType.FIT_XY);
                                    Global.deleteImage(mContext, Global.prevProfileImgName);
                                }
                            });
                        }
                        displayPicSB.show();
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
    public void clearDisplayPicture() {
        Global.deleteImage(mContext, Global.profilePicImgName);
    }

    public void clearBackgroundPicture() {
        Global.deleteImage(mContext, Global.profileBgPicImgName);
    }

    public void clearAllPictures() {
        clearDisplayPicture();
        clearBackgroundPicture();
    }

    public void logout() {
        Global.clearSharedPreferences(mActivity);
        clearAllPictures();
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
            hideKeyboard(mActivity);
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
                    uiUpdateUsername(username);
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
                    mFragment = new DevelopmentLog();
                    startFragment(mFragment, getString(R.string.development_log));
                    break;
                case R.id.nav_drawer_contact:
                    mFragment = new ContactMe();
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
            Log.d(TAG, "Long pressed username");
            showMerchantIdBox();
            return true;
        }
    }

    private class toolBarImageLongClickListener implements OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            Log.d(TAG, "Long pressed background_image");
            Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
            startActivityForResult(gallery_Intent, Global.GALLERY_ACTIVITY_CODE);
            return true;
        }
    }

    //public ui methods
    public static Snackbar createSnackBar(String message, int length) {
        Snackbar sb = Snackbar.make(rootLayoutCCL, message, length);
        sb.show();
        return sb;
    }

    public static void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
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
    private void uiUpdateUsername(String name) {
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
        uiUpdateUsername(username);
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

    private void showMerchantIdBox() {
        final Dialog merchantIdBoxDialog = new Dialog(this);
        merchantIdBoxDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        merchantIdBoxDialog.setContentView(R.layout.dialog_username_edit);
        merchantIdBoxDialog.setCanceledOnTouchOutside(false);
        final Button button = (Button) merchantIdBoxDialog.findViewById(R.id.Dialog_merchantIdBox_Button_Submit);
        final Button cancelB = (Button) merchantIdBoxDialog.findViewById(R.id.Fragment_Sms_Button_Cancel);
        final ProgressBar progressBar = (ProgressBar) merchantIdBoxDialog.findViewById(R.id.Dialog_merchantIdBox_Progressbar);
        final EditText editText = (EditText) merchantIdBoxDialog.findViewById(R.id.Dialog_merchantIdBox_EditText_Id);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText.getText().length() > 0) {
                    editText.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editText.requestFocus();
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            }
        }, 30);

        cancelB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                merchantIdBoxDialog.dismiss();
            }
        });

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (editText.getText().toString().isEmpty()) {
                    //Toast.makeText(v.getContext(), "Please enter more than one character", Toast.LENGTH_SHORT).show();
                    //Global.createAndShowToast(v.getContext(), getString(R.string.toast_enter_a_character), Toast.LENGTH_SHORT);
                    editText.setError(getString(R.string.toast_enter_a_character));
                    return;
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                progressBar.setVisibility(View.VISIBLE);
                try {
                    final String previousUsername = Global.loadSavedPreferences(mActivity, Global.sharedPref_Username, Global.EMPTY_STRING);
                    username = editText.getText().toString();
                    Log.d(TAG, "Username is: " + username);
                    Global.savePreferences(mActivity, Global.sharedPref_Username, username);
                    uiUpdateUsername(username);
                    Snackbar usernameSB = Snackbar.make(rootLayoutCCL, "Hello " + username, Snackbar.LENGTH_INDEFINITE);
                    if (!previousUsername.equals(Global.EMPTY_STRING)) {
                        usernameSB.setAction("Undo", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                username = previousUsername;
                                Global.savePreferences(mActivity, Global.sharedPref_Username, username);
                                uiUpdateUsername(username);
                            }
                        });
                    }
                    usernameSB.show();
                } catch (Exception e) {
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "saved " + editText.getText().toString());
                        merchantIdBoxDialog.dismiss();
                    }
                }, 750);
                closeDrawer();
            }
        });
        merchantIdBoxDialog.show();
    }

}


