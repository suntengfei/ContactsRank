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
	
	/**ï¿½ï¿½È¡ï¿½ï¿½Phonï¿½ï¿½ï¿½Ö¶ï¿½**/
    private static final String[] PHONES_PROJECTION = new String[] {
    	Phone.DISPLAY_NAME, Phone.NUMBER, Photo._ID,Phone.CONTACT_ID };
   
    /**ï¿½ï¿½Ïµï¿½ï¿½ï¿½ï¿½Ê¾ï¿½ï¿½ï¿½ï¿½**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    
    /**ï¿½ç»°ï¿½ï¿½ï¿½ï¿½**/
    private static final int PHONES_NUMBER_INDEX = 1;
    
    /**Í·ï¿½ï¿½ID**/
    private static final int PHONES_PHOTO_ID_INDEX = 2;
   
    /**ï¿½ï¿½Ïµï¿½Ëµï¿½ID**/
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
		//ï¿½ï¿½È¡ï¿½ï¿½Ïµï¿½ï¿½
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,PHONES_PROJECTION,null,null,null);
		//mContactsID = new Long[phoneCursor.getCount()+1];
		if(phoneCursor!=null)
		{
			int i = 0;
			while(phoneCursor.moveToNext())
			{
				
				//ï¿½Ãµï¿½ï¿½Ö»ï¿½ï¿½ï¿½ï¿½ï¿½
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				
				if(TextUtils.isEmpty(phoneNumber))
					continue;
				//ï¿½ï¿½Ïµï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
				//ï¿½ï¿½Ïµï¿½ï¿½ID
				int contactId = phoneCursor.getInt(PHONES_CONTACT_ID_INDEX);
				//ï¿½ï¿½Ïµï¿½ï¿½Í·ï¿½ï¿½ID
				Long photoId = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
				//ï¿½ï¿½Ïµï¿½ï¿½Í·ï¿½ï¿½bitMAP
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
		return cts;
	}
	
	
	//Ã¿¸öcidÖ»³öÏÖÒ»´Î£¬²»ÖØ¸´ÓÃ»§
	public ArrayList<Contact> getPhoneContactsS(){
		cts.clear();
		ContentResolver resolver = mContext.getContentResolver();
		//»ñÈ¡ÁªÏµÈË
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,PHONES_PROJECTION,null,null,null);
		if(phoneCursor!=null)
		{
			int j;
			while(phoneCursor.moveToNext())
			{
				
				//µÃµ½ÊÖ»úºÅÂë
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				
				if(TextUtils.isEmpty(phoneNumber))
					continue;
				//ÁªÏµÈËÃû³Æ
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
				//ÁªÏµÈËID
				int contactId = phoneCursor.getInt(PHONES_CONTACT_ID_INDEX);
				//ÁªÏµÈËÍ·ÏñID
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
