package com.app.nirogstreet.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.CommentsRepilesActivity;
import com.app.nirogstreet.activites.CreateDrProfile;
import com.app.nirogstreet.activites.Dr_Profile;
import com.app.nirogstreet.activites.EditCommentActivity;
import com.app.nirogstreet.model.CommentsModel;
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
 * Created by Preeti on 27-10-2017.
 */
public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.MyHolderView> {
    ArrayList<CommentsModel> commentsModels;
    String authToken, userId;
    SesstionManager sessionManager;
    Context context;
    boolean isVisible;
    String feedId;
    String type;
    String url;


    public CommentsRecyclerAdapter(Context context, ArrayList<CommentsModel> commentsModels, String feedId, boolean isvisible, String type) {
        this.commentsModels = commentsModels;
        this.context = context;
        this.feedId = feedId;
        this.isVisible = isvisible;
        this.type = type;
        sessionManager = new SesstionManager(context);
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        authToken = userDetails.get(SesstionManager.AUTH_TOKEN);
        userId = userDetails.get(SesstionManager.USER_ID);

    }

    @Override
    public MyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, null);
        MyHolderView viewHolder = new MyHolderView(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyHolderView holder, final int position) {
        try {
            final CommentsModel rowItem = commentsModels.get(position);
            try {
                Picasso.with(context)
                        .load(rowItem.getProfile_pic_url())
                        .placeholder(R.drawable.user)
                        .error(R.drawable.user)
                        .into(holder.imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (rowItem.getUserId().equalsIgnoreCase(AppUrl.NIROGSTREET_DESK_ID)) {

                if (rowItem.getFname() != null) {
                    if (rowItem.getLname() != null)
                        holder.name.setText(rowItem.getFname() + " " + rowItem.getLname());
                    else
                        holder.name.setText(rowItem.getFname());
                }
            } else {
                if (rowItem.getFname() != null) {
                    if (rowItem.getLname() != null)
                        holder.name.setText(Methods.getName(rowItem.getTitle(), rowItem.getFname() + " " + rowItem.getLname()));
                    else
                        holder.name.setText(Methods.getName(rowItem.getTitle(), rowItem.getFname()));
                }
            }
            if (commentsModels.get(position).isUserLiked()) {
                holder.like.setText("Unlike");
            } else {
                holder.like.setText("Like");

            }
            if (rowItem.getUserId().equalsIgnoreCase(sessionManager.getUserDetails().get(SesstionManager.USER_ID)) || rowItem.getUserId().equalsIgnoreCase(AppUrl.NIROGSTREET_DESK_ID)) {
                holder.del_editImageView.setVisibility(View.VISIBLE);
                holder.del_editImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteOrEditPopup(holder.del_editImageView, rowItem, position);

                    }
                });
            } else {
                if (sessionManager.getUserDetails().get(SesstionManager.USER_ID).equalsIgnoreCase(AppUrl.NIROGSTREET_DESK_ID)) {
                    holder.del_editImageView.setVisibility(View.VISIBLE);
                    holder.del_editImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteOrEditPopup(holder.del_editImageView, rowItem, position);

                        }
                    });

                } else
                    holder.del_editImageView.setVisibility(View.GONE);
            }

            holder.numberOfReplies.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CommentsRepilesActivity.class);
                    intent.putExtra("commentModel", commentsModels.get(position));
                    intent.putExtra("type", "1");

                    intent.putExtra("feedId", feedId);
                    context.startActivity(intent);
                }
            });
            holder.reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CommentsRepilesActivity.class);
                    intent.putExtra("commentModel", commentsModels.get(position));
                    intent.putExtra("feedId", feedId);
                    intent.putExtra("type", "1");

                    context.startActivity(intent);
                }
            });
            if (commentsModels.get(position).getCommentsModels() != null && commentsModels.get(position).getCommentsModels().size() >= 1) {
                holder.subComment.setVisibility(View.VISIBLE);
                if (rowItem.getCommentsModels().get(rowItem.getCommentsModels().size() - 1).getUserId().equalsIgnoreCase(AppUrl.NIROGSTREET_DESK_ID))

                    holder.subCommntName.setText(rowItem.getCommentsModels().get(rowItem.getCommentsModels().size() - 1).getFname());
                else
                    holder.subCommntName.setText(Methods.getName(rowItem.getCommentsModels().get(rowItem.getCommentsModels().size() - 1).getTitle(), rowItem.getCommentsModels().get(rowItem.getCommentsModels().size() - 1).getFname()));

                Glide.with(context)
                        .load(rowItem.getCommentsModels().get(rowItem.getCommentsModels().size() - 1).getProfile_pic_url()).placeholder(R.drawable.user) // Uri of the picture
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .crossFade()
                        .override(100, 100)
                        .into(holder.subcommentimg);
                // imageLoader.DisplayImage(context, rowItem.getCommentsModels().get(rowItem.getCommentsModels().size() - 1).getProfile_pic_url(), holder.subcommentimg, null, 150, 150, R.drawable.profile_default);
                holder.subcommnt.setText(rowItem.getCommentsModels().get(rowItem.getCommentsModels().size() - 1).getComment());
                Methods.hyperlink(holder.subcommnt, rowItem.getCommentsModels().get(rowItem.getCommentsModels().size() - 1).getComment(), context, 0);

                if (commentsModels.get(position).getCommentsModels().size() > 1) {
                    holder.numberOfReplies.setVisibility(View.VISIBLE);
                    int noOfReply = commentsModels.get(position).getCommentsModels().size() - 1;
                    if (noOfReply == 1) {
                        holder.numberOfReplies.setText("View " + noOfReply + " previous reply");

                    } else {
                        holder.numberOfReplies.setText("View " + noOfReply + " previous replies");

                    }
                } else {
                    holder.numberOfReplies.setVisibility(View.GONE);

                }
            } else {
                holder.subComment.setVisibility(View.GONE);

            }
            if (isVisible) {
                holder.likeslayout.setVisibility(View.VISIBLE);
                url = AppUrl.BaseUrl + "feed/delete-comment";
            } else {
                holder.likeslayout.setVisibility(View.GONE);
                url = AppUrl.BaseUrl + "feed/delete-sub-comment";
            }

            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommentsModel likesModel = commentsModels.get(position);
               /* Intent resultIntent = new Intent(context, Dr_Profile.class);

                resultIntent.putExtra("UserId", likesModel.getUserId());
                context.startActivity(resultIntent);*/
                    Methods.openUserActivities(context, likesModel.getUserId(), likesModel.getName(), likesModel.getProfile_pic_url(), likesModel.getTitle(), likesModel.getUser_type());

                    //  Methods.profileUser(likesModel.getUser_type(),context,likesModel.getUserId());
                }
            });
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommentsModel likesModel = commentsModels.get(position);

               /* Intent resultIntent = new Intent(context, Dr_Profile.class);
                resultIntent.putExtra("UserId", likesModel.getUserId());
                context.startActivity(resultIntent);*/
                    Methods.openUserActivities(context, likesModel.getUserId(), likesModel.getName(), likesModel.getProfile_pic_url(), likesModel.getTitle(), likesModel.getUser_type());

                }
            });
            //  holder.name.setText(rowItem.getName());
            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LikeCommentAsynctask likeCommentAsynctask = new LikeCommentAsynctask(commentsModels.get(position).getCommentId(), userId, authToken, position);
                    likeCommentAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            });
            holder.time.setText(rowItem.getTimeStamp());
            holder.totalLike.setText(commentsModels.get(position).getTotalLikes() + "");
            holder.message.setText(rowItem.getComment());
            Methods.hyperlink(holder.message, rowItem.getComment(), context, 0);
            // imageLoader.DisplayImage(context, rowItem.getProfile_pic_url(), holder.imageView, null, 150, 150, R.drawable.profile_default);
            holder.subComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CommentsRepilesActivity.class);
                    intent.putExtra("commentModel", commentsModels.get(position));
                    intent.putExtra("feedId", feedId);
                    intent.putExtra("type", type);
                    context.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBindViewHolder(MyHolderView holder, int position, List<Object> payloads) {
        if (!payloads.isEmpty()) {
            if (payloads.get(0) instanceof Integer) {
                holder.totalLike.setText(String.valueOf(payloads.get(0)));
                if (commentsModels.get(position).isUserLiked()) {
                    holder.like.setText("Unlike");
                } else {
                    holder.like.setText("Like");

                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public int getItemCount() {
        return commentsModels.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {
        public TextView message, time, name, like, totalLike, subcommnt, numberOfReplies, reply;
        CircleImageView imageView;
        ImageView del_editImageView;
        CircleImageView subcommentimg;
        LinearLayout subComment, likeslayout;

        TextView subCommntName;

        public MyHolderView(View convertView) {
            super(convertView);
            del_editImageView = (ImageView) convertView.findViewById(R.id.del_edit);
            subCommntName = (TextView) convertView.findViewById(R.id.subCommntName);
            subcommentimg = (CircleImageView) convertView.findViewById(R.id.subcommentimg);
            subcommnt = (TextView) convertView.findViewById(R.id.subcommnt);
            name = (TextView) convertView.findViewById(R.id.name);
            likeslayout = (LinearLayout) convertView.findViewById(R.id.likeslayout);
            numberOfReplies = (TextView) convertView.findViewById(R.id.numberOfReplies);
            totalLike = (TextView) convertView.findViewById(R.id.totalLike);
            subComment = (LinearLayout) convertView.findViewById(R.id.sub);
            reply = (TextView) convertView.findViewById(R.id.reply);
            like = (TextView) convertView.findViewById(R.id.like);
            message = (TextView) convertView.findViewById(R.id.comment_text);
            time = (TextView) convertView.findViewById(R.id.timestamp);
            imageView = (CircleImageView) convertView.findViewById(R.id.profileImage);
        }
    }

    public class LikeCommentAsynctask extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        int pos;
        String CommentId, userId;


        private String responseBody;
        HttpClient client;
        Context context;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public LikeCommentAsynctask(String commentId, String userId, String authToken, int pos) {
            this.CommentId = commentId;
            this.authToken = authToken;
            this.userId = userId;
            this.pos = pos;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (jo != null) {
                    if (jo.has("responce") && !jo.isNull("responce")) {
                        JSONObject res = jo.getJSONObject("responce");
                        int totallike = 0;
                        int userhasliked = 0;
                        if (res.has("totalLikes") && !res.isNull("totalLikes")) {
                            totallike = res.getInt("totalLikes");
                            commentsModels.get(pos).setTotalLikes(totallike);
                        }
                        if (res.has("user_hasLiked") && !res.isNull("user_hasLiked")) {
                            userhasliked = res.getInt("user_hasLiked");
                            if (userhasliked == 1) {
                                commentsModels.get(pos).setUserLiked(true);

                            } else {
                                commentsModels.get(pos).setUserLiked(false);

                            }
                        }
                        notifyItemChanged(pos, new Integer(commentsModels.get(pos).getTotalLikes()));

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {


                String url = AppUrl.BaseUrl + "feed/like-dislike-comment";
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
                pairs.add(new BasicNameValuePair("commentID", CommentId));
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

    public void deleteOrEditPopup(ImageView view, final CommentsModel commentsModel, final int position) {
        PopupMenu popup = new PopupMenu((Activity)context, view);
        popup.getMenuInflater().inflate(R.menu.popup_menu_edit_delete, popup.getMenu());


        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.edit:
                        Intent intent = new Intent(context, EditCommentActivity.class);
                        intent.putExtra("feedId", feedId);
                        intent.putExtra("type", "1");
                        if (!isVisible)
                            intent.putExtra("is_sub_comment", true);
                        intent.putExtra("commentId", commentsModel.getCommentId());
                        intent.putExtra("msg", commentsModel.getComment());
                        context.startActivity(intent);
                        break;
                    case R.id.del:
                        setDialog(commentsModel, position);
                        break;
                }
                /*if (item.getTitle().equals(R.string.SharePublic)) {


                } else if (item.getTitle().equals(R.string.shareonFriendsTimeline)) {

                } else {

                }*/
                return true;
            }
        });

        popup.show();//showing popup menu
    }

    public void setDialog(final CommentsModel feedModel, final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to Delete this Comment?");// Add the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                if (NetworkUtill.isNetworkAvailable(context)) {
                    DeletepostAsyncTask deletepostAsyncTask = new DeletepostAsyncTask(feedModel.getCommentId(), userId, authToken, position);
                    deletepostAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    NetworkUtill.showNoInternetDialog(context);
                    //feedId
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.cancel();
            }
        });
        builder.show();
// Set other dialog properties

// Create the AlertDialog
        AlertDialog dialog = builder.create();
    }

    public class DeletepostAsyncTask extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String feedId, userId;
        int position;

        private String responseBody;
        HttpClient client;
        Context context;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public DeletepostAsyncTask(String feedId, String userId, String authToken, int position) {
            this.feedId = feedId;
            this.authToken = authToken;
            this.position = position;
            this.userId = userId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (jo != null) {
                    if (isVisible) {
                        if (jo.has("responce") && !jo.isNull("responce")) {
                            JSONObject res = jo.getJSONObject("responce");
                            if (res.has("total_comment") && !res.isNull("total_comment")) {
                                ApplicationSingleton.setIsCommented(true);
                                ApplicationSingleton.setNo_of_count(res.getInt("total_comment"));

                            }
                        }
                    }
                    commentsModels.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, commentsModels.size());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {


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
                pairs.add(new BasicNameValuePair("userID", sessionManager.getUserDetails().get(SesstionManager.USER_ID)));
                pairs.add(new BasicNameValuePair("commentID", feedId));
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
