package co.ilumi.ilumiexample.model.services;

import android.util.Log;

import java.io.IOException;

import co.ilumi.ilumiexample.utils.HeyIlumiControlMode;
import co.ilumi.ilumiexample.utils.StringCommandParser;
import co.ilumi.ilumiexample.model.data.Entities;
import co.ilumi.ilumiexample.model.data.IlumiNLPResponseJSON;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by ruby_ on 4/17/16.
 */
public class HeyIlumiServices {
    public static final String API_URL = "https://api.wit.ai";
    StringCommandParser parser;
//    byte[] mMacAddress;

//    private HashMap<HeyIlumiControlMode, String> mBulbSetting;

    public HeyIlumiServices(StringCommandParser pParser) {
//        mMacAddress = pMacAddress;
        parser = pParser;
//        mMacAddress = Base64.decode(Util.loadSharePreferences(mContext, mContext.getString(R.string.pref_key)), Base64.DEFAULT);

    }

    public interface HeyIlumi {
        @Headers({"Authorization: Bearer GLXMVKBSLSREEVOS4T2IZR7GDNXGLFUZ"})
        @GET("/message?v=20160417")
        Call<IlumiNLPResponseJSON> getNLPResponseJSON(@Query("q") String queryCommends);
    }

    public void callIlumiServices(String pQueryCommands) throws IOException {
        // Create a very simple REST adapter which points the GitHub API.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of our HeyIlumi API interface.
        HeyIlumi heyIlumi = retrofit.create(HeyIlumi.class);

        // Create a call instance for looking up Retrofit contributors.
        Call<IlumiNLPResponseJSON> call = heyIlumi.getNLPResponseJSON(pQueryCommands);

        call.enqueue(new Callback<IlumiNLPResponseJSON>() {
            @Override
            public void onResponse(Call<IlumiNLPResponseJSON> call, Response<IlumiNLPResponseJSON> response) {
                // http response status code + headers
                System.out.println("Response status code: " + response.code());

                // isSuccess is true if response code => 200 and <= 300
                if (!response.isSuccessful()) {
                    // print response body if unsuccessful
                    try {
                        Log.d("Test", response.errorBody().string());
                    } catch (IOException e) {
                        // do nothing
                    }
                    return;
                }

                // if parsing the JSON body failed, `response.body()` returns null
                IlumiNLPResponseJSON decodedResponse = response.body();
                if (decodedResponse == null) return;

                // at this point the JSON body has been successfully parsed
                //TODO: Quick and Dirty implementation of bulb behavior, this part of code should be incapsulated in seperated class
                Log.i("TEST", "Response (contains request infos):");
                Log.i("TEST", "- misg_id:         " + decodedResponse.getMsgId());
                Log.i("TEST", "- _text:          " + decodedResponse.getText());
                Entities entities = decodedResponse.getOutcomes().get(0).getEntities();
                if(!entities.getColor().isEmpty()) {
                    Log.i("TEST", "Color:       " + entities.getColor().get(0).getValue());
                    parser.updateSettings(HeyIlumiControlMode.SET_COLOR_MODE, entities.getColor().get(0).getValue());
                }
//                else
//                    parser.updateSettings(HeyIlumiControlMode.SET_COLOR_MODE, "0");

                if (!entities.getSentiment().isEmpty()) {
                    Log.i("TEST", "Sentiment:       " + entities.getSentiment().get(0).getValue());
                    parser.updateSettings(HeyIlumiControlMode.SET_SENTIMENT_MODE, entities.getSentiment().get(0).getValue());
                }
//                else
//                    parser.updateSettings(HeyIlumiControlMode.SET_SENTIMENT_MODE, null);

//                //Change the bulb behavior according to the heyilumi response
                parser.updateBulb();
//
                //TODO: release resources
//                mBulbSetting.clear();
//                mBulbSetting = null;
            }

            @Override
            public void onFailure(Call<IlumiNLPResponseJSON> call, Throwable t) {
                Log.d("ERROR_RETROFIT", "onFailure");
                Log.d("ERROR_RETROFIT",t.getMessage());
            }
        });
    }

//    private void updateBulb(){
//        if(mBulbSetting.isEmpty())
//            flashRed();//Indicate HeyIlumi couldn't detect the instruction
//        else {
//            if(mBulbSetting.containsKey(HeyIlumiControlMode.SET_COLOR_MODE)) {
//                //TODO: Set color based on sentiment
//                Log.i("TEST", "DEBUG 1" + mMacAddress.toString());
//                if(mBulbSetting.containsKey(HeyIlumiControlMode.SET_SENTIMENT_MODE) && (mBulbSetting.get(HeyIlumiControlMode.SET_SENTIMENT_MODE) == Util.COMMAND_KEYWORD_HATE)) {
//                    //TODO: Set any color except for the color mode
//
//                } else {
//                    Log.i("TEST", "DEBUG 2" + mMacAddress.toString());
//                    Log.i("TEST", "The stored color is: " + mBulbSetting.get(HeyIlumiControlMode.SET_COLOR_MODE));
//                    setColor(mBulbSetting.get(HeyIlumiControlMode.SET_COLOR_MODE));
//                }
//
//            }
//
//        }
//    }




}
