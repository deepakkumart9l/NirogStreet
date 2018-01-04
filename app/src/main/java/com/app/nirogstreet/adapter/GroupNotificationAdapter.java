package com.app.nirogstreet.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.fragments.About_Fragment;
import com.app.nirogstreet.model.GroupNotificationModel;
import com.app.nirogstreet.model.NotificationModel;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLContext;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Preeti on 29-12-2017.
 */

public class GroupNotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<GroupNotificationModel> notificationModels;
    Context context;
    SesstionManager sessionManager;
    String userId;
    String authToken;

    public GroupNotificationAdapter(Context context, ArrayList<GroupNotificationModel> notificationModels, String authToken) {
        this.context = context;
        this.notificationModels = notificationModels;
        this.authToken = authToken;


        sessionManager = new SesstionManager(context);
        HashMap<String, String> user = sessionManager.getUserDetails();
        authToken = user.get(SesstionManager.AUTH_TOKEN);
        userId = user.get(SesstionManager.USER_ID);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View v = layoutInflater.inflate(R.layout.group_notification_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return notificationModels.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder genericViewHolder = (MyViewHolder) holder;
        final GroupNotificationModel groupNotificationModel = notificationModels.get(position);
        if (groupNotificationModel.getUserProfile() != null && !groupNotificationModel.getUserProfile().equalsIgnoreCase(""))
            Glide.with(context)
                    .load(groupNotificationModel.getUserProfile()).placeholder(R.drawable.user)// Uri of the picture
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .crossFade()
                    .override(100, 100)
                    .into(genericViewHolder.imageView);
        genericViewHolder.message.setText(groupNotificationModel.getUserName() + " requested to join " + groupNotificationModel.getComunityName() + "");
        genericViewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtill.isNetworkAvailable(context)) {
                    AcceptDeclineJoinAsyncTask acceptDeclineJoinAsyncTask = new AcceptDeclineJoinAsyncTask(groupNotificationModel.getCommunityId(), groupNotificationModel.getUserId(), sessionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN), 2, position, 3);
                    acceptDeclineJoinAsyncTask.execute();
                } else {
                    NetworkUtill.showNoInternetDialog(context);
                }
            }
        });
        genericViewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtill.isNetworkAvailable(context)) {
                    AcceptDeclineJoinAsyncTask acceptDeclineJoinAsyncTask = new AcceptDeclineJoinAsyncTask(groupNotificationModel.getCommunityId(), groupNotificationModel.getUserId(), sessionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN), 1, position, 4);
                    acceptDeclineJoinAsyncTask.execute();
                } else {
                    NetworkUtill.showNoInternetDialog(context);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView message, time, name;
        CircleImageView imageView;
        ImageView cancel, accept;
        RelativeLayout mainitem_noti;

        public MyViewHolder(View view) {
            super(view);
            cancel = (ImageView) view.findViewById(R.id.cancel);
            accept = (ImageView) view.findViewById(R.id.accept);
            message = (TextView) view.findViewById(R.id.text);
            imageView = (CircleImageView) view.findViewById(R.id.pro);

        }
    }

    public class AcceptDeclineJoinAsyncTask extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String groupId, userId;

        int status1;
        HttpClient client;
        int pos;
        int addedType;
        private String responseBody;

        public AcceptDeclineJoinAsyncTask(String groupId, String userId, String authToken, int status, int pos, int addedType) {
            this.groupId = groupId;
            this.status1 = status;
            this.authToken = authToken;
            this.pos = pos;
            this.addedType = addedType;
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
                            if (jsonObject.has("message") && !jsonObject.isNull("message")) {
                                Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                             /*   groupModels.remove(pos);
                                notifyItemRemoved(pos);
                                notifyItemRangeChanged(pos, groupModels.size());*/
                                notificationModels.remove(pos);
                                notifyItemRemoved(pos);
                                notifyItemRangeChanged(pos, notificationModels.size());

                                ApplicationSingleton.setGroupRequestCount(notificationModels.size());
                            }


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
                String url = AppUrl.BaseUrl + "group/accept-decline";
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
                pairs.add(new BasicNameValuePair("userID", userId));
                pairs.add(new BasicNameValuePair("groupID", groupId));
                pairs.add(new BasicNameValuePair("status", status1 + ""));
                pairs.add(new BasicNameValuePair("addedType", addedType + ""));
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

}
