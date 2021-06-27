package com.example.diseaseprediction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diseaseprediction.adapter.testAdapter;
import com.example.diseaseprediction.object.Account;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class test extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH = 10;
    FirebaseUser firebaseUser;
    DatabaseReference myRef;

    private List<String> mSymptom;
    private ArrayAdapter<String> symptomAdapter;


    private TextInputLayout testlayout, testlayout2;
    private AutoCompleteTextView spinnerr;
    EditText editText;
    private RecyclerView recyclerView;
    private testAdapter userAdapter;
    private List<Account> mUser;
    Button btn, btn2;
    TextView txt;
    ImageView imageView2;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    //Firebase
    StorageReference sRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mSymptom = new ArrayList<>();
        mSymptom.add("aaa");
        mSymptom.add("afsda");
        mSymptom.add("aaavxcvxc");
        mSymptom.add("aaa");
        mSymptom.add("afsda");
        mSymptom.add("aaavxcvxc");
        mSymptom.add("aaa");
        mSymptom.add("afsda");
        mSymptom.add("aaavxcvxc");
        mSymptom.add("aaa");
        mSymptom.add("afsda");
        mSymptom.add("aaavxcvxc");
        mSymptom.add("aaa");
        mSymptom.add("afsda");
        mSymptom.add("aaavxcvxc");
        mSymptom.add("aaa");
        mSymptom.add("afsda");
        mSymptom.add("aaavxcvxc");
        mSymptom.add("aaa");
        mSymptom.add("afsda");
        mSymptom.add("aaavxcvxc");
        mSymptom.add("aaa");
        mSymptom.add("afsda");
        mSymptom.add("aaavxcvxc");
        mSymptom.add("aaa");
        mSymptom.add("afsda");
        mSymptom.add("aaavxcvxc");
        mSymptom.add("aaa");
        mSymptom.add("afsda");
        mSymptom.add("aaavxcvxc");
        mSymptom.add("aaa");
        mSymptom.add("afsda");
        mSymptom.add("aaavxcvxc");
        mSymptom.add("aaa");
        mSymptom.add("afsda");
        mSymptom.add("vvvvvvvvvvvvvvvvvvvvvvvvvv");
        ListView ls = findViewById(R.id.item_chat_checkbox);
        symptomAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, mSymptom);
        ls.setAdapter(symptomAdapter);


//        btn = findViewById(R.id.btn1);
//        imageView2 = findViewById(R.id.imageView2);
//        btn2 = findViewById(R.id.button);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//            }
//        });
//
//        btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                uploadImage();
//            }
//        });


//        addDataSpecialization(new DoctorSpecialization("1","Respiratory",new Date(),new Date(),1));
//        addDataSpecialization(new DoctorSpecialization("1","Hepatology",new Date(),new Date(),1));
//        addDataSpecialization(new DoctorSpecialization("1","Polyclinic",new Date(),new Date(),1));

//        testlayout = findViewById(R.id.testlayout);
//        testlayout2 = findViewById(R.id.testlayout2);
//
//        spinnerr = findViewById(R.id.spinnerr);
//        ArrayList<String> gender = new ArrayList<String>();
//        gender.add(getString(R.string.default_choose_gender));
//        gender.add(getString(R.string.default_gender_male));
//        gender.add(getString(R.string.default_gender_female));
//        ArrayAdapter genderAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, gender);
//        spinnerr.setAdapter(genderAdapter);
//        spinnerr.setText(spinnerr.getAdapter().getItem(0).toString());
//        genderAdapter.getFilter().filter(null);

//        recyclerView = findViewById(R.id.recycler);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

//        mUser = new ArrayList<>();
//      txt = findViewById(R.id.txt1);
//


//
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView2.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            sRef = FirebaseStorage.getInstance().getReference().child("images/"+ UUID.randomUUID().toString());
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(test.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(test.this, uri.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(test.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
//    public void getSpeechInput(View view) {
//
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, REQUEST_CODE_SPEECH);
//        } else {
//            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case REQUEST_CODE_SPEECH:
//                if (resultCode == RESULT_OK && data != null) {
//                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    txt.setText(result.get(0));
//                }
//                break;
//        }
//    }

//    void addDataSpecialization(DoctorSpecialization sp){
//        myRef = FirebaseDatabase.getInstance().getReference("Specialization");
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                sp.setSpecializationID(myRef.push().getKey());
//                myRef.child(sp.getSpecializationID()).setValue(sp);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    void addDataAdvise(Advise ad){
//        myRef = FirebaseDatabase.getInstance().getReference("Advise");
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                ad.setAdviseID(myRef.push().getKey());
//                myRef.child(ad.getAdviseID()).setValue(ad);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    void addDataMedicine(Medicine md){
//        myRef = FirebaseDatabase.getInstance().getReference("Medicine");
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                md.setMedicineID(myRef.push().getKey());
//                myRef.child(md.getMedicineID()).setValue(md);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    void addDataDisease(Disease ds){
//        myRef = FirebaseDatabase.getInstance().getReference("Disease");
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                ds.setDiseaseID(myRef.push().getKey());
//                myRef.child(ds.getDiseaseID()).setValue(ds);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    void addDataSymptom(Symptom sm){
//        myRef = FirebaseDatabase.getInstance().getReference("Sympton");
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                sm.setSymptomsID(myRef.push().getKey());
//                myRef.child(sm.getSymptomsID()).setValue(sm);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

//        addDataAdvise(new Advise("1", "che miệng trong lúc ho", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("2", "tham khảo ý kiến bác sĩ", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("3", "uống thuốc theo hướng dẫn của bác sĩ", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("4", "nghỉ ngơi điều độ", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("5", "ăn uống lành mạnh, khoa học", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("6", "theo dõi tình hình sức khỏe bản thân", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("7", "không uống rượu, bia", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("8", "tập luyện thể thao", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("9", "không hút thuốc lá", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("10", "ăn uống lành mạnh", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("11", "thường xuyên rữa tay", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("12", "tránh ăn đồ ăn có nhiều chất béo, cay, nóng...", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("13", "đến bệnh viện gần nhất để kiểm tra (đây là bệnh có tỉ lệ tử vong cao)", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("14", "tránh bị muỗi đốt", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("15", "uống đồ uống giàu vitamin c", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("16", "tránh đồ ăn, thức uống lạnh", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("17", "thực hiện các biện pháp giảm sốt", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("18", "đến ngay bệnh viện để kiểm tra tình trạng bệnh", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("19", "tránh tiếp xúc trực tiếp với mọi người xung quanh", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("20", "uống nước nhiều nước", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("21", "sử dụng các biện pháp giảm nhiệt độ", new Date(), new Date(), 1));

//        addDataSymptom(new Symptom("1",  "ho mãn tính",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("2",  "ho",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("3",  "sốt",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("4",  "ho ra máu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("5",  "sụt cân",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("6",  "buồn nôn",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("7",  "nhức đầu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("8",  "đau họng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("9",  "sổ mũi",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("10",  "ho khan",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("11",  "khó thở",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("12",  "thở nhanh",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("13",  "mệt mỏi",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("14",  "mất vị giác",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("15",  "mất khứ giác",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("16",  "nước tiểu đậm",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("17",  "tiêu chảy",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("18",  "vàng da",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("19",  "buồn nôn ói mửa",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("20",  "đau tinh hoàn",  "Default", new Date(),  new Date(), 1));

//        addDataDisease(new Disease("1", "Bệnh sốt rét", "Một bệnh truyền nhiễm do ký sinh trùng đơn bào thuộc họ Plasmodium gây ra, có thể lây truyền qua vết đốt của muỗi Anopheles hoặc do kim tiêm bị ô nhiễm hoặc truyền máu. Sốt rét Falciparum là loại gây tử vong cao nhất.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("2", "viêm gan A", "Viêm gan A là một bệnh nhiễm trùng gan rất dễ lây lan do vi rút viêm gan A gây ra. Virus này là một trong số các loại virus viêm gan gây viêm và ảnh hưởng đến khả năng hoạt động của gan.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("3", "Cảm lạnh thông thường", "Cảm lạnh thông thường là một bệnh nhiễm trùng do vi-rút ở mũi và cổ họng (đường hô hấp trên). Nó thường vô hại, mặc dù nó có thể không cảm thấy như vậy. Nhiều loại vi-rút có thể gây ra cảm lạnh thông thường.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("4", "Bệnh viêm gan B", "Viêm gan B là một bệnh nhiễm trùng ở gan của bạn. Nó có thể gây sẹo nội tạng, suy gan và ung thư. Nó có thể gây tử vong nếu không được điều trị. Nó lây lan khi mọi người tiếp xúc với máu, vết loét hở hoặc chất dịch cơ thể của người có vi rút viêm gan B.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("5", "Viêm gan C", "Viêm gan do siêu vi viêm gan C (HCV), thường lây lan qua truyền máu (hiếm gặp), chạy thận nhân tạo và dùng kim tiêm. Những tổn thương mà viêm gan C gây ra cho gan có thể dẫn đến xơ gan và các biến chứng của nó cũng như ung thư.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("6", "Viêm gan E", "Một dạng viêm gan hiếm gặp do nhiễm vi rút viêm gan E (HEV). Nó lây truyền qua thức ăn hoặc đồ uống do người bị nhiễm bệnh cầm nắm hoặc qua nguồn cung cấp nước bị nhiễm bệnh ở những khu vực mà phân có thể ngấm vào nước. Viêm gan E không gây ra bệnh gan mãn tính.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("7", "Viêm gan siêu vi D", "Viêm gan D, còn được gọi là virus viêm gan delta, là một bệnh nhiễm trùng khiến gan bị viêm. Vết sưng này có thể làm suy giảm chức năng gan và gây ra các vấn đề về gan lâu dài, bao gồm cả sẹo gan và ung thư. Tình trạng này do vi rút viêm gan D (HDV) gây ra.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("8", "Viêm phổi", "Viêm phổi là tình trạng nhiễm trùng ở một hoặc cả hai phổi. Vi khuẩn, vi rút và nấm gây ra nó. Nhiễm trùng gây viêm các túi khí trong phổi của bạn, được gọi là phế nang. Các phế nang chứa đầy dịch hoặc mủ, gây khó thở.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("9", "Bệnh lao", "Bệnh lao (TB) là một bệnh truyền nhiễm thường do vi khuẩn Mycobacterium tuberculosis (MTB) gây ra. Bệnh lao thường ảnh hưởng đến phổi, nhưng cũng có thể ảnh hưởng đến các bộ phận khác của cơ thể. Hầu hết các trường hợp nhiễm trùng không có triệu chứng, trong trường hợp đó, nó được gọi là bệnh lao tiềm ẩn.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("10", "Covid 19", "COVID-19 (từ tiếng Anh: coronavirus disease 2019 nghĩa là bệnh virus corona 2019)là một bệnh đường hô hấp cấp tính truyền nhiễm gây ra bởi chủng virus corona SARS-CoV-2. Bệnh được phát hiện lần đầu tiên trong đại dịch COVID-19 năm 2019–2020.", new Date(), new Date(), 1));


    //adivse of 9 disease
//        addDataAdvise(new Advise("id", "Tham khảo ý kiến bệnh viện gần nhất", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "tránh thức ăn nhiều dầu mỡ", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "tránh thức ăn không chay", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "giữ muỗi ra ngoài", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "uống đồ uống giàu vitamin c", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "tham khảo ý kiến bác sĩ", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "che miệng", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "rửa tay qua", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "lấy hơi", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "tiêm chủng", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "nghỉ ngơi", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "thuốc", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "tránh đồ ăn cay béo", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "tránh đồ ăn lạnh", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "giữ sốt", new Date(), new Date(), 1));

    //9 disease
//        addDataDisease(new Disease("id", "Bệnh sốt rét", "Một bệnh truyền nhiễm do ký sinh trùng đơn bào thuộc họ Plasmodium gây ra, có thể lây truyền qua vết đốt của muỗi Anopheles hoặc do kim tiêm bị ô nhiễm hoặc truyền máu. Sốt rét Falciparum là loại gây tử vong cao nhất.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("id", "viêm gan A", "Viêm gan A là một bệnh nhiễm trùng gan rất dễ lây lan do vi rút viêm gan A gây ra. Virus này là một trong số các loại virus viêm gan gây viêm và ảnh hưởng đến khả năng hoạt động của gan.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("id", "Cảm lạnh thông thường", "Cảm lạnh thông thường là một bệnh nhiễm trùng do vi-rút ở mũi và cổ họng (đường hô hấp trên). Nó thường vô hại, mặc dù nó có thể không cảm thấy như vậy. Nhiều loại vi-rút có thể gây ra cảm lạnh thông thường.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("id", "Bệnh viêm gan B", "Viêm gan B là một bệnh nhiễm trùng ở gan của bạn. Nó có thể gây sẹo nội tạng, suy gan và ung thư. Nó có thể gây tử vong nếu không được điều trị. Nó lây lan khi mọi người tiếp xúc với máu, vết loét hở hoặc chất dịch cơ thể của người có vi rút viêm gan B.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("id", "Viêm gan C", "Viêm gan do siêu vi viêm gan C (HCV), thường lây lan qua truyền máu (hiếm gặp), chạy thận nhân tạo và dùng kim tiêm. Những tổn thương mà viêm gan C gây ra cho gan có thể dẫn đến xơ gan và các biến chứng của nó cũng như ung thư.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("id", "Viêm gan E", "Một dạng viêm gan hiếm gặp do nhiễm vi rút viêm gan E (HEV). Nó lây truyền qua thức ăn hoặc đồ uống do người bị nhiễm bệnh cầm nắm hoặc qua nguồn cung cấp nước bị nhiễm bệnh ở những khu vực mà phân có thể ngấm vào nước. Viêm gan E không gây ra bệnh gan mãn tính.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("id", "Viêm gan siêu vi D", "Viêm gan D, còn được gọi là virus viêm gan delta, là một bệnh nhiễm trùng khiến gan bị viêm. Vết sưng này có thể làm suy giảm chức năng gan và gây ra các vấn đề về gan lâu dài, bao gồm cả sẹo gan và ung thư. Tình trạng này do vi rút viêm gan D (HDV) gây ra.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("id", "Viêm phổi", "Viêm phổi là tình trạng nhiễm trùng ở một hoặc cả hai phổi. Vi khuẩn, vi rút và nấm gây ra nó. Nhiễm trùng gây viêm các túi khí trong phổi của bạn, được gọi là phế nang. Các phế nang chứa đầy dịch hoặc mủ, gây khó thở.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("id", "Bệnh lao", "Bệnh lao (TB) là một bệnh truyền nhiễm thường do vi khuẩn Mycobacterium tuberculosis (MTB) gây ra. Bệnh lao thường ảnh hưởng đến phổi, nhưng cũng có thể ảnh hưởng đến các bộ phận khác của cơ thể. Hầu hết các trường hợp nhiễm trùng không có triệu chứng, trong trường hợp đó, nó được gọi là bệnh lao tiềm ẩn.", new Date(), new Date(), 1));

//        //Full symptom
//        addDataSymptom(new Symptom("id",  "ngứa",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "phát ban da",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "nổi nốt trên da",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "hắt hơi liên tục",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "rùng mình",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "ớn lạnh",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau khớp",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau bụng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "acid dạ dày",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "vết loét trên lưỡi",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mỏi cơ",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "nôn mửa",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tiểu rát",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đi tiểu nhỏ giọt",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mệt mỏi",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tăng cân",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "thở nhanh",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "bàn tay và bàn chân lạnh",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "nhanh chóng thay đổi tâm trạng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "giảm cân",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "bồn chồn",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mơ màng và buồn ngủ",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đàm trong cổ họng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tăng đường huyết",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "ho",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "sốt cao",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mắt trũng sâu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "khó thở",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đổ mồ hôi",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mất nước",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "khó tiêu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau đầu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "da hơi vàng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "Nước tiểu đậm",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "buồn nôn",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "ăn mất ngon",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau sau mắt",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau lưng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "táo bón",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau bụng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "bệnh tiêu chảy",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "sốt nhẹ",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "nước tiểu vàng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "vàng mắt",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "suy gan cấp tính",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tình trạng quá tải chất lỏng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "sưng bụng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "sưng hạch bạch huyết",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "khó chịu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "blurred and distorted vision",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đờm dãi",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "viêm họng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đỏ mắt",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "áp lực xoang",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "sổ mũi",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tắc nghẽn",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tức ngực",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "yếu tay chân",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "nhịp tim nhanh",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau khi đi tiểu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau ở vùng hậu môn",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "phân có máu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "kích ứng ở hậu môn",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau cổ",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "chóng mặt",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "chuột rút",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "bầm tím",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "béo phì",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "sưng chân",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mạch máu sưng lên",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mặt và mắt sưng húp",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tuyến giáp mở rộng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "móng tay dễ gãy",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tứ chi sưng tấy",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đói bụng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "extra marital contacts",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "khô và ngứa môi",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "nói lắp",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau đầu gối",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau khớp háng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "yếu cơ",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "cổ cứng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "sưng khớp",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "cử động cứng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "spinning movements",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mất thăng bằng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "loạng choạng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau ở một bên cơ thể",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "không cảm nhận được mùi vị",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "khó chịu bàng quang",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mùi hôi của nước tiểu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "cảm giác mắc tiểu liên tục",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "passage of gases",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "ngứa nội tạng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "toxic look (typhos)",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "Phiền muộn",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "cáu gắt",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau cơ",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "altered sensorium",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đốm đỏ trên cơ thể",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau bụng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "kinh nguyệt bất thường",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "dischromic patches",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "chảy nước mắt",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tăng khẩu vị",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đa niệu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "gia đình có người nhiễm",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đờm nhầy",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đờm rỉ",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "thiếu tập trung",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "rối loạn thị giác",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "nhận truyền máu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tiêm thuốc không vô trùng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "hôn mê",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "chảy máu dạ dày",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "chướng bụng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "từng uống rượu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "fluid overload",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đờm trong máu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tĩnh mạch nổi rõ trên bắp chân",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau ngực",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau khi đi bộ",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mụn đầy mủ",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mụn đầu đen",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "hối hả",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "lột da",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "silver like dusting",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "vết lõm nhỏ trên móng tay",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "móng tay bị viêm",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mụn rộp",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau đỏ quanh mũi",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "rỉ nước vàng",  "Default", new Date(),  new Date(), 1));


//        addDataMedicine(new Medicine("id", "name",  "description",  "manufacturer",  "content", new Date(), new Date(), 1));
}