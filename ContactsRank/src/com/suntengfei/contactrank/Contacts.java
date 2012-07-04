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
	
	/**��ȡ��Phon���ֶ�**/
    private static final String[] PHONES_PROJECTION = new String[] {
    	Phone.DISPLAY_NAME, Phone.NUMBER, Photo._ID,Phone.CONTACT_ID };
   
    /**��ϵ����ʾ����**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    
    /**�绰����**/
    private static final int PHONES_NUMBER_INDEX = 1;
    
    /**ͷ��ID**/
    private static final int PHONES_PHOTO_ID_INDEX = 2;
   
    /**��ϵ�˵�ID**/
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
		//��ȡ��ϵ��
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,PHONES_PROJECTION,null,null,null);
		//mContactsID = new Long[phoneCursor.getCount()+1];
		if(phoneCursor!=null)
		{
			int i = 0;
			while(phoneCursor.moveToNext())
			{
				
				//�õ��ֻ�����
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				
				if(TextUtils.isEmpty(phoneNumber))
					continue;
				//��ϵ������
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
				//��ϵ��ID
				int contactId = phoneCursor.getInt(PHONES_CONTACT_ID_INDEX);
				//��ϵ��ͷ��ID
				Long photoId = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
				//��ϵ��ͷ��bitMAP
/*				Bitmap contactPhoto = null;
				if(photoId>0)
				{
					Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
					InputStream input;
					input= ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);				}
					contactPhoto = BitmapFactory.decodeStream(input); 	
				}*/
				cts.add(new Contact(contactId,contactName,phoneNumber));
/*				mContactsName.add(contactName);
				mContactsID[i++] = contactId;
				mContactsNumber.add(phoneNumber); */
			}
			phoneCursor.close();
		}
		return cts;
	}
	
}
