package com.asosapp.phone.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.asosapp.phone.R;
import com.asosapp.phone.bean.FeedbackEntity;


public class MegViewAdapter extends BaseAdapter {


	private static final String TAG = FeedbackEntity.class.getSimpleName();
	private List<FeedbackEntity> data;
	private Context context;
	private LayoutInflater mInflater;
	private Activity activity;

	public MegViewAdapter(List<FeedbackEntity> data, Context context,Activity activity) {
		this.data = data;
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.activity = activity;
	}
	//ListView视图的内容由IMsgViewType决定
	public static interface IMsgViewType
	{
		//对方发来的信息
		int IMVT_COM_MSG = 0;
		//自己发出的信息
		int IMVT_TO_MSG = 1;
	}

	// 获取ListView的项个数
	public int getCount() {
		return data.size();
	}

	// 获取项
	public Object getItem(int position) {
		return data.get(position);
	}

	// 获取项的ID
	public long getItemId(int position) {
		return position;
	}

	//获取项的类型
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		FeedbackEntity entity = data.get(position);

		if (entity.getMsgType())
		{
			return IMsgViewType.IMVT_COM_MSG;
		}else{
			return IMsgViewType.IMVT_TO_MSG;
		}

	}

	//获取项的类型数
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		FeedbackEntity entity = data.get(position);
		boolean isComMsg = entity.getMsgType();
		ViewHolder viewHolder = null;
		if(convertView ==null){
			if(isComMsg){
				convertView =mInflater.inflate(R.layout.feedback_receive,null);
			}else{
				convertView=mInflater.inflate(R.layout.feedback_send, null);
			}
			viewHolder=new ViewHolder();
			viewHolder.tvSendTime=(TextView)convertView.findViewById(R.id.tv_sendtime);
			viewHolder.tvContent=(TextView)convertView.findViewById(R.id.tv_chatcontent);
			viewHolder.tvUserName=(TextView)convertView.findViewById(R.id.tv_username);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		if(!(entity.getName().equals("客服"))){
			entity.setName("我");
		}
		viewHolder.tvSendTime.setText(entity.getDate());
		viewHolder.tvContent.setText(entity.getText());
		viewHolder.tvUserName.setText(entity.getName());
		//监听ListView 隐藏软键盘
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(activity.findViewById(R.id.et_sendmessage).getWindowToken(), 0);
			}
		});
		return convertView;
	}

	// 通过ViewHolder显示项的内容
	static class ViewHolder {
		public TextView tvSendTime;
		public TextView tvUserName;
		public TextView tvContent;
		public boolean isComMsg = true;
	}

}
