package com.AmiCity.Planner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;

import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;
import com.AmiCity.Planner.TileItem.TileType;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.os.Build;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.MediaStore;

public class CreateTaskActivity extends FragmentActivity implements OnDateSetListener,OnTimeSetListener{
	private static final int REQUEST_TAKE_PHOTO = 755; 
	private static final int REQUEST_ATTACH_FILE = 756;
	private static final int REQUEST_PICK_CONTACT = 757;
	private Task m_task;
	private ImageView m_imageContainer;
	private EditText m_descriptionEditText;
	private HashSet<TileItem> m_tileList;
	private StaggeredGridView m_tileGridView;
	private String m_tempPhotoPath;
	StaggeredAdapter m_staggeredAdapter;
	private GregorianCalendar m_newNotification;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_task = new Task();
		m_tileList = new HashSet<TileItem>();
		setContentView(R.layout.activity_create_task);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.create_task, menu);
		inflater.inflate(R.menu.action_bar_new_task, menu);
		m_descriptionEditText = (EditText)findViewById(R.id.TaskDetails);
		//this.imageContainer = (ImageView)this.findViewById(R.id.ImgContainer);
		Bundle extras = getIntent().getExtras();
		
		m_tileGridView = (StaggeredGridView) this.findViewById(R.id.staggeredGridView1);
		m_tileGridView.setItemMargin(20); // set the GridView margin
		m_tileGridView.setPadding(0, 0, 0, 0); // have the margin on the sides as well 
		
		if(extras != null)
		{
			String serializedTask = extras.getString("data");
			Gson deserializer = new Gson();
			m_task = deserializer.fromJson(serializedTask, Task.class);
			LoadTaskDataToUI();
		}
		
		m_tileGridView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(StaggeredGridView parent, View view, int position,
					long id) {
				TileItem item = m_staggeredAdapter.getItem(position);
				HadleTitleClick(item);
				
				
				// TODO Auto-generated method stub
				
			}
		});
		return true;
	}
	
	public void HadleTitleClick(TileItem item)
	{
		if(item.GetType() == TileType.FILE  || item.GetType() == TileType.PHOTO)
		{
			LaunchFileFromPath(item.GetText());
		}
		int contactID = -1;
		if(item.GetType() == TileType.CONTACT)
		{
			try
			{
				contactID = Integer.parseInt(item.values.get("contact_id"));
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), "An error occured when launching the contact.", 4000).show();
			}
			if(contactID != -1)
			{
				LaunchContact(contactID);
			}
		}
	}
	
	public void LaunchContact(int contactID)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
	    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactID));
	    intent.setData(uri);
	    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
	    getApplicationContext().startActivity(intent);
	}
	
	public void LaunchFileFromPath(String path)
	{
		MimeTypeMap myMime = MimeTypeMap.getSingleton();

		Intent newIntent = new Intent(android.content.Intent.ACTION_VIEW);

		String mimeType = myMime.getMimeTypeFromExtension(fileExt(path.toString()).substring(1));
		newIntent.setDataAndType(Uri.fromFile(new File(path)),mimeType);
		newIntent.setFlags(newIntent.FLAG_ACTIVITY_NEW_TASK);
		try {
			getApplicationContext().startActivity(newIntent);
		} catch (android.content.ActivityNotFoundException e) {
		    Toast.makeText(getApplicationContext(), "No handler for this type of file.", 4000).show();
		}
	}
	
	private String fileExt(String url) {
	    if (url.indexOf("?")>-1) {
	        url = url.substring(0,url.indexOf("?"));
	    }
	    if (url.lastIndexOf(".") == -1) {
	        return null;
	    } else {
	        String ext = url.substring(url.lastIndexOf(".") );
	        if (ext.indexOf("%")>-1) {
	            ext = ext.substring(0,ext.indexOf("%"));
	        }
	        if (ext.indexOf("/")>-1) {
	            ext = ext.substring(0,ext.indexOf("/"));
	        }
	        return ext.toLowerCase();

	    }
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		m_task.SetDescription(m_descriptionEditText.getText().toString());
		if (id == R.id.action_settings) 
		{
			return true;
		}
		if (id == R.id.action_camera) 
		{
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
			
			File photoFile = null;
	        photoFile = createImageFile();
	        // Continue only if the File was successfully created
	        if (photoFile != null) 	
	        {
	        	cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
	        }
            startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO); 
			return true;
		}
		if (id == R.id.action_attach) 
		{
			Intent intent = new Intent(this, FilePicker.class);
	    	startActivityForResult(intent, REQUEST_ATTACH_FILE);
			return true;
		}
		if (id == R.id.action_contact)
		{
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, REQUEST_PICK_CONTACT);
		}
		if (id == R.id.action_alarm)
		{
			Bundle b = new Bundle();
			Calendar calendar = Calendar.getInstance();
			b.putInt(DatePickerDialogFragment.YEAR, calendar.get(Calendar.YEAR) );
			b.putInt(DatePickerDialogFragment.MONTH, calendar.get(Calendar.MONTH));
			b.putInt(DatePickerDialogFragment.DATE, calendar.get(Calendar.DAY_OF_MONTH));
			DatePickerDialogFragment picker = new DatePickerDialogFragment();
			picker.setArguments(b);
			picker.show(getSupportFragmentManager(), "fragment_date_picker");
		}
		if (id == R.id.action_accept) 
		{
			/*Return the newly created task*/
			Gson gson = new Gson();
			String serializedTask = gson.toJson(m_task);
			/*TODO: Image Paths*/
			
			Intent databackIntent = new Intent(); 
			databackIntent.putExtra("new_task", serializedTask); 
			setResult(Activity.RESULT_OK, databackIntent);
			finish();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{  
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) 
        {   
            m_task.AddImagePath(m_tempPhotoPath);
            m_tempPhotoPath = "";
        }
        if (requestCode == REQUEST_ATTACH_FILE && resultCode == RESULT_OK) 
        {  
        	String fileName = (String) data.getExtras().get("file_name"); 
        	/*Perhaps add another list here to allow the user to upload more files*/
        	m_task.AddNewFilePath(fileName);
        	
        }
        if (requestCode == REQUEST_PICK_CONTACT && resultCode == RESULT_OK) 
	    if (resultCode == Activity.RESULT_OK) {
	        Uri contactData = data.getData();
	        Cursor c =  getContentResolver().query(contactData, null, null, null, null);
	        if (c.moveToFirst()) {
	          int contactID = c.getInt(c.getColumnIndexOrThrow(PhoneLookup._ID));
	          m_task.AddContact(contactID);
	          // TODO Whatever you want to do with the selected contact name.
	        }
	    }
        LoadTaskDataToUI();
    }
	
	private void LoadTaskDataToUI()
	{
		m_descriptionEditText.setText(m_task.GetDescription());
		m_tileList.clear();
		
		if(m_task.GetContacts().size() > 0)
		{
			for (Integer contactID : m_task.GetContacts()) 
			{
				Drawable image = openDisplayPhoto(contactID);
				if(image == null)
				{
					image = getResources().getDrawable(R.drawable.ic_person);
				}
				if(image != null)
				{
					TileItem newContactTile = new TileItem(image, GetContactNameFromID(contactID), TileItem.TileType.CONTACT);
					newContactTile.values.put("contact_id", contactID.toString()); 
					m_tileList.add(newContactTile);
				}
			}
		}
		
		if(m_task.GetImagePaths().size() > 0)
		{
			for (String file : m_task.GetImagePaths()) 
			{
				Drawable image = GetDrawableFromPath(file,640,460);
				m_tileList.add(new TileItem(image, file, TileItem.TileType.PHOTO));
			}
		}
		if(m_task.GetFilePaths().size() > 0)
		{
			for (String file : m_task.GetFilePaths()) 
			{
				m_tileList.add(new TileItem(getResources().getDrawable(R.drawable.ic_file), file, TileItem.TileType.FILE));
			}
		}
		
		if(m_task.GetNotifications().size() > 0)
		{
			for (GregorianCalendar calendar : m_task.GetNotifications()) 
			{
				SimpleDateFormat date_format = new SimpleDateFormat("MMM dd,yyyy HH:mm");
			    Date resultdate = calendar.getTime();
				m_tileList.add(new TileItem(getResources().getDrawable(R.drawable.ic_clock),date_format.format(resultdate) , TileItem.TileType.NOTIFICATION));
			}
		}
		
		if(m_tileList.size() > 0)
		{
			StaggeredAdapter adapter = new StaggeredAdapter(this, R.id.imageView1, m_tileList.toArray(new TileItem[m_tileList.size()]));
			m_staggeredAdapter = adapter;
			m_tileGridView.setAdapter(adapter);
			adapter.notifyDataSetChanged();	
		}
	}
	
	public Drawable openDisplayPhoto(long contactId) 
	{
		Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
		InputStream photo = ContactsContract.Contacts.openContactPhotoInputStream (getContentResolver(), uri, true);
		if(photo == null)
			return null;
		return Drawable.createFromStream(photo, null);
	 }
	
	public String GetContactNameFromID(int contactId)
	{
		String contactName = "";
		Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
		ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(contactUri,
               null, null, null, null);
        if (cursor == null) {
            return "";
        }
        if (cursor.moveToFirst()) {
        	contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        return contactName;
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_create_task,
					container, false);
			return rootView;
		}
	}
		private File createImageFile() {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    File image = null;
		try {
			image = File.createTempFile(
			    imageFileName,  /* prefix */
			    ".jpg",         /* suffix */
			    storageDir      /* directory */
			);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    // Save a file: path for use with ACTION_VIEW intents
		m_tempPhotoPath = image.getAbsolutePath();
	    return image;
	}
	
	private BitmapDrawable GetDrawableFromPath(String filePath,final int targetW, final int targetH) {

	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(filePath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
	    return new BitmapDrawable(getResources(),bitmap);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		m_newNotification = new GregorianCalendar(year, monthOfYear, dayOfMonth);
		
		Calendar cal = Calendar.getInstance();
	    int hour = cal.get(Calendar.HOUR_OF_DAY);
	    int minute = cal.get(Calendar.MINUTE);


	    Bundle b = new Bundle();
	    b.putInt(TimePickerDialogFragment.HOUR, hour);
	    b.putInt(TimePickerDialogFragment.MINUTE, minute);

	    TimePickerDialogFragment picker = new TimePickerDialogFragment();
	    picker.setArguments(b);
	    picker.show(getSupportFragmentManager(), "frag_time_picker");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		m_newNotification.set(Calendar.HOUR_OF_DAY, hourOfDay);
		m_newNotification.set(Calendar.MINUTE, minute);
		createScheduledNotification(m_newNotification,m_task.GetDescription());
		m_task.AddNewNotification(m_newNotification);
		LoadTaskDataToUI();
	}
	
	private void createScheduledNotification(GregorianCalendar calendar,String text)
	 {
	 getBaseContext();
	// Retrieve alarm manager from the system
	 AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
	 // Every scheduled intent needs a different ID, else it is just executed once
	 int id = (int) System.currentTimeMillis();

	 // Prepare the intent which should be launched at the date
	 Intent intent = new Intent(this, AmiCityAlarmReceiver.class);
	 intent.putExtra("description", text);
	 // Prepare the pending intent
	 PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

	 // Register the alert in the system. You have the option to define if the device has to wake up on the alert or not
	 alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
	 }

}
