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

import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.AppointmentActivity;
import com.app.nirogstreet.activites.CommunitiesDetails;
import com.app.nirogstreet.activites.GroupNotificationListing;
import com.app.nirogstreet.activites.InviteNotificationListing;
import com.app.nirogstreet.activites.PostDetailActivity;
import com.app.nirogstreet.uttil.SesstionManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;


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
        String msg = "", url = "", postId = "", groupId = "", forumId = "", eventId = "", appointment_id = "", notification_type = "";
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
                                if (msgObject.has("community_id") && !msgObject.isNull("community_id")) {
                                    groupId = msgObject.getString("community_id");
                                }
                                if (msgObject.has("forum_id") && !msgObject.isNull("forum_id")) {
                                    forumId = msgObject.getString("forum_id");
                                }
                                if (msgObject.has("appointment_id") && !msgObject.isNull("appointment_id")) {
                                    appointment_id = msgObject.getString("appointment_id");
                                }
                                if (msgObject.has("notification_type") && !msgObject.isNull("notification_type")) {
                                    notification_type = msgObject.getString("notification_type");
                                }
                                if (msgObject.has("message") && !msgObject.isNull("message")) {
                                    msg = msgObject.getString("message");
                                    sendNotification(msg, url, forumId, eventId, groupId, postId, appointment_id, notification_type);

                                }


                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Toast.makeText(ctx, "Key: "+key+" Value: "+value, Toast.LENGTH_LONG).show();
                // sendNotification(value);
            } else {
                myVeryOwnIterator.next();
            }
        }
    }

    private void sendNotification(String messageBody, String url, String forum, String event, String group, String post, String appointment_id, String notification_type) {
        Intent intent = null;
        SesstionManager sessionManager = new SesstionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String authToken = user.get(SesstionManager.AUTH_TOKEN);
        String userId = user.get(SesstionManager.USER_ID);
        if (!group.equalsIgnoreCase("")) {
            intent = new Intent(this, CommunitiesDetails.class);
            intent.putExtra("userId", userId);
            intent.putExtra("openMain",true);

            intent.putExtra("groupId", group);
        } else if (!post.equalsIgnoreCase("")) {
            intent = new Intent(this, PostDetailActivity.class);
            intent.putExtra("openMain",true);

            intent.putExtra("feedId", post);
        } else if (notification_type.equalsIgnoreCase("9")) {
            intent = new Intent(this, GroupNotificationListing.class);
            intent.putExtra("openMain",true);

        } else if (notification_type.equalsIgnoreCase("10")) {
            intent = new Intent(this, InviteNotificationListing.class);
            intent.putExtra("openMain",true);

        } else {
            intent = new Intent(this, AppointmentActivity.class);
            intent.putExtra("openMain",true);

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
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.white_noti)
                    .setContentTitle("NirogStreet")
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
                    .setSmallIcon(R.drawable.color_noti)
                    .setContentTitle("NirogStreet")
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        }
    }

}
