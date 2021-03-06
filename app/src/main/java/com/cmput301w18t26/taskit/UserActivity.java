/*
 * Copyright 2018, Team 26 CMPUT 301. University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under the terms and coditions fo the Code of Student Behaviour at the University of Alberta.
 */

package com.cmput301w18t26.taskit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by kevingordon on 2018-02-26.
 */

/**
 * Creates a new user or modifies user details.
 * Also allows user to view their profile once registered depending on the intent passed in
 */
public class UserActivity extends AppCompatActivity {

    private TaskItData db;

    protected static final String TYPE = "type";

    private EditText usernameEdit;
    private EditText emailEdit;
    private EditText phoneEdit;
    private EditText nameEdit;
    private TextView usernameText;
    private TextView nameText;
    private TextView emailText;
    private TextView phoneText;
//    private TextView invalidUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String type = intent.getStringExtra(HomeActivity.TYPE);
        //setTitle(type);
        setTitle(Html.fromHtml("<font color=#ffffff>" + type + "</font>"));
        db = TaskItData.getInstance();

        /**
         * Registers a new user, takes in the user inputted information
         * Allows user to register if the username doesn't already exist in database
         * Creates new User object
         */
        if (type.equals("Register")) {
            setContentView(R.layout.registeruser);
            usernameEdit = (EditText) findViewById(R.id.username1);
            emailEdit = (EditText) findViewById(R.id.email);
            phoneEdit = (EditText) findViewById(R.id.phone);
            nameEdit = (EditText) findViewById(R.id.name);
//            invalidUsername = (TextView) findViewById(R.id.invalid_username);
            Button actionButton = (Button) findViewById(R.id.confirmuser);
            Button cancelButton = (Button) findViewById(R.id.cancel);
            actionButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    String usernameInput = usernameEdit.getText().toString();
                    String nameInput = nameEdit.getText().toString();
                    String emailInput = emailEdit.getText().toString();
                    String phonestring = phoneEdit.getText().toString();
                     if (db.userExists(usernameInput)) {
                         Toast.makeText(UserActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
//                        invalidUsername.setText("Username already exists");
//                        invalidUsername.setVisibility(View.VISIBLE);
                    } else {
                        User user = new User();
                        if (usernameInput.equals("")) {
                            Log.i("UserActivity", "no username inserted");
                            Toast.makeText(UserActivity.this, "Please insert username", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (nameInput.equals("")) {
                            Log.i("UserActivity", "no name inserted");
                            Toast.makeText(UserActivity.this, "Please insert name", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (emailInput.equals("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                            Log.i("UserActivity", "bad email inserted");
                            Toast.makeText(UserActivity.this, "Please insert valid email", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (phonestring.length() != 10) {
                            Log.i("UserActivity", "wrong phone format");
                            Toast.makeText(UserActivity.this, "Please insert valid 10 digit phone number", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        user.setUsername(usernameInput);
                        user.setName(nameInput);
                        user.setEmail(emailInput);
                        long phoneInput = Long.parseLong(phonestring);
                        user.setPhone(phoneInput);
                        db.setCurrentUser(user);
                        db.addUser(user);
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    setResult(RESULT_OK);
                    finish();
                }
            });

        /**
         * Retrieves user details to update, and sets the text displayed to current details
         * so user can modify them.
         */
        } else if (type.equals("Update")) {
            setContentView(R.layout.edituser);
            Button actionButton = (Button) findViewById(R.id.confirmuser);
            Button cancelButton = (Button) findViewById(R.id.cancel);
            usernameText = (TextView) findViewById(R.id.username1);
            usernameText.setText(db.getCurrentUser().getUsername());
            emailEdit = (EditText) findViewById(R.id.email);
            emailEdit.setText(db.getCurrentUser().getEmail());
            phoneEdit = (EditText) findViewById(R.id.phone);
            phoneEdit.setText(String.valueOf(db.getCurrentUser().getPhone()));
            nameEdit = (EditText) findViewById(R.id.name);
            nameEdit.setText(db.getCurrentUser().getName());
            actionButton.setText(type);
            actionButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    db.getCurrentUser().setName(nameEdit.getText().toString());
                    db.getCurrentUser().setEmail(emailEdit.getText().toString());
                    db.getCurrentUser().setPhone(Long.parseLong(phoneEdit.getText().toString()));
                    db.updateUser(db.getCurrentUser());
                    setResult(RESULT_OK);
                    finish();
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    setResult(RESULT_OK);
                    finish();
                }
            });
        } else {
            setContentView(R.layout.viewuser);
            Button editButton = (Button) findViewById(R.id.edit);
            usernameText = (TextView) findViewById(R.id.update_username);
            nameText = (TextView) findViewById(R.id.update_name);
            emailText = (TextView) findViewById(R.id.update_email);
            phoneText = (TextView) findViewById(R.id.update_phone);
            RatingBar userRating = (RatingBar) findViewById(R.id.userrating);
            TextView reviewCount = (TextView) findViewById(R.id.reviewcount);
            final User user;

            if (type.equals("My Profile")) {
                usernameText.setText(db.getCurrentUser().getUsername());
                nameText.setText(db.getCurrentUser().getName());
                emailText.setText(db.getCurrentUser().getEmail());
                phoneText.setText(String.valueOf(db.getCurrentUser().getPhone()));
                user = db.getCurrentUser();
            } else if (type.equals("Other User")) {
                String userString = intent.getStringExtra("User");
                user = db.getUserByUsername(userString);
                usernameText.setText(user.getUsername());
                nameText.setText(user.getName());
                emailText.setText(user.getEmail());
                phoneText.setText(String.valueOf(user.getPhone()));
                editButton.setVisibility(View.GONE);
            } else {
                Log.e("UserActivity", "THIS SHOULDN'T HAPPEN, LOOK INTO IT...");
                user = new User();
            }

            reviewCount.setText(Integer.toString(user.getRatings().size()));
            userRating.setRating(user.getRatingsAverage());

            userRating.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        Intent reviewDescriptionIntent = new Intent(getApplicationContext(), ReviewDescriptionActivity.class);
                        reviewDescriptionIntent.putExtra("Username", user.getUsername());
                        startActivity(reviewDescriptionIntent);
                        setResult(RESULT_OK);
                    }
                    return true;
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Intent updateIntent = new Intent(getApplicationContext(), UserActivity.class);
                    updateIntent.putExtra(TYPE, "Update");
                    startActivity(updateIntent);
                    setResult(RESULT_OK);
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String type = intent.getStringExtra(HomeActivity.TYPE);
        if (type.equals("My Profile")) {
            usernameText.setText(db.getCurrentUser().getUsername());
            nameText.setText(db.getCurrentUser().getName());
            emailText.setText(db.getCurrentUser().getEmail());
            phoneText.setText(String.valueOf(db.getCurrentUser().getPhone()));
        }
    }
}
