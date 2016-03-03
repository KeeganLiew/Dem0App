package com.keegan.experiment.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.keegan.experiment.Global;
import com.keegan.experiment.Intents;
import com.keegan.experiment.R;
import com.keegan.experiment.utilities.DisplayPictureUtil;
import com.keegan.experiment.utilities.GalleryUtil;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Keegan on 26/02/16.
 */
public class SettingsFragment extends Fragment implements OnClickListener {

    private final String TAG = SettingsFragment.class.getSimpleName();
    private Activity mActivity;

    private RelativeLayout wholeScreenRL;

    private LinearLayout backgroundImgEditorLL;
    private ImageView backgroundImgOriginalIV;
    private ImageView backgroundImgNewIV;
    private TextView backgroundImgTV;
    private ImageView backgroundImgChangeIV;
    private ImageView backgroundImgDeleteIV;
    private ImageView backgroundImgSaveIV;
    private ImageView backgroundImgRevertIV;

    private LinearLayout profilePicEditorLL;
    private ImageView profilePicOriginalIV;
    private ImageView profilePicNewIV;
    private TextView profilePicTV;
    private ImageView profilePicChangeIV;
    private ImageView profilePicDeleteIV;
    private ImageView profilePicSaveIV;
    private ImageView profilePicRevertIV;

    private LinearLayout usernameEditorLL;
    private EditText usernameET;
    private TextInputLayout usernameTIL;
    private ImageView usernameEditIV;
    private ImageView usernameSaveIV;
    private ImageView usernameRevertIV;

    private LinearLayout pinEditorLL;
    private EditText pinET;
    private TextInputLayout pinTIL;
    private ImageView pinEditIV;
    private ImageView pinSaveIV;
    private ImageView pinRevertIV;

    private LinearLayout passwordEditorLL;
    private EditText passwordET;
    private TextInputLayout passwordTIL;
    private ImageView passwordEditIV;
    private ImageView passwordSaveIV;
    private ImageView passwordRevertIV;

    private boolean cropGalleryImage = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        mActivity = getActivity();
        initializeViewObjects(rootView);
        return rootView;
    }

    private void initializeViewObjects(View rootView) {
        wholeScreenRL = (RelativeLayout) rootView.findViewById(R.id.Fragment_Settings_RelativeLayout_WholeScreen);

        //username editor
        usernameEditorLL = (LinearLayout) rootView.findViewById(R.id.Fragment_Settings_LinearLayout_Username);
        usernameET = (EditText) usernameEditorLL.findViewById(R.id.Editor_EditText_MainText);
        usernameTIL = (TextInputLayout) usernameEditorLL.findViewById(R.id.Editor_TextInputLayout_MainText);
        usernameEditIV = (ImageView) usernameEditorLL.findViewById(R.id.Editor_ImageView_Edit);
        usernameSaveIV = (ImageView) usernameEditorLL.findViewById(R.id.Editor_ImageView_Save);
        usernameRevertIV = (ImageView) usernameEditorLL.findViewById(R.id.Editor_ImageView_Revert);

        usernameET.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        usernameET.setText(Global.loadSavedPreferences(mActivity, Global.sharedPref_Username, Global.username_default),
                TextView.BufferType.EDITABLE);
        usernameTIL.setHint(getString(R.string.username));
        usernameEditIV.setOnClickListener(new EditIVOnClick(usernameET, usernameSaveIV, usernameRevertIV));
        usernameSaveIV.setOnClickListener(new SaveIVOnClick(usernameET, usernameEditIV, usernameRevertIV, Global.sharedPref_Username));
        usernameRevertIV.setOnClickListener(new RevertIVOnClick(usernameET, usernameEditIV, usernameSaveIV, usernameET.getText().toString()));

        //pin editor
        pinEditorLL = (LinearLayout) rootView.findViewById(R.id.Fragment_Settings_LinearLayout_Pin);
        pinET = (EditText) pinEditorLL.findViewById(R.id.Editor_EditText_MainText);
        pinTIL = (TextInputLayout) pinEditorLL.findViewById(R.id.Editor_TextInputLayout_MainText);
        pinEditIV = (ImageView) pinEditorLL.findViewById(R.id.Editor_ImageView_Edit);
        pinSaveIV = (ImageView) pinEditorLL.findViewById(R.id.Editor_ImageView_Save);
        pinRevertIV = (ImageView) pinEditorLL.findViewById(R.id.Editor_ImageView_Revert);

        pinET.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        pinET.setRawInputType(Configuration.KEYBOARD_12KEY);
        pinET.setTransformationMethod(PasswordTransformationMethod.getInstance());
        pinET.setText(Global.loadSavedPreferences(mActivity, Global.sharedPref_Pin, Global.pin_default),
                TextView.BufferType.EDITABLE);
        pinTIL.setHint(getString(R.string.pin));
        pinEditIV.setOnClickListener(new EditIVOnClick(pinET, pinSaveIV, pinRevertIV));
        pinSaveIV.setOnClickListener(new SaveIVOnClick(pinET, pinEditIV, pinRevertIV, Global.sharedPref_Pin));
        pinRevertIV.setOnClickListener(new RevertIVOnClick(pinET, pinEditIV, pinSaveIV, pinET.getText().toString()));

        //password editor
        passwordEditorLL = (LinearLayout) rootView.findViewById(R.id.Fragment_Settings_LinearLayout_Password);
        passwordET = (EditText) passwordEditorLL.findViewById(R.id.Editor_EditText_MainText);
        passwordTIL = (TextInputLayout) passwordEditorLL.findViewById(R.id.Editor_TextInputLayout_MainText);
        passwordEditIV = (ImageView) passwordEditorLL.findViewById(R.id.Editor_ImageView_Edit);
        passwordSaveIV = (ImageView) passwordEditorLL.findViewById(R.id.Editor_ImageView_Save);
        passwordRevertIV = (ImageView) passwordEditorLL.findViewById(R.id.Editor_ImageView_Revert);

        passwordET.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
        passwordET.setText(Global.loadSavedPreferences(mActivity, Global.sharedPref_Password, Global.password_default),
                TextView.BufferType.EDITABLE);
        passwordTIL.setHint(getString(R.string.password));
        passwordEditIV.setOnClickListener(new EditIVOnClick(passwordET, passwordSaveIV, passwordRevertIV));
        passwordSaveIV.setOnClickListener(new SaveIVOnClick(passwordET, passwordEditIV, passwordRevertIV, Global.sharedPref_Password));
        passwordRevertIV.setOnClickListener(new RevertIVOnClick(passwordET, passwordEditIV, passwordSaveIV, passwordET.getText().toString()));

        //images
        backgroundImgEditorLL = (LinearLayout) rootView.findViewById(R.id.Fragment_Settings_LinearLayout_BackgroundImage);
        backgroundImgOriginalIV = (ImageView) backgroundImgEditorLL.findViewById(R.id.Editor_ImageView_Original);
        backgroundImgNewIV = (ImageView) backgroundImgEditorLL.findViewById(R.id.Editor_ImageView_New);
        backgroundImgTV = (TextView) backgroundImgEditorLL.findViewById(R.id.Editor_TextView_Label);
        backgroundImgChangeIV = (ImageView) backgroundImgEditorLL.findViewById(R.id.Editor_ImageView_Change);
        backgroundImgDeleteIV = (ImageView) backgroundImgEditorLL.findViewById(R.id.Editor_ImageView_Delete);
        backgroundImgSaveIV = (ImageView) backgroundImgEditorLL.findViewById(R.id.Editor_ImageView_Save);
        backgroundImgRevertIV = (ImageView) backgroundImgEditorLL.findViewById(R.id.Editor_ImageView_Revert);

        backgroundImgTV.setText(R.string.background_image);
        backgroundImgChangeIV.setOnClickListener(new ChangeImageOnClickListener(false));
        //images
        profilePicEditorLL = (LinearLayout) rootView.findViewById(R.id.Fragment_Settings_LinearLayout_ProfileImage);
        profilePicOriginalIV = (ImageView) profilePicEditorLL.findViewById(R.id.Editor_ImageView_Original);
        profilePicNewIV = (ImageView) profilePicEditorLL.findViewById(R.id.Editor_ImageView_New);
        profilePicTV = (TextView) profilePicEditorLL.findViewById(R.id.Editor_TextView_Label);
        profilePicChangeIV = (ImageView) profilePicEditorLL.findViewById(R.id.Editor_ImageView_Change);
        profilePicDeleteIV = (ImageView) profilePicEditorLL.findViewById(R.id.Editor_ImageView_Delete);
        profilePicSaveIV = (ImageView) profilePicEditorLL.findViewById(R.id.Editor_ImageView_Save);
        profilePicRevertIV = (ImageView) profilePicEditorLL.findViewById(R.id.Editor_ImageView_Revert);

        profilePicTV.setText(R.string.display_picture);
        profilePicChangeIV.setOnClickListener(new ChangeImageOnClickListener(true));

        Global.loadImageIntoImageView(mActivity, backgroundImgOriginalIV, Global.profileBgPicImgName, R.drawable.wallpaper2);
        Global.loadImageIntoImageView(mActivity, profilePicOriginalIV, Global.profilePicImgName, R.drawable.name);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity = getActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case (RESULT_OK):
                Log.d(TAG, "requestCode: " + requestCode);
                switch (requestCode) {
                    case Global.GALLERY_ACTIVITY_CODE:
                        final String picturePath = data.getStringExtra("picturePath");
                        if (cropGalleryImage) { //perform crop on the Image Selected from Gallery
                            Intent cropIntent = DisplayPictureUtil.performCrop(picturePath, Global.display_picture_crop_size);
                            if (cropIntent != null) {
                                startActivityForResult(cropIntent, Global.RESULT_CROP);
                            } else {
                                Global.createAndShowToast(mActivity, getString(R.string.toast_error_message_crop_support), Toast.LENGTH_SHORT);
                            }
                            cropGalleryImage = false;
                        } else { //else save as background picture
                            Log.d(TAG, "picturePath: " + picturePath);

                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 2;
                            final Bitmap bmpPic1 = BitmapFactory.decodeFile(picturePath, options);
                            backgroundImgOriginalIV.setImageBitmap(bmpPic1);

                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    ContextWrapper cw = new ContextWrapper(mActivity.getApplicationContext());
                                    DisplayPictureUtil.saveToInternalStorage(cw, bmpPic1, Global.profileBgPicImgName);
                                }
                            });
                            //Global.loadImageIntoImageView(mActivity, backgroundImgOriginalIV, Global.profileBgPicImgName);
                        }

                        break;
                    case Global.RESULT_CROP:
                        final ContextWrapper cw = new ContextWrapper(mActivity.getApplicationContext());
                        DisplayPictureUtil.backUpDisplayPictureFromStorage(cw); //back up
                        //crop result
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = extras.getParcelable("data");
                        Bitmap circleCroppedBitmap = DisplayPictureUtil.performCircleCrop(imageBitmap);//circle crop
                        //save final to internal
                        DisplayPictureUtil.saveToInternalStorage(cw, circleCroppedBitmap, Global.profilePicImgName);

                        // Set The Bitmap Data To ImageView
                        profilePicOriginalIV.setImageBitmap(circleCroppedBitmap);
                        break;
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        Intent successIntent = new Intent(Intents.RELOAD_PROFILE.toString());
        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(successIntent);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    //listeners
    private class ChangeImageOnClickListener implements OnClickListener {
        boolean crop;

        public ChangeImageOnClickListener(boolean crop) {
            this.crop = crop;
        }

        @Override
        public void onClick(View v) {
            Intent gallery_Intent = new Intent(mActivity, GalleryUtil.class);
            startActivityForResult(gallery_Intent, Global.GALLERY_ACTIVITY_CODE);
            cropGalleryImage = crop;
        }
    }

    private class SaveIVOnClick implements OnClickListener {
        EditText textET;
        ImageView editIV;
        ImageView saveIV;
        ImageView revertIV;
        String key;

        public SaveIVOnClick(EditText textET, ImageView editIV, ImageView revertIV, String key) {
            this.textET = textET;
            this.editIV = editIV;
            this.revertIV = revertIV;
            this.key = key;
        }

        @Override
        public void onClick(View v) {
            Global.savePreferences(mActivity, key, textET.getText().toString());
            saveIV = (ImageView) v;
            editorEnableView(false, textET, editIV, saveIV, revertIV);
        }
    }

    private class RevertIVOnClick implements OnClickListener {
        EditText textET;
        ImageView editIV;
        ImageView saveIV;
        ImageView revertIV;
        String valueBeforeChange;

        public RevertIVOnClick(EditText textET, ImageView editIV, ImageView saveIV, String valueBeforeChange) {
            this.textET = textET;
            this.editIV = editIV;
            this.saveIV = saveIV;
            this.valueBeforeChange = valueBeforeChange;
        }

        @Override
        public void onClick(View v) {
            textET.setText(valueBeforeChange, TextView.BufferType.EDITABLE);
            revertIV = (ImageView) v;
            editorEnableView(false, textET, editIV, saveIV, revertIV);
        }
    }

    private class EditIVOnClick implements OnClickListener {
        EditText textET;
        ImageView editIV;
        ImageView saveIV;
        ImageView revertIV;

        public EditIVOnClick(EditText textET, ImageView saveIV, ImageView revertIV) {
            this.textET = textET;
            this.saveIV = saveIV;
            this.revertIV = revertIV;
        }

        @Override
        public void onClick(View v) {
            editIV = (ImageView) v;
            editorEnableView(true, textET, editIV, saveIV, revertIV);
        }
    }

    private void editorEnableView(Boolean enable, EditText textET, ImageView editIV, ImageView saveIV, ImageView revertIV) {
        textET.setEnabled(enable);
        if (enable) {
            textET.requestFocus();

            if (textET.getInputType() == InputType.TYPE_CLASS_TEXT)
                Log.d(TAG, "1");
            if (textET.getInputType() == InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
                Log.d(TAG, "2");
            if (textET.getInputType() == InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                Log.d(TAG, "3");
            if (textET.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD)
                Log.d(TAG, "4");

            editIV.setVisibility(View.GONE);
            saveIV.setVisibility(View.VISIBLE);
            revertIV.setVisibility(View.VISIBLE);
        } else {
            textET.clearFocus();
            editIV.setVisibility(View.VISIBLE);
            saveIV.setVisibility(View.GONE);
            revertIV.setVisibility(View.GONE);
        }
    }
}
