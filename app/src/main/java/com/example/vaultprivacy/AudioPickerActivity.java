package com.example.vaultprivacy;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;

public class AudioPickerActivity extends AppCompatActivity {

    private TextView    tvAudioName, tvInfo;
    private MediaPlayer mediaPlayer;
    private Uri         selectedUri;

    private final ActivityResultLauncher<Intent> pickAudio =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedUri = result.getData().getData();
                    if (selectedUri != null) {
                        try {
                            getContentResolver().takePersistableUriPermission(
                                    selectedUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } catch (Exception ignored) {}
                        tvAudioName.setText(selectedUri.getLastPathSegment());
                        tvInfo.setText("✅ Fichier sélectionné\nURI : " + selectedUri);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_picker);

        tvAudioName = findViewById(R.id.tvAudioName);
        tvInfo      = findViewById(R.id.tvInfo);
        MaterialButton btnSelect = findViewById(R.id.btnSelectAudio);
        MaterialButton btnPlay   = findViewById(R.id.btnPlay);
        MaterialButton btnStop   = findViewById(R.id.btnStop);
        MaterialButton btnBack   = findViewById(R.id.btnBack);

        btnSelect.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("audio/*");
            pickAudio.launch(intent);
        });

        btnPlay.setOnClickListener(v -> {
            if (selectedUri == null) {
                Toast.makeText(this, "Sélectionne un fichier d'abord", Toast.LENGTH_SHORT).show();
                return;
            }
            stopAudio();
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(this, selectedUri);
                mediaPlayer.prepare();
                mediaPlayer.start();
                tvInfo.setText("▶ Lecture en cours…");
            } catch (IOException e) {
                tvInfo.setText("❌ Erreur lecture : " + e.getMessage());
            }
        });

        btnStop.setOnClickListener(v -> {
            stopAudio();
            tvInfo.setText("■ Lecture arrêtée");
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void stopAudio() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAudio();
    }
}