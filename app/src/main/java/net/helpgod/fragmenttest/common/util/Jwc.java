package net.helpgod.fragmenttest.common.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

/**
 * start : 2012.08.30<br>
 * <br>
 * 비동기 Layout Controll인 JwViewController를 확장한 클래스이다.<br>
 * Layout에 관련된 Util을 추가확장 했다.<br>
 * 
 * @author 오재웅      
 * @version 2013.11.17
 */
public class Jwc {
	
	
	public static int getColor(String color){
		return Color.parseColor(color);
	}

	public static float getDensity(Context context){
		return  context.getResources().getDisplayMetrics().density;
	}
	public static float getDensity(Activity context){
		return  context.getResources().getDisplayMetrics().density;
	}
	public static float getDensity(View view){
		return  view.getContext().getResources().getDisplayMetrics().density;
	}

	public static int getDp(Context context, int pix){
		return (int)(pix/context.getResources().getDisplayMetrics().density);
	}

	public static int getPix(Context context, int dp){
		return (int)(dp*context.getResources().getDisplayMetrics().density);
	}

	public static int getWindowHeight(Context context){
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size.y;
	}

	public static int getWindowWidth(Context context){
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size.x;
	}

	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public static void setVisible(View target, boolean isVisible, int falseVisible){
		if(isVisible){
			target.setVisibility(View.VISIBLE);
		}else{
			target.setVisibility(falseVisible);
		}
	}

	public static void setVisible(View target, boolean isVisible){
		Jwc.setVisible(target,isVisible, View.GONE);
	}


	public static boolean isActivityRunning(Context context){
		ActivityManager actMng = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> list = actMng.getRunningAppProcesses();

		for(ActivityManager.RunningAppProcessInfo info : list){
			if(info.processName.equals(context.getPackageName()))
			{
				return true;
			}

		}
		return false;
	}

	public static void toast(Context context, String msg){
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void hideKeyboard(Context context, EditText editText){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	public static void hideKeyboard(Activity activity){
		View view = activity.getCurrentFocus();
		if ( view != null ) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	public static void showKeyboard(Context context, EditText editText){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editText, 0);

	}

	public static View lastChild(ViewGroup parantView){
		return parantView.getChildAt(parantView.getChildCount() - 1);
	}

	public static View getInfalterView(Context context, int layout_id){
//		return LayoutInflater.from(context).inflate(layout_id, null);
		return ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layout_id, null);
//		return View.inflate(context, layout_id,null);
	}

	public static View getInfalterView(Context context, int layout_id, ViewGroup parents){
		return ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layout_id, parents);
	}


	public static View findViewById(View view, int R_id){
		return view.findViewById(R_id);
	}


	public static View findViewWithTag(View parants, Object tag){
		return parants.findViewWithTag(tag);
	}

	public static void saveViewToImage(View saveView, File saveFile) throws IOException {
		OutputStream out = new FileOutputStream(saveFile);

		Bitmap bitmap = Bitmap.createBitmap(saveView.getMeasuredWidth(),saveView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
		Canvas bitmapHolder = new Canvas(bitmap);
		saveView.draw(bitmapHolder);
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		out.flush();
		out.close();
		saveFile.setReadable(true, false);
	}

	public static void setlargeDialog(Dialog dialog){
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes( ) ;
		WindowManager wm = ((WindowManager)dialog.getContext().getApplicationContext().getSystemService(dialog.getContext().getApplicationContext().WINDOW_SERVICE)) ;
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		lp.width = size.x;
		lp.height = size.y - getStatusBarHeight(dialog.getContext())-(int)(50*getDensity(dialog.getContext()));

		dialog.getWindow().setAttributes( lp ) ;
//		dialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
	}

	public static String getHashKey(Context context) throws NoSuchAlgorithmException, PackageManager.NameNotFoundException {
		MessageDigest md = null;
		PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),PackageManager.GET_SIGNATURES);
		for (android.content.pm.Signature signature : info.signatures) {
			md = MessageDigest.getInstance("SHA1");
			md.update(signature.toByteArray());
		}
		return Base64.encodeToString(md.digest(), Base64.DEFAULT);
	}

	public static String getVersion(Context context) throws PackageManager.NameNotFoundException {
		return getPackageInfo(context).versionName;
	}

	public static PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException {
		return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
	}

	public static void gotMarket(Context context){
		String packageName = context.getPackageName();
		try {
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
		} catch (android.content.ActivityNotFoundException anfe) {
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
		}
	}

	public static void appStore(Context context){
		gotMarket(context);
	}

	public static void clearNotification(Context context){
		NotificationManager notifiyMgr = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		notifiyMgr.cancelAll();
	}

	public static void clipboardCopy(Context context, String label, String text){
		ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clipData = ClipData.newPlainText(label, text);
		clipboardManager.setPrimaryClip(clipData);
	}

	public static void share(Context context, String title, String subject, String text){
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, text);
		Intent chooser = Intent.createChooser(intent, title);
		context.startActivity(chooser);
	}

	@SuppressLint("MissingPermission")
	public static boolean isConnectNetwork(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			Network network = null;
			if (connectivityManager == null) {
				return false;
			} else {
				network = connectivityManager.getActiveNetwork();
				NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
				if (networkCapabilities == null) {
					return false;
				}
				return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
						networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
			}
		} else {
			if (connectivityManager == null) {
				return false;
			}
			if (connectivityManager.getActiveNetworkInfo() == null) {
				return false;
			}
			return connectivityManager.getActiveNetworkInfo().isConnected();
		}
	}

	@SuppressLint("MissingPermission")
	public static boolean isWifi(Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			Network network = null;
			if (connectivityManager == null) {
				return false;
			} else {
				network = connectivityManager.getActiveNetwork();
				NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
				if (networkCapabilities == null) {
					return false;
				}
				return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
			}
		} else {
			if (connectivityManager == null) {
				return false;
			}
			if (connectivityManager.getActiveNetworkInfo() == null) {
				return false;
			}
			return connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
		}
	}

	public static String getMacAddress() {
		String interfaceName = "wlan0";
		try {
			List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				if (interfaceName != null) {
					if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
				}
				byte[] mac = intf.getHardwareAddress();
				if (mac==null) return "";
				StringBuilder buf = new StringBuilder();
				for (int idx=0; idx<mac.length; idx++)
					buf.append(String.format("%02X:", mac[idx]));
				if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
				return buf.toString();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}


	public static String getStringStackTrace(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();

	}

}
