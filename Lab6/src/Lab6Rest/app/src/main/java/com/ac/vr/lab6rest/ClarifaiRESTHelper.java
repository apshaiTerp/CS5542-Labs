package com.ac.vr.lab6rest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import okhttp3.OkHttpClient;

/**
 * Created by AC010168 on 3/1/2017.
 */

public class ClarifaiRESTHelper {

    public static String doClarifaiCall(int image) throws Exception {
        final ClarifaiClient client = new ClarifaiBuilder("FAvm9tkD6_K7wWDlA6eidzFBmgXO3LgKqSVTmS7j",
                "7y6nYIoYbjljAToHf9E9Gkm6InMriR7bkBjGHvLe").client(new OkHttpClient()).buildSync(); // or use .build() to get a Future<ClarifaiClient>
        client.getToken();

        Resources res = MainActivity.reference.getResources();
        Drawable drawable = res.getDrawable(R.drawable.dino2);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();

        ClarifaiResponse response = client.getDefaultModels().generalModel().predict().withInputs(
                ClarifaiInput.forImage(ClarifaiImage.of(bitmapdata))).executeSync();

        MBFImage image = null;

        return "NOPE";
    }
}
