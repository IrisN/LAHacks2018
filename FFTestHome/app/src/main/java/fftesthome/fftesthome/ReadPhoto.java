package fftesthome.fftesthome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.view.*;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.ImageView;
import 	java.io.ByteArrayOutputStream;
import 	android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;



public class ReadPhoto extends AppCompatActivity {

    public final static int MY_REQUEST_CODE = 1;

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_photo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if(requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK) {

            // Convert image data to bitmap
            Bitmap picture = (Bitmap)data.getExtras().get("data");

            // Set the bitmap as the source of the ImageView
            ((ImageView)findViewById(R.id.previewImage))
                    .setImageBitmap(picture);

            // More code goes here
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.JPEG, 90, byteStream);

            String base64Data = Base64.encodeToString(byteStream.toByteArray(),
                    Base64.URL_SAFE);

            String requestURL =
                    "https://vision.googleapis.com/v1/images:annotate?key=" +
                            getResources().getString(R.string.mykey);

            // Create an array containing
            // the LABEL_DETECTION feature
            JSONArray features = new JSONArray();
            JSONObject feature = new JSONObject();
            try {
                feature.put("type", "LABEL_DETECTION");
            }
            catch(Exception e)
            {
                System.out.println("ERROR");
            }
            features.put(feature);

            // Create an object containing
            // the Base64-encoded image data
            JSONObject imageContent = new JSONObject();
            try {
                imageContent.put("content", base64Data);
            }
            catch (Exception e)
            {
                System.out.println("ERROR");
            }

            // Put the array and object into a single request
            // and then put the request into an array of requests
            JSONArray requests = new JSONArray();
            JSONObject request = new JSONObject();
            try {
                request.put("image", imageContent);
                request.put("features", features);
            }
            catch(Exception e)
            {
                System.out.println("ERROR");
            }
            requests.put(request);
            JSONObject postData = new JSONObject();
            try {
                postData.put("requests", requests);
            }
            catch(Exception e)
            {
                System.out.println("ERROR");
            }

            // Convert the JSON into a
            // string
            String body = postData.toString();


//            Fuel.post(requestURL)
//                    .header(
//                            new Pair<String, Object>("content-length", body.length()),
//                            new Pair<String, Object>("content-type", "application/json")
//                    )
//                    .body(body.getBytes())
//                    .responseString(new Handler<String>() {
//                        @Override
//                        public void success(@NotNull Request request,
//                                            @NotNull Response response,
//                                            String data) {
//                            // More code goes here
//                        }
//
//                        @Override
//                        public void failure(@NotNull Request request,
//                                            @NotNull Response response,
//                                            @NotNull FuelError fuelError) {}
//                    });

//            HttpURLConnection con = null;
//
//            try {
//
//                try {
//                    URL myurl = new URL(requestURL);
//                    con = (HttpURLConnection) myurl.openConnection();
//
//                    con.setRequestMethod("GET");
//                }
//                catch (Exception e) {}
//
//                StringBuilder content = null;
//
//                try {
//                    try (BufferedReader in = new BufferedReader(
//                            new InputStreamReader(con.getInputStream()))) {
//
//                        String line;
//                        content = new StringBuilder();
//
//                        try {
//                            while ((line = in.readLine()) != null) {
//                                content.append(line);
//                                content.append(System.lineSeparator());
//                            }
//                        } catch (Exception e) {
//                        }
//                    }
//                }
//                catch(Exception e) {}
//
//                System.out.println(content.toString());
//
//            } finally {
//
//                if(con != null)
//                con.disconnect();
//            }
        }
    }
}
