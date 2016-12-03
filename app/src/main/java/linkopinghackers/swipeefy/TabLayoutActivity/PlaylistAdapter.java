package linkopinghackers.swipeefy.TabLayoutActivity;

/**
 * Created by Alexander on 2016-07-05.
 */
        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.TextView;

        //import com.google.gson.JsonParser;

        import java.io.IOException;
        import java.security.acl.Group;
        import java.util.List;

        import linkopinghackers.swipeefy.R;

        /*import a46elks.a46elksapp.R;
        import a46elks.a46elksapp.tabLayout.Contacts.Contact;
        import a46elks.a46elksapp.tabLayout.Groups.Group;
        import a46elks.a46elksapp.tabLayout.History.HistoryListItem;*/

public class PlaylistAdapter extends ArrayAdapter {
    private final Context context;
    private List<Group> valuesGroups;
    private List<Playlist> valuesPlaylist;
    private final String LISTVIEWADAPTER_ACTION;
  //  private JsonParser jsonParser;

    // Might wanna create a specific adapter for history, handling "HistoryEvents" instead of Strings

    public PlaylistAdapter(Context context, String LISTVIEWADAPTER_ACTION, List values) {
        super(context, -1, values);
        this.context = context;
        switch (LISTVIEWADAPTER_ACTION) {

            case "GROUPS":
                this.valuesGroups = values;
                break;
            case "PLAYLIST":
                this.valuesPlaylist = values;
                break;
        }
        this.LISTVIEWADAPTER_ACTION = LISTVIEWADAPTER_ACTION;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;
        TextView textView;

        switch (LISTVIEWADAPTER_ACTION){


            case "PLAYLIST":
                rowView = inflater.inflate(R.layout.list_item, parent, false);
                textView = (TextView) rowView.findViewById(R.id.text_contact_mobile_number);
                textView.setText(valuesPlaylist.get(position).getName());
                ImageView imageView = (ImageView) rowView.findViewById(R.id.listView_contact_picture);
                try {
                imageView.setImageBitmap(valuesPlaylist.get(position).getImage());

                }catch (IOException i){
                    i.printStackTrace();
                }
                return rowView;

            case "SETTINGS":

            default:
                rowView = convertView;
                break;

        }

        //ImageView imageView = (ImageView) rowView.findViewById(R.id.listView_contact_picture);
        //textView.setText(valuesHistory[position]);

        return rowView;
    }


}
