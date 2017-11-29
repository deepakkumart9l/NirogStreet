package com.app.nirogstreet.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.CommentsActivity;
import com.app.nirogstreet.activites.Dr_Profile;
import com.app.nirogstreet.activites.LikesDisplayActivity;
import com.app.nirogstreet.activites.MainActivity;
import com.app.nirogstreet.activites.PostingActivity;
import com.app.nirogstreet.model.FeedModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Preeti on 31-10-2017.
 */

public class MoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    ArrayList<FeedModel> feedModels;
    Context context;

    public MoreAdapter(ArrayList<FeedModel> feedModels, Context context) {
        this.context = context;
        this.feedModels = feedModels;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case TYPE_HEADER:
                UserDetailModel userDetailModel = ApplicationSingleton.getUserDetailModel();

                HeaderView myViewHolder = (HeaderView) holder;
                myViewHolder.profileRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, Dr_Profile.class);
                        context.startActivity(intent);
                    }
                });
                // Glide.with(context).load(askQuestionImages).into(myViewHolder.circleImageView);
                TypeFaceMethods.setRegularTypeBoldFaceTextView(myViewHolder.nameTv, context);
                TypeFaceMethods.setRegularTypeFaceForTextView(myViewHolder.view_detail, context);

                TypeFaceMethods.setRegularTypeFaceForTextView(myViewHolder.emailTv, context);
                TypeFaceMethods.setRegularTypeFaceForTextView(myViewHolder.phoneTv, context);
                TypeFaceMethods.setRegularTypeFaceForTextView(myViewHolder.QualificationTv, context);

                TypeFaceMethods.setRegularTypeFaceForTextView(myViewHolder.
                        WebTv, context);
                TypeFaceMethods.setRegularTypeFaceForTextView(myViewHolder.yearOfBirthTv, context);

                TypeFaceMethods.setRegularTypeFaceForTextView(myViewHolder.yearOfExperienceTv, context);
                TypeFaceMethods.setRegularTypeBoldFaceTextView(myViewHolder.myActivitiesTextView, context);
                TypeFaceMethods.setRegularTypeBoldFaceTextView(myViewHolder.aboutTextView, context);
                try {
                    SesstionManager sesstionManager=new SesstionManager(context);
                    String name=sesstionManager.getUserDetails().get(SesstionManager.KEY_FNAME)+" "+sesstionManager.getUserDetails().get(SesstionManager.KEY_LNAME);
                    if(name!=null)
                    myViewHolder.nameTv.setText(name);
                    if(userDetailModel.getEmail()!=null)
                    myViewHolder.emailTv.setText(userDetailModel.getEmail());
                    if(userDetailModel.getMobile()!=null)
                    myViewHolder.phoneTv.setText(userDetailModel.getMobile());
                    if(userDetailModel.getDob()!=null)
                    myViewHolder.yearOfBirthTv.setText(userDetailModel.getDob());
                    if(userDetailModel.getExperience()!=null)
                    myViewHolder.yearOfExperienceTv.setText(userDetailModel.getExperience()+" years experince");
                    if (userDetailModel != null && userDetailModel.getSpecializationModels() != null) {
                        myViewHolder.QualificationTv.setText(getSelectedNameCsv(userDetailModel));
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case TYPE_ITEM:
                MyViewHolder viewHolder = (MyViewHolder) holder;
                viewHolder.sectionTextView.setVisibility(View.VISIBLE);
                final FeedModel feedModel = feedModels.get(position);
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
                TypeFaceMethods.setRegularTypeFaceForTextView(viewHolder.sectionTextView, context);
                TypeFaceMethods.setRegularTypeBoldFaceTextView(viewHolder.QuestionTextView, context);
                TypeFaceMethods.setRegularTypeBoldFaceTextView(viewHolder.nameTextView, context);
                TypeFaceMethods.setRegularTypeFaceForTextView(viewHolder.timeStampTextView, context);
                TypeFaceMethods.setRegularTypeFaceForTextView(viewHolder.statusTextView, context);
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_layout, parent, false);
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
        ImageView editInfo;
        TextView QualificationTv, yearOfBirthTv, yearOfExperienceTv, WebTv, emailTv, phoneTv, view_detail, nameTv, myActivitiesTextView;
        TextView postAn, aboutTextView;
        RelativeLayout profileRelativeLayout;

        public HeaderView(View itemView) {
            super(itemView);
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
            circleImageView = (CircleImageView) itemView.findViewById(R.id.pro);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView playicon;
        TextView youHaveWishedTextView, comment_text;
        TextView statusTextView, nameTextView, QuestionTextView,
                timeStampTextView, announcementTextView,
                noOfLikeTextView, whisesTextView, noOfCommentTextView,
                linkTitleTextView, linkDescriptiontextView, docNameTextView, docTypeTextView,
                anniversaryTextView, announcementTypeTextView, notificationTitleTextView, viewAllTextView, moreviewTextView, sectionTextView;
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
            sectionTextView = (TextView) itemView.findViewById(R.id.section);
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
}
