package co.ue.edu.huertaconectaapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import co.ue.edu.huertaconectaapp.views.AboutFragment;
import co.ue.edu.huertaconectaapp.views.ContactFragment;
import co.ue.edu.huertaconectaapp.views.HomeFragment;
import co.ue.edu.huertaconectaapp.views.HowItWorksFragment;
import co.ue.edu.huertaconectaapp.views.LoginFragment;
import co.ue.edu.huertaconectaapp.views.RegisterFragment;
import co.ue.edu.huertaconectaapp.views.ServicesFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    HomeFragment homeFragment = new HomeFragment();
    AboutFragment aboutFragment = new AboutFragment();
    ServicesFragment servicesFragment = new ServicesFragment();
    HowItWorksFragment howItWorksFragment = new HowItWorksFragment();
    ContactFragment contactFragment = new ContactFragment();
    LoginFragment loginFragment = new LoginFragment();
    RegisterFragment registerFragment = new RegisterFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.content_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.main);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        loadFragment(loginFragment);
        showMenu(false);


    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
    public void showMenu(boolean show) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (show) {
            toolbar.setVisibility(View.VISIBLE);
        } else {
            toolbar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            loadFragment(homeFragment);
            return true;
        } else if (id == R.id.nav_about) {
            loadFragment(aboutFragment);
            return true;
        } else if (id == R.id.nav_service) {
            loadFragment(servicesFragment);
            return true;
        } else if (id == R.id.nav_howItWorks) {
            loadFragment(howItWorksFragment);
            return true;
        } else if (id == R.id.nav_contact) {
            loadFragment(contactFragment);
            return true;
        }else if (id == R.id.nav_auth) {
            loadFragment(loginFragment);
            return true;
        }

        return false;
    }
}

