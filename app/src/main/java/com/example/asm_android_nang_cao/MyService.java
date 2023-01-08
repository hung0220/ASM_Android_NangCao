package com.example.asm_android_nang_cao;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyService extends Service {



    FirebaseDatabase db =FirebaseDatabase.getInstance("https://asm-android-a58d4-default-rtdb.asia-southeast1.firebasedatabase.app/");;
    DatabaseReference node = db.getReference("SanPham");
    int i = 0;
    NotificationManagerCompat notificationManager;
    public static String NOTIFICATION_CHANNEL = "Nguyen huu hung";
    public MyService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this.getApplicationContext(), "Bật Service", Toast.LENGTH_SHORT).show();

        notificationManager = NotificationManagerCompat.from(MyService.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Thông báo";
            String description = "Mô tả thông báo";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        node.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable  String previousChildName) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this,
                        NOTIFICATION_CHANNEL)
                        .setSmallIcon(R.drawable.thongbao)
                        .setContentTitle("Thông Tin")
                        .setContentText("Có sản Phẩm mới được thêm")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                notificationManager.notify(33, builder.build());
            }

            @Override
            public void onChildChanged(@NonNull  DataSnapshot snapshot, @Nullable  String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull  DataSnapshot snapshot, @Nullable  String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this.getApplicationContext(), "Tắt Service", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}