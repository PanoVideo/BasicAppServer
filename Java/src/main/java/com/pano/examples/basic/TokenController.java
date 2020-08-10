package com.pano.examples.basic;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

/**
 * <p>description: sample app server restful api </p>
 *
 * @author pano
 */
@RestController
public class TokenController {

    private final static String TOKEN_URL = "https://api.pano.video/auth/token";
    //put your appSecret here
    private final static String APP_SECRET = "xxxxx";

    /**
     * @param request
     * @return
     */
    @PostMapping("/app/login")
    public String getToken(@RequestBody TokenRequest request) {
        URL url = null;
        try {
            url = new URL(TOKEN_URL);
            JSONObject body = new JSONObject();

            body.put("userId", request.getUserId());
            body.put("channelId", request.getChannelId());
            body.put("duration", request.getDuration());
            body.put("privileges", 0);
            body.put("channelDuration", request.getChannelDuration());

            JSONObject jsonObject = postAndParseJSON(url, body.toString(), request.getAppId());
            return (String) jsonObject.get("token");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param appId     Please user your appId,you can get from application module in user console
     * @param appSecret Same as above
     * @return PanoSign
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String generatePanoSign(String appId, String appSecret)
            throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        long timestamp = System.currentTimeMillis() / 1000L;
        String sign = appId + "." + timestamp + "." + signature(appId, timestamp, appSecret);
        return sign;
    }

    public static String signature(String appId, long timestamp, String appSecret)
            throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String message = appId + timestamp;
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(appSecret.getBytes("UTF-8"), "HmacSHA256");
        hmacSha256.init(secretKey);
        byte[] bytes = hmacSha256.doFinal(message.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(bytes);
    }


    private static JSONObject postAndParseJSON(URL url, String postData, String appId) throws IOException,
            InvalidKeyException,
            NoSuchAlgorithmException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty(
                "Content-Type", "application/json");
        urlConnection.setRequestProperty(
                "charset", StandardCharsets.UTF_8.displayName());
        urlConnection.setRequestProperty("Content-Length", Integer.toString(postData.length()));
        urlConnection.setRequestProperty("Authorization", "PanoSign " + generatePanoSign(appId, APP_SECRET));
        urlConnection.setRequestProperty("Tracking-Id", UUID.randomUUID().toString());
        urlConnection.setUseCaches(false);
        urlConnection.getOutputStream()
                .write(postData.getBytes(StandardCharsets.UTF_8));
        JSONTokener jsonTokener = new JSONTokener(urlConnection.getInputStream());
        return new JSONObject(jsonTokener);
    }
}
