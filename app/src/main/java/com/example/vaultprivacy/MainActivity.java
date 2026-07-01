package com.example.vaultprivacy;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnPhotoPicker).setOnClickListener(v ->
                startActivity(new Intent(this, PhotoPickerActivity.class)));

        findViewById(R.id.btnAudioPicker).setOnClickListener(v ->
                startActivity(new Intent(this, AudioPickerActivity.class)));

        findViewById(R.id.btnLocation).setOnClickListener(v ->
                startActivity(new Intent(this, LocationActivity.class)));

        findViewById(R.id.btnPrivacySandbox).setOnClickListener(v ->
                showPrivacySandboxInfo());

        findViewById(R.id.btnFinalWorkshop).setOnClickListener(v ->
                startActivity(new Intent(this, FinalWorkshopActivity.class)));
    }

    private void showPrivacySandboxInfo() {
        new AlertDialog.Builder(this)
                .setTitle("🔒 Privacy Sandbox")
                .setMessage(
                        "Initiative Google pour améliorer la vie privée sur Android.\n\n" +
                                "1. SDK Runtime\n" +
                                "   Les SDK tiers s'exécutent dans un sandbox isolé — " +
                                "ils n'ont plus accès aux données de l'app principale.\n\n" +
                                "2. Privacy Manifest\n" +
                                "   Déclare explicitement quelles données sensibles sont " +
                                "utilisées, comment et pourquoi.\n\n" +
                                "3. Topics API\n" +
                                "   Remplace les cookies tiers en fournissant des centres " +
                                "d'intérêt généraux sans identifier précisément l'utilisateur.\n\n" +
                                "4. Principe du moindre privilège\n" +
                                "   Ne déclarer que les permissions strictement nécessaires. " +
                                "Utiliser maxSdkVersion pour limiter les permissions aux " +
                                "versions qui en ont besoin.")
                .setPositiveButton("Compris", null)
                .show();
    }
}