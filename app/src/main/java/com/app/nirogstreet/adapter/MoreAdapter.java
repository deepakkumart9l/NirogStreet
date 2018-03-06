package com.app.nirogstreet.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.AlbumGallary;
import com.app.nirogstreet.activites.AppointmentActivity;
import com.app.nirogstreet.activites.CommentsActivity;
import com.app.nirogstreet.activites.CommunitiesDetails;
import com.app.nirogstreet.activites.Dr_Profile;
import com.app.nirogstreet.activites.FullScreenImage;
import com.app.nirogstreet.activites.LikesDisplayActivity;
import com.app.nirogstreet.activites.MainActivity;
import com.app.nirogstreet.activites.OpenDocument;
import com.app.nirogstreet.activites.PostDetailActivity;
import com.app.nirogstreet.activites.PostEditActivity;
import com.app.nirogstreet.activites.PostingActivity;
import com.app.nirogstreet.activites.ProfileActivity;
import com.app.nirogstreet.activites.PublicShare;
import com.app.nirogstreet.activites.ReferalActivity;
import com.app.nirogstreet.activites.ShareOnFriendsTimeline;
import com.app.nirogstreet.activites.Student_Profile;
import com.app.nirogstreet.activites.VideoPlay_Activity;
import com.app.nirogstreet.activites.YoutubeVideo_Play;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.FeedModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.parser.FeedParser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.Methods;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
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
 * Created by Preeti on 31-10-2017.
 */

public class MoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    int positionat;
    SpannableString span2, str3, spanStatus, spanStatus2;
    String text, videourl, title;


    FrameLayout customViewContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;

    private String authToken, userId;
    private View mCustomView;

    WebView webView;
    SpannableString span;


    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int FEED_TYPE_YOUTUBEVIDEO_LINK = 3;
    private static final int LINK_TYPE_YOUTUBE_VIDEO = 1;
    private static final int LINK_TYPE_WEB_LINK = 2;
    private static final int FEED_TYPE_IMAGE = 2;
    private static final int TEXT_ONLY = 1;
    private static final int VIDEO_UPLOADED_BY_USER = 5;
    private static final int FEED_TYPE_DOCUMENTS = 6;
    private static final int FEED_FOR_ALBUM = 3;
    ArrayList<FeedModel> feedModels;
    Context context;
    Activity activity;
    SesstionManager sesstionManager;
    CircularProgressBar circularProgressBar;
    String groupId = "";
    private SpannableStringBuilder builder, builder1;
    SpannableString str2, str4;

    public MoreAdapter(Context context, ArrayList<FeedModel> feedModel, Activity activity, String s, FrameLayout customViewContainer, CircularProgressBar circularProgressBar) {
        this.context = context;
        this.feedModels = feedModel;
        this.activity = activity;
        this.customViewContainer = customViewContainer;
        this.circularProgressBar = circularProgressBar;
        sesstionManager = new SesstionManager(context);
        HashMap<String, String> userDetails = sesstionManager.getUserDetails();
        authToken = userDetails.get(SesstionManager.AUTH_TOKEN);
        userId = userDetails.get(SesstionManager.USER_ID);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {

        if (!payloads.isEmpty()) {
            MyViewHolder viewHolder = (MyViewHolder) holder;

            if (payloads.get(0) instanceof String) {
                if (String.valueOf(payloads.get(0)).equalsIgnoreCase("1"))
                    viewHolder.likesTextView.setText(String.valueOf(payloads.get(0)) + " Like");
                else
                    viewHolder.likesTextView.setText(String.valueOf(payloads.get(0)) + " Likes");

                if (!viewHolder.feedlikeimg.isSelected())
                    viewHolder.feedlikeimg.setSelected(true);
                else
                    viewHolder.feedlikeimg.setSelected(false);
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case TYPE_HEADER:
                UserDetailModel userDetailModel1 = ApplicationSingleton.getUserDetailModel();

                HeaderView myViewHolder = (HeaderView) holder;
                myViewHolder.profileRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sesstionManager.getUserDetails().get(SesstionManager.USER_TYPE).equalsIgnoreCase(AppUrl.DOCTOR_ROLE)) {
                            Intent intent = new Intent(context, Dr_Profile.class);
                            context.startActivity(intent);
                        } else if (sesstionManager.getUserDetails().get(SesstionManager.USER_TYPE).equalsIgnoreCase(AppUrl.STUDENT_ROLE)) {
                            Intent intent = new Intent(context, Student_Profile.class);
                            context.startActivity(intent);
                        }
                    }
                });


                try {
                    SesstionManager sesstionManager = new SesstionManager(context);
                    String name = sesstionManager.getUserDetails().get(SesstionManager.KEY_FNAME) + " " + sesstionManager.getUserDetails().get(SesstionManager.KEY_LNAME);
                    if (userDetailModel1.getProfile_pic() != null && !userDetailModel1.getProfile_pic().equalsIgnoreCase("")) {

                        Picasso.with(context)
                                .load(userDetailModel1.getProfile_pic())
                                .placeholder(R.drawable.user)
                                .error(R.drawable.user)
                                .into(myViewHolder.circleImageView);
                        sesstionManager.updateProfile(userDetailModel1.getProfile_pic());

                    }

                    myViewHolder.card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ReferalActivity.class);
                            context.startActivity(intent);
                        }
                    });
                    if (name != null)
                        if (sesstionManager.getUserDetails().get(SesstionManager.USER_ID).equalsIgnoreCase(AppUrl.NIROGSTREET_DESK_ID)) {
                            myViewHolder.nameTv.setText(name);
                        } else {

                            myViewHolder.nameTv.setText(Methods.getName(sesstionManager.getUserDetails().get(SesstionManager.TITLE), name));


                        }
                    if (userDetailModel1.getEmail() != null)
                        myViewHolder.emailTv.setText(userDetailModel1.getEmail());
                    if (userDetailModel1.getMobile() != null)
                        myViewHolder.phoneTv.setText(userDetailModel1.getMobile());
                    if (userDetailModel1.getDob() != null)
                        myViewHolder.yearOfBirthTv.setText(userDetailModel1.getDob());
                    if (userDetailModel1.getExperience() != null && !userDetailModel1.getExperience().equalsIgnoreCase(""))
                        myViewHolder.view_detail.setText(userDetailModel1.getExperience() + " years experience");
                    else
                        myViewHolder.view_detail.setVisibility(View.GONE);
                    if (userDetailModel1 != null && userDetailModel1.getSpecializationModels() != null) {
                        myViewHolder.QualificationTv.setText(getSelectedNameCsv(userDetailModel1));
                    }
                    if (userDetailModel1 != null && userDetailModel1.getWebSite() != null && !userDetailModel1.getWebSite().equalsIgnoreCase("")) {
                        myViewHolder.WebTv.setText(userDetailModel1.getWebSite());
                        myViewHolder.webSite_icon.setVisibility(View.VISIBLE);
                    } else {
                        myViewHolder.WebTv.setVisibility(View.GONE);
                        myViewHolder.webSite_icon.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case TYPE_ITEM:
                try {
                    final MyViewHolder viewHolder = (MyViewHolder) holder;

                    int feed_type = 0;
                    final FeedModel feedModel = feedModels.get(position);
                    if (feedModel.getFeed_type() != null)
                        feed_type =
                                Integer.parseInt(feedModel.getFeed_type());
                    int link_type = 0;
                    if (feedModel.getLink_type() != null) {
                        link_type = Integer.parseInt(feedModel.getLink_type());
                    }
                    viewHolder.frameVideoFrameLayout.setVisibility(View.GONE);
                    viewHolder.playicon.setVisibility(View.GONE);
                    viewHolder.docTypeLayout.setVisibility(View.GONE);
                    viewHolder.videoView.setVisibility(View.GONE);
                    viewHolder.comment_section.setVisibility(View.GONE);
                    viewHolder.linkImageView.setVisibility(View.GONE);
                    viewHolder.link_title_des_lay.setVisibility(View.GONE);
                    viewHolder.left_view.setVisibility(View.GONE);
                    viewHolder.relativeLayout1.setVisibility(View.GONE);
                    viewHolder.parentLay.setVisibility(View.GONE);
                    viewHolder.right_view.setVisibility(View.GONE);
                    viewHolder.bottom_view.setVisibility(View.GONE);
                    viewHolder.two_or_moreLinearLayout.setVisibility(View.GONE);
                    viewHolder.docTypeLayout.setVisibility(View.GONE
                    );
                    viewHolder.linkTitleTextView.setVisibility(View.GONE);
                    viewHolder.linkDescriptiontextView.setVisibility(View.GONE);
                    viewHolder.feedImageView.setVisibility(View.GONE);
                    switch (feed_type) {
                        case FEED_TYPE_YOUTUBEVIDEO_LINK:
                            switch (link_type) {
                                case LINK_TYPE_YOUTUBE_VIDEO:
                                    viewHolder.link_title_des_lay.setVisibility(View.GONE);
                                    viewHolder.left_view.setVisibility(View.GONE);
                                    viewHolder.right_view.setVisibility(View.GONE);
                                    viewHolder.bottom_view.setVisibility(View.GONE);
                                    viewHolder.playicon.setVisibility(View.VISIBLE);
                                    viewHolder.frameVideoFrameLayout.setVisibility(View.GONE);
                                    viewHolder.linkImageView.setVisibility(View.GONE);
                                    viewHolder.linkTitleTextView.setVisibility(View.GONE);
                                    viewHolder.linkDescriptiontextView.setVisibility(View.GONE);
                                    viewHolder.feedImageView.setVisibility(View.VISIBLE);
                                    viewHolder.two_or_moreLinearLayout.setVisibility(View.GONE);
                                    viewHolder.docTypeLayout.setVisibility(View.GONE);
                                    viewHolder.profileSectionLinearLayout.setVisibility(View.VISIBLE);
                                    viewHolder.anniversaryLinearLayout.setVisibility(View.GONE);
                                    viewHolder.anniverasaryLayoutImage.setVisibility(View.GONE);
                                    viewHolder.videoView.setVisibility(View.GONE);
                                    viewHolder.playicon.setVisibility(View.VISIBLE);
                                    viewHolder.relativeLayout1.setVisibility(View.VISIBLE);

                                    Picasso.with(context)
                                            .load(feedModel.getUrl_image())
                                            .placeholder(R.drawable.default_)
                                            .into(viewHolder.feedImageView);

                                    viewHolder.feedImageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(context, YoutubeVideo_Play.class);
                                            intent.putExtra("videourl", feedModel.getFeed_source());
                                            context.startActivity(intent);
                                        }
                                    });
                                    break;
                                case LINK_TYPE_WEB_LINK:
                                    viewHolder.frameVideoFrameLayout.setVisibility(View.GONE);
                                    viewHolder.playicon.setVisibility(View.GONE);
                                    viewHolder.two_or_moreLinearLayout.setVisibility(View.GONE);
                                    viewHolder.link_title_des_lay.setVisibility(View.VISIBLE);
                                    viewHolder.left_view.setVisibility(View.VISIBLE);
                                    viewHolder.right_view.setVisibility(View.VISIBLE);
                                    viewHolder.bottom_view.setVisibility(View.VISIBLE);
                                    viewHolder.relativeLayout1.setVisibility(View.VISIBLE);

                                    viewHolder.videoView.setVisibility(View.GONE);
                                    viewHolder.profileSectionLinearLayout.setVisibility(View.VISIBLE);
                                    viewHolder.linkImageView.setVisibility(View.VISIBLE);
                                    viewHolder.feedImageView.setVisibility(View.GONE);
                                    viewHolder.linkTitleTextView.setVisibility(View.VISIBLE);
                                    viewHolder.linkDescriptiontextView.setVisibility(View.VISIBLE);

                                    if (feedModel.getUrl_title() != null && feedModel.getUrl_title().length() > 0 && !feedModel.getUrl_title().equalsIgnoreCase("")) {
                                        viewHolder.linkTitleTextView.setText(feedModel.getUrl_title());
                                    } else {
                                        viewHolder.linkTitleTextView.setVisibility(View.GONE);
                                    }
                                    viewHolder.linkDescriptiontextView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String urlString = feedModel.getFeed_source();
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(urlString));
                                            context.startActivity(i);
                                        }
                                    });
                                    viewHolder.linkTitleTextView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String urlString = feedModel.getFeed_source();
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(urlString));
                                            context.startActivity(i);
                                        }
                                    });
                                    viewHolder.linkDescriptiontextView.setText(feedModel.getUrl_description());

                                    Picasso.with(context)
                                            .load(feedModel.getUrl_image())
                                            .placeholder(R.drawable.default_)
                                            .error(R.drawable.default_)
                                            .into(viewHolder.linkImageView);
                                    viewHolder.linkImageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String urlString = feedModel.getFeed_source();
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(urlString));
                                            context.startActivity(i);
                                        }
                                    });
                                    break;
                            }
                            break;
                        case FEED_TYPE_IMAGE:
                            viewHolder.frameVideoFrameLayout.setVisibility(View.GONE);
                            viewHolder.playicon.setVisibility(View.GONE);
                            viewHolder.two_or_moreLinearLayout.setVisibility(View.GONE);
                            viewHolder.linkImageView.setVisibility(View.GONE);
                            viewHolder.linkTitleTextView.setVisibility(View.GONE);
                            viewHolder.link_title_des_lay.setVisibility(View.GONE);
                            viewHolder.left_view.setVisibility(View.GONE);
                            viewHolder.right_view.setVisibility(View.GONE);
                            viewHolder.bottom_view.setVisibility(View.GONE);
                            viewHolder.relativeLayout1.setVisibility(View.VISIBLE);


                            viewHolder.linkDescriptiontextView.setVisibility(View.GONE);
                            viewHolder.CommentSectionLinearLayout.setVisibility(View.VISIBLE);
                            viewHolder.profileSectionLinearLayout.setVisibility(View.VISIBLE);
                            viewHolder.docTypeLayout.setVisibility(View.GONE);
                            viewHolder.videoView.setVisibility(View.GONE);
                            String extension = feedModel.getFeed_source().substring(feedModel.getFeed_source().lastIndexOf("."));

                            if (extension.equalsIgnoreCase(".gif")) {
                                viewHolder.feedImageView.setVisibility(View.VISIBLE);
                                Glide.with(context)
                                        .load(feedModel.getFeed_source())
                                        .asGif().placeholder(R.drawable.default_)
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(viewHolder.feedImageView);

                                viewHolder.feedImageView.setVisibility(View.VISIBLE);
                                viewHolder.feedImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(context, FullScreenImage.class);
                                        intent.putExtra("ImageUrl", feedModel.getFeed_source());
                                        context.startActivity(intent);

                                    }
                                });
                            } else {
                                if (feedModel.getFeedSourceArrayList().size() > 1) {
                                    ArrayList<String> strings = feedModel.getFeedSourceArrayList();
                                    if (strings.size() > 1) {
                                        if (strings.size() > 2) {
                                            viewHolder.moreLinearLayout.setVisibility(View.VISIBLE);
                                            int num = strings.size() - 1;
                                            viewHolder.moreviewTextView.setText("+" + num + "");
                                        } else {
                                            viewHolder.moreLinearLayout.setVisibility(View.VISIBLE);
                                            viewHolder.moreLinearLayout.setVisibility(View.GONE);
                                            int num = strings.size() - 1;
                                            viewHolder.moreviewTextView.setText("+" + num + "");
                                        }
                                        viewHolder.two_or_moreLinearLayout.setVisibility(View.VISIBLE);
                                        viewHolder.feedImageView.setVisibility(View.GONE);


                                        Picasso.with(context)
                                                .load(strings.get(0))
                                                .placeholder(R.drawable.default_)
                                                .error(R.drawable.default_)
                                                .into(viewHolder.imageFirstImageView);
                                        Picasso.with(context)
                                                .load(strings.get(1))
                                                .placeholder(R.drawable.default_)
                                                .error(R.drawable.default_)
                                                .into(viewHolder.imageSecImageView);
                                    } else {
                                        String singleImageUrl = strings.get(0);
                                        viewHolder.two_or_moreLinearLayout.setVisibility(View.GONE);


                                        Picasso.with(context)
                                                .load(singleImageUrl)
                                                .placeholder(R.drawable.default_)
                                                .error(R.drawable.default_)
                                                .into(viewHolder.feedImageView);
                                    }

                                    viewHolder.two_or_moreLinearLayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(context, AlbumGallary.class);
                                            intent.putExtra("position", 0);
                                            intent.putExtra("images", feedModel.getFeedSourceArrayList());
                                            context.startActivity(intent);
                                        }
                                    });
                                    viewHolder.feedImageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            Intent intent = new Intent(context, AlbumGallary.class);
                                            intent.putExtra("position", 0);
                                            intent.putExtra("images", feedModel.getFeedSourceArrayList());
                                            context.startActivity(intent);
                                        }
                                    });
                                } else {
                                    viewHolder.feedImageView.setVisibility(View.VISIBLE);

                                    Picasso.with(context)
                                            .load(feedModel.getFeedSourceArrayList().get(0))
                                            .placeholder(R.drawable.default_)
                                            .error(R.drawable.default_)
                                            .into(viewHolder.feedImageView);
                                    viewHolder.feedImageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(context, AlbumGallary.class);
                                            intent.putExtra("position", 0);
                                            intent.putExtra("images", feedModel.getFeedSourceArrayList());
                                            context.startActivity(intent);
                                        }
                                    });
                                }
                            }
                            break;
                        case VIDEO_UPLOADED_BY_USER:
                            viewHolder.frameVideoFrameLayout.setVisibility(View.GONE);
                            viewHolder.playicon.setVisibility(View.VISIBLE);
                            viewHolder.linkImageView.setVisibility(View.GONE);
                            viewHolder.linkTitleTextView.setVisibility(View.GONE);
                            viewHolder.feedImageView.setVisibility(View.VISIBLE);
                            viewHolder.two_or_moreLinearLayout.setVisibility(View.GONE);
                            viewHolder.two_or_moreLinearLayout.setVisibility(View.GONE);
                            viewHolder.linkDescriptiontextView.setVisibility(View.GONE);
                            viewHolder.link_title_des_lay.setVisibility(View.GONE);
                            viewHolder.left_view.setVisibility(View.GONE);
                            viewHolder.relativeLayout1.setVisibility(View.GONE);

                            viewHolder.right_view.setVisibility(View.GONE);
                            viewHolder.bottom_view.setVisibility(View.GONE);
                            viewHolder.CommentSectionLinearLayout.setVisibility(View.VISIBLE);
                            viewHolder.profileSectionLinearLayout.setVisibility(View.VISIBLE);
                            viewHolder.profileSectionLinearLayout.setVisibility(View.VISIBLE);
                            viewHolder.videoView.setVisibility(View.GONE);
                            viewHolder.webView.setVisibility(View.GONE);
                            viewHolder.relativeLayout1.setVisibility(View.VISIBLE);

                            if (feedModel.getUrl_image() != null && !feedModel.getUrl_image().equalsIgnoreCase("")) {
                                Picasso.with(context)
                                        .load(feedModel.getUrl_image())
                                        .placeholder(R.drawable.default_)
                                        .error(R.drawable.default_)
                                        .into(viewHolder.feedImageView);
                            } else {
                                viewHolder.feedImageView.setImageResource(R.drawable.default_videobg);
                            }
                            viewHolder.feedImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(context, VideoPlay_Activity.class);
                                    intent.putExtra("video", feedModel.getFeed_source());
                                    intent.putExtra("videotype", "native");
                                    intent.putExtra("feedVideo", true);
                                    context.startActivity(intent);
                                }
                            });
                            break;

                        case FEED_TYPE_DOCUMENTS:
                            viewHolder.frameVideoFrameLayout.setVisibility(View.GONE);
                            viewHolder.playicon.setVisibility(View.GONE);
                            viewHolder.docTypeLayout.setVisibility(View.VISIBLE);
                            viewHolder.videoView.setVisibility(View.GONE);
                            viewHolder.linkImageView.setVisibility(View.GONE);
                            viewHolder.two_or_moreLinearLayout.setVisibility(View.GONE);
                            viewHolder.link_title_des_lay.setVisibility(View.GONE);
                            viewHolder.relativeLayout1.setVisibility(View.VISIBLE);
                            viewHolder.left_view.setVisibility(View.GONE);
                            viewHolder.right_view.setVisibility(View.GONE);
                            viewHolder.bottom_view.setVisibility(View.GONE);
                            viewHolder.linkTitleTextView.setVisibility(View.GONE);
                            viewHolder.linkDescriptiontextView.setVisibility(View.GONE);
                            viewHolder.feedImageView.setVisibility(View.GONE);
                            viewHolder.CommentSectionLinearLayout.setVisibility(View.VISIBLE);
                            viewHolder.profileSectionLinearLayout.setVisibility(View.VISIBLE);
                            if (feedModel != null)
                                if (feedModel.getFeed_source() != null) {
                                    try {
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (feedModel.getDoc_name() != null) {
                                        viewHolder.docNameTextView.setText(feedModel.getDoc_name());
                                    }
                                    if (feedModel.getDoc_Type() != null) {
                                        viewHolder.docTypeTextView.setText(feedModel.getDoc_Type());
                                    }
                                    viewHolder.buttondownload.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(context, OpenDocument.class);
                                            intent.putExtra("url", feedModel.getFeed_source());
                                            context.startActivity(intent);
                                        }
                                    });
                                }
                            break;
                        case TEXT_ONLY:
                            viewHolder.frameVideoFrameLayout.setVisibility(View.GONE);
                            viewHolder.playicon.setVisibility(View.GONE);
                            viewHolder.docTypeLayout.setVisibility(View.VISIBLE);
                            viewHolder.videoView.setVisibility(View.GONE);
                            viewHolder.linkImageView.setVisibility(View.GONE);
                            viewHolder.link_title_des_lay.setVisibility(View.GONE);
                            viewHolder.left_view.setVisibility(View.GONE);
                            viewHolder.right_view.setVisibility(View.GONE);
                            viewHolder.bottom_view.setVisibility(View.GONE);
                            viewHolder.relativeLayout1.setVisibility(View.GONE);

                            viewHolder.two_or_moreLinearLayout.setVisibility(View.GONE);
                            viewHolder.docTypeLayout.setVisibility(View.GONE
                            );
                            viewHolder.linkTitleTextView.setVisibility(View.GONE);
                            viewHolder.linkDescriptiontextView.setVisibility(View.GONE);
                            viewHolder.feedImageView.setVisibility(View.GONE);
                            viewHolder.CommentSectionLinearLayout.setVisibility(View.VISIBLE);
                            viewHolder.profileSectionLinearLayout.setVisibility(View.VISIBLE);
                            break;

                    }

                    if (feedModel.getMessage() != null && !feedModel.getMessage().equalsIgnoreCase("")) {
                        viewHolder.statusTextView.setVisibility(View.VISIBLE);
                        viewHolder.statusTextView.setText(feedModel.getMessage());

                        if (feedModel.getMessage().length() > 170)
                            if (feedModel.getMessage().length() > 170) {
                                try {
                                    builder1 = new SpannableStringBuilder();
                                    spanStatus = new SpannableString(feedModel.getMessage().substring(0, 170));

                                    spanStatus.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanStatus.length(), 0);
                                    Methods.hyperlinkSet(viewHolder.statusTextView, spanStatus.toString(), context, feedModel.getIs_pin(), spanStatus);
                                    builder1.append(spanStatus);
                                    spanStatus2 = new SpannableString(" ... view more");
                                    spanStatus2.setSpan(new ForegroundColorSpan(Color.rgb(148, 148, 156)), 0, spanStatus2.length(), 0);

                                    builder1.append(spanStatus2);
                                    ClickableSpan clickSpan1 = new ClickableSpan() {
                                        @Override
                                        public void updateDrawState(TextPaint ds) {
                                            ds.setColor(context.getResources().getColor(R.color.cardbluebackground));// you can use custom color
                                            ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                                            ds.setUnderlineText(false);// this remove the underline
                                        }

                                        @Override
                                        public void onClick(View textView) {
                                            ApplicationSingleton.setPostSelectedPostion(position);
                                            Intent intent = new Intent(context, PostDetailActivity.class);
                                            intent.putExtra("feedId", feedModel.getFeed_id());
                                            context.startActivity(intent);
                                        }
                                    };
                                    String thirdspan = spanStatus2.toString();
                                    int third = builder1.toString().indexOf(thirdspan);
                                    builder1.setSpan(clickSpan1, third, third + spanStatus2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    viewHolder.statusTextView.setText(builder1, TextView.BufferType.SPANNABLE);
                                    viewHolder.statusTextView.setMovementMethod(LinkMovementMethod.getInstance());

                                    if (feedModel.getMessage() != null && feedModel.getMessage().length() > 0) {

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                viewHolder.statusTextView.setText(feedModel.getMessage());

                            }
                        Methods.hyperlink(viewHolder.statusTextView, feedModel.getMessage(), context, feedModel.getIs_pin());

                    } else {
                        viewHolder.statusTextView.setVisibility(View.GONE);

                    }
                    if (feedModel.getTotal_likes() != null && !feedModel.getTotal_likes().equalsIgnoreCase("0")) {
                        if (feedModel.getTotal_likes().equalsIgnoreCase("1"))
                            viewHolder.likesTextView.setText(feedModel.getTotal_likes() + " Like");
                        else
                            viewHolder.likesTextView.setText(feedModel.getTotal_likes() + " Likes");

                    } else {
                        viewHolder.likesTextView.setText("0 Likes");

                    }
                    if (feedModel.getTotal_comments() != null) {
                        if (feedModel.getTotal_comments().equalsIgnoreCase("0") || feedModel.getTotal_comments().equalsIgnoreCase("1"))

                            viewHolder.commntsTextView.setText(feedModel.getTotal_comments() + " Comment");
                        else
                            viewHolder.commntsTextView.setText(feedModel.getTotal_comments() + " Comments");

                    } else {
                        viewHolder.commntsTextView.setText("0 Comments");

                    }
                    final UserDetailModel userDetailModel = feedModel.getUserDetailModel_creator();

                    if (userDetailModel.getProfile_pic() != null && !userDetailModel.getProfile_pic().equalsIgnoreCase("")) {
                        Picasso.with(context)
                                .load(userDetailModel.getProfile_pic())
                                .placeholder(R.drawable.user)
                                .error(R.drawable.user)
                                .into(viewHolder.profileImageView);
                    } else {
                        viewHolder.profileImageView.setImageResource(R.drawable.user);
                    }
                    if (feedModel.commentsModel != null) {
                        viewHolder.comment_section.setVisibility(View.VISIBLE);
                        if (feedModel.getCommentsModel().getProfile_pic_url() != null) {
                            Picasso.with(context)
                                    .load(feedModel.getCommentsModel().getProfile_pic_url())
                                    .placeholder(R.drawable.user)
                                    .into(viewHolder.profileImage);
                        } else {
                            viewHolder.profileImage.setImageResource(R.drawable.user);
                        }
                        viewHolder.user_comment.setText(feedModel.commentsModel.getComment());
                        if(feedModel.getCommentsModel().getTitle()!=null)
                            viewHolder.commenter_name.setText(Methods.getName(feedModel.getCommentsModel().getTitle(), feedModel.getCommentsModel().getName()));
                        else
                            viewHolder.commenter_name.setText( feedModel.getCommentsModel().getName());

                        viewHolder.user_comment_time.setText(feedModel.getCommentsModel().getTimeStamp());

                        viewHolder.user_comment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, CommentsActivity.class);
                                intent.putExtra("feedId", feedModel.getFeed_id());
                                intent.putExtra("type","1");

                                context.startActivity(intent);
                            }
                        });
                        viewHolder.profileImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Methods.openUserActivities(context,feedModel.getCommentsModel().getUserId(),feedModel.getCommentsModel().getName(),feedModel.getCommentsModel().getProfile_pic_url(),feedModel.getCommentsModel().getTitle(),feedModel.getCommentsModel().getUser_type());

                            }
                        });
                        viewHolder.commenter_name.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Methods.openUserActivities(context,feedModel.getCommentsModel().getUserId(),feedModel.getCommentsModel().getName(),feedModel.getCommentsModel().getProfile_pic_url(),feedModel.getCommentsModel().getTitle(),feedModel.getCommentsModel().getUser_type());

                            }
                        });
                    }
                    viewHolder.nameTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Methods.openUserActivities(context,userDetailModel.getUserId(),userDetailModel.getName(),userDetailModel.getProfile_pic(),userDetailModel.getTitle(),userDetailModel.getUser_Type());
                         //   Methods.profileUser(userDetailModel.getUser_Type(), context, userDetailModel.getUserId());

                        }
                    });
                    if (feedModel.getCreated() != null) {
                        try {
                            long now = System.currentTimeMillis();
                            long datetime1 = Integer.parseInt(feedModel.getCreated()) * 1000L;
                            CharSequence relavetime1 = DateUtils.getRelativeTimeSpanString(
                                    datetime1,
                                    now,
                                    DateUtils.SECOND_IN_MILLIS);
                            viewHolder.timeStampTextView.setText(relavetime1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }                    }
                    if (feedModel.getTitleQuestion() != null && !feedModel.getTitleQuestion().equalsIgnoreCase("")) {
                        viewHolder.QuestionTextView.setText(feedModel.getTitleQuestion());
                        viewHolder.QuestionTextView.setVisibility(View.VISIBLE);
                        Methods.hyperlink(viewHolder.QuestionTextView, feedModel.getTitleQuestion(), context, feedModel.getIs_pin());

                    } else {
                        viewHolder.QuestionTextView.setVisibility(View.GONE);

                    }
                    if (feedModel.getUser_has_liked() == 1) {
                        viewHolder.feedlikeimg.setSelected(true);
                    } else {
                        viewHolder.feedlikeimg.setSelected(false);

                    }
                    viewHolder.feedcommentlisting.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, CommentsActivity.class);
                            intent.putExtra("feedId", feedModel.getFeed_id());
                            intent.putExtra("type","1");

                            context.startActivity(intent);

                        }
                    });
                    viewHolder.likeFeedLinearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            positionat = position;
                            if (NetworkUtill.isNetworkAvailable(context)) {
                                LikePostAsynctask likePostAsynctask = new LikePostAsynctask(feedModel.getFeed_id(), userId, authToken, feedModel.getUser_has_liked());
                                likePostAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                NetworkUtill.showNoInternetDialog(context);
                            }
                        }
                    });
                    viewHolder.feeddeletelistingLinearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sharePopup(viewHolder.feeddeletelistingLinearLayout, feedModel);
                        }
                    });
                    viewHolder.feedlikeimg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            positionat = position;
                            if (NetworkUtill.isNetworkAvailable(context)) {
                                LikePostAsynctask likePostAsynctask = new LikePostAsynctask(feedModel.getFeed_id(), userId, authToken, feedModel.getUser_has_liked());
                                likePostAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                NetworkUtill.showNoInternetDialog(context);
                            }
                        }
                    });
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ApplicationSingleton.setPostSelectedPostion(position);
                            Intent intent = new Intent(context, PostDetailActivity.class);
                            intent.putExtra("feedId", feedModel.getFeed_id());
                            context.startActivity(intent);

                        }
                    });
                    viewHolder.feedlikeLinearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, LikesDisplayActivity.class);
                            intent.putExtra("feedId", feedModel.getFeed_id());
                            context.startActivity(intent);
                        }
                    });
                    viewHolder.delImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteOrEditPopup(viewHolder.delImageView, feedModel, position);
                        }
                    });
                    if (feedModel.getUserDetailModel_creator().getUserId().equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID))) {
                        viewHolder.delImageView.setVisibility(View.VISIBLE);
                    } else {
                        if (sesstionManager.getUserDetails().get(SesstionManager.USER_ID).equalsIgnoreCase(AppUrl.NIROGSTREET_DESK_ID)) {
                            viewHolder.delImageView.setVisibility(View.VISIBLE);


                        } else
                            viewHolder.delImageView.setVisibility(View.GONE);
                    }
                    if (feedModel.getEnable_comment().equalsIgnoreCase("1")) {
                        viewHolder.feedcommentlistingLinearLayout.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.feedcommentlistingLinearLayout.setVisibility(View.GONE);

                    }
                    viewHolder.likesTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LikesDisplayActivity.class);
                            intent.putExtra("feedId", feedModel.getFeed_id());
                            context.startActivity(intent);
                        }
                    });
                    viewHolder.commntsTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, CommentsActivity.class);
                            intent.putExtra("feedId", feedModel.getFeed_id());
                            intent.putExtra("type","1");

                            ApplicationSingleton.setPost_position(position);

                            context.startActivity(intent);
                        }
                    });
                    viewHolder.feedcommentlistingLinearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, CommentsActivity.class);
                            intent.putExtra("feedId", feedModel.getFeed_id());
                            intent.putExtra("type","1");

                            ApplicationSingleton.setPost_position(position);

                            context.startActivity(intent);
                        }
                    });
                    viewHolder.sectionTv.setVisibility(View.VISIBLE);

                    if (userDetailModel != null && userDetailModel.getName() != null) {
                        String name;
                        if (userDetailModel.getUserId().equalsIgnoreCase(AppUrl.NIROGSTREET_DESK_ID)) {
                            name = userDetailModel.getName();
                        } else {
                            name = Methods.getName(userDetailModel.getTitle(), userDetailModel.getName());
                        }
                        builder = new SpannableStringBuilder();
                        span = new SpannableString(name);
                        span.setSpan(new ForegroundColorSpan(Color.BLACK), 0, span.length(), 0);
                        span.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builder.append(span);
                        // viewHolder.nameTextView.setText("Dr. " + userDetailModel.getName().trim());
                        String xxx = feedModel.getCommunity_Id();
                        if (feedModel.getParent_feed() != null) {
                            if (feedModel.getParent_feed() != null) {
                                if (feedModel.getShareWithModels() != null && feedModel.getShareWithModels().size() > 0) {
                                    if (feedModel.getShareWithModels().get(0).getShareMessage() != null && !feedModel.getShareWithModels().get(0).getShareMessage().equalsIgnoreCase("")) {
                                        viewHolder.statusshare.setText(feedModel.getShareWithModels().get(0).getShareMessage());
                                        viewHolder.statusshare.setVisibility(View.VISIBLE);
                                        viewHolder.parentLay.setVisibility(View.VISIBLE);
                                    }
                                }
                                if (feedModel.getCreatedBy().getProfile_pic() != null && !feedModel.getCreatedBy().getProfile_pic().equalsIgnoreCase("")) {
                                    Picasso.with(context)
                                            .load(feedModel.getCreatedBy().getProfile_pic())
                                            .placeholder(R.drawable.user)
                                            .error(R.drawable.user)
                                            .into(viewHolder.profilePicparent);
                                    viewHolder.profilePicparent.setVisibility(View.VISIBLE);

                                } else {
                                    viewHolder.profilePicparent.setImageResource(R.drawable.user);
                                    viewHolder.profilePicparent.setVisibility(View.VISIBLE);

                                }
                                viewHolder.profilePicparent.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //Methods.profileUser(feedModel.getCreatedBy().getUser_Type(), context, feedModel.getCreatedBy().getUserId());

                              Methods.openUserActivities(context,feedModel.getCreatedBy().getUserId(),feedModel.getCreatedBy().getName(),feedModel.getCreatedBy().getProfile_pic(),feedModel.getCreatedBy().getTitle(),feedModel.getCreatedBy().getUser_Type());
                                    }
                                });
                                viewHolder.parentname.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                     //   Methods.profileUser(feedModel.getCreatedBy().getUser_Type(), context, feedModel.getCreatedBy().getUserId());

                                        Methods.openUserActivities(context,feedModel.getCreatedBy().getUserId(),feedModel.getCreatedBy().getName(),feedModel.getCreatedBy().getProfile_pic(),feedModel.getCreatedBy().getTitle(),feedModel.getCreatedBy().getUser_Type());

                                    }
                                });
                                viewHolder.parentname.setText(Methods.getName(feedModel.getCreatedBy().getTitle(), feedModel.getCreatedBy().getName()));
                                viewHolder.parentname.setVisibility(View.VISIBLE);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                params.setMargins(5, 0, 5, 5);

                                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                params1.setMargins(0, 0, 0, 0);
                                viewHolder.relativeLayout1.setLayoutParams(params1);
                                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.MATCH_PARENT,
                                        500
                                );

                                params2.setMargins(0, 0, 0, 0);
                                viewHolder.feedImageView.setLayoutParams(params2);
                                viewHolder.sharedLay.setLayoutParams(params);
                                str2 = new SpannableString(" shared ");
                                str2.setSpan(new ForegroundColorSpan(Color.rgb(148, 148, 156)), 0, str2.length(), 0);

                                viewHolder.sharedLay.setBackgroundResource(R.drawable.round_new);
                                builder.append(str2);
                                ClickableSpan clickSpan1 = new ClickableSpan() {
                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        ds.setColor(context.getResources().getColor(R.color.share_n_postcolor));// you can use custom color
                                        ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                                        ds.setUnderlineText(false);// this remove the underline
                                    }

                                    @Override
                                    public void onClick(View textView) {
                                    }
                                };

                                String thirdspan = str2.toString();
                                int third = builder.toString().indexOf(thirdspan);
                                builder.setSpan(clickSpan1, third, third + str2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                viewHolder.nameTextView.setText(builder, TextView.BufferType.SPANNABLE);
                                viewHolder.nameTextView.setMovementMethod(LinkMovementMethod.getInstance());

                                if (feedModel.getCreatedBy() != null && feedModel.getCreatedBy().getUserId() != null) {
                                    if (feedModel.getCreatedBy().getUserId().equalsIgnoreCase(feedModel.getUserDetailModel_creator().getUserId()))
                                        str2 = new SpannableString("their");
                                    else if (feedModel.getCreatedBy().getUserId().equalsIgnoreCase(AppUrl.NIROGSTREET_DESK_ID)) {
                                        str2 = new SpannableString(feedModel.getCreatedBy().getName());

                                    } else {
                                        str2 = new SpannableString(Methods.getName(feedModel.getCreatedBy().getTitle(), feedModel.getCreatedBy().getName()));
                                    }
                                }
                                str2.setSpan(new ForegroundColorSpan(Color.rgb(148, 148, 156)), 0, str2.length(), 0);

                                viewHolder.sharedLay.setBackgroundResource(R.drawable.round_new);
                                builder.append(str2);

                                ClickableSpan clickSpan12;

                                clickSpan12 = new ClickableSpan() {
                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        if (!feedModel.getCreatedBy().getUserId().equalsIgnoreCase(feedModel.getUserDetailModel_creator().getUserId())) {
                                            ds.setColor(context.getResources().getColor(R.color.black));// you can use custom color
                                            ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                                            ds.setUnderlineText(false);// this remove the underline

                                        } else {
                                            ds.setColor(context.getResources().getColor(R.color.share_n_postcolor));// you can use custom color
                                            ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                                            ds.setUnderlineText(false);// this remove the underline

                                        }
                                    }

                                    @Override
                                    public void onClick(View textView) {
                                        if (!feedModel.getCreatedBy().getUserId().equalsIgnoreCase(feedModel.getUserDetailModel_creator().getUserId())) {
                                            Methods.openUserActivities(context,feedModel.getCreatedBy().getUserId(),feedModel.getCreatedBy().getName(),feedModel.getCreatedBy().getProfile_pic(),feedModel.getCreatedBy().getTitle(),feedModel.getCreatedBy().getUser_Type());

                                         //   Methods.profileUser(feedModel.getCreatedBy().getUser_Type(), context, feedModel.getCreatedBy().getUserId());
                                        }
                                    }
                                };


                                String fourthspan = str2.toString();
                                int fourth = builder.toString().indexOf(fourthspan);
                                builder.setSpan(clickSpan12, fourth, fourth + str2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                viewHolder.nameTextView.setText(builder, TextView.BufferType.SPANNABLE);
                                viewHolder.nameTextView.setMovementMethod(LinkMovementMethod.getInstance());


                                if (feedModel.getCreatedBy().getUserId().equalsIgnoreCase(feedModel.getUserDetailModel_creator().getUserId()) && feedModel.getShareWithModels() != null && feedModel.getShareWithModels().size() > 0 && !feedModel.getShareWithModels().get(0).getShareType().equalsIgnoreCase("Public share")) {
                                    if (feedModel.getCommunity_Id() != null && !feedModel.getCommunity_Id().equalsIgnoreCase("") && feedModel.getCommunity_name() != null && !feedModel.getCommunity_name().equalsIgnoreCase(""))

                                        str2 = new SpannableString(" post in ");
                                    else
                                        str2 = new SpannableString(" post ");

                                } else {
                                    if (feedModel.getCommunity_Id() != null && !feedModel.getCommunity_Id().equalsIgnoreCase("") && feedModel.getCommunity_name() != null && !feedModel.getCommunity_name().equalsIgnoreCase("") && feedModel.getShareWithModels() != null && feedModel.getShareWithModels().size() > 0 && !feedModel.getShareWithModels().get(0).getShareType().equalsIgnoreCase("Public share"))

                                        str2 = new SpannableString("'s post in ");
                                    else if (feedModel.getCreatedBy().getUserId().equalsIgnoreCase(feedModel.getUserDetailModel_creator().getUserId())) {
                                        str2 = new SpannableString(" post ");

                                    } else {
                                        str2 = new SpannableString("'s post ");


                                    }


                                }
                                str2.setSpan(new ForegroundColorSpan(Color.rgb(148, 148, 156)), 0, str2.length(), 0);

                                viewHolder.sharedLay.setBackgroundResource(R.drawable.round_new);
                                builder.append(str2);

                                ClickableSpan clickSpan13 = new ClickableSpan() {
                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        ds.setColor(context.getResources().getColor(R.color.share_n_postcolor));// you can use custom color
                                        ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                                        ds.setUnderlineText(false);// this remove the underline
                                    }

                                    @Override
                                    public void onClick(View textView) {

                                    }
                                };

                                String fifthspan = str2.toString();
                                int fifth = builder.toString().indexOf(fifthspan);
                                builder.setSpan(clickSpan13, fifth, fifth + str2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                viewHolder.nameTextView.setText(builder, TextView.BufferType.SPANNABLE);
                                viewHolder.nameTextView.setMovementMethod(LinkMovementMethod.getInstance());
                                if (feedModel.getCommunity_Id() != null && !feedModel.getCommunity_Id().equalsIgnoreCase("") && feedModel.getCommunity_name() != null && !feedModel.getCommunity_name().equalsIgnoreCase("") && feedModel.getShareWithModels() != null && feedModel.getShareWithModels().size() > 0 && !feedModel.getShareWithModels().get(0).getShareType().equalsIgnoreCase("Public share")) {
                                    str2 = new SpannableString(feedModel.getCommunity_name());
                                    str2.setSpan(new ForegroundColorSpan(Color.rgb(148, 148, 156)), 0, str2.length(), 0);

                                    viewHolder.sharedLay.setBackgroundResource(R.drawable.round_new);
                                    builder.append(str2);

                                    ClickableSpan clickSpan14 = new ClickableSpan() {
                                        @Override
                                        public void updateDrawState(TextPaint ds) {
                                            ds.setColor(context.getResources().getColor(R.color.black));// you can use custom color
                                            ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                                            ds.setUnderlineText(false);// this remove the underline
                                        }

                                        @Override
                                        public void onClick(View textView) {
                                            Intent intent = new Intent(context, CommunitiesDetails.class);
                                            intent.putExtra("groupId", feedModel.getCommunity_Id());
                                            context.startActivity(intent);
                                        }
                                    };

                                    String sixthspan = str2.toString();
                                    int six = builder.toString().indexOf(sixthspan);
                                    builder.setSpan(clickSpan14, six, six + str2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    viewHolder.nameTextView.setText(builder, TextView.BufferType.SPANNABLE);
                                    viewHolder.nameTextView.setMovementMethod(LinkMovementMethod.getInstance());

                                }
                            }
                        } else {
                            viewHolder.statusshare.setVisibility(View.GONE);
                            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    500
                            );
                            viewHolder.parentLay.setVisibility(View.GONE);
                            params2.setMargins(0, 0, 0, 0);
                            viewHolder.feedImageView.setLayoutParams(params2);
                            viewHolder.sharedLay.setBackgroundResource(0);
                            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            params1.setMargins(0, 10, 0, 0);
                            viewHolder.relativeLayout1.setLayoutParams(params1);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(0, 0, 0, 0);
                            viewHolder.sharedLay.setLayoutParams(params);
                            if (feedModel.getCommunity_name() != null && !feedModel.getCommunity_name().equalsIgnoreCase("")) {
                                str3 = new SpannableString(" posted in ");
                                str3.setSpan(new ForegroundColorSpan(Color.rgb(148, 148, 156)), 0, str3.length(), 0);
                                builder.append(str3);

                                str4 = new SpannableString(feedModel.getCommunity_name());
                                str4.setSpan(new ForegroundColorSpan(Color.rgb(148, 148, 156)), 0, str4.length(), 0);
                                builder.append(str4);

                                ClickableSpan clickSpan1 = new ClickableSpan() {
                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        ds.setColor(context.getResources().getColor(R.color.black));// you can use custom color
                                        ds.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));
                                        ds.setUnderlineText(false);// this remove the underline
                                    }

                                    @Override
                                    public void onClick(View textView) {
                                        Intent intent = new Intent(context, CommunitiesDetails.class);
                                        intent.putExtra("groupId", feedModel.getCommunity_Id());
                                        context.startActivity(intent);
                                    }
                                };

                                String thirdspan = str4.toString();
                                int third = builder.toString().indexOf(thirdspan);
                                builder.setSpan(clickSpan1, third, third + str4.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                viewHolder.nameTextView.setText(builder, TextView.BufferType.SPANNABLE);
                                viewHolder.nameTextView.setMovementMethod(LinkMovementMethod.getInstance());

                            }
                        }

                    }
                    ClickableSpan clickSpan = new ClickableSpan() {
                        @Override
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(context.getResources().getColor(R.color.black));// you can use custom color
                            ds.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));
                            ds.setUnderlineText(false);// this remove the underline
                        }

                        @Override
                        public void onClick(View textView) {
                            Methods.openUserActivities(context,userDetailModel.getUserId(),userDetailModel.getName(),userDetailModel.getProfile_pic(),userDetailModel.getTitle(),userDetailModel.getUser_Type());

                         //   Methods.profileUser(userDetailModel.getUser_Type(), context, userDetailModel.getUserId());

                        }
                    };
                    builder.setSpan(clickSpan, 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    viewHolder.nameTextView.setText(builder, TextView.BufferType.SPANNABLE);
                    viewHolder.nameTextView.setMovementMethod(LinkMovementMethod.getInstance());
                    if (feedModel.getActivity_detail() != null) {
                        if (feedModel.getActivity_detail().equalsIgnoreCase("1"))
                            viewHolder.sectionTv.setText("You created this post");

                    }
                    if (feedModel.getActivity_detail().equalsIgnoreCase("2")) {
                        viewHolder.sectionTv.setText("You commented on this post");

                    }
                    if (feedModel.getActivity_detail().equalsIgnoreCase("3")) {
                        viewHolder.sectionTv.setText("You shared this post");

                    }
                    if (feedModel.getActivity_detail().equalsIgnoreCase("4")) {
                        viewHolder.sectionTv.setText("You liked this post");

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
        }


    }

    public void setDialog(final FeedModel feedModel) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to Delete this post?");// Add the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (NetworkUtill.isNetworkAvailable(context)) {
                    DeletepostAsyncTask deletepostAsyncTask = new DeletepostAsyncTask(feedModel.getFeed_id(), userId, authToken);
                    deletepostAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    NetworkUtill.showNoInternetDialog(context);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();

        AlertDialog dialog = builder.create();
    }

    public void deleteOrEditPopup(ImageView view, final FeedModel feedModel, final int position) {
        PopupMenu popup = new PopupMenu(context, view);
        popup.getMenuInflater().inflate(R.menu.popup_menu_edit_delete, popup.getMenu());
        if (feedModel.getParentFeedDetail() != null) {
            popup.getMenu().getItem(0).setVisible(false);
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                ;
                switch (item.getItemId()) {
                    case R.id.edit:


                        Intent intent = new Intent(context, PostEditActivity.class);
                        intent.putExtra("position", position);
                        intent.putExtra("feedId", feedModel.getFeed_id());
                        context.startActivity(intent);
                        break;
                    case R.id.del:
                        setDialog(feedModel);
                        break;
                }

                return true;
            }
        });

        popup.show();//showing popup menu
    }

    public class DeletepostAsyncTask extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String feedId, userId;


        private String responseBody;
        HttpClient client;
        Context context;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public DeletepostAsyncTask(String feedId, String userId, String authToken) {
            this.feedId = feedId;
            this.authToken = authToken;
            this.userId = userId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (jo != null) {

                    feedModels.remove(positionat);
                    notifyItemRemoved(positionat);
                    notifyItemRangeChanged(positionat, feedModels.size());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {


                String url = AppUrl.BaseUrl + "feed/delete";
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
                pairs.add(new BasicNameValuePair("userID", sesstionManager.getUserDetails().get(SesstionManager.USER_ID)));
                pairs.add(new BasicNameValuePair("feedID", feedId));
                httppost.setHeader("Authorization", "Basic " + sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN));

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

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public String getSelectedNameCsv(UserDetailModel userDetailModel) {
        String languageCSV = "";

        if (userDetailModel != null && userDetailModel.getSpecializationModels() != null && userDetailModel.getSpecializationModels().size() > 0) {
            for (int i = 0; i < userDetailModel.getSpecializationModels().size(); i++) {
                String language = userDetailModel.getSpecializationModels().get(i).getSpecializationName();
                if (language != null && !language.trim().isEmpty()
                        && languageCSV != null && !languageCSV.trim().isEmpty())
                    languageCSV = languageCSV + ", ";
                languageCSV = languageCSV + language;

            }
        }
        return languageCSV;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item_more, parent, false);
            return new HeaderView(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_feed, parent, false);
            return new MyViewHolder(v);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return feedModels.size();
    }

    public class HeaderView extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        ImageView editInfo, webSite_icon;
        RelativeLayout card;
        TextView QualificationTv, yearOfBirthTv, yearOfExperienceTv, WebTv, emailTv, phoneTv, view_detail, nameTv, myActivitiesTextView;
        TextView postAn, aboutTextView;
        RelativeLayout profileRelativeLayout;

        public HeaderView(View itemView) {
            super(itemView);
            card = (RelativeLayout) itemView.findViewById(R.id.card);
            webSite_icon = (ImageView) itemView.findViewById(R.id.webSite_icon);
            profileRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.profile);
            view_detail = (TextView) itemView.findViewById(R.id.view_detail);
            nameTv = (TextView) itemView.findViewById(R.id.name1);
            emailTv = (TextView) itemView.findViewById(R.id.emailTv);
            myActivitiesTextView = (TextView) itemView.findViewById(R.id.myActivities);
            phoneTv = (TextView) itemView.findViewById(R.id.phoneTv);
            editInfo = (ImageView) itemView.findViewById(R.id.editInfo);
            QualificationTv = (TextView) itemView.findViewById(R.id.QualificationTv);
            yearOfBirthTv = (TextView) itemView.findViewById(R.id.yearOfBirthTv);
            yearOfExperienceTv = (TextView) itemView.findViewById(R.id.yearOfExperienceTv);
            WebTv = (TextView) itemView.findViewById(R.id.WebTv);
            aboutTextView = (TextView) itemView.findViewById(R.id.about);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.profilePic);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView playicon;
        TextView sectionTv;
        CircleImageView profileImage;
        RelativeLayout comment_section;
        TextView commenter_name, user_comment_time, user_comment;

        TextView txtTextView;
        RelativeLayout relativeLayout1;

        TextView likesTextView, commntsTextView;

        View left_view, right_view, bottom_view;
        TextView youHaveWishedTextView, comment_text;
        LinearLayout parentLay;

        TextView statusTextView, nameTextView, QuestionTextView,
                timeStampTextView, announcementTextView,
                noOfLikeTextView, whisesTextView, noOfCommentTextView,
                linkTitleTextView, linkDescriptiontextView, docNameTextView, docTypeTextView,
                anniversaryTextView, announcementTypeTextView, notificationTitleTextView, viewAllTextView, moreviewTextView;
        ImageView feedImageView, linkImageView, anniverasaryLayoutImage, docImageView, announcementImage, userImage, feedlikeimg, cancelAnnouncementImageView, basicAnnouncemetImage;
        LinearLayout docTypeLayout, sharedLay, announcementLinearLayout, feeddeletelistingLinearLayout, CommentSectionLinearLayout, feedcommentlistingLinearLayout, feedcommentlisting, feedlikeLinearLayout, likeFeedLinearLayout, share_feedLinearLayout, normalFeedLayout, cardshoderLinearLayout;
        CircleImageView profileImageView;
        FrameLayout frameVideoFrameLayout;
        RelativeLayout profileSectionLinearLayout, basicAnnouncemetLinearLayout, sayCongratsRelativeLayout, anniversaryLinearLayout, hetrogenousAnnouncementLinearLayout;
        Button buttondownload;
        VideoView videoView;
        TextView statusshare;
        WebView webView;

        CircleImageView profilePicparent;
        TextView parentname;
        ImageView delImageView;

        LinearLayout link_title_des_lay;
        View basicAnnouncemet_view;
        LinearLayout moreLinearLayout, two_or_moreLinearLayout;
        ImageView imageFirstImageView, imageSecImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            relativeLayout1 = (RelativeLayout) itemView.findViewById(R.id.relativeLayout1);
            parentname = (TextView) itemView.findViewById(R.id.parentname);
            profilePicparent = (CircleImageView) itemView.findViewById(R.id.profilePicparent);
            commenter_name=(TextView)itemView.findViewById(R.id.commenter_name);

            left_view = (View) itemView.findViewById(R.id.left_view);
            delImageView = (ImageView) itemView.findViewById(R.id.del);
            statusshare = (TextView) itemView.findViewById(R.id.statusshare);
            parentLay = (LinearLayout) itemView.findViewById(R.id.parentLay);
            comment_section = (RelativeLayout) itemView.findViewById(R.id.comment_section);
            profileImage = (CircleImageView) itemView.findViewById(R.id.profileImage);
            user_comment_time = (TextView) itemView.findViewById(R.id.user_comment_time);
            user_comment = (TextView) itemView.findViewById(R.id.user_comment);
            commntsTextView = (TextView) itemView.findViewById(R.id.commnts);
            bottom_view = (View) itemView.findViewById(R.id.bottom_view);
            right_view = (View) itemView.findViewById(R.id.right_view);
            sectionTv = (TextView) itemView.findViewById(R.id.section);
            txtTextView = (TextView) itemView.findViewById(R.id.txt);
            likesTextView = (TextView) itemView.findViewById(R.id.likes);
            commntsTextView = (TextView) itemView.findViewById(R.id.commnts);
            sharedLay = (LinearLayout) itemView.findViewById(R.id.sharedLay);

            link_title_des_lay = (LinearLayout) itemView.findViewById(R.id.link_title_des_lay);
            webView = (WebView) itemView.findViewById(R.id.webview);
            QuestionTextView = (TextView) itemView.findViewById(R.id.Question);
            playicon = (ImageView) itemView.findViewById(R.id.playicon);
            youHaveWishedTextView = (TextView) itemView.findViewById(R.id.youHaveWished);
            moreLinearLayout = (LinearLayout) itemView.findViewById(R.id.moreLinearLayout);
            moreviewTextView = (TextView) itemView.findViewById(R.id.moreview);
            imageFirstImageView = (ImageView) itemView.findViewById(R.id.imageFirst);
            imageSecImageView = (ImageView) itemView.findViewById(R.id.imageSec);
            two_or_moreLinearLayout = (LinearLayout) itemView.findViewById(R.id.two_or_more);
            frameVideoFrameLayout = (FrameLayout) itemView.findViewById(R.id.frameVideo);
            viewAllTextView = (TextView) itemView.findViewById(R.id.viewAll);
            announcementImage = (ImageView) itemView.findViewById(R.id.announcementImage);
            anniverasaryLayoutImage = (ImageView) itemView.findViewById(R.id.anniverasaryLayoutImage);
            cancelAnnouncementImageView = (ImageView) itemView.findViewById(R.id.cancelAnnouncement);
            notificationTitleTextView = (TextView) itemView.findViewById(R.id.notificationTitle);
            cardshoderLinearLayout = (LinearLayout) itemView.findViewById(R.id.llGallery);
            feeddeletelistingLinearLayout = (LinearLayout) itemView.findViewById(R.id.feeddeletelisting);
            basicAnnouncemetImage = (ImageView) itemView.findViewById(R.id.basicAnnouncemetImage);
            basicAnnouncemetLinearLayout = (RelativeLayout) itemView.findViewById(R.id.basicAnnouncemet);
            share_feedLinearLayout = (LinearLayout) itemView.findViewById(R.id.share_feed);
            docTypeLayout = (LinearLayout) itemView.findViewById(R.id.docTypeLayout);
            comment_text = (TextView) itemView.findViewById(R.id.comment_text);
            announcementTypeTextView = (TextView) itemView.findViewById(R.id.announcementType);
            announcementTextView = (TextView) itemView.findViewById(R.id.announcementText);
            feedcommentlistingLinearLayout = (LinearLayout) itemView.findViewById(R.id.feedcommentlisting);
            feedcommentlisting = (LinearLayout) itemView.findViewById(R.id.comment_feed);
            sayCongratsRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.sayCongrats);
            hetrogenousAnnouncementLinearLayout = (RelativeLayout) itemView.findViewById(R.id.hetrogenousAnnouncement);
            whisesTextView = (TextView) itemView.findViewById(R.id.whises);
            normalFeedLayout = (LinearLayout) itemView.findViewById(R.id.normalFeedLayout);
            announcementLinearLayout = (LinearLayout) itemView.findViewById(R.id.announcement);
            feedlikeLinearLayout = (LinearLayout) itemView.findViewById(R.id.count_like);
            feedlikeimg = (ImageView) itemView.findViewById(R.id.feedlikeimg);
            profileSectionLinearLayout = (RelativeLayout) itemView.findViewById(R.id.profileSection);
            likeFeedLinearLayout = (LinearLayout) itemView.findViewById(R.id.feedlike);
            videoView = (VideoView) itemView.findViewById(R.id.videoView);
            anniversaryTextView = (TextView) itemView.findViewById(R.id.anniversaryText);
            statusTextView = (TextView) itemView.findViewById(R.id.status);
            docImageView = (ImageView) itemView.findViewById(R.id.docImage);
            buttondownload = (Button) itemView.findViewById(R.id.download);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            timeStampTextView = (TextView) itemView.findViewById(R.id.timeStamp);
            noOfLikeTextView = (TextView) itemView.findViewById(R.id.no_of_like);
            docNameTextView = (TextView) itemView.findViewById(R.id.docName);
            anniversaryLinearLayout = (RelativeLayout) itemView.findViewById(R.id.anniverasaryLayout);
            docTypeTextView = (TextView) itemView.findViewById(R.id.docType);
            webView = (WebView) itemView.findViewById(R.id.webview);
            userImage = (ImageView) itemView.findViewById(R.id.userImage);
            noOfCommentTextView = (TextView) itemView.findViewById(R.id.no_of_comments);
            linkDescriptiontextView = (TextView) itemView.findViewById(R.id.description);
            linkTitleTextView = (TextView) itemView.findViewById(R.id.title);
            profileImageView = (CircleImageView) itemView.findViewById(R.id.profilePic);
            feedImageView = (ImageView) itemView.findViewById(R.id.feedImage);
            linkImageView = (ImageView) itemView.findViewById(R.id.linkImage);
            CommentSectionLinearLayout = (LinearLayout) itemView.findViewById(R.id.CommentSection);
            basicAnnouncemet_view = (View) itemView.findViewById(R.id.basicAnnouncemet_view);

        }
    }


    public void sharePopup(LinearLayout view, final FeedModel feedModel) {
        PopupMenu popup = new PopupMenu(context, view);
        popup.getMenuInflater().inflate(R.menu.popup_menu_share, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                             public boolean onMenuItemClick(MenuItem item) {

                                                 switch (item.getItemId()) {
                                                     case R.id.publicshare:
                                                         Intent intent = new Intent(context, PublicShare.class);
                                                         if (feedModel.getParentFeedDetail() != null) {
                                                             intent.putExtra("parentFeedId", feedModel.getParent_feed());

                                                         }
                                                         intent.putExtra("feedId", feedModel.getFeed_id());
                                                         intent.putExtra("userId", userId);
                                                         context.startActivity(intent);

                                                         break;
                                                     case R.id.groupstimeline:
                                                         Intent intent1 = new Intent(context, ShareOnFriendsTimeline.class);
                                                         if (feedModel.getParentFeedDetail() != null) {
                                                             intent1.putExtra("parentFeedId", feedModel.getParent_feed());

                                                         }
                                                         intent1.putExtra("feedId", feedModel.getFeed_id());
                                                         intent1.putExtra("userId", userId);
                                                         intent1.putExtra("shareOnGroup", true);
                                                         context.startActivity(intent1);

                                                         break;
                                                     case R.id.share_exteraly:
                                                         try {
                                                             if (feedModel.getMessage() != null && feedModel.getMessage().length() > 0) {
                                                                 text = feedModel.getMessage();
                                                             }
                                                             ArrayList<Uri> files = new ArrayList<Uri>();
                                                             if (feedModel.getFeedSourceArrayList() != null && feedModel.getFeedSourceArrayList().size() > 0) {
                                                                 try {
                                                                     for (int i = 0; i < feedModel.getFeedSourceArrayList().size(); i++) {
                                                                         URL url = new URL(feedModel.getFeedSourceArrayList().get(i));
                                                                         Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                                                                         Uri bitmapUri = null;

                                                                         String bitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), image, "title", null);
                                                                         bitmapUri = Uri.parse(bitmapPath);
                                                                         files.add(bitmapUri);
                                                                     }
                                                                 } catch (IOException e) {
                                                                     System.out.println(e);
                                                                 }
                                                             }
                                                             if (feedModel.getTitleQuestion() != null && feedModel.getTitleQuestion().length() > 0) {
                                                                 title = feedModel.getTitleQuestion();
                                                             }
                                                             if (feedModel.getLink_type() != null && feedModel.getLink_type().equalsIgnoreCase("2")) {

                                                                 videourl = feedModel.getFeed_source();
                                                             }
                                                             Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                                             shareIntent.setType("text/plain");
                                                             if (files != null && files.size() > 0) {

                                                                 shareIntent.putExtra(Intent.EXTRA_STREAM, files);
                                                                 shareIntent.setType("image/*");
                                                                 shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                             }
                                                             if (title != null && title.length() > 0 || text != null && text.length() > 0 || videourl != null && videourl.length() > 0 || files.size() > 0 || feedModel.getLink_type() != null) {
                                                                 if (title != null && title.length() > 0 && text != null && text.length() > 0 && videourl != null && videourl.length() > 0) {
                                                                     shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, title + "\n\n" + text + "\n\n" + videourl);
                                                                 } else if (title != null && title.length() > 0 && text != null && text.length() > 0) {
                                                                     shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, title + "\n\n" + text);
                                                                 } else if (title != null && title.length() > 0) {
                                                                     shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, title);
                                                                 } else if (text != null && text.length() > 0) {
                                                                     shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
                                                                 } else if (feedModel.getLink_type() != null) {
                                                                     shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, feedModel.getFeed_source());

                                                                 }
                                                                 shareIntent.putExtra(Intent.EXTRA_SUBJECT, "I found this Etiquettes ");
                                                                 context.startActivity(Intent.createChooser(shareIntent,
                                                                         context.getResources().getString(R.string.share_with)));


                                                             }
                                                         } catch (Exception e) {
                                                             // TODO: handle exception
                                                             e.printStackTrace();
                                                         }
                                                         break;
                                                 }

                                                 return true;
                                             }
                                         }

        );

        popup.show();//showing popup menu
    }


    public class LikePostAsynctask extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String feedId, userId;
        int liked;

        private String responseBody;
        HttpClient client;
        Context context;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public LikePostAsynctask(String feedId, String userId, String authToken, int liked) {
            this.feedId = feedId;
            this.liked = liked;
            this.authToken = authToken;
            this.userId = userId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                circularProgressBar.setVisibility(View.GONE);
                if (jo != null) {
                    if (jo.has("responce") && !jo.isNull("responce")) {
                        JSONObject jsonObject = jo.getJSONObject("responce");
                        if (jsonObject.has("totalLikes") && !jsonObject.isNull("totalLikes")) {
                            FeedModel feedModel = feedModels.get(positionat);
                            feedModel.setTotal_likes(jsonObject.getString("totalLikes"));
                            notifyItemChanged(positionat, new String(feedModel.getTotal_likes()));
                            if (liked == 1) {
                                feedModels.get(positionat).setUser_has_liked(0);
                            } else {
                                feedModels.get(positionat).setUser_has_liked(1);
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


                String url = AppUrl.BaseUrl + "feed/like";
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
                pairs.add(new BasicNameValuePair("feedID", feedId));
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
            circularProgressBar.setVisibility(View.VISIBLE);
        }
    }


}
