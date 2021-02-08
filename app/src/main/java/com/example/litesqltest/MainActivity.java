package com.example.litesqltest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    // references to buttons and other control on the layout
    Button btn_add, btn_viewAll;
    EditText et_name, et_age;
    Switch sw_activeCustomer;
    ListView lv_customerList;

    ArrayAdapter  customerArrayAdapter;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    btn_add = findViewById(R.id.btn_add);
    btn_viewAll = findViewById(R.id.btn_viewAll);
    et_name = findViewById(R.id.et_name);
    et_age = findViewById(R.id.et_age);
    sw_activeCustomer = findViewById(R.id.sw_activeCustomer);
    lv_customerList = findViewById(R.id.lv_customerList);


    dataBaseHelper = new DataBaseHelper(MainActivity.this);
    ShowCustomerOnListView(dataBaseHelper);

    btn_add.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){

            CustomerModel customerModel;
            try{
                //Line below crashes with age field is left empty - error (java.lang.NumberFormatException: For input string: ""
                customerModel  = new CustomerModel(-1, et_name.getText().toString(), Integer.parseInt(et_age.getText().toString()), sw_activeCustomer.isChecked());
                Toast.makeText(MainActivity.this, customerModel.toString(), Toast.LENGTH_SHORT).show();
            }
            catch(Exception e) {
                Toast.makeText(MainActivity.this, "Error creating Customer", Toast.LENGTH_SHORT).show();
                customerModel = new CustomerModel(-1, "error",0,false);


            }
            DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
            //Parse integer and change to string with Integer.parseInt(et_age.getText().toString());

            boolean success = dataBaseHelper.addOne(customerModel);

            Toast.makeText(MainActivity.this, "Success= " + success, Toast.LENGTH_SHORT).show();
            ShowCustomerOnListView(dataBaseHelper);
        }
    });
    btn_viewAll.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){

            DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);

            ShowCustomerOnListView(dataBaseHelper);

            //Toast.makeText(MainActivity.this, everyone.toString(), Toast.LENGTH_SHORT).show();
        }
    });

        lv_customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerModel clickedCustomer = (CustomerModel) parent.getItemAtPosition(position);
                dataBaseHelper.deleteOne(clickedCustomer);
                ShowCustomerOnListView(dataBaseHelper);
                Toast.makeText(MainActivity.this, "Deleted" + clickedCustomer.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowCustomerOnListView(DataBaseHelper dataBaseHelper2) {
        customerArrayAdapter = new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper2.getEveryone());
        lv_customerList.setAdapter((customerArrayAdapter));
    }

}