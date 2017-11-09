package com.app.nirogstreet.parser;

import com.app.nirogstreet.model.FeedModel;
import com.app.nirogstreet.model.UserDetailModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Preeti on 07-11-2017.
 */
public class FeedParser {

    public static ArrayList<FeedModel> feedParserList(JSONObject jo, int page) {
        ArrayList<FeedModel> feedModels = new ArrayList<>();
        try {
            if (jo != null) {
                if (jo.has("status") && !jo.isNull("status")) {
                    boolean statusboolean = jo.getBoolean("status");
                    if (statusboolean) {
                        if (page == 1)

                            if (jo.has("feeds") && !jo.isNull("feeds")) {
                                JSONArray jsonArray = jo.getJSONArray("feeds");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String community_id = "";
                                    ArrayList<String> mediaList = new ArrayList<>();
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
                                    if (jsonObject.has("user_id") && !jsonObject.isNull("user_id")) {
                                        String category = "", gender = "", experience = "", profile_picuser = "", createdOn = "", id = "", email = "", mobile = "", dob = "", about = "", city = "", title = "", website = "", name = "";

                                        JSONObject userJsonObject = jsonObject.getJSONObject("user_id");
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
                                        userDetailModel = new UserDetailModel(id, name, email, mobile, gender, experience, profile_pic, category, dob, website, about, title, city, null, null, null, null, null, null, null, null);

                                    }
                                    if (jsonObject.has("community_id") && !jsonObject.isNull("community_id")) {
                                        community_id = jsonObject.getString("community_id");

                                    }
                                    if (jsonObject.has("parent_feed_detail") && !jsonObject.isNull("parent_feed_detail")) {
                                        if (jsonObject.getJSONObject("parent_feed_detail").has("user_id") && !jsonObject.getJSONObject("parent_feed_detail").isNull("user_id")) {
                                            String category = "", gender = "", experience = "", profile_picuser = "", createdOn = "", id = "", email = "", mobile = "", dob = "", about = "", city = "", title = "", website = "", name = "";

                                            JSONObject userJsonObject = jsonObject.getJSONObject("user_id");
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
                                            parentFeedDetail = new UserDetailModel(id, name, email, mobile, gender, experience, profile_pic, category, dob, website, about, title, city, null, null, null, null, null, null, null, null);

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

                                        if (jsonObject.getJSONObject("created_by").has("user_id") && !jsonObject.getJSONObject("created_by").isNull("user_id")) {
                                            String category = "", gender = "", experience = "", profile_picuser = "", createdOn = "", id = "", email = "", mobile = "", dob = "", about = "", city = "", title = "", website = "", name = "";

                                            JSONObject userJsonObject = jsonObject.getJSONObject("user_id");
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
                                            cretedBy = new UserDetailModel(id, name, email, mobile, gender, experience, profile_pic, category, dob, website, about, title, city, null, null, null, null, null, null, null, null);

                                        }
                                    }
                                    if (jsonObject.has("user_hasLiked") && !jsonObject.isNull("user_hasLiked")) {
                                        user_has_liked = jsonObject.getInt("user_hasLiked");

                                    }

                                    feedModels.add(new FeedModel(feed_id, userDetailModel, community_id, parentFeedDetail, parent_feed, feed_type, post_type, titleQuestion, message, link_type, url_title, url_description, url_image, feedSourceArrayList, enable_comment, created, updated, user_has_liked, cretedBy,feed_source,totalComments,totalLikes,status,"",""));
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

}