package com.ac.vr.testvr;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;

import com.google.vr.sdk.widgets.common.VrWidgetView;
import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * This class launches a fairly simple VrPanoramaView, then allows the user to take a screencap
 * by pressing the cardboard trigger.  Technically, the Screenshot happens whenever the screen is
 * pressed, but it serves the same purpose as the more specific onCardboardTrigger supported
 * by other GoogleVR stuff.
 *
 * The screenshot is facilitated by capturing the current Pitch and Yaw of the Headset.  From there
 * we derive what portion of the root image the user is looking at, identify the center point, then
 * grab all data within a fixed number of degrees out and up from the image.
 *
 * The algorithm handles wrapping around an edge, but it does that by relying on an image that has
 * been rotated 180 degrees so the seam is in the middle of the image.  I do this manually to the
 * asset, but you can see how it's used below.  I also prevent users from taking screenshots when
 * the vertical angle is too extreme.  The kinds of images we can take with the Cardboard Camera
 * make that useless information anyway, but for better quality images, it's too hard to figure
 * out how to capture the top of the sphere, so I just prevent it.
 */
public class MainVRActivity extends Activity {
    public static final String TAG = MainVRActivity.class.getSimpleName();

    //Access the vibration action (used for feedback during the image capture)
    private Vibrator vibrator;
    //The Panorama View
    private VrPanoramaView myPanorama;
    //The image we want to render in our Panorama Viewer
    private Bitmap panoImage;
    //Needed to take a healthy screenshot along the image seam
    private Bitmap wrapImage;

    //Used to generate a pretty much unique name for screenshot images
    private static Random rand = new Random();

    //Fixed value to capture X degrees to either side of the center screenshot point
    private final static double SCREENSHOT_WIDTH_DEGREES  = 12.5;
    //Fixed value to capture Y degrees above and below the center screenshot point
    private final static double SCREENSHOT_HEIGHT_DEGREES = 25.0;

    //Candidate Images
    private final static String ANDES_ASSET_NAME         = "andes.jpg";
    private final static String ANDES_ASSET_WRAP_NAME    = "andeshalfsplit.jpg";
    private final static String GAMEROOM_ASSET_NAME      = "gameroom.jpg";
    private final static String GAMEROOM_ASSET_WRAP_NAME = "gameroomhalfsplit.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_vr);

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        if (vibrator.hasVibrator()) Log.d(TAG, "Can Vibrate: YES");
        else                        Log.d(TAG, "Can Vibrate: NO");

        myPanorama = (VrPanoramaView)findViewById(R.id.pano_view);
        myPanorama.setEventListener(new VrPanoramaEventListener() {
            @Override
            public void onClick() {
                takeScreenshot();
            }
        });

        myPanorama.setDisplayMode(VrWidgetView.DisplayMode.FULLSCREEN_STEREO);
        loadPhotoSphere();
    }

    private void loadPhotoSphere() {
        //This could take a while. Should do on a background thread, but fine for current example
        VrPanoramaView.Options options = new VrPanoramaView.Options();

        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open(ANDES_ASSET_NAME);
            options.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
            panoImage = BitmapFactory.decodeStream(inputStream);

            //Log.d(TAG, "panoImage.width: " + panoImage.getWidth());
            //Log.d(TAG, "panoImage.height: " + panoImage.getHeight());

            myPanorama.loadImageFromBitmap(panoImage, options);
            inputStream.close();

            InputStream inputStream2 = assetManager.open(ANDES_ASSET_WRAP_NAME);
            wrapImage = BitmapFactory.decodeStream(inputStream2);
            inputStream2.close();
        } catch (IOException e) {
            Log.e(TAG, "Exception in loadPhotoSphere: " + e.getMessage() );
        }
    }

    @Override
    protected void onPause() {
        myPanorama.pauseRendering();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myPanorama.resumeRendering();
    }

    @Override
    protected void onDestroy() {
        myPanorama.shutdown();
        super.onDestroy();
    }

    private void takeScreenshot() {
        //DEBUG
        Log.d(TAG, "I'm in your screenshot, killing your dudez");
        try {
            //Let's try some different tasks:
            float[] yawAndPitch = new float[2];
            myPanorama.getHeadRotation(yawAndPitch);
            Log.d(TAG, "Yaw: " + yawAndPitch[0]);
            Log.d(TAG, "Pitch: " + yawAndPitch[1]);

            boolean useWrapImage = false;
            //If the snapshot bounds would exceed a standard boundary, we need to use our alternate image
            if (((yawAndPitch[0] - SCREENSHOT_WIDTH_DEGREES) < -180.0) || ((yawAndPitch[0] + SCREENSHOT_WIDTH_DEGREES) > 180.0)) {
                Log.i(TAG, "I need to use the wrapped image to screen cap near seam");
                useWrapImage = true;
            }

            //Prevent vertical seam wrapping.  Not practical to simulate screen shot
            if (((yawAndPitch[1] - SCREENSHOT_HEIGHT_DEGREES) < -90.0) || ((yawAndPitch[1] + SCREENSHOT_HEIGHT_DEGREES) > 90.0)) {
                Log.i(TAG, "Cannot screencapture vertical edges.  No Screenshot will be generated");
                return;
            }

            //I'm being really explicit here to make sure I got the math right
            double centerX = (double)panoImage.getWidth() / 2.0;
            double centerY = centerX / 2.0;
            Log.d(TAG, "centerX: " + centerX);
            Log.d(TAG, "centerY: " + centerY);

            double incrementX = (double)panoImage.getWidth()  / 360.0;
            double incrementY = (double)panoImage.getHeight() / 720.0;
            Log.d(TAG, "Basic Increment X: " + incrementX);
            Log.d(TAG, "Basic Increment Y: " + incrementY);

            int trueX = (int)Math.round(centerX + (incrementX * yawAndPitch[0]));
            int trueY = (int)Math.round(centerY - (incrementY * yawAndPitch[1]));
            Log.d(TAG, "True Center X: " + trueX);
            Log.d(TAG, "True Center Y: " + trueY);

            int xOffset = (int)Math.round(incrementX * 12.5);
            int yOffset = (int)Math.round(incrementY * 25.0);


            Bitmap croppedBitmap = null;

            if (!useWrapImage)
                croppedBitmap = Bitmap.createBitmap(panoImage, trueX - xOffset, trueY - yOffset, (xOffset * 2) + 1, (yOffset * 2) + 1);
            else {
                //If we have to use the wrap, determine which way around we need to move our view to hit center
                if (trueX < centerX) trueX += centerX;
                else trueX -= centerX;
                croppedBitmap = Bitmap.createBitmap(wrapImage, trueX - xOffset, trueY - yOffset, (xOffset * 2) + 1, (yOffset * 2) + 1);
            }

            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/vrcap_" + rand.nextInt(100000000) + ".jpg";

            Log.i(TAG, "Writing screenshot to file " + mPath);

            FileOutputStream fos = new FileOutputStream(mPath);
            croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            croppedBitmap.recycle();

            //If the snapshot succeeds, trigger the feedback vibration
            vibrator.vibrate(500);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }
}
