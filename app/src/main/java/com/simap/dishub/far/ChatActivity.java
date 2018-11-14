package com.simap.dishub.far;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.simap.dishub.far.asynctas.NotifTopikAsyncTask;
import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.Notification;

import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    public static String TAG = "FirebaseUI.chat";
    private Firebase mRef;
    private Query mChatRef;
    private Intent intent;
    private int userId;
    private String mName, iduser;
    private String mTime;
    private Button mSendButton, clear;
    private EditText mMessageEdit;
    private SessionManager session;
    private HashMap<String, String> hashMap;
    private RecyclerView mMessages;
    private FirebaseRecyclerAdapter<ChatModel, ChatHolder> mRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Firebase.setAndroidContext(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSendButton = (Button) findViewById(R.id.sendButton);
        clear = (Button) findViewById(R.id.btnclear);
        mMessageEdit = (EditText) findViewById(R.id.messageEdit);

//        mRef = new Firebase("https://simap-b7729.firebaseio.com/chat");
        mRef = new Firebase("https://sipekaapill.firebaseio.com/chat");
        mChatRef = mRef.limitToLast(50);

        intent = getIntent();
        iduser = intent.getStringExtra(FieldName.ID_USER);
        Log.e("userid", iduser);
        if (iduser.equals("teknisi")){
            clear.setVisibility(View.GONE);
        }

        session = new SessionManager(getApplicationContext());
        final String mName = session.isNama();
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatModel chat = new ChatModel(mMessageEdit.getText().toString(), mName, iduser, System.currentTimeMillis(), mTime);
                mRef.push().setValue(chat, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        notif();
                        notif1();

                        if (firebaseError != null) {
                            Log.e(TAG, firebaseError.toString());
                        }
                    }
                });
                mMessageEdit.setText("");
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.removeValue();
            }
        });

        mMessages = (RecyclerView) findViewById(R.id.messagesList);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(false);

        mMessages.setHasFixedSize(false);
        mMessages.setLayoutManager(manager);
        manager.setStackFromEnd(true);

        mRecycleViewAdapter = new FirebaseRecyclerAdapter<ChatModel, ChatHolder>(ChatModel.class,
                R.layout.text_message, ChatHolder.class, mChatRef) {
            @Override
            public void populateViewHolder(ChatHolder chatView, ChatModel chat, int position) {
                chatView.setText(chat.getMessage());
                chatView.setName(chat.getName());
                chatView.setTime(chat.getFormattedTime());

                Log.e("userid", chat.getUserId() + " ;;; " + iduser);

                if(chat.getUserId().equals(iduser)) {
                    chatView.setIsSender(true);
                } else {
                    chatView.setIsSender(false);
                }
            }
        };

        mMessages.setAdapter(mRecycleViewAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (iduser.equals("teknisi")){
                    finish();
                    Intent intent = new Intent(ChatActivity.this, Teknisi1Activity.class);
                    startActivity(intent);
                } else {
                    finish();
                    Intent intent = new Intent(ChatActivity.this, Pegawai1Activity.class);
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class ChatHolder extends RecyclerView.ViewHolder {
        View mView;

        public ChatHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setIsSender(Boolean isSender) {
            FrameLayout left_arrow = (FrameLayout) mView.findViewById(R.id.left_arrow);
            FrameLayout right_arrow = (FrameLayout) mView.findViewById(R.id.right_arrow);
            RelativeLayout messageContainer = (RelativeLayout) mView.findViewById(R.id.message_container);
            LinearLayout message = (LinearLayout) mView.findViewById(R.id.message);


            if (isSender) {
                left_arrow.setVisibility(View.GONE);
                right_arrow.setVisibility(View.VISIBLE);
                messageContainer.setGravity(Gravity.RIGHT);
            } else {
                left_arrow.setVisibility(View.VISIBLE);
                right_arrow.setVisibility(View.GONE);
                messageContainer.setGravity(Gravity.LEFT);
            }
        }

        public void setName(String name) {
            TextView field = (TextView) mView.findViewById(R.id.name_text);
            field.setText(name);
        }

        public void setText(String text) {
            TextView field = (TextView) mView.findViewById(R.id.message_text);
            field.setText(text);
        }

        public void setTime(String time){
            TextView field = (TextView) mView.findViewById(R.id.time_text);
            field.setText(time);
        }
    }

    protected void notif() {
        hashMap = new HashMap<String, String>();
        hashMap.put(FieldName.TOPIC, "teknisi");
        hashMap.put(FieldName.MESSAGE, "Chat Baru");
        Log.e("hashmap", hashMap + "");

        new NotifTopikAsyncTask(this, hashMap) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
//                pDialog = new ProgressDialog(ReportAllProgresActivity.this);
//                pDialog.setMessage("Please wait...");
//                pDialog.setCancelable(false);
//                pDialog.show();
            }

            @Override
            protected void onPostExecute(Notification notification) {
                super.onPostExecute(notification);
                // Dismiss the progress dialog
//                if (pDialog.isShowing())
//                    pDialog.dismiss();

                if (notification != null) {
                    Toast.makeText(ChatActivity.this, notification.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ChatActivity.this, "Tidak dapat terhubung ke server.", Toast.LENGTH_LONG).show();
                }

                /**
                 * Updating parsed JSON data into ListView
                 * */
            }

        }.execute();
    }


    protected void notif1() {
        hashMap = new HashMap<String, String>();
        hashMap.put(FieldName.TOPIC, "admin");
        hashMap.put(FieldName.MESSAGE, "Chat Baru");
        Log.e("hashmap", hashMap + "");

        new NotifTopikAsyncTask(this, hashMap) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
//                pDialog = new ProgressDialog(ReportAllProgresActivity.this);
//                pDialog.setMessage("Please wait...");
//                pDialog.setCancelable(false);
//                pDialog.show();
            }

            @Override
            protected void onPostExecute(Notification notification) {
                super.onPostExecute(notification);
                // Dismiss the progress dialog
//                if (pDialog.isShowing())
//                    pDialog.dismiss();

                if (notification != null) {
                    Toast.makeText(ChatActivity.this, notification.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ChatActivity.this, "Tidak dapat terhubung ke server.", Toast.LENGTH_LONG).show();
                }

                /**
                 * Updating parsed JSON data into ListView
                 * */
            }

        }.execute();
    }
}
