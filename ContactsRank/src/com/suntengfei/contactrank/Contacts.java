package com.suntengfei.contactrank;

import java.util.ArrayList;

import com.sutnengfei.contactrank.model.Contact;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.text.TextUtils;
import android.util.Log;

public class Contacts
{
	private Context mContext;
	
	/**获取库Phon表字段**/
    private static final String[] PHONES_PROJECTION = new String[] {
    	Phone.DISPLAY_NAME, Phone.NUMBER, Photo._ID,Phone.CONTACT_ID };
   
    /**联系人显示名称**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    
    /**电话号码**/
    private static final int PHONES_NUMBER_INDEX = 1;
    
    /**头像ID**/
    private static final int PHONES_PHOTO_ID_INDEX = 2;
   
    /**联系人的ID**/
    private static final int PHONES_CONTACT_ID_INDEX = 3;
    
/*    private ArrayList<String> mContactsName = new ArrayList<String>();
    private ArrayList<String> mContactsNumber = new ArrayList<String>();
    private Long[] mContactsID;
    private ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();*/
    
    private ArrayList<Contact> cts;
    
    public Contacts(Context mContext)
    {
    	cts = new ArrayList<Contact>();
    	this.mContext = mContext;
    }
    
	public ArrayList<Contact> getPhoneContacts(){
		cts.clear();
		ContentResolver resolver = mContext.getContentResolver();
		//获取联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,PHONES_PROJECTION,null,null,null);
		//mContactsID = new Long[phoneCursor.getCount()+1];
		if(phoneCursor!=null)
		{
			int i = 0;
			while(phoneCursor.moveToNext())
			{
				
				//得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				
				if(TextUtils.isEmpty(phoneNumber))
					continue;
				//联系人名称
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
				//联系人ID
				int contactId = phoneCursor.getInt(PHONES_CONTACT_ID_INDEX);
				//联系人头像ID
				Long photoId = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
				//联系人头像bitMAP
/*				Bitmap contactPhoto = null;
				if(photoId>0)
				{
					Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
					InputStream input;
					input= ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);				}
					contactPhoto = BitmapFactory.decodeStream(input); 	
				}*/
				if(phoneNumber.length()>11)
					phoneNumber = phoneNumber.substring(phoneNumber.length()-11);
				cts.add(new Contact(contactId,contactName,phoneNumber));
/*				mContactsName.add(contactName);
				mContactsID[i++] = contactId;
				mContactsNumber.add(phoneNumber); */
			}
			phoneCursor.close();
		}
		
		for(int i = 0;i<cts.size();i++)
			Log.i("2102749",cts.get(i).toString());
		
		return cts;
	}
	
	
	//每个cid只出现一次，不重复用户
	public ArrayList<Contact> getPhoneContactsS(){
		cts.clear();
		ContentResolver resolver = mContext.getContentResolver();
		//获取联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,PHONES_PROJECTION,null,null,null);
		if(phoneCursor!=null)
		{
			int j;
			while(phoneCursor.moveToNext())
			{
				
				//得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				
				if(TextUtils.isEmpty(phoneNumber))
					continue;
				//联系人名称
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
				//联系人ID
				int contactId = phoneCursor.getInt(PHONES_CONTACT_ID_INDEX);
				//联系人头像ID
				Long photoId = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
				
				if(phoneNumber.length()>11)
					phoneNumber = phoneNumber.substring(phoneNumber.length()-11);
				for( j = 0;j<cts.size();j++)
					if(contactId==cts.get(j).get_cid())
						break;
				if(j>=cts.size())
					cts.add(new Contact(contactId,contactName,phoneNumber));
			}
			phoneCursor.close();
		}
		
		return cts;
	}
	
}
