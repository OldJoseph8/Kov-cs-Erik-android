package com.example.smithcar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.fragment.NavHostFragment;

import com.example.smithcar.u.i.HomeFragment;
import com.example.smithcar.u.i.WelcomeFragment;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_MEDIA_PERMISSIONS = 101;
    public boolean isUserLoggedIn = false;

    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        }

        checkMediaPermissions();
    }

    // --- MENÜ LÉTREHOZÁSA (Jobb felső 3 pont) ---
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // --- MENÜ KATTINTÁS KEZELÉSE ---
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_buy) {
            // Hirdetések (Böngészés)
            navigateTo(R.id.buyFragment, null);
            return true;
        } else if (id == R.id.action_sales) {
            // Hirdetek (Feladás)
            navigateTo(R.id.salesFragment, null);
            return true;
        } else if (id == R.id.action_about) {
            // Rólunk
            navigateTo(R.id.aboutFragment, null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void navigateTo(int destinationId, Bundle bundle) {
        try {
            if (navController != null) {
                if (navController.getCurrentDestination() != null &&
                        navController.getCurrentDestination().getId() == destinationId) {
                    return;
                }
                navController.navigate(destinationId, bundle);
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Navigációs hiba: " + e.getMessage());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void checkMediaPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            boolean hasImagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
            boolean hasVideoPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;

            if (!hasImagePermission || !hasVideoPermission) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO},
                        REQUEST_MEDIA_PERMISSIONS);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_MEDIA_PERMISSIONS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}