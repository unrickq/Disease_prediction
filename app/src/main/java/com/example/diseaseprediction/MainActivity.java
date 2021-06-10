package com.example.diseaseprediction;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.diseaseprediction.ui.account.AccountFragment;
import com.example.diseaseprediction.ui.alert.AlertFragment;
import com.example.diseaseprediction.ui.consultation.ConsultationListFragment;
import com.example.diseaseprediction.ui.home.HomeFragment;
import com.example.diseaseprediction.ui.prediction.PredictionListFragment;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mRef;
    private FirebaseUser fUser;
    private AppBarConfiguration mAppBarConfiguration;

    private Account mAccount;

    private TextView nav_header_txt_acc_name,nav_header_txt_acc_phone;
    private CircleImageView nav_header_avatar;
    private NavigationView nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger);
        //Set navigation
        setNavigation();

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

    private void setNavigation(){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_account,
                R.id.nav_consultationList,
                R.id.nav_predictionList,
                R.id.nav_settings
//                R.id.nav_about
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
                   /* case R.id.nav_about: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                new AboutFragment()).commit();
                        drawer.close();
                        break;
                    }*/
                    case R.id.nav_out: {
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(MainActivity.this,Login.class);
                        startActivity(i);
                        break;
                    }
                }
                return true;
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
        if(id == R.id.alert) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            //Comment
            transaction.replace(R.id.nav_host_fragment, new AlertFragment());
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title) {
        TextView t = (TextView) findViewById(R.id.toolbar_title) ;
        t.setText(title) ;
    }

    public void setIconToolbar(){
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger);
    }

    private void getUIofNavHeader(){
        //get user by id
        mAccount = new Account();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("Accounts");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //check user exist in firebase
                //if exist then set UI
                if (snapshot.hasChild(fUser.getUid())){
                    mAccount = snapshot.child(fUser.getUid()).getValue(Account.class);
                    //Set text navigation header
                    nav_view = findViewById(R.id.nav_view);
                    View headerView = nav_view.getHeaderView(0);
                    nav_header_txt_acc_name = headerView.findViewById(R.id.nav_header_txt_acc_name);
                    nav_header_txt_acc_phone = headerView.findViewById(R.id.nav_header_txt_acc_phone);
                    nav_header_txt_acc_name.setText(mAccount.getName());
                    if (mAccount.getPhone().equals("Default")){
                        nav_header_txt_acc_phone.setText("");
                    }else{
                        nav_header_txt_acc_phone.setText(mAccount.getPhone());
                    }

                    //set image
                    nav_header_avatar = findViewById(R.id.nav_header_avatar);
                    if (!mAccount.getImage().equals("Default")){
                        Glide.with(MainActivity.this).load(mAccount.getImage()).into(nav_header_avatar);
                    }else{
                        Glide.with(MainActivity.this).load(R.drawable.background_avatar).into(nav_header_avatar);
                    }

                }else{
                    System.out.println("k tim ra");
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}