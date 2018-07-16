package com.example.apple.codequiz;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.text.LoginFilter;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.apple.codequiz.Tools.Common;
import com.example.apple.codequiz.Tools.Pojo.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    //Constants
    private static final String ROOT_USERS_REFERENCE = "Users";
    private static final String REMEMBER_TAG = "remember";
    private static final String SAVED_LOGIN_TAG = "savedlogin";
    private static final String SAVED_PASSWORD_TAG = "savedpassword";
    private static final String EMPTY_FIELD = "";

    private static final int NOTIFICATION_HOUR = 12;
    private static final int NOTIFICATION_MINUTE = 15;
    private static final int NOTIFICATION_SECOND = 0;

    private static final int PENDING_INTENT_REQUEST_CODE = 0;

    //Tool fields
    Boolean remember;
    String savedLogin;
    String savedPassword;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    //Text Fields
    @BindView(R.id.et_user_name)
    MaterialEditText etUserName;

    @BindView(R.id.et_user_password)
    MaterialEditText etUserPassword;

    //Buttons
    @BindView(R.id.btn_sign_in)
    Button signInBtn;

    @BindView(R.id.btn_sign_up)
    Button signUpBtn;

    //CheckBox
    @BindView(R.id.remember_btn)
    AppCompatCheckBox rememberBtn;

    //Layout and ProgressBar
    @BindView(R.id.root_layout)
    LinearLayout rootLayout;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //register notification receiver
        //registerNotification();

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(ROOT_USERS_REFERENCE);

        ButterKnife.bind(this);

        initData();
    }

    private void registerNotification() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, NOTIFICATION_HOUR);
        calendar.set(Calendar.MINUTE, NOTIFICATION_MINUTE);
        calendar.set(Calendar.SECOND, NOTIFICATION_SECOND);

        Intent notificationReceiver = new Intent(MainActivity.this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,
                PENDING_INTENT_REQUEST_CODE,
                notificationReceiver,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent);
    }

    @OnCheckedChanged(R.id.remember_btn)
    public void onChangeChecked(AppCompatCheckBox checkBox) {
        remember = checkBox.isChecked();
    }

    @OnClick(R.id.btn_sign_in)
    public void OnSignIn(View view) {
        signIn();
    }

    @OnClick(R.id.btn_sign_up)
    public void onSignUp(View view) {
        signUp();
    }

    private void initData() {
       loadData();
       rememberBtn.setChecked(remember);
       if (remember && !savedLogin.equals(EMPTY_FIELD) && !savedPassword.equals(EMPTY_FIELD)){
           rootLayout.setVisibility(View.GONE);
           progressBar.setVisibility(View.VISIBLE);
           signIn();
       } else {
           rootLayout.setVisibility(View.VISIBLE);
           progressBar.setVisibility(View.GONE);
       }
    }

    private void signIn() {
        final String name;
        final String password;

        if (remember && !savedLogin.equals(EMPTY_FIELD) && !savedPassword.equals(EMPTY_FIELD)) {
            name = savedLogin;
            password = savedPassword;
        } else {
            name = etUserName.getText().toString();
            password = etUserPassword.getText().toString();
        }

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(name).exists()) {
                    if (!name.isEmpty()) {
                        User user = dataSnapshot.child(name).getValue(User.class);
                        if (user.getPassword().equals(password)){
                            savedLogin = name;
                            savedPassword = password;

                            Common.NAME= name;
                            Common.userGlobalResult = user.getScore() * -1;
                            Toast.makeText(MainActivity.this,"Logged In Successfully",Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(MainActivity.this,HomeActivity.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        } else {
                            Toast.makeText(MainActivity.this,"Wrong password", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this,"No such user, please sign up",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void signUp() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setIcon(R.drawable.ic_account)
                .setTitle("Sign Up")
                .setMessage("Please fill fields")
                .setCancelable(false);
        //inflate view
        View signUpLayout = getLayoutInflater().inflate(R.layout.sign_up_layout,null);

        MaterialEditText etNewUserName = signUpLayout.findViewById(R.id.et_new_user_name);
        MaterialEditText etNewUserPassword = signUpLayout.findViewById(R.id.et_new_user_password);
        MaterialEditText etnewUserEmail = signUpLayout.findViewById(R.id.et_new_user_email);

        alertDialog.setView(signUpLayout);

        alertDialog.setPositiveButton("Confirm", ((dialogInterface, i) -> {
            String name = etNewUserName.getText().toString();
            String password = etNewUserPassword.getText().toString();
            String email = etnewUserEmail.getText().toString();

            if (!email.equals("") && !name.equals("") && !password.equals("")) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(name).exists()) {
                            Toast.makeText(MainActivity.this,"This name is already exist",Toast.LENGTH_SHORT).show();
                        }else {
                            User user = new User(name,password,email,0);
                            databaseReference.child(user.getName())
                                    .setValue(user);
                            Toast.makeText(MainActivity.this,"Successful sign Up",Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else
                Toast.makeText(MainActivity.this,"Fields can't be empty",Toast.LENGTH_SHORT).show();
        }));

        alertDialog.setNegativeButton("Cancel", ((dialogInterface, i) -> {
            dialogInterface.dismiss();
        }));

        //build and show
        alertDialog
                .create()
                .show();
    }

    private void saveData() {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean(REMEMBER_TAG,remember);
        editor.putString(SAVED_LOGIN_TAG,savedLogin);
        editor.putString(SAVED_PASSWORD_TAG, savedPassword);

        editor.apply();
    }

    private void loadData() {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);

        remember = sp.getBoolean(REMEMBER_TAG,true);
        savedLogin = sp.getString(SAVED_LOGIN_TAG,EMPTY_FIELD);
        savedPassword = sp.getString(SAVED_PASSWORD_TAG,EMPTY_FIELD);

        Toast.makeText(MainActivity.this,"Hi, " + savedLogin ,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }
}
