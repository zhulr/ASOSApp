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
	 * @return 当前登录用户的当前会议的图片文件夹目录
	 */
	public static String getImageFolder(String url) {
		String sDir = "";
		String bitmapName = url.substring(url.lastIndexOf("/") + 1);
		sDir = Const.SD_CARD_URL + "/Image/";
		// 创建文件夹
		File destDir = new File(sDir);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		// 返回该用户id下的文件夹目录
		return sDir + bitmapName;
	}
	/**
	 * @return 当前登录用户的当前会议的图片文件夹目录
	 */
	public static String getApkFolder() {
		String sDir = "";
		sDir = Const.SD_CARD_URL + "/apk/";
		// 创建文件夹
		File destDir = new File(sDir);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		// 返回该用户id下的文件夹目录
		return sDir;
	}

	/**
	 * 保存网络图片
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
	 * 传入网络图片的路径,将图片保存,并赋予ImageView
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
	 * 若根文件夹不存在则创建
	 */
	public static String createFolder() {
		String sDir = "";
		if (DeviceHelper.hasSDCard()) {
			sDir = Const.SD_CARD_URL;
		} else {
			// sDir = NOSDCARD_DIR;
		}

		// 创建文件夹
		File destDir = new File(sDir);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		return sDir;
	}

	/**
	 * 删除文件夹所有内容
	 *
	 */
	public static void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		}
	}

}
