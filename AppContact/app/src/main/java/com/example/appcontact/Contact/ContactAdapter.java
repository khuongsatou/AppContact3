package com.example.appcontact.Contact;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcontact.MainActivity;
import com.example.appcontact.R;

import java.util.List;



public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder>{

    private List<Contact> contactList;
    private Context context;
    private MainActivity mainActivity;


    public ContactAdapter(Context context,List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
        this.mainActivity = (MainActivity) context;
    }

    @NonNull
    @Override
    public ContactAdapter.ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_list_contact,parent,false);
        return new ContactHolder(view,mainActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ContactHolder holder, int position) {
        holder.tvNameContact.setText(contactList.get(position).getName());
        holder.tvNumberContact.setText(contactList.get(position).getNumber());

        //khi chưa longclick

        if (!mainActivity.action_mode_delete){
            holder.cbDelete.setVisibility(View.GONE);
            holder.cbDelete.setChecked(false);
        }else{
            holder.cbDelete.setVisibility(View.VISIBLE);

        }


    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

    //remove những thằng đã selection
    public void UpdateAdapter(List<Contact> list){
        for (Contact contact : list){
            int id = mainActivity.db.SelectIDContact(contact.getName(),contact.getNumber());
            mainActivity.db.DeleteContact(id);
            //contactList.remove(contact);
        }
        mainActivity.RestartListContact();
        Toast.makeText(context,"Mode Mutiple Success",Toast.LENGTH_SHORT);
        //notifyDataSetChanged();
    }

    public class ContactHolder extends  RecyclerView.ViewHolder  implements View.OnClickListener{
        private TextView tvNameContact,tvNumberContact;
        private CheckBox cbDelete;
        private CardView cvItem;
        public ContactHolder(@NonNull View itemView, final MainActivity mainActivity) {
            super(itemView);
            tvNameContact = itemView.findViewById(R.id.tvNameContact);
            tvNumberContact = itemView.findViewById(R.id.tvNumberPhone);
            cvItem = itemView.findViewById(R.id.cvItem);
            cbDelete = itemView.findViewById(R.id.cbDelete);
            cbDelete.setOnClickListener(this);//nó sẽ gọi đến onclick bên dưới
            cvItem.setOnLongClickListener(mainActivity);//dùng 1 lần nên reference đến mainActivity
            cvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.ShowDialog(getAdapterPosition());
                }
            });
        }

        //dùng nhiều lần
        @Override
        public void onClick(View view) {
            if (mainActivity.action_mode_delete){
                mainActivity.PrepareSelection(view,getAdapterPosition());
            }

        }
    }
}
