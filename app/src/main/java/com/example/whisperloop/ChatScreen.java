package com.example.whisperloop;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatScreen extends AppCompatActivity {
    String reciverimg;
    String reciverUid;
    String reciverName;
    String SenderUID;
    CircleImageView profile;
    TextView reciverNName;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public static String senderImage;
    public static String receiverImage;
    CardView sendButton;
    EditText textMessage;
    String senderRoom, receiverRoom;
    RecyclerView recyclerView;
    ArrayList<Message> messagesArrayList;
    MessageAdapter messageAdapter;
    DatabaseReference chatreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        reciverName = getIntent().getStringExtra("nameeee");
        reciverimg = getIntent().getStringExtra("reciverImg");
        reciverUid = getIntent().getStringExtra("uid");

        messagesArrayList = new ArrayList<>();

        sendButton = findViewById(R.id.sendButtonId1);
        textMessage = findViewById(R.id.textMessage1);
        reciverNName = findViewById(R.id.senderNameId1);
        profile = findViewById(R.id.senderProfilePictureId1);
        recyclerView = findViewById(R.id.recyclerView1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(ChatScreen.this, messagesArrayList);
        recyclerView.setAdapter(messageAdapter);

        Picasso.get().load(reciverimg).into(profile);
        reciverNName.setText("" + reciverName);

        SenderUID = firebaseAuth.getUid();

        senderRoom = SenderUID + reciverUid;
        receiverRoom = reciverUid + SenderUID;

        chatreference = database.getReference().child("chats").child(senderRoom).child("messages");


        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message messages = dataSnapshot.getValue(Message.class);
                    messagesArrayList.add(messages);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendButton.setOnClickListener(view -> {
            String message = textMessage.getText().toString();
            if (message.isEmpty()) {
                Toast.makeText(ChatScreen.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
                return;
            }
            textMessage.setText("");
            Date date = new Date();
            Message messageNew = new Message(message, SenderUID, date.getTime());

            database = FirebaseDatabase.getInstance();
            database.getReference().child("chats")
                    .child(senderRoom)
                    .child("messages")
                    .push().setValue(messageNew).addOnCompleteListener(task -> database.getReference().child("chats")
                            .child(receiverRoom)
                            .child("messages")
                            .push().setValue(messageNew).addOnCompleteListener(task1 -> {
                                Toast.makeText(ChatScreen.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                            }));
        });

    }
}