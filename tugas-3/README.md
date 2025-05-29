# Aplikasi Manajemen Rental Kendaraan

## Deskripsi

Aplikasi GUI Java untuk manajemen rental kendaraan yang mengimplementasikan konsep Pemrograman Berbasis Objek (OOP) meliputi:

- **Abstraksi**: Kelas abstrak `Kendaraan`
- **Inheritance**: Kelas `Mobil`, `Motor`, dan `Bus` yang mewarisi dari `Kendaraan`
- **Encapsulation**: Private attributes dengan getter/setter methods
- **Polymorphism**: Override method `tampilKendaraan()` di setiap kelas turunan
- **Exception Handling**: Try-catch untuk validasi input dan error handling
- **File I/O**: Penyimpanan data kendaraan dan struk penyewaan ke file

## Struktur Kelas

### 1. Kelas Abstrak `Kendaraan`

- Atribut: `id`, `nama`, `hargaSewa`, `jenis`, `status`
- Method abstrak: `tampilKendaraan()`
- Method getter/setter untuk encapsulation

### 2. Kelas Turunan

- **`Mobil`**: Tambahan atribut `kapasitasPenumpang`, `transmisi`, `bahanBakar`
- **`Motor`**: Tambahan atribut `ccMesin`, `tipeMesin`
- **`Bus`**: Tambahan atribut `kapasitasPenumpang`, `kelasLayanan`, `acTersedia`

### 3. Kelas Manajemen

- **`RentalKendaraan`**: Mengelola ArrayList kendaraan dan operasi file
- **`Penyewaan`**: Mencatat transaksi penyewaan
- **`ItemPenyewaan`**: Detail item dalam satu transaksi

## Fitur Aplikasi

### Tab 1: Daftar Kendaraan

- Menampilkan semua kendaraan dalam tabel
- Informasi lengkap termasuk ID, nama, jenis, harga, status, dan detail spesifik
- Tombol refresh untuk memperbarui data

### Tab 2: Tambah Kendaraan

- Form untuk menambahkan kendaraan baru
- Dropdown untuk memilih jenis kendaraan (Mobil/Motor/Bus)
- Form dinamis yang berubah sesuai jenis kendaraan
- Validasi input dan pengecekan ID duplikat

### Tab 3: Penyewaan (🔄 IMPROVED USER INTERFACE)

**Step-by-Step Flow with Progressive Disclosure:**

- **Step 1: Customer Information**

  - Modern card-based layout with emoji icons
  - Input validation for customer name and phone number
  - Visual feedback when data is saved

- **Step 2: Vehicle Selection**

  - Enhanced table display with improved formatting
  - Shopping cart metaphor with "Add to Cart" functionality
  - Real-time availability updates
  - Duration selector (1-30 days) with spinner control

- **Step 3: Checkout & Payment**
  - Professional cart display with detailed breakdown
  - Real-time total calculation with currency formatting
  - Multiple action buttons (Complete Transaction, Clear Cart, New Transaction)
  - Comprehensive receipt generation and display

**UI Enhancements:**

- 🎨 Color-coded sections for better visual hierarchy
- 📱 Modern, mobile-inspired interface design
- 🛒 Intuitive shopping cart experience
- ✅ Smart form validation with helpful error messages
- 📄 Professional receipt formatting
- 🔄 Seamless transaction flow

**User Experience Features:**

- Progressive disclosure (steps revealed as user progresses)
- Auto-scrolling to relevant sections
- Button state management (enabled/disabled based on context)
- Confirmation dialogs for important actions
- Professional typography and spacing

## Cara Menjalankan

1. **Kompilasi:**

   ```bash
   javac tugas-3-mareta.java
   ```

2. **Jalankan:**
   ```bash
   java VehicleRentalGUI
   ```

## File yang Dihasilkan

1. **`kendaraan.dat`**: Menyimpan data semua kendaraan (binary file)
2. **`penyewaan.dat`**: Menyimpan history semua transaksi penyewaan
3. **`struk_[ID_TRANSAKSI].txt`**: File struk individual untuk setiap transaksi

## Data Default

Aplikasi akan membuat data default saat pertama kali dijalankan:

- **Mobil**: Toyota Avanza, Honda Jazz
- **Motor**: Honda Beat, Yamaha NMAX
- **Bus**: Mercedes Benz, Isuzu Elf

## Exception Handling

Aplikasi menangani berbagai error:

- Input kosong atau tidak valid
- ID kendaraan duplikat
- Penyewaan kendaraan yang tidak tersedia
- Error file I/O
- Format angka yang salah

## Implementasi OOP

### Abstraksi

```java
abstract class Kendaraan {
    public abstract String tampilKendaraan();
}
```

### Inheritance

```java
class Mobil extends Kendaraan { ... }
class Motor extends Kendaraan { ... }
class Bus extends Kendaraan { ... }
```

### Encapsulation

```java
private String nama;
public String getNama() { return nama; }
public void setNama(String nama) { this.nama = nama; }
```

### Polymorphism

```java
// Setiap kelas override method tampilKendaraan() dengan implementasi berbeda
@Override
public String tampilKendaraan() { ... }
```

## Struktur File & Directory

```
tugas-3/
├── tugas-3-mareta.java     # Source code utama
├── README.md               # Dokumentasi ini
├── kendaraan.dat          # Data kendaraan (auto-generated)
├── penyewaan.dat          # Data penyewaan (auto-generated)
└── struk_*.txt            # File struk (auto-generated)
```

## Catatan Pengembangan

- Aplikasi menggunakan Swing untuk GUI dengan desain modern dan user-friendly
- **UI/UX Improvements**: Redesigned rental interface dengan step-by-step flow, progressive disclosure, dan visual feedback
- Data persistence menggunakan Java Serialization
- Look and feel menggunakan cross-platform untuk kompatibilitas
- Error handling yang komprehensif untuk user experience yang baik
- **Modern Design Elements**: Color-coded sections, emoji integration, card-based layout
- **Professional User Flow**: Shopping cart metaphor, smart validation, auto-scrolling

## Status Pengembangan: ✅ COMPLETE

Aplikasi telah selesai dikembangkan dengan fitur lengkap:

- ✅ Implementasi OOP yang komprehensif
- ✅ GUI yang modern dan user-friendly
- ✅ Exception handling yang robust
- ✅ File I/O operations yang reliable
- ✅ User experience yang profesional
