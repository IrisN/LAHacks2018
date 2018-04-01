package fftesthome.fftesthome;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import junit.framework.Assert;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public  class Util {

    public static String imageToBase64(Bitmap image) {

        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 90, byteStream);
            String base64Data = Base64.encodeToString(byteStream.toByteArray(),Base64.URL_SAFE);
            return base64Data;
        } catch(Exception e) {
            Log.e("error", "could not convert image to base64 string");
            e.printStackTrace();
        }

        return null;
    }

    public static String toJson(String base64Data) {

        try {
            JSONArray requests = new JSONArray();
            JSONObject request = new JSONObject();

            JSONArray features = new JSONArray();
            JSONObject feature = new JSONObject();
            feature.put("type", "LABEL_DETECTION");
            features.put(feature);

            JSONObject imageContent = new JSONObject();
            imageContent.put("content", base64Data);

            request.put("image", imageContent);
            request.put("features", features);
            requests.put(request);

            JSONObject postData = new JSONObject();
            postData.put("requests", requests);

            return postData.toString();
        } catch(Exception e) {
            Log.e("error", "could not make json object");
            e.printStackTrace();
        }

        return null;
    }

    public static void httpPost(String postData, String requestURL) {

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(requestURL);

            StringEntity entity = new StringEntity(postData);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
            httpClient.close();
        } catch(Exception e) {
            Log.e("error", "failed  POST request");
            e.printStackTrace();
        }

    }

}
