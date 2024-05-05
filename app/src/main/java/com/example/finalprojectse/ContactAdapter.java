package com.example.finalprojectse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private ArrayList<Contact> contactsList;
    private Context context;

    public ContactAdapter(Context context, ArrayList<Contact> contactsList) {
        super(context, 0, contactsList);
        this.contactsList = contactsList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
        }

        Contact currentContact = contactsList.get(position);

        TextView nameTextView = listItem.findViewById(R.id.nameTextView);
        nameTextView.setText(currentContact.getName());

        TextView phoneTextView = listItem.findViewById(R.id.phoneTextView);
        phoneTextView.setText(currentContact.getPhone());

        return listItem;
    }
}
