# 🔏 VaultPrivacy

Application Android de démonstration des bonnes pratiques de gestion des permissions et de protection de la vie privée — développée en Java avec Material Design 3.

---

## 📱 Aperçu

| Écran | Description |
|---|---|
| 🖼️ Photo Picker | Sélection photos sans permission READ_MEDIA_IMAGES |
| 🎵 Audio Picker | Sélection audio sans permission READ_MEDIA_AUDIO |
| 📍 Localisation | Runtime permission avec rationale dialog |
| 🔒 Privacy Sandbox | Explication SDK Runtime + Topics API |
| 🚀 Atelier Final | Combinaison Photo Picker + Localisation |

---

## ✨ Fonctionnalités

- **Photo Picker moderne** — `PickVisualMedia` (simple ou multiple) sans permission globale, `takePersistableUriPermission` pour accès persistant
- **Audio Picker** — `ACTION_OPEN_DOCUMENT` + `MediaPlayer` natif, pas de permission `READ_MEDIA_AUDIO`
- **Runtime Permission** — flux complet `checkSelfPermission` → `shouldShowRationale` → dialog explicatif → `requestPermissionLauncher`
- **Localisation sécurisée** — updates GPS arrêtés dans `onPause` pour économiser la batterie
- **Privacy Sandbox** — explication SDK Runtime, Privacy Manifest, Topics API
- **Principe du moindre privilège** — `maxSdkVersion="32"` limite `READ_EXTERNAL_STORAGE` aux anciennes versions
- **`allowBackup="false"`** — bloque l'extraction via `adb backup`

---

## DEMO



https://github.com/user-attachments/assets/836fd38d-1eac-44be-a706-ce8b43964169



## 🗂️ Structure

```
app/src/main/java/com/example/vaultprivacy/
├── MainActivity.java              # Hub — navigation + dialog Privacy Sandbox
├── PhotoPickerActivity.java       # PickVisualMedia simple + multiple
├── AudioPickerActivity.java       # ACTION_OPEN_DOCUMENT + MediaPlayer
├── LocationActivity.java          # Runtime permission localisation GPS
└── FinalWorkshopActivity.java     # Photo Picker + Localisation combinés

res/layout/
├── activity_main.xml
├── activity_photo_picker.xml
├── activity_audio_picker.xml
├── activity_location.xml
└── activity_final_workshop.xml
```

---

## 🛡️ Mesures de Vie Privée

| Risque | Contre-mesure | Implémentation |
|---|---|---|
| Sur-permission médias | Pickers modernes | `PickVisualMedia`, `ACTION_OPEN_DOCUMENT` |
| Accès global aux photos | URI ciblée | `takePersistableUriPermission` |
| Permission sans explication | Rationale dialog | `shouldShowRequestPermissionRationale` |
| GPS en permanence | Arrêt dans `onPause` | `locationManager.removeUpdates()` |
| Permissions inutiles anciennes | `maxSdkVersion` | `READ_EXTERNAL_STORAGE` limité à API ≤ 32 |
| Extraction backup | `allowBackup="false"` | AndroidManifest.xml |

---

## 🔄 Flux Permission Localisation

```
Bouton cliqué
      ↓
checkSelfPermission
      ↓
  Accordée ? ──→ startLocationUpdates()
      ↓ Non
shouldShowRationale ?
      ↓ Oui              ↓ Non
Dialog explicatif    requestPermissionLauncher
      ↓
Résultat → granted / denied
      ↓
onPause → removeUpdates()
```

---

## 🛠️ Stack

| Outil | Version |
|---|---|
| Java | 11 |
| Android SDK min | API 24 (Android 7.0) |
| `androidx.appcompat` | 1.6.1 |
| `material` | 1.10.0 |
| `androidx.activity` | 1.8.0 |
| `constraintlayout` | 2.1.4 |

---

## 🚀 Lancer le projet

```bash
git clone https://github.com/yasser-ch/VaultPrivacy.git
```

Ouvrir dans Android Studio → **Run** sur émulateur ou appareil physique (API 24+).

> ⚠️ Sur émulateur, la localisation GPS nécessite d'activer la localisation simulée dans les paramètres de l'émulateur (Extended Controls → Location).

---

## 📚 Contexte

TP réalisé dans le cadre du cursus **Génie Cyberdéfense & Télécommunications Embarquées** à l'ENSA Marrakech.  
Concepts abordés : Runtime Permissions, Photo Picker, Audio Picker, LocationManager, Privacy Sandbox, SDK Runtime, Privacy Manifest, principe du moindre privilège, conformité Google Play Data Safety.
