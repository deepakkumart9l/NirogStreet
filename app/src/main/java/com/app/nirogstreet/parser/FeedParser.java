package com.app.nirogstreet.parser;

import com.app.nirogstreet.model.FeedModel;
import com.app.nirogstreet.model.ShareWithModel;
import com.app.nirogstreet.model.SpecializationModel;
import com.app.nirogstreet.model.UserDetailModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Preeti on 07-11-2017.
 */
public class FeedParser {
    public static FeedModel singleFeed(JSONObject jo) {
        FeedModel feedModel = null;
        try {

            if (jo != null) {

                String community_id = "", community_name = "";
                ArrayList<String> mediaList = new ArrayList<>();
                String user_id = null, fname = null, lname = null, slug = null, profile_pic = null;
                ArrayList<String> feedSourceArrayList = new ArrayList<>();
                UserDetailModel ParentFeedDetail = null;
                String share_id = null, share_feed_id = null, share_type = null, shareUser_id = null, share_user_name = null, share_user_message = null;
                String feed_tag_name = null;
                ArrayList<ShareWithModel> shareWithModels = new ArrayList<>();
                String alubmName = null, albumDescription = null, albumLocation = null, albumCreatorUserId = null, albumCreatorfname = null, albumCreatorlname = null, albumCreaterProfile_pic = null, albumCreatorSlug = null;
                String feed_id = null, feed_from = null, group_id = null, event_id = null, parent_feed = null, titleQuestion = null, post_type = null, feed_type = null, feed_for = null, feed_source = null, refrence = null, message = null, at_place = null, link_type = null, url_title = null, url_description = null, url_image = null, at_lang = null, at_long = null, created = null, updated = null, status = null, announcment_id = null, totalLikes = null, enable_comment = null, totalComments = null, docName = null, docIcon = null, docType = null, wishes = null;
                int user_has_liked = 0, youcongratulated = 0;
                UserDetailModel userDetailModel = null;
                String feed_Tag_id = null;
                ArrayList<SpecializationModel> specializationModelsForTags = new ArrayList<>();
                String activity_detail = null;
                UserDetailModel parentFeedDetail = null;
                UserDetailModel cretedBy = null;
                if (jo.has("feed") && !jo.isNull("feed")) {
                    JSONObject jsonObject = jo.getJSONObject("feed");

                    if (jsonObject.has("id") && !jsonObject.isNull("id")) {
                        feed_id = jsonObject.getString("id");
                    }
                    if (jsonObject.has("feed_tags") && !jsonObject.isNull("feed_tags")) {
                        JSONArray jsonArrayFeed_Tags = jsonObject.getJSONArray("feed_tags");
                        for (int l = 0; l < jsonArrayFeed_Tags.length(); l++) {
                            if (jsonArrayFeed_Tags.getJSONObject(l).has("id") && !jsonArrayFeed_Tags.getJSONObject(l).isNull("id")) {
                                feed_Tag_id = jsonArrayFeed_Tags.getJSONObject(l).getString("id");

                            }

                            if (jsonArrayFeed_Tags.getJSONObject(l).has("name") && !jsonArrayFeed_Tags.getJSONObject(l).isNull("name")) {
                                feed_tag_name = jsonArrayFeed_Tags.getJSONObject(l).getString("name");

                            }
                            specializationModelsForTags.add(new SpecializationModel(feed_tag_name, feed_Tag_id, true));
                        }
                    }

                    if (jsonObject.has("activity_detail") && !jsonObject.isNull("activity_detail")) {
                        activity_detail = jsonObject.getString("activity_detail");
                    }
                    if (jsonObject.has("user_id") && !jsonObject.isNull("user_id")) {
                        String category = "", gender = "", experience = "", profile_picuser = "", createdOn = "", id = "", email = "", mobile = "", dob = "", about = "", city = "", title = "", website = "", name = "";
                        String referral_code = "";
                        String user_type = null;

                        JSONObject userJsonObject = jsonObject.getJSONObject("user_id");
                        if (userJsonObject.has("name") && !userJsonObject.isNull("name")) {
                            name = userJsonObject.getString("name");
                        }
                        if (userJsonObject.has("id") && !userJsonObject.isNull("id")) {
                            id = userJsonObject.getString("id");
                        }
                        if (userJsonObject.has("experience") && !userJsonObject.isNull("experience")) {
                            experience = userJsonObject.getString("experience");
                        }
                        if (userJsonObject.has("gender") && !userJsonObject.isNull("gender")) {
                            gender = userJsonObject.getString("gender");
                        }
                        if (userJsonObject.has("email") && !userJsonObject.isNull("email")) {
                            email = userJsonObject.getString("email");
                        }
                        if (userJsonObject.has("mobile") && !userJsonObject.isNull("mobile")) {
                            mobile = userJsonObject.getString("mobile");
                        }
                        if (userJsonObject.has("profile_pic") && !userJsonObject.isNull("profile_pic")) {
                            profile_picuser = userJsonObject.getString("profile_pic");
                        }
                        if (userJsonObject.has("category") && !userJsonObject.isNull("category")) {
                            category = userJsonObject.getString("category");
                        }
                        if (userJsonObject.has("dob") && !userJsonObject.isNull("dob")) {
                            dob = userJsonObject.getString("dob");
                        }
                        if (userJsonObject.has("website") && !userJsonObject.isNull("website")) {
                            website = userJsonObject.getString("website");
                        }
                        if (userJsonObject.has("aboutus") && !userJsonObject.isNull("aboutus")) {
                            about = userJsonObject.getString("aboutus");
                        }
                        if (userJsonObject.has("Title") && !userJsonObject.isNull("Title")) {
                            title = userJsonObject.getString("Title");
                        }
                        if (userJsonObject.has("city") && !userJsonObject.isNull("city")) {
                            city = userJsonObject.getString("city");
                        }
                        if (userJsonObject.has("referral_code") && !userJsonObject.isNull("referral_code")) {
                            referral_code = userJsonObject.getString("referral_code");
                        }
                        if (userJsonObject.has("user_type") && !userJsonObject.isNull("user_type")) {
                            user_type = userJsonObject.getString("user_type");
                        }
                        userDetailModel = new UserDetailModel(id, name, 1, email, mobile, gender, experience, profile_picuser, category, dob, website, about, title, city, null, null, null, null, null, null, null, null, null, referral_code, user_type);

                    }
                    if (jsonObject.has("community_id") && !jsonObject.isNull("community_id")) {
                        community_id = jsonObject.getString("community_id");

                    }
                    if (jsonObject.has("community detail") && !jsonObject.isNull("community detail")) {
                        JSONObject community = jsonObject.getJSONObject("community detail");

                        if (community.has("name") && !community.isNull("name")) {
                            community_name = community.getString("name");
                        }
                        if (community.has("community_id") && !community.isNull("community_id")) {
                            community_id = community.getString("community_id");

                        }
                    }
                    if (jsonObject.has("parent_feed_detail") && !jsonObject.isNull("parent_feed_detail")) {
                        String category = "", gender = "", experience = "", profile_picuser = "", createdOn = "", id = "", email = "", mobile = "", dob = "", about = "", parent_created_by = null, city = "", title = "", website = "", name = "";
                        String referral_code = "";
                        String user_type = null;
                        JSONObject userJsonObject = jsonObject.getJSONObject("parent_feed_detail");
                        if (userJsonObject.has("user_id") && !userJsonObject.isNull("user_id")) {
                            id = userJsonObject.getString("user_id");
                        }
                        if (userJsonObject.has("user_type") && !userJsonObject.isNull("user_type")) {
                            user_type = userJsonObject.getString("user_type");
                        }
                        if (userJsonObject.has("referral_code") && !userJsonObject.isNull("referral_code")) {
                            referral_code = userJsonObject.getString("referral_code");
                        }
                        if (userJsonObject.has("name") && !userJsonObject.isNull("name")) {
                            name = userJsonObject.getString("name");
                        }
                        if (userJsonObject.has("experience") && !userJsonObject.isNull("experience")) {
                            experience = userJsonObject.getString("experience");
                        }
                        if (userJsonObject.has("gender") && !userJsonObject.isNull("gender")) {
                            gender = userJsonObject.getString("gender");
                        }
                        if (userJsonObject.has("email") && !userJsonObject.isNull("email")) {
                            email = userJsonObject.getString("email");
                        }
                        if (userJsonObject.has("mobile") && !userJsonObject.isNull("mobile")) {
                            mobile = userJsonObject.getString("mobile");
                        }
                        if (userJsonObject.has("profile_pic") && !userJsonObject.isNull("profile_pic")) {
                            profile_picuser = userJsonObject.getString("profile_pic");
                        }
                        if (userJsonObject.has("category") && !userJsonObject.isNull("category")) {
                            category = userJsonObject.getString("category");
                        }
                        if (userJsonObject.has("dob") && !userJsonObject.isNull("dob")) {
                            dob = userJsonObject.getString("dob");
                        }
                        if (userJsonObject.has("website") && !userJsonObject.isNull("website")) {
                            website = userJsonObject.getString("website");
                        }
                        if (userJsonObject.has("aboutus") && !userJsonObject.isNull("aboutus")) {
                            about = userJsonObject.getString("aboutus");
                        }
                        if (userJsonObject.has("Title") && !userJsonObject.isNull("Title")) {
                            title = userJsonObject.getString("Title");
                        }
                        if (userJsonObject.has("city") && !userJsonObject.isNull("city")) {
                            city = userJsonObject.getString("city");
                        }
                        if (userJsonObject.has("created_by") && !userJsonObject.isNull("created_by")) {
                            parent_created_by = userJsonObject.getString("created_by");
                        }

                        parentFeedDetail = new UserDetailModel(id, name, 1, email, mobile, gender, experience, profile_picuser, category, dob, website, about, title, city, null, null, null, null, null, null, null, null, parent_created_by, referral_code, user_type);

                    }

                    if (jsonObject.has("sharewith") && !jsonObject.isNull("sharewith")) {
                        JSONArray jsonArray1 = jsonObject.getJSONArray("sharewith");
                        for (int l = 0; l < jsonArray1.length(); l++) {
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(l);
                            if (jsonObject1.has("id") && !jsonObject1.isNull("id")) {
                                share_id = jsonObject1.getString("id");
                            }
                            if (jsonObject1.has("message") && !jsonObject1.isNull("message")) {
                                share_user_message = jsonObject1.getString("message");
                            }
                            if (jsonObject1.has("feed_id") && !jsonObject1.isNull("feed_id")) {
                                share_feed_id = jsonObject1.getString("feed_id");
                            }
                            if (jsonObject1.has("share_type") && !jsonObject1.isNull("share_type")) {
                                share_type = jsonObject1.getString("share_type");
                            }
                            if (jsonObject1.has("share_with") && !jsonObject1.isNull("share_with")) {
                                JSONObject jsonObject2 = jsonObject1.getJSONObject("share_with");
                                if (jsonObject2.has("id") && !jsonObject2.isNull("id")) {
                                    shareUser_id = jsonObject2.getString("id");
                                }
                                if (jsonObject2.has("name") && !jsonObject2.isNull("name")) {
                                    share_user_name = jsonObject2.getString("name");
                                }
                            }
                            shareWithModels.add(new ShareWithModel(share_id, share_feed_id, shareUser_id, share_user_name, share_user_message, share_type));

                        }
                    }
                    if (jsonObject.has("parent_feed") && !jsonObject.isNull("parent_feed")) {
                        parent_feed = jsonObject.getString("parent_feed");
                    }
                    if (jsonObject.has("feed_type") && !jsonObject.isNull("feed_type")) {
                        feed_type = jsonObject.getString("feed_type");
                    }
                    if (jsonObject.has("post_type") && !jsonObject.isNull("post_type")) {
                        post_type = jsonObject.getString("post_type");
                    }
                    if (jsonObject.has("title") && !jsonObject.isNull("title")) {
                        titleQuestion = jsonObject.getString("title");
                    }
                    if (jsonObject.has("message") && !jsonObject.isNull("message")) {
                        message = jsonObject.getString("message");
                    }
                    if (jsonObject.has("refrence") && !jsonObject.isNull("refrence")) {
                        refrence = jsonObject.getString("refrence");
                    }
                    if (jsonObject.has("link_type") && !jsonObject.isNull("link_type")) {
                        link_type = jsonObject.getString("link_type");
                    }
                    if (jsonObject.has("url_title") && !jsonObject.isNull("url_title")) {
                        url_title = jsonObject.getString("url_title");
                    }
                    if (jsonObject.has("url_description") && !jsonObject.isNull("url_description")) {
                        url_description = jsonObject.getString("url_description");
                    }
                    if (jsonObject.has("url_image") && !jsonObject.isNull("url_image")) {
                        url_image = jsonObject.getString("url_image");
                    }
                    if (jsonObject.has("feed_images") && !jsonObject.isNull("feed_images")) {
                        JSONArray jsonArrayFeed_images = jsonObject.getJSONArray("feed_images");
                        for (int j = 0; j < jsonArrayFeed_images.length(); j++) {
                            feedSourceArrayList.add(jsonArrayFeed_images.getString(j));
                        }
                    }
                    if (jsonObject.has("feed_source") && !jsonObject.isNull("feed_source")) {
                        feed_source = jsonObject.getString("feed_source");
                    }
                    if (jsonObject.has("total_like") && !jsonObject.isNull("total_like")) {
                        totalLikes = jsonObject.getString("total_like");
                    }
                    if (jsonObject.has("total_comment") && !jsonObject.isNull("total_comment")) {
                        totalComments = jsonObject.getString("total_comment");
                    }
                    if (jsonObject.has("enable_comment") && !jsonObject.isNull("enable_comment")) {
                        enable_comment = jsonObject.getString("enable_comment");
                    }
                    if (jsonObject.has("status") && !jsonObject.isNull("status")) {
                        status = jsonObject.getString("status");
                    }
                    if (jsonObject.has("created") && !jsonObject.isNull("created")) {
                        created = jsonObject.getString("created");
                    }
                    if (jsonObject.has("updated") && !jsonObject.isNull("updated")) {
                        updated = jsonObject.getString("updated");
                    }
                    if (jsonObject.has("created_by") && !jsonObject.isNull("created_by")) {

                        String user_type = null;
                        String category = "", gender = "", experience = "", profile_picuser = "", createdOn = "", id = "", email = "", mobile = "", dob = "", about = "", city = "", title = "", website = "", name = "";
                        String referral_code = "";
                        JSONObject userJsonObject = jsonObject.getJSONObject("created_by");
                        if (userJsonObject.has("name") && !userJsonObject.isNull("name")) {
                            name = userJsonObject.getString("name");
                        }
                        if (userJsonObject.has("user_type") && !userJsonObject.isNull("user_type")) {
                            user_type = userJsonObject.getString("user_type");
                        }
                        if (userJsonObject.has("id") && !userJsonObject.isNull("id")) {
                            id = userJsonObject.getString("id");
                        }
                        if (userJsonObject.has("experience") && !userJsonObject.isNull("experience")) {
                            experience = userJsonObject.getString("experience");
                        }
                        if (userJsonObject.has("referral_code") && !userJsonObject.isNull("referral_code")) {
                            referral_code = userJsonObject.getString("referral_code");
                        }
                        if (userJsonObject.has("gender") && !userJsonObject.isNull("gender")) {
                            gender = userJsonObject.getString("gender");
                        }
                        if (userJsonObject.has("email") && !userJsonObject.isNull("email")) {
                            email = userJsonObject.getString("email");
                        }
                        if (userJsonObject.has("mobile") && !userJsonObject.isNull("mobile")) {
                            mobile = userJsonObject.getString("mobile");
                        }
                        if (userJsonObject.has("profile_pic") && !userJsonObject.isNull("profile_pic")) {
                            profile_picuser = userJsonObject.getString("profile_pic");
                        }
                        if (userJsonObject.has("category") && !userJsonObject.isNull("category")) {
                            category = userJsonObject.getString("category");
                        }
                        if (userJsonObject.has("dob") && !userJsonObject.isNull("dob")) {
                            dob = userJsonObject.getString("dob");
                        }
                        if (userJsonObject.has("website") && !userJsonObject.isNull("website")) {
                            website = userJsonObject.getString("website");
                        }
                        if (userJsonObject.has("aboutus") && !userJsonObject.isNull("aboutus")) {
                            about = userJsonObject.getString("aboutus");
                        }
                        if (userJsonObject.has("Title") && !userJsonObject.isNull("Title")) {
                            title = userJsonObject.getString("Title");
                        }
                        if (userJsonObject.has("city") && !userJsonObject.isNull("city")) {
                            city = userJsonObject.getString("city");
                        }
                        cretedBy = new UserDetailModel(id, name, 1, email, mobile, gender, experience, profile_picuser, category, dob, website, about, title, city, null, null, null, null, null, null, null, null, null, referral_code, user_type);


                    }
                    if (jsonObject.has("user_hasLiked") && !jsonObject.isNull("user_hasLiked")) {
                        user_has_liked = jsonObject.getInt("user_hasLiked");

                    }
                    if (jsonObject.has("docName") && !jsonObject.isNull("docName")) {
                        docName = jsonObject.getString("docName");

                    }
                    feedModel = new FeedModel(feed_id, userDetailModel, community_id, parentFeedDetail, parent_feed, feed_type, post_type, titleQuestion, message, link_type, url_title, url_description, url_image, feedSourceArrayList, enable_comment, created, updated, user_has_liked, cretedBy, feed_source, totalComments, totalLikes, status, docName, "", community_name, activity_detail, specializationModelsForTags, refrence, shareWithModels);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedModel;
    }

    public static ArrayList<FeedModel> feedParserList(JSONObject jo, int page) {
        ArrayList<FeedModel> feedModels = new ArrayList<>();
        try {
            if (jo != null) {
                if (jo.has("status") && !jo.isNull("status")) {
                    boolean statusboolean = jo.getBoolean("status");
                    if (statusboolean) {

                        if (jo.has("feeds") && !jo.isNull("feeds")) {
                            JSONArray jsonArray = jo.getJSONArray("feeds");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String community_id = "", community_name = "";
                                ArrayList<String> mediaList = new ArrayList<>();
                                String activity_detail = null;
                                String feed_Tag_id = null;
                                String share_id = null, share_feed_id = null, share_type = null, shareUser_id = null, share_user_name = null, share_user_message = null;
                                String feed_tag_name = null;
                                ArrayList<ShareWithModel> shareWithModels = new ArrayList<>();

                                ArrayList<SpecializationModel> specializationModelsForTags = new ArrayList<>();
                                String user_id = null, fname = null, lname = null, slug = null, profile_pic = null;
                                ArrayList<String> feedSourceArrayList = new ArrayList<>();
                                UserDetailModel ParentFeedDetail = null;
                                String alubmName = null, albumDescription = null, albumLocation = null, albumCreatorUserId = null, albumCreatorfname = null, albumCreatorlname = null, albumCreaterProfile_pic = null, albumCreatorSlug = null;
                                String feed_id = null, feed_from = null, group_id = null, event_id = null, parent_feed = null, titleQuestion = null, post_type = null, feed_type = null, feed_for = null, feed_source = null, refrence = null, message = null, at_place = null, link_type = null, url_title = null, url_description = null, url_image = null, at_lang = null, at_long = null, created = null, updated = null, status = null, announcment_id = null, totalLikes = null, enable_comment = null, totalComments = null, docName = null, docIcon = null, docType = null, wishes = null;
                                int user_has_liked = 0, youcongratulated = 0;
                                UserDetailModel userDetailModel = null;
                                UserDetailModel parentFeedDetail = null;
                                UserDetailModel cretedBy = null;
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (jsonObject.has("id") && !jsonObject.isNull("id")) {
                                    feed_id = jsonObject.getString("id");
                                }
                                if (jsonObject.has("feed_tags") && !jsonObject.isNull("feed_tags")) {
                                    JSONArray jsonArrayFeed_Tags = jsonObject.getJSONArray("feed_tags");
                                    for (int l = 0; l < jsonArrayFeed_Tags.length(); l++) {
                                        if (jsonArrayFeed_Tags.getJSONObject(l).has("id") && !jsonArrayFeed_Tags.getJSONObject(l).isNull("id")) {
                                            feed_Tag_id = jsonArrayFeed_Tags.getJSONObject(l).getString("id");

                                        }

                                        if (jsonArrayFeed_Tags.getJSONObject(l).has("name") && !jsonArrayFeed_Tags.getJSONObject(l).isNull("name")) {
                                            feed_tag_name = jsonArrayFeed_Tags.getJSONObject(l).getString("name");

                                        }
                                        specializationModelsForTags.add(new SpecializationModel(feed_tag_name, feed_Tag_id, true));
                                    }
                                }

                                if (jsonObject.has("activity_detail") && !jsonObject.isNull("activity_detail")) {
                                    activity_detail = jsonObject.getString("activity_detail");
                                }
                                if (jsonObject.has("user_id") && !jsonObject.isNull("user_id")) {
                                    String category = "", gender = "", experience = "", profile_picuser = "", createdOn = "", id = "", email = "", mobile = "", dob = "", about = "", city = "", title = "", website = "", name = "";
                                    String referral_code = "";
                                    String user_type = null;
                                    JSONObject userJsonObject = jsonObject.getJSONObject("user_id");
                                    if (userJsonObject.has("name") && !userJsonObject.isNull("name")) {
                                        name = userJsonObject.getString("name");
                                    }
                                    if (userJsonObject.has("referral_code") && !userJsonObject.isNull("referral_code")) {
                                        referral_code = userJsonObject.getString("referral_code");
                                    }
                                    if (userJsonObject.has("id") && !userJsonObject.isNull("id")) {
                                        id = userJsonObject.getString("id");
                                    }
                                    if (userJsonObject.has("experience") && !userJsonObject.isNull("experience")) {
                                        experience = userJsonObject.getString("experience");
                                    }
                                    if (userJsonObject.has("gender") && !userJsonObject.isNull("gender")) {
                                        gender = userJsonObject.getString("gender");
                                    }
                                    if (userJsonObject.has("email") && !userJsonObject.isNull("email")) {
                                        email = userJsonObject.getString("email");
                                    }
                                    if (userJsonObject.has("mobile") && !userJsonObject.isNull("mobile")) {
                                        mobile = userJsonObject.getString("mobile");
                                    }
                                    if (userJsonObject.has("profile_pic") && !userJsonObject.isNull("profile_pic")) {
                                        profile_picuser = userJsonObject.getString("profile_pic");
                                    }
                                    if (userJsonObject.has("category") && !userJsonObject.isNull("category")) {
                                        category = userJsonObject.getString("category");
                                    }
                                    if (userJsonObject.has("user_type") && !userJsonObject.isNull("user_type")) {
                                        user_type = userJsonObject.getString("user_type");
                                    }
                                    if (userJsonObject.has("dob") && !userJsonObject.isNull("dob")) {
                                        dob = userJsonObject.getString("dob");
                                    }
                                    if (userJsonObject.has("website") && !userJsonObject.isNull("website")) {
                                        website = userJsonObject.getString("website");
                                    }
                                    if (userJsonObject.has("aboutus") && !userJsonObject.isNull("aboutus")) {
                                        about = userJsonObject.getString("aboutus");
                                    }
                                    if (userJsonObject.has("Title") && !userJsonObject.isNull("Title")) {
                                        title = userJsonObject.getString("Title");
                                    }
                                    if (userJsonObject.has("city") && !userJsonObject.isNull("city")) {
                                        city = userJsonObject.getString("city");
                                    }
                                    userDetailModel = new UserDetailModel(id, name, 1, email, mobile, gender, experience, profile_picuser, category, dob, website, about, title, city, null, null, null, null, null, null, null, null, null, referral_code, user_type);

                                }
                                if (jsonObject.has("community_id") && !jsonObject.isNull("community_id")) {
                                    community_id = jsonObject.getString("community_id");

                                }
                                if (jsonObject.has("community detail") && !jsonObject.isNull("community detail")) {
                                    JSONObject community = jsonObject.getJSONObject("community detail");

                                    if (community.has("name") && !community.isNull("name")) {
                                        community_name = community.getString("name");
                                    }
                                    if (community.has("community_id") && !community.isNull("community_id")) {
                                        community_id = community.getString("community_id");

                                    }
                                }
                                if (jsonObject.has("parent_feed_detail") && !jsonObject.isNull("parent_feed_detail")) {
                                    String category = "", gender = "", experience = "", profile_picuser = "", createdOn = "", id = "", email = "", mobile = "", dob = "", about = "", parent_created_by = null, city = "", title = "", website = "", name = "";
                                    String referral_code = "";
                                    String user_type = null;
                                    JSONObject userJsonObject = jsonObject.getJSONObject("parent_feed_detail");
                                    if (userJsonObject.has("user_id") && !userJsonObject.isNull("user_id")) {
                                        id = userJsonObject.getString("user_id");
                                    }
                                    if (userJsonObject.has("name") && !userJsonObject.isNull("name")) {
                                        name = userJsonObject.getString("name");
                                    }
                                    if (userJsonObject.has("referral_code") && !userJsonObject.isNull("referral_code")) {
                                        referral_code = userJsonObject.getString("referral_code");
                                    }
                                    if (userJsonObject.has("experience") && !userJsonObject.isNull("experience")) {
                                        experience = userJsonObject.getString("experience");
                                    }
                                    if (userJsonObject.has("gender") && !userJsonObject.isNull("gender")) {
                                        gender = userJsonObject.getString("gender");
                                    }
                                    if (userJsonObject.has("email") && !userJsonObject.isNull("email")) {
                                        email = userJsonObject.getString("email");
                                    }
                                    if (userJsonObject.has("mobile") && !userJsonObject.isNull("mobile")) {
                                        mobile = userJsonObject.getString("mobile");
                                    }
                                    if (userJsonObject.has("profile_pic") && !userJsonObject.isNull("profile_pic")) {
                                        profile_picuser = userJsonObject.getString("profile_pic");
                                    }
                                    if (userJsonObject.has("category") && !userJsonObject.isNull("category")) {
                                        category = userJsonObject.getString("category");
                                    }
                                    if (userJsonObject.has("dob") && !userJsonObject.isNull("dob")) {
                                        dob = userJsonObject.getString("dob");
                                    }
                                    if (userJsonObject.has("website") && !userJsonObject.isNull("website")) {
                                        website = userJsonObject.getString("website");
                                    }
                                    if (userJsonObject.has("aboutus") && !userJsonObject.isNull("aboutus")) {
                                        about = userJsonObject.getString("aboutus");
                                    }
                                    if (userJsonObject.has("Title") && !userJsonObject.isNull("Title")) {
                                        title = userJsonObject.getString("Title");
                                    }
                                    if (userJsonObject.has("city") && !userJsonObject.isNull("city")) {
                                        city = userJsonObject.getString("city");
                                    }
                                    if (userJsonObject.has("created_by") && !userJsonObject.isNull("created_by")) {
                                        parent_created_by = userJsonObject.getString("created_by");
                                    }
                                    if (userJsonObject.has("user_type") && !userJsonObject.isNull("user_type")) {
                                        user_type = userJsonObject.getString("user_type");
                                    }
                                    parentFeedDetail = new UserDetailModel(id, name, 1, email, mobile, gender, experience, profile_picuser, category, dob, website, about, title, city, null, null, null, null, null, null, null, null, parent_created_by, referral_code, user_type);


                                }
                                if (jsonObject.has("sharewith") && !jsonObject.isNull("sharewith")) {
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("sharewith");
                                    for (int l = 0; l < jsonArray1.length(); l++) {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(l);
                                        if (jsonObject1.has("id") && !jsonObject1.isNull("id")) {
                                            share_id = jsonObject1.getString("id");
                                        }
                                        if (jsonObject1.has("message") && !jsonObject1.isNull("message")) {
                                            share_user_message = jsonObject1.getString("message");
                                        }
                                        if (jsonObject1.has("feed_id") && !jsonObject1.isNull("feed_id")) {
                                            share_feed_id = jsonObject1.getString("feed_id");
                                        }
                                        if (jsonObject1.has("share_type") && !jsonObject1.isNull("share_type")) {
                                            share_type = jsonObject1.getString("share_type");
                                        }
                                        if (jsonObject1.has("share_with") && !jsonObject1.isNull("share_with")) {
                                            JSONObject jsonObject2 = jsonObject1.getJSONObject("share_with");
                                            if (jsonObject2.has("id") && !jsonObject2.isNull("id")) {
                                                shareUser_id = jsonObject2.getString("id");
                                            }
                                            if (jsonObject2.has("name") && !jsonObject2.isNull("name")) {
                                                share_user_name = jsonObject2.getString("name");
                                            }
                                        }
                                        shareWithModels.add(new ShareWithModel(share_id, share_feed_id, shareUser_id, share_user_name, share_user_message, share_type));
                                    }
                                }
                                if (jsonObject.has("parent_feed") && !jsonObject.isNull("parent_feed")) {
                                    parent_feed = jsonObject.getString("parent_feed");
                                }
                                if (jsonObject.has("feed_type") && !jsonObject.isNull("feed_type")) {
                                    feed_type = jsonObject.getString("feed_type");
                                }
                                if (jsonObject.has("post_type") && !jsonObject.isNull("post_type")) {
                                    post_type = jsonObject.getString("post_type");
                                }
                                if (jsonObject.has("title") && !jsonObject.isNull("title")) {
                                    titleQuestion = jsonObject.getString("title");
                                }
                                if (jsonObject.has("message") && !jsonObject.isNull("message")) {
                                    message = jsonObject.getString("message");
                                }
                                if (jsonObject.has("refrence") && !jsonObject.isNull("refrence")) {
                                    refrence = jsonObject.getString("refrence");
                                }
                                if (jsonObject.has("link_type") && !jsonObject.isNull("link_type")) {
                                    link_type = jsonObject.getString("link_type");
                                }
                                if (jsonObject.has("url_title") && !jsonObject.isNull("url_title")) {
                                    url_title = jsonObject.getString("url_title");
                                }
                                if (jsonObject.has("url_description") && !jsonObject.isNull("url_description")) {
                                    url_description = jsonObject.getString("url_description");
                                }
                                if (jsonObject.has("url_image") && !jsonObject.isNull("url_image")) {
                                    url_image = jsonObject.getString("url_image");
                                }
                                if (jsonObject.has("feed_images") && !jsonObject.isNull("feed_images")) {
                                    JSONArray jsonArrayFeed_images = jsonObject.getJSONArray("feed_images");
                                    for (int j = 0; j < jsonArrayFeed_images.length(); j++) {
                                        feedSourceArrayList.add(jsonArrayFeed_images.getString(j));
                                    }
                                }
                                if (jsonObject.has("feed_source") && !jsonObject.isNull("feed_source")) {
                                    feed_source = jsonObject.getString("feed_source");
                                }
                                if (jsonObject.has("total_like") && !jsonObject.isNull("total_like")) {
                                    totalLikes = jsonObject.getString("total_like");
                                }
                                if (jsonObject.has("total_comment") && !jsonObject.isNull("total_comment")) {
                                    totalComments = jsonObject.getString("total_comment");
                                }
                                if (jsonObject.has("enable_comment") && !jsonObject.isNull("enable_comment")) {
                                    enable_comment = jsonObject.getString("enable_comment");
                                }
                                if (jsonObject.has("status") && !jsonObject.isNull("status")) {
                                    status = jsonObject.getString("status");
                                }
                                if (jsonObject.has("created") && !jsonObject.isNull("created")) {
                                    created = jsonObject.getString("created");
                                }
                                if (jsonObject.has("updated") && !jsonObject.isNull("updated")) {
                                    updated = jsonObject.getString("updated");
                                }
                                if (jsonObject.has("created_by") && !jsonObject.isNull("created_by")) {

                                    String category = "", gender = "", experience = "", profile_picuser = "", createdOn = "", id = "", email = "", mobile = "", dob = "", about = "", city = "", title = "", website = "", name = "";
                                    String referral_code = "";
                                    String user_type = null;
                                    JSONObject userJsonObject = jsonObject.getJSONObject("created_by");
                                    if (userJsonObject.has("name") && !userJsonObject.isNull("name")) {
                                        name = userJsonObject.getString("name");
                                    }
                                    if (userJsonObject.has("referral_code") && !userJsonObject.isNull("referral_code")) {
                                        referral_code = userJsonObject.getString("referral_code");
                                    }
                                    if (userJsonObject.has("id") && !userJsonObject.isNull("id")) {
                                        id = userJsonObject.getString("id");
                                    }
                                    if (userJsonObject.has("experience") && !userJsonObject.isNull("experience")) {
                                        experience = userJsonObject.getString("experience");
                                    }
                                    if (userJsonObject.has("gender") && !userJsonObject.isNull("gender")) {
                                        gender = userJsonObject.getString("gender");
                                    }
                                    if (userJsonObject.has("user_type") && !userJsonObject.isNull("user_type")) {
                                        user_type = userJsonObject.getString("user_type");
                                    }
                                    if (userJsonObject.has("email") && !userJsonObject.isNull("email")) {
                                        email = userJsonObject.getString("email");
                                    }
                                    if (userJsonObject.has("mobile") && !userJsonObject.isNull("mobile")) {
                                        mobile = userJsonObject.getString("mobile");
                                    }
                                    if (userJsonObject.has("profile_pic") && !userJsonObject.isNull("profile_pic")) {
                                        profile_picuser = userJsonObject.getString("profile_pic");
                                    }
                                    if (userJsonObject.has("category") && !userJsonObject.isNull("category")) {
                                        category = userJsonObject.getString("category");
                                    }
                                    if (userJsonObject.has("dob") && !userJsonObject.isNull("dob")) {
                                        dob = userJsonObject.getString("dob");
                                    }
                                    if (userJsonObject.has("website") && !userJsonObject.isNull("website")) {
                                        website = userJsonObject.getString("website");
                                    }
                                    if (userJsonObject.has("aboutus") && !userJsonObject.isNull("aboutus")) {
                                        about = userJsonObject.getString("aboutus");
                                    }
                                    if (userJsonObject.has("Title") && !userJsonObject.isNull("Title")) {
                                        title = userJsonObject.getString("Title");
                                    }
                                    if (userJsonObject.has("city") && !userJsonObject.isNull("city")) {
                                        city = userJsonObject.getString("city");
                                    }
                                    if (userJsonObject.has("referral_code") && !userJsonObject.isNull("referral_code")) {
                                        referral_code = userJsonObject.getString("referral_code");
                                    }
                                    cretedBy = new UserDetailModel(id, name, 1, email, mobile, gender, experience, profile_picuser, category, dob, website, about, title, city, null, null, null, null, null, null, null, null, null, referral_code, user_type);


                                }
                                if (jsonObject.has("user_hasLiked") && !jsonObject.isNull("user_hasLiked")) {
                                    user_has_liked = jsonObject.getInt("user_hasLiked");

                                }
                                if (jsonObject.has("docName") && !jsonObject.isNull("docName")) {
                                    docName = jsonObject.getString("docName");

                                }
                                feedModels.add(new FeedModel(feed_id, userDetailModel, community_id, parentFeedDetail, parent_feed, feed_type, post_type, titleQuestion, message, link_type, url_title, url_description, url_image, feedSourceArrayList, enable_comment, created, updated, user_has_liked, cretedBy, feed_source, totalComments, totalLikes, status, docName, "", community_name, activity_detail, specializationModelsForTags, refrence, shareWithModels));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedModels;
    }

    public static ArrayList<FeedModel> singlePostFeed(JSONObject jsonObject) {
        ArrayList<FeedModel> feedModels = new ArrayList<>();
        FeedModel feedModel = null;
        try {
            if (jsonObject != null) {

                String community_id = "", community_name = "";
                ArrayList<String> mediaList = new ArrayList<>();
                String user_id = null, fname = null, lname = null, slug = null, profile_pic = null;
                ArrayList<String> feedSourceArrayList = new ArrayList<>();
                UserDetailModel ParentFeedDetail = null;
                String activity_detail = null;
                String share_type = null;
                String share_id = null, share_feed_id = null, shareUser_id = null, share_user_name = null, share_user_message = null;
                ArrayList<ShareWithModel> shareWithModels = new ArrayList<>();
                String alubmName = null, albumDescription = null, albumLocation = null, albumCreatorUserId = null, albumCreatorfname = null, albumCreatorlname = null, albumCreaterProfile_pic = null, albumCreatorSlug = null;
                String feed_id = null, feed_from = null, group_id = null, event_id = null, parent_feed = null, titleQuestion = null, post_type = null, feed_type = null, feed_for = null, feed_source = null, refrence = null, message = null, at_place = null, link_type = null, url_title = null, url_description = null, url_image = null, at_lang = null, at_long = null, created = null, updated = null, status = null, announcment_id = null, totalLikes = null, enable_comment = null, totalComments = null, docName = null, docIcon = null, docType = null, wishes = null;
                int user_has_liked = 0, youcongratulated = 0;
                UserDetailModel userDetailModel = null;
                UserDetailModel parentFeedDetail = null;
                String feed_Tag_id = null;
                String feed_tag_name = null;
                ArrayList<SpecializationModel> specializationModelsForTags = new ArrayList<>();
                UserDetailModel cretedBy = null;
                if (jsonObject.has("id") && !jsonObject.isNull("id")) {
                    feed_id = jsonObject.getString("id");
                }
                if (jsonObject.has("feed_tags") && !jsonObject.isNull("feed_tags")) {
                    JSONArray jsonArrayFeed_Tags = jsonObject.getJSONArray("feed_tags");
                    for (int i = 0; i < jsonArrayFeed_Tags.length(); i++) {
                        if (jsonArrayFeed_Tags.getJSONObject(i).has("id") && !jsonArrayFeed_Tags.getJSONObject(i).isNull("id")) {
                            feed_Tag_id = jsonArrayFeed_Tags.getJSONObject(i).getString("id");

                        }

                        if (jsonArrayFeed_Tags.getJSONObject(i).has("name") && !jsonArrayFeed_Tags.getJSONObject(i).isNull("name")) {
                            feed_tag_name = jsonArrayFeed_Tags.getJSONObject(i).getString("name");

                        }
                        specializationModelsForTags.add(new SpecializationModel(feed_tag_name, feed_Tag_id, true));
                    }
                }
                if (jsonObject.has("activity_detail") && !jsonObject.isNull("activity_detail")) {
                    activity_detail = jsonObject.getString("activity_detail");
                }

                if (jsonObject.has("user_id") && !jsonObject.isNull("user_id")) {
                    String category = "", gender = "", experience = "", profile_picuser = "", createdOn = "", id = "", email = "", mobile = "", dob = "", about = "", city = "", title = "", website = "", name = "";
                    String referral_code = "";
                    String user_type = null;
                    JSONObject userJsonObject = jsonObject.getJSONObject("user_id");
                    if (userJsonObject.has("name") && !userJsonObject.isNull("name")) {
                        name = userJsonObject.getString("name");
                    }
                    if (userJsonObject.has("user_type") && !userJsonObject.isNull("user_type")) {
                        user_type = userJsonObject.getString("user_type");
                    }

                    if (userJsonObject.has("referral_code") && !userJsonObject.isNull("referral_code")) {
                        referral_code = userJsonObject.getString("referral_code");
                    }
                    if (userJsonObject.has("id") && !userJsonObject.isNull("id")) {
                        id = userJsonObject.getString("id");
                    }
                    if (userJsonObject.has("experience") && !userJsonObject.isNull("experience")) {
                        experience = userJsonObject.getString("experience");
                    }
                    if (userJsonObject.has("gender") && !userJsonObject.isNull("gender")) {
                        gender = userJsonObject.getString("gender");
                    }
                    if (userJsonObject.has("email") && !userJsonObject.isNull("email")) {
                        email = userJsonObject.getString("email");
                    }
                    if (userJsonObject.has("mobile") && !userJsonObject.isNull("mobile")) {
                        mobile = userJsonObject.getString("mobile");
                    }
                    if (userJsonObject.has("profile_pic") && !userJsonObject.isNull("profile_pic")) {
                        profile_picuser = userJsonObject.getString("profile_pic");
                    }
                    if (userJsonObject.has("category") && !userJsonObject.isNull("category")) {
                        category = userJsonObject.getString("category");
                    }
                    if (userJsonObject.has("dob") && !userJsonObject.isNull("dob")) {
                        dob = userJsonObject.getString("dob");
                    }
                    if (userJsonObject.has("website") && !userJsonObject.isNull("website")) {
                        website = userJsonObject.getString("website");
                    }
                    if (userJsonObject.has("aboutus") && !userJsonObject.isNull("aboutus")) {
                        about = userJsonObject.getString("aboutus");
                    }
                    if (userJsonObject.has("Title") && !userJsonObject.isNull("Title")) {
                        title = userJsonObject.getString("Title");
                    }
                    if (userJsonObject.has("city") && !userJsonObject.isNull("city")) {
                        city = userJsonObject.getString("city");
                    }
                    userDetailModel = new UserDetailModel(id, name, 1, email, mobile, gender, experience, profile_picuser, category, dob, website, about, title, city, null, null, null, null, null, null, null, null, null, referral_code, user_type);

                }
                if (jsonObject.has("community_id") && !jsonObject.isNull("community_id")) {
                    community_id = jsonObject.getString("community_id");

                }
                if (jsonObject.has("community detail") && !jsonObject.isNull("community detail")) {
                    JSONObject community = jsonObject.getJSONObject("community detail");

                    if (community.has("name") && !community.isNull("name")) {
                        community_name = community.getString("name");
                    }
                    if (community.has("community_id") && !community.isNull("community_id")) {
                        community_id = community.getString("community_id");

                    }
                }
                if (jsonObject.has("parent_feed_detail") && !jsonObject.isNull("parent_feed_detail")) {
                    String category = "", gender = "", experience = "", profile_picuser = "", createdOn = "", id = "", parent_created_by = null, email = "", mobile = "", dob = "", about = "", city = "", title = "", website = "", name = "";
                    String referral_code = "";
                    String user_type = null;
                    JSONObject userJsonObject = jsonObject.getJSONObject("parent_feed_detail");
                    if (userJsonObject.has("user_id") && !userJsonObject.isNull("user_id")) {
                        id = userJsonObject.getString("user_id");
                    }
                    if (userJsonObject.has("user_type") && !userJsonObject.isNull("user_type")) {
                        user_type = userJsonObject.getString("user_type");
                    }
                    if (userJsonObject.has("referral_code") && !userJsonObject.isNull("referral_code")) {
                        referral_code = userJsonObject.getString("referral_code");
                    }
                    if (userJsonObject.has("name") && !userJsonObject.isNull("name")) {
                        name = userJsonObject.getString("name");
                    }
                    if (userJsonObject.has("experience") && !userJsonObject.isNull("experience")) {
                        experience = userJsonObject.getString("experience");
                    }
                    if (userJsonObject.has("gender") && !userJsonObject.isNull("gender")) {
                        gender = userJsonObject.getString("gender");
                    }
                    if (userJsonObject.has("email") && !userJsonObject.isNull("email")) {
                        email = userJsonObject.getString("email");
                    }
                    if (userJsonObject.has("mobile") && !userJsonObject.isNull("mobile")) {
                        mobile = userJsonObject.getString("mobile");
                    }
                    if (userJsonObject.has("profile_pic") && !userJsonObject.isNull("profile_pic")) {
                        profile_picuser = userJsonObject.getString("profile_pic");
                    }
                    if (userJsonObject.has("category") && !userJsonObject.isNull("category")) {
                        category = userJsonObject.getString("category");
                    }
                    if (userJsonObject.has("dob") && !userJsonObject.isNull("dob")) {
                        dob = userJsonObject.getString("dob");
                    }
                    if (userJsonObject.has("website") && !userJsonObject.isNull("website")) {
                        website = userJsonObject.getString("website");
                    }
                    if (userJsonObject.has("aboutus") && !userJsonObject.isNull("aboutus")) {
                        about = userJsonObject.getString("aboutus");
                    }
                    if (userJsonObject.has("Title") && !userJsonObject.isNull("Title")) {
                        title = userJsonObject.getString("Title");
                    }
                    if (userJsonObject.has("city") && !userJsonObject.isNull("city")) {
                        city = userJsonObject.getString("city");
                    }
                    if (userJsonObject.has("created_by") && !userJsonObject.isNull("created_by")) {
                        parent_created_by = userJsonObject.getString("created_by");
                    }
                    parentFeedDetail = new UserDetailModel(id, name, 1, email, mobile, gender, experience, profile_picuser, category, dob, website, about, title, city, null, null, null, null, null, null, null, null, parent_created_by, referral_code, user_type);


                }
                if (jsonObject.has("sharewith") && !jsonObject.isNull("sharewith")) {
                    JSONArray jsonArray1 = jsonObject.getJSONArray("sharewith");
                    for (int l = 0; l < jsonArray1.length(); l++) {
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(l);
                        if (jsonObject1.has("id") && !jsonObject1.isNull("id")) {
                            share_id = jsonObject1.getString("id");
                        }
                        if (jsonObject1.has("message") && !jsonObject1.isNull("message")) {
                            share_user_message = jsonObject1.getString("message");
                        }
                        if (jsonObject1.has("feed_id") && !jsonObject1.isNull("feed_id")) {
                            share_feed_id = jsonObject1.getString("feed_id");
                        }
                        if (jsonObject1.has("share_type") && !jsonObject1.isNull("share_type")) {
                            share_type = jsonObject1.getString("share_type");
                        }
                        if (jsonObject1.has("share_with") && !jsonObject1.isNull("share_with")) {
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("share_with");
                            if (jsonObject2.has("id") && !jsonObject2.isNull("id")) {
                                shareUser_id = jsonObject2.getString("id");
                            }
                            if (jsonObject2.has("name") && !jsonObject2.isNull("name")) {
                                share_user_name = jsonObject2.getString("name");
                            }
                        }
                        shareWithModels.add(new ShareWithModel(share_id, share_feed_id, shareUser_id, share_user_name, share_user_message, share_type));

                    }
                }

                if (jsonObject.has("parent_feed") && !jsonObject.isNull("parent_feed")) {
                    parent_feed = jsonObject.getString("parent_feed");
                }
                if (jsonObject.has("feed_type") && !jsonObject.isNull("feed_type")) {
                    feed_type = jsonObject.getString("feed_type");
                }
                if (jsonObject.has("post_type") && !jsonObject.isNull("post_type")) {
                    post_type = jsonObject.getString("post_type");
                }
                if (jsonObject.has("title") && !jsonObject.isNull("title")) {
                    titleQuestion = jsonObject.getString("title");
                }
                if (jsonObject.has("message") && !jsonObject.isNull("message")) {
                    message = jsonObject.getString("message");
                }
                if (jsonObject.has("refrence") && !jsonObject.isNull("refrence")) {
                    refrence = jsonObject.getString("refrence");
                }
                if (jsonObject.has("link_type") && !jsonObject.isNull("link_type")) {
                    link_type = jsonObject.getString("link_type");
                }
                if (jsonObject.has("url_title") && !jsonObject.isNull("url_title")) {
                    url_title = jsonObject.getString("url_title");
                }
                if (jsonObject.has("url_description") && !jsonObject.isNull("url_description")) {
                    url_description = jsonObject.getString("url_description");
                }
                if (jsonObject.has("url_image") && !jsonObject.isNull("url_image")) {
                    url_image = jsonObject.getString("url_image");
                }
                if (jsonObject.has("feed_images") && !jsonObject.isNull("feed_images")) {
                    JSONArray jsonArrayFeed_images = jsonObject.getJSONArray("feed_images");
                    for (int j = 0; j < jsonArrayFeed_images.length(); j++) {
                        feedSourceArrayList.add(jsonArrayFeed_images.getString(j));
                    }
                }
                if (jsonObject.has("feed_source") && !jsonObject.isNull("feed_source")) {
                    feed_source = jsonObject.getString("feed_source");
                }
                if (jsonObject.has("total_like") && !jsonObject.isNull("total_like")) {
                    totalLikes = jsonObject.getString("total_like");
                }
                if (jsonObject.has("total_comment") && !jsonObject.isNull("total_comment")) {
                    totalComments = jsonObject.getString("total_comment");
                }
                if (jsonObject.has("enable_comment") && !jsonObject.isNull("enable_comment")) {
                    enable_comment = jsonObject.getString("enable_comment");
                }
                if (jsonObject.has("status") && !jsonObject.isNull("status")) {
                    status = jsonObject.getString("status");
                }
                if (jsonObject.has("created") && !jsonObject.isNull("created")) {
                    created = jsonObject.getString("created");
                }
                if (jsonObject.has("updated") && !jsonObject.isNull("updated")) {
                    updated = jsonObject.getString("updated");
                }
                if (jsonObject.has("created_by") && !jsonObject.isNull("created_by")) {

                    String category = "", gender = "", experience = "", profile_picuser = "", createdOn = "", id = "", email = "", mobile = "", dob = "", about = "", city = "", title = "", website = "", name = "";
                    String referral_code = "";
                    String user_type = null;
                    JSONObject userJsonObject = jsonObject.getJSONObject("created_by");
                    if (userJsonObject.has("name") && !userJsonObject.isNull("name")) {
                        name = userJsonObject.getString("name");
                    }
                    if (userJsonObject.has("id") && !userJsonObject.isNull("id")) {
                        id = userJsonObject.getString("id");
                    }
                    if (userJsonObject.has("experience") && !userJsonObject.isNull("experience")) {
                        experience = userJsonObject.getString("experience");
                    }
                    if (userJsonObject.has("gender") && !userJsonObject.isNull("gender")) {
                        gender = userJsonObject.getString("gender");
                    }
                    if (userJsonObject.has("email") && !userJsonObject.isNull("email")) {
                        email = userJsonObject.getString("email");
                    }
                    if (userJsonObject.has("mobile") && !userJsonObject.isNull("mobile")) {
                        mobile = userJsonObject.getString("mobile");
                    }
                    if (userJsonObject.has("profile_pic") && !userJsonObject.isNull("profile_pic")) {
                        profile_picuser = userJsonObject.getString("profile_pic");
                    }
                    if (userJsonObject.has("category") && !userJsonObject.isNull("category")) {
                        category = userJsonObject.getString("category");
                    }
                    if (userJsonObject.has("dob") && !userJsonObject.isNull("dob")) {
                        dob = userJsonObject.getString("dob");
                    }
                    if (userJsonObject.has("website") && !userJsonObject.isNull("website")) {
                        website = userJsonObject.getString("website");
                    }
                    if (userJsonObject.has("aboutus") && !userJsonObject.isNull("aboutus")) {
                        about = userJsonObject.getString("aboutus");
                    }
                    if (userJsonObject.has("Title") && !userJsonObject.isNull("Title")) {
                        title = userJsonObject.getString("Title");
                    }
                    if (userJsonObject.has("city") && !userJsonObject.isNull("city")) {
                        city = userJsonObject.getString("city");
                    }
                    if (userJsonObject.has("user_type") && !userJsonObject.isNull("user_type")) {
                        user_type = userJsonObject.getString("user_type");
                    }
                    if (userJsonObject.has("referral_code") && !userJsonObject.isNull("referral_code")) {
                        referral_code = userJsonObject.getString("referral_code");
                    }
                    cretedBy = new UserDetailModel(id, name, 1, email, mobile, gender, experience, profile_picuser, category, dob, website, about, title, city, null, null, null, null, null, null, null, null, null, referral_code, user_type);


                }
                if (jsonObject.has("user_hasLiked") && !jsonObject.isNull("user_hasLiked")) {
                    user_has_liked = jsonObject.getInt("user_hasLiked");

                }
                if (jsonObject.has("docName") && !jsonObject.isNull("docName")) {
                    docName = jsonObject.getString("docName");

                }

                feedModels.add(new FeedModel(feed_id, userDetailModel, community_id, parentFeedDetail, parent_feed, feed_type, post_type, titleQuestion, message, link_type, url_title, url_description, url_image, feedSourceArrayList, enable_comment, created, updated, user_has_liked, cretedBy, feed_source, totalComments, totalLikes, status, docName, "", community_name, activity_detail, specializationModelsForTags, refrence, shareWithModels));


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedModels;
    }
}
