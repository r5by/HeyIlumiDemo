package co.ilumi.ilumiexample.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.ilumi.ilumiexample.R;
import co.ilumi.ilumiexample.utils.StringCommandParser;

/**
 * Created by mxmerce on 4/16/16.
 */
public class PairedBulbAdapter extends ArrayAdapter<byte[]> {

    private List<byte[]> items = null;
    private static final int REQUEST_CODE = 1234;

    private byte[] mNearestMacAddress;

    Context mContext;

    static private char[] charArray = {
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'A', 'B',
            'C', 'D', 'E', 'F'
    };

    private static char NibbleToChar(int data) {
        return charArray[data & 0xF];
    }

    // Format raw mac address bytes to a human readable form.
    public static String bytesToString(byte[] mac) {

        if (mac != null && mac.length > 0) {

            char[] result = new char[(mac.length * 3) - 1];
            int temp;

            for (int i = 0; i < mac.length; i++) {

                temp = mac[mac.length - 1 - i] & 0x0F;
                result[i * 3 + 1] = NibbleToChar(temp);

                temp = mac[mac.length - 1 - i] >> 4;
                result[i * 3] = NibbleToChar(temp);

                if ((i + 1) < mac.length) {
                    result[i * 3 + 2] = ':';
                }
            }

            return new String(result);
        } else {
            return "null";
        }
    }

    public PairedBulbAdapter(Context context, List<byte[]> items) {
        super(context, 0, items);
        this.items = items;

    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
    }

    @Override
    public byte[] getItem(int position) {
        // TODO Auto-generated method stub
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item, null);
        }

        String bulbLabel = bytesToString(getItem(position));
        mContext = parent.getContext();

        if (bulbLabel != null) {
            TextView label = (TextView) v.findViewById(R.id.bulbLabel);
//            Button setBulbColorButton = (Button) v.findViewById(R.id.setBulbColorButton);

            Button speakButton = (Button) v.findViewById(R.id.setBulbColorButton);

            if (label != null) {
                label.setText(bulbLabel);
            }

            // TEST
            PackageManager pm = mContext.getPackageManager();
            List<ResolveInfo> activities = pm.queryIntentActivities(
                    new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
            if (activities.size() == 0) {
                speakButton.setEnabled(false);
                speakButton.setText("Recognizer not present");
            }

            speakButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNearestMacAddress = (getItem(position));

                    //SAVE MacAddress in sharedpref
                    //TODO: Is going to cause bug in multiple devies
//                    Log.i("ADDR", "Orig: " + Base64.encodeToString(mNearestMacAddress, Base64.DEFAULT));
//                    Util.writeToSharedPreferences(mContext, mContext.getString(R.string.pref_key),
//                            Base64.encodeToString(mNearestMacAddress, Base64.DEFAULT));
//
//                    String saved = Util.loadSharePreferences(mContext, mContext.getString(R.string.pref_key));
//                    Log.i("ADDR", "Saved: " + saved);


                    startVoiceRecognitionActivity();
                }
            });


            //Original Impl
//            setBulbColorButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    setBlueColor(getItem(position));
//                }
//            });

        }

        return v;
    }

    /**
     * Fire an intent to start the voice recognition activity.
     */
    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Talking to Ilumi...");

        ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            String command = matches.get(0).toLowerCase();
            Log.i("TEST", "command is " + command);
//            if(command.contains("off"))
////                Toast.makeText(mContext, "testing voice integration", Toast.LENGTH_LONG).show();
//                IlumiSDK.sharedManager().turnOff(mNearestMacAddress);

            StringCommandParser commandParser = new StringCommandParser(mNearestMacAddress, command);
            commandParser.parseCommandsOnDevice();

            matches.clear();
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }

}
