package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.materialdaterangepicker.time.RadialPickerLayout;
import com.app.nirogstreet.materialdaterangepicker.time.TimePickerDialog;
import com.app.nirogstreet.model.SesstionModel;
import com.app.nirogstreet.model.TimingsModel;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Preeti on 27-09-2017.
 */

public class Timings   extends Activity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    ArrayList<TimingsModel> timingsModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lst);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(Timings.this);
        //  recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        timingsModels.add(new TimingsModel("Mon", new SesstionModel("", ""), new SesstionModel("", "")));
        timingsModels.add(new TimingsModel("Tues", new SesstionModel("", ""), new SesstionModel("", "")));

        timingsModels.add(new TimingsModel("Wed", new SesstionModel("", ""), new SesstionModel("", "")));

        timingsModels.add(new TimingsModel("Thurs", new SesstionModel("", ""), new SesstionModel("", "")));

        timingsModels.add(new TimingsModel("Fri", new SesstionModel("", ""), new SesstionModel("", "")));

        timingsModels.add(new TimingsModel("Sat", new SesstionModel("", ""), new SesstionModel("", "")));
        timingsModels.add(new TimingsModel("Sun", new SesstionModel("", ""), new SesstionModel("", "")));
        TimingsAdapter timingsAdapter = new TimingsAdapter(timingsModels, Timings.this, Timings.this);
        recyclerView.setAdapter(timingsAdapter);
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
            holder.addTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isFirstTime[0] = true;
                    Calendar now = Calendar.getInstance();
                    TimePickerDialog tpd;
                    tpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                                                           @Override
                                                           public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd, TextView textView, int amOrpmstart, int amorpmEnd) {
                                                               holder.addTextView.setVisibility(View.GONE);
                                                               holder.firstSesstion.setVisibility(View.VISIBLE);
                                                               holder.view.setVisibility(View.VISIBLE);

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
                                                               String time = hourString + ":" + minuteString + start + "  - " + hourStringEnd + ":" + minuteStringEnd + end;
                                                               if (isFirstTime[0])
                                                                   holder.firstSesstion.setText(time);
                                                               else
                                                                   holder.secondSesstion.setText(time);

                                                           }
                                                       },
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            false, false, "", "",true
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
                    String arr[] = holder.firstSesstion.getText().toString().split(" ");
                    Calendar now = Calendar.getInstance();
                    TimePickerDialog tpd = TimePickerDialog.newInstance(
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd, TextView textView, int amOrpmstart, int amorpmEnd) {
                                    holder.addTextView.setVisibility(View.GONE);
                                    holder.view.setVisibility(View.VISIBLE);

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
                                    String time = hourString + ":" + minuteString + start + "  - " + hourStringEnd + ":" + minuteStringEnd + end;
                                    if (isFirstTime[0])
                                        holder.firstSesstion.setText(time);
                                    else
                                        holder.secondSesstion.setText(time);

                                }
                            },
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            false, true, arr[0], arr[4],false
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
                    isFirstTime[0] = false;

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
                                    if (isFirstTime[0])
                                        holder.firstSesstion.setText(time);
                                    else
                                        holder.secondSesstion.setText(time);
                                }
                            },
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            false, false, "", "",true
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
            View view;

            public MyHolderView(View itemView) {
                super(itemView);
                addTextView = (TextView) itemView.findViewById(R.id.add);
                dayTextView = (TextView) itemView.findViewById(R.id.day);
                secondSesstion = (TextView) itemView.findViewById(R.id.secondSesstion);
                firstSesstion = (TextView) itemView.findViewById(R.id.firstSesstion);
                view = (View) itemView.findViewById(R.id.view);
            }
        }
    }

}

