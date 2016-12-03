package linkopinghackers.swipeefy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;


import java.util.HashMap;


/**
 * Created by Alexander on 2016-07-22.
 */
public class SessionManager {

    private static SQLiteDatabase writableDatabase;
    private static SQLiteDatabase readableDatabase;
    private static SharedPreferences pref;

    // Editor for Shared preferences
    private static SharedPreferences.Editor editor;

    // Context
    private static Context context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static String PREF_NAME;
    //private String PREF_NAME;

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    private static final String KEY_API_USERNAME = "id";

    // Email address (make variable public to access from outside)
    private static final String KEY_API_PASSWORD = "secret";
    private static final String CLIENT_ID = "8740928683fe4ab6be03091a875ac618";


    // Constructor
    public SessionManager(){
    }

    public void createLoginSession (Context context, String apiUserName){
        this.context = context;
        PREF_NAME = apiUserName;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_MULTI_PROCESS | PRIVATE_MODE);
        //pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        // Storing login value as TRUE
        editor.clear();
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref

        editor.putString("id", apiUserName);

        // Storing pw in pref
        editor.commit();

    }

    public String getToken (){return PREF_NAME;}

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put("id", pref.getString("id", null));

        // user email id

        // return user
        return user;
    }

    public String getClientId (){return CLIENT_ID;}


    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);
        }
    }

    public String getPrefName (){
        return PREF_NAME;
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Starting Login Activity
        context.startActivity(i);
    }
}
