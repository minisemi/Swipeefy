package linkopinghackers.swipeefy.TabLayoutActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import linkopinghackers.swipeefy.R;
import linkopinghackers.swipeefy.SessionManager;

/**
 * Created by Alexander on 2016-08-10.
 */
public class FavouritesFragment extends Fragment{

    private SessionManager sessionManager;
    private FragmentCommunicator fragmentCommunicator;
    private List<Playlist> playlists;
    private PlaylistAdapter adapter;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sessionManager = fragmentCommunicator.getSessionManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        setRetainInstance(true);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.list);


        playlists = new ArrayList<>();
        adapter = new PlaylistAdapter(getContext(), "PLAYLIST", playlists );
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                followPlaylist(playlists.get(i));
                Snackbar snackbar = Snackbar
                        .make(view, "You are now following this playlist", Snackbar.LENGTH_LONG);

                snackbar.show();
                return false;
            }
        });


    }

    private void followPlaylist(Playlist playlist) {
        final String acc = "Bearer " + sessionManager.getToken();
        String url = "https://api.spotify.com/v1/users/" + playlist.owner + "/playlists/" + playlist.id +"/followers";
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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
                headers.put("Authorization", "Bearer " + sessionManager.getToken());
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(request);

    }

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



    public void addFavourite (Playlist playlist){
        playlists.add(playlist);
        adapter.notifyDataSetChanged();

    }
}
