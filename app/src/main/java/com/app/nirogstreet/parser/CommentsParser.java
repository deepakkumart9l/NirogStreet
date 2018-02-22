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
                        String user_type_comment=null,title_comment=null;
                        String commentId = null, userId = null, fname = null, lname = null, slug = null, userProfile_pic = null, message = null, createdOn = null;
                        JSONObject jsonObjectComment = jsonArray.getJSONObject(i);
                        if (jsonObjectComment.has("id") && !jsonObjectComment.isNull("id")) {
                            commentId = jsonObjectComment.getString("id");

                        }
                        if (jsonObjectComment.has("subcumment") && !jsonObjectComment.isNull("subcumment")) {

                            JSONArray subComments = jsonObjectComment.getJSONArray("subcumment");
                            for (int k = 0; k < subComments.length(); k++) {
                                JSONObject sub_commentObject = subComments.getJSONObject(k);
String User_type=null,title=null;
                                String userIdSubComment = "", fnameSubComment = "", lnameSubComment = "",subcommentId="", userProfile_picSubComment = "", slugSubComment = "", subCommentmsg = "", subCommentCreatedOn = "";
                                if (sub_commentObject.has("userdetail") && !sub_commentObject.isNull("userdetail")) {
                                    JSONObject userDetail = sub_commentObject.getJSONObject("userdetail");
                                    {
                                        if (userDetail.has("id") && !userDetail.isNull("id")) {
                                            userIdSubComment = userDetail.getString("id");

                                        }
                                        if(userDetail.has("user_type")&&!userDetail.isNull("user_type"))
                                        {
                                            User_type=userDetail.getString("user_type");
                                        }
                                        if(userDetail.has("Title")&&!userDetail.isNull("Title"))
                                        {
                                            title=userDetail.getString("Title");
                                        }
                                        if (userDetail.has("name") && !userDetail.isNull("name")) {
                                            fnameSubComment = userDetail.getString("name");

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
                                if (sub_commentObject.has("id") && !sub_commentObject.isNull("id")) {
                                    subcommentId = sub_commentObject.getString("id");
                                }
                                subComment.add(new CommentsModel(fnameSubComment, lnameSubComment, userIdSubComment, userIdSubComment, subcommentId, userProfile_picSubComment, "", subCommentCreatedOn, subCommentmsg, 0, false, null,User_type,title));

                            }
                        }
                        if (jsonObjectComment.has("total_like") && !jsonObjectComment.isNull("total_like")) {
                            totalLikes = jsonObjectComment.getInt("total_like");
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
                                    if(userDetail.has("user_type")&&!userDetail.isNull("user_type"))
                                    {
                                        user_type_comment=userDetail.getString("user_type");
                                    }
                                if(userDetail.has("Title")&&!userDetail.isNull("Title"))
                                {
                                    title_comment=userDetail.getString("Title");
                                }
                            }
                        }

                        commentsModels.add(new CommentsModel(fname, lname, slug, userId, commentId, userProfile_pic, "", createdOn, message, totalLikes, isuserLiked, subComment,user_type_comment,title_comment));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return commentsModels;
    }

}
