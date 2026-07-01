package com.example.vaultprivacy;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

public class LocationActivity extends AppCompatActivity implements LocationListener {

    private TextView       tvStatus, tvLocation;
    private LocationManager locationManager;

    private final ActivityResultLauncher<String> requestPerm =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    tvStatus.setText("✅ Permission accordée");
                    tvStatus.setTextColor(getColor(R.color.success));
                    startUpdates();
                } else {
                    tvStatus.setText("❌ Permission refusée");
                    tvStatus.setTextColor(getColor(R.color.danger));
                    Toast.makeText(this,
                            "Sans cette permission, la localisation est indisponible",
                            Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        tvStatus   = findViewById(R.id.tvStatus);
        tvLocation = findViewById(R.id.tvLocation);
        MaterialButton btnRequest = findViewById(R.id.btnRequest);
        MaterialButton btnBack    = findViewById(R.id.btnBack);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        btnRequest.setOnClickListener(v -> checkAndRequest());
        btnBack.setOnClickListener(v -> finish());
    }

    private void checkAndRequest() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            tvStatus.setText("✅ Permission déjà accordée");
            tvStatus.setTextColor(getColor(R.color.success));
            startUpdates();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle("Pourquoi cette permission ?")
                    .setMessage("La localisation est nécessaire pour afficher votre position. "
                            + "Elle est demandée uniquement quand vous appuyez sur ce bouton "
                            + "et arrêtée dès que vous quittez cet écran.")
                    .setPositiveButton("Accorder", (d, w) ->
                            requestPerm.launch(Manifest.permission.ACCESS_FINE_LOCATION))
                    .setNegativeButton("Refuser", (d, w) -> {
                        tvStatus.setText("⚠️ Permission non accordée");
                        tvStatus.setTextColor(getColor(R.color.warning));
                    })
                    .show();
        } else {
            requestPerm.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void startUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) return;

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Activez le GPS", Toast.LENGTH_SHORT).show();
            tvLocation.setText("⚠️ GPS désactivé");
            return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 10_000, 10, this);

        Location last = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (last != null) updateUI(last);
        else tvLocation.setText("🔍 Recherche de la position…");
    }

    private void updateUI(Location loc) {
        tvLocation.setText(String.format(
                "Latitude  : %.6f\nLongitude : %.6f\nPrécision : %.1f m",
                loc.getLatitude(), loc.getLongitude(), loc.getAccuracy()));
    }

    @Override public void onLocationChanged(@NonNull Location location) { updateUI(location); }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
}