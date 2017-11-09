package com.app.nirogstreet.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.VideoView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.AlbumGallary;
import com.app.nirogstreet.activites.CommentsActivity;
import com.app.nirogstreet.activites.FullScreenImage;
import com.app.nirogstreet.activites.LikesDisplayActivity;
import com.app.nirogstreet.activites.PostingActivity;
import com.app.nirogstreet.activites.VideoPlay_Activity;
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
 * Created by Preeti on 26-10-2017.
 */

public class TimelineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    int positionat;
    private  String authToken, userId;

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
    String groupId="";


    public TimelineAdapter(Context context, ArrayList<FeedModel> feedModels, Activity activity,String groupId) {
        this.feedModels = feedModels;
        this.context = context;
        this.activity = activity;
        this.groupId=groupId;
        sesstionManager=new SesstionManager(context);
        HashMap<String, String> userDetails = sesstionManager.getUserDetails();
        authToken = userDetails.get(SesstionManager.AUTH_TOKEN);
        userId = userDetails.get(SesstionManager.USER_ID);


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
                TypeFaceMethods.setRegularTypeFaceForTextView(myViewHolder.postAn, context);
                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PostingActivity.class);
                        if(!groupId.equalsIgnoreCase("")) {
                            intent.putExtra("groupId", groupId);
                        }
                        context.startActivity(intent);

                    }
                });
                break;
            case TYPE_ITEM:


                MyViewHolder viewHolder = (MyViewHolder) holder;

                final FeedModel feedModel = feedModels.get(position);
                int feed_type = Integer.parseInt(feedModel.getFeed_type());
                int link_type = 0;
                if (feedModel.getLink_type() != null) {
                    link_type = Integer.parseInt(feedModel.getLink_type());
                }
                switch (feed_type) {
                    case FEED_TYPE_YOUTUBEVIDEO_LINK:
                        switch (link_type) {
                            case LINK_TYPE_YOUTUBE_VIDEO:
                                viewHolder.playicon.setVisibility(View.GONE);
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
                                if (feedModel.getTotal_comments() != null) {
                                    //   holder.noOfCommentTextView.setText(feedModel.getTotalComments());
                                }
                                break;
                            case LINK_TYPE_WEB_LINK:
                                viewHolder.frameVideoFrameLayout.setVisibility(View.GONE);
                                viewHolder.playicon.setVisibility(View.GONE);
                                viewHolder.two_or_moreLinearLayout.setVisibility(View.GONE);

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
                                break;
                        }
                        break;
                    case FEED_TYPE_IMAGE:
                        viewHolder.frameVideoFrameLayout.setVisibility(View.GONE);
                        viewHolder.playicon.setVisibility(View.GONE);
                        viewHolder.two_or_moreLinearLayout.setVisibility(View.GONE);
                        viewHolder.linkImageView.setVisibility(View.GONE);
                        viewHolder.linkTitleTextView.setVisibility(View.GONE);
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
                                    .asGif()
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
                                        int num = strings.size() - 1;
                                        viewHolder.moreviewTextView.setText("+" + num + "");
                                    }
                                    viewHolder.two_or_moreLinearLayout.setVisibility(View.VISIBLE);
                                    viewHolder.feedImageView.setVisibility(View.GONE);
                                    Glide.with(context)
                                            .load(strings.get(0)) // Uri of the picture
                                            .centerCrop()
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .crossFade()
                                            .override(100, 100)
                                            .into(viewHolder.imageFirstImageView);
                                    Glide.with(context)
                                            .load(strings.get(1)) // Uri of the picture
                                            .centerCrop()
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .crossFade()
                                            .override(100, 100)
                                            .into(viewHolder.imageSecImageView);

                                } else {
                                    String singleImageUrl = strings.get(0);
                                    viewHolder.two_or_moreLinearLayout.setVisibility(View.GONE);

                                    Glide.with(context)
                                            .load(singleImageUrl) // Uri of the picture
                                            .centerCrop()
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .crossFade()
                                            .override(100, 100)
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
                        viewHolder.two_or_moreLinearLayout.setVisibility(View.GONE);
                        viewHolder.linkDescriptiontextView.setVisibility(View.GONE);
                        viewHolder.CommentSectionLinearLayout.setVisibility(View.VISIBLE);
                        viewHolder.profileSectionLinearLayout.setVisibility(View.VISIBLE);
                        viewHolder.profileSectionLinearLayout.setVisibility(View.VISIBLE);
                        viewHolder.videoView.setVisibility(View.GONE);
                        viewHolder.webView.setVisibility(View.GONE);
                        viewHolder.feedImageView.setImageResource(R.drawable.default_videobg);
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
                                        String s[] = feedModel.getFeed_source().split("documents/");
                                        String s1[] = s[1].split("\\.");
                                        if (feedModel.getDoc_name().contains("\\.")) {
                                            feedModel.setDoc_name(feedModel.getDoc_name().replace("\\.", ""));
                                        }

                                        String extntion = feedModel.getFeed_source().substring(feedModel.getFeed_source().lastIndexOf(".") + 1);
                                        String filename = feedModel.getFeed_source().substring(feedModel.getFeed_source().lastIndexOf("/") + 1);
                                        Methods.downloadFile(feedModel.getFeed_source(), activity, extntion, feedModel.getDoc_name());
                                        Methods.showProgress(feedModel.getFeed_source(), activity);
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
                    viewHolder.statusTextView.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.statusTextView.setVisibility(View.GONE);

                }
                if(feedModel.getTotal_likes()!=null)
                {
                    viewHolder.noOfLikeTextView.setText(feedModel.getTotal_likes()+" Likes");
                }
                if(feedModel.getTotal_comments()!=null)
                {
                    viewHolder.noOfCommentTextView.setText(feedModel.getTotal_comments()+" Comments");
                }
                UserDetailModel userDetailModel=feedModel.getUserDetailModel_creator();
                if(userDetailModel!=null&&userDetailModel.getName()!=null)
                {
                    viewHolder.nameTextView.setText("By "+userDetailModel.getName());
                }
                if(feedModel.getCreated()!=null)
                {
                    viewHolder.timeStampTextView.setText(feedModel.getCreated());
                }
                if (feedModel.getTitleQuestion() != null && !feedModel.getTitleQuestion().equalsIgnoreCase("")) {
                    viewHolder.QuestionTextView.setText(feedModel.getTitleQuestion());
                    viewHolder.QuestionTextView.setVisibility(View.VISIBLE);
                }
                else {
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
                        Intent intent = new Intent(context, LikesDisplayActivity.class);
                        intent.putExtra("feedId", feedModel.getFeed_id());
                        context.startActivity(intent);
                    }
                });
                viewHolder.noOfLikeTextView.setOnClickListener(new View.OnClickListener() {
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
                        Intent intent = new Intent(context, CommentsActivity.class);
                        intent.putExtra("feedId", feedModel.getFeed_id());
                        ApplicationSingleton.setPost_position(position);

                        context.startActivity(intent);
                    }
                });
                TypeFaceMethods.setRegularTypeBoldFaceTextView(viewHolder.QuestionTextView, context);
                TypeFaceMethods.setRegularTypeBoldFaceTextView(viewHolder.nameTextView, context);
                TypeFaceMethods.setRegularTypeFaceForTextView(viewHolder.timeStampTextView, context);
                TypeFaceMethods.setRegularTypeFaceForTextView(viewHolder.statusTextView, context);
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
                viewHolder.noOfLikeTextView.setText(String.valueOf(payloads.get(0))+" Likes");
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
        TextView youHaveWishedTextView, comment_text;
        TextView statusTextView, nameTextView, QuestionTextView,
                timeStampTextView, announcementTextView,
                noOfLikeTextView, whisesTextView, noOfCommentTextView,
                linkTitleTextView, linkDescriptiontextView, docNameTextView, docTypeTextView,
                anniversaryTextView, announcementTypeTextView, notificationTitleTextView, viewAllTextView, moreviewTextView;
        ImageView feedImageView, linkImageView, anniverasaryLayoutImage, docImageView, announcementImage, userImage, feedlikeimg, cancelAnnouncementImageView, basicAnnouncemetImage;
        LinearLayout docTypeLayout, announcementLinearLayout, feeddeletelistingLinearLayout, CommentSectionLinearLayout, feedcommentlistingLinearLayout, feedcommentlisting, feedlikeLinearLayout, likeFeedLinearLayout, share_feedLinearLayout, normalFeedLayout, cardshoderLinearLayout;
        WebView webView;
        CircleImageView profileImageView;
        FrameLayout frameVideoFrameLayout;
        RelativeLayout profileSectionLinearLayout, basicAnnouncemetLinearLayout, sayCongratsRelativeLayout, anniversaryLinearLayout, hetrogenousAnnouncementLinearLayout;
        Button buttondownload;
        VideoView videoView;
        View basicAnnouncemet_view;
        LinearLayout moreLinearLayout, two_or_moreLinearLayout;
        ImageView imageFirstImageView, imageSecImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
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
        }
    }

}