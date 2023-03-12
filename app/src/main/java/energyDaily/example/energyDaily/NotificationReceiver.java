package energyDaily.example.energyDaily;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;

public class NotificationReceiver extends BroadcastReceiver {

    private NotificationManagerCompat notificationManager;


    public void saveData(String day, String co, Context context) {
        SharedPreferences sharedPreferences1 = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        String json1 = sharedPreferences1.getString("task list", null);
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        HashMap<String, String> datacolo = new HashMap<>();

        if (json1 != null && !json1.isEmpty()) {
            datacolo = new Gson().fromJson(json1, type);
        }

        datacolo.put(day, co);

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(datacolo);
        Log.d("SaveData", json);
        editor.putString("task list", json);
        editor.apply();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        sendOnChannel1(context);

        int day = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH);
        int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
        int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        String fulldate = year + " " + month + " " + day;

        if (notificationManager == null)
            notificationManager = NotificationManagerCompat.from(context);

        switch (intent.getAction()) {
            case "blue":
                saveData(fulldate, "blue", context);
                Toast.makeText(context, "Normal energy..", Toast.LENGTH_SHORT).show();
                notificationManager.cancelAll();

                break;
            case "red":
                saveData(fulldate, "red", context);
                Toast.makeText(context, "Low energy..", Toast.LENGTH_SHORT).show();
                notificationManager.cancelAll();

                break;
            case "green":
                saveData(fulldate, "green", context);
                Toast.makeText(context, "High energy..", Toast.LENGTH_SHORT).show();
                notificationManager.cancelAll();

                break;
            default:
                // code block
        }


    }


    public void sendOnChannel1(Context context) {

        Intent activityIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, activityIntent, PendingIntent.FLAG_IMMUTABLE);
        Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
        broadcastIntent.setAction("blue");
        PendingIntent actionIntent = PendingIntent.getBroadcast(context,
                0, broadcastIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent broadcastIntent2 = new Intent(context, NotificationReceiver.class);
        broadcastIntent2.setAction("red");

        PendingIntent actionIntent2 = PendingIntent.getBroadcast(context,
                0, broadcastIntent2, PendingIntent.FLAG_IMMUTABLE);

        Intent broadcastIntent3 = new Intent(context, NotificationReceiver.class);
        broadcastIntent3.setAction("green");
        PendingIntent actionIntent3 = PendingIntent.getBroadcast(context,
                0, broadcastIntent3, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(context, "channel1")
                .setSmallIcon(R.drawable.ic_face)
                .setContentTitle("Daily Energy Check-In")
                .setContentText("How are you feeling today?")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher, "Low", actionIntent2)
                .addAction(R.mipmap.ic_launcher, "Normal", actionIntent)
                .addAction(R.mipmap.ic_launcher, "Good", actionIntent3)
                .setAutoCancel(true)
                .build();

        if (notificationManager == null)
            notificationManager = NotificationManagerCompat.from(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS);
        }

        notificationManager.notify(1, notification);

    }
}
