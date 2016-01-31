package com.asosapp.phone.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

	/**
	 * @return ��ǰ��¼�û��ĵ�ǰ�����ͼƬ�ļ���Ŀ¼
	 */
	public static String getImageFolder(String url) {
		String sDir = "";
		String bitmapName = url.substring(url.lastIndexOf("/") + 1);
		sDir = Const.SD_CARD_URL + "/Image/";
		// �����ļ���
		File destDir = new File(sDir);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		// ���ظ��û�id�µ��ļ���Ŀ¼
		return sDir + bitmapName;
	}
	/**
	 * @return ��ǰ��¼�û��ĵ�ǰ�����ͼƬ�ļ���Ŀ¼
	 */
	public static String getApkFolder() {
		String sDir = "";
		sDir = Const.SD_CARD_URL + "/apk/";
		// �����ļ���
		File destDir = new File(sDir);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		// ���ظ��û�id�µ��ļ���Ŀ¼
		return sDir;
	}

	/**
	 * ��������ͼƬ
	 */
	public static void saveBitmap(final Context context, final String url) {
		File bitMapFile = new File(getImageFolder(url));

		if (!bitMapFile.exists()) {
			RequestQueue mQueue = Volley.newRequestQueue(context);

			ImageRequest imageRequest = new ImageRequest(url,
					new Response.Listener<Bitmap>() {
						@Override
						public void onResponse(Bitmap response) {
							if (response != null) {
								String path = getImageFolder(url);
								File imageFile = new File(path);
								FileOutputStream out;
								try {
									out = new FileOutputStream(imageFile);
									response.compress(
											Bitmap.CompressFormat.PNG, 90, out);
									out.flush();
									out.close();
								} catch (FileNotFoundException e) {
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}, 0, 0, Config.RGB_565, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
				}
			});

			mQueue.add(imageRequest);
		}
	}

	/**
	 * ��������ͼƬ��·��,��ͼƬ����,������ImageView
	 *
	 * @param context
	 * @param url
	 * @param imgView
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void saveBitmap(final Context context, final String url,
								  final ImageView imgView) {
		File bitMapFile = new File(getImageFolder(url));

		if (bitMapFile.exists()) {
			Drawable titleBgDrawable = new BitmapDrawable(getImageFolder(url));
			imgView.setImageDrawable(titleBgDrawable);
		} else {
			RequestQueue mQueue = Volley.newRequestQueue(context);

			ImageRequest imageRequest = new ImageRequest(url,
					new Response.Listener<Bitmap>() {
						@Override
						public void onResponse(Bitmap response) {
							if (response != null) {
								String path = getImageFolder(url);
								File imageFile = new File(path);
								FileOutputStream out;
								try {
									out = new FileOutputStream(imageFile);
									response.compress(
											Bitmap.CompressFormat.PNG, 90, out);
									out.flush();
									out.close();

									Drawable titleBgDrawable = new BitmapDrawable(
											path);
									imgView.setImageDrawable(titleBgDrawable);

								} catch (FileNotFoundException e) {
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}, 0, 0, Config.RGB_565, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
				}
			});

			mQueue.add(imageRequest);
		}
	}

	/**
	 * �����ļ��в������򴴽�
	 */
	public static String createFolder() {
		String sDir = "";
		if (DeviceHelper.hasSDCard()) {
			sDir = Const.SD_CARD_URL;
		} else {
			// sDir = NOSDCARD_DIR;
		}

		// �����ļ���
		File destDir = new File(sDir);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		return sDir;
	}

	/**
	 * ɾ���ļ�����������
	 *
	 */
	public static void deleteFile(File file) {
		if (file.exists()) { // �ж��ļ��Ƿ����
			if (file.isFile()) { // �ж��Ƿ����ļ�
				file.delete(); // delete()���� ��Ӧ��֪�� ��ɾ������˼;
			} else if (file.isDirectory()) { // �����������һ��Ŀ¼
				File files[] = file.listFiles(); // ����Ŀ¼�����е��ļ� files[];
				for (int i = 0; i < files.length; i++) { // ����Ŀ¼�����е��ļ�
					deleteFile(files[i]); // ��ÿ���ļ� ������������е���
				}
			}
			file.delete();
		}
	}

}
