package com.coderschool.sosvn.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;

import static android.R.attr.bitmap;

/**
 * Created by Admin on 7/31/2017.
 */

public class StringUtil {

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public static String randomString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

   public static String getStringFromBitMap(Bitmap bitmap) {
       String temp = "";
       ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
       bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
       byte[] b = outputStream.toByteArray();
       temp = Base64.encodeToString(b,0,b.length,Base64.DEFAULT);
       return temp;
   }

    public static class BitMapToString extends AsyncTask<Bitmap,Void,String> {

        private String result;

        public BitMapToString() {}

        public String getResult() {
            return result;
        }

        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmaps[0].compress(Bitmap.CompressFormat.PNG,100,outputStream);
            byte[] b = outputStream.toByteArray();
            String temp = Base64.encodeToString(b,0,b.length,Base64.DEFAULT);
            return temp;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            result = s;
        }
    }

    public static class StringToBitMap extends AsyncTask<String,Void,Bitmap> {
        private Bitmap result;

        @Override
        protected Bitmap doInBackground(String... strings) {
             try {
                byte[] encodeByte = Base64.decode(strings[0],Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte,0,encodeByte.length);
                return bitmap;
            } catch (Exception e) {
                e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            result = bitmap;
        }
    }

}
