package com.asosapp.phone.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asosapp.phone.R;
import com.asosapp.phone.activity.FeedbackActivity;
import com.asosapp.phone.bean.ServiceInfo;
import com.asosapp.phone.utils.HandleResponseCode;
import com.asosapp.phone.utils.SortConvList;
import com.asosapp.phone.utils.TimeFormat;
import com.asosapp.phone.view.CircleImageView;
import com.asosapp.phone.view.ToastView;

import java.util.Collections;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

public class ConversationListAdapter extends BaseAdapter{

    List<Conversation> mDatas;
    private Activity mContext;
    private int mDensityDpi;

    public ConversationListAdapter(Activity context, List<Conversation> data, int densityDpi) {
        this.mContext = context;
        this.mDatas = data;
        this.mDensityDpi = densityDpi;
    }

    /**
     * 收到消息后将会话置顶
     *
     * @param conv 要置顶的会话
     */
    public void setToTop(Conversation conv) {
        for (Conversation conversation : mDatas) {
            if (conv.getId().equals(conversation.getId())) {
                mDatas.remove(conversation);
                mDatas.add(0, conv);
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
                return;
            }
        }
        //如果是新的会话
        mDatas.add(0, conv);
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void sortConvList() {
        SortConvList sortConvList = new SortConvList();
        Collections.sort(mDatas, sortConvList);
        notifyDataSetChanged();
    }

    public void addNewConversation(Conversation conv) {
        mDatas.add(0, conv);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    @Override
    public Conversation getItem(int position) {
        if (mDatas == null) {
            return null;
        }
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Conversation convItem = mDatas.get(position);
//        ToastView.toast(mContext, position + "");
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.conversation_list_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.headIcon = (CircleImageView) convertView
                    .findViewById(R.id.msg_item_head_icon);
            viewHolder.convName = (TextView) convertView
                    .findViewById(R.id.conv_item_name);
            viewHolder.msg_item_ll= (LinearLayout) convertView.findViewById(R.id.msg_item_ll);
            if (mDensityDpi <= 160) {
                viewHolder.convName.setEms(6);
            }else if (mDensityDpi <= 240) {
                viewHolder.convName.setEms(8);
            }else {
                viewHolder.convName.setEms(10);
            }
            viewHolder.content = (TextView) convertView
                    .findViewById(R.id.msg_item_content);
            viewHolder.datetime = (TextView) convertView
                    .findViewById(R.id.msg_item_date);
            viewHolder.newMsgNumber = (TextView) convertView
                    .findViewById(R.id.new_msg_number);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        Message lastMsg = convItem.getLatestMessage();
        if (lastMsg != null) {
            TimeFormat timeFormat = new TimeFormat(mContext, lastMsg.getCreateTime());
            viewHolder.datetime.setText(timeFormat.getTime());
            // 按照最后一条消息的消息类型进行处理
            switch (lastMsg.getContentType()) {
                case image:
                    viewHolder.content.setText(mContext.getString(R.string.type_picture));
                    break;
                case voice:
                    viewHolder.content.setText(mContext.getString(R.string.type_voice));
                    break;
                case location:
                    viewHolder.content.setText(mContext.getString(R.string.type_location));
                    break;
                case eventNotification:
                    viewHolder.content.setText(mContext.getString(R.string.group_notification));
                    break;
                case custom:
                    CustomContent content = (CustomContent)lastMsg.getContent();
                    Boolean isBlackListHint = content.getBooleanValue("blackList");
                    if (isBlackListHint != null && isBlackListHint) {
                        viewHolder.content.setText(mContext.getString(R.string.server_803008));
                    }else {
                        viewHolder.content.setText(mContext.getString(R.string.type_custom));
                    }
                    break;
                default:
                    viewHolder.content.setText(((TextContent) lastMsg.getContent()).getText());
            }
        }else {
            TimeFormat timeFormat = new TimeFormat(mContext, convItem.getLastMsgDate());
            viewHolder.datetime.setText(timeFormat.getTime());
            viewHolder.content.setText("");
        }
        // 如果是单聊
        if (convItem.getType().equals(ConversationType.single)) {

                viewHolder.convName.setText( ((UserInfo) mDatas.get(position).getTargetInfo()).getNickname());


            UserInfo userInfo = (UserInfo) convItem.getTargetInfo();
            if (userInfo != null && !TextUtils.isEmpty(userInfo.getAvatar())) {
                userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                    @Override
                    public void gotResult(int status, String desc, Bitmap bitmap) {
                        if (status == 0) {
                            viewHolder.headIcon.setImageBitmap(bitmap);
                        }else {
                            viewHolder.headIcon.setImageResource(R.mipmap.head_icon);
                            HandleResponseCode.onHandle(mContext, status, false);
                        }
                    }
                });
            }else {
                viewHolder.headIcon.setImageResource(R.mipmap.head_icon);
            }
        }
        // 群聊
        else {
//            viewHolder.headIcon.setImageResource(R.drawable.group);
//            viewHolder.convName.setText(convItem.getTitle());
//            Log.d("ConversationListAdapter", "Conversation title: " + convItem.getTitle());
        }

        // TODO 更新Message的数量,
        if (convItem.getUnReadMsgCnt() > 0) {
            viewHolder.newMsgNumber.setVisibility(View.VISIBLE);
            if (convItem.getUnReadMsgCnt() < 100) {
                viewHolder.newMsgNumber.setText(String.valueOf(convItem.getUnReadMsgCnt()));
            }
            else {
                viewHolder.newMsgNumber.setText("99");
            }
        } else {
            viewHolder.newMsgNumber.setVisibility(View.GONE);
        }
        //点击进入聊天页面
        viewHolder.msg_item_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.newMsgNumber.setVisibility(View.GONE);
                String targetId = ((UserInfo) mDatas.get(position).getTargetInfo()).getUserName();
                Intent intent=new Intent(mContext, FeedbackActivity.class);
                intent.putExtra("myID", JMessageClient.getMyInfo().getUserName());
                intent.putExtra("service", targetId);
                intent.putExtra("nameID","1");
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }


    private class ViewHolder {
        CircleImageView headIcon;
        TextView convName;
        TextView content;
        TextView datetime;
        TextView newMsgNumber;
        LinearLayout msg_item_ll;

    }



}
