package com.addonvisioninfotech.whatsappstatusdownloader.application;

import android.app.Activity;
import android.app.Application;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.addonvisioninfotech.whatsappstatusdownloader.R;
import com.addonvisioninfotech.whatsappstatusdownloader.model.Status;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyApp extends Application {

    public static final String DISPLAY_MESSAGE_ACTION = "pushnotifications.DISPLAY_MESSAGE";
    public static final String EXTRA_MESSAGE = "message";
    private static final String KEYSERVERID = "keyserverid";
    public static String SHARED_PREF_NAME = "RS_PREF";
    public static ImageLoader loader;
    public static DisplayImageOptions OPTIONS_Profile = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(0).cacheInMemory(true).showImageOnFail(0).cacheInMemory(true)
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565).build();
    public static DisplayImageOptions OPTIONS = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.noimage).cacheInMemory(true)
            .showImageOnFail(R.drawable.noimage)
            .considerExifParams(true)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565).build();

    public static DisplayImageOptions OPTIONS_CONTRIBUTION = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.noimage).cacheInMemory(true)
            .showImageOnFail(R.drawable.noimage)
            .considerExifParams(true)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .imageScaleType(ImageScaleType.NONE)
            .bitmapConfig(Bitmap.Config.RGB_565).build();
    private static ProgressDialog dialog;
    private static Context ctx;
    private static MyApp myApplication = null;
    public ImageLoaderConfiguration config;

    public static MyApp getApplication() {
        return myApplication;
    }

    public static void spinnerStart(Context context, String text) {
        String pleaseWait = text;
        dialog = ProgressDialog.show(context, "", pleaseWait, true);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    public static void spinnerStop() {
       try {
           if (dialog != null) {
               if (dialog.isShowing()) {
                   dialog.dismiss();
               }
           }
       }catch (Exception e){}
    }

    public static double roundDouble(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public static boolean myDeviceIdFlag() {
        boolean deviceId = false;
        SharedPreferences sp = ctx.getSharedPreferences(SHARED_PREF_NAME, 0);
        deviceId = sp.getBoolean(KEYSERVERID, false);
        return deviceId;
    }

    public static void saveDeviceIdFlag(boolean flag) {
        SharedPreferences sp = ctx.getSharedPreferences(SHARED_PREF_NAME, 0);
        // String e = sp.getString(PASSWORD, null);
        boolean settoserverflag = sp.getBoolean(KEYSERVERID, false);
        if (settoserverflag == true) {
            // Do not save, data already in preference
            return;
        }

        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEYSERVERID, flag);
        editor.commit();

    }

    public static void printValue(String text) {
        System.out.println(text);
    }

    public static void popMessage(String titleMsg, String errorMsg,
                                  Context context) {
        // pop error message
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titleMsg).setMessage(errorMsg)
                .setPositiveButton("OK", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showMassage(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
    static DownloadManager dm;


//    public static void writeStatus(ArrayList<Status> statuses) {
//        try {
//            String path = "/data/data/" + ctx.getPackageName()
//                    + "/status.ser";
//            File f = new File(path);
//            if (f.exists()) {
//                f.delete();
//            }
//            FileOutputStream fileOut = new FileOutputStream(path);
//            ObjectOutputStream out = new ObjectOutputStream(fileOut);
//            out.writeObject(statuses);
//            out.close();
//            fileOut.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    public static ArrayList<Status> readStatus() {
//        String path = "/data/data/" + ctx.getPackageName()
//                + "/status.ser";
//        File f = new File(path);
//        ArrayList<Status> websiteWrappers = new ArrayList<>();
//        if (f.exists()) {
//            try {
//                System.gc();
//                FileInputStream fileIn = new FileInputStream(path);
//                ObjectInputStream in = new ObjectInputStream(fileIn);
//                websiteWrappers = (ArrayList<Status>) in.readObject();
//                in.close();
//                fileIn.close();
//            } catch (StreamCorruptedException e) {
//                e.printStackTrace();
//            } catch (OptionalDataException e) {
//                e.printStackTrace();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return websiteWrappers;
//    }

//    public static void videoDownload(String vidData) {
//        try {
//            String mBaseFolderPath = android.os.Environment
//                    .getExternalStorageDirectory()
//                    + File.separator
//                    + "My Downloader" + File.separator;
//            if (!new File(mBaseFolderPath).exists()) {
//                new File(mBaseFolderPath).mkdir();
//            }
//            String mFilePath = "";
//            if (vidData.contains(".mp4")) {
//                mFilePath = "file://" + mBaseFolderPath + "/" + System.currentTimeMillis() + ".mp4";
//            } else if (vidData.contains(".mov")) {
//                mFilePath = "file://" + mBaseFolderPath + "/" + System.currentTimeMillis() + ".mov";
//            } else if (vidData.contains("HD")) {
//                mFilePath = "file://" + mBaseFolderPath + "/" + System.currentTimeMillis() + ".mp4";
//            } else if (vidData.contains("MP4")) {
//                mFilePath = "file://" + mBaseFolderPath + "/" + System.currentTimeMillis() + ".mp4";
//            } else if (vidData.contains("FHD")) {
//                mFilePath = "file://" + mBaseFolderPath + "/" + System.currentTimeMillis() + ".mp4";
//            } else if (vidData.contains("3gp")) {
//                mFilePath = "file://" + mBaseFolderPath + "/" + System.currentTimeMillis() + ".3gp";
//            }
//            Uri downloadUri = Uri.parse(vidData);
//            DownloadManager.Request req = new DownloadManager.Request(downloadUri);
//            req.setDestinationUri(Uri.parse(mFilePath));
//            req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//            dm = (DownloadManager) ctx.getSystemService(ctx.DOWNLOAD_SERVICE);
//            dm.enqueue(req);
//            Toast.makeText(ctx, "Download Started", Toast.LENGTH_LONG).show();
//            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
//            String[] strings = currentDateTimeString.split(" ");
//            int count=MyApp.getSharedPrefInteger(StaticData.COINS);
//            int totalcount=MyApp.getSharedPrefInteger(StaticData.TOTAL_COINS);
//            if(MyApp.getSharedPrefString(StaticData.DATE).equals(strings[0]+strings[1]+strings[2])){
//                MyApp.setSharedPrefString(StaticData.DATE,strings[0]+strings[1]+strings[2]);
//                if(count<10){
//                    MyApp.setSharedPrefInteger(StaticData.COINS,count+1);
//                    MyApp.setSharedPrefInteger(StaticData.TOTAL_COINS,totalcount+1);
//                }
//            }else {
//                MyApp.setSharedPrefInteger(StaticData.COINS,0);
//                MyApp.setSharedPrefString(StaticData.DATE,strings[0]+strings[1]+strings[2]);
//                MyApp.setSharedPrefInteger(StaticData.COINS,1);
//                MyApp.setSharedPrefInteger(StaticData.TOTAL_COINS,totalcount+1);
//            }
//
//            MainActivity.updatecount();
//        } catch (Exception e) {
//            Toast.makeText(ctx, "Download Failed: " + e.toString(), Toast.LENGTH_LONG).show();
//        }
//    }

    public static long getSharedPrefLong(String preffConstant) {
        long longValue = 0;
        SharedPreferences sp = myApplication.getSharedPreferences(
                SHARED_PREF_NAME, 0);
        longValue = sp.getLong(preffConstant, 0);
        return longValue;
    }

    public static void setSharedPrefLong(String preffConstant, long longValue) {
        SharedPreferences sp = myApplication.getSharedPreferences(
                SHARED_PREF_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(preffConstant, longValue);
        editor.commit();
    }

    public static void clearSharedPreferences() {
        SharedPreferences sp = myApplication.getSharedPreferences(
                SHARED_PREF_NAME, 0);
        sp.edit().clear().commit();
    }

    public static String getSharedPrefString(String preffConstant) {
        String stringValue = "";
        SharedPreferences sp = myApplication.getSharedPreferences(
                SHARED_PREF_NAME, 0);
        stringValue = sp.getString(preffConstant, "");
        return stringValue;
    }

    public static void setSharedPrefString(String preffConstant,
                                           String stringValue) {
        SharedPreferences sp = myApplication.getSharedPreferences(
                SHARED_PREF_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(preffConstant, stringValue);
        editor.commit();
    }

    public static int getSharedPrefInteger(String preffConstant) {
        int intValue = 0;
        SharedPreferences sp = myApplication.getSharedPreferences(
                SHARED_PREF_NAME, 0);
        intValue = sp.getInt(preffConstant, 0);
        return intValue;
    }

    public static void setSharedPrefInteger(String preffConstant, int value) {
        SharedPreferences sp = myApplication.getSharedPreferences(
                SHARED_PREF_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(preffConstant, value);
        editor.commit();
    }

    public static float getSharedPrefFloat(String preffConstant) {
        float floatValue = 0;
        SharedPreferences sp = myApplication.getSharedPreferences(
                preffConstant, 0);
        floatValue = sp.getFloat(preffConstant, 0);
        return floatValue;
    }

    public static void setSharedPrefFloat(String preffConstant, float floatValue) {
        SharedPreferences sp = myApplication.getSharedPreferences(
                preffConstant, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(preffConstant, floatValue);
        editor.commit();
    }

    public static void setSharedPrefArray(String preffConstant, float floatValue) {
        SharedPreferences sp = myApplication.getSharedPreferences(
                preffConstant, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(preffConstant, floatValue);
        editor.commit();
    }

    public static boolean getStatus(String name) {
        boolean status;
        SharedPreferences sp = myApplication.getSharedPreferences(
                SHARED_PREF_NAME, 0);
        status = sp.getBoolean(name, false);
        return status;
    }

    public static void setStatus(String name, boolean istrue) {
        SharedPreferences sp = myApplication.getSharedPreferences(
                SHARED_PREF_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(name, istrue);
        editor.commit();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                .getWindowToken(), 0);
    }

    public static Bitmap getimagebitmap(String imagepath) {
        Bitmap bitmap = decodeFile(new File(imagepath));

        // rotate bitmap
        Matrix matrix = new Matrix();
        // matrix.postRotate(MyApplication.getExifOrientation(imagepath));
        // create new rotated bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);

        return bitmap;
    }

    private static Bitmap decodeFile(File F) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(F), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 204;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE
                    && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(F), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

//    public static String getDeviceId() {
//
//        String android_id = "";
//        final TelephonyManager tm = (TelephonyManager) ctx
//                .getSystemService(Context.TELEPHONY_SERVICE);
//        final String tmDevice, tmSerial, androidId;
//        tmDevice = "" + tm.getDeviceId();
//        tmSerial = "" + tm.getSimSerialNumber();
//        androidId = ""
//                + android.provider.Settings.Secure.getString(
//                ctx.getContentResolver(),
//                android.provider.Settings.Secure.ANDROID_ID);
//
//        UUID deviceUuid = new UUID(androidId.hashCode(),
//                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
//        android_id = deviceUuid.toString();
//        return android_id;
//
//    }

    public static int getDisplayWidth() {
        WindowManager wm = (WindowManager) ctx
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    public static int getDisplayHeight() {
        WindowManager wm = (WindowManager) ctx
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return height;
    }

    public static boolean isEmailValid(String email) {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    public static String millsToDate(long mills) {

        Date d = new Date(mills);
        String format = "MM/dd/yyyy";
        return new SimpleDateFormat(format).format(d);
    }

    public static String millsToDate2(long mills) {

        Date d = new Date(mills);
        String format = "dd-MMM-yyyy";
        return new SimpleDateFormat(format).format(d);
    }

    public static String millsToTime(long mills) {

        Date d = new Date(mills);
        String format = "hh:mm a";
        return new SimpleDateFormat(format).format(d);
    }

    public static double millsToDayTime(long mills) {

        Date d = new Date(mills);
        String format = "kk:mm";
        String dateString = new SimpleDateFormat(format).format(d);
        double hr = Double.parseDouble(dateString.split(":")[0]);
        double min = Double.parseDouble(dateString.split(":")[1]);
        return hr + (min / 100d);
    }

    public static String parseDateToddMMyyyy(String time) {
        String inputPattern = "dd-MM-yyyy";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getDateOrTimeFromMillis(String x) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yy - hh:mm a");

        long milliSeconds;
        try {
            milliSeconds = Long.parseLong(x);
        } catch (Exception e) {
            milliSeconds = Long.parseLong(x.replace(".", ""));
        }
        System.out.println(milliSeconds);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(System.currentTimeMillis());

        String s = formatter.format(calendar.getTime()).split("-")[0];
        String s1 = formatter.format(calendar2.getTime()).split("-")[0];

        if (s.equals(s1)) {
            return formatter.format(calendar.getTime()).split("-")[1];
        } else {
            return formatter.format(calendar.getTime()).split("-")[0];
        }
    }

    public static String getDateOrTimeFromMillis(Long x) {
        DateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM yy");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(x);

        return formatter.format(calendar.getTime());

    }

    public static void openFile(Context context, File url) throws IOException {
        // Create URI
        File file = url;
        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onLowMemory() {
        Runtime.getRuntime().gc();
        super.onLowMemory();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);


    }

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = getApplicationContext();
        loader = ImageLoader.getInstance();
        loader.init(ImageLoaderConfiguration.createDefault(this));
        setImageLoaderConfig();
        myApplication = this;

        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }





    public void setImageLoaderConfig() {

        config = new ImageLoaderConfiguration.Builder(getApplicationContext())

                .memoryCacheSize(20 * 1024 * 1024)

                // 20 Mb
                .memoryCache(new LruMemoryCache(20 * 1024 * 1024))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();

        // Initialize ImageLoader with configuration.
        loader.init(config);
    }

}
