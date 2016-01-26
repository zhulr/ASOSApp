package com.asosapp.phone.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.asosapp.phone.R;
import com.asosapp.phone.adapter.MegViewAdapter;
import com.asosapp.phone.bean.FeedbackEntity;


public class FeedbackActivity extends Activity implements OnClickListener {

    private Button mBtnSend;
    private Button mBtnBack;
    private EditText mEditTextContent;
    //	private static final String FEEDBACK_URL = "??��URL???";
    // ???????????????
    private MegViewAdapter mAdapter;
    private ListView mListView;
    SQLiteOpenHelper openHelper;
    private String userId;
    private SharedPreferences sp;
    // ?????????
    private List<FeedbackEntity> mDataArrays = new ArrayList<FeedbackEntity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        //????????
        sp = this.getSharedPreferences("login_Info",
                Context.MODE_WORLD_READABLE);
        userId = sp.getString("USERID", "");
        //????activity???????????????
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView();
        //????????????????
//		String dbName=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Chat.db";
//		try{
//			File file=new File(dbName);
//			if(!file.exists()){
//				InputStream fis=getAssets().open("database/Chat.db");
//				FileOutputStream fos=new FileOutputStream(dbName,false);
//				byte[] buff=new byte[1024];
//				int len=0;
//				while((len=fis.read(buff))>0){
//					fos.write(buff, 0,len);
//				}
//				fos.close();
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		openHelper=new SQLiteOpenHelper(this,dbName,null,1) {
//			@Override
//			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//				// TODO Auto-generated method stub
//			}
//			@Override
//			public void onCreate(SQLiteDatabase db) {
//				// TODO Auto-generated method stub
//			}
//		};
//		receive();
    }

    // ????????
    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        this.mListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub

            }
        });
        mBtnBack = (Button) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
    }

    List<String> rev_list = new ArrayList<String>();
    List<String> send_list = new ArrayList<String>();

    //????
    private void receive() {
        //??????????,???????��????????,??????????????????
        String msg = "????????KingPeng????????????????????????????????!";
        SQLiteDatabase db = openHelper.getReadableDatabase();
        String sql = "select chatUser,chatDate,chatContext from  chat";
        Cursor c = db.rawQuery(sql, null);
        if (!c.moveToNext()) {
            db = openHelper.getReadableDatabase();
            String sql1 = "insert into chat(chatUser,chatDate,chatContext) values(?,?,?)";
            System.out.println(getDate());
            db.execSQL(sql1, new Object[]{"???", getDate(), msg});
        }
    }

    @Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    //????????????????
    private void send() {
        String sendMsg = mEditTextContent.getText().toString();
        if (!sendMsg.equals("")) {
            FeedbackEntity entity = new FeedbackEntity();
            entity.setDate(getDate());
            entity.setName("??");
            entity.setMsgType(false);
            entity.setText(sendMsg);
            mDataArrays.add(entity);
            mAdapter = new MegViewAdapter(mDataArrays, this, FeedbackActivity.this);
            mAdapter.notifyDataSetChanged();
            mListView.setAdapter(mAdapter);
            mListView.setSelection(mListView.getCount() - 1);
            send_list.add(sendMsg);
//			sendkMessage(entity);
            //???????????��????????????
//			SQLiteDatabase db=openHelper.getReadableDatabase();
//            String sql = "insert into chat(chatUser,chatDate,chatContext) values(?,?,?)";
//			db.execSQL(sql, new Object[]{userId,getDate(),sendMsg});
            System.out.println(getDate());
            mEditTextContent.setText("");
        }
    }

    //???????��???????????listView?????????????????????
    public void chatRecords() {
        String user = "";
        String date = "";
        String context = "";
        //????????��????
        SQLiteDatabase db = openHelper.getReadableDatabase();
        String sql = "select chatUser,chatDate,chatContext from chat where chatUser=? or chatUser=?";
        Cursor c = db.rawQuery(sql, new String[]{userId, "???"});
        while (c.moveToNext()) {
            user = c.getString(0);
            date = c.getString(1);
            context = c.getString(2);
            Log.v("ceshi", user + date + context);
            FeedbackEntity entity = new FeedbackEntity();
            entity.setName(user);

            if (user.equals("???")) {
                entity.setMsgType(true);
            } else {
                entity.setMsgType(false);
                System.out.println("????????");
            }
            entity.setDate(date);
            entity.setText(context);
            mDataArrays.add(entity);
            mAdapter = new MegViewAdapter(mDataArrays, this, FeedbackActivity.this);
            mAdapter.notifyDataSetChanged();
            mListView.setAdapter(mAdapter);
            mListView.setSelection(mListView.getCount() - 1);
        }
    }

    //?????????????????????
//	public void sendkMessage(final FeedbackEntity entity){
//		final String url=FEEDBACK_URL;
//		new AsyncTask<Object,Object,Object>() {
//			String msg="";
//			@Override
//			protected Object doInBackground(Object... arg0) {
//				// TODO Auto-generated method stub
//				Map<String, String> map=new HashMap<String, String>();
////				String userId=LoginActivity.sureUser;
//			    map.put("feedbackUser", userId);
//				map.put("feedbackInfo", entity.getText());
//				map.put("feedbackDate", entity.getDate());
////				msg=HttpUtils.getInputStreamByPost(url, map, "UTF-8");
//				Log.v("Tag", msg);
//				return msg;
//			}
//		}.execute(url);
//	}

    // ?????????? String ????
    private String getDate() {
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));
        String sec = String.valueOf(c.get(Calendar.SECOND));
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins + ":" + sec);
        return sbBuffer.toString();
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.btn_send:
                send();
                break;
            case R.id.btn_back:
                FeedbackActivity.this.finish();
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
//		chatRecords();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        int size = mDataArrays.size();
        if (size > 0) {
            System.out.println(size);
            mDataArrays.removeAll(mDataArrays);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
