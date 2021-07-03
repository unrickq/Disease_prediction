package com.example.diseaseprediction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.diseaseprediction.object.Account;
import com.example.diseaseprediction.object.DoctorInfo;
import com.example.diseaseprediction.ui.account.AccountFragment;
import com.example.diseaseprediction.ui.alert.AlertFragment;
import com.example.diseaseprediction.ui.consultation.ConsultationListFragment;
import com.example.diseaseprediction.ui.home.HomeFragment;
import com.example.diseaseprediction.ui.prediction.PredictionListFragment;
import com.example.diseaseprediction.ui.predictionListConfirm.PredictionListConfirm;
import com.example.diseaseprediction.ui.settings.SettingsFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private DatabaseReference mRef;
    private FirebaseUser fUser;

    private Account mAccount;
    private DoctorInfo mDoctor;

    private AppBarConfiguration mAppBarConfiguration;
    private TextView nav_header_txt_acc_name, nav_header_txt_acc_phone;
    private CircleImageView nav_header_avatar;
    private NavigationView nav_view;
    private DrawerLayout drawer;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get current user
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        //Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger);
        //Set navigation
        setNavigation();

        //Check data of account
        checkDataOfAccount(fUser);

        //Detect data change
        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getUIofNavHeader();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Set left navigation
     */
    private void setNavigation() {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_account,
                R.id.nav_consultationList,
                R.id.nav_predictionList,
                R.id.nav_settings
        )
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //set item click on navigation
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                new HomeFragment()).commit();
                        drawer.close();
                        break;
                    }
                    case R.id.nav_account: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                new AccountFragment()).commit();
                        drawer.close();
                        break;
                    }
                    case R.id.nav_consultationList: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                new ConsultationListFragment()).commit();
                        drawer.close();
                        break;
                    }
                    case R.id.nav_predictionListConfirm: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                new PredictionListConfirm()).commit();
                        drawer.close();
                        break;
                    }
                    case R.id.nav_predictionList: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                new PredictionListFragment()).commit();
                        drawer.close();
                        break;
                    }
                    case R.id.nav_settings: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                new SettingsFragment()).commit();
                        drawer.close();
                        break;
                    }
                    case R.id.nav_out: {
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(MainActivity.this, Login.class);
                        startActivity(i);
                        break;
                    }
                }
                return true;
            }
        });

        //Set prediction list confirm visibility depend on type account
        mRef = FirebaseDatabase.getInstance().getReference("Accounts").child(fUser.getUid());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Account ac = snapshot.getValue(Account.class);
                if (ac.getTypeID() == 0) {
                    Menu mn = navigationView.getMenu();
                    mn.findItem(R.id.nav_predictionListConfirm).setVisible(true);
                } else if (ac.getTypeID() == 1) {
                    Menu mn = navigationView.getMenu();
                    mn.findItem(R.id.nav_predictionListConfirm).setVisible(false);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.alert) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            //Comment
            transaction.replace(R.id.nav_host_fragment, new AlertFragment());
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Set title of toolbar
     *
     * @param title
     */
    public void setActionBarTitle(String title) {
        TextView t = (TextView) findViewById(R.id.toolbar_title);
        t.setText(title);
    }

    /**
     * Set toolbar icon
     */
    public void setIconToolbar() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger);
    }

    /**
     * Get data for navigation header
     */
    private void getUIofNavHeader() {
        //get user by id
        mAccount = new Account();
        mRef = FirebaseDatabase.getInstance().getReference("Accounts");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //check user exist in firebase
                //if exist then set UI
                if (snapshot.hasChild(fUser.getUid())) {
                    mAccount = snapshot.child(fUser.getUid()).getValue(Account.class);
                    //Set text navigation header
                    nav_view = findViewById(R.id.nav_view);
                    View headerView = nav_view.getHeaderView(0);
                    nav_header_txt_acc_name = headerView.findViewById(R.id.nav_header_txt_acc_name);
                    nav_header_txt_acc_phone = headerView.findViewById(R.id.nav_header_txt_acc_phone);
                    nav_header_txt_acc_name.setText(mAccount.getName());
                    if (mAccount.getPhone().equals("Default")) {
                        nav_header_txt_acc_phone.setText("");
                    } else {
                        nav_header_txt_acc_phone.setText(mAccount.getPhone());
                    }

                    //set image
                    nav_header_avatar = findViewById(R.id.nav_header_avatar);
                    if (!mAccount.getImage().equals("Default")) {
                        Glide.with(MainActivity.this).load(mAccount.getImage()).into(nav_header_avatar);
                    } else {
                        nav_header_avatar.setImageResource(R.mipmap.ic_default_avatar_round);
//                        Glide.with(MainActivity.this).load(R.drawable.background_avatar).into(nav_header_avatar);
                    }

                } else {
                    //Can't find any data
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    /**
     * Check data of current account
     * If there exists default data, go to account information activity
     * If data is full, go to main activity
     *
     * @param user current user
     */
    private void checkDataOfAccount(FirebaseUser user) {
        mAccount = new Account();
        mRef = FirebaseDatabase.getInstance().getReference("Accounts");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //check user exist in firebase
                if (snapshot.hasChild(user.getUid())) {
                    mAccount = snapshot.child(user.getUid()).getValue(Account.class);
                    try {
                        //Get data
                        //Go to info activity if data is default
                        if (mAccount.getName().equals("Default") || mAccount.getGender() == -1 ||
                                mAccount.getPhone().equals("Default") || mAccount.getEmail().equals("Default") ||
                                mAccount.getAddress().equals("Default")) {
                            Intent intent = new Intent(MainActivity.this, AccountInfo.class);
                            startActivity(intent);
                        } else {
                            //Check data if account type is doctor
                            if (mAccount.getTypeID() == 0) {
                                checkDataOfAccountDoctor(user);
                            }
                        }
                    } catch (NullPointerException e) {
                        Log.d(TAG, "Home. Patient ID null", e);
                    }
                } else {
                    //IF can't find any data
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    /**
     * Check data of current doctor account
     * If there exists default data, go to account information doctor activity
     * If data is full, go to main activity
     *
     * @param user current user
     */
    private void checkDataOfAccountDoctor(FirebaseUser user) {
        mAccount = new Account();
        mRef = FirebaseDatabase.getInstance().getReference("DoctorInfo");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //check user exist in firebase
                if (snapshot.hasChild(user.getUid())) {
                    mDoctor = snapshot.child(user.getUid()).getValue(DoctorInfo.class);
                    try {
                        //Get data
                        //Go to info activity if data is default
                        if (mDoctor.getShortDescription().equals("Default") || mDoctor.getExperience() == -1 ||
                                mDoctor.getSpecializationID().equals("Default")) {
                            Intent intent = new Intent(MainActivity.this, AccountInfoDoctor.class);
                            startActivity(intent);
                        }
                    } catch (NullPointerException e) {
                        Log.d(TAG, "Main. checkDataOfAccountDoctor", e);
                    }
                } else {
                    DoctorInfo doctorInfo = new DoctorInfo(user.getUid(), "Default", "Default", -1, new Date(), new Date(), 1);
                    mRef.child(user.getUid()).setValue(doctorInfo);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}