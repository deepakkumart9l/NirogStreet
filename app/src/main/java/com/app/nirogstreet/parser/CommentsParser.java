package com.app.nirogstreet.parser;

import com.app.nirogstreet.model.CommentsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Preeti on 07-11-2017.
 */
public class CommentsParser {
    public static ArrayList<CommentsModel> commentsParser(JSONObject jsonObject) {
        ArrayList<CommentsModel> commentsModels = new ArrayList<>();
        try {
            if (jsonObject != null) {
                if (jsonObject.has("comments") && !jsonObject.isNull("comments")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("comments");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        int totalLikes = 0;
                        boolean isuserLiked = false;
                        ArrayList<CommentsModel> subComment = new ArrayList<>();
                        String commentId = null, userId = null, fname = null, lname = null, slug = null, userProfile_pic = null, message = null, createdOn = null;
                        JSONObject jsonObjectComment = jsonArray.getJSONObject(i);
                        if (jsonObjectComment.has("id") && !jsonObjectComment.isNull("id")) {
                            commentId = jsonObjectComment.getString("id");

                        }
                        if (jsonObjectComment.has("subcumment") && !jsonObjectComment.isNull("subcumment")) {

                            JSONArray subComments = jsonObjectComment.getJSONArray("subcumment");
                            for (int k = 0; k < subComments.length(); k++) {
                                JSONObject sub_commentObject = subComments.getJSONObject(k);
                                String userIdSubComment = "", fnameSubComment = "", lnameSubComment = "", userProfile_picSubComment = "", slugSubComment = "", subCommentmsg = "", subCommentCreatedOn = "";
                                if (sub_commentObject.has("userdetail") && !sub_commentObject.isNull("userdetail")) {
                                    JSONObject userDetail = sub_commentObject.getJSONObject("userdetail");
                                    {
                                        if (userDetail.has("id") && !userDetail.isNull("id")) {
                                            userIdSubComment = userDetail.getString("id");

                                        }
                                        if (userDetail.has("fname") && !userDetail.isNull("fname")) {
                                            fnameSubComment = userDetail.getString("fname");

                                        }
                                        if (userDetail.has("lname") && !userDetail.isNull("lname")) {
                                            lnameSubComment = userDetail.getString("lname");

                                        }
                                        if (userDetail.has("profile_pic") && !userDetail.isNull("profile_pic")) {
                                            userProfile_picSubComment = userDetail.getString("profile_pic");
                                        }
                                        if (userDetail.has("slug") && !userDetail.isNull("slug")) {
                                            slugSubComment = userDetail.getString("slug");
                                        }
                                    }
                                }
                                if (sub_commentObject.has("message") && !sub_commentObject.isNull("message")) {
                                    subCommentmsg = sub_commentObject.getString("message");

                                }
                                if (sub_commentObject.has("created") && !sub_commentObject.isNull("created")) {
                                    subCommentCreatedOn = sub_commentObject.getString("created");
                                }
                                subComment.add(new CommentsModel(fnameSubComment, lnameSubComment, userIdSubComment, userIdSubComment, "", userProfile_picSubComment, "", subCommentCreatedOn, subCommentmsg, 0, false, null));

                            }
                        }
                        if (jsonObjectComment.has("totalLike") && !jsonObjectComment.isNull("totalLike")) {
                            totalLikes = jsonObjectComment.getInt("totalLike");
                        }
                        if (jsonObjectComment.has("user_hasLiked") && !jsonObjectComment.isNull("user_hasLiked")) {
                            int userLike = jsonObjectComment.getInt("user_hasLiked");
                            if (userLike == 1) {
                                isuserLiked = true;
                            } else {
                                isuserLiked = false;

                            }
                        }
                        if (jsonObjectComment.has("message") && !jsonObjectComment.isNull("message")) {
                            message = jsonObjectComment.getString("message");

                        }
                        if (jsonObjectComment.has("created") && !jsonObjectComment.isNull("created")) {
                            createdOn = jsonObjectComment.getString("created");
                        }

                        if (jsonObjectComment.has("userdetail") && !jsonObjectComment.isNull("userdetail")) {
                            JSONObject userDetail = jsonObjectComment.getJSONObject("userdetail");
                            {
                                if (userDetail.has("id") && !userDetail.isNull("id")) {
                                    userId = userDetail.getString("id");
                                }
                                if (userDetail.has("name") && !userDetail.isNull("name")) {
                                    fname = userDetail.getString("name");

                                }
                                 if (userDetail.has("profile_pic") && !userDetail.isNull("profile_pic")) {
                                    userProfile_pic = userDetail.getString("profile_pic");
                                }
                                if (userDetail.has("slug") && !userDetail.isNull("slug")) {
                                    slug = userDetail.getString("slug");
                                }
                            }
                        }

                        commentsModels.add(new CommentsModel(fname, lname, slug, userId, commentId, userProfile_pic, "", createdOn, message, totalLikes, isuserLiked, subComment));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return commentsModels;
    }

}
