package com.example.diseaseprediction.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diseaseprediction.R;
import com.example.diseaseprediction.object.Message;
import com.example.diseaseprediction.object.RecommendSymptom;
import com.example.diseaseprediction.object.Symptom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private FirebaseUser fUser;
    private DatabaseReference mRef;
    private DatabaseReference mRef2;

    private static final String LOG_TAG = "Chat adapter";
    private Context mContext;
    private List<Message> mMessage;
    private List<Symptom> mSymptom;
    private ArrayAdapter<Symptom> symptomAdapter;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private String imgURL;


    public ChatAdapter(@NonNull Context context, List<Message> mMessage) {
        this.mContext = context;
        this.mMessage = mMessage;
    }

    @NotNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Get view type. Right layout if sender, left layout if receiver
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_right, parent, false);
            return new ChatAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_left, parent, false);
            return new ChatAdapter.ViewHolder(view);
        }
    }

    /**
     * Set height of listview manual
     *
     * @param listView ListView
     */
    public static void setListViewHeightBasedOnChildren(final ListView listView) {
        listView.post(new Runnable() {
            @Override
            public void run() {
                ListAdapter listAdapter = listView.getAdapter();
                if (listAdapter == null) {
                    return;
                }
                int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
                int listWidth = listView.getMeasuredWidth();
                for (int i = 0; i < listAdapter.getCount(); i++) {
                    View listItem = listAdapter.getView(i, null, listView);
                    listItem.measure(
                            View.MeasureSpec.makeMeasureSpec(listWidth, View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    totalHeight += listItem.getMeasuredHeight();
                }
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = (totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() * 15)));
                listView.setLayoutParams(params);
                listView.requestLayout();
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        //Get message
        Message msg = mMessage.get(position);
        holder.item_chat_show_message.setText(msg.getMessage());
        //Get checkbox if message have a suggestion
        getCheckBox(msg, holder);

        //Custom date under message
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        String shortTimeStr = sdf.format(msg.getDateSend().getTime());
        holder.item_chat_time.setText(shortTimeStr);

        //On clicked in message then show time send
        holder.item_chat_layout_show_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.item_chat_time.getVisibility() == View.VISIBLE) {
                    holder.item_chat_time.setVisibility(View.GONE);
                } else {
                    holder.item_chat_time.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mMessage.size();
    }

    @Override
    public int getItemViewType(int position) {
        //Get type depend on user
        //Right if sender, Left if receiver
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mMessage.get(position).getSenderID().equals(fUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    /**
     * Create checkbox
     *
     * @param holder View holder
     * @param ls     List<Symptom>()
     */
    public void createCheckBox(ChatAdapter.ViewHolder holder, List<Symptom> ls, List<RecommendSymptom> rc, Message msg) {
        if (holder.item_chat_layout_checkbox != null) {
            if (ls.size() != 0) {
                //Set checkbox layout enable or disable
                if (msg.getStatus() == 2) {
                    holder.item_chat_checkbox.setEnabled(false);
                    holder.item_chat_checkbox_done.setEnabled(false);
                } else {
                    holder.item_chat_checkbox.setEnabled(true);
                    holder.item_chat_checkbox_done.setEnabled(true);
                }

                //Set layout checkbox visible
                holder.item_chat_layout_checkbox.setVisibility(View.VISIBLE);
                //Create new adapter
                symptomAdapter = new ArrayAdapter<Symptom>(mContext, android.R.layout.simple_list_item_multiple_choice, ls);
                holder.item_chat_checkbox.setAdapter(symptomAdapter);
                //Set height of checkbox
                setListViewHeightBasedOnChildren(holder.item_chat_checkbox);

                //Set item checked
                for (int i = 0; i < rc.size(); i++) {
                    if (rc.get(i).getStatus() == 1) {
                        for (int j = 0; j < holder.item_chat_checkbox.getAdapter().getCount(); j++) {
                            Symptom sm = (Symptom) holder.item_chat_checkbox.getItemAtPosition(j);
                            if (sm.getSymptomsID().equals(rc.get(i).getSymptomsID())) {
                                holder.item_chat_checkbox.setItemChecked(j, true);
                            }
                        }
                    }
                }

                //Set onclick in item
                holder.item_chat_checkbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //Get item value checked
                        Symptom value = (Symptom) holder.item_chat_checkbox.getItemAtPosition(i);

                    }
                });

                //Button done clicked
                holder.item_chat_checkbox_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SparseBooleanArray sparseBooleanArray = holder.item_chat_checkbox.getCheckedItemPositions();
                        String itemsSelected = "";

                        //Set check status in firebase as unchecked (status = 0)
                        for (int i = 0; i < rc.size(); i++) {
                            updateRecommendValue(rc.get(i).getMessageID(), rc.get(i).getSymptomsID(), 0);
                        }

                        //Then set check status that row have checked (status = 1)
                        for (int i = 0; i < sparseBooleanArray.size(); i++) {
                            int position = sparseBooleanArray.keyAt(i);
                            if (sparseBooleanArray.get(position)) {
                                System.out.println("checked position" + position);
                                //Get value of index checked
                                Symptom st = (Symptom) holder.item_chat_checkbox.getItemAtPosition(position);
                                for (int j = 0; j < rc.size(); j++) {
                                    if (rc.get(j).getSymptomsID().equals(st.getSymptomsID())) {
                                        updateRecommendValue(rc.get(j).getMessageID(), rc.get(j).getSymptomsID(), 1);
                                    }
                                }
                            }
                        }
                        //Set disable checkbox layout
                        mRef2 = FirebaseDatabase.getInstance().getReference("Message");
                        mRef2.child(msg.getMessageID()).child("status").setValue(2);
                    }
                });
            } else {
                //Layout checkbox gone
                holder.item_chat_layout_checkbox.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Get Checkbox if message have a suggestion
     *
     * @param msg    Message
     * @param holder ViewHolder
     */
    public void getCheckBox(Message msg, ChatAdapter.ViewHolder holder) {
        //List symptom
        mSymptom = new ArrayList<>();
        //List id of symptom (String type)
        List<String> tempSymptom = new ArrayList<>();
        List<RecommendSymptom> tempRecommend = new ArrayList<>();
        //Go to recommendSymptom
        mRef = FirebaseDatabase.getInstance().getReference("RecommendSymptom");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tempSymptom.clear();
                tempRecommend.clear();
                //Loop and get all value that equal to message ID
                for (DataSnapshot sn : snapshot.getChildren()) {
                    RecommendSymptom rcs = sn.getValue(RecommendSymptom.class);
                    if (rcs.getMessageID().equals(msg.getMessageID())) {
                        //Add symptomID to temp array
                        tempSymptom.add(rcs.getSymptomsID());
                        //tempRecommend list use for get status (checked or not)
                        tempRecommend.add(rcs);
                    }
                }

                //Get name of symptom
                mRef2 = FirebaseDatabase.getInstance().getReference("Symptom");
                mRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mSymptom.clear();
                        for (DataSnapshot sn : snapshot.getChildren()) {
                            for (String id : tempSymptom) {
                                Symptom st = sn.getValue(Symptom.class);
                                if (id.equals(st.getSymptomsID())) {
                                    //Add symptom to list symptom (Object)
                                    mSymptom.add(st);
                                }
                            }
                        }
                        //Create checkbox layout
                        createCheckBox(holder, mSymptom, tempRecommend, msg);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Update recommendSymptom status to firebase
     *
     * @param messageID Message ID
     * @param symptomID Symptom ID
     * @param status    Status (0: Unchecked | 1: Checked)
     */
    private void updateRecommendValue(String messageID, String symptomID, int status) {
        mRef = FirebaseDatabase.getInstance().getReference("RecommendSymptom");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Loop and get all value that equal to message ID
                for (DataSnapshot sn : snapshot.getChildren()) {
                    RecommendSymptom rcs = sn.getValue(RecommendSymptom.class);
                    if (rcs.getMessageID().equals(messageID) && rcs.getSymptomsID().equals(symptomID)) {
                        mRef.child(sn.getKey()).child("status").setValue(status);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item_chat_show_message, item_chat_time;
        public LinearLayout item_chat_layout_show_message, item_chat_layout_checkbox;
        public ListView item_chat_checkbox;
        public Button item_chat_checkbox_done;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Find view
            item_chat_show_message = itemView.findViewById(R.id.item_chat_show_message);
            item_chat_time = itemView.findViewById(R.id.item_chat_time);
            item_chat_layout_show_message = itemView.findViewById(R.id.item_chat_layout_show_message);
            item_chat_layout_checkbox = itemView.findViewById(R.id.item_chat_layout_checkbox);
            item_chat_checkbox_done = itemView.findViewById(R.id.item_chat_checkbox_done);
            item_chat_checkbox = itemView.findViewById(R.id.item_chat_checkbox);
        }
    }
}
