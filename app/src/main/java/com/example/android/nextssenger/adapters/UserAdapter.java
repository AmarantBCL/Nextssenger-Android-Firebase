package com.example.android.nextssenger.adapters;

import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.nextssenger.R;
import com.example.android.nextssenger.pojo.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> users = new ArrayList<>();
    private OnUserClickListener listener;

    public interface OnUserClickListener {
        void onClick(User user);
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public void setOnUserClickListener(OnUserClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        String userInfo = String.format("%s, %s", user.getName(), user.getAge());
        holder.textViewUser.setText(userInfo);
        int backgroundId = user.isOnline() ? R.drawable.circle_green : R.drawable.circle_red;
        Drawable drawable = ContextCompat.getDrawable(holder.itemView.getContext(), backgroundId);
        holder.onlineStatus.setBackground(drawable);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewUser;
        private final View onlineStatus;

        public UserViewHolder(@NonNull View view) {
            super(view);
            textViewUser = view.findViewById(R.id.tv_user);
            onlineStatus = view.findViewById(R.id.circle);
        }
    }
}
