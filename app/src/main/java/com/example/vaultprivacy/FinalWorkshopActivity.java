package com.example.vaultprivacy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

public class FinalWorkshopActivity extends AppCompatActivity implements LocationListener {

    private ImageView      ivPhoto;
    private TextView       tvPhotoInfo, tvLocationInfo;
    private LocationManager locationManager;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickPhoto =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    ivPhoto.setImageURI(uri);
                    tvPhotoInfo.setText("📷 " + uri.getLastPathSegment());
                    try {
                        getContentResolver().takePersistableUriPermission(
                                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } catch (Exception ignored) {}
                } else {
                    tvPhotoInfo.setText("📷 Aucune photo sélectionnée");
                }
            });

    private final ActivityResultLauncher<String> requestPerm =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) getLocation();
                else tvLocationInfo.setText("📍 Permission refusée");
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_workshop);

        ivPhoto       = findViewById(R.id.ivPhoto);
        tvPhotoInfo   = findViewById(R.id.tvPhotoInfo);
        tvLocationInfo = findViewById(R.id.tvLocationInfo);
        MaterialButton btnPhoto    = findViewById(R.id.btnSelectPhoto);
        MaterialButton btnLocation = findViewById(R.id.btnGetLocation);
        MaterialButton btnBack     = findViewById(R.id.btnBack);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        btnPhoto.setOnClickListener(v ->
                pickPhoto.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build()));

        btnLocation.setOnClickListener(v -> checkLocation());
        btnBack.setOnClickListener(v -> finish());
    }

    private void checkLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle("Localisation requise")
                    .setMessage("Nécessaire pour afficher votre position. Arrêtée dès que vous quittez cet écran.")
                    .setPositiveButton("OK", (d, w) ->
                            requestPerm.launch(Manifest.permission.ACCESS_FINE_LOCATION))
                    .setNegativeButton("Non", null)
                    .show();
        } else {
            requestPerm.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) return;

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            tvLocationInfo.setText("📍 GPS désactivé");
            return;
        }

        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
        tvLocationInfo.setText("📍 Recherche…");

        Location last = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (last != null) updateLocation(last);
    }

    private void updateLocation(Location loc) {
        tvLocationInfo.setText(String.format(
                "📍 %.6f, %.6f (±%.1fm)",
                loc.getLatitude(), loc.getLongitude(), loc.getAccuracy()));
    }

    @Override public void onLocationChanged(@NonNull Location location) { updateLocation(location); }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
}