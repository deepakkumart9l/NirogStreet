package com.app.nirogstreet.adapter;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.AlbumGallary;
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
import com.app.nirogstreet.activites.PublicShare;
import com.app.nirogstreet.activites.ShareOnFriendsTimeline;
import com.app.nirogstreet.activites.Test;
import com.app.nirogstreet.activites.VideoPlay_Activity;
import com.app.nirogstreet.activites.YoutubeVideo_Play;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.FeedModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.parser.FeedParser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.ExpandableTextView;
import com.app.nirogstreet.uttil.GoToURLSpan;
import com.app.nirogstreet.uttil.ImageProcess;
import com.app.nirogstreet.uttil.Methods;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

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
 * Created by Preeti on 26-10-2017.
 */

public class TimelineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    int positionat;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    SpannableString str2,spanStatus2;
    String text, videourl, title;
    SpannableString span,spanStatus;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    FrameLayout customViewContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;

    private String authToken, userId;
    private View mCustomView;

    WebView webView;
    private final myWebViewClient mWebViewClient;
    private final myWebChromeClient mWebChromeClient;

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
    SpannableString span2, str3, str4;
    SesstionManager sesstionManager;
    CircularProgressBar circularProgressBar;
    String groupId = "";
    private SpannableStringBuilder builder,builder1;


    public TimelineAdapter(Context context, ArrayList<FeedModel> feedModels, Activity activity, String groupId, FrameLayout customViewContainer, CircularProgressBar circularProgressBar) {
        this.feedModels = feedModels;
        this.context = context;
        this.activity = activity;
        this.groupId = groupId;
        this.circularProgressBar = circularProgressBar;
        this.customViewContainer = customViewContainer;
        sesstionManager = new SesstionManager(context);
        HashMap<String, String> userDetails = sesstionManager.getUserDetails();
        authToken = userDetails.get(SesstionManager.AUTH_TOKEN);
        userId = userDetails.get(SesstionManager.USER_ID);
        mWebViewClient = new myWebViewClient();

        mWebChromeClient = new myWebChromeClient();

    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item, parent, false);
            return new HeaderView(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_layout, parent, false);
            return new MyViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case TYPE_HEADER:
                HeaderView myViewHolder = (HeaderView) holder;
                if (sesstionManager.getProfile().get(SesstionManager.KEY_POFILE_PIC) != null) {
                    String url;
                    url = sesstionManager.getProfile().get(SesstionManager.KEY_POFILE_PIC);
                    if (url != null && !url.equalsIgnoreCase(""))
                        Picasso.with(context)
                                .load(url)
                                .placeholder(R.drawable.user)
                                .error(R.drawable.user)
                                .into(myViewHolder.circleImageView);
                }
                //   Glide.with(context).load(askQuestionImages).into(myViewHolder.circleImageView);
                //  TypeFaceMethods.setRegularTypeFaceForTextView(myViewHolder.postAn, context);

                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PostingActivity.class);
                        if (!groupId.equalsIgnoreCase("")) {
                            intent.putExtra("groupId", groupId);
                        }
                        context.startActivity(intent);

                    }
                });
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
                    viewHolder.linkImageView.setVisibility(View.GONE);
                    viewHolder.link_title_des_lay.setVisibility(View.GONE);
                    viewHolder.left_view.setVisibility(View.GONE);
                    viewHolder.relativeLayout1.setVisibility(View.GONE);
                    viewHolder.right_view.setVisibility(View.GONE);
                    viewHolder.bottom_view.setVisibility(View.GONE);
                    viewHolder.profilePicparent.setVisibility(View.GONE);
                    viewHolder.parentname.setVisibility(View.GONE);
                    viewHolder.parentLay.setVisibility(View.GONE);
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
                                    viewHolder.relativeLayout1.setVisibility(View.VISIBLE);
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

                                 /*   Glide.with(context)
                                            .load(feedModel.getUrl_image()) // Uri of the picture
                                            .centerCrop()
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .crossFade()
                                            .override(100, 100)
                                            .into(viewHolder.feedImageView);*/
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
/*
                                    viewHolder.link_title_des_lay.setVisibility(View.GONE);
                                    viewHolder.left_view.setVisibility(View.GONE);
                                    viewHolder.right_view.setVisibility(View.GONE);
                                    viewHolder.bottom_view.setVisibility(View.GONE);
                                    viewHolder.relativeLayout1.setVisibility(View.GONE);
                                    viewHolder.playicon.setVisibility(View.GONE);
                                    viewHolder.frameVideoFrameLayout.setVisibility(View.GONE);
                                    viewHolder.linkImageView.setVisibility(View.GONE);
                                    viewHolder.linkTitleTextView.setVisibility(View.GONE);
                                    viewHolder.linkDescriptiontextView.setVisibility(View.GONE);
                                    viewHolder.feedImageView.setVisibility(View.VISIBLE);
                                    viewHolder.two_or_moreLinearLayout.setVisibility(View.GONE);
                                    viewHolder.docTypeLayout.setVisibility(View.GONE);
                                    viewHolder.relativeLayout1.setVisibility(View.VISIBLE);
                                    viewHolder.profileSectionLinearLayout.setVisibility(View.VISIBLE);
                                    viewHolder.anniversaryLinearLayout.setVisibility(View.GONE);
                                    viewHolder.anniverasaryLayoutImage.setVisibility(View.GONE);
                                    viewHolder.videoView.setVisibility(View.GONE);
                                    viewHolder.playicon.setVisibility(View.VISIBLE);
                                    Glide.with(context)
                                            .load(feedModel.getUrl_image()) // Uri of the picture
                                            .centerCrop()
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .crossFade()
                                            .override(100, 100)
                                            .into(viewHolder.feedImageView);


                                    String videoUrl[];
                                    String frameVideo = null;
                                    try {
                                        if (feedModel.getFeed_source().contains("=")) {
                                            videoUrl = feedModel.getFeed_source().split("=");
                                        } else {
                                            videoUrl = feedModel.getFeed_source().split("be/");
                                        }
                                        String video_id = videoUrl[1];
                                        frameVideo = "<iframe width=\"100%\" height=" + "185" + " src=\"https://www.youtube.com/embed/" + video_id + "?" + "\" frameborder=\"0\" allowfullscreen></iframe>";
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    viewHolder.webView.setVisibility(View.VISIBLE);
                                    viewHolder.webView.setWebViewClient(new WebViewClient() {
                                        @Override
                                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                            return false;
                                        }
                                    });
                                    WebSettings webSettings = viewHolder.webView.getSettings();
                                    webSettings.setJavaScriptEnabled(true);
                                    viewHolder.webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                                    webView = (WebView) viewHolder.webView;
                                    webView.setWebChromeClient(mWebChromeClient);
                                    if (Build.VERSION.SDK_INT < 8) {
                                        //webView.getSettings().setPluginsEnabled(true);
                                    } else {
                                        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
                                    }
                                    viewHolder.webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

                                    viewHolder.webView.getSettings().setJavaScriptEnabled(true);
                                    viewHolder.webView.getSettings().setAppCacheEnabled(true);
                                    viewHolder.webView.getSettings().setSaveFormData(true);
                                    viewHolder.webView.loadData(frameVideo, "text/html", "utf-8");
*/


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
                                    // imageLoader.getInstance().displayImage(feedModel.getUrl_image(), holder.linkImageView, defaultOptions);
                                    // imageLoader1.DisplayImage(context, feedModel.getUrl_image(), holder.linkImageView, null, 150, 150, R.drawable.default_image);
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
                            viewHolder.relativeLayout1.setVisibility(View.VISIBLE);

                            viewHolder.right_view.setVisibility(View.GONE);
                            viewHolder.bottom_view.setVisibility(View.GONE);

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
                                                .load(strings.get(1))
                                                .placeholder(R.drawable.default_)
                                                .error(R.drawable.default_)
                                                .into(viewHolder.imageFirstImageView);
                                        Picasso.with(context)
                                                .load(strings.get(0))
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
                                            .error(R.drawable.default_).into(viewHolder.feedImageView);

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
                            } //   imageLoader1.DisplayImage(context, feedModel.getFeed_source(), holder.feedImageView, null, 150, 150, R.drawable.default_image);
                            break;
                        case VIDEO_UPLOADED_BY_USER:
                            viewHolder.frameVideoFrameLayout.setVisibility(View.GONE);
                            viewHolder.playicon.setVisibility(View.VISIBLE);
                            viewHolder.linkImageView.setVisibility(View.GONE);
                            viewHolder.linkTitleTextView.setVisibility(View.GONE);
                            viewHolder.feedImageView.setVisibility(View.VISIBLE);
                            viewHolder.two_or_moreLinearLayout.setVisibility(View.GONE);
                            viewHolder.relativeLayout1.setVisibility(View.VISIBLE);

                            viewHolder.two_or_moreLinearLayout.setVisibility(View.GONE);
                            viewHolder.linkDescriptiontextView.setVisibility(View.GONE);
                            viewHolder.link_title_des_lay.setVisibility(View.GONE);
                            viewHolder.left_view.setVisibility(View.GONE);
                            viewHolder.right_view.setVisibility(View.GONE);
                            viewHolder.bottom_view.setVisibility(View.GONE);
                            viewHolder.CommentSectionLinearLayout.setVisibility(View.VISIBLE);
                            viewHolder.profileSectionLinearLayout.setVisibility(View.VISIBLE);
                            viewHolder.profileSectionLinearLayout.setVisibility(View.VISIBLE);
                            viewHolder.videoView.setVisibility(View.GONE);
                            viewHolder.webView.setVisibility(View.GONE);
                            viewHolder.docTypeLayout.setVisibility(View.GONE);
                            viewHolder.feedImageView.setVisibility(View.VISIBLE);
                            if (feedModel.getUrl_image() != null && !feedModel.getUrl_image().equalsIgnoreCase("")) {
                                Picasso.with(context)
                                        .load(feedModel.getUrl_image())
                                        .placeholder(R.drawable.default_)
                                        .error(R.drawable.default_)
                                        .into(viewHolder.feedImageView);
                            } else {
                                viewHolder.feedImageView.setImageResource(R.drawable.default_videobg);
                            }
                            viewHolder.relativeLayout1.setVisibility(View.VISIBLE);
                            String load = "data:image/jpeg;base64" + "," + feedModel.getUrl_image();
                        /*    final byte[] decodedBytes = Base64.decode(feedModel.getUrl_image(), Base64.DEFAULT);
                            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                            viewHolder.feedImageView.setImageBitmap(decodedBitmap);*/
                            //Glide.with(context).load(decodedBytes).crossFade().fitCenter().into(viewHolder.feedImageView);
                       /*   Bitmap bmThumbnail;
                            bmThumbnail = ThumbnailUtils.createVideoThumbnail(feedModel.getUrl_image(), MediaStore.Video.Thumbnails.MINI_KIND);
                            viewHolder.feedImageView.setImageBitmap(bmThumbnail);*/

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
                            //  FeedMethods.setNativeVideo(holder.videoView, feedModel.getFeed_source(), context, holder.frameVideoFrameLayout);
                            break;

                        case FEED_TYPE_DOCUMENTS:
                            viewHolder.frameVideoFrameLayout.setVisibility(View.GONE);
                            viewHolder.playicon.setVisibility(View.GONE);
                            viewHolder.docTypeLayout.setVisibility(View.VISIBLE);
                            viewHolder.videoView.setVisibility(View.GONE);
                            viewHolder.linkImageView.setVisibility(View.GONE);
                            viewHolder.two_or_moreLinearLayout.setVisibility(View.GONE);
                            viewHolder.link_title_des_lay.setVisibility(View.GONE);
                            viewHolder.left_view.setVisibility(View.GONE);
                            viewHolder.relativeLayout1.setVisibility(View.VISIBLE);

                            viewHolder.right_view.setVisibility(View.GONE);
                            viewHolder.bottom_view.setVisibility(View.GONE);
                            viewHolder.linkTitleTextView.setVisibility(View.GONE);
                            viewHolder.linkDescriptiontextView.setVisibility(View.GONE);
                            viewHolder.feedImageView.setVisibility(View.GONE);
                            viewHolder.CommentSectionLinearLayout.setVisibility(View.VISIBLE);
                            viewHolder.profileSectionLinearLayout.setVisibility(View.VISIBLE);
                            if (feedModel != null)
                                if (feedModel.getFeed_source() != null) {
                                    // imageLoader.DisplayImage(context, feedModel.getDoc_Icon(), holder.docImageView, null, 150, 150, R.drawable.dummyuser);
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
                                          /*  String s[] = feedModel.getFeed_source().split("documents/");
                                            String s1[] = s[1].split("\\.");
                                            if (feedModel.getDoc_name().contains("\\.")) {
                                                feedModel.setDoc_name(feedModel.getDoc_name().replace("\\.", ""));
                                            }
                                            verifyStoragePermissions(activity);

                                            String extntion = feedModel.getFeed_source().substring(feedModel.getFeed_source().lastIndexOf(".") + 1);
                                            String filename = feedModel.getFeed_source().substring(feedModel.getFeed_source().lastIndexOf("/") + 1);
                                            Methods.downloadFile(feedModel.getFeed_source(), activity, extntion, feedModel.getDoc_name());
                                            Methods.showProgress(feedModel.getFeed_source(), activity);*/
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
                            viewHolder.relativeLayout1.setVisibility(View.GONE);
                            viewHolder.right_view.setVisibility(View.GONE);
                            viewHolder.bottom_view.setVisibility(View.GONE);
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
                       // viewHolder.statusTextView.setText(feedModel.getMessage().trim().toString());

                        viewHolder.statusTextView.setVisibility(View.VISIBLE);
                        if (feedModel.getMessage().length() > 120) {
                            try {
                                builder1 = new SpannableStringBuilder();
                                spanStatus = new SpannableString(feedModel.getMessage().substring(0, 170));

                                spanStatus.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanStatus.length(), 0);
                              //  spanStatus.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                Methods.hyperlinkSet(viewHolder.statusTextView,spanStatus.toString(),context,feedModel.getIs_pin(),spanStatus);
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
                                //  doResizeTextView(viewHolder.statusTextView, 3, "view more", true);
                                builder1.setSpan(clickSpan1, third, third + spanStatus2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                viewHolder.statusTextView.setText(builder1, TextView.BufferType.SPANNABLE);
                                viewHolder.statusTextView.setMovementMethod(LinkMovementMethod.getInstance());

                                //viewHolder.statusTextView.setText(feedModel.getMessage());
                                //viewHolder.statusTextView.setText(feedModel.getMessage());
                                //  cycleTextViewExpansion(viewHolder.statusTextView);
                                if (feedModel.getMessage() != null && feedModel.getMessage().length() > 0) {
                                    //Methods.hyperlink(viewHolder.statusTextView, viewHolder.statusTextView.getText().toString(), context,feedModel.getIs_pin());
                                    // Linkify.addLinks(viewHolder.statusTextView, Linkify.WEB_URLS);
                                }
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }  else {
                            viewHolder.statusTextView.setText(feedModel.getMessage());
                            if (feedModel.getMessage() != null && feedModel.getMessage().length() > 0) {
                                Methods.hyperlink(viewHolder.statusTextView, feedModel.getMessage(), context,feedModel.getIs_pin());
                                // Linkify.addLinks(viewHolder.statusTextView, Linkify.WEB_URLS);
                            }
                        }
                     /*   String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
                        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
                        String scheme = "http://zipinfo.com";*/

                    } else {
                        viewHolder.statusTextView.setVisibility(View.GONE);

                    }
                    viewHolder.delImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteOrEditPopup(viewHolder.delImageView, feedModel, position);
                        }
                    });
                    if (feedModel.getUserDetailModel_creator().getUserId().equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID))) {
                        viewHolder.delImageView.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.delImageView.setVisibility(View.GONE);
                    }
                   /* if (feedModel.getParentFeedDetail() != null && feedModel.getUserDetailModel_creator() != null) {
                        if (feedModel.getParentFeedDetail().getUserId() != null && !feedModel.getParentFeedDetail().getUserId().equalsIgnoreCase("") && feedModel.getUserDetailModel_creator().getUserId() != null && !feedModel.getUserDetailModel_creator().getUserId().equalsIgnoreCase("")) {
                            if (feedModel.getParentFeedDetail().getUserId().equalsIgnoreCase(feedModel.getUserDetailModel_creator().getUserId())) {
                                viewHolder.feeddeletelistingLinearLayout.setVisibility(View.GONE);
                            } else {
                                viewHolder.feeddeletelistingLinearLayout.setVisibility(View.VISIBLE);

                            }
                        } else {
                            viewHolder.feeddeletelistingLinearLayout.setVisibility(View.VISIBLE);

                        }
                    } else {
                        viewHolder.feeddeletelistingLinearLayout.setVisibility(View.VISIBLE);

                    }*/
                  /*  if (feedModel.getParentFeedDetail() != null) {
                        viewHolder.feeddeletelistingLinearLayout.setVisibility(View.GONE);

                    } else {
                        viewHolder.feeddeletelistingLinearLayout.setVisibility(View.VISIBLE);

                    }*/
                    if (feedModel.getTotal_likes() != null) {
                        if (feedModel.getTotal_likes().equalsIgnoreCase("1"))
                            viewHolder.likesTextView.setText(feedModel.getTotal_likes() + " Like");
                        else
                            viewHolder.likesTextView.setText(feedModel.getTotal_likes() + " Likes");

                    } else {
                        viewHolder.likesTextView.setText("0 Likes");

                    }
                    if (feedModel.getTotal_comments() != null) {
                        if (feedModel.getTotal_comments().equalsIgnoreCase("1"))
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
                 /*   viewHolder.nameTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, Dr_Profile.class);
                            if (!userDetailModel.getUserId().equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID)))
                                intent.putExtra("UserId", userDetailModel.getUserId());
                            context.startActivity(intent);
                        }
                    });*/
                    if (feedModel.getCreated() != null) {
                        viewHolder.timeStampTextView.setText(feedModel.getCreated());
                    }
                    if (feedModel.getTitleQuestion() != null && !feedModel.getTitleQuestion().equalsIgnoreCase("")) {
                        viewHolder.QuestionTextView.setText(feedModel.getTitleQuestion().trim().toString());
                        viewHolder.QuestionTextView.setVisibility(View.VISIBLE);
                        //  Linkify.addLinks(viewHolder.QuestionTextView, Linkify.WEB_URLS);
                        Methods.hyperlink(viewHolder.QuestionTextView, feedModel.getTitleQuestion(), context,feedModel.getIs_pin());

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
                            //ApplicationSingleton.setPost_position(position);
                            context.startActivity(intent);

                        }
                    });
                    viewHolder.likeFeedLinearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            positionat = position;
                            if (NetworkUtill.isNetworkAvailable(context)) {
                                LikePostAsynctask likePostAsynctask = new LikePostAsynctask(feedModel.getFeed_id(), userId, authToken, feedModel.getUser_has_liked());
                                likePostAsynctask.execute();
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
                                likePostAsynctask.execute();
                            } else {
                                NetworkUtill.showNoInternetDialog(context);
                            }
                        }
                    });
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           /* */
                            try{
                            if (position == 1 && feedModel.getIs_pin() == 1) {



// Receiving side
                                String q             = Base64.encodeToString(sesstionManager.getUserDetails().get(SesstionManager.USER_ID).getBytes(), Base64.NO_WRAP);

                                String str = Methods.getUrl(feedModel.getMessage());
                                str=str+"?userId="+q;
                                if (!str.equalsIgnoreCase("")) {



                                    Uri uri = Uri.parse(str)
                                            .buildUpon()
                                            .build();
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(uri);
                                    context.startActivity(i);
                                }
                            } else {
                                ApplicationSingleton.setPostSelectedPostion(position);
                                Intent intent = new Intent(context, PostDetailActivity.class);
                                intent.putExtra("feedId", feedModel.getFeed_id());
                                context.startActivity(intent);
                            }

                        }catch (Exception e)
                            {
                            e.printStackTrace();}
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
                            ApplicationSingleton.setPost_position(position);

                            context.startActivity(intent);
                        }
                    });
                    viewHolder.feedcommentlistingLinearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, CommentsActivity.class);
                            intent.putExtra("feedId", feedModel.getFeed_id());
                            ApplicationSingleton.setPost_position(position);

                            context.startActivity(intent);
                        }
                    });
                    //   TypeFaceMethods.setRegularTypeFaceForTextView(viewHolder.nameTextView, context);
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
                                      /*  Intent intent = new Intent(context, Dr_Profile.class);
                                        if (!feedModel.getCreatedBy().getUserId().equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID)))
                                            intent.putExtra("UserId", feedModel.getCreatedBy().getUserId());
                                        context.startActivity(intent);*/
                                        Methods.profileUser(feedModel.getCreatedBy().getUser_Type(), context, feedModel.getCreatedBy().getUserId());
                                    }
                                });
                                viewHolder.parentname.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                      /*  Intent intent = new Intent(context, Dr_Profile.class);
                                        if (!feedModel.getCreatedBy().getUserId().equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID)))
                                            intent.putExtra("UserId", feedModel.getCreatedBy().getUserId());
                                        context.startActivity(intent);*/
                                        Methods.profileUser(feedModel.getCreatedBy().getUser_Type(), context, feedModel.getCreatedBy().getUserId());

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
                                           /* Intent intent = new Intent(context, CommunitiesDetails.class);
                                            intent.putExtra("groupId", feedModel.getCommunity_Id());
                                            context.startActivity(intent);*/
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

                                    } else
                                        str2 = new SpannableString(Methods.getName(feedModel.getCreatedBy().getTitle(), feedModel.getCreatedBy().getName()));

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

                                          /*  Intent intent = new Intent(context, Dr_Profile.class);
                                            if (!feedModel.getCreatedBy().getUserId().equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID)))
                                                intent.putExtra("UserId", feedModel.getCreatedBy().getUserId());
                                            context.startActivity(intent);*/
                                            Methods.profileUser(feedModel.getCreatedBy().getUser_Type(), context, feedModel.getCreatedBy().getUserId());

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
                           /* Intent intent = new Intent(context, Dr_Profile.class);
                            if (!userDetailModel.getUserId().equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID)))
                                intent.putExtra("UserId", userDetailModel.getUserId());
                            context.startActivity(intent);*/
                            Methods.profileUser(userDetailModel.getUser_Type(), context, userDetailModel.getUserId());

                        }
                    };
                    builder.setSpan(clickSpan, 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    viewHolder.nameTextView.setText(builder, TextView.BufferType.SPANNABLE);
                    viewHolder.nameTextView.setMovementMethod(LinkMovementMethod.getInstance());

               /*     TypeFaceMethods.setRegularTypeBoldFaceTextView(viewHolder.QuestionTextView, context);
                    TypeFaceMethods.setRegularTypeBoldFaceTextView(viewHolder.nameTextView, context);
                    TypeFaceMethods.setRegularTypeFaceForTextView(viewHolder.timeStampTextView, context);
                    TypeFaceMethods.setRegularTypeFaceForTextView(viewHolder.statusTextView, context);*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }


    }

    @Override
    public int getItemCount() {
        return feedModels.size();
    }

    public void setData(ArrayList<FeedModel> feedItems) {
        this.feedModels = feedItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    public class HeaderView extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView postAn;

        public HeaderView(View itemView) {
            super(itemView);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.pro);
            postAn = (TextView) itemView.findViewById(R.id.post_an);
        }
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView playicon;
        View left_view, right_view, bottom_view;
        TextView youHaveWishedTextView, comment_text;
        TextView likesTextView, commntsTextView;
        TextView statusTextView;
        CircleImageView profilePicparent;
        TextView  nameTextView, QuestionTextView,
                timeStampTextView, announcementTextView,
                noOfLikeTextView, whisesTextView, noOfCommentTextView,
                linkTitleTextView, linkDescriptiontextView, docNameTextView, docTypeTextView,
                anniversaryTextView, announcementTypeTextView, notificationTitleTextView, viewAllTextView, moreviewTextView;
        ImageView feedImageView, linkImageView, anniverasaryLayoutImage, docImageView, announcementImage, userImage, feedlikeimg, cancelAnnouncementImageView, basicAnnouncemetImage;
        LinearLayout docTypeLayout, sharedLay, announcementLinearLayout, feeddeletelistingLinearLayout, CommentSectionLinearLayout, feedcommentlistingLinearLayout, feedcommentlisting, feedlikeLinearLayout, likeFeedLinearLayout, share_feedLinearLayout, normalFeedLayout, cardshoderLinearLayout;
        CircleImageView profileImageView;
        FrameLayout frameVideoFrameLayout;
        TextView statusshare;
        LinearLayout parentLay;
        RelativeLayout profileSectionLinearLayout, basicAnnouncemetLinearLayout, sayCongratsRelativeLayout, anniversaryLinearLayout, hetrogenousAnnouncementLinearLayout;
        Button buttondownload;
        VideoView videoView;
        TextView txtTextView;
        ImageView delImageView;
        WebView webView;
        TextView parentname;
        LinearLayout link_title_des_lay;
        View basicAnnouncemet_view;
        LinearLayout moreLinearLayout, two_or_moreLinearLayout;
        ImageView imageFirstImageView, imageSecImageView;
        RelativeLayout relativeLayout1;

        public MyViewHolder(View itemView) {
            super(itemView);
            relativeLayout1 = (RelativeLayout) itemView.findViewById(R.id.relativeLayout1);
            left_view = (View) itemView.findViewById(R.id.left_view);
            likesTextView = (TextView) itemView.findViewById(R.id.likes);
            parentname = (TextView) itemView.findViewById(R.id.parentname);
            profilePicparent = (CircleImageView) itemView.findViewById(R.id.profilePicparent);

            commntsTextView = (TextView) itemView.findViewById(R.id.commnts);
            bottom_view = (View) itemView.findViewById(R.id.bottom_view);
            right_view = (View) itemView.findViewById(R.id.right_view);
            txtTextView = (TextView) itemView.findViewById(R.id.txt);
            link_title_des_lay = (LinearLayout) itemView.findViewById(R.id.link_title_des_lay);
            webView = (WebView) itemView.findViewById(R.id.webview);
            QuestionTextView = (TextView) itemView.findViewById(R.id.Question);
            playicon = (ImageView) itemView.findViewById(R.id.playicon);
            youHaveWishedTextView = (TextView) itemView.findViewById(R.id.youHaveWished);
            moreLinearLayout = (LinearLayout) itemView.findViewById(R.id.moreLinearLayout);
            moreviewTextView = (TextView) itemView.findViewById(R.id.moreview);
            imageFirstImageView = (ImageView) itemView.findViewById(R.id.imageFirst);

            parentLay = (LinearLayout) itemView.findViewById(R.id.parentLay);
            imageSecImageView = (ImageView) itemView.findViewById(R.id.imageSec);
            delImageView = (ImageView) itemView.findViewById(R.id.del);
            two_or_moreLinearLayout = (LinearLayout) itemView.findViewById(R.id.two_or_more);
            frameVideoFrameLayout = (FrameLayout) itemView.findViewById(R.id.frameVideo);
            statusshare = (TextView) itemView.findViewById(R.id.statusshare);
            sharedLay = (LinearLayout) itemView.findViewById(R.id.sharedLay);
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

    public class LikePostAsynctask extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String feedId, userId;
        int liked;

        private String responseBody;
        HttpClient client;


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
            circularProgressBar.setVisibility(View.GONE);
            try {
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

    class myWebChromeClient extends WebChromeClient {
        private Bitmap mDefaultVideoPoster;
        private View mVideoProgressView;

        @Override
        public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
            super.onReceivedTouchIconUrl(view, url, precomposed);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }

        @Override
        public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {

            onShowCustomView(view, callback);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {

            // if a view already exists then immediately terminate the new one
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
            mCustomView.setVisibility(View.VISIBLE);

            webView.setVisibility(View.GONE);

            customViewContainer.setVisibility(View.VISIBLE);
            ((MainActivity) context).setTabsVisibility(false);

            customViewContainer.addView(view);
            customViewCallback = callback;
        }

        @Override
        public View getVideoLoadingProgressView() {

            if (mVideoProgressView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                mVideoProgressView = inflater.inflate(R.layout.video_progress, null);


            }
            return mVideoProgressView;
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();    //To change body of overridden methods use File | Settings | File Templates.
            if (mCustomView == null)
                return;

            webView.setVisibility(View.VISIBLE);
            customViewContainer.setVisibility(View.GONE);
            ((MainActivity) context).setTabsVisibility(true);
            mCustomView.setVisibility(View.GONE);
            // Remove the custom view from its container.
            customViewContainer.removeView(mCustomView);
            customViewCallback.onCustomViewHidden();

            mCustomView = null;
        }
    }

    public void sharePopup(LinearLayout view, final FeedModel feedModel) {
        PopupMenu popup = new PopupMenu(context, view);
        popup.getMenuInflater().inflate(R.menu.popup_menu_share, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                             public boolean onMenuItemClick(MenuItem item) {
                                                 //        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                                                 //  int index = info.position;
                                                 //  System.out.print(index);
                                                 switch (item.getItemId()) {
                                                     case R.id.publicshare:
                                                         Intent intent = new Intent(context, PublicShare.class);
                                                         intent.putExtra("feedId", feedModel.getFeed_id());
                                                         intent.putExtra("userId", userId);
                                                         if (feedModel.getParentFeedDetail() != null) {
                                                             intent.putExtra("parentFeedId", feedModel.getParent_feed());

                                                         }
                                                         context.startActivity(intent);

                                                       /*  if (NetworkUtill.isNetworkAvailable(context)) {
                                                             SharePublicAsyncTask sharePublicAsyncTask = new SharePublicAsyncTask(feedModel.getFeed_id(), userId, authToken);
                                                             sharePublicAsyncTask.execute();
                                                         } else {
                                                             NetworkUtill.showNoInternetDialog(context);
                                                         }*/
                                                         break;
                                                     case R.id.groupstimeline:
                                                         Intent intent1 = new Intent(context, ShareOnFriendsTimeline.class);

                                                         intent1.putExtra("feedId", feedModel.getFeed_id());
                                                         if (feedModel.getParentFeedDetail() != null) {
                                                             intent1.putExtra("parentFeedId", feedModel.getParent_feed());

                                                         }
                                                         intent1.putExtra("userId", userId);
                                                         intent1.putExtra("shareOnGroup", true);
                                                         context.startActivity(intent1);

                                                         break;
                                                     case R.id.share_exteraly:
                                                         try {
                                                             if (feedModel.getMessage() != null && feedModel.getMessage().length() > 0) {
                                                                 text = feedModel.getMessage();
                                                             }
       /* if (feedModel.getFeedSourceArrayList() != null && feedModel.getFeedSourceArrayList().size() > 0) {
            if (feedModel.getFeedSourceArrayList().get(0) != null && feedModel.getFeedSourceArrayList().get(0).length() > 0) {
                shareText = feedModel.getFeedSourceArrayList().get(0);
            }
        }*/
                                                             String path = null;
                                                             Uri imageUri = null;
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
                                                                     //  f.getAbsoluteFile();
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
                                                                 // shareIntent.setDataAndType(imageUri,context. getContentResolver().getType(imageUri));

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
                /*if (item.getTitle().equals(R.string.SharePublic)) {


                } else if (item.getTitle().equals(R.string.shareonFriendsTimeline)) {

                } else {

                }*/
                                                 return true;
                                             }
                                         }

        );

        popup.show();//showing popup menu
    }

    public void deleteOrEditPopup(ImageView view, final FeedModel feedModel, final int position) {
        PopupMenu popup = new PopupMenu(context, view);
        popup.getMenuInflater().inflate(R.menu.popup_menu_edit_delete, popup.getMenu());

        if (feedModel.getParentFeedDetail() != null) {
            popup.getMenu().getItem(0).setVisible(false);
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                //  int index = info.position;
                //  System.out.print(index);
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
                /*if (item.getTitle().equals(R.string.SharePublic)) {


                } else if (item.getTitle().equals(R.string.shareonFriendsTimeline)) {

                } else {

                }*/
                return true;
            }
        });

        popup.show();//showing popup menu
    }

    public void setDialog(final FeedModel feedModel) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to Delete this post?");// Add the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                if (NetworkUtill.isNetworkAvailable(context)) {
                    DeletepostAsyncTask deletepostAsyncTask = new DeletepostAsyncTask(feedModel.getFeed_id(), userId, authToken);
                    deletepostAsyncTask.execute();
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

    public class SharePublicAsyncTask extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String feedId, userId;


        private String responseBody;
        HttpClient client;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public SharePublicAsyncTask(String feedId, String userId, String authToken) {
            this.feedId = feedId;
            this.authToken = authToken;
            this.authToken = authToken;
            this.authToken = authToken;
            this.userId = userId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (jo != null) {
                    if (jo.has("responce") && !jo.isNull("responce")) {
                        int responseerror = jo.getJSONObject("responce").getInt("error");
                        if (responseerror == 0) {
                            Toast.makeText(context, "Post shared successfully", Toast.LENGTH_LONG).show();
                        }
                        FeedModel feedModel = FeedParser.singleFeed(jo.getJSONObject("responce"));
                        feedModels.add(1, feedModel);
                        notifyItemInserted(1);
                        notifyItemRangeChanged(1, feedModels.size());
                        // ApplicationSingleton.setIsProfilePostExecuted(true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {


                String url = AppUrl.BaseUrl + "feed/share";
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
                pairs.add(new BasicNameValuePair("shareType", "1"));
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

    class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String url1 = url;
            System.out.print(url1);
            return super.shouldOverrideUrlLoading(view, url);    //To change body of overridden methods use File | Settings | File Templates.
        }
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

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    public static void doResizeTextView(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        doResizeTextView(tv, -1, "View Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        doResizeTextView(tv, 3, "View More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }
    private void cycleTextViewExpansion(TextView tv){
        int collapsedMaxLines = 3;
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, "maxLines",
                15 == collapsedMaxLines? tv.getLineCount() : collapsedMaxLines);
        animation.setDuration(200).start();
    }
}


