package peter.com.task;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {
    Spinner genderSpinner;
    EditText fullNameEditText, emailEditText, mobileEditText, hospitalEditTextEditText, universityEditText, clinicAddressEditText, birthdateEditText, governmentEditText, passwordEditText, confirmPasswordEditText;
    Bundle bundle, oldBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        bundle = new Bundle();
        oldBundle = getIntent().getExtras();

        initControls();
        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                RadioGroup genderRadioGroup = (RadioGroup) findViewById(R.id.rad_gender);

//
//                fullNameEditText.setText(getString(R.string.fullname_key));
//                emailEditText.setText("petershawki4@gmail.com");
//                mobileEditText.setText("01271341907");
//                hospitalEditTextEditText.setText(getString(R.string.fullname_key));
//                universityEditText.setText(getString(R.string.fullname_key));
//                clinicAddressEditText.setText(getString(R.string.fullname_key));
//                governmentEditText.setText(getString(R.string.fullname_key));
//                passwordEditText.setText(getString(R.string.password_key));
//                confirmPasswordEditText.setText(getString(R.string.password_key));
////                birthdateEditText.setText("24/12/1995"); wrong format !!!
//                birthdateEditText.setText("1995-12-12");

                if (passwordEditText.getText().length() > 0 && passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) {
                    Intent intent = new Intent(SignupActivity.this, UploadImageActivity.class);
                    bundle.putString(getString(R.string.fullname_key), fullNameEditText.getText().toString());
                    bundle.putString(getString(R.string.email_key), emailEditText.getText().toString());
                    bundle.putString(getString(R.string.mobile_key), mobileEditText.getText().toString());
                    bundle.putString(getString(R.string.hospital_key), hospitalEditTextEditText.getText().toString());
                    bundle.putString(getString(R.string.university_key), universityEditText.getText().toString());
                    bundle.putString(getString(R.string.clinic_key), clinicAddressEditText.getText().toString());
                    bundle.putString(getString(R.string.bdate_key), birthdateEditText.getText().toString());
                    bundle.putString(getString(R.string.gov_key), governmentEditText.getText().toString());
                    bundle.putString(getString(R.string.password_key), passwordEditText.getText().toString());
                    bundle.putString(getString(R.string.gender_key), String.valueOf(genderSpinner.getSelectedItemPosition()));
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else
                    Toast.makeText(SignupActivity.this, "Password not matched", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void initControls() {

        fullNameEditText = (EditText) findViewById(R.id.edt_fullname);
        emailEditText = (EditText) findViewById(R.id.edt_email);
        mobileEditText = (EditText) findViewById(R.id.edt_mobile);
        hospitalEditTextEditText = (EditText) findViewById(R.id.edt_hospital);
        universityEditText = (EditText) findViewById(R.id.edt_univ);
        clinicAddressEditText = (EditText) findViewById(R.id.edt_clinic_address);
        birthdateEditText = (EditText) findViewById(R.id.edt_birthdate);
        governmentEditText = (EditText) findViewById(R.id.edt_government);
        passwordEditText = (EditText) findViewById(R.id.edt_password);
        confirmPasswordEditText = (EditText) findViewById(R.id.edt_conf_password);

        ArrayList<String> genders = new ArrayList<>();
        genders.add(getString(R.string.male));
        genders.add(getString(R.string.female));
        genderSpinner = (Spinner) findViewById(R.id.spn_gender);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        genders); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        genderSpinner.setAdapter(spinnerArrayAdapter);

        if (oldBundle.getString(getString(R.string.LOGIN_TYPE)).equals(getString(R.string.social))) {
            fullNameEditText.setText(oldBundle.getString(getString(R.string.display_name_key)));
            emailEditText.setText(oldBundle.getString(getString(R.string.email_key)));
            bundle.putString(getString(R.string.social_type_key), oldBundle.getString(getString(R.string.social_type_key)));
            bundle.putString(getString(R.string.social_image_key), oldBundle.getString(getString(R.string.social_image_key)));
            bundle.putString(getString(R.string.LOGIN_TYPE), getString(R.string.social));
        } else {
            bundle.putString(getString(R.string.LOGIN_TYPE), getString(R.string.manual));
        }
    }


}
