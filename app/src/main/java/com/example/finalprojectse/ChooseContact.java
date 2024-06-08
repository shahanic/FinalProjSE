package com.example.finalprojectse;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class ChooseContact extends AppCompatActivity {
    TextInputLayout edt_name, edt_num;
    Button add, contacts;
    Database db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_contact);
        edt_name = findViewById(R.id.et8);
        edt_num = findViewById(R.id.et9);
        add = findViewById(R.id.b3);
        contacts = findViewById(R.id.b4);
        db = new Database(this);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt_name.getEditText().getText().toString().trim();
                String number = edt_num.getEditText().getText().toString().trim();
                if (edt_name.getEditText().length() != 0 && !checkPhoneNumber() && !isCheckContactExist(number)) {
                    boolean isInserted = db.insertData(name, number);
                    if (isInserted) {
                        edt_name.getEditText().setText(null);
                        edt_num.getEditText().setText(null);
                        Toast.makeText(ChooseContact.this, "Contact added successfully!", Toast.LENGTH_SHORT).show();
                        edt_name.requestFocus();
                    } else
                        Toast.makeText(ChooseContact.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(ChooseContact.this, "Please enter correct data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean checkPhoneNumber(){
        String number = edt_num.getEditText().getText().toString().trim();
        if(number.length() != 10){
            edt_num.setError("Invalid Number");
            edt_num.requestFocus();
            return true;
        } else {
            return false;
        }
    }

    private boolean isCheckContactExist(String phoneNo){
        if(db.getContact(phoneNo) != 0) {
            edt_num.setError("Contact Number Already Exists");
            edt_num.requestFocus();
            return true;
        }
        return false;
    }
}
