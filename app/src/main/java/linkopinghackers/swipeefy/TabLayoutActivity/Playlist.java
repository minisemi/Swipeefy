package linkopinghackers.swipeefy.TabLayoutActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Matilda on 2016-08-10.
 */
public class Playlist {
    String uri;
    Bitmap image;
    String name;
    String id;
    String owner;


    public Playlist(String uri, Bitmap image, String name, String id, String owner){
        this.uri = uri;
        this.image = image;
        this.name = name;
        this.id = id;
        this.owner = owner;
    }

    public Bitmap getImage() throws IOException {
        return image;
    }

    public String getSongList () {return uri;}
    public String getName(){return name;}
}
