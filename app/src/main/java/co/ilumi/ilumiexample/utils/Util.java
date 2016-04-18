package co.ilumi.ilumiexample.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import co.ilumi.ilumiexample.R;

/**
 * Created by ruby_ on 4/16/16.
 */
public class Util {
    //TODO: This value is supposed to be kepted in sharedpreferences
//    public static byte[] mMacAddress;

    public static final String COMMAND_OFF = "off";
    public static final String COMMAND_SLEEP = "sleep";
    public static final String COMMAND_ON = "on";
    public static final String COMMAND_HELLO = "Hello";
    public static final String COMMAND_START = "start";
    public static final String COMMAND_BEGIN = "begin";
    public static final String COMMAND_OPEN = "open";
    public static final String COMMAND_UP = "up";
    public static final String COMMAND_HEY = "hey";
    public static final String COMMAND_WAKE = "wake";
    public static final String COMMAND_DOWN = "down";


    public static final String COMMAND_KEYWORD_HATE = "hate";
    public static final String COMMAND_KEYWORD_COLOR = "color";
    public static final String COMMAND_KEYWORD_RED = "red";
    public static final String COMMAND_KEYWORD_BLUE = "blue";
    public static final String COMMAND_KEYWORD_GREEN = "green";
    public static final String COMMAND_KEYWORD_CANDLE = "candle";
    public static final String COMMAND_KEYWORD_CANDLELIGHT = "candlelight";
    public static final String COMMAND_KEYWORD_PARTY = "party";
    public static final String COMMAND_KEYWORD_ROMANTIC = "romantic";
    public static final String COMMAND_KEYWORD_CUTE = "cute";
    public static final String COMMMAN_KEYWORD_BLINKING = "blinking";


    //Write to sharedpreferences tool
    public static void writeToSharedPreferences(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.pref_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //Get sharedPreferences tool
    public static String loadSharePreferences(Context context, String key) {
        Map<String, ?> prefs = context.getSharedPreferences(context.getString(R.string.pref_key), Context.MODE_PRIVATE).getAll();
        return String.valueOf(prefs.get(key));
    }
}
