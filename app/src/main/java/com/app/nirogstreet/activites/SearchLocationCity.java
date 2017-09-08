package com.app.nirogstreet.activites;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.model.LocSearchModel;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.Methods;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Preeti on 08-09-2017.
 */

public class SearchLocationCity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        View.OnClickListener {
    private List<LocSearchModel> liLocSearchModels = new ArrayList<LocSearchModel>();
    public EditText autoCompleteTextView;
    TextView searchET;
    private String text = "";
    ImageView backImageView;
    private GoogleApiClient mGoogleApiClient1;

    ListView list;
    LocGogAsync locGogAsync = null;
    String res;
    int RESULT_CODE_LOCATION = 2;

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locGogAsync != null && !locGogAsync.isCancelled()) {
            locGogAsync.cancelAsyncTask();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_cities);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchET = (TextView) findViewById(R.id.searchET);
        TypeFaceMethods.setRegularTypeFaceForTextView(searchET, SearchLocationCity.this);
        buildGoogleApiClient();

          /*  findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });*/
        autoCompleteTextView = (EditText) findViewById(R.id.autoCompleteTextView1);
        TypeFaceMethods.setRegularTypeFaceEditText(autoCompleteTextView, SearchLocationCity.this);
        Methods.showSoftKeyboard(autoCompleteTextView, this);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                text = s.toString();
                if (liLocSearchModels != null) {
                    list.setAdapter(null);
                }

                if (locGogAsync != null)
                    locGogAsync.cancel(true);

                locGogAsync = new LocGogAsync();
                locGogAsync.execute();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        list = (ListView) findViewById(R.id.list);

    }

    private void buildGoogleApiClient() {
        mGoogleApiClient1 = new GoogleApiClient.Builder(getApplicationContext()).addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Places.GEO_DATA_API)

                .addApi(Places.PLACE_DETECTION_API).addApi(LocationServices.API).build();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient1.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient1.isConnected()) {
            mGoogleApiClient1.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoCompleteTextView.post(new Runnable() {
            @Override
            public void run() {
                autoCompleteTextView.requestFocus();
                InputMethodManager imgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imgr.showSoftInput(autoCompleteTextView, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    class LocGogAsync extends AsyncTask<Void, Void, Void> {
        int result = 0;
        private String response;
        JSONObject jo;
        HttpURLConnection httpURLConnection;

        public void cancelAsyncTask() {
            if (httpURLConnection != null && !isCancelled()) {
                cancel(true);
                try {
                    httpURLConnection.disconnect();
                    httpURLConnection = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + URLEncoder.encode(text, "UTF-8") + "&key=" + AppUrl.PLACES_API_KEY;
                URL uri = new URL(url);
                Log.d("doin", "" + uri);

                httpURLConnection = (HttpURLConnection) uri.openConnection();
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                Log.v("My Request :: ", url);
                httpURLConnection.connect();
                BufferedReader br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                StringBuilder sb = new StringBuilder();
                String output;

                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }

                res = sb.toString();
                System.out.println("My Response :: " + res.toString());
                Log.e("result=", "" + res.toString());
                //System.out.println("My Response :: " + responseBody.toString());
                jo = new JSONObject(res);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void r) {
            super.onPostExecute(r);
            try {
                if (jo.has("predictions")) {
                    liLocSearchModels = null;
                    liLocSearchModels = new ArrayList<LocSearchModel>();
                    JSONArray ja = jo.getJSONArray("predictions");
                    for (int i = 0; i < ja.length(); i++) {
                        LocSearchModel locSearchModel = new LocSearchModel();
                        JSONObject joP = ja.getJSONObject(i);
                        locSearchModel.setFormattedtext(joP
                                .getString("description"));
                       /* locSearchModel.setLat(joP.getJSONObject("geometry")
                                .getJSONObject("location").getString("lat"));
                        locSearchModel.setLon(joP.getJSONObject("geometry")
                                .getJSONObject("location").getString("lng"));*/
                        locSearchModel.setPlace_id(joP
                                .getString("place_id"));


                     /*   Places.GeoDataApi.getPlaceById(mGoogleApiClient1, joP
                                .getString("place_id"))
                                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                                    @Override
                                    public void onResult(PlaceBuffer places) {
                                        if (places.getStatus().isSuccess()) {
                                            final Place myPlace = places.get(0);
                                            LatLng queriedLocation = myPlace.getLatLng();
                                            Log.v("Latitude is", "" + queriedLocation.latitude);
                                            Log.v("Longitude is", "" + queriedLocation.longitude);
                                        }
                                        places.release();
                                    }
                                });*/
                        liLocSearchModels.add(locSearchModel);

                        /*Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(joP.getJSONObject("geometry")
                                .getJSONObject("location").getDouble("lat"), joP.getJSONObject("geometry")
                                .getJSONObject("location").getDouble("lng"), 1);
*/
                       /* if (addresses != null && addresses.size() > 0) {
                            for (Address adr : addresses) {
                                if (adr.getLocality() != null && adr.getLocality().length() > 0) {
                                    locSearchModel.setmLocality(adr.getLocality());
                                }
                                if (adr.getLocality() != null && adr.getLocality().length() > 0) {
                                    locSearchModel.setAdminArea(adr.getAdminArea());
                                }
                            }
                        }*/
                      /*  String stateName = addresses.get(0).getAddressLine(1);
                        String countryName = addresses.get(0).getAddressLine(2);*/
                    }

                    if (liLocSearchModels != null
                            && liLocSearchModels.size() > 0) {
                        list.setAdapter(new SettingListAdapter(SearchLocationCity.this,
                                liLocSearchModels, ""));
                    }

                }

            } catch (Exception e) {
            }

        }
    }

    public class SettingListAdapter extends BaseAdapter {

        Context mContext;
        List<LocSearchModel> data;
        String s3Url;

        public SettingListAdapter(Context context, List<LocSearchModel> list,
                                  String str) {
            this.mContext = context;
            this.data = list;
            this.s3Url = str;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public class ViewHolder {
            public TextView title;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                // Utilities.log("convert null " + position);
                holder = new SettingListAdapter.ViewHolder();
                convertView = View.inflate(mContext,
                        R.layout.setting_list_row, null);
                holder.title = (TextView) convertView
                        .findViewById(R.id.txtSettingListTitle);

                convertView.setTag(holder);
            } else {
                // Utilities.log("convert not null " + position);
                holder = (SettingListAdapter.ViewHolder) convertView.getTag();

            }
            holder.title.setText(data.get(position).getFormattedtext());
            TypeFaceMethods.setRegularTypeFaceForTextView(holder.title, SearchLocationCity.this);
            holder.title.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Places.GeoDataApi.getPlaceById(mGoogleApiClient1, data.get(position).getPlace_id())
                            .setResultCallback(new ResultCallback<PlaceBuffer>() {
                                @Override
                                public void onResult(PlaceBuffer places) {
                                    if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                        final Place myPlace = places.get(0);
                                        LatLng latLng = myPlace.getLatLng();
                                        String address = myPlace.getAddress().toString();
                                        LocSearchModel locSearchModel = new LocSearchModel();

                                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                        List<Address> addresses = null;
                                        try {
                                            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                            if (addresses != null && addresses.size() > 0) {
                                                for (Address adr : addresses) {
                                                 if(   adr.getPostalCode()!=null&&adr.getPostalCode().length()>0)
                                                 {
                                                     locSearchModel.setPincode(adr.getPostalCode());
                                                 }
                                                    if (adr.getLocality() != null && adr.getLocality().length() > 0) {
                                                        data.get(position).setmLocality(adr.getLocality());
                                                        locSearchModel.setmLocality(adr.getLocality());
                                                    }
                                                    if (adr.getLocality() != null && adr.getLocality().length() > 0) {
                                                        data.get(position).setAdminArea(adr.getAdminArea());
                                                        locSearchModel.setAdminArea(adr.getAdminArea());

                                                    }
                                                }
                                                if (locSearchModel != null && locSearchModel.getmLocality() != null) {


                                                }
                                            } else {


                                            }


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        /*try {
                                            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                                            LocSearchModel locSearchModel = new LocSearchModel();
                                            if (addresses != null && addresses.size() > 0) {
                                                for (Address adr : addresses) {
                                                    if (adr.getLocality() != null && adr.getLocality().length() > 0) {
                                                        data.get(position).setmLocality(adr.getLocality());
                                                        locSearchModel.setmLocality(adr.getLocality());
                                                    }
                                                    if (adr.getLocality() != null && adr.getLocality().length() > 0) {
                                                        data.get(position).setAdminArea(adr.getAdminArea());
                                                        locSearchModel.setAdminArea(adr.getAdminArea());

                                                    }
                                                }
                                                if (locSearchModel != null && locSearchModel.getmLocality() != null) {
                                                    if(RESULT_CODE==1) {
                                                        Intent return_intent = new Intent();
                                                        return_intent.putExtra("city", locSearchModel.getmLocality());
                                                        setResult(1, return_intent);
                                                        finish();
                                                    }
                                                    else {
                                                        Intent return_intent = new Intent();
                                                        return_intent.putExtra("city", locSearchModel.getmLocality());
                                                        setResult(RESULT_CODE, return_intent);
                                                        finish();
                                                    }
                                                } else {
                                                    if (locSearchModel != null && locSearchModel.getAdminArea() != null) {
                                                        {
                                                            if(RESULT_CODE==1) {

                                                                Intent return_intent = new Intent();
                                                                return_intent.putExtra("city", locSearchModel.getAdminArea());
                                                                setResult(RESULT_CODE, return_intent);
                                                                finish();
                                                            }
                                                            else {
                                                                Intent return_intent = new Intent();
                                                                return_intent.putExtra("city", locSearchModel.getAdminArea());
                                                                setResult(RESULT_CODE, return_intent);
                                                                finish();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                           // Log.i(TAG, "Place found: " + myPlace.getName());

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }*/
                                        if (latLng != null) {
                                            Methods.hideKeyboardOfView(autoCompleteTextView, SearchLocationCity.this);
                                            Intent return_intent = new Intent();
                                            if (locSearchModel.getmLocality() != null)
                                                return_intent.putExtra("city", locSearchModel.getmLocality());

                                            else
                                                return_intent.putExtra("city", locSearchModel.getAdminArea());
                                            if (locSearchModel.getPincode() != null) {
                                                return_intent.putExtra("pincode", locSearchModel.getPincode());

                                            }

                                            return_intent.putExtra("longitude", String.valueOf(latLng.longitude));
                                            return_intent.putExtra("latitude", String.valueOf(latLng.latitude));
                                            return_intent.putExtra("address", data.get(position).getFormattedtext());
                                            System.out.print(latLng.latitude);
                                            setResult(RESULT_CODE_LOCATION, return_intent);
                                            finish();
                                        }
                                    } else {
                                        //  Log.e(TAG, "Place not found");
                                    }
                                    places.release();
                                }
                            });


                }
            });

            return convertView;
        }
    }
}
