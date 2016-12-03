package linkopinghackers.swipeefy.TabLayoutActivity;

import java.util.ArrayList;
import java.util.HashMap;

import linkopinghackers.swipeefy.SessionManager;

/**
 * Created by Alexander on 2016-08-10.
 */
public interface FragmentCommunicator {

    public SessionManager getSessionManager ();

    public void addPlaylistToFavourites (Playlist playlist);

}
