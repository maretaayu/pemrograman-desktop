import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

// Abstract class Kendaraan
abstract class Kendaraan implements Serializable {
    private String nama;
    private double hargaSewa;
    private String jenis;
    private boolean status; // true = tersedia, false = tidak tersedia
    private String id;

    public Kendaraan(String id, String nama, double hargaSewa, String jenis) {
        this.id = id;
        this.nama = nama;
        this.hargaSewa = hargaSewa;
        this.jenis = jenis;
        this.status = true; // default tersedia
    }

    // Abstract method
    public abstract String tampilKendaraan();
    
    // Getters and Setters (Encapsulation)
    public String getId() { return id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public double getHargaSewa() { return hargaSewa; }
    public void setHargaSewa(double hargaSewa) { this.hargaSewa = hargaSewa; }
    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
    
    public String getStatusText() {
        return status ? "Tersedia" : "Disewa";
    }
}

// Kelas turunan Mobil
class Mobil extends Kendaraan {
    private int kapasitasPenumpang;
    private String transmisi;
    private String bahanBakar;

    public Mobil(String id, String nama, double hargaSewa, int kapasitasPenumpang, String transmisi, String bahanBakar) {
        super(id, nama, hargaSewa, "Mobil");
        this.kapasitasPenumpang = kapasitasPenumpang;
        this.transmisi = transmisi;
        this.bahanBakar = bahanBakar;
    }

    @Override
    public String tampilKendaraan() {
        return String.format("Mobil: %s | Harga: Rp%.0f/hari | Kapasitas: %d orang | Transmisi: %s | Bahan Bakar: %s | Status: %s",
                getNama(), getHargaSewa(), kapasitasPenumpang, transmisi, bahanBakar, getStatusText());
    }

    // Getters
    public int getKapasitasPenumpang() { return kapasitasPenumpang; }
    public String getTransmisi() { return transmisi; }
    public String getBahanBakar() { return bahanBakar; }
}

// Kelas turunan Motor
class Motor extends Kendaraan {
    private int ccMesin;
    private String tipeMesin;

    public Motor(String id, String nama, double hargaSewa, int ccMesin, String tipeMesin) {
        super(id, nama, hargaSewa, "Motor");
        this.ccMesin = ccMesin;
        this.tipeMesin = tipeMesin;
    }

    @Override
    public String tampilKendaraan() {
        return String.format("Motor: %s | Harga: Rp%.0f/hari | CC: %d | Tipe Mesin: %s | Status: %s",
                getNama(), getHargaSewa(), ccMesin, tipeMesin, getStatusText());
    }

    // Getters
    public int getCcMesin() { return ccMesin; }
    public String getTipeMesin() { return tipeMesin; }
}

// Kelas turunan Bus
class Bus extends Kendaraan {
    private int kapasitasPenumpang;
    private String kelasLayanan;
    private boolean acTersedia;

    public Bus(String id, String nama, double hargaSewa, int kapasitasPenumpang, String kelasLayanan, boolean acTersedia) {
        super(id, nama, hargaSewa, "Bus");
        this.kapasitasPenumpang = kapasitasPenumpang;
        this.kelasLayanan = kelasLayanan;
        this.acTersedia = acTersedia;
    }

    @Override
    public String tampilKendaraan() {
        return String.format("Bus: %s | Harga: Rp%.0f/hari | Kapasitas: %d orang | Kelas: %s | AC: %s | Status: %s",
                getNama(), getHargaSewa(), kapasitasPenumpang, kelasLayanan, 
                acTersedia ? "Ya" : "Tidak", getStatusText());
    }

    // Getters
    public int getKapasitasPenumpang() { return kapasitasPenumpang; }
    public String getKelasLayanan() { return kelasLayanan; }
    public boolean isAcTersedia() { return acTersedia; }
}

// Kelas RentalKendaraan untuk mengelola daftar kendaraan
class RentalKendaraan {
    private ArrayList<Kendaraan> daftarKendaraan;
    private final String FILE_KENDARAAN = "kendaraan.dat";

    public RentalKendaraan() {
        daftarKendaraan = new ArrayList<>();
        muatDataKendaraan();
    }

    public void tambahKendaraan(Kendaraan kendaraan) throws Exception {
        // Cek apakah ID sudah ada
        for (Kendaraan k : daftarKendaraan) {
            if (k.getId().equals(kendaraan.getId())) {
                throw new Exception("ID kendaraan sudah ada!");
            }
        }
        daftarKendaraan.add(kendaraan);
        simpanDataKendaraan();
    }

    public ArrayList<Kendaraan> getDaftarKendaraan() {
        return daftarKendaraan;
    }

    public ArrayList<Kendaraan> getDaftarKendaraanTersedia() {
        ArrayList<Kendaraan> tersedia = new ArrayList<>();
        for (Kendaraan k : daftarKendaraan) {
            if (k.isStatus()) {
                tersedia.add(k);
            }
        }
        return tersedia;
    }

    public Kendaraan cariKendaraan(String id) throws Exception {
        for (Kendaraan k : daftarKendaraan) {
            if (k.getId().equals(id)) {
                return k;
            }
        }
        throw new Exception("Kendaraan dengan ID " + id + " tidak ditemukan!");
    }

    // File I/O operations
    public void simpanDataKendaraan() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_KENDARAAN))) {
            oos.writeObject(daftarKendaraan);
        } catch (IOException e) {
            System.err.println("Error menyimpan data kendaraan: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void muatDataKendaraan() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_KENDARAAN))) {
            daftarKendaraan = (ArrayList<Kendaraan>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File belum ada, buat data default
            daftarKendaraan = new ArrayList<>();
            buatDataDefault();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error memuat data kendaraan: " + e.getMessage());
            daftarKendaraan = new ArrayList<>();
            buatDataDefault();
        }
    }

    private void buatDataDefault() {
        try {
            tambahKendaraan(new Mobil("M001", "Toyota Avanza", 300000, 7, "Manual", "Bensin"));
            tambahKendaraan(new Mobil("M002", "Honda Jazz", 350000, 5, "Automatic", "Bensin"));
            tambahKendaraan(new Motor("MT001", "Honda Beat", 75000, 110, "4-Tak"));
            tambahKendaraan(new Motor("MT002", "Yamaha NMAX", 100000, 155, "4-Tak"));
            tambahKendaraan(new Bus("B001", "Mercedes Benz", 800000, 50, "Executive", true));
            tambahKendaraan(new Bus("B002", "Isuzu Elf", 500000, 20, "Economy", false));
        } catch (Exception e) {
            System.err.println("Error membuat data default: " + e.getMessage());
        }
    }
}

// Kelas untuk item penyewaan
class ItemPenyewaan implements Serializable {
    private String idKendaraan;
    private String namaKendaraan;
    private String jenisKendaraan;
    private int jumlahHari;
    private double hargaPerHari;
    private double subtotal;

    public ItemPenyewaan(Kendaraan kendaraan, int jumlahHari) {
        this.idKendaraan = kendaraan.getId();
        this.namaKendaraan = kendaraan.getNama();
        this.jenisKendaraan = kendaraan.getJenis();
        this.jumlahHari = jumlahHari;
        this.hargaPerHari = kendaraan.getHargaSewa();
        this.subtotal = hargaPerHari * jumlahHari;
    }

    // Getters
    public String getIdKendaraan() { return idKendaraan; }
    public String getNamaKendaraan() { return namaKendaraan; }
    public String getJenisKendaraan() { return jenisKendaraan; }
    public int getJumlahHari() { return jumlahHari; }
    public double getHargaPerHari() { return hargaPerHari; }
    public double getSubtotal() { return subtotal; }
}

// Kelas Penyewaan untuk mencatat penyewaan
class Penyewaan implements Serializable {
    private String idPenyewaan;
    private String namaPelanggan;
    private String nomorTelepon;
    private Date tanggalSewa;
    private ArrayList<ItemPenyewaan> itemPenyewaan;
    private double totalBiaya;
    private static final String FILE_PENYEWAAN = "penyewaan.dat";

    public Penyewaan(String namaPelanggan, String nomorTelepon) {
        this.idPenyewaan = generateIdPenyewaan();
        this.namaPelanggan = namaPelanggan;
        this.nomorTelepon = nomorTelepon;
        this.tanggalSewa = new Date();
        this.itemPenyewaan = new ArrayList<>();
        this.totalBiaya = 0;
    }

    private String generateIdPenyewaan() {
        return "TXN" + System.currentTimeMillis();
    }

    public void tambahKendaraan(Kendaraan kendaraan, int jumlahHari) throws Exception {
        if (!kendaraan.isStatus()) {
            throw new Exception("Kendaraan " + kendaraan.getNama() + " sedang tidak tersedia!");
        }
        
        ItemPenyewaan item = new ItemPenyewaan(kendaraan, jumlahHari);
        itemPenyewaan.add(item);
        totalBiaya += item.getSubtotal();
        
        // Update status kendaraan
        kendaraan.setStatus(false);
    }

    public String generateStruk() {
        StringBuilder struk = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        struk.append("========================================\n");
        struk.append("           STRUK PENYEWAAN\n");
        struk.append("========================================\n");
        struk.append("ID Transaksi : ").append(idPenyewaan).append("\n");
        struk.append("Nama Pelanggan : ").append(namaPelanggan).append("\n");
        struk.append("No. Telepon    : ").append(nomorTelepon).append("\n");
        struk.append("Tanggal Sewa   : ").append(sdf.format(tanggalSewa)).append("\n");
        struk.append("========================================\n");
        struk.append("DETAIL PENYEWAAN:\n");
        struk.append("----------------------------------------\n");
        
        for (ItemPenyewaan item : itemPenyewaan) {
            struk.append(String.format("%-20s | %s\n", item.getNamaKendaraan(), item.getJenisKendaraan()));
            struk.append(String.format("Durasi: %d hari x Rp%.0f = Rp%.0f\n", 
                item.getJumlahHari(), item.getHargaPerHari(), item.getSubtotal()));
            struk.append("----------------------------------------\n");
        }
        
        struk.append(String.format("TOTAL BIAYA: Rp%.0f\n", totalBiaya));
        struk.append("========================================\n");
        struk.append("   Terima kasih atas kepercayaan Anda!\n");
        struk.append("========================================\n");
        
        return struk.toString();
    }

    public void simpanStruk() {
        try {
            // Simpan ke file dengan nama berdasarkan ID transaksi
            String fileName = "struk_" + idPenyewaan + ".txt";
            try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
                writer.print(generateStruk());
            }
            
            // Simpan juga ke history penyewaan
            simpanHistoryPenyewaan();
        } catch (IOException e) {
            System.err.println("Error menyimpan struk: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void simpanHistoryPenyewaan() {
        ArrayList<Penyewaan> historyPenyewaan = new ArrayList<>();
        
        // Muat history yang sudah ada
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PENYEWAAN))) {
            historyPenyewaan = (ArrayList<Penyewaan>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File belum ada, buat baru
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error memuat history penyewaan: " + e.getMessage());
        }
        
        // Tambah penyewaan baru
        historyPenyewaan.add(this);
        
        // Simpan kembali
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PENYEWAAN))) {
            oos.writeObject(historyPenyewaan);
        } catch (IOException e) {
            System.err.println("Error menyimpan history penyewaan: " + e.getMessage());
        }
    }

    // Getters
    public String getIdPenyewaan() { return idPenyewaan; }
    public String getNamaPelanggan() { return namaPelanggan; }
    public String getNomorTelepon() { return nomorTelepon; }
    public Date getTanggalSewa() { return tanggalSewa; }
    public ArrayList<ItemPenyewaan> getItemPenyewaan() { return itemPenyewaan; }
    public double getTotalBiaya() { return totalBiaya; }
}

// Main GUI Application
class VehicleRentalGUI extends JFrame {
    private RentalKendaraan rentalKendaraan;
    private JTabbedPane tabbedPane;
    private DefaultTableModel modelKendaraan;
    private JTable tableKendaraan;
    private Penyewaan penyewaanAktif;

    public VehicleRentalGUI() {
        rentalKendaraan = new RentalKendaraan();
        initializeGUI();
        loadDataToTable();
    }

    private void initializeGUI() {
        setTitle("Aplikasi Manajemen Rental Kendaraan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Create tabbed pane
        tabbedPane = new JTabbedPane();

        // Tab 1: Daftar Kendaraan
        tabbedPane.addTab("Daftar Kendaraan", createKendaraanPanel());
        
        // Tab 2: Tambah Kendaraan
        tabbedPane.addTab("Tambah Kendaraan", createTambahKendaraanPanel());
        
        // Tab 3: Penyewaan
        tabbedPane.addTab("Penyewaan", createPenyewaanPanel());

        add(tabbedPane);
        
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createKendaraanPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Title
        JLabel title = new JLabel("Daftar Kendaraan", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(title, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Nama", "Jenis", "Harga/Hari", "Status", "Detail"};
        modelKendaraan = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableKendaraan = new JTable(modelKendaraan);
        tableKendaraan.setRowHeight(25);
        tableKendaraan.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(tableKendaraan);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadDataToTable());
        buttonPanel.add(refreshButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTambahKendaraanPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Title
        JLabel title = new JLabel("Tambah Kendaraan Baru", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(title, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Jenis kendaraan
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Jenis Kendaraan:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> jenisCombo = new JComboBox<>(new String[]{"Mobil", "Motor", "Bus"});
        formPanel.add(jenisCombo, gbc);

        // ID Kendaraan
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("ID Kendaraan:"), gbc);
        gbc.gridx = 1;
        JTextField idField = new JTextField(15);
        formPanel.add(idField, gbc);

        // Nama Kendaraan
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Nama Kendaraan:"), gbc);
        gbc.gridx = 1;
        JTextField namaField = new JTextField(15);
        formPanel.add(namaField, gbc);

        // Harga Sewa
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Harga Sewa/Hari:"), gbc);
        gbc.gridx = 1;
        JTextField hargaField = new JTextField(15);
        formPanel.add(hargaField, gbc);

        // Panel untuk atribut spesifik
        JPanel specificPanel = new JPanel(new GridBagLayout());
        GridBagConstraints specGbc = new GridBagConstraints();
        specGbc.insets = new Insets(5, 5, 5, 5);
        specGbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(specificPanel, gbc);

        // Update specific fields based on vehicle type
        jenisCombo.addActionListener(e -> updateSpecificFields(specificPanel, specGbc, (String) jenisCombo.getSelectedItem()));

        // Initialize with default selection
        updateSpecificFields(specificPanel, specGbc, "Mobil");

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton tambahButton = new JButton("Tambah Kendaraan");
        JButton resetButton = new JButton("Reset Form");
        
        tambahButton.addActionListener(e -> {
            try {
                String jenis = (String) jenisCombo.getSelectedItem();
                String id = idField.getText().trim();
                String nama = namaField.getText().trim();
                String hargaText = hargaField.getText().trim();

                // Validation
                if (id.isEmpty() || nama.isEmpty() || hargaText.isEmpty()) {
                    throw new Exception("Semua field harus diisi!");
                }

                double harga = Double.parseDouble(hargaText);
                if (harga <= 0) {
                    throw new Exception("Harga harus lebih dari 0!");
                }

                Kendaraan kendaraan = createKendaraanFromForm(jenis, id, nama, harga, specificPanel);
                rentalKendaraan.tambahKendaraan(kendaraan);
                
                JOptionPane.showMessageDialog(this, "Kendaraan berhasil ditambahkan!", 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
                // Reset form
                idField.setText("");
                namaField.setText("");
                hargaField.setText("");
                clearSpecificFields(specificPanel);
                
                // Refresh table
                loadDataToTable();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Harga harus berupa angka yang valid!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        resetButton.addActionListener(e -> {
            idField.setText("");
            namaField.setText("");
            hargaField.setText("");
            clearSpecificFields(specificPanel);
        });
        
        buttonPanel.add(tambahButton);
        buttonPanel.add(resetButton);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updateSpecificFields(JPanel panel, GridBagConstraints gbc, String jenis) {
        panel.removeAll();
        
        switch (jenis) {
            case "Mobil":
                // Kapasitas Penumpang
                gbc.gridx = 0; gbc.gridy = 0;
                panel.add(new JLabel("Kapasitas Penumpang:"), gbc);
                gbc.gridx = 1;
                JTextField kapasitasField = new JTextField(10);
                kapasitasField.setName("kapasitas");
                panel.add(kapasitasField, gbc);

                // Transmisi
                gbc.gridx = 0; gbc.gridy = 1;
                panel.add(new JLabel("Transmisi:"), gbc);
                gbc.gridx = 1;
                JComboBox<String> transmisiCombo = new JComboBox<>(new String[]{"Manual", "Automatic"});
                transmisiCombo.setName("transmisi");
                panel.add(transmisiCombo, gbc);

                // Bahan Bakar
                gbc.gridx = 0; gbc.gridy = 2;
                panel.add(new JLabel("Bahan Bakar:"), gbc);
                gbc.gridx = 1;
                JComboBox<String> bahanBakarCombo = new JComboBox<>(new String[]{"Bensin", "Solar", "Hybrid"});
                bahanBakarCombo.setName("bahanBakar");
                panel.add(bahanBakarCombo, gbc);
                break;

            case "Motor":
                // CC Mesin
                gbc.gridx = 0; gbc.gridy = 0;
                panel.add(new JLabel("CC Mesin:"), gbc);
                gbc.gridx = 1;
                JTextField ccField = new JTextField(10);
                ccField.setName("cc");
                panel.add(ccField, gbc);

                // Tipe Mesin
                gbc.gridx = 0; gbc.gridy = 1;
                panel.add(new JLabel("Tipe Mesin:"), gbc);
                gbc.gridx = 1;
                JComboBox<String> tipeMesinCombo = new JComboBox<>(new String[]{"2-Tak", "4-Tak"});
                tipeMesinCombo.setName("tipeMesin");
                panel.add(tipeMesinCombo, gbc);
                break;

            case "Bus":
                // Kapasitas Penumpang
                gbc.gridx = 0; gbc.gridy = 0;
                panel.add(new JLabel("Kapasitas Penumpang:"), gbc);
                gbc.gridx = 1;
                JTextField kapasitasBusField = new JTextField(10);
                kapasitasBusField.setName("kapasitasBus");
                panel.add(kapasitasBusField, gbc);

                // Kelas Layanan
                gbc.gridx = 0; gbc.gridy = 1;
                panel.add(new JLabel("Kelas Layanan:"), gbc);
                gbc.gridx = 1;
                JComboBox<String> kelasCombo = new JComboBox<>(new String[]{"Economy", "Business", "Executive"});
                kelasCombo.setName("kelas");
                panel.add(kelasCombo, gbc);

                // AC
                gbc.gridx = 0; gbc.gridy = 2;
                panel.add(new JLabel("AC Tersedia:"), gbc);
                gbc.gridx = 1;
                JCheckBox acCheckbox = new JCheckBox();
                acCheckbox.setName("ac");
                panel.add(acCheckbox, gbc);
                break;
        }
        
        panel.revalidate();
        panel.repaint();
    }

    private void clearSpecificFields(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JTextField) {
                ((JTextField) comp).setText("");
            } else if (comp instanceof JComboBox) {
                ((JComboBox<?>) comp).setSelectedIndex(0);
            } else if (comp instanceof JCheckBox) {
                ((JCheckBox) comp).setSelected(false);
            }
        }
    }

    private Kendaraan createKendaraanFromForm(String jenis, String id, String nama, double harga, JPanel specificPanel) throws Exception {
        switch (jenis) {
            case "Mobil":
                int kapasitas = Integer.parseInt(getFieldValue(specificPanel, "kapasitas"));
                String transmisi = getFieldValue(specificPanel, "transmisi");
                String bahanBakar = getFieldValue(specificPanel, "bahanBakar");
                return new Mobil(id, nama, harga, kapasitas, transmisi, bahanBakar);

            case "Motor":
                int cc = Integer.parseInt(getFieldValue(specificPanel, "cc"));
                String tipeMesin = getFieldValue(specificPanel, "tipeMesin");
                return new Motor(id, nama, harga, cc, tipeMesin);

            case "Bus":
                int kapasitasBus = Integer.parseInt(getFieldValue(specificPanel, "kapasitasBus"));
                String kelas = getFieldValue(specificPanel, "kelas");
                boolean ac = Boolean.parseBoolean(getFieldValue(specificPanel, "ac"));
                return new Bus(id, nama, harga, kapasitasBus, kelas, ac);

            default:
                throw new Exception("Jenis kendaraan tidak valid!");
        }
    }

    private String getFieldValue(JPanel panel, String fieldName) {
        for (Component comp : panel.getComponents()) {
            if (comp.getName() != null && comp.getName().equals(fieldName)) {
                if (comp instanceof JTextField) {
                    return ((JTextField) comp).getText();
                } else if (comp instanceof JComboBox) {
                    return (String) ((JComboBox<?>) comp).getSelectedItem();
                } else if (comp instanceof JCheckBox) {
                    return String.valueOf(((JCheckBox) comp).isSelected());
                }
            }
        }
        return "";
    }

    private JPanel createPenyewaanPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Title
        JLabel title = new JLabel("🚗 Penyewaan Kendaraan - Mudah & Cepat", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(0, 102, 204));
        title.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        panel.add(title, BorderLayout.NORTH);

        // Create a card-layout style main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Step 1: Customer Information Card
        JPanel step1Panel = createStepPanel("LANGKAH 1", "Masukkan Data Pelanggan", new Color(52, 152, 219));
        JPanel customerForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        customerForm.add(new JLabel("👤 Nama Pelanggan:"), gbc);
        gbc.gridx = 1;
        JTextField namaCustomerField = new JTextField(25);
        namaCustomerField.setFont(new Font("Arial", Font.PLAIN, 14));
        customerForm.add(namaCustomerField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        customerForm.add(new JLabel("📱 No. Telepon:"), gbc);
        gbc.gridx = 1;
        JTextField teleponField = new JTextField(25);
        teleponField.setFont(new Font("Arial", Font.PLAIN, 14));
        customerForm.add(teleponField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton startButton = new JButton("▶ Mulai Penyewaan");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setBackground(new Color(46, 204, 113));
        startButton.setForeground(Color.WHITE);
        startButton.setPreferredSize(new Dimension(200, 35));
        customerForm.add(startButton, gbc);

        step1Panel.add(customerForm, BorderLayout.CENTER);
        mainPanel.add(step1Panel);
        mainPanel.add(Box.createVerticalStrut(15));

        // Step 2: Vehicle Selection Card
        JPanel step2Panel = createStepPanel("LANGKAH 2", "Pilih Kendaraan yang Tersedia", new Color(155, 89, 182));
        step2Panel.setVisible(false); // Initially hidden

        // Instructions label
        JLabel instructionLabel = new JLabel("💡 Klik pada kendaraan di tabel, atur durasi sewa, lalu klik 'Tambah ke Keranjang'");
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        instructionLabel.setForeground(new Color(127, 140, 141));
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        step2Panel.add(instructionLabel, BorderLayout.NORTH);

        // Available vehicles table
        String[] vehicleColumns = {"ID", "Nama Kendaraan", "Jenis", "Harga/Hari", "Detail Spesifikasi"};
        DefaultTableModel modelTersedia = new DefaultTableModel(vehicleColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable tableTersedia = new JTable(modelTersedia);
        tableTersedia.setRowHeight(30);
        tableTersedia.setFont(new Font("Arial", Font.PLAIN, 12));
        tableTersedia.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableTersedia.getTableHeader().setBackground(new Color(236, 240, 241));
        tableTersedia.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableTersedia.setSelectionBackground(new Color(174, 214, 241));
        
        JScrollPane scrollTersedia = new JScrollPane(tableTersedia);
        scrollTersedia.setPreferredSize(new Dimension(0, 200));
        step2Panel.add(scrollTersedia, BorderLayout.CENTER);

        // Vehicle selection controls
        JPanel selectionControls = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        selectionControls.setBackground(new Color(248, 249, 250));
        selectionControls.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        
        selectionControls.add(new JLabel("📅 Durasi Sewa:"));
        JSpinner durasiSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 30, 1));
        durasiSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        ((JSpinner.DefaultEditor) durasiSpinner.getEditor()).getTextField().setColumns(3);
        selectionControls.add(durasiSpinner);
        selectionControls.add(new JLabel("hari"));
        
        JButton addToCartButton = new JButton("🛒 Tambah ke Keranjang");
        addToCartButton.setFont(new Font("Arial", Font.BOLD, 12));
        addToCartButton.setBackground(new Color(52, 152, 219));
        addToCartButton.setForeground(Color.WHITE);
        addToCartButton.setEnabled(false);
        selectionControls.add(addToCartButton);
        
        step2Panel.add(selectionControls, BorderLayout.SOUTH);
        mainPanel.add(step2Panel);
        mainPanel.add(Box.createVerticalStrut(15));

        // Step 3: Shopping Cart and Checkout
        JPanel step3Panel = createStepPanel("LANGKAH 3", "Keranjang Belanja & Pembayaran", new Color(230, 126, 34));
        step3Panel.setVisible(false); // Initially hidden

        // Shopping cart area
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("🛒 Keranjang Belanja"));
        
        JTextArea cartArea = new JTextArea(6, 50);
        cartArea.setEditable(false);
        cartArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        cartArea.setBackground(new Color(253, 254, 255));
        cartArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cartArea.setText("Keranjang belanja kosong. Silakan pilih kendaraan terlebih dahulu.");
        JScrollPane cartScroll = new JScrollPane(cartArea);
        cartPanel.add(cartScroll, BorderLayout.CENTER);

        // Total and action buttons
        JPanel checkoutPanel = new JPanel(new BorderLayout());
        
        JLabel totalLabel = new JLabel("💰 Total Biaya: Rp 0", SwingConstants.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(192, 57, 43));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        checkoutPanel.add(totalLabel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton checkoutButton = new JButton("💳 Selesai & Cetak Struk");
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        checkoutButton.setBackground(new Color(46, 204, 113));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setPreferredSize(new Dimension(200, 40));
        checkoutButton.setEnabled(false);
        
        JButton clearCartButton = new JButton("🗑️ Kosongkan Keranjang");
        clearCartButton.setFont(new Font("Arial", Font.PLAIN, 12));
        clearCartButton.setBackground(new Color(231, 76, 60));
        clearCartButton.setForeground(Color.WHITE);
        clearCartButton.setEnabled(false);
        
        JButton newTransactionButton = new JButton("🔄 Transaksi Baru");
        newTransactionButton.setFont(new Font("Arial", Font.PLAIN, 12));
        newTransactionButton.setBackground(new Color(149, 165, 166));
        newTransactionButton.setForeground(Color.WHITE);
        
        buttonPanel.add(checkoutButton);
        buttonPanel.add(clearCartButton);
        buttonPanel.add(newTransactionButton);
        checkoutPanel.add(buttonPanel, BorderLayout.CENTER);
        
        cartPanel.add(checkoutPanel, BorderLayout.SOUTH);
        step3Panel.add(cartPanel, BorderLayout.CENTER);
        mainPanel.add(step3Panel);
        cartPanel.add(checkoutPanel, BorderLayout.SOUTH);
        step3Panel.add(cartPanel, BorderLayout.CENTER);
        mainPanel.add(step3Panel);

        // Add main panel to scroll pane
        JScrollPane mainScroll = new JScrollPane(mainPanel);
        mainScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(mainScroll, BorderLayout.CENTER);

        // Event handlers with improved UX flow
        startButton.addActionListener(e -> {
            try {
                String nama = namaCustomerField.getText().trim();
                String telepon = teleponField.getText().trim();
                
                if (nama.isEmpty() || telepon.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "⚠️ Mohon lengkapi nama dan nomor telepon terlebih dahulu!", 
                        "Data Belum Lengkap", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                penyewaanAktif = new Penyewaan(nama, telepon);
                
                // Show success and reveal step 2
                step2Panel.setVisible(true);
                step3Panel.setVisible(true);
                startButton.setText("✅ Data Tersimpan");
                startButton.setEnabled(false);
                startButton.setBackground(new Color(149, 165, 166));
                
                // Load available vehicles
                loadAvailableVehicles(modelTersedia);
                
                // Update cart
                cartArea.setText("🎉 Penyewaan dimulai!\n" +
                              "Pelanggan: " + nama + "\n" +
                              "Telepon: " + telepon + "\n" +
                              "ID Transaksi: " + penyewaanAktif.getIdPenyewaan() + "\n\n" +
                              "Silakan pilih kendaraan dari tabel di atas...");
                
                // Auto scroll to step 2
                SwingUtilities.invokeLater(() -> {
                    step2Panel.scrollRectToVisible(step2Panel.getBounds());
                });
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Table selection listener
        tableTersedia.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                addToCartButton.setEnabled(tableTersedia.getSelectedRow() != -1 && penyewaanAktif != null);
            }
        });

        addToCartButton.addActionListener(e -> {
            try {
                if (penyewaanAktif == null) {
                    JOptionPane.showMessageDialog(this, 
                        "⚠️ Silakan mulai penyewaan terlebih dahulu!", 
                        "Belum Memulai Penyewaan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int selectedRow = tableTersedia.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, 
                        "⚠️ Pilih kendaraan terlebih dahulu!", 
                        "Belum Memilih Kendaraan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                String idKendaraan = (String) modelTersedia.getValueAt(selectedRow, 0);
                int durasi = (Integer) durasiSpinner.getValue();
                
                Kendaraan kendaraan = rentalKendaraan.cariKendaraan(idKendaraan);
                penyewaanAktif.tambahKendaraan(kendaraan, durasi);
                
                // Update cart display
                updateCartDisplay(cartArea, totalLabel);
                
                // Enable checkout button
                checkoutButton.setEnabled(true);
                clearCartButton.setEnabled(true);
                
                // Refresh available vehicles
                loadAvailableVehicles(modelTersedia);
                rentalKendaraan.simpanDataKendaraan();
                loadDataToTable(); // Refresh main table
                
                // Show success message
                JOptionPane.showMessageDialog(this, 
                    "✅ " + kendaraan.getNama() + " berhasil ditambahkan ke keranjang!", 
                    "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        checkoutButton.addActionListener(e -> {
            try {
                if (penyewaanAktif == null || penyewaanAktif.getItemPenyewaan().isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "⚠️ Keranjang kosong! Pilih kendaraan terlebih dahulu.", 
                        "Keranjang Kosong", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Generate and save receipt
                penyewaanAktif.simpanStruk();
                
                // Show receipt in a nice dialog
                JTextArea strukArea = new JTextArea(25, 60);
                strukArea.setText(penyewaanAktif.generateStruk());
                strukArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
                strukArea.setEditable(false);
                strukArea.setBackground(Color.WHITE);
                
                JScrollPane strukScroll = new JScrollPane(strukArea);
                JOptionPane.showMessageDialog(this, strukScroll, 
                    "🧾 Struk Penyewaan - " + penyewaanAktif.getIdPenyewaan(), 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Show success message
                JOptionPane.showMessageDialog(this, 
                    "✅ Transaksi berhasil!\n💾 Struk telah disimpan ke file: struk_" + 
                    penyewaanAktif.getIdPenyewaan() + ".txt", 
                    "Transaksi Berhasil", JOptionPane.INFORMATION_MESSAGE);
                
                // Reset for new transaction
                resetRentalForm(namaCustomerField, teleponField, cartArea, totalLabel, 
                              startButton, step2Panel, step3Panel, checkoutButton, 
                              clearCartButton, addToCartButton, modelTersedia);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        clearCartButton.addActionListener(e -> {
            if (penyewaanAktif != null) {
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "🗑️ Yakin ingin mengosongkan keranjang?\nSemua kendaraan akan dikembalikan ke status tersedia.", 
                    "Konfirmasi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    // Return vehicles to available status
                    for (ItemPenyewaan item : penyewaanAktif.getItemPenyewaan()) {
                        try {
                            Kendaraan k = rentalKendaraan.cariKendaraan(item.getIdKendaraan());
                            k.setStatus(true);
                        } catch (Exception ex) {
                            // Handle error silently
                        }
                    }
                    rentalKendaraan.simpanDataKendaraan();
                    loadDataToTable();
                    
                    // Clear cart
                    penyewaanAktif.getItemPenyewaan().clear();
                    updateCartDisplay(cartArea, totalLabel);
                    loadAvailableVehicles(modelTersedia);
                    
                    checkoutButton.setEnabled(false);
                    clearCartButton.setEnabled(false);
                }
            }
        });

        newTransactionButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "🔄 Mulai transaksi baru?\nData saat ini akan dihapus.", 
                "Konfirmasi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Return any unreturned vehicles
                if (penyewaanAktif != null) {
                    for (ItemPenyewaan item : penyewaanAktif.getItemPenyewaan()) {
                        try {
                            Kendaraan k = rentalKendaraan.cariKendaraan(item.getIdKendaraan());
                            k.setStatus(true);
                        } catch (Exception ex) {
                            // Handle error silently
                        }
                    }
                    rentalKendaraan.simpanDataKendaraan();
                    loadDataToTable();
                }
                
                resetRentalForm(namaCustomerField, teleponField, cartArea, totalLabel, 
                              startButton, step2Panel, step3Panel, checkoutButton, 
                              clearCartButton, addToCartButton, modelTersedia);
            }
        });

        // Initialize available vehicles
        loadAvailableVehicles(modelTersedia);

        return panel;
    }

    private JPanel createStepPanel(String stepNumber, String stepTitle, Color headerColor) {
        JPanel stepPanel = new JPanel(new BorderLayout());
        stepPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedSoftBevelBorder(),
            BorderFactory.createEmptyBorder(10, 15, 15, 15)
        ));
        stepPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(headerColor);
        header.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        
        JLabel stepLabel = new JLabel(stepNumber);
        stepLabel.setFont(new Font("Arial", Font.BOLD, 14));
        stepLabel.setForeground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(stepTitle);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        
        header.add(stepLabel, BorderLayout.WEST);
        header.add(titleLabel, BorderLayout.CENTER);
        
        stepPanel.add(header, BorderLayout.NORTH);
        
        return stepPanel;
    }

    private void updateCartDisplay(JTextArea cartArea, JLabel totalLabel) {
        if (penyewaanAktif == null || penyewaanAktif.getItemPenyewaan().isEmpty()) {
            cartArea.setText("Keranjang belanja kosong. Silakan pilih kendaraan terlebih dahulu.");
            totalLabel.setText("💰 Total Biaya: Rp 0");
            return;
        }
        
        StringBuilder cart = new StringBuilder();
        cart.append("🛒 KERANJANG BELANJA\n");
        cart.append("═══════════════════════════════════════════════════\n");
        cart.append("Pelanggan: ").append(penyewaanAktif.getNamaPelanggan()).append("\n");
        cart.append("ID Transaksi: ").append(penyewaanAktif.getIdPenyewaan()).append("\n\n");
        
        int itemNo = 1;
        for (ItemPenyewaan item : penyewaanAktif.getItemPenyewaan()) {
            cart.append(String.format("%d. %s (%s)\n", itemNo++, 
                item.getNamaKendaraan(), item.getJenisKendaraan()));
            cart.append(String.format("   Durasi: %d hari × Rp%,.0f = Rp%,.0f\n\n", 
                item.getJumlahHari(), item.getHargaPerHari(), item.getSubtotal()));
        }
        
        cart.append("═══════════════════════════════════════════════════\n");
        cart.append(String.format("TOTAL: Rp%,.0f", penyewaanAktif.getTotalBiaya()));
        
        cartArea.setText(cart.toString());
        totalLabel.setText(String.format("💰 Total Biaya: Rp%,.0f", penyewaanAktif.getTotalBiaya()));
    }

    private void resetRentalForm(JTextField namaField, JTextField teleponField, 
                               JTextArea cartArea, JLabel totalLabel, JButton startButton,
                               JPanel step2Panel, JPanel step3Panel, JButton checkoutButton,
                               JButton clearCartButton, JButton addToCartButton, 
                               DefaultTableModel modelTersedia) {
        penyewaanAktif = null;
        namaField.setText("");
        teleponField.setText("");
        cartArea.setText("Keranjang belanja kosong. Silakan pilih kendaraan terlebih dahulu.");
        totalLabel.setText("💰 Total Biaya: Rp 0");
        
        startButton.setText("▶ Mulai Penyewaan");
        startButton.setEnabled(true);
        startButton.setBackground(new Color(46, 204, 113));
        
        step2Panel.setVisible(false);
        step3Panel.setVisible(false);
        checkoutButton.setEnabled(false);
        clearCartButton.setEnabled(false);
        addToCartButton.setEnabled(false);
        
        loadAvailableVehicles(modelTersedia);    }

    private void loadDataToTable() {
        modelKendaraan.setRowCount(0);
        for (Kendaraan k : rentalKendaraan.getDaftarKendaraan()) {
            modelKendaraan.addRow(new Object[]{
                k.getId(),
                k.getNama(),
                k.getJenis(),
                String.format("Rp%.0f", k.getHargaSewa()),
                k.getStatusText(),
                getKendaraanDetail(k)
            });
        }
    }

    private void loadAvailableVehicles(DefaultTableModel model) {
        model.setRowCount(0);
        for (Kendaraan k : rentalKendaraan.getDaftarKendaraanTersedia()) {
            model.addRow(new Object[]{
                k.getId(),
                k.getNama(),
                k.getJenis(),
                String.format("Rp%.0f", k.getHargaSewa()),
                getKendaraanDetail(k)
            });
        }
    }

    private String getKendaraanDetail(Kendaraan k) {
        if (k instanceof Mobil) {
            Mobil m = (Mobil) k;
            return String.format("Kapasitas: %d, %s, %s", 
                m.getKapasitasPenumpang(), m.getTransmisi(), m.getBahanBakar());
        } else if (k instanceof Motor) {
            Motor m = (Motor) k;
            return String.format("CC: %d, %s", m.getCcMesin(), m.getTipeMesin());
        } else if (k instanceof Bus) {
            Bus b = (Bus) k;
            return String.format("Kapasitas: %d, %s, AC: %s", 
                b.getKapasitasPenumpang(), b.getKelasLayanan(), 
                b.isAcTersedia() ? "Ya" : "Tidak");
        }
        return "";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new VehicleRentalGUI().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error starting application: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}