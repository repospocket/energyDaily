package energyDaily.example.energyDaily;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class NotificationReceiver extends BroadcastReceiver {

    private NotificationManagerCompat notificationManager;


    @Override
    public void onReceive(Context context, Intent intent) {


        sendOnChannel1(context);

        int day = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH);
        int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
        int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        String lala = Integer.toString(year) + " " + Integer.toString(month) + " " + Integer.toString(day);

        gg sa = new gg();

        if (notificationManager == null)
            notificationManager = NotificationManagerCompat.from(context);

        switch (intent.getAction()) {
            case "blue":
                sa.saveData(lala, "blue", context);
                Toast.makeText(context, "Normal energy..", Toast.LENGTH_SHORT).show();
                notificationManager.cancelAll();


                break;
            case "red":
                sa.saveData(lala, "red", context);
                Toast.makeText(context, "Low energy..", Toast.LENGTH_SHORT).show();
                notificationManager.cancelAll();

                break;
            case "green":
                sa.saveData(lala, "green", context);
                Toast.makeText(context, "High energy today..", Toast.LENGTH_SHORT).show();
                notificationManager.cancelAll();

                break;
            default:
                // code block
        }


    }


    public void sendOnChannel1(Context context) {


        Intent activityIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, activityIntent, 0);
        Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
        broadcastIntent.setAction("blue");
        PendingIntent actionIntent = PendingIntent.getBroadcast(context,
                0, broadcastIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent broadcastIntent2 = new Intent(context, NotificationReceiver.class);
        broadcastIntent2.setAction("red");

        PendingIntent actionIntent2 = PendingIntent.getBroadcast(context,
                0, broadcastIntent2, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent broadcastIntent3 = new Intent(context, NotificationReceiver.class);
        broadcastIntent3.setAction("green");
        PendingIntent actionIntent3 = PendingIntent.getBroadcast(context,
                0, broadcastIntent3, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context, "channel1")
                .setSmallIcon(R.drawable.ic_face)
                .setContentTitle("Time to check in..")
                .setContentText("How are you feeling today?\n(click on me to add a note too)")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher, "Bad", actionIntent2)
                .addAction(R.mipmap.ic_launcher, "Neutral", actionIntent)
                .addAction(R.mipmap.ic_launcher, "Good", actionIntent3)
                .setAutoCancel(true)
                .build();

        if (notificationManager == null)
            notificationManager = NotificationManagerCompat.from(context);


        notificationManager.notify(1, notification);
    }


}
