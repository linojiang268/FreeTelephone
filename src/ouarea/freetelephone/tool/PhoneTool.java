package ouarea.freetelephone.tool;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 手机工具
 * 
 * @author ouArea
 * 
 */
public class PhoneTool {
	/**
	 * 获取电话号
	 * 
	 * @param context
	 * @return
	 */
	public static String getPhoneNumber(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getLine1Number();
	}
}
