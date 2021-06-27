package com.example.diseaseprediction.adapter;

import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private FirebaseUser fUser;

    private Context mContext;
    private List<Message> mMessage;
    //private List<String> mSymptom;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private String imgURL;
    private ArrayAdapter<String> symptomAdapter;


    public ChatAdapter(@NonNull Context context, List<Message> mMessage) {
        this.mContext = context;
        this.mMessage = mMessage;
//        this.mSymptom = mSymptom;
//        this.imgURL = imgURL;
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
     * @param listView
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
        if (msg.getListSymptom() != null) {

            //Set symptom adapter
            if (holder.item_chat_layout_checkbox != null) {
                if (msg.getListSymptom() != null) {
                    System.out.println("Quang k null");
                    //Set layout checkbox visible
                    holder.item_chat_layout_checkbox.setVisibility(View.VISIBLE);
                    //Create new adapter
                    symptomAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_multiple_choice, msg.getListSymptom());
                    holder.item_chat_checkbox.setAdapter(symptomAdapter);
                    //Set height of checkbox
                    setListViewHeightBasedOnChildren(holder.item_chat_checkbox);

                    //Set onclick in item
                    holder.item_chat_checkbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //Get item value checked
                            String value = holder.item_chat_checkbox.getItemAtPosition(i).toString();

                            //Button done clicked
                            holder.item_chat_checkbox_done.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // do something
                                }
                            });
                        }
                    });
                } else {
                    //Layout checkbox gone
                    holder.item_chat_layout_checkbox.setVisibility(View.GONE);
                }

            }
        }

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
