package com.example.appcontact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcontact.Contact.Contact;
import com.example.appcontact.Contact.ContactAdapter;
import com.example.appcontact.Database.ContactReader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener{

    public ContactReader db;
    private Toolbar tbMenu;
    private TextView tvCounter;

    private RecyclerView rvList;
    private Switch sLayout;
    private FloatingActionButton fabAdd;

    private List<Contact> listContact;
    private List<Contact> listContact_Selection = new ArrayList<>();
    private ContactAdapter contactAdapter;

    public boolean action_mode_delete = false;
    private int Counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Innit();

    }

    private void Innit() {
        CreateDB();
        Radiation();
        SetToolbar();
        CreateAdapter();
        Event();
    }

    private void CreateDB() {
        db = new ContactReader(MainActivity.this);
        db.CreateDB();
    }

    private void SetToolbar() {
        tvCounter.setVisibility(View.GONE);
        setSupportActionBar(tbMenu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_toolbar_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void Event() {
        sLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sLayout.isChecked()){
                    rvList.setLayoutManager( new GridLayoutManager(MainActivity.this,2));
                    ClearActionMode();
                }else{
                    rvList.setLayoutManager( new LinearLayoutManager(MainActivity.this));
                    ClearActionMode();
                }
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogCustom();
            }
        });

    }

    private void ShowDialogCustom() {
        final Dialog dialog = new Dialog(MainActivity.this);

        //Delete title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set view
        dialog.setContentView(R.layout.custom_alertdialog);

        //Radiation dialog
        final EditText edtContactName = dialog.findViewById(R.id.edtContactName);
        final EditText edtContactNumber =  dialog.findViewById(R.id.edtContactNumber);
        Button btnClose =  dialog.findViewById(R.id.btnClose);
        Button btnSave =  dialog.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtContactName.getText().toString();
                String number = edtContactNumber.getText().toString();

                //add array fist ArrayList
                //listContact.add(0,new Contact(name,number));
                long result = db.InsertContact(new Contact(name,number));
                if (result > 0){
                    //Update status
                    RestartListContact();
                    Toast.makeText(MainActivity.this,"Save Success",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"Save Fail",Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void CreateAdapter() {
        rvList.setHasFixedSize(true);
        listContact = new ArrayList<Contact>();

        //add vào mảng
//        for (int i = 0 ; i< 10 ; i++) {
//            listContact.add(new Contact("Heo " + i, "035624196" + i));
//        }

        listContact.addAll(db.GetAllContact());
        //array reverse 0-> 9 to 9->0
        Collections.reverse(listContact);

        rvList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        contactAdapter = new ContactAdapter(MainActivity.this,listContact);
        rvList.setAdapter(contactAdapter);
    }

    private void Radiation() {
        rvList = findViewById(R.id.rvList);
        sLayout =  findViewById(R.id.sLayout);
        fabAdd= findViewById(R.id.fabAdd);
        tbMenu = findViewById(R.id.tbMenu);
        tvCounter = findViewById(R.id.tvCounter);
    }

    @Override
    public void onBackPressed() {
        if (action_mode_delete){
            ClearActionMode();
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //default
        if (item.getItemId() == android.R.id.home){
            ClearActionMode();
            contactAdapter.notifyDataSetChanged();
        }
        if (item.getItemId() == R.id.item_toolbar_delete){
            ContactAdapter adapter_temp = contactAdapter;
            adapter_temp.UpdateAdapter(listContact_Selection);
            ClearActionMode();
        }
        return true;
    }

    public void ClearActionMode(){
        //switch toolbar
        tbMenu.getMenu().clear();
        tbMenu.inflateMenu(R.menu.custom_toolbar_home);
        tvCounter.setVisibility(View.GONE);
        tvCounter.setText(" 0 items Selected");
        Counter = 0;
        //remove back
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //switch mode delete
        action_mode_delete = false;
        contactAdapter.notifyDataSetChanged();

    }


    //chỉ dùng 1 lần nên để ở hàm main
    @Override
    public boolean onLongClick(View view) {
        tbMenu.getMenu().clear();
        tbMenu.inflateMenu(R.menu.custom_toolbar_mode_multiple_delete);
        //show button back press in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        action_mode_delete = true;
        contactAdapter.notifyDataSetChanged();
        tvCounter.setVisibility(View.VISIBLE);
        return true;
    }

    public void PrepareSelection(View v , int position){
        if (((CheckBox)v).isChecked()){
            listContact_Selection.add(listContact.get(position));
            Counter++;
            UpdateCounter(Counter);
        }else{
            listContact_Selection.remove(listContact.get(position));
            Counter--;
            UpdateCounter(Counter);
        }
    }

    private void UpdateCounter(int counter) {
        if (Counter == 0){
            tvCounter.setText("0 items Selected");
        }else{
            tvCounter.setText(counter + " items Selected");
        }
    }



    public void ShowDialog(int position) {
        if (action_mode_delete){
            return;
        }
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_items_show);

        final int i = position;
        final EditText edtName =  dialog.findViewById(R.id.edtContactName);
        final EditText edtNumber =  dialog.findViewById(R.id.edtContactNumber);
        Button btnSave =  dialog.findViewById(R.id.btnSave);
        Button btnDelete =  dialog.findViewById(R.id.btnDelete);

        edtName.setText(listContact.get(i).getName());
        edtNumber.setText(listContact.get(i).getNumber());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String number = edtNumber.getText().toString();
                int id = db.SelectIDContact(listContact.get(i).getName(),listContact.get(i).getNumber());
                long result = db.UpdateContact(id,new Contact(name,number));
                if (result > 0){
                    RestartListContact();
                    Toast.makeText(MainActivity.this,"Save Success",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"Save Fail",Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int id = db.SelectIDContact(listContact.get(i).getName(),listContact.get(i).getNumber());
                long result = db.DeleteContact(id);
                if (result > 0){
                    RestartListContact();
                    Toast.makeText(MainActivity.this,"Delete Success",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"Delete Fail",Toast.LENGTH_SHORT).show();
                }

                //listContact.remove(i);
                //Toast.makeText(MainActivity.this,"Delete Success",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void RestartListContact(){
        listContact.clear();
        listContact.addAll(db.GetAllContact());
        Collections.reverse(listContact);
        contactAdapter.notifyDataSetChanged();
    }

}
