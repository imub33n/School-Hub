package com.example.schoolhub.ui.inbox;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.ConversationsRequest;
import com.cometchat.pro.core.UsersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.ui_components.chats.CometChatConversationList;
import com.cometchat.pro.uikit.ui_components.shared.cometchatConversations.CometChatConversations;
import com.cometchat.pro.uikit.ui_resources.utils.item_clickListener.OnItemClickListener;
import com.example.schoolhub.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.content.ContentValues.TAG;

public class InboxFragment extends Fragment {
    //List<Conversation> conversations;
    FloatingActionButton floatingActionButton;
    private UsersRequest usersRequest = null;
    private int limit = 30;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inbox, container, false);
        CometChatConversations cometChatConversations = root.findViewById(R.id.cometchatConversations);

        floatingActionButton=root.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //Retrieve List of Users
        usersRequest = new UsersRequest.UsersRequestBuilder()
                .setLimit(15)
//                .friendsOnly(true)
                .build();
        usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
            @Override
            public void onSuccess(List <User> list) {
                Log.d(TAG, "User list received: " + list.size());
            }
            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "User list fetching failed with exception: " + e.getMessage());
            }
        });

        ConversationsRequest conversationsRequest = new ConversationsRequest.ConversationsRequestBuilder().setLimit(20).build();
        conversationsRequest.fetchNext(new CometChat.CallbackListener<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conver) {
                //conversations=conver;
                //cometChatConversations.setConversationList(conver);
                
            }

            @Override
            public void onError(CometChatException e) {
                // Hanlde failure
            }
        });
//
//
//        // list: fetched using the ConversationsRequestBuilder
        cometChatConversations.setItemClickListener(new OnItemClickListener<Conversation>() {
            @Override
            public void OnItemClick(Conversation var, int position) {

            }

            @Override
            public void OnItemLongClick(Conversation var, int position) {
                super.OnItemLongClick(var, position);
            }
        });

        CometChatConversationList.setItemClickListener(new OnItemClickListener<Conversation>() {
            @Override
            public void OnItemClick(Conversation var, int position) {

            }

            @Override
            public void OnItemLongClick(Conversation var, int position) {
                super.OnItemLongClick(var, position);
            }
        });
        return root;
    }
}
