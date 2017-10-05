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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.materialdaterangepicker.time.RadialPickerLayout;
import com.app.nirogstreet.materialdaterangepicker.time.TimePickerDialog;
import com.app.nirogstreet.model.SesstionModel;
import com.app.nirogstreet.model.TimingsModel;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Preeti on 27-09-2017.
 */

public class Timings extends Activity {
    private RecyclerView recyclerView;
    ImageView backImageView;
    private LinearLayoutManager linearLayoutManager;
    ArrayList<TimingsModel> timingsModels1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lst);

        backImageView=(ImageView) findViewById(R.id.back);
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
        timingsModels1.add(new TimingsModel("Mon", new SesstionModel("6:3", "7:3"), new SesstionModel("1:3", "2:3")));
        timingsModels1.add(new TimingsModel("Tue", new SesstionModel("", ""), new SesstionModel("", "")));

        timingsModels1.add(new TimingsModel("Wed", new SesstionModel("", ""), new SesstionModel("", "")));

        timingsModels1.add(new TimingsModel("Thu", new SesstionModel("", ""), new SesstionModel("", "")));

        timingsModels1.add(new TimingsModel("Fri", new SesstionModel("", ""), new SesstionModel("", "")));

        timingsModels1.add(new TimingsModel("Sat", new SesstionModel("", ""), new SesstionModel("", "")));
        timingsModels1.add(new TimingsModel("Sun", new SesstionModel("", ""), new SesstionModel("", "")));
        TimingsAdapter timingsAdapter = new TimingsAdapter(timingsModels1, Timings.this, Timings.this);
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
            TypeFaceMethods.setRegularTypeFaceForTextView(holder.dayTextView, context);
            if(!timingsModels.get(position).getSesstionModel1().getStartTime().equalsIgnoreCase("") &&!timingsModels.get(position).getSesstionModel1().getEndTime().equalsIgnoreCase(""))
            {
                holder.addTextView.setVisibility(View.GONE);
                holder.firstSesstion.setVisibility(View.VISIBLE);
                holder.firstSesstionLayout.setVisibility(View.VISIBLE);
                holder.view.setVisibility(View.VISIBLE);
                holder.secondSesstionLayout.setVisibility(View.VISIBLE);
                holder.secondSesstion.setVisibility(View.VISIBLE);

                holder.firstSesstion.setText(timingsModels.get(position).getSesstionModel1().getStartTime()+":"+timingsModels.get(position).getSesstionModel1().getEndTime());
            }
            if(!timingsModels.get(position).getSesstionModel2().getStartTime().equalsIgnoreCase("") &&!timingsModels.get(position).getSesstionModel2().getEndTime().equalsIgnoreCase(""))
            {
                holder.secondSesstion.setText(timingsModels.get(position).getSesstionModel2().getStartTime()+":"+timingsModels.get(position).getSesstionModel2().getEndTime());

            }
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
                                                               String _24HourFormat=hourString+":"+minuteString+hourStringEnd+":"+minuteStringEnd;
                                                               String time = amPmFromat(hourString + ":" + minuteString ) + "  - " + amPmFromat(hourStringEnd + ":" + minuteStringEnd) ;
                                                               if (isFirstTime[0]) {
                                                                   holder.firstSesstion.setText(time);
                                                                   timingsModels1.get(position).setSesstionModel1(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString));
                                                                   timingsModels.get(position).setSesstionModel1(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString));

                                                               } else {
                                                                   holder.secondSesstion.setText(time);
                                                                   timingsModels1.get(position).setSesstionModel2(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString));
                                                                   timingsModels.get(position).setSesstionModel2(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString));

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
                    String previousStartTime=timingsModels.get(position).getSesstionModel1().getStartTime();
                    String previousEndTime=timingsModels.get(position).getSesstionModel1().getEndTime();

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
                                    String time = amPmFromat(hourString + ":" + minuteString ) + "  - " + amPmFromat(hourStringEnd + ":" + minuteStringEnd) ;
                                    if (isFirstTime[0]) {
                                        holder.firstSesstion.setText(time);
                                        timingsModels.get(position).setSesstionModel1(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString));
                                        timingsModels1.get(position).setSesstionModel1(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString));

                                    } else {
                                        holder.secondSesstion.setText(time);
                                        timingsModels1.get(position).setSesstionModel2(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString));

                                        timingsModels.get(position).setSesstionModel2(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString));

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
                    String previousStartTime=timingsModels.get(position).getSesstionModel2().getStartTime();


                    String previousEndTime=timingsModels.get(position).getSesstionModel2().getEndTime();
                    if(!previousStartTime.equalsIgnoreCase("")&&!previousEndTime.equalsIgnoreCase(""))
                    {
                      val=true;
                    }
                    else {
                        val=false;
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
                                    String time = amPmFromat(hourString + ":" + minuteString ) + "  - " + amPmFromat(hourStringEnd + ":" + minuteStringEnd) ;
                                    if (isFirstTime[0]) {
                                        holder.firstSesstion.setText(time);
                                        timingsModels1.get(position).setSesstionModel1(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString));

                                        timingsModels.get(position).setSesstionModel1(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString));
                                    } else {
                                        holder.secondSesstion.setText(time);
                                        timingsModels.get(position).setSesstionModel2(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString));
                                        timingsModels1.get(position).setSesstionModel2(new SesstionModel(hourString + ":" + minuteString, hourStringEnd + ":" + minuteString));

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
                    timingsModels.get(position).setSesstionModel1(new SesstionModel("",""));
                    notifyDataSetChanged();
                }
            });
            holder.secondSessionCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timingsModels.get(position).setSesstionModel1(timingsModels.get(position).getSesstionModel2());
                    timingsModels.get(position).setSesstionModel2(new SesstionModel("",""));

                    notifyDataSetChanged();
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
            ImageView firstSessionCancel,secondSessionCancel;
            RelativeLayout firstSesstionLayout,secondSesstionLayout;
            View view;

            public MyHolderView(View itemView) {
                super(itemView);
                addTextView = (TextView) itemView.findViewById(R.id.add);
                firstSessionCancel=(ImageView)itemView.findViewById(R.id.firstSessionCancel);
                secondSessionCancel=(ImageView)itemView.findViewById(R.id.secondSesstioncancel);
                dayTextView = (TextView) itemView.findViewById(R.id.day);
                secondSesstionLayout=(RelativeLayout)itemView.findViewById(R.id.secondSesstionLayout);
                firstSesstionLayout=(RelativeLayout)itemView.findViewById(R.id.firstSesstionLayout) ;
                secondSesstion = (TextView) itemView.findViewById(R.id.secondSesstion);
                firstSesstion = (TextView) itemView.findViewById(R.id.firstSesstion);
                view = (View) itemView.findViewById(R.id.view);
                TypeFaceMethods.setRegularTypeFaceForTextView(dayTextView, context);
                TypeFaceMethods.setRegularTypeFaceForTextView(addTextView, context);
                TypeFaceMethods.setRegularTypeFaceForTextView(secondSesstion, context);
                TypeFaceMethods.setRegularTypeFaceForTextView(firstSesstion, context);


            }
        }
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

}

