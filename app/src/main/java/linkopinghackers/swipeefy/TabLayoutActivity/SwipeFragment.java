package linkopinghackers.swipeefy.TabLayoutActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import linkopinghackers.swipeefy.R;
import linkopinghackers.swipeefy.SessionManager;
import linkopinghackers.swipeefy.cardstack.CardStack;
import linkopinghackers.swipeefy.cardstack.CardStackListener;
import linkopinghackers.swipeefy.cardstack.CardsDataAdapter;
import linkopinghackers.swipeefy.HttpConnection;

/**
 * Created by Alexander on 2016-08-10.
 */
public class SwipeFragment extends android.support.v4.app.Fragment implements PlayerNotificationCallback, ConnectionStateCallback {

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String LISTVIEWADAPTER_ACTION = "RECEIVER";
    private List<String> listDataHeader;
    private EditText editTextMessage;
    private FragmentCommunicator fragmentCommunicator;
    private ExpandableListView expandableListView;
    private Player mPlayer;
    private CardStack mCardStack;
    private CardsDataAdapter mCardAdapter;
    private SessionManager sessionManager;
    private ImageButton previous, pause, play, next;
    private String playURI;
    public List<Playlist> playlistlist = new ArrayList();
    private SwipeFragment swipeFragment = this;
    private Boolean paused;

    // Container Activity must implement this interface
   /* public interface OnEventStartedListener {
        public void onSmsSent(String message, String senderName, HashMap<String, List<Contact>> listDataChild);

    }*/

    // Might need to use Activity instead of context in this method
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            fragmentCommunicator = (FragmentCommunicator) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnEventStartedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sessionManager = fragmentCommunicator.getSessionManager();
        Config playerConfig = new Config(getActivity().getApplicationContext(), sessionManager.getToken(), sessionManager.getClientId());
        mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {

            @Override
            public void onInitialized(Player player) {
                mPlayer.addConnectionStateCallback(SwipeFragment.this);
                mPlayer.addPlayerNotificationCallback(SwipeFragment.this);
                paused = false;
                //mPlayer.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");


            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
            }
        });

        for (int i = 0; i <= mCardStack.DEFAULT_STACK_MARGIN;i++){

        getPlaylist("tlds", sessionManager.getToken());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_swipe, container, false);
        setRetainInstance(true);

        //   setRetainInstance(true);
        return view;
    }
    public void getPlaylist(String clientId, String accessToken){
        final String acc = "Bearer " + accessToken;
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                        Random random = new Random();
                        try {

                            int rand = random.nextInt(response.getJSONArray("items").length()-1);

                                    JSONObject playlist = response.getJSONArray("items").getJSONObject(rand);
                            String imageUri = playlist.getJSONArray("images").getJSONObject(0).getString("url");
                            String playlistUri = playlist.getString("uri");
                            String playlistName = playlist.getString("name");
                            String id = playlist.getString("id");
                            String owner = playlist.getJSONObject("owner").getString("id");
                            SetPlaylistImage conn = new SetPlaylistImage(playlistUri, imageUri, playlistName, id, owner);
                            conn.execute((Void) null);

                            //mCardAdapter.notifyDataSetChanged();
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

    public class SetPlaylistImage extends AsyncTask<Void, Void, Boolean>{
        private Playlist playlist1;
        private String playlistUri, imageUri;
        private Bitmap bmp;
        private String name;
        private String id;
        private String owner;

        public SetPlaylistImage (String playlistUri, String imageUri, String name, String id, String owner){
            this.playlistUri = playlistUri;
            this.imageUri = imageUri;
            this.name = name;
            this.id = id;
            this.owner = owner;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
            URL url = new URL(imageUri);

            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            }catch (IOException m){
                m.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            playlist1 = new Playlist(playlistUri, bmp, name, id, owner);
            playlistlist.add(playlist1);
            mCardAdapter.add(playlist1);
            if (playlistlist.size()==20){
                mPlayer.play(playlistlist.get(0).getSongList());
            }

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        previous = (ImageButton) view.findViewById(R.id.action_previous);
        pause = (ImageButton) view.findViewById(R.id.action_pause);
        play = (ImageButton) view.findViewById(R.id.action_play);
        next = (ImageButton) view.findViewById(R.id.action_next);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.skipToPrevious();
                //getPlaylist("tlds", sessionManager.getToken());
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.pause();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (playlistlist.size()>0){

                    mPlayer.resume();

                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.skipToNext();
            }
        });

        mCardStack = (CardStack) view.findViewById(R.id.container);

        mCardStack.setContentResource(R.layout.card_content);
        mCardStack.setStackMargin(20);

        mCardAdapter = new CardsDataAdapter(getActivity().getApplicationContext(),0);


        mCardStack.setListener(new CardStackListener(swipeFragment, mCardStack));

        mCardStack.setAdapter(mCardAdapter);



    }

    public void onDiscard(int i){
        Playlist discarded = playlistlist.get(0);
        //SKICKA TILL FAVOURITES OM DIRECTION = HÖGER
        fragmentCommunicator.addPlaylistToFavourites(discarded);

        playlistlist.remove(discarded);
        if (playlistlist.size()>0){

        mPlayer.play(playlistlist.get(0).getSongList());
        } else {mPlayer.pause();}

    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("MainActivity", "Playback event received: " + eventType.name());
        switch (eventType) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        Log.d("MainActivity", "Playback error received: " + errorType.name());
        switch (errorType) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    @Override
    public void onDestroyView() {
        Spotify.destroyPlayer(this);

        super.onDestroyView();
    }

}
