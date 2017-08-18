package com.app.nirogstreet.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by Preeti on 07-02-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    int badgeCount = 1;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // remoteMessage.getData()
        //Map<String, String> s=  remoteMessage.getData();

        Map<String, String> bundle = remoteMessage.getData();
        remoteMessage.getNotification();
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        String message = "";
        //  Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Iterator myVeryOwnIterator = bundle.keySet().iterator();
        String msg = "", url = "", postId = "", groupId = "", forumId = "", eventId = "";
        while (myVeryOwnIterator.hasNext()) {
            String key = (String) myVeryOwnIterator.next();
            if (key.equalsIgnoreCase("default")) {
                String value = (String) bundle.get("default");
                try {
                    JSONObject jsonObject = new JSONObject(value);
                    if (jsonObject != null) {
                        if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                            if (jsonObject1.has("message") && !jsonObject1.isNull("message")) {
                                JSONObject msgObject = jsonObject1.getJSONObject("message");
                                if (msgObject.has("Url") && !msgObject.isNull("Url")) {
                                    url = msgObject.getString("Url");
                                }
                                if (msgObject.has("total") && !msgObject.isNull("total")) {
                                    badgeCount = msgObject.getInt("total");
                                }
                                if (msgObject.has("post_id") && !msgObject.isNull("post_id")) {
                                    postId = msgObject.getString("post_id");
                                }
                                if (msgObject.has("event_id") && !msgObject.isNull("event_id")) {
                                    eventId = msgObject.getString("event_id");
                                }
                                if (msgObject.has("group_id") && !msgObject.isNull("group_id")) {
                                    groupId = msgObject.getString("group_id");
                                }
                                if (msgObject.has("forum_id") && !msgObject.isNull("forum_id")) {
                                    forumId = msgObject.getString("forum_id");
                                }
                                if (msgObject.has("message") && !msgObject.isNull("message")) {
                                    msg = msgObject.getString("message");
                                   // sendNotification(msg, url, forumId, eventId, groupId, postId);

                                }

                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Toast.makeText(ctx, "Key: "+key+" Value: "+value, Toast.LENGTH_LONG).show();
                //   sendNotification(value);
            } else {
                myVeryOwnIterator.next();
            }
        }
    }

/*
    private void sendNotification(String messageBody, String url, String forum, String event, String group, String post) {
        Intent intent = null;
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String authToken = user.get(SessionManager.AUTH_TOKEN);
        String userId = user.get(SessionManager.USER_ID);
        if (!forum.equalsIgnoreCase("")) {

            intent = new Intent(this, ForumDetailActivity.class);
            intent.putExtra("forumId", forum);
        } else if (!event.equalsIgnoreCase("")) {

            intent = new Intent(this, EventDetailActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("eventID", event);
        } else if (!group.equalsIgnoreCase("")) {
            intent = new Intent(this, GroupDetailActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("groupId", group);
        } else {
            intent = new Intent(this, PostDetailActivity.class);
            intent.putExtra("feedId", post);
        }
        Bundle bundle = new Bundle();
        bundle.putString("notificationUrl", url);
        bundle.putBoolean("openTab", true);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try {
            ShortcutBadger.setBadge(getApplicationContext(), badgeCount); //for 1.1.4+
            ShortcutBadger.with(getApplicationContext()).count(badgeCount); //for 1.1.3

        } catch (ShortcutBadgeException e) {
            e.printStackTrace();
        }*/
/**//*

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.white_notification)
                    .setContentTitle("Empwin")
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
            // notificationManager.notify(1,notificationBuilder.build());
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.blue_notification)
                    .setContentTitle("Empwin")
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        }
    }
*/

}
