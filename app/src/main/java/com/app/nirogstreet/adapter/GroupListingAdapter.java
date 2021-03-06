package com.app.nirogstreet.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.CommunitiesDetails;
import com.app.nirogstreet.model.GroupModel;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.Event_For_Firebase;
import com.app.nirogstreet.uttil.LetterTileProvider;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.app.nirogstreet.uttil.WordUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;

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
 * Created by Preeti on 01-11-2017.
 */
public class GroupListingAdapter extends
        RecyclerView.Adapter<GroupListingAdapter.MyHolderView> {
    private static final int REQUEST_FOR_ACTIVITY_CODE = 6;
    private String authToken, userId;
    ArrayList<GroupModel> groupModels;
    boolean ishHide;
    private LetterTileProvider mLetterTileProvider;

    Context context;
    boolean showJoin;
    HashMap<String, String> userDetails;
    SesstionManager sessionManager;


    public GroupListingAdapter(ArrayList<GroupModel> groupModels, Context context, boolean hide, String userId, boolean showJoin) {
        this.groupModels = groupModels;
        this.showJoin = showJoin;
        this.context = context;
        this.ishHide = hide;

        mLetterTileProvider = new LetterTileProvider(context);

        sessionManager = new SesstionManager(context);
        userDetails = sessionManager.getUserDetails();
        authToken = userDetails.get(SesstionManager.AUTH_TOKEN);
        this.userId = userId;
    }

    @Override
    public MyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item_listing, null);
        MyHolderView viewHolder = new MyHolderView(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyHolderView holder, final int position) {
        final GroupModel groupModel = groupModels.get(position);
        try {
            if (groupModel.getPrivacy() != null && groupModel.getPrivacy().equalsIgnoreCase("0")) {
                if (!groupModel.isJoinShow()) {
                    holder.joinTextView.setVisibility(View.GONE);
                    if (showJoin)
                        holder.join1.setVisibility(View.VISIBLE);
                } else {
                    holder.joinTextView.setVisibility(View.VISIBLE);
                    holder.join1.setVisibility(View.GONE);

                    //  TypeFaceMethods.setRegularTypeFaceForTextView(holder.joinTextView, context);
                }
            } else if (groupModel.getPrivacy().equalsIgnoreCase("1")) {
                if (groupModel.getStatusdata() == null || groupModel.getStatusdata().equalsIgnoreCase("2")) {
                    holder.joinTextView.setText("Send Request");
                    if (showJoin) {
                        holder.joinTextView.setVisibility(View.VISIBLE);
                        holder.join1.setVisibility(View.GONE);

                    }
                } else if (groupModel.getStatusdata() != null && groupModel.getStatusdata().equalsIgnoreCase("0")) {
                    holder.join1.setText("Request Sent");
                    if (showJoin) {
                        holder.join1.setVisibility(View.VISIBLE);
                        holder.joinTextView.setVisibility(View.GONE);
                    }

                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.joinTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event_For_Firebase.getEventCount(context, "Feed_Communities_MyCommunities_JoinCommunityButton_Click");
                if (groupModel.getPrivacy().equalsIgnoreCase("1")) {
                    if (groupModel.getStatusdata() == null || groupModel.getStatusdata().equalsIgnoreCase("2")) {
                        if (NetworkUtill.isNetworkAvailable(context)) {
                            SentRequrestJoinAsyncTask sentRequrestJoinAsyncTask = new SentRequrestJoinAsyncTask(groupModel.getGroupId(), position);
                            sentRequrestJoinAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            NetworkUtill.showNoInternetDialog(context);
                        }
                    }
                } else {
                    if (NetworkUtill.isNetworkAvailable(context)) {
                        AcceptDeclineJoinAsyncTask acceptDeclineJoinAsyncTask = new AcceptDeclineJoinAsyncTask(groupModel.getGroupId(), userId, authToken, 1, position);
                        acceptDeclineJoinAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        NetworkUtill.showNoInternetDialog(context);
                    }
                }
            }
        });
        if (ishHide) {
            holder.linearLayoutbuttons.setVisibility(View.GONE);
        } else {

        }
        if (groupModel.getGroupBanner() != null && !groupModel.getGroupBanner().contains("banner-default") && !groupModel.getGroupBanner().contains("tempimages")) {
            Glide.with(context)
                    .load(groupModel.getGroupBanner()).placeholder(R.drawable.default_).centerCrop() // Uri of the picture
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .crossFade()
                    .override(100, 100)
                    .into(holder.groupIconImageView);

            // imageLoader1.getInstance().displayImage(groupModel.getGroupBanner(),  holder.groupIconImageView, defaultOptions);
        } else {
            holder.groupIconImageView.setImageBitmap(mLetterTileProvider.getLetterTile(groupModel.getGroupName()));

        }
        //imageLoader.DisplayImage(context,groupModel.getGroupBanner(), holder.groupIconImageView, null, 150, 150, R.drawable.default_image);
        holder.groupMembers.setText(groupModel.getTotalMembers() + " " + "Members");
        //holder.groupName.setText(groupModel.getGroupName());
        holder.groupName.setText(WordUtils.capitalize(groupModel.getGroupName()));
        //  TypeFaceMethods.setRegularTypeBoldFaceTextView(holder.groupName, context);
        //  TypeFaceMethods.setRegularTypeFaceForTextView(holder.groupMembers, context);
        //  TypeFaceMethods.setRegularTypeFaceForTextView(holder.descriptionTextView, context);
        holder.descriptionTextView.setText(groupModel.getGroupDescription());
        //  TypeFaceMethods.setRegularTypeFaceForTextView(holder.noticountTextView, context);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent intent = new Intent(context, GroupDetailActivity.class);
                intent.putExtra("cover", groupModel.getGroupBanner());
                intent.putExtra("position", position);
                intent.putExtra("userId", userId);
                intent.putExtra("privacy", groupModel.getPrivacy());
                intent.putExtra("groupname", groupModel.getGroupName());
                intent.putExtra("groupId", groupModel.getGroupId());
                intent.putExtra("status", groupModel.getStatus());
                ((Activity) context).startActivityForResult(intent, REQUEST_FOR_ACTIVITY_CODE);*/
                if (showJoin)
                    if (groupModel.getPrivacy().equalsIgnoreCase("0"))
                    {
                        Intent intent = new Intent(context, CommunitiesDetails.class);
                        intent.putExtra("groupId", groupModel.getGroupId());
                        intent.putExtra("user_invitation", groupModel.getUser_invitation());
                        context.startActivity(intent);
                    } else {
                        if (groupModel.getCreatedByUser().getUserId().equalsIgnoreCase(sessionManager.getUserDetails().get(SesstionManager.USER_ID))) {
                            Intent intent = new Intent(context, CommunitiesDetails.class);
                            intent.putExtra("groupId", groupModel.getGroupId());
                            intent.putExtra("user_invitation", groupModel.getUser_invitation());
                            context.startActivity(intent);
                        } else if (groupModel.getStatusdata() != null && groupModel.getStatusdata().equalsIgnoreCase("0")) {
                            Toast.makeText(context, "Request Sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Request to Join", Toast.LENGTH_SHORT).show();
                        }
                    }
                else {
                    Event_For_Firebase.getEventCount(context, "Feed_Communities_MyCommunities_Community_Feed_Screen_Post_Click");
                    Intent intent = new Intent(context, CommunitiesDetails.class);
                    intent.putExtra("groupId", groupModel.getGroupId());
                    intent.putExtra("user_invitation", groupModel.getUser_invitation());
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return groupModels.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {
        RoundedImageView groupIconImageView;
        TextView groupName, groupMembers, noticountTextView, descriptionTextView;
        TextView acceptTextView, declineTextView;
        RelativeLayout relativeLayout;
        TextView joinTextView;
        TextView join1;
        LinearLayout linearLayoutbuttons;

        public MyHolderView(View itemView) {
            super(itemView);
            join1 = (TextView) itemView.findViewById(R.id.join1);
            joinTextView = (TextView) itemView.findViewById(R.id.join);
            noticountTextView = (TextView) itemView.findViewById(R.id.noticount);
            descriptionTextView = (TextView) itemView.findViewById(R.id.description);
            linearLayoutbuttons = (LinearLayout) itemView.findViewById(R.id.buttons);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout1);
            groupName = (TextView) itemView.findViewById(R.id.groupName);
            acceptTextView = (TextView) itemView.findViewById(R.id.accept);
            declineTextView = (TextView) itemView.findViewById(R.id.decline);
            groupMembers = (TextView) itemView.findViewById(R.id.groupMembers);
            groupIconImageView = (RoundedImageView) itemView.findViewById(R.id.groupImage);
        }
    }

    @Override
    public void onBindViewHolder(MyHolderView holder, int position, List<Object> payloads) {

        if (!payloads.isEmpty()) {

            if (payloads.get(0) instanceof String) {
                try {
                    if (groupModels.get(position).getPrivacy().equalsIgnoreCase("0")) {
                        holder.joinTextView.setVisibility(View.GONE);
                        holder.join1.setVisibility(View.VISIBLE);
                        holder.join1.setText("Joined");

                        groupModels.get(position).setJoinShow(false);
                    } else if (groupModels.get(position).getPrivacy().equalsIgnoreCase("1")) {
                        holder.joinTextView.setVisibility(View.GONE);
                        holder.join1.setVisibility(View.VISIBLE);
                        holder.join1.setText("Request Sent");
                        groupModels.get(position).setPrivacy("1");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    public class AcceptDeclineJoinAsyncTask extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String groupId, userId;

        int status1;
        HttpClient client;
        int pos;
        private String responseBody;

        public AcceptDeclineJoinAsyncTask(String groupId, String userId, String authToken, int status, int pos) {
            this.groupId = groupId;
            this.status1 = status;
            this.authToken = authToken;
            this.pos = pos;
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
                                Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                             /*   groupModels.remove(pos);
                                notifyItemRemoved(pos);
                                notifyItemRangeChanged(pos, groupModels.size());*/
                                ApplicationSingleton.setIsJoinedCommunity(true);
                                notifyItemChanged(pos, new String("joined"));

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
                pairs.add(new BasicNameValuePair("addedType", 1 + ""));
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

    public class SentRequrestJoinAsyncTask extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String groupId, userId;

        int status1;
        HttpClient client;
        int pos;
        private String responseBody;

        public SentRequrestJoinAsyncTask(String groupId, int pos) {
            this.groupId = groupId;
            this.pos = pos;
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
                        //ApplicationSingleton.setIsJoinedCommunity(true);
                        notifyItemChanged(pos, new String("joined"));


                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url;
                url = AppUrl.BaseUrl + "group/invite";


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
                pairs.add(new BasicNameValuePair("invited_to", sessionManager.getUserDetails().get(SesstionManager.USER_ID)));


                pairs.add(new BasicNameValuePair("groupID", groupId));
                pairs.add(new BasicNameValuePair("addedType", 2 + ""));


                pairs.add(new BasicNameValuePair("status", "0"));
                httppost.setHeader("Authorization", "Basic " + sessionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN));

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
