package com.app.nirogstreet.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.CommunitiesDetails;
import com.app.nirogstreet.activites.Dr_Profile;
import com.app.nirogstreet.activites.PostEditActivity;
import com.app.nirogstreet.model.SearchModel;
import com.app.nirogstreet.model.UserList;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.Methods;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
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
 * Created by Preeti on 30-11-2017.
 */

public class MemberListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    String groupId;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    ArrayList<UserList> rowItems;

    boolean isLogedInUser_Admin = false;
    SesstionManager sesstionManager;
    String des="";
    String privacy="";

    public MemberListingAdapter(Context context, ArrayList<UserList> items, String groupId, boolean isLogedInUser_Admin,String des,String privacy) {
        this.context = context;
        this.rowItems = items;
        this.groupId = groupId;
        this.isLogedInUser_Admin = isLogedInUser_Admin;
        sesstionManager = new SesstionManager(context);
        this.des=des;
        this.privacy=privacy;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView txtTitle, department;

        public ViewHolder(View itemView) {
            super(itemView);
            department = (TextView) itemView.findViewById(R.id.depart);
            txtTitle = (TextView) itemView.findViewById(R.id.name);
            imageView = (CircleImageView) itemView.findViewById(R.id.img);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_member, parent, false);
            return new HeaderView(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_view, parent, false);
            return new ViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case TYPE_HEADER:
                final HeaderView  headerView=(HeaderView) holder;
                headerView.postAn.setText(des);
                headerView.privacy.setText(privacy);
                break;
            case TYPE_ITEM:
                final ViewHolder viewHolder = (ViewHolder) holder;

                final UserList rowItem = rowItems.get(position);
                viewHolder.txtTitle.setText(Methods.getName(rowItem.getTitle(), rowItem.getName()));
                //ImageLoader imageLoader=new ImageLoader(context);
                if (position == 1|| rowItem.getIs_admin() != null && rowItem.getIs_admin().equalsIgnoreCase("1")) {
                    viewHolder.department.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.department.setVisibility(View.GONE);

                }

                String imgUrl = rowItem.getProfile_pic();
                if (imgUrl != null && !imgUrl.equalsIgnoreCase(""))

                    Picasso.with(context)
                            .load(imgUrl)
                            .placeholder(R.drawable.user)
                            .error(R.drawable.user)
                            .into(viewHolder.imageView);
                else
                    Picasso.with(context)
                            .load(R.drawable.user)
                            .placeholder(R.drawable.user)
                            .error(R.drawable.user)
                            .into(viewHolder.imageView);
                // imageLoader.DisplayImage(context,imgUrl,holder.imageView,null,150,150,R.drawable.profile_default);
                ((RecyclerView.ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
              /*  Intent intent = new Intent(context, Dr_Profile.class);
                if (!rowItem.getId().equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID)))

                    intent.putExtra("UserId", rowItem.getId());
                context.startActivity(intent);*/
                        menu_popup(((RecyclerView.ViewHolder) viewHolder).itemView, rowItem, position);
                    }
                });
                break;
        }
    }

    public class HeaderView extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView postAn,privacy;

        public HeaderView(View itemView) {
            super(itemView);
            postAn = (TextView) itemView.findViewById(R.id.info);
            privacy=(TextView)itemView.findViewById(R.id.privacy);
        }
    }


    @Override
    public int getItemCount() {
        return rowItems.size();
    }

    public void menu_popup(View view, final UserList userList, final int position) {
        PopupMenu popup = new PopupMenu((Activity)context, view, Gravity.CENTER);
        popup.getMenuInflater().inflate(R.menu.view_profile_make_admin, popup.getMenu());
        if (isLogedInUser_Admin) {
            popup.getMenu().findItem(R.id.make_admin).setVisible(true);
            if (userList.getIs_admin() != null && userList.getIs_admin().equalsIgnoreCase("1")) {
                popup.getMenu().findItem(R.id.make_admin).setVisible(false);
            }
        } else {
            popup.getMenu().findItem(R.id.make_admin).setVisible(false);
        }


        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                //  int index = info.position;
                //  System.out.print(index);
                switch (item.getItemId()) {
                    case R.id.make_admin:
                        if (NetworkUtill.isNetworkAvailable(context)) {

                            MakeAdminAsynctask makeAdminAsynctask = new MakeAdminAsynctask(groupId, userList.getId(), sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN), position);
                            makeAdminAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            NetworkUtill.showNoInternetDialog(context);
                        }

                        break;
                    case R.id.view_profile:
                        Methods.profileUser(userList.getUser_type(), context, userList.getId());

                        break;
                }

                return true;
            }
        });

        popup.show();//showing popup menu
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position, List<Object> payloads) {

        if (!payloads.isEmpty()) {
            ViewHolder viewHolder = (ViewHolder) holder;

            if (payloads.get(0) instanceof String) {
                if (String.valueOf(payloads.get(0)).equalsIgnoreCase("1")) {
                    viewHolder.department.setVisibility(View.VISIBLE);


                } else {
                    viewHolder.department.setVisibility(View.GONE);

                }

            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    public class MakeAdminAsynctask extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String groupId, userId;

        int status1;
        HttpClient client;
        int pos;
        private String responseBody;

        public MakeAdminAsynctask(String groupId, String userId, String authToken, int pos) {
            this.groupId = groupId;
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


                    rowItems.get(pos).setIs_admin("1");
                    notifyItemChanged(pos, new String(rowItems.get(pos).getIs_admin()));


                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url;
                url = AppUrl.BaseUrl + "group/make-admin";

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

                pairs.add(new BasicNameValuePair("groupID", groupId));

                pairs.add(new BasicNameValuePair("userID[0]", userId + ""));
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
