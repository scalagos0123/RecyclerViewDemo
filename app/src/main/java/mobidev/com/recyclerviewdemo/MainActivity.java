package mobidev.com.recyclerviewdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView r;
    private SampleAdapter adapter;
    private RecyclerView.OnItemTouchListener s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        ArrayList<SampleData> dataCollection = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            SampleData myData = new SampleData("Sample title #" + i, getResources().getString(R.string.lorem_ipsum));
            dataCollection.add(myData);
        }

        r = (RecyclerView) findViewById(R.id.my_recycler_view);

        adapter = new SampleAdapter(dataCollection);
        r.setAdapter(adapter);
        r.setLayoutManager(layoutManager);
    }

    public void addItem(View view) {
        adapter.addItem(new SampleData("Added item", "Sample content"), 0);
        r.scrollToPosition(0);
    }

    private void makeNotification(String selectedCard) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_star_black_24dp);
        builder.setContentTitle("Sample notification");
        builder.setContentText(selectedCard + " selected");
        builder.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0));
        builder.setSound(RingtoneManager.getActualDefaultRingtoneUri(MainActivity.this, RingtoneManager.TYPE_NOTIFICATION));
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setVibrate(new long[] {0, 250, 250, 250});
        builder.setLights(Color.GREEN, 0, 1000);

        NotificationManager notifService = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifService.notify(0, builder.build());
    }

    private class SampleData {
        String title;
        String content;

        public SampleData(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    private class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ViewHolder> {

        ArrayList<SampleData> dataCollection;

        public SampleAdapter (ArrayList<SampleData> dataCollection) {
            this.dataCollection = dataCollection;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public TextView content;
            public SampleData itemSelected;

            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println(itemSelected.getTitle());
                        System.out.println(itemSelected.getContent());

                        makeNotification(itemSelected.getTitle());
                    }
                });
            }
        }

        public SampleData getItem(int position) {
            return dataCollection.get(position);
        }

        @Override
        public SampleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater l = getLayoutInflater();
            View view = l.inflate(R.layout.adapter_layout, parent, false);

            ViewHolder vHolder = new ViewHolder(view);
            vHolder.title = (TextView) view.findViewById(R.id.title);
            vHolder.content = (TextView) view.findViewById(R.id.content);

            return vHolder;
        }

        @Override
        public void onBindViewHolder(SampleAdapter.ViewHolder holder, int position) {
            holder.title.setText(dataCollection.get(position).getTitle());
            holder.content.setText(dataCollection.get(position).getContent());

            holder.itemSelected = getItem(position);
        }

        @Override
        public int getItemCount() {
            return dataCollection.size();
        }

        public void addItem(SampleData dataToBeAdded, int position) {
            dataCollection.add(position, dataToBeAdded);
            notifyItemInserted(position);
        }

    }
}
