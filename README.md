# Lostify 📱🔍

Lostify is an Android application designed to help users **report, browse, and manage lost and found items** in a simple and user‑friendly way. The app is currently under active development and focuses on clean UI, modern Android architecture, and scalable design.

---

## 🚀 Project Status

**In Development** – core UI and architecture are implemented, features are being incrementally added and improved.

---

## 🛠 Tech Stack Used So Far

* **Language:** Kotlin
* **UI:** Jetpack Compose (Material 3)
* **Architecture:** MVVM (Model–View–ViewModel)
* **State Management:** ViewModel + LiveData / State
* **Navigation:** Jetpack Navigation Compose
* **Database:** Room (Local database)
* **Image Handling:** Coil (Async image loading)

---

## ✨ Features Implemented So Far

### 🔐 Authentication UI

* Login Screen UI
* Sign Up Screen UI
* Clean Material 3 design

### 🏠 Home Screen

* Displays list of lost items
* Uses **LazyColumn** (RecyclerView equivalent in Compose)
* Item cards with image, title, category, and location

### 📄 Item Details Screen

* Detailed view of a selected lost item
* Image preview with improved padding and rounded corners
* Back navigation support
* Structured layout for better readability

### ➕ Add Lost Item Screen

* Form to add a lost item
* Fields for item name, description, category, and location
* Image selection support (UI level)

### 🗂 Local Database (Room)

* Room database setup
* Entity and DAO created
* ViewModel connected to database
* Handles app crashes caused by schema changes using migrations

---

## 🧱 Architecture Overview

```
UI (Jetpack Compose)
   ↓
ViewModel
   ↓
Repository
   ↓
Room Database (DAO)
```

* Clear separation of concerns
* Lifecycle‑aware components
* Easily testable and scalable

---

## 📸 UI Design Focus

* Material 3 components
* Rounded corners and spacing for better UX
* Responsive layouts
* Clean and minimal visual style

---

## 🧪 Known Issues / Work in Progress

* Image upload persistence
* Authentication backend integration
* Cloud database sync
* Search & filter functionality
* User profile screen

---

## 🔮 Planned Features

* Firebase Authentication
* Cloud Firestore integration
* Image upload to cloud storage
* Search and category filters
* Contact item owner feature
* Notifications for matched items

---

## 👨‍💻 Developer

**Adarsha Ghosh**
Android Developer (Learning & Building)

---

## 📄 License

This project is currently for learning and portfolio purposes.

---

> 💡 Lostify is being built step‑by‑step as part of Android full‑stack learning, focusing on real‑world app architecture and best practices.
