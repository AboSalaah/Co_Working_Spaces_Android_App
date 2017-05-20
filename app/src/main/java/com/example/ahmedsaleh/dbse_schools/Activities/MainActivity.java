package com.example.ahmedsaleh.dbse_schools.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmedsaleh.dbse_schools.Fragments.AboutDbseFragement;
import com.example.ahmedsaleh.dbse_schools.Fragments.EditProfileFragement;
import com.example.ahmedsaleh.dbse_schools.Fragments.HomeFragement;
import com.example.ahmedsaleh.dbse_schools.R;
import com.example.ahmedsaleh.dbse_schools.Fragments.ViewProfileFragement;

import static com.example.ahmedsaleh.dbse_schools.Activities.SignIn.openBbSE_FacebookPage;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, new HomeFragement());
        ft.commit();



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_logout){
            movToSignInActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int id) {

        Fragment fragment = null;
        switch (id) {
            case R.id.Home_icon:
                fragment = new HomeFragement();
                break;
            case R.id.View_Profile:
                fragment = new ViewProfileFragement();
                break;
            case R.id.Edit_profile:
                fragment = new EditProfileFragement();
                break;
            case R.id.about_dbse:
                fragment = new AboutDbseFragement();
                break;
            case R.id.Contacts:
                fragment = new HomeFragement();
                viewContactsDialog();
                break;
            case R.id.Sign_out:
                movToSignInActivity();
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        displaySelectedScreen(id);

        return true;
    }
    void viewContactsDialog(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        final View mview=getLayoutInflater().inflate(R.layout.contacts_dialog,null);
        mBuilder.setTitle(R.string.contacts_text);
        mBuilder.setView(mview);
        mBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        AlertDialog dialog=mBuilder.create();
        dialog.show();

        ImageView facbookpage = (ImageView) mview.findViewById(R.id.Facebook_dialog);
        facbookpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent facebookIntent = openBbSE_FacebookPage(MainActivity.this);
                Intent chooser = Intent.createChooser(facebookIntent,"Open By");
                startActivity(chooser);
            }
        });
        TextView DbSE = (TextView) mview.findViewById(R.id.Dbse_dialog);
        DbSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DbSE_Intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.dbse.co/coworking"));
                Intent chooser = Intent.createChooser(DbSE_Intent,"Open By");
                startActivity(chooser);
            }
        });
    }

    private void movToSignInActivity() {
        Intent i = new Intent(getApplicationContext(),SignIn.class);
        startActivity(i);
        finish();
    }





}
