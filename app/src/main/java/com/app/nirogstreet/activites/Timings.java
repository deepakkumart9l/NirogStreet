package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.materialdaterangepicker.time.RadialPickerLayout;
import com.app.nirogstreet.materialdaterangepicker.time.TimePickerDialog;
import com.app.nirogstreet.model.ClinicDetailModel;
import com.app.nirogstreet.model.SesstionModel;
import com.app.nirogstreet.model.TimingsModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.parser.Clinic_Detail_Parser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.net.ssl.SSLContext;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by Preeti on 27-09-2017.
 */

public class Timings extends Activity {
    private RecyclerView recyclerView;
    ImageView backImageView;
    TextView sentTv;
    UpdateProfileEditAsyncTask updateProfileEditAsyncTask;
    UpdateProfileAsyncTask updateProfileAsyncTask;
    SesstionManager sesstionManager;
    private LinearLayoutManager linearLayoutManager;
    ArrayList<TimingsModel> timingsModels1 = new ArrayList<>();
    ClinicDetailModel clinicDetailModel;
    CircularProgressBar circularProgressBar;
    Switch switch_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lst);
        switch_button = (Switch) findViewById(R.id.switch_button);

        sesstionManager = new SesstionManager(Timings.this);
        if (getIntent().hasExtra("ClinicModel")) {
            clinicDetailModel = (ClinicDetailModel) getIntent().getSerializableExtra("ClinicModel");
            timingsModels1 = clinicDetailModel.getTimingsModels();
        }
        sentTv = (TextView) findViewById(R.id.sentTv);
        sentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    if (switch_button.isChecked()) {
                        ArrayList<TimingsModel> timingsModels = new ArrayList<TimingsModel>();

                        timingsModels.add(timingsModels1.get(0));
                        timingsModels.add(timingsModels1.get(0));
                        timingsModels.add(timingsModels1.get(0));

                        timingsModels.add(timingsModels1.get(0));
                        timingsModels.add(timingsModels1.get(0));
                        timingsModels.add(timingsModels1.get(1));
                        timingsModels.add(timingsModels1.get(2));
                        clinicDetailModel.setTimingsModels(timingsModels);

                    }else {
                        clinicDetailModel.setTimingsModels(timingsModels1);

                    }
                    if (NetworkUtill.isNetworkAvailable(Timings.this)) {

                        if (clinicDetailModel.getClinic_docID() != null && !clinicDetailModel.getClinic_docID().equalsIgnoreCase("")) {
                            updateProfileEditAsyncTask = new UpdateProfileEditAsyncTask(0);
                            updateProfileEditAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                        } else {
                            updateProfileAsyncTask = new UpdateProfileAsyncTask(0);
                            updateProfileAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    } else {
                        NetworkUtill.showNoInternetDialog(Timings.this);
                    }
                }
            }
        });
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(Timings.this);
        //  recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (clinicDetailModel != null && clinicDetailModel.getTimingsModels() != null && clinicDetailModel.getTimingsModels().size() > 0) {
            timingsModels1 = clinicDetailModel.getTimingsModels();
        } else {

            timingsModels1.add(new TimingsModel("Mon", new SesstionModel("", "", "", "", false), new SesstionModel("", "", "", "", false)));
            timingsModels1.add(new TimingsModel("Tue", new SesstionModel("", "", "", "", false), new SesstionModel("", "", "", "", false)));

            timingsModels1.add(new TimingsModel("Wed", new SesstionModel("", "", "", "", false), new SesstionModel("", "", "", "", false)));

            timingsModels1.add(new TimingsModel("Thu", new SesstionModel("", "", "", "", false), new SesstionModel("", "", "", "", false)));

            timingsModels1.add(new TimingsModel("Fri", new SesstionModel("", "", "", "", false), new SesstionModel("", "", "", "", false)));

            timingsModels1.add(new TimingsModel("Sat", new SesstionModel("", "", "", "", false), new SesstionModel("", "", "", "", false)));
            timingsModels1.add(new TimingsModel("Sun", new SesstionModel("", "", "", "", false), new SesstionModel("", "", "", "", false)));
        }
        TimingsAdapter timingsAdapter = new TimingsAdapter(timingsModels1, Timings.this, Timings.this);
        recyclerView.setAdapter(timingsAdapter);
        switch_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SesstionModel sesstionModelsat1 = new SesstionModel("", "", "", "", false);
                    SesstionModel sesstionModelsat2 = new SesstionModel("", "", "", "", false);
                    SesstionModel sesstionModelsun1 = new SesstionModel("", "", "", "", false);
                    SesstionModel sesstionModelsun2 = new SesstionModel("", "", "", "", false);
                    if (timingsModels1.get(5).getSesstionModel1().isAvailable() || timingsModels1.get(5).getSesstionModel2().isAvailable()) {
                        sesstionModelsat1 = timingsModels1.get(5).getSesstionModel1();
                        sesstionModelsat2 = timingsModels1.get(5).getSesstionModel2();
                    }
                    if (timingsModels1.get(6).getSesstionModel1().isAvailable() || timingsModels1.get(6).getSesstionModel2().isAvailable()) {
                        sesstionModelsun1 = timingsModels1.get(6).getSesstionModel1();
                        sesstionModelsun2 = timingsModels1.get(6).getSesstionModel2();
                    }
                    if (timingsModels1.get(0).getSesstionModel1().isAvailable() || timingsModels1.get(0).getSesstionModel2().isAvailable()) {
                        SesstionModel sesstionModel1 = timingsModels1.get(0).getSesstionModel1();
                        SesstionModel sesstionModel2 = timingsModels1.get(0).getSesstionModel2();
                        timingsModels1.clear();
                        timingsModels1.add(new TimingsModel("Mon-Fri", sesstionModel1, sesstionModel2));


                    } else {
                        timingsModels1.clear();

                        timingsModels1.add(new TimingsModel("Mon-Fri", new SesstionModel("", "", "", "", false), new SesstionModel("", "", "", "", false)));


                    }

                    timingsModels1.add(new TimingsModel("Sat", sesstionModelsat1, sesstionModelsat2));
                    timingsModels1.add(new TimingsModel("Sun", sesstionModelsun1, sesstionModelsun2));
                    TimingsAdapter timingsAdapter = new TimingsAdapter(timingsModels1, Timings.this, Timings.this);
                    recyclerView.setAdapter(timingsAdapter);
                } else {
                    SesstionModel sesstionModelsat1 = new SesstionModel("", "", "", "", false);
                    SesstionModel sesstionModelsat2 = new SesstionModel("", "", "", "", false);
                    SesstionModel sesstionModelsun1 = new SesstionModel("", "", "", "", false);
                    SesstionModel sesstionModelsun2 = new SesstionModel("", "", "", "", false);
                    if (timingsModels1.get(1).getSesstionModel1().isAvailable() || timingsModels1.get(1).getSesstionModel2().isAvailable()) {
                        sesstionModelsat1 = timingsModels1.get(1).getSesstionModel1();
                        sesstionModelsat2 = timingsModels1.get(1).getSesstionModel2();
                    }
                    if (timingsModels1.get(2).getSesstionModel1().isAvailable() || timingsModels1.get(2).getSesstionModel2().isAvailable()) {
                        sesstionModelsun1 = timingsModels1.get(2).getSesstionModel1();
                        sesstionModelsun2 = timingsModels1.get(2).getSesstionModel2();
                    }
                    if (timingsModels1.get(0).getSesstionModel1().isAvailable() || timingsModels1.get(0).getSesstionModel2().isAvailable()) {
                        SesstionModel sesstionModel1 = timingsModels1.get(0).getSesstionModel1();
                        SesstionModel sesstionModel2 = timingsModels1.get(0).getSesstionModel2();
                        timingsModels1.clear();
                        timingsModels1.add(new TimingsModel("Mon", sesstionModel1, sesstionModel2));
                        timingsModels1.add(new TimingsModel("Tue", sesstionModel1, sesstionModel2));

                        timingsModels1.add(new TimingsModel("Wed", sesstionModel1, sesstionModel2));

                        timingsModels1.add(new TimingsModel("Thu", sesstionModel1, sesstionModel2));

                        timingsModels1.add(new TimingsModel("Fri", sesstionModel1, sesstionModel2));

                    } else {
                        timingsModels1.clear();
                        timingsModels1.add(new TimingsModel("Mon", new SesstionModel("", "", "", "", false), new SesstionModel("", "", "", "", false)));
                        timingsModels1.add(new TimingsModel("Tue", new SesstionModel("", "", "", "", false), new SesstionModel("", "", "", "", false)));

                        timingsModels1.add(new TimingsModel("Wed", new SesstionModel("", "", "", "", false), new SesstionModel("", "", "", "", false)));

                        timingsModels1.add(new TimingsModel("Thu", new SesstionModel("", "", "", "", false), new SesstionModel("", "", "", "", false)));

                        timingsModels1.add(new TimingsModel("Fri", new SesstionModel("", "", "", "", false), new SesstionModel("", "", "", "", false)));


                    }
                    timingsModels1.add(new TimingsModel("Sat", sesstionModelsat1, sesstionModelsat2));
                    timingsModels1.add(new TimingsModel("Sun", sesstionModelsun1, sesstionModelsun2));
                    TimingsAdapter timingsAdapter = new TimingsAdapter(timingsModels1, Timings.this, Timings.this);
                    recyclerView.setAdapter(timingsAdapter);
                }
            }
        });
    }

    public class TimingsAdapter extends
            RecyclerView.Adapter<TimingsAdapter.MyHolderView> {
        ArrayList<TimingsModel> timingsModels;
        Context context;
        Activity activity;
        int pos;

        public TimingsAdapter(ArrayList<TimingsModel> timingsModels, Context context, Activity activity) {
            this.timingsModels = timingsModels;
            this.context = context;
            this.activity = activity;
        }

        @Override
        public MyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lst_item, null);
            MyHolderView viewHolder = new MyHolderView(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final MyHolderView holder, final int position) {
            holder.dayTextView.setText(timingsModels.get(position).getDay());
            final boolean[] isFirstTime = {false};
            if (!timingsModels.get(position).getSesstionModel1().getStartTime().equalsIgnoreCase("") && !timingsModels.get(position).getSesstionModel1().getEndTime().equalsIgnoreCase("")) {
                holder.addTextView.setVisibility(View.GONE);
                holder.firstSesstion.setVisibility(View.VISIBLE);
                holder.firstSesstionLayout.setVisibility(View.VISIBLE);
                holder.view.setVisibility(View.VISIBLE);
                holder.secondSesstionLayout.setVisibility(View.VISIBLE);
                holder.secondSesstion.setVisibility(View.VISIBLE);

                holder.firstSesstion.setText(amPmFromat(timingsModels.get(position).getSesstionModel1().getStartTime()) + " - " + amPmFromat(timingsModels.get(position).getSesstionModel1().getEndTime()));
            }
            if (!timingsModels.get(position).getSesstionModel2().getStartTime().equalsIgnoreCase("") && !timingsModels.get(position).getSesstionModel2().getEndTime().equalsIgnoreCase("")) {
                holder.secondSesstion.setText(amPmFromat(timingsModels.get(position).getSesstionModel2().getStartTime()) + " - " + amPmFromat(timingsModels.get(position).getSesstionModel2().getEndTime()));

            }
            holder.addTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.secondSessionCancel.setVisibility(View.VISIBLE);
                    isFirstTime[0] = true;
                    Calendar now = Calendar.getInstance();
                    TimePickerDialog tpd;

                    tpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                                                           @Override
                                                           public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd, TextView textView, int amOrpmstart, int amorpmEnd) {
                                                               holder.addTextView.setVisibility(View.GONE);
                                                               holder.firstSesstion.setVisibility(View.VISIBLE);
                                                               holder.view.setVisibility(View.VISIBLE);
                                                               holder.firstSesstionLayout.setVisibility(View.VISIBLE);
                                                               holder.secondSesstionLayout.setVisibility(View.VISIBLE);
                                                               holder.secondSesstion.setVisibility(View.VISIBLE);
                                                               String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                                                               String minuteString = minute < 10 ? "0" + minute : "" + minute;
                                                               String hourStringEnd = hourOfDayEnd < 10 ? "0" + hourOfDayEnd : "" + hourOfDayEnd;
                                                               String minuteStringEnd = minuteEnd < 10 ? "0" + minuteEnd : "" + minuteEnd;
                                                               String start, end;
                                                               if (amOrpmstart == 0) {
                                                                   start = " Am";
                                                               } else {
                                                                   start = " Pm";

                                                               }
                                                               if (amorpmEnd == 0) {
                                                                   end = " Am";
                                                               } else {
                                                                   end = " Pm";

                                                               }
                                                               String _24HourFormat = hourString + ":" + minuteString + hourStringEnd + ":" + minuteStringEnd;
                                                               String time = amPmFromat(hourString + ":" + minuteString) + "  - " + amPmFromat(hourStringEnd + ":" + minuteStringEnd);
                                                               if (isFirstTime[0]) {
                                                                   holder.firstSesstion.setText(time);
                                                                   timingsModels1.get(position).setSesstionModel1(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString, "", "1", true));
                                                                   timingsModels.get(position).setSesstionModel1(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString, "", "1", true));

                                                               } else {
                                                                   holder.secondSesstion.setText(time);
                                                                   timingsModels1.get(position).setSesstionModel2(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString, "", "2", true));
                                                                   timingsModels.get(position).setSesstionModel2(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString, "", "2", true));

                                                               }

                                                           }
                                                       },
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            false, false, "", "", true
                    );
                    tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            Log.d("TimePicker", "Dialog was cancelled");
                        }
                    });
                    tpd.show(activity.getFragmentManager(), "Timepickerdialog");

                }
            });
            holder.secondSesstion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isFirstTime[0] = false;
                    String previousStartTime = timingsModels.get(position).getSesstionModel1().getStartTime();
                    String previousEndTime = timingsModels.get(position).getSesstionModel1().getEndTime();

                    Calendar now = Calendar.getInstance();
                    TimePickerDialog tpd = TimePickerDialog.newInstance(
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd, TextView textView, int amOrpmstart, int amorpmEnd) {
                                    holder.addTextView.setVisibility(View.GONE);
                                    holder.view.setVisibility(View.VISIBLE);
                                    holder.firstSesstionLayout.setVisibility(View.VISIBLE);
                                    holder.secondSesstionLayout.setVisibility(View.VISIBLE);
                                    holder.firstSesstion.setVisibility(View.VISIBLE);
                                    holder.secondSesstion.setVisibility(View.VISIBLE);
                                    String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                                    String minuteString = minute < 10 ? "0" + minute : "" + minute;
                                    String hourStringEnd = hourOfDayEnd < 10 ? "0" + hourOfDayEnd : "" + hourOfDayEnd;
                                    String minuteStringEnd = minuteEnd < 10 ? "0" + minuteEnd : "" + minuteEnd;
                                    String start, end;
                                    if (amOrpmstart == 0) {
                                        start = " Am";
                                    } else {
                                        start = " Pm";

                                    }
                                    if (amorpmEnd == 0) {
                                        end = " Am";
                                    } else {
                                        end = " Pm";

                                    }
                                    String time = amPmFromat(hourString + ":" + minuteString) + "  - " + amPmFromat(hourStringEnd + ":" + minuteStringEnd);
                                    if (isFirstTime[0]) {
                                        holder.firstSesstion.setText(time);
                                        timingsModels.get(position).setSesstionModel1(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString, "", "1", true));
                                        timingsModels1.get(position).setSesstionModel1(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString, "", "1", true));

                                    } else {
                                        holder.secondSesstion.setText(time);
                                        timingsModels1.get(position).setSesstionModel2(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString, "", "2", true));

                                        timingsModels.get(position).setSesstionModel2(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString, "", "2", true));

                                    }
                                }
                            },
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            false, true, previousStartTime, previousEndTime, false
                    );
                    tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            Log.d("TimePicker", "Dialog was cancelled");
                        }
                    });
                    tpd.show(activity.getFragmentManager(), "Timepickerdialog");
                }
            });

            holder.firstSesstion.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    pos = position;
                    isFirstTime[0] = true;
                    boolean val;
                    String previousStartTime = timingsModels.get(position).getSesstionModel2().getStartTime();


                    String previousEndTime = timingsModels.get(position).getSesstionModel2().getEndTime();
                    if (!previousStartTime.equalsIgnoreCase("") && !previousEndTime.equalsIgnoreCase("")) {
                        val = true;
                    } else {
                        val = false;
                    }
                    Calendar now = Calendar.getInstance();
                    // isFirstTime = true;
                    TimePickerDialog tpd = TimePickerDialog.newInstance(
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd, TextView textView, int amOrpmstart, int amorpmEnd) {
                                    holder.addTextView.setVisibility(View.GONE);
                                    holder.firstSesstion.setVisibility(View.VISIBLE);
                                    holder.view.setVisibility(View.VISIBLE);
                                    holder.secondSesstion.setVisibility(View.VISIBLE);
                                    holder.firstSesstionLayout.setVisibility(View.VISIBLE);
                                    holder.secondSesstionLayout.setVisibility(View.VISIBLE);
                                    String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                                    String minuteString = minute < 10 ? "0" + minute : "" + minute;
                                    String hourStringEnd = hourOfDayEnd < 10 ? "0" + hourOfDayEnd : "" + hourOfDayEnd;
                                    String minuteStringEnd = minuteEnd < 10 ? "0" + minuteEnd : "" + minuteEnd;
                                    String start, end;
                                    if (amOrpmstart == 0) {
                                        start = " Am";
                                    } else {
                                        start = " Pm";

                                    }
                                    if (amorpmEnd == 0) {
                                        end = " Am";
                                    } else {
                                        end = " Pm";

                                    }
                                    String time = amPmFromat(hourString + ":" + minuteString) + "  - " + amPmFromat(hourStringEnd + ":" + minuteStringEnd);
                                    if (isFirstTime[0]) {
                                        holder.firstSesstion.setText(time);
                                        timingsModels1.get(position).setSesstionModel1(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString, "", "1", true));

                                        timingsModels.get(position).setSesstionModel1(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString, "", "1", true));
                                    } else {
                                        holder.secondSesstion.setText(time);
                                        timingsModels.get(position).setSesstionModel2(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString, "", "2", true));
                                        timingsModels1.get(position).setSesstionModel2(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString, "", "2", true));

                                    }
                                }
                            },
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            false, val, previousStartTime, previousEndTime, true
                    );
                    tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            Log.d("TimePicker", "Dialog was cancelled");
                        }
                    });
                    tpd.show(activity.getFragmentManager(), "Timepickerdialog");

                }
            });
            holder.firstSessionCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timingsModels.get(position).setSesstionModel1(new SesstionModel("", "", "", "", false));
                    timingsModels1.get(position).setSesstionModel1(new SesstionModel("", "", "", "", false));
                    holder.firstSesstion.setText("Add Session Time");

                    if (!timingsModels.get(position).getSesstionModel1().isAvailable() && !timingsModels.get(position).getSesstionModel2().isAvailable()) {
                        holder.secondSesstionLayout.setVisibility(View.GONE);
                        holder.firstSesstionLayout.setVisibility(View.GONE);
                        holder.addTextView.setVisibility(View.VISIBLE);
                    }
                    //notifyItemChanged(position);
                }
            });
            holder.secondSessionCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    timingsModels.get(position).setSesstionModel1(timingsModels.get(position).getSesstionModel1());
                    timingsModels.get(position).setSesstionModel2(new SesstionModel("", "", "", "", false));
                    timingsModels1.get(position).setSesstionModel1(timingsModels.get(position).getSesstionModel1());

                    timingsModels1.get(position).setSesstionModel2(new SesstionModel("", "", "", "", false));
                    holder.secondSessionCancel.setVisibility(View.GONE);
                    holder.secondSesstion.setText("Add Session Time");
                    if (!timingsModels.get(position).getSesstionModel1().isAvailable() && !timingsModels.get(position).getSesstionModel2().isAvailable()) {
                        holder.addTextView.setVisibility(View.VISIBLE);

                        holder.secondSesstionLayout.setVisibility(View.GONE);
                        holder.firstSesstionLayout.setVisibility(View.GONE);
                    }
                    // notifyItemChanged(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return timingsModels.size();
        }

   /* @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd, TextView textView, int amOrpmstart, int amorpmEnd) {
        addTextView.setVisibility(View.GONE);

        firstSesstion.setVisibility(View.VISIBLE);
        secondSesstion.setVisibility(View.VISIBLE);
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String hourStringEnd = hourOfDayEnd < 10 ? "0" + hourOfDayEnd : "" + hourOfDayEnd;
        String minuteStringEnd = minuteEnd < 10 ? "0" + minuteEnd : "" + minuteEnd;
        String start, end;
        if (amOrpmstart == 0) {
            start = " Am";
        } else {
            start = " Pm";

        }
        if (amorpmEnd == 0) {
            end = " Am";
        } else {
            end = " Pm";

        }
        String time = hourString + ":" + minuteString + start + "  - " + hourStringEnd + ":" + minuteStringEnd + end;
        if (isFirstTime)
            firstSesstion.setText(time);
        else
            secondSesstion.setText(time);
    }
*/

        public class MyHolderView extends RecyclerView.ViewHolder {
            private TextView firstSesstion, secondSesstion;
            TextView addTextView, dayTextView;
            ImageView firstSessionCancel, secondSessionCancel;
            RelativeLayout firstSesstionLayout, secondSesstionLayout;
            View view;

            public MyHolderView(View itemView) {
                super(itemView);
                addTextView = (TextView) itemView.findViewById(R.id.add);
                firstSessionCancel = (ImageView) itemView.findViewById(R.id.firstSessionCancel);
                secondSessionCancel = (ImageView) itemView.findViewById(R.id.secondSesstioncancel);
                dayTextView = (TextView) itemView.findViewById(R.id.day);
                secondSesstionLayout = (RelativeLayout) itemView.findViewById(R.id.secondSesstionLayout);
                firstSesstionLayout = (RelativeLayout) itemView.findViewById(R.id.firstSesstionLayout);
                secondSesstion = (TextView) itemView.findViewById(R.id.secondSesstion);
                firstSesstion = (TextView) itemView.findViewById(R.id.firstSesstion);
                view = (View) itemView.findViewById(R.id.view);


            }
        }
    }

    private boolean validate() {
        boolean check = false;
        for (int i = 0; i < timingsModels1.size(); i++) {
            if (timingsModels1.get(i).getSesstionModel1().isAvailable() || timingsModels1.get(i).getSesstionModel2().isAvailable()) {
                check = true;
                return check;
            }
        }
        return false;
    }

    public String amPmFromat(String str) {
        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
        Date _24HourDt = null;
        try {


            _24HourDt = _24HourSDF.parse(str);
            System.out.println(_24HourDt);
            System.out.println(_12HourSDF.format(_24HourDt).toString());
        } catch (Exception e) {
            e.printStackTrace();

        }
        return _12HourSDF.format(_24HourDt).toString();
    }

    public class UpdateProfileAsyncTask extends AsyncTask<Void, Void, Void> {
        String responseBody;
        CircularProgressBar bar;
        //PlayServiceHelper regId;
        JSONObject jo;
        int pos;
        HttpClient client;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public UpdateProfileAsyncTask(int pos) {

            this.pos = pos;
        }

        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.AppBaseUrl + "user/add-clinic";
                SSLSocketFactory sf = new SSLSocketFactory(
                        SSLContext.getDefault(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme sch = new Scheme("https", 443, sf);
                client = new DefaultHttpClient();

                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;

                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder
                        .create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                entityBuilder.addTextBody("Content-Type", "applicaion/json");
                entityBuilder.addTextBody("userID", sesstionManager.getUserDetails().get(SesstionManager.USER_ID));
                entityBuilder.addTextBody(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST);
                if (clinicDetailModel != null) {
                    if (clinicDetailModel.getId() != null) {
                        entityBuilder.addTextBody("ClinicProfile[id]", clinicDetailModel.getId());
                    } else {
                        entityBuilder.addTextBody("ClinicProfile[city]", clinicDetailModel.getCity());
                        entityBuilder.addTextBody("ClinicProfile[pincode]", clinicDetailModel.getPincode());
                        entityBuilder.addTextBody("ClinicProfile[state]", clinicDetailModel.getState());
                        entityBuilder.addTextBody("ClinicProfile[clinic_name]", clinicDetailModel.getName());
                        entityBuilder.addTextBody("ClinicProfile[locality]", clinicDetailModel.getLocality());


                        entityBuilder.addTextBody("ClinicProfile[address]", clinicDetailModel.getAddress());
                        if (clinicDetailModel.getServicesModels() != null && clinicDetailModel.getServicesModels().size() > 0) {
                            for (int i = 0; i < clinicDetailModel.getServicesModels().size(); i++) {
                                entityBuilder.addTextBody("ClinicServices[name][" + i + "]", clinicDetailModel.getServicesModels().get(i).getSpecializationName());
                                if (clinicDetailModel.getServicesModels().get(i).getId() != null)
                                    entityBuilder.addTextBody("ClinicServices[id][" + i + "]", clinicDetailModel.getServicesModels().get(i).getId());
                                else
                                    entityBuilder.addTextBody("ClinicServices[id][" + i + "]", "");


                            }
                        }
                    }
                    if (clinicDetailModel.getTimingsModels() != null) {
                        for (int l = 0; l < clinicDetailModel.getTimingsModels().size(); l++) {
                            if (l == 0) {
                                if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().isAvailable()) {
                                    entityBuilder.addTextBody("Timing[" + 0 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getStartTime());
                                    entityBuilder.addTextBody("Timing[" + 0 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getEndTime());
                                    entityBuilder.addTextBody("Timing[" + 0 + "][day]", l + 1 + "");
                                    entityBuilder.addTextBody("Timing[" + 0 + "][session]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getSesstion());
                                    entityBuilder.addTextBody("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee());

                                }
                                if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().isAvailable()) {

                                    entityBuilder.addTextBody("Timing[" + 1 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getStartTime());
                                    entityBuilder.addTextBody("Timing[" + 1 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getEndTime());
                                    entityBuilder.addTextBody("Timing[" + 1 + "][day]", l + 1 + "");
                                    entityBuilder.addTextBody("Timing[" + 1 + "][session]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getSesstion());
                                    entityBuilder.addTextBody("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee());


                                }
                            } else if (l == 1) {
                                if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().isAvailable()) {
                                    entityBuilder.addTextBody("Timing[" + 2 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getStartTime());
                                    entityBuilder.addTextBody("Timing[" + 2 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getEndTime());
                                    entityBuilder.addTextBody("Timing[" + 2 + "][day]", l + 1 + "");
                                    entityBuilder.addTextBody("Timing[" + 2 + "][session]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getSesstion());
                                    entityBuilder.addTextBody("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee());

                                }
                                if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().isAvailable()) {

                                    entityBuilder.addTextBody("Timing[" + 3 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getStartTime());
                                    entityBuilder.addTextBody("Timing[" + 3 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getEndTime());
                                    entityBuilder.addTextBody("Timing[" + 3 + "][day]", l + 1 + "");
                                    entityBuilder.addTextBody("Timing[" + 3 + "][session]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getSesstion());
                                    entityBuilder.addTextBody("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee());


                                }
                            } else if (l == 2) {
                                if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().isAvailable()) {
                                    entityBuilder.addTextBody("Timing[" + 4 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getStartTime());
                                    entityBuilder.addTextBody("Timing[" + 4 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getEndTime());
                                    entityBuilder.addTextBody("Timing[" + 4 + "][day]", l + 1 + "");
                                    entityBuilder.addTextBody("Timing[" + 4 + "][session]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getSesstion());
                                    entityBuilder.addTextBody("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee());

                                }
                                if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().isAvailable()) {

                                    entityBuilder.addTextBody("Timing[" + 5 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getStartTime());
                                    entityBuilder.addTextBody("Timing[" + 5 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getEndTime());
                                    entityBuilder.addTextBody("Timing[" + 5 + "][day]", l + 1 + "");
                                    entityBuilder.addTextBody("Timing[" + 5 + "][session]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getSesstion());
                                    entityBuilder.addTextBody("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee());


                                }

                            } else if (l == 3) {
                                if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().isAvailable()) {
                                    entityBuilder.addTextBody("Timing[" + 6 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getStartTime());
                                    entityBuilder.addTextBody("Timing[" + 6 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getEndTime());
                                    entityBuilder.addTextBody("Timing[" + 6 + "][day]", l + 1 + "");
                                    entityBuilder.addTextBody("Timing[" + 6 + "][session]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getSesstion());

                                    entityBuilder.addTextBody("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee());

                                }
                                if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().isAvailable()) {

                                    entityBuilder.addTextBody("Timing[" + 7 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getStartTime());
                                    entityBuilder.addTextBody("Timing[" + 7 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getEndTime());
                                    entityBuilder.addTextBody("Timing[" + 7 + "][day]", l + 1 + "");
                                    entityBuilder.addTextBody("Timing[" + 7 + "][session]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getSesstion());
                                    entityBuilder.addTextBody("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee());


                                }
                            } else if (l == 4) {
                                if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().isAvailable()) {
                                    entityBuilder.addTextBody("Timing[" + 8 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getStartTime());
                                    entityBuilder.addTextBody("Timing[" + 8 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getEndTime());
                                    entityBuilder.addTextBody("Timing[" + 8 + "][day]", l + 1 + "");
                                    entityBuilder.addTextBody("Timing[" + 8 + "][session]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getSesstion());
                                    entityBuilder.addTextBody("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee());

                                }
                                if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().isAvailable()) {

                                    entityBuilder.addTextBody("Timing[" + 9 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getStartTime());
                                    entityBuilder.addTextBody("Timing[" + 9 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getEndTime());
                                    entityBuilder.addTextBody("Timing[" + 9 + "][day]", l + 1 + "");
                                    entityBuilder.addTextBody("Timing[" + 9 + "][session]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getSesstion());
                                    entityBuilder.addTextBody("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee());


                                }
                            } else if (l == 5) {
                                if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().isAvailable()) {
                                    entityBuilder.addTextBody("Timing[" + 10 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getStartTime());
                                    entityBuilder.addTextBody("Timing[" + 10 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getEndTime());
                                    entityBuilder.addTextBody("Timing[" + 10 + "][day]", l + 1 + "");
                                    entityBuilder.addTextBody("Timing[" + 10 + "][session]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getSesstion());
                                    entityBuilder.addTextBody("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee());

                                }
                                if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().isAvailable()) {

                                    entityBuilder.addTextBody("Timing[" + 11 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getStartTime());
                                    entityBuilder.addTextBody("Timing[" + 11 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getEndTime());
                                    entityBuilder.addTextBody("Timing[" + 11 + "][day]", l + 1 + "");
                                    entityBuilder.addTextBody("Timing[" + 11 + "][session]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getSesstion());
                                    entityBuilder.addTextBody("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee());


                                }
                            } else if (l == 6) {
                                if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().isAvailable()) {
                                    entityBuilder.addTextBody("Timing[" + 12 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getStartTime());
                                    entityBuilder.addTextBody("Timing[" + 12 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getEndTime());
                                    entityBuilder.addTextBody("Timing[" + 12 + "][day]", l + 1 + "");
                                    entityBuilder.addTextBody("Timing[" + 12 + "][session]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getSesstion());
                                    entityBuilder.addTextBody("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee());

                                }
                                if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().isAvailable()) {

                                    entityBuilder.addTextBody("Timing[" + 13 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getStartTime());
                                    entityBuilder.addTextBody("Timing[" + 13 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getEndTime());
                                    entityBuilder.addTextBody("Timing[" + 13 + "][day]", l + 1 + "");
                                    entityBuilder.addTextBody("Timing[" + 13 + "][session]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getSesstion());
                                    entityBuilder.addTextBody("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee());


                                }
                            }

                        }
                    }
                }

                httppost.setHeader("Authorization", "Basic " + sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN));

                HttpEntity entity = entityBuilder.build();
                httppost.setEntity(entity);
                response = client.execute(httppost);

                responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                jo = new JSONObject(responseBody);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            circularProgressBar.setVisibility(View.GONE);
            try {
                if (jo != null) {
                    if (jo.has("data") && !jo.isNull("data")) {
                        JSONObject jsonObject = jo.getJSONObject("data");
                        if (jsonObject.has("profile_complete") && !jsonObject.isNull("profile_complete")) {
                            ApplicationSingleton.getUserDetailModel().setProfile_complete(jsonObject.getInt("profile_complete"));
                        }
                        if (jsonObject.has("status") && !jsonObject.isNull("status")) {
                            boolean check = jsonObject.getBoolean("status");
                            if (check) {
                                ArrayList<ClinicDetailModel> clinicDetailModels = new ArrayList<>();
                                clinicDetailModels = Clinic_Detail_Parser.clinic_detail_parser(jsonObject);
                                UserDetailModel userDetailModel = ApplicationSingleton.getUserDetailModel();
                                userDetailModel.setClinicDetailModels(clinicDetailModels);
                                ApplicationSingleton.setUserDetailModel(userDetailModel);
                                ApplicationSingleton.setIsClinicUpdated(true);
                                ApplicationSingleton.setIsListingFinish(true);
                                finish();

                            }

                        }
                        if (jsonObject.has("message") && !jsonObject.isNull("message")) {
                            Toast.makeText(Timings.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    }

                    JSONArray errorArray;
                    JSONObject dataJsonObject;
                    boolean status = false;
                    String auth_token = "", createdOn = "", id = "", email = "", mobile = "", user_type = "", lname = "", fname = "";
                    if (jo.has("data") && !jo.isNull("data")) {
                        dataJsonObject = jo.getJSONObject("data");


                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public class UpdateProfileEditAsyncTask extends AsyncTask<Void, Void, Void> {
        String responseBody;
        CircularProgressBar bar;
        //PlayServiceHelper regId;
        JSONObject jo;
        int pos;
        HttpClient client;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public UpdateProfileEditAsyncTask(int pos) {

            this.pos = pos;
        }

        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.AppBaseUrl + "user/update-clinic";
                SSLSocketFactory sf = new SSLSocketFactory(
                        SSLContext.getDefault(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme sch = new Scheme("https", 443, sf);
                client = new DefaultHttpClient();
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();

                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;

                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder
                        .create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                pairs.add(new BasicNameValuePair(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST));
                pairs.add(new BasicNameValuePair("userID", sesstionManager.getUserDetails().get(SesstionManager.USER_ID)));
                if (clinicDetailModel != null) {

                    if (clinicDetailModel.getCreated_by().equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID))) {
                        pairs.add(new BasicNameValuePair("ClinicProfile[city]", clinicDetailModel.getCity()));
                        pairs.add(new BasicNameValuePair("ClinicProfile[pincode]", clinicDetailModel.getPincode()));
                        pairs.add(new BasicNameValuePair("ClinicProfile[state]", clinicDetailModel.getState()));
                        pairs.add(new BasicNameValuePair("ClinicProfile[clinic_name]", clinicDetailModel.getName()));
                        pairs.add(new BasicNameValuePair("ClinicProfile[locality]", clinicDetailModel.getLocality()));


                        pairs.add(new BasicNameValuePair("ClinicProfile[address]", clinicDetailModel.getAddress()));
                    }
                    if (clinicDetailModel.getServicesModels() != null && clinicDetailModel.getServicesModels().size() > 0) {
                        for (int i = 0; i < clinicDetailModel.getServicesModels().size(); i++) {
                            pairs.add(new BasicNameValuePair("ClinicServices[name][" + i + "]", clinicDetailModel.getServicesModels().get(i).getSpecializationName()));
                            if (clinicDetailModel.getServicesModels().get(i).getId() != null)
                                pairs.add(new BasicNameValuePair("ClinicServices[id][" + i + "]", clinicDetailModel.getServicesModels().get(i).getId()));
                            else
                                pairs.add(new BasicNameValuePair("ClinicServices[id][" + i + "]", ""));


                        }
                    }
                }
                if (clinicDetailModel.getTimingsModels() != null) {
                    for (int l = 0; l < clinicDetailModel.getTimingsModels().size(); l++) {
                        pairs.add(new BasicNameValuePair("ClinicDoctors[id]", clinicDetailModel.getClinic_docID()));
                        if (l == 0) {
                            if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().isAvailable()) {
                                pairs.add(new BasicNameValuePair("Timing[" + 0 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getStartTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 0 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getEndTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 0 + "][day]", 1 + ""));
                                pairs.add(new BasicNameValuePair("Timing[" + 0 + "][session]", 1 + ""));
                                pairs.add(new BasicNameValuePair("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee()));

                            }
                            if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().isAvailable()) {

                                pairs.add(new BasicNameValuePair("Timing[" + 1 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getStartTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 1 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getEndTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 1 + "][day]", l + 1 + ""));
                                pairs.add(new BasicNameValuePair("Timing[" + 1 + "][session]", 2 + ""));
                                pairs.add(new BasicNameValuePair("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee()));


                            }
                        } else if (l == 1) {
                            if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().isAvailable()) {
                                pairs.add(new BasicNameValuePair("Timing[" + 2 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getStartTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 2 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getEndTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 2 + "][day]", l + 1 + ""));
                                pairs.add(new BasicNameValuePair("Timing[" + 2 + "][session]", 1 + ""));
                                pairs.add(new BasicNameValuePair("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee()));

                            }
                            if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().isAvailable()) {

                                pairs.add(new BasicNameValuePair("Timing[" + 3 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getStartTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 3 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getEndTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 3 + "][day]", l + 1 + ""));
                                pairs.add(new BasicNameValuePair("Timing[" + 3 + "][session]", 2 + ""));
                                pairs.add(new BasicNameValuePair("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee()));


                            }
                        } else if (l == 2) {
                            if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().isAvailable()) {
                                pairs.add(new BasicNameValuePair("Timing[" + 4 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getStartTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 4 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getEndTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 4 + "][day]", l + 1 + ""));
                                pairs.add(new BasicNameValuePair("Timing[" + 4 + "][session]", 1 + ""));
                                pairs.add(new BasicNameValuePair("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee()));

                            }
                            if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().isAvailable()) {

                                pairs.add(new BasicNameValuePair("Timing[" + 5 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getStartTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 5 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getEndTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 5 + "][day]", l + 1 + ""));
                                pairs.add(new BasicNameValuePair("Timing[" + 5 + "][session]", 2 + ""));
                                pairs.add(new BasicNameValuePair("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee()));


                            }

                        } else if (l == 3) {
                            if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().isAvailable()) {
                                pairs.add(new BasicNameValuePair("Timing[" + 6 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getStartTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 6 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getEndTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 6 + "][day]", l + 1 + ""));
                                pairs.add(new BasicNameValuePair("Timing[" + 6 + "][session]", 1 + ""));

                                pairs.add(new BasicNameValuePair("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee()));

                            }
                            if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().isAvailable()) {

                                pairs.add(new BasicNameValuePair("Timing[" + 7 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getStartTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 7 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getEndTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 7 + "][day]", l + 1 + ""));
                                pairs.add(new BasicNameValuePair("Timing[" + 7 + "][session]", 2 + ""));
                                pairs.add(new BasicNameValuePair("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee()));


                            }
                        } else if (l == 4) {
                            if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().isAvailable()) {
                                pairs.add(new BasicNameValuePair("Timing[" + 8 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getStartTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 8 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getEndTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 8 + "][day]", l + 1 + ""));
                                pairs.add(new BasicNameValuePair("Timing[" + 8 + "][session]", 1 + ""));
                                pairs.add(new BasicNameValuePair("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee()));

                            }
                            if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().isAvailable()) {

                                pairs.add(new BasicNameValuePair("Timing[" + 9 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getStartTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 9 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getEndTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 9 + "][day]", l + 1 + ""));
                                pairs.add(new BasicNameValuePair("Timing[" + 9 + "][session]", 2 + ""));
                                pairs.add(new BasicNameValuePair("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee()));


                            }
                        } else if (l == 5) {
                            if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().isAvailable()) {
                                pairs.add(new BasicNameValuePair("Timing[" + 10 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getStartTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 10 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getEndTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 10 + "][day]", l + 1 + ""));
                                pairs.add(new BasicNameValuePair("Timing[" + 10 + "][session]", 1 + ""));
                                pairs.add(new BasicNameValuePair("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee()));

                            }
                            if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().isAvailable()) {

                                pairs.add(new BasicNameValuePair("Timing[" + 11 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getStartTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 11 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getEndTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 11 + "][day]", l + 1 + ""));
                                pairs.add(new BasicNameValuePair("Timing[" + 11 + "][session]", 2 + ""));
                                pairs.add(new BasicNameValuePair("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee()));


                            }
                        } else if (l == 6) {
                            if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().isAvailable()) {
                                pairs.add(new BasicNameValuePair("Timing[" + 12 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getStartTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 12 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel1().getEndTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 12 + "][day]", l + 1 + ""));
                                pairs.add(new BasicNameValuePair("Timing[" + 12 + "][session]", 1 + ""));
                                pairs.add(new BasicNameValuePair("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee()));

                            }
                            if (clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().isAvailable()) {

                                pairs.add(new BasicNameValuePair("Timing[" + 13 + "][start_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getStartTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 13 + "][end_time]", clinicDetailModel.getTimingsModels().get(l).getSesstionModel2().getEndTime()));
                                pairs.add(new BasicNameValuePair("Timing[" + 13 + "][day]", l + 1 + ""));
                                pairs.add(new BasicNameValuePair("Timing[" + 13 + "][session]", 2 + ""));
                                pairs.add(new BasicNameValuePair("ClinicDoctors[fee]", clinicDetailModel.getConsultation_fee()));


                            }
                        }

                    }
                }


                httppost.setHeader("Authorization", "Basic " + sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN));

                HttpEntity entity = entityBuilder.build();
                httppost.setEntity(new UrlEncodedFormEntity(pairs));
                response = client.execute(httppost);

                responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                jo = new JSONObject(responseBody);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            circularProgressBar.setVisibility(View.GONE);
            try {
                if (jo != null) {
                    if (jo.has("data") && !jo.isNull("data")) {
                        JSONObject jsonObject = jo.getJSONObject("data");
                        if (jsonObject.has("profile_complete") && !jsonObject.isNull("profile_complete")) {
                            ApplicationSingleton.getUserDetailModel().setProfile_complete(jsonObject.getInt("profile_complete"));
                        }
                        if (jsonObject.has("status") && !jsonObject.isNull("status")) {
                            boolean check = jsonObject.getBoolean("status");
                            if (check) {
                                ArrayList<ClinicDetailModel> clinicDetailModels = new ArrayList<>();
                                clinicDetailModels = Clinic_Detail_Parser.clinic_detail_parser(jsonObject);
                                UserDetailModel userDetailModel = ApplicationSingleton.getUserDetailModel();
                                userDetailModel.setClinicDetailModels(clinicDetailModels);
                                ApplicationSingleton.setUserDetailModel(userDetailModel);
                                ApplicationSingleton.setIsClinicUpdated(true);
                                ApplicationSingleton.setIsListingFinish(true);
                                finish();

                            }

                        }
                        if (jsonObject.has("message") && !jsonObject.isNull("message")) {
                            Toast.makeText(Timings.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    }

                    JSONArray errorArray;
                    JSONObject dataJsonObject;
                    boolean status = false;
                    String auth_token = "", createdOn = "", id = "", email = "", mobile = "", user_type = "", lname = "", fname = "";
                    if (jo.has("data") && !jo.isNull("data")) {
                        dataJsonObject = jo.getJSONObject("data");


                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

}

