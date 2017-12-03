package com.app.nirogstreet.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.CommunitiesDetails;
import com.app.nirogstreet.activites.PostDetailActivity;
import com.app.nirogstreet.model.NotificationModel;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
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

/**
 * Created by Preeti on 28-10-2017.
 */
public class Notification_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<NotificationModel> notificationModels;
    Context context;
    ReadUnReadAsyncTask readUnReadAsyncTask;
    SesstionManager sessionManager;
    String userId;
    String authToken;

    public Notification_Adapter(Context context, ArrayList<NotificationModel> notificationModels, String authToken) {
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
        final View v = layoutInflater.inflate(R.layout.notificationitem, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return notificationModels.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder genericViewHolder = (MyViewHolder) holder;
        final NotificationModel item = notificationModels.get(position);
        try {
            genericViewHolder.name.setText(Html.fromHtml("<b>" + item.getName() + "</b>" + " " + item.getMessage()));
            TypeFaceMethods.setRegularTypeFaceForTextView(genericViewHolder.name, context);
            genericViewHolder.time.setText(item.getTime());
            TypeFaceMethods.setRegularTypeFaceForTextView(genericViewHolder.time, context);

            String imgUrl = item.getProfile_pic();
            try {
                if (imgUrl != null && !imgUrl.equalsIgnoreCase(""))
                    Glide.with(context)
                            .load(imgUrl).placeholder(R.drawable.user) // Uri of the picture
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .crossFade()
                            .override(100, 100)
                            .into(genericViewHolder.imageView);
                else
                    Glide.with(context)
                            .load(R.drawable.user) // Uri of the picture
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .crossFade()
                            .override(100, 100)
                            .into(genericViewHolder.imageView);
                //  imageLoader.DisplayImage(context, imgUrl, genericViewHolder.imageView, null, 150, 150, R.drawable.profile_default);

            } catch (Exception e) {

            }
            if (notificationModels.get(position).getUnread() == 0)
                genericViewHolder.itemView.setBackgroundColor(Color.WHITE);
            else
                genericViewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
            // else
            //  genericViewHolder.itemView.setBackgroundColor(R.color.background);

            genericViewHolder.mainitem_noti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NotificationModel notificationModel = notificationModels.get(position);
                    if (notificationModel.getUnread() == 1) {
                        if (NetworkUtill.isNetworkAvailable(context)) {
                            readUnReadAsyncTask = new ReadUnReadAsyncTask(notificationModel, userId, authToken, position);
                            readUnReadAsyncTask.execute();
                        } else {
                            openNotification(notificationModel);
                        }

                    } else {

                         openNotification(notificationModel);
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView message, time, name;
        ImageView imageView;
        RelativeLayout mainitem_noti;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            message = (TextView) view.findViewById(R.id.message);
            time = (TextView) view.findViewById(R.id.time);
            imageView = (ImageView) view.findViewById(R.id.img);
            mainitem_noti = (RelativeLayout) view.findViewById(R.id.mainitem_noti);

        }
    }

    public class ReadUnReadAsyncTask extends AsyncTask<Void, Void, Void> {
        String responseBody;
        int pos;

        String userId, authToken;
        NotificationModel notificationModel;

        //PlayServiceHelper regId;
        public ReadUnReadAsyncTask(NotificationModel notificationModel, String userId, String authToken, int pos) {
            this.userId = userId;
            this.notificationModel = notificationModel;
            this.authToken = authToken;
            this.pos = pos;
        }

        JSONObject jo;
        HttpClient client;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        @Override
        protected void onPreExecute() {
            // circularProgressBar.setVisibility(View.VISIBLE);
            //  bar = (ProgressBar) findViewById(R.id.progressBar);
            //   bar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.BaseUrl + "feed/notificationseen";
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
                pairs.add(new BasicNameValuePair("id", notificationModel.getId()));
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //  circularProgressBar.setVisibility(View.GONE);
            try {
                notificationModels.get(pos).setUnread(0);
                notifyItemChanged(pos);
                openNotification(notificationModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void openNotification(NotificationModel notificationModel) {
     if (notificationModel.getGroupId() != null && !notificationModel.getGroupId().equals("")) {
            Intent intent = new Intent(context, CommunitiesDetails.class);
            intent.putExtra("userId", userId);
            intent.putExtra("groupId", notificationModel.getGroupId());
            context.startActivity(intent);
        } else if (notificationModel.getPostId() != null && !notificationModel.getPostId().equals("")) {
            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra("feedId", notificationModel.getPostId());
            context.startActivity(intent);
        }
    }
}

