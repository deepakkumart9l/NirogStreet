package com.app.nirogstreet.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.CommunitiesDetails;
import com.app.nirogstreet.activites.Knowledge_Centre_Detail;
import com.app.nirogstreet.model.CoursesModel;
import com.app.nirogstreet.model.GroupModel;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.LetterTileProvider;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.app.nirogstreet.uttil.WordUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

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
 * Created by Preeti on 11-12-2017.
 */

public class Courses_Listing_Adapter extends
        RecyclerView.Adapter<Courses_Listing_Adapter.MyHolderView> {
    private static final int REQUEST_FOR_ACTIVITY_CODE = 6;
    private String authToken, userId;
    ArrayList<CoursesModel> coursesModels;
    boolean ishHide;
    private LetterTileProvider mLetterTileProvider;

    Context context;
    boolean showJoin;
    HashMap<String, String> userDetails;
    SesstionManager sessionManager;


    public Courses_Listing_Adapter(ArrayList<CoursesModel> coursesModels, Context context, boolean hide, String userId, boolean showJoin) {
        this.coursesModels = coursesModels;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_item_listing, null);
        MyHolderView viewHolder = new MyHolderView(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyHolderView holder, final int position) {
        final CoursesModel coursesModel = coursesModels.get(position);
        holder.time.setText(coursesModel.getCreated_at());
        TypeFaceMethods.setRegularTypeBoldFaceTextView(holder.time, context);
        if (coursesModel.getAuthor_detail_module() != null) {
            holder.dr_name_csv.setText("by " + coursesModel.getAuthor_detail_module().getName());
            TypeFaceMethods.setRegularTypeBoldFaceTextView(holder.dr_name_csv, context);
            if (coursesModel.getAuthor_detail_module().getProfile_pic() != null) {

                Picasso.with(context)
                        .load(coursesModel.getAuthor_detail_module().getProfile_pic())
                        .placeholder(R.drawable.user)
                        .error(R.drawable.user)
                        .into(holder.circleImageView);
            }
        }
        holder.course_name.setText(coursesModel.getName());
        TypeFaceMethods.setRegularTypeBoldFaceTextView(holder.course_name, context);
        holder.descriptionTextView.setText(coursesModel.getDescription().trim().toString());
        TypeFaceMethods.setRegularTypeFaceForTextView(holder.descriptionTextView, context);
        if (coursesModel.getBanner() != null && !coursesModel.getBanner().contains("banner-default") && !coursesModel.getBanner().contains("tempimages")) {

            Picasso.with(context)
                    .load(coursesModel.getBanner())
                    .placeholder(R.drawable.default_)
                    .error(R.drawable.default_)
                    .into(holder.groupIconImageView);
            // imageLoader1.getInstance().displayImage(groupModel.getGroupBanner(),  holder.groupIconImageView, defaultOptions);
        } else {
            holder.groupIconImageView.setImageBitmap(mLetterTileProvider.getLetterTile(coursesModel.getName()));

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Knowledge_Centre_Detail.class);
                intent.putExtra("courseID", coursesModel.getId());
                intent.putExtra("isHide", ishHide);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return coursesModels.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {
        RoundedImageView groupIconImageView;
        TextView course_name, descriptionTextView;
        RelativeLayout relativeLayout;
        TextView dr_name_csv, time;
        LinearLayout linearLayoutbuttons;
        CircleImageView circleImageView;

        public MyHolderView(View itemView) {
            super(itemView);
            groupIconImageView = (RoundedImageView) itemView.findViewById(R.id.groupImage);
            descriptionTextView = (TextView) itemView.findViewById(R.id.description);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout1);
            course_name = (TextView) itemView.findViewById(R.id.course_name);
            dr_name_csv = (TextView) itemView.findViewById(R.id.dr_name_csv);
            time = (TextView) itemView.findViewById(R.id.duration);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.pro_image);

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

