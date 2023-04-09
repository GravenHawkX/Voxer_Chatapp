package com.example.voxer.network;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;

import com.example.voxer.activities.ChatActivity;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class TTSServer {

    String result;
    private static final String TTS_FILE_NAME = "tts_cache.txt";

    private Context mContext;

    public TTSServer(Context context) {
        mContext = context;
    }

    //..

    public void ttsServer(String msg, ChatActivity.ChatBotCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // your existing code here
                    URL url = new URL("https://talktome.ngrok.app/message");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("message", msg);
                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());
                    os.flush();
                    os.close();
                    if (conn.getResponseCode()==200){
                        InputStream responseBody = conn.getInputStream();
                        InputStreamReader responseBodyReader =
                                new InputStreamReader(responseBody, "UTF-8");
                        JsonReader jsonReader = new JsonReader(responseBodyReader);
                        jsonReader.beginObject(); // Start processing the JSON object
                        while (jsonReader.hasNext()) { // Loop through all keys
                            String key = jsonReader.nextName(); // Fetch the next key
                            if (key.equals("audio_base64")) { // Check if desired key
                                // Fetch the value as a String
                                result = jsonReader.nextString();
                                break; // Break out of the loop
                            } else {
                                jsonReader.skipValue(); // Skip values of other keys
                            }
                        }
                        jsonReader.close();
                    }else {
                        result = "error_404";
                        //Toast.makeText(ChatActivity.this, "Chat Bot server is down", Toast.LENGTH_SHORT).show();
                    }
                    conn.disconnect();
                    // Notify the callback on success
                    callback.onSuccess(result);
                } catch (Exception e) {
                    // Notify the callback on failure
                    callback.onFailure(e);
                }
            }
        }).start();
    }

    public void playAudio(String text) {

        byte[] decodedAudio = Base64.decode(text, Base64.DEFAULT);
        String fileName = "audio.wav";

        try {
            FileOutputStream outputStream = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(decodedAudio);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaPlayer mediaPlayer = MediaPlayer.create(mContext.getApplicationContext(), Uri.parse(mContext.getFilesDir() + "/" + fileName));
        mediaPlayer.start();

    }

    /*public void playAudio(String encodedAudio){
        byte[] decodedAudio = Base64.decode(encodedAudio, Base64.DEFAULT);
        String fileName = "audio.wav";

        try {
            FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(decodedAudio);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(getFilesDir() + "/" + fileName));
        mediaPlayer.start();

    }*/


}
