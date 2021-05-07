package com.example.schoolhub.ui.inbox;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.provider.FontRequest;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.Conversation;

import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.ui_components.chats.CometChatConversationList;
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity;
import com.cometchat.pro.uikit.ui_components.users.user_list.CometChatUserList;
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.cometchat.pro.uikit.ui_resources.utils.item_clickListener.OnItemClickListener;
import com.example.schoolhub.R;

import static android.content.ContentValues.TAG;

public class InboxFragment extends Fragment {
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_inbox, container, false);
//        //again initialize idk why
//        AppSettings appSettings=new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(region).build();
//
//        CometChat.init(getContext(), appID,appSettings, new CometChat.CallbackListener<String>() {
//            @Override
//            public void onSuccess(String successMessage) {
//                //UIKitSettings.setAuthKey(authKey);
//                CometChat.setSource("ui-kit","android","java");
//                Log.d(TAG, "Initialization completed successfully");
//            }
//
//            @Override
//            public void onError(CometChatException e) {
//                Log.d(TAG, "Initialization failed with exception: " + e.getMessage());
//            }
//        });

        //load chats
        loadFragment(new CometChatConversationList());

        //set stuff
        CometChatUserList.setItemClickListener(new OnItemClickListener<User>() {
            @Override
            public void OnItemClick(User var, int position) {
                userIntent((User)var);
            }
        });
        CometChatConversationList.setItemClickListener(new OnItemClickListener<Conversation>() {
            @Override
            public void OnItemClick(Conversation conversation, int position) {
                User user = ((User) conversation.getConversationWith());
                userIntent(user);
            }
        });

        return root;
    }
    //chat fragment loading here
    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();

        }
    }
    //idk some user calls
    public void userIntent(User user) {
        Intent intent = new Intent(getContext(), CometChatMessageListActivity.class);
        intent.putExtra(UIKitConstants.IntentStrings.UID, user.getUid());
        intent.putExtra(UIKitConstants.IntentStrings.AVATAR, user.getAvatar());
        intent.putExtra(UIKitConstants.IntentStrings.STATUS, user.getStatus());
        intent.putExtra(UIKitConstants.IntentStrings.NAME, user.getName());
        intent.putExtra(UIKitConstants.IntentStrings.TYPE, CometChatConstants.RECEIVER_TYPE_USER);
        startActivity(intent);
    }
}
