package co.ue.edu.huertaconectaapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

import co.ue.edu.huertaconectaapp.model.db.DatabaseHelper;
import co.ue.edu.huertaconectaapp.model.db.dao.SesionDao;
import co.ue.edu.huertaconectaapp.views.AboutFragment;
import co.ue.edu.huertaconectaapp.views.ContactFragment;
import co.ue.edu.huertaconectaapp.views.CreateHuertaFragment;
import co.ue.edu.huertaconectaapp.views.HomeFragment;
import co.ue.edu.huertaconectaapp.views.HowItWorksFragment;
import co.ue.edu.huertaconectaapp.views.HuertasFragment;
import co.ue.edu.huertaconectaapp.views.LoginFragment;
import co.ue.edu.huertaconectaapp.views.MisHuertasFragment;
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

    private SesionDao sesionDao;
    private DrawerLayout drawerLayout;


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

        drawerLayout = findViewById(R.id.main);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        sesionDao = new SesionDao(DatabaseHelper.getInstance(this));
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        if (sesionDao.haySesionActiva()) {
            actualizarNavHeader(navigationView);
            loadFragment(homeFragment);
            showMenu(true);
        } else {
            loadFragment(loginFragment);
            showMenu(false);
        }


    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    /** Abre un fragment encima del actual y permite volver atrás con el botón atrás. */
    public void pushFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit();
    }
    public void actualizarNavHeader(NavigationView navigationView) {
        View header = navigationView.getHeaderView(0);
        TextView tvNombre = header.findViewById(R.id.tvNavNombre);
        TextView tvCorreo = header.findViewById(R.id.tvNavCorreo);
        String correo = sesionDao.obtenerCorreo();
        if (correo != null) {
            String nombre = correo.contains("@") ? correo.substring(0, correo.indexOf("@")) : correo;
            tvNombre.setText(nombre.substring(0, 1).toUpperCase() + nombre.substring(1));
            tvCorreo.setText(correo);
        }
    }

    public void showMenu(boolean show) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (show) {
            toolbar.setVisibility(View.VISIBLE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            actualizarNavHeader(navigationView);
        } else {
            toolbar.setVisibility(View.GONE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (sesionDao != null && sesionDao.haySesionActiva()) {
            getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_nueva_huerta) {
            loadFragment(new CreateHuertaFragment());
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        } else if (id == R.id.nav_huertas) {
            loadFragment(new HuertasFragment());
            return true;
        } else if (id == R.id.nav_mis_huertas) {
            loadFragment(new MisHuertasFragment());
            return true;
        } else if (id == R.id.nav_contact) {
            loadFragment(contactFragment);
            return true;
        } else if (id == R.id.nav_auth) {
            sesionDao.cerrarSesion();
            showMenu(false);
            loadFragment(loginFragment);
            return true;
        }

        return false;
    }
}

