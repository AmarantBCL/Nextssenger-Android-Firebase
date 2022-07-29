package com.example.android.nextssenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.nextssenger.adapters.MessagesAdapter;
import com.example.android.nextssenger.pojo.Message;
import com.example.android.nextssenger.pojo.User;
import com.example.android.nextssenger.viewmodel.ChatViewModel;
import com.example.android.nextssenger.viewmodel.ChatViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private static final String EXTRA_CURRENT_USER_ID = "currentUserId";
    private static final String EXTRA_OTHER_USER_ID = "otherUserId";

    private TextView textViewName;
    private View onlineStatus;
    private RecyclerView recyclerViewMessages;
    private EditText editTextTypeMessage;
    private ImageView imageViewSend;

    private MessagesAdapter adapter;
    private String currentUserId;
    private String otherUserId;

    private ChatViewModel viewModel;
    private ChatViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        currentUserId = intent.getStringExtra(EXTRA_CURRENT_USER_ID);
        otherUserId = intent.getStringExtra(EXTRA_OTHER_USER_ID);
        initViews();
        viewModelFactory = new ChatViewModelFactory(currentUserId, otherUserId);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ChatViewModel.class);
        observeViewModel();
        setClickListeners();
    }

    private void setClickListeners() {
        imageViewSend.setOnClickListener(v -> {
            Message message = new Message(editTextTypeMessage.getText().toString().trim(),
                    currentUserId, otherUserId);
            viewModel.sendMessage(message);
        });
    }

    private void observeViewModel() {
        viewModel.getMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                adapter.setMessages(messages);
            }
        });
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMsg) {
                if (errorMsg != null) {
                    Toast.makeText(ChatActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getMessageSent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean sent) {
                if (sent) {
                    editTextTypeMessage.setText("");
                }
            }
        });
        viewModel.getOtherUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                textViewName.setText(String.format("%s", user.getName()));
                int backgroundId = user.isOnline() ? R.drawable.circle_green : R.drawable.circle_red;
                Drawable drawable = ContextCompat.getDrawable(ChatActivity.this, backgroundId);
                onlineStatus.setBackground(drawable);
            }
        });
    }

    private void initViews() {
        textViewName = findViewById(R.id.tv_user_info);
        onlineStatus = findViewById(R.id.online_status);
        imageViewSend = findViewById(R.id.img_send_message);
        editTextTypeMessage = findViewById(R.id.edit_type_message);
        recyclerViewMessages = findViewById(R.id.recycler_view_messages);
        adapter = new MessagesAdapter(currentUserId);
        recyclerViewMessages.setAdapter(adapter);
    }

    public static Intent newIntent(Context context, String currentUserId, String otherUserId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        intent.putExtra(EXTRA_OTHER_USER_ID, otherUserId);
        return intent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.setUserOnline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.setUserOnline(false);
    }
}