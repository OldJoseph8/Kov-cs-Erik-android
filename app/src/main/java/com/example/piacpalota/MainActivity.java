package com.example.piacpalota;

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
import androidx.fragment.app.Fragment; // Ezt az importot hagyjuk, de a replaceFragment törlődik
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.fragment.NavHostFragment;

// Importáljuk a Fragmentjeinket, de a hívásuk megváltozik
import com.example.piacpalota.u.i.HomeFragment;
import com.example.piacpalota.u.i.WelcomeFragment;
// ... a többi fragment import...

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_MEDIA_PERMISSIONS = 101;
    public boolean isUserLoggedIn = false;

    private NavController navController; // A modern navigáció vezérlője
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        // A "SmithCar" címet az XML-ben állítottuk be, ami jó!

        // --- EZ AZ ÚJ, MODERN RÉSZ ---
        // Megkeressük a NavController-t
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        // Beállítjuk, hogy a Toolbar (lila sáv) együttműködjön a navigációval
        // Ez automatikusan megcsinál MINDENT:
        // 1. Megjeleníti a "SmithCar" címet
        // 2. A főképernyőn (Home/Welcome) elrejti a nyilat
        // 3. A többi képernyőn (Login, Buy) megmutatja a nyilat
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        // ------------------------------------

        // A rendszer "vissza" gombja is a modern rendszerre támaszkodik
        // (Ezt nem kell bántani, az OnBackPressedCallback már jó volt)

        checkMediaPermissions();
        // Az "if (savedInstanceState == null)" részt kivettük,
        // mert a nav_graph.xml automatikusan betölti a kezdőképernyőt (HomeFragment)
    }

    // EZT A FÜGGVÉNYT IS A MODERN RENDSZER HASZNÁLJA A NYÍL KEZELÉSÉRE
    @Override
    public boolean onSupportNavigateUp() {
        // Ez kezeli a lila sávon lévő nyílra kattintást
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // AZ onCreateOptionsMenu ÉS az onOptionsItemSelected marad, ahogy volt
    // (A "kamu" bejelentkezéshez és a menühöz még kelleni fognak)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isUserLoggedIn) {
            getMenuInflater().inflate(R.menu.menu_logged_in, menu);
        } else {
            getMenuInflater().inflate(R.menu.main_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Fontos: Itt már nem a replaceFragment-et hívjuk,
        // hanem a NavController-t használjuk a navigálásra!
        // Ezt a részt a KÖVETKEZŐ LÉPÉSBEN kell megcsinálnunk!
        // Egyelőre a menü gombjaid nem fognak működni, csak a "kamu" kijelentkezés:

        if (id == R.id.action_logout) {
            isUserLoggedIn = false;
            invalidateOptionsMenu();
            navController.navigate(R.id.homeFragment); // <-- Modern navigáció a Home-ra
            return true;
        }

        // A többi menüpontot (pl. action_buy) a nav_graph alapján kell majd beállítani
        // de egyelőre hagyjuk, hogy a rendszer kezelje
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    // --- A replaceFragment(...) METÓDUST TELJESEN TÖRÖLTÜK ---
    // (Már nincs rá szükség, a NavController végzi a munkát)


    // A media engedélykezelő függvények maradnak (ezek jók voltak)
    private void checkMediaPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            boolean hasImagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
            boolean hasVideoPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;

            if (!hasImagePermission || !hasVideoPermission) {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.READ_MEDIA_IMAGES,
                                Manifest.permission.READ_MEDIA_VIDEO
                        },
                        REQUEST_MEDIA_PERMISSIONS);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_MEDIA_PERMISSIONS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_MEDIA_PERMISSIONS) {
            boolean granted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }
            if (granted) {
                Log.i("Permissions", "Media permissions granted.");
            } else {
                Log.e("Permissions", "Media permissions denied.");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}