package com.framgia.elsytem.mypackage;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;

import com.framgia.elsytem.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by avishek on 12/31/15.
 */
public class AvatarHandler extends AppCompatActivity {
    Context context;
    ImageView imageView;

    public AvatarHandler(Context _context, ImageView _imageview) {
        context = _context;
        imageView = _imageview;
    }

    public void loadAvatar(String avatar) {
        if (!avatar.isEmpty()) {
            if (isUrl(avatar)) {
                Picasso.with(context)
                        .load(avatar)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .resize(100, 100)
                        .centerCrop()
                        .placeholder(R.drawable.ic_person_outline_black_36dp)
                        .error(R.drawable.ico_fail)
                        .into(imageView);
            } else {
                // Write image byte array from base64 string into file system
                mCreateFileFromBase64String(avatar);
                // get the uri of the created image
                Uri uri = mGetUri(getFilesDir() + Constants.KEY_AVATAR_FILE_NAME);
                Picasso.with(context)
                        .load(uri)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .resize(100, 100)
                        .centerCrop()
                        .placeholder(R.drawable.ic_person_outline_black_36dp)
                        .error(R.drawable.ico_fail)
                        .into(imageView);
            }
        }
    }

    public boolean isUrl(String avatar) {
        java.net.URL url = null;
        try {
            url = new java.net.URL(avatar);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url != null;
    }

    public Uri mGetUri(String filePath) {
        return Uri.fromFile(new File(filePath));
    }

    public void mCreateFileFromBase64String(String avatar) {
        FileOutputStream imageOutputFile = null;
        try {
            imageOutputFile = new FileOutputStream(getFilesDir()
                    + Constants.KEY_AVATAR_FILE_NAME, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            imageOutputFile.write(decodeImage(avatar));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            imageOutputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] decodeImage(String imageDataString) {
        return Base64.decode(imageDataString, Base64.DEFAULT);
    }
}
