package linkopinghackers.swipeefy;

import android.content.Context;
import android.graphics.Point;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matilda on 2016-08-09.
 */
public class HttpConnection {
     private String  playlistUri;
     String imageUri;
     JSONObject playlist;
    static RequestQueue queue;
    public HttpConnection httpConnection = this;

    public HttpConnection(String imageUri){
    this.imageUri = imageUri;

    }


   // public void postLocation(String userId){
       /* RequestQueue queue = Volley.newRequestQueue(LoginActivity. );
        // Instantiate the RequestQueue.

        String url = "10.47.12.22:8000";
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                       System.out.print("Response is: " + response.substring(0, 500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }*/

    public void getFriend(Point point, String AccessToken){

    }

    public void instatiateQueue(Context context){
        queue = Volley.newRequestQueue(context);
    }

    public void transferString(String img, String pl){
        playlistUri = pl;


    }

    public void getPlaylist(String clientId, String accessToken){
        final String acc = "Bearer " + accessToken;
        String url = "https://api.spotify.com/v1/users/" + clientId + "/playlists";





        //Unclear if param needed when getHeaders is overridden below, commented away
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Accept", "application/json");
        params.put("Authorization", "Bearer " + accessToken);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                       // Log.d("Response", response.toString());

                        try {
                            playlist = response.getJSONArray("items").getJSONObject(0);
                            httpConnection.imageUri = playlist.getJSONArray("images").getJSONObject(0).getString("url");
                            System.out.println(imageUri);
                            httpConnection.setimageUri(imageUri);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d("Error.Response", response);
                        System.out.println(error.networkResponse);
                    }
                }
        )
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", acc);
                return headers;
            }
        };
        queue.add(request);
    }

    public String getPlaylistUri(String clientId, String accessToken) throws JSONException {

        // getPlaylist(clientId, accessToken);
        return playlist.getString("uri");
    }

    public String getImageUrl(String clientId, String accessToken) throws JSONException {

        /*JSONObject playlist = getPlaylist(clientId, accessToken);
        String imageUri = playlist.getJSONArray("images").getJSONObject(0).getString("url");
        return imageUri;*/
        return httpConnection.imageUri;
    }

    public void followPlaylist(String friendId, String accessToken, String playlistId){
        String url = "https://api.spotify.com/v1/users/" + friendId + "/playlists/"+ playlistId + "/followers";
        final String acc = "Bearer " + accessToken;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d("Error.Response", response);

                    }
                }
        )
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", acc);
                return headers;
            }
        };
        queue.add(request);

    }

    public void setimageUri(String imageUri) {
        httpConnection.imageUri = imageUri;
    }
}
