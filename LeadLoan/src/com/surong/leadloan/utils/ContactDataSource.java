package com.surong.leadloan.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts.People;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.surong.leadloan.entity.MobileSoftware;

/**
 * 得到联系人的一个类 ，此类的一个方法只能在程序第一次打开的时候 开会被 调用 获取手机通信录信息
 * 
 * @author 崔
 */

@SuppressWarnings("deprecation")
public class ContactDataSource {

	private static final String[] PHONES_PROJECTION = new String[] { Phone._ID,
			Phone.DISPLAY_NAME, Phone.NUMBER, Phone.PHOTO_ID };
	private static final String SIM_URI = "content://icc/adn"; // smi閸楋拷

	public ContactDataSource(Context context) {
		super();
	}

	/**
	 * 得到系统联系人信息
	 * 
	 * @param context
	 * @return
	 */
	public static List<MobileSoftware> getMobileContacts(Context contexts) {
		List<MobileSoftware> contactsList = new ArrayList<MobileSoftware>();
		Uri parse = Uri.parse(SIM_URI); // 鐎规矮绠熸總绲猰i閻ㄥ嫯鐭惧锟�
		ContentResolver resolver = contexts.getContentResolver(); // 得到对象
		// 得到结果集
		Cursor cursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION,
				null, null, null);
		// 便利结果集
		while (cursor != null && cursor.moveToNext()) {
			MobileSoftware mo = new MobileSoftware();
			String phone = cursor
					.getString(cursor.getColumnIndex(Phone.NUMBER));
			String numberPhone = phone.replaceAll(" ", ""); // 鍘绘帀绌烘牸
			String name = cursor.getString(cursor
					.getColumnIndex(Phone.DISPLAY_NAME));
			String phoneName = name.replaceAll(" ", "");
			// String sort_key_alt =
			// cursor.getString(cursor.getColumnIndex(Phone.s));
			if (numberPhone.substring(0, 1).equals("+")) {
				numberPhone = numberPhone.substring(3, numberPhone.length());
			}
			mo.setPhoneName(phoneName);
			mo.setPhoneNum(numberPhone);
			contactsList.add(mo);
		}

		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		// 得到结果集smi卡上联系人的信息
		Cursor cursors = resolver.query(parse, null, null, null, null);

		while (cursors != null && cursors.moveToNext()) {
			String phone = cursors.getString(cursors
					.getColumnIndex(People.NUMBER));
			String numberPhone = phone.replaceAll(" ", "");
			String name = cursors
					.getString(cursors.getColumnIndex(People.NAME));
			String phoneName = name.replaceAll(" ", "");

			if (numberPhone.substring(0, 1).equals("+")) {
				numberPhone = numberPhone.substring(3, numberPhone.length());
			}
			boolean bo = true;
			for (MobileSoftware mobileSoftware : contactsList) {
				if (mobileSoftware.getPhoneNum().equals(numberPhone)) {
					bo = false;
					break;
				}
			}
			MobileSoftware mo = new MobileSoftware();
			mo.setPhoneName(phoneName);
			mo.setPhoneNum(numberPhone);
			if (bo) {
				contactsList.add(mo);
			}
		}
		// jiashuju
		for (int i = 0; i < contactsList.size(); i++) {
			if (i % 3 == 0) {
				contactsList.get(i).setState(0);
			}
			if (i % 3 == 1) {
				contactsList.get(i).setState(1);
			}
			if (i % 3 == 2) {
				contactsList.get(i).setState(2);
			}
		}
		if (cursors != null) {
			cursors.close();
			cursors = null;
		}
		return contactsList;
	}
}
