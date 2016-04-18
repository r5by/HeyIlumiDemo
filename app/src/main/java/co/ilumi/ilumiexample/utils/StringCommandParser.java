package co.ilumi.ilumiexample.utils;

import android.util.Log;

import com.ilumi.sdk.IlumiSDK;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import co.ilumi.ilumiexample.model.services.HeyIlumiServices;
import co.ilumi.ilumiexample.utils.HeyIlumiControlMode;
import co.ilumi.ilumiexample.utils.Util;

/**
 * Created by ruby_ on 4/16/16.
 *
 * Quick and dirty impliment a device controler, note it should be responsible for only early-processing
 * of human(google) voice input, and the actual light bulb control should be put into a seperate class!
 */
public class StringCommandParser {

    HashSet<String> inputCommandSet;
    String mInputCommandString;

    private byte[] mMacAddress;

    private HashMap<HeyIlumiControlMode, String> mBulbSetting = new HashMap<>();

    //TODO: FALSE by default, should support "Hey Illumi" Feature (commend to initial online NLP support)
    boolean takeSettingCommandFlag = true;

    public StringCommandParser(byte[] pMacAddress, String pCommandString) {
        mMacAddress = pMacAddress;
        mInputCommandString = pCommandString;

        String[] commandsLine = pCommandString.split(" ");
        inputCommandSet = new HashSet<String>(Arrays.asList(commandsLine));
    }

    public void parseCommandsOnDevice() {
        Log.i("TEST", "TEST COMMAND IS" + inputCommandSet.toString());
        //Initialize the device controlled

        //TODO: save/get paired ilumi devices in shared pref
//        mMacAddress = Base64.decode(Util.loadSharePreferences(mContext, mContext.getString(R.string.pref_key)), Base64.DEFAULT);
//        loadSharedPrefs(mContext.getString(R.string.pref_key));


        //TODO: Fat client side due to time limited to demonstrate some online functions
        //Note that only turn on/off or "hey ilumi" should be in here...
        if (inputCommandSet.contains(Util.COMMAND_OFF)
                || inputCommandSet.contains(Util.COMMAND_SLEEP)
                || inputCommandSet.contains(Util.COMMAND_DOWN))
            turnOffIlumi();
        else if (inputCommandSet.contains(Util.COMMAND_ON)
                || inputCommandSet.contains(Util.COMMAND_START)
                || inputCommandSet.contains(Util.COMMAND_BEGIN)
                || inputCommandSet.contains(Util.COMMAND_UP)
                || inputCommandSet.contains(Util.COMMAND_WAKE)
                || inputCommandSet.contains(Util.COMMAND_OPEN))
            turnOnIlumi();
        else if (inputCommandSet.contains(Util.COMMAND_KEYWORD_CANDLE)
                || inputCommandSet.contains(Util.COMMAND_KEYWORD_CANDLELIGHT)
                || inputCommandSet.contains(Util.COMMAND_KEYWORD_ROMANTIC)
                || inputCommandSet.contains(Util.COMMAND_KEYWORD_CUTE)
                || inputCommandSet.contains(Util.COMMMAN_KEYWORD_BLINKING))
            setCandle();
        else if (inputCommandSet.contains(Util.COMMAND_HEY))
            heyIllumi();
        else if (takeSettingCommandFlag == true && canBeInterpreted())
            callIllumi();
        else
            blinkNo();
    }


    /**
     * Update the setting set of device, get values from NLP server
     *
     * @param mode
     * @param value
     */
    public void updateSettings(HeyIlumiControlMode mode, String value) {
//        mBulbSetting.put(mode, "0");
        mBulbSetting.put(mode, value);
    }

    /**
     * updateBulb(), the logic unit of processing NLP feedbacks, these functions can be put into a different module
     */
    public void updateBulb() {
        if (mBulbSetting.isEmpty())
            blinkNo();//Indicate HeyIlumi couldn't detect the instruction
        else {
            if (mBulbSetting.containsKey(HeyIlumiControlMode.SET_SENTIMENT_MODE) && (mBulbSetting.get(HeyIlumiControlMode.SET_SENTIMENT_MODE) == Util.COMMAND_KEYWORD_HATE)) {
                //TODO: Set any color except for the color mode (use key word hate), require server NLP
                Log.i("TEST", "DEBUG SENTIMent" + mMacAddress.toString());
                setRandomColor();
            } else if (mBulbSetting.containsKey(HeyIlumiControlMode.SET_COLOR_MODE)) {
                //TODO: Set color based on sentiment, require server NLP
//                Log.i("TEST", "DEBUG 1" + mMacAddress.toString());
//                Log.i("TEST", "DEBUG 2" + mMacAddress.toString());
//                Log.i("TEST", "The stored color is: " + mBulbSetting.get(HeyIlumiControlMode.SET_COLOR_MODE));
                setColor(mBulbSetting.get(HeyIlumiControlMode.SET_COLOR_MODE));

                //TODO: Much more can be done here... to let the bulb light be amazing...
            }
        }
    }


    //--------------------------
    // Control light bulb: Move these functions to a different module!
    //--------------------------

    private void turnOnIlumi() {
        IlumiSDK.sharedManager().turnOn(mMacAddress);
    }

    private void turnOffIlumi() {
        IlumiSDK.sharedManager().turnOff(mMacAddress);
    }

    /**
     * HeyIllumi: user says "hey..." and set the color scheme of the ilumi
     */
    private void heyIllumi() {
        blinkYes();
        if (takeSettingCommandFlag == false)
            takeSettingCommandFlag = true;
    }

    /**
     * HTTP Request to Illumi NLP server
     */
    private void callIllumi() {
        Log.i("TEST", "Calling Illumi...");
        try {
            new HeyIlumiServices(this).callIlumiServices(mInputCommandString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO: reset the flag
    }

    /**
     * Blink
     */
    private void blinkYes() {
        //Give a flash of colors to indicate changing colors is on
        IlumiSDK.IlumiColor blinkColor = new IlumiSDK.IlumiColor(0, 0xFF, 0, 0, 0xFF);//Blink on green
        IlumiSDK.sharedManager().blinkWithColor(mMacAddress, blinkColor, 0.5f, 2);
    }

    private void blinkNo() {
        IlumiSDK.IlumiColor blinkColor = new IlumiSDK.IlumiColor(0xFF, 0, 0, 0, 0xFF);
        ;//Blink on green
        IlumiSDK.sharedManager().blinkWithColor(mMacAddress, blinkColor, 0.5f, 1);
    }

    private boolean canBeInterpreted() {
        //TODO: Add environment support here
        return (mInputCommandString.contains(Util.COMMAND_KEYWORD_COLOR));
    }


    /**
     * Currently support red/green/blue from NLP online services
     *
     * @param pColor
     */
    private void setColor(String pColor) {

        IlumiSDK.IlumiColor color = colorGenerator(pColor);
        if (color != null)
            IlumiSDK.sharedManager().setColor(mMacAddress, color);
    }

    //TODO: Suppose to detect certain color range except for a specific given RPG
    private void setRandomColor() {

        //TODO: use sdk support to pool a random color ... just for demo... easy to impl anyways...
        IlumiSDK.sharedManager().setRandomColor(mMacAddress);
    }

    private IlumiSDK.IlumiColor colorGenerator(String colorName) {
        IlumiSDK.IlumiColor color = null;
        if (colorName.equals(Util.COMMAND_KEYWORD_RED))
            color = new IlumiSDK.IlumiColor(0xFF, 0, 0, 0, 0xFF);
        else if (colorName.equals(Util.COMMAND_KEYWORD_BLUE))
            color = new IlumiSDK.IlumiColor(0, 0, 0xFF, 0, 0xFF);
        else if (colorName.equals(Util.COMMAND_KEYWORD_GREEN))
            color = new IlumiSDK.IlumiColor(0, 0xFF, 0, 0, 0xFF);
        else {
            //Default white
            Log.i("TEST", "Debug 3 " + mMacAddress.toString());
            color = new IlumiSDK.IlumiColor(0xFF, 0xFF, 0xFF, 0x00, 0xFF);
        }

        return color;
    }

    //TODO: quick demo of candle/romantic feature, however should supported by NLP from server side
    private void setCandle() {
        IlumiSDK.IlumiColor candleColor = new IlumiSDK.IlumiColor(0x90, 0x80, 0, 0, 0x80);
        IlumiSDK.sharedManager().setCandleModeWithColor(mMacAddress, candleColor);
    }


//END
}

