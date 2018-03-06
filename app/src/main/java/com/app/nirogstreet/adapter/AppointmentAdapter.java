package com.app.nirogstreet.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.model.AppointmentModel;
import com.app.nirogstreet.model.CoursesModel;
import com.app.nirogstreet.uttil.LetterTileProvider;
import com.app.nirogstreet.uttil.SesstionManager;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Preeti on 17-12-2017.
 */

public class AppointmentAdapter extends
        RecyclerView.Adapter<AppointmentAdapter.MyHolderView> {
    private static final int REQUEST_FOR_ACTIVITY_CODE = 6;
    private String authToken, userId;
    ArrayList<AppointmentModel> appointmentModels;
    boolean ishHide;
    private LetterTileProvider mLetterTileProvider;

    Context context;
    boolean showJoin;
    HashMap<String, String> userDetails;
    SesstionManager sessionManager;

    public AppointmentAdapter(ArrayList<AppointmentModel> appointmentModels, Context context) {
        this.appointmentModels = appointmentModels;
        this.context = context;

    }

    @Override
    public AppointmentAdapter.MyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item, null);
        MyHolderView viewHolder = new MyHolderView(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyHolderView holder, final int position) {
        final AppointmentModel coursesModel = appointmentModels.get(position);
        holder.appointment_id.setText(coursesModel.getAppointment_id());
        holder.feeTextView.setText(coursesModel.getFee());
        holder.patient_name.setText(coursesModel.getPatient_detail().getName());
        holder.age.setText(coursesModel.getPatient_detail().getAge());
        holder.clinic.setText(coursesModel.getClinic_detail());
        holder.mobile.setText(coursesModel.getPatient_detail().getPhone());
        holder.time.setText(coursesModel.getTime());
        holder.day.setText(coursesModel.getDate());
        if(coursesModel.getPayment_method().equalsIgnoreCase("0"))
        {
            holder.mode.setText("Card");
        }else if(coursesModel.getPayment_method().equalsIgnoreCase("1"))
        {
            holder.mode.setText("Net Banking");

        }else if(coursesModel.getPayment_method().equalsIgnoreCase("2"))
        {
            holder.mode.setText("Wallet");
        }else if(coursesModel.getPayment_method().equalsIgnoreCase("3"))
        {
            holder.mode.setText("Upi");
        }else if(coursesModel.getPayment_method().equalsIgnoreCase("4")){
            holder.mode.setText("Cash on Consultation");

        }
        if(coursesModel.getTitle()!=null&&!coursesModel.getTitle().equalsIgnoreCase(""))
        {
            holder.title.setVisibility(View.VISIBLE);
            holder.title_lay.setVisibility(View.VISIBLE);
            holder.title.setText(coursesModel.getTitle());
        }
        else {
            holder.title.setVisibility(View.GONE);
            holder.title_lay.setVisibility(View.GONE);

        }
     //   holder.mode.setText(coursesModel.get);


    }

    @Override
    public int getItemCount() {
        return appointmentModels.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {
        TextView appointment_id, feeTextView, patient_name, age, clinic, mobile, time, day,mode,title;
LinearLayout title_lay;
        public MyHolderView(View itemView) {
            super(itemView);
            appointment_id = (TextView) itemView.findViewById(R.id.appointmentId);
            feeTextView = (TextView) itemView.findViewById(R.id.fee);
            title=(TextView)itemView.findViewById(R.id.title);
            mode=(TextView)itemView.findViewById(R.id.mode) ;
            title_lay=(LinearLayout)itemView.findViewById(R.id.title_lay);
            patient_name = (TextView) itemView.findViewById(R.id.patient_name);
            age = (TextView) itemView.findViewById(R.id.age);
            clinic = (TextView) itemView.findViewById(R.id.clinic);
            mobile = (TextView) itemView.findViewById(R.id.mobile);
            time = (TextView) itemView.findViewById(R.id.time);
            day = (TextView) itemView.findViewById(R.id.day);

        }
    }
/*
    public class AcceptDeclineJoinAsyncTask extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String groupId, userId;

        int status1;
        HttpClient client;
        int pos;
        private String responseBody;

        public AcceptDeclineJoinAsyncTask(String groupId, String userId, String authToken, int status,int pos) {
            this.groupId = groupId;
            this.status1 = status;
            this.authToken = authToken;
            this.pos=pos;
            this.userId = userId;
        }

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                if (jo != null) {


                    if (jo.has("response") && !jo.isNull("response")) {
                        JSONObject jsonObjectresponse = jo.getJSONObject("response");
                        if (jsonObjectresponse.has("message") && !jsonObjectresponse.isNull("message")) {
                            JSONObject jsonObject = jsonObjectresponse.getJSONObject("message");
                           */
/* if (jsonObject.has("message") && !jsonObject.isNull("message")) {
                                Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                groupModels.remove(pos);
                                notifyItemRemoved(pos);
                                notifyItemRangeChanged(pos, groupModels.size());
                            }*//*






                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {


                String url = AppUrl.BaseUrl + "group/invite";
                SSLSocketFactory sf = new SSLSocketFactory(
                        SSLContext.getDefault(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme sch = new Scheme("https", 443, sf);
                client = new DefaultHttpClient();
                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST));
                pairs.add(new BasicNameValuePair("invited_to", userId));
                pairs.add(new BasicNameValuePair("groupID", groupId));
                pairs.add(new BasicNameValuePair("status", status1 + ""));
                pairs.add(new BasicNameValuePair("addedType",1+""));
                httppost.setHeader("Authorization", "Basic " + authToken);

                httppost.setEntity(new UrlEncodedFormEntity(pairs));
                response = client.execute(httppost);

                responseBody = EntityUtils
                        .toString(response.getEntity());
                jo = new JSONObject(responseBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
*/

}

