package com.example.techsprint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView messageText;
        private TextView senderEmail;
        private TextView timestamp;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            senderEmail = itemView.findViewById(R.id.senderEmail);
            timestamp = itemView.findViewById(R.id.timestamp);
        }

        public void bind(Message message) {
            messageText.setText(message.getText());
            senderEmail.setText(message.getSenderEmail());

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.getDefault());
            timestamp.setText(sdf.format(message.getTimestamp()));
        }
    }
}
