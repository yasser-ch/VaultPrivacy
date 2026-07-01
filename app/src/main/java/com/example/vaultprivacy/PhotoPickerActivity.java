package com.example.vaultprivacy;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class PhotoPickerActivity extends AppCompatActivity {

    private ImageView ivPhoto;
    private TextView  tvInfo;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickSingle =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    displayPhoto(uri);
                    tvInfo.setText("✅ 1 photo sélectionnée\n" + uri.getLastPathSegment());
                } else {
                    tvInfo.setText("Aucune photo sélectionnée");
                }
            });

    private final ActivityResultLauncher<PickVisualMediaRequest> pickMultiple =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
                if (!uris.isEmpty()) {
                    displayPhoto(uris.get(0));
                    tvInfo.setText("✅ " + uris.size() + " photo(s) sélectionnée(s)\n"
                            + "Première : " + uris.get(0).getLastPathSegment());
                } else {
                    tvInfo.setText("Aucune photo sélectionnée");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);

        ivPhoto = findViewById(R.id.ivPhoto);
        tvInfo  = findViewById(R.id.tvInfo);
        MaterialButton btnSingle   = findViewById(R.id.btnSingle);
        MaterialButton btnMultiple = findViewById(R.id.btnMultiple);
        MaterialButton btnBack     = findViewById(R.id.btnBack);

        btnSingle.setOnClickListener(v ->
                pickSingle.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build()));

        btnMultiple.setOnClickListener(v ->
                pickMultiple.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build()));

        btnBack.setOnClickListener(v -> finish());
    }

    private void displayPhoto(Uri uri) {
        ivPhoto.setImageURI(uri);
        try {
            getContentResolver().takePersistableUriPermission(
                    uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Toast.makeText(this, "Accès persistant accordé", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Accès temporaire uniquement", Toast.LENGTH_SHORT).show();
        }
    }
}