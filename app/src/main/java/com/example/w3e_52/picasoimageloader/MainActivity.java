package com.example.w3e_52.picasoimageloader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements View.OnClickListener , AdapterView.OnItemClickListener{

    private Button buttonUpload;
    private Button buttonChoose;
    private EditText editLink;
    private EditText editText;
    private String name,image;
    private ImageView imageView;
    RecyclerView recyclerView;
    private static final String TAG_PERSON = "person";

    public static final String KEY_IMAGE = "image";
    public static final String KEY_TEXT = "name";

    public static final String UPLOAD_URL = "http://192.168.2.29:80/upload.php";
    private static String url= "http://192.168.2.29:80/get.php";
    private int PICK_IMAGE_REQUEST = 1;

    private Bitmap bitmap;
    private ArrayList<Persons> contactList;
    private ProgressDialog pDialog;
    private ArrayList<String>results=new ArrayList<String>();
  //Hash map for recyler view
//    ArrayList<HashMap<String, String>> contactList;
    private RecyclerView.Adapter mAdapter;
    JSONArray person=null;
    CardViewAdapter mAdapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonChoose = (Button) findViewById(R.id.buttonChooseImage);

        editText = (EditText) findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.imageView);
        editLink=(EditText)findViewById(R.id.editTextLink);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        new GetContacts(this).execute();
        recyclerView.setHasFixedSize(true);
//        Button b=(Button)findViewById(R.id.button1);
//        b.setOnClickListener(listener);
        // ListView
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));


    }

//    public View.OnClickListener listener=new View.OnClickListener(){
//        @Override
//        public void onClick(View arg0) {
//            mAdapter2.imageLoader.clearCache();
//            mAdapter2.notifyDataSetChanged();
//        }
//    };

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                String real=getRealPathFromURI(this,filePath);
                InputStream is = getContentResolver().openInputStream(filePath);
               bitmap= BitmapFactory.decodeStream(is);

                int imageWidth = bitmap.getWidth();
                int imageHeight = bitmap.getHeight();
                int newHeight = (imageHeight * 200)/imageWidth;
                bitmap = Bitmap.createScaledBitmap(bitmap, 200, newHeight, false);
//                bitmap=Bitmap.createScaledBitmap(bitmap,600,500,true);
                is.close();
//                Bitmap   bm = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                  bitmap = Bitmap.createBitmap(Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), filePath), 3000, 2000, true));

                imageView.setImageBitmap(bitmap);
                editLink.setText(real);
                System.out.println("Image Width="+imageWidth+"  "+"Image Height="+imageHeight);
                System.out.println("Image New Width="+500+"  " +"Image New Height="+newHeight);
                Toast.makeText(getApplicationContext(),"Image has been set",Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }



    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public void uploadImage() {

        final String text = editText.getText().toString().trim();
        final String image = getStringImage(bitmap);
        class UploadImage extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            JSONObject hay;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please wait...", "uploading", false, false);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {
                    hay = new JSONObject(s);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Toast.makeText(MainActivity.this, hay.getString("status"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Toast.makeText(MainActivity.this, hay.getString("Data"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {

                RequestHandler rh = new RequestHandler();
                HashMap<String, String> param = new HashMap<String, String>();
                param.put(KEY_TEXT, text);
                param.put(KEY_IMAGE, image);


                String result = rh.sendPostRequest(UPLOAD_URL, param);
                return result;
            }
        }
        UploadImage u = new UploadImage();
        u.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    private class GetContacts extends AsyncTask<Void, Void, Void> {
Context context;
        public GetContacts(Context context) {
            this.context=context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            Log.d("url: ", "> " + url);
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    person = jsonObj.getJSONArray(TAG_PERSON);
                    contactList  = new ArrayList<Persons>();
                    // looping through All Contacts
                    for (int i = 0; i < person.length(); i++) {
                        JSONObject c = person.getJSONObject(i);

//                        String id = c.getString(TAG_ID);
                        name = c.getString(KEY_TEXT);
                        image = c.getString(KEY_IMAGE);
                        Log.d("ASHRADHA",""+image);

                        Persons feed = new Persons();

                        feed.setId(name);
                        feed.setName(image);
                        contactList.add(feed);

                        //Grid View
                        // recyclerView.setLayoutManager(new GridLayoutManager(this,2,1,false));
//                        Toast.makeText(getApplicationContext(), "Image is: " + image, Toast.LENGTH_SHORT).show();
                        //StaggeredGridView
                        // recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,1));

                        // create an Object for Adapter
//                        mAdapter = new CardViewAdapter(contactList);


//                        // tmp hashmap for single contact
//                        HashMap<String, String> contact = new HashMap<String, String>();
//
//                        // adding each child node to HashMap key => value
//                         contact.put(KEY_IMAGE, image);
//                        contact.put(KEY_TEXT, name);
//
//                        // adding contact to contact list
//                        contactList.add(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

             return null;
        }



        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             *
             *
             *
             * */


            mAdapter = new CardViewAdapter(contactList,context);
            // set the adapter object to the Recyclerview
            recyclerView.setAdapter(mAdapter);

//            ListAdapter adapter = new SimpleAdapter(
//                    MainActivity.this, contactList,
//                    R.layout.list_item, new String[]{TAG_NAME,TAG_EMAIL, TAG_PHONE,TAG_SPINNER,TAG_DESCFRIPTION}, new int[]{R.id.name,R.id.email, R.id.mobile,R.id.spinner,R.id.showdes});
////            WainActivity.this, contactList,
////                    R.layout.list_item, new String[] { TAG_NAME, TAG_EMAIL,
////                    TAG_PHONE_MOBILE }, new int[] { R.id.name,
////                    R.id.email, R.id.mobile });
////                      results.add("Name: " + name + "  phone: " + a_phone);
//
//            setListAdapter(adapter);
        }

    }
//To parse json object
//    private List<Message> decodeJson(String fromServer) {
//        List<Message> list = new ArrayList<Message>();
//        try {
//            JSONArray jsAry = new JSONArray(fromServer);
//            for (int i = 0; i < jsAry.length(); i++) {
//                JSONObject jsObj = (JSONObject) jsAry.get(i);
//                Message msg = new Message();
//                msg.setSender(jsObj.getString("sender"));
//                msg.setMessage(jsObj.getString("message"));
//                list.add(msg);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }
        if (v == buttonUpload) {
            uploadImage();
        }
    }
}