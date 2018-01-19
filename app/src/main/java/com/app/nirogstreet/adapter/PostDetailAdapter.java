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
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.app.nirogstreet.activites.VideoPlay_Activity;
import com.app.nirogstreet.activites.YoutubeVideo_Play;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.FeedModel;
import com.app.nirogstreet.model.UserDetailModel;
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
 * Created by Preeti on 28-11-2017.
 */

public class PostDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    int positionat;
    FrameLayout customViewContainer;
    String text, videourl, title;

    private WebChromeClient.CustomViewCallback customViewCallback;

    private String authToken, userId;
    SpannableString span;
    SpannableString span2, str3, str4;

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
    SesstionManager sesstionManager;
    String groupId = "";
    CircularProgressBar circularProgressBar;
    private SpannableStringBuilder builder;
    SpannableString str2;

    public PostDetailAdapter(Context context, ArrayList<FeedModel> feedModels, Activity activity, String groupId, FrameLayout customViewContainer, CircularProgressBar circularProgressBar) {
        this.feedModels = feedModels;
        this.context = context;
        this.activity = activity;
        this.circularProgressBar = circularProgressBar;
        this.groupId = groupId;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case TYPE_HEADER:
                HeaderView myViewHolder = (HeaderView) holder;
                // Glide.with(context).load(askQuestionImages).into(myViewHolder.circleImageView);
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
                switch (feed_type) {
                    case FEED_TYPE_YOUTUBEVIDEO_LINK:
                        switch (link_type) {
                            case LINK_TYPE_YOUTUBE_VIDEO:
                               /* viewHolder.playicon.setVisibility(View.GONE);
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
                                viewHolder.relativeLayout1.setVisibility(View.VISIBLE);
                                viewHolder.link_title_des_lay.setVisibility(View.GONE);
                                viewHolder.left_view.setVisibility(View.GONE);
                                viewHolder.right_view.setVisibility(View.GONE);
                                viewHolder.bottom_view.setVisibility(View.GONE);
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

                               /* Glide.with(context)
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
                                viewHolder.linkImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String urlString = feedModel.getFeed_source();
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(urlString));
                                        context.startActivity(i);
                                    }
                                });


                                Picasso.with(context)
                                        .load(feedModel.getUrl_image())
                                        .placeholder(R.drawable.default_)
                                        .error(R.drawable.default_)
                                        .into(viewHolder.linkImageView);
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
                        viewHolder.relativeLayout1.setVisibility(View.VISIBLE);

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
                        viewHolder.link_title_des_lay.setVisibility(View.GONE);
                        viewHolder.relativeLayout1.setVisibility(View.GONE);

                        viewHolder.left_view.setVisibility(View.GONE);
                        viewHolder.right_view.setVisibility(View.GONE);
                        viewHolder.bottom_view.setVisibility(View.GONE);
                        viewHolder.two_or_moreLinearLayout.setVisibility(View.GONE);
                        viewHolder.two_or_moreLinearLayout.setVisibility(View.GONE);
                        viewHolder.linkDescriptiontextView.setVisibility(View.GONE);
                        viewHolder.CommentSectionLinearLayout.setVisibility(View.VISIBLE);
                        viewHolder.profileSectionLinearLayout.setVisibility(View.VISIBLE);
                        viewHolder.profileSectionLinearLayout.setVisibility(View.VISIBLE);
                        viewHolder.videoView.setVisibility(View.GONE);
                        viewHolder.webView.setVisibility(View.GONE);
                        viewHolder.relativeLayout1.setVisibility(View.VISIBLE);
                        if(feedModel.getUrl_image()!=null&&!feedModel.getUrl_image().equalsIgnoreCase(""))
                        {
                            Picasso.with(context)
                                    .load(feedModel.getUrl_image())
                                    .placeholder(R.drawable.default_)
                                    .error(R.drawable.default_)
                                    .into(viewHolder.feedImageView);
                        }else {
                            viewHolder.feedImageView.setImageResource(R.drawable.default_videobg);
                        }                      /*  Bitmap bmThumbnail;
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
                        viewHolder.linkTitleTextView.setVisibility(View.GONE);
                        viewHolder.link_title_des_lay.setVisibility(View.GONE);
                        viewHolder.left_view.setVisibility(View.GONE);
                        viewHolder.right_view.setVisibility(View.GONE);
                        viewHolder.bottom_view.setVisibility(View.GONE);
                        viewHolder.relativeLayout1.setVisibility(View.VISIBLE);

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
                                viewHolder.docNameTextView.setVisibility(View.VISIBLE);

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
                        viewHolder.relativeLayout1.setVisibility(View.GONE);

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
                    viewHolder.statusTextView.setText(feedModel.getMessage());
                    Methods.hyperlink(viewHolder.statusTextView, feedModel.getMessage(), context);

                    viewHolder.statusTextView.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.statusTextView.setVisibility(View.GONE);

                }

                if (feedModel.getTotal_likes() != null) {
                    ApplicationSingleton.setTotalLike(Integer.parseInt(feedModel.getTotal_likes()));

                    if (feedModel.getTotal_likes().equalsIgnoreCase("0") || feedModel.getTotal_likes().equalsIgnoreCase("1"))
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
                if (feedModel.getParentFeedDetail() != null && feedModel.getUserDetailModel_creator() != null) {
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

                }

                final UserDetailModel userDetailModel = feedModel.getUserDetailModel_creator();

                if (userDetailModel.getProfile_pic() != null && !userDetailModel.getProfile_pic().equalsIgnoreCase("")) {
                    Picasso.with(context)
                            .load(userDetailModel.getProfile_pic())
                            .placeholder(R.drawable.user)
                            .error(R.drawable.user)
                            .into(viewHolder.profileImageView);
                }
                viewHolder.nameTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, Dr_Profile.class);
                        if (!userDetailModel.getUserId().equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID)))
                            intent.putExtra("UserId", userDetailModel.getUserId());
                        context.startActivity(intent);
                    }
                });
                if (feedModel.getCreated() != null) {
                    viewHolder.timeStampTextView.setText(feedModel.getCreated());
                }
                if (feedModel.getUser_has_liked() == 1) {
                    ApplicationSingleton.setCurruntUserLiked(true);
                    viewHolder.feedlikeimg.setSelected(true);
                } else {
                    viewHolder.feedlikeimg.setSelected(false);
                    ApplicationSingleton.setCurruntUserLiked(true);


                }
                if (feedModel.getTitleQuestion() != null && !feedModel.getTitleQuestion().equalsIgnoreCase("")) {
                    viewHolder.QuestionTextView.setText(feedModel.getTitleQuestion());
                    viewHolder.QuestionTextView.setVisibility(View.VISIBLE);
                    Methods.hyperlink(viewHolder.QuestionTextView, feedModel.getTitleQuestion(), context);

                } else {
                    viewHolder.QuestionTextView.setVisibility(View.GONE);

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
                try {
                    if (feedModel.getTotal_comments() != null)
                        ApplicationSingleton.setNoOfComment(Integer.parseInt(feedModel.getTotal_comments()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (feedModel.getEnable_comment().equalsIgnoreCase("1")) {
                    viewHolder.noOfCommentTextView.setVisibility(View.VISIBLE);

                } else {
                    viewHolder.noOfCommentTextView.setVisibility(View.VISIBLE);

                }

                viewHolder.feedlikeLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, LikesDisplayActivity.class);
                        intent.putExtra("feedId", feedModel.getFeed_id());
                        context.startActivity(intent);
                    }
                });
                viewHolder.feedcommentlistingLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      /*  Intent intent = new Intent(context, CommentsActivity.class);
                        intent.putExtra("feedId", feedModel.getFeed_id());
                        ApplicationSingleton.setPost_position(position);

                        context.startActivity(intent);
               */
                    }
                });


                if (userDetailModel != null && userDetailModel.getName() != null) {
                    String name = "Dr. " + userDetailModel.getName();
                    builder = new SpannableStringBuilder();
                    span = new SpannableString(name);
                    span.setSpan(new ForegroundColorSpan(Color.BLACK), 0, span.length(), 0);
                    span.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(span);
                    // viewHolder.nameTextView.setText("Dr. " + userDetailModel.getName().trim());
                    String xxx = feedModel.getCommunity_Id();
                    if (feedModel.getShareWithModels()!=null&&feedModel.getShareWithModels().size()>0) {
                        if (feedModel.getParent_feed() != null) {
                            if(feedModel.getShareWithModels()!=null&&feedModel.getShareWithModels().size()>0)
                            {
                                if(feedModel.getShareWithModels().get(0).getShareMessage()!=null&&!feedModel.getShareWithModels().get(0).getShareMessage().equalsIgnoreCase("")) {
                                    viewHolder.statusshare.setText(feedModel.getShareWithModels().get(0).getShareMessage());
                                    viewHolder.statusshare.setVisibility(View.VISIBLE);
                                }
                            }
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(5, 0, 5, 5);
                            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            params1.setMargins(0, 0, 0, 0);
                            viewHolder.relativeLayout1.setLayoutParams(params1);

                            viewHolder.sharedLay.setLayoutParams(params);
                            viewHolder.sharedLay.setBackgroundResource(R.drawable.round_new);

                            if (feedModel.getFeed_type().equalsIgnoreCase("5")) {
                                str2 = new SpannableString(" shared a video ");
                                str2.setSpan(new ForegroundColorSpan(Color.rgb(148, 148, 156)), 0, str2.length(), 0);
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
                            }
                            if (feedModel.getFeed_type().equalsIgnoreCase("1")) {
                                str2 = new SpannableString(" shared a post ");
                                str2.setSpan(new ForegroundColorSpan(Color.rgb(148, 148, 156)), 0, str2.length(), 0);
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
                                          /*  Intent intent = new Intent(context, CommunitiesDetails.class);
                                            intent.putExtra("groupId", feedModel.getCommunity_Id());
                                            context.startActivity(intent);*/
                                    }
                                };

                                String thirdspan = str2.toString();
                                int third = builder.toString().indexOf(thirdspan);
                                builder.setSpan(clickSpan1, third, third + str2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                viewHolder.nameTextView.setText(builder, TextView.BufferType.SPANNABLE);
                                viewHolder.nameTextView.setMovementMethod(LinkMovementMethod.getInstance());

                            }
                            if (feedModel.getLink_type() != null && feedModel.getLink_type().equalsIgnoreCase("2")) {
                                str2 = new SpannableString(" shared a link ");
                                str2.setSpan(new ForegroundColorSpan(Color.rgb(148, 148, 156)), 0, str2.length(), 0);
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

                            } else {
                                if (feedModel.getFeed_type().equalsIgnoreCase("2")) {
                                    str2 = new SpannableString(" shared an image ");
                                    str2.setSpan(new ForegroundColorSpan(Color.rgb(148, 148, 156)), 0, str2.length(), 0);
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

                                }
                            }
                            if (feedModel.getFeed_type().equalsIgnoreCase("6")) {
                                str2 = new SpannableString(" shared a document ");
                                str2.setSpan(new ForegroundColorSpan(Color.rgb(148, 148, 156)), 0, str2.length(), 0);
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

                            }
                        }
                    } else {
                        viewHolder.statusshare.setVisibility(View.GONE);

                        viewHolder.sharedLay.setBackgroundResource(0);
                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params1.setMargins(0, 10, 0, 0);
                        viewHolder.relativeLayout1.setLayoutParams(params1);

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
                        Intent intent = new Intent(context, Dr_Profile.class);
                        if (!userDetailModel.getUserId().equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID)))
                            intent.putExtra("UserId", userDetailModel.getUserId());
                        context.startActivity(intent);
                    }
                };
                builder.setSpan(clickSpan, 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.nameTextView.setText(builder, TextView.BufferType.SPANNABLE);
                viewHolder.nameTextView.setMovementMethod(LinkMovementMethod.getInstance());

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
            return TYPE_ITEM;
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
                if (String.valueOf(payloads.get(0)).equalsIgnoreCase("0") || String.valueOf(payloads.get(0)).equalsIgnoreCase("1"))
                    viewHolder.likesTextView.setText(String.valueOf(payloads.get(0)) + " Like");
                else
                    viewHolder.likesTextView.setText(String.valueOf(payloads.get(0)) + " Likes");
                ApplicationSingleton.setTotalLike(Integer.parseInt(payloads.get(0) + ""));
                if (!viewHolder.feedlikeimg.isSelected()) {
                    viewHolder.feedlikeimg.setSelected(true);
                    ApplicationSingleton.setCurruntUserLiked(true);
                } else {
                    viewHolder.feedlikeimg.setSelected(false);
                    ApplicationSingleton.setCurruntUserLiked(false);

                }
            } else if (payloads.get(0) instanceof Integer) {
                int i;
                if (feedModels.get(position).getTotal_comments() != null) {
                    i = Integer.parseInt(feedModels.get(position).getTotal_comments());
                } else {
                    i = 0;
                }
                i = i + 1;
                ApplicationSingleton.setNoOfComment(i);

                feedModels.get(position).setTotal_comments(i + "");
                if (feedModels.get(position).getTotal_comments().equalsIgnoreCase("0") || feedModels.get(position).getTotal_comments().equalsIgnoreCase("1"))
                    viewHolder.commntsTextView.setText(i + " Comment");
                else
                    viewHolder.commntsTextView.setText(i + " Comments");

            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView playicon;
        TextView youHaveWishedTextView, comment_text;
        TextView statusTextView, nameTextView, QuestionTextView,
                timeStampTextView, announcementTextView,
                noOfLikeTextView, whisesTextView, noOfCommentTextView,
                linkTitleTextView, linkDescriptiontextView, docNameTextView, docTypeTextView,
                anniversaryTextView, announcementTypeTextView, notificationTitleTextView, viewAllTextView, moreviewTextView;
        ImageView feedImageView, linkImageView, anniverasaryLayoutImage, docImageView, announcementImage, userImage, feedlikeimg, cancelAnnouncementImageView, basicAnnouncemetImage;
        LinearLayout docTypeLayout,sharedLay, announcementLinearLayout, feeddeletelistingLinearLayout, CommentSectionLinearLayout, feedcommentlistingLinearLayout, feedcommentlisting, feedlikeLinearLayout, likeFeedLinearLayout, share_feedLinearLayout, normalFeedLayout, cardshoderLinearLayout;
        CircleImageView profileImageView;
        FrameLayout frameVideoFrameLayout;
        TextView likesTextView, commntsTextView;
        RelativeLayout relativeLayout1;

        RelativeLayout profileSectionLinearLayout, basicAnnouncemetLinearLayout, sayCongratsRelativeLayout, anniversaryLinearLayout, hetrogenousAnnouncementLinearLayout;
        Button buttondownload;
        VideoView videoView;
        TextView txtTextView;
        ImageView delImageView;
        TextView statusshare;
        WebView webView;
        View left_view, right_view, bottom_view;
        LinearLayout link_title_des_lay;

        View basicAnnouncemet_view;
        LinearLayout moreLinearLayout, two_or_moreLinearLayout;
        ImageView imageFirstImageView, imageSecImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            relativeLayout1 = (RelativeLayout) itemView.findViewById(R.id.relativeLayout1);
            delImageView = (ImageView) itemView.findViewById(R.id.del);
            likesTextView = (TextView) itemView.findViewById(R.id.likes);
            commntsTextView = (TextView) itemView.findViewById(R.id.commnts);
            left_view = (View) itemView.findViewById(R.id.left_view);
            bottom_view = (View) itemView.findViewById(R.id.bottom_view);
            right_view = (View) itemView.findViewById(R.id.right_view);
            txtTextView = (TextView) itemView.findViewById(R.id.txt);
            sharedLay=(LinearLayout)itemView.findViewById(R.id.sharedLay);
            statusshare=(TextView)itemView.findViewById(R.id.statusshare);

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

    public void setDialog(final FeedModel feedModel) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to Delete this post?");// Add the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
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

        AlertDialog dialog = builder.create();
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
                                                                     for (int i=0;i<feedModel.getFeedSourceArrayList().size();i++) {
                                                                         URL url = new URL(feedModel.getFeedSourceArrayList().get(i));
                                                                         Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                                                                         Uri bitmapUri=null;

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
                                                             if (files != null&&files.size()>0) {
                                                                 // shareIntent.setDataAndType(imageUri,context. getContentResolver().getType(imageUri));

                                                                 shareIntent.putExtra(Intent.EXTRA_STREAM, files);
                                                                 shareIntent.setType("image/*");
                                                                 shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                             }
                                                             if (title != null && title.length() > 0 || text != null && text.length() > 0 || videourl != null && videourl.length() > 0||files.size()>0||feedModel.getLink_type()!=null) {
                                                                 if (title != null && title.length() > 0 && text != null && text.length() > 0 && videourl != null && videourl.length() > 0) {
                                                                     shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, title + "\n\n" + text + "\n\n" + videourl);
                                                                 } else if (title != null && title.length() > 0 && text != null && text.length() > 0) {
                                                                     shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, title + "\n\n" + text);
                                                                 } else if (title != null && title.length() > 0) {
                                                                     shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, title);
                                                                 } else if (text != null && text.length() > 0) {
                                                                     shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
                                                                 }else if(feedModel.getLink_type()!=null)
                                                                 {
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

    public class SharePublicAsyncTask extends AsyncTask<Void, Void, Void> {
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

        public SharePublicAsyncTask(String feedId, String userId, String authToken) {
            this.feedId = feedId;
            this.authToken = authToken;
            this.userId = userId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (jo != null) {
                 /*   FeedModel feedModel = FeedParser.singleFeed(jo);
                    feedModels.add(0, feedModel);
                    notifyItemInserted(0);

                    notifyItemRangeChanged(0, feedModels.size());
                    sharepublic = true;
                    SharedPreferences sharedPref2 = context.getSharedPreferences("imgvidupdate", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = sharedPref2.edit();
                    editor2.putBoolean("imgvidupdate", sharepublic);
                    editor2.commit();

*/
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
                pairs.add(new BasicNameValuePair("shreType", "1"));
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

    public void deleteOrEditPopup(ImageView view, final FeedModel feedModel, final int position) {
        PopupMenu popup = new PopupMenu(context, view);
        popup.getMenuInflater().inflate(R.menu.popup_menu_edit_delete, popup.getMenu());
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
                    ApplicationSingleton.setIsProfilePostExecuted(true);
                    ((Activity) context).finish();
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
}

