package com.example.sqlite_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //references the buttons and other controls on the layout
    Button btn_add, btn_viewAll;
    EditText et_name, et_age;
    Switch sw_activeCustomer;
    ListView lv_customerList;
    ArrayAdapter customerModelArrayAdapter;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_add = findViewById(R.id.btn_add);
        btn_viewAll = findViewById(R.id.btn_viewAll);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        sw_activeCustomer = findViewById(R.id.sw_active);
        lv_customerList = findViewById(R.id.lv_customerList);

        databaseHelper = new DatabaseHelper(MainActivity.this);
        showCustomerOnListView(databaseHelper);

        //button listener for the add and view all buttons
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerModel customerModel;
                try {
                     customerModel = new CustomerModel(-1,
                            et_name.getText().toString(),
                            Integer.parseInt(et_age.getText().toString()),
                            sw_activeCustomer.isChecked());

                    Toast.makeText(MainActivity.this,
                            customerModel.toString(),
                            Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this,
                            "Error Creating a Customer bitch",
                            Toast.LENGTH_SHORT).show();

                    customerModel = new CustomerModel(-1,
                            "error",
                            0,
                            false);
                }

                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                boolean success = databaseHelper.addCustomer(customerModel);
                Toast.makeText(MainActivity.this, "success= "+ success, Toast.LENGTH_SHORT).show();
                showCustomerOnListView(databaseHelper);
            }
        });

        btn_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);

                showCustomerOnListView(databaseHelper);
                /* Toast.makeText(MainActivity.this,
                        wholeList.toString(),
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        lv_customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustomerModel clickedCustomer = (CustomerModel) adapterView.getItemAtPosition(i);
                databaseHelper.deleteCustomer(clickedCustomer);
                showCustomerOnListView(databaseHelper);
                Toast.makeText(MainActivity.this, clickedCustomer.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void showCustomerOnListView(DatabaseHelper databaseHelper2) {
        customerModelArrayAdapter = new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1, databaseHelper2.getAll());
        lv_customerList.setAdapter(customerModelArrayAdapter);
    }
}