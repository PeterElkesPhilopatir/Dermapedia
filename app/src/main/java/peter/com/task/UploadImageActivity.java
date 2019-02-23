package peter.com.task;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.koushikdutta.ion.builder.MultipartBodyBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class UploadImageActivity extends AppCompatActivity {
    private Uri fileUri1, fileUri2, fileUri3, fileUri4;
    int choice;
    static int SELECT_IMAGE = 1234;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        bundle = getIntent().getExtras();
        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        findViewById(R.id.img_backID).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice = 1;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                getPermission();
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
            }
        });

        findViewById(R.id.img_frontID).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice = 2;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                getPermission();
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
            }
        });

        findViewById(R.id.img_licBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice = 3;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                getPermission();
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
            }
        });

        findViewById(R.id.img_licFront).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice = 4;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                getPermission();
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
            }
        });

        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileUri1 != null && fileUri2 != null && fileUri3 != null && fileUri4 != null)
                    if (getIntent().getExtras().getString(getString(R.string.LOGIN_TYPE)).equals(getString(R.string.manual)))
                        manualRegister();
                    else
                        socialRegister();
                else
                    Toast.makeText(UploadImageActivity.this, "Please select all images", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imageView;
        Bitmap bitmap;
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        switch (choice) {
                            case 1:
                                bitmap = MediaStore.Images.Media.getBitmap(UploadImageActivity.this.getContentResolver(), data.getData());
                                imageView = (ImageView) findViewById(R.id.img_backID);
                                imageView.setImageBitmap(bitmap);
                                fileUri1 = getImageUri(getApplicationContext(), bitmap);
                                break;

                            case 2:
                                bitmap = MediaStore.Images.Media.getBitmap(UploadImageActivity.this.getContentResolver(), data.getData());
                                imageView = (ImageView) findViewById(R.id.img_frontID);
                                imageView.setImageBitmap(bitmap);
                                fileUri2 = getImageUri(getApplicationContext(), bitmap);

                                break;

                            case 3:
                                bitmap = MediaStore.Images.Media.getBitmap(UploadImageActivity.this.getContentResolver(), data.getData());
                                imageView = (ImageView) findViewById(R.id.img_licBack);
                                imageView.setImageBitmap(bitmap);
                                fileUri3 = getImageUri(getApplicationContext(), bitmap);

                                break;

                            case 4:
                                bitmap = MediaStore.Images.Media.getBitmap(UploadImageActivity.this.getContentResolver(), data.getData());
                                imageView = (ImageView) findViewById(R.id.img_licFront);
                                imageView.setImageBitmap(bitmap);
                                fileUri4 = getImageUri(getApplicationContext(), bitmap);

                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(UploadImageActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    void manualRegister() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Register");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String url = "PUT YOUR URL HERE";

//(“full_name”, “email”, “password”, “phone_number”, “hospital_name”, “university”, “clinc_address”, “date_of_birth”, “governorate”, “gender”, “id_pic”  (File), “id_pic_back”  (File),
//“license_pic”  (File), “license_pic_back”  (File))

        Ion.with(UploadImageActivity.this)
                .load("POST", url)
                .setMultipartParameter("full_name", bundle.getString(getString(R.string.fullname_key)))
                .setMultipartParameter("email", bundle.getString(getString(R.string.email_key)))
                .setMultipartParameter("password", bundle.getString(getString(R.string.password_key)))
                .setMultipartParameter("phone_number", bundle.getString(getString(R.string.mobile_key)))
                .setMultipartParameter("hospital_name", bundle.getString(getString(R.string.hospital_key)))
                .setMultipartParameter("university", bundle.getString(getString(R.string.university_key)))
                .setMultipartParameter("clinc_address", bundle.getString(getString(R.string.clinic_key)))
                .setMultipartParameter("date_of_birth", bundle.getString(getString(R.string.bdate_key)))
                .setMultipartParameter("governorate", bundle.getString(getString(R.string.gov_key)))
                .setMultipartParameter("gender", bundle.getString(getString(R.string.gender_key)))
                .setMultipartFile("id_pic", "id_pic/jpeg", new File(getRealPathFromURI(fileUri1)))
                .setMultipartFile("id_pic_back", "id_pic_back/jpeg", new File(getRealPathFromURI(fileUri2)))
                .setMultipartFile("license_pic", "license_pic/jpeg", new File(getRealPathFromURI(fileUri3)))
                .setMultipartFile("license_pic_back", "license_pic_back/jpeg", new File(getRealPathFromURI(fileUri4)))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onCompleted(Exception e, String result) {
                        //check error
                        progressDialog.dismiss();
                        if (e != null) {
                            Log.e("ERROR", "" + e.getMessage());
                        } else {
                            Log.i("REGISTER_RESULT", result);

                        }
                    }
                });

    }

    void socialRegister() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Register");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String url = "PUT YOUR URL HERE";
//(“full_name”, “email”, “password”, “phone_number”, “hospital_name”, “university”, “clinc_address”, “date_of_birth”, “governorate”, “gender”, “id_pic”  (File), “id_pic_back”  (File),
//“license_pic”  (File), “license_pic_back”  (File), “social_id”, “social_type”  (Facebook), ”social_image”)
        Ion.with(UploadImageActivity.this)
                .load("POST", url)
                .setMultipartParameter("full_name", bundle.getString(getString(R.string.fullname_key)))
                .setMultipartParameter("email", bundle.getString(getString(R.string.email_key)))
                .setMultipartParameter("password", bundle.getString(getString(R.string.password_key)))
                .setMultipartParameter("phone_number", bundle.getString(getString(R.string.mobile_key)))
                .setMultipartParameter("hospital_name", bundle.getString(getString(R.string.hospital_key)))
                .setMultipartParameter("university", bundle.getString(getString(R.string.university_key)))
                .setMultipartParameter("clinc_address", bundle.getString(getString(R.string.clinic_key)))
                .setMultipartParameter("date_of_birth", bundle.getString(getString(R.string.bdate_key)))
                .setMultipartParameter("governorate", bundle.getString(getString(R.string.gov_key)))
                .setMultipartParameter("gender", bundle.getString(getString(R.string.gender_key)))
                .setMultipartFile("id_pic", "id_pic/jpeg", new File(getRealPathFromURI(fileUri1)))
                .setMultipartFile("id_pic_back", "id_pic_back/jpeg", new File(getRealPathFromURI(fileUri2)))
                .setMultipartFile("license_pic", "license_pic/jpeg", new File(getRealPathFromURI(fileUri3)))
                .setMultipartFile("license_pic_back", "license_pic_back/jpeg", new File(getRealPathFromURI(fileUri4)))
                .setMultipartParameter("social_id", bundle.getString(getString(R.string.gender_key)))
                .setMultipartParameter("social_type", bundle.getString(getString(R.string.gender_key)))
                .setMultipartParameter("social_image", bundle.getString(getString(R.string.gender_key)))

                .asString()
                .setCallback(new FutureCallback<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onCompleted(Exception e, String result) {
                        //check error
                        progressDialog.dismiss();
                        if (e != null) {
                            Log.e("ERROR", "" + e.getMessage());
                        } else {
                            Log.i("REGISTER_RESULT", result);
                            try {
                                Toast.makeText(UploadImageActivity.this, new JSONObject(result).getString("success"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }


                        }
                    }
                });

    }

    void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMSSION", "Permission is granted");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SELECT_IMAGE);
            }
        }
    }

}