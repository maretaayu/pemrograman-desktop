import java.util.Scanner;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * Kelas Buku untuk merepresentasikan buku perpustakaan
 */
class Buku {
    private String judul;
    private String penulis;
    private String kategori;
    private boolean tersedia;
    private LocalDate tanggalPinjam;
    
    // Constructor
    public Buku(String judul, String penulis, String kategori, boolean tersedia) {
        this.judul = judul;
        this.penulis = penulis;
        this.kategori = kategori;
        this.tersedia = tersedia;
        this.tanggalPinjam = null;
    }
    
    // Getter dan Setter
    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }
    
    public String getPenulis() { return penulis; }
    public void setPenulis(String penulis) { this.penulis = penulis; }
    
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    
    public boolean isTersedia() { return tersedia; }
    public void setTersedia(boolean tersedia) { this.tersedia = tersedia; }
    
    public LocalDate getTanggalPinjam() { return tanggalPinjam; }
    public void setTanggalPinjam(LocalDate tanggalPinjam) { this.tanggalPinjam = tanggalPinjam; }
    
    @Override
    public String toString() {
        String status = tersedia ? "Tersedia" : "Dipinjam";
        return String.format("%-30s | %-20s | %-10s | %s", 
                           judul, penulis, kategori, status);
    }
}

/**
 * Kelas Main untuk Aplikasi Manajemen Perpustakaan dengan GUI
 */
class Main extends JFrame {
    private static List<Buku> perpustakaan = new ArrayList<>();
    private static List<Buku> bukuDipinjam = new ArrayList<>();
    private JTable tableBuku;
    private DefaultTableModel tableModel;
    private JTextArea textAreaInfo;
    
    public Main() {
        inisialisasiDataBuku();
        initializeGUI();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Use default look and feel if system L&F is not available
            }
            new Main().setVisible(true);
        });
    }
    
    /**
     * Inisialisasi data buku perpustakaan
     */
    private static void inisialisasiDataBuku() {
        // Kategori Fiksi (4 buku)
        perpustakaan.add(new Buku("Harry Potter dan Batu Bertuah", "J.K. Rowling", "Fiksi", true));
        perpustakaan.add(new Buku("Laskar Pelangi", "Andrea Hirata", "Fiksi", true));
        perpustakaan.add(new Buku("Bumi Manusia", "Pramoedya Ananta Toer", "Fiksi", true));
        perpustakaan.add(new Buku("5 cm", "Donny Dhirgantoro", "Fiksi", true));
        
        // Kategori Non-Fiksi (4 buku)
        perpustakaan.add(new Buku("Sapiens", "Yuval Noah Harari", "Non-Fiksi", true));
        perpustakaan.add(new Buku("The 7 Habits of Highly Effective People", "Stephen Covey", "Non-Fiksi", true));
        perpustakaan.add(new Buku("Atomic Habits", "James Clear", "Non-Fiksi", true));
        perpustakaan.add(new Buku("How to Win Friends and Influence People", "Dale Carnegie", "Non-Fiksi", true));
        
        // Kategori Teknologi (4 buku)
        perpustakaan.add(new Buku("Clean Code", "Robert C. Martin", "Teknologi", true));
        perpustakaan.add(new Buku("Algoritma dan Pemrograman", "Rinaldi Munir", "Teknologi", true));
        perpustakaan.add(new Buku("Design Patterns", "Gang of Four", "Teknologi", true));
        perpustakaan.add(new Buku("Introduction to Algorithms", "Thomas H. Cormen", "Teknologi", true));
        
        // Kategori Sejarah (4 buku)
        perpustakaan.add(new Buku("Sejarah Indonesia Modern", "M.C. Ricklefs", "Sejarah", true));
        perpustakaan.add(new Buku("The Silk Roads", "Peter Frankopan", "Sejarah", true));
        perpustakaan.add(new Buku("Guns, Germs, and Steel", "Jared Diamond", "Sejarah", true));
        perpustakaan.add(new Buku("A Brief History of Time", "Stephen Hawking", "Sejarah", true));
    }
    
    /**
     * Inisialisasi GUI
     */
    private void initializeGUI() {
        setTitle("Aplikasi Manajemen Perpustakaan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Create menu bar
        createMenuBar();
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Create table for books
        createBookTable();
        JScrollPane scrollPane = new JScrollPane(tableBuku);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Buku Perpustakaan"));
        
        // Create info panel
        textAreaInfo = new JTextArea(8, 50);
        textAreaInfo.setEditable(false);
        textAreaInfo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textAreaInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane infoScrollPane = new JScrollPane(textAreaInfo);
        infoScrollPane.setBorder(BorderFactory.createTitledBorder("Informasi & Statistik"));
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Add components to main panel
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(infoScrollPane, BorderLayout.SOUTH);
        mainPanel.add(buttonPanel, BorderLayout.EAST);
        
        add(mainPanel);
        
        // Update display
        updateBookTable();
        updateStatistik();
    }
    
    /**
     * Create menu bar
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Keluar");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        // Admin menu
        JMenu adminMenu = new JMenu("Admin");
        JMenuItem tambahItem = new JMenuItem("Tambah Buku");
        JMenuItem ubahItem = new JMenuItem("Ubah Buku");
        JMenuItem hapusItem = new JMenuItem("Hapus Buku");
        
        tambahItem.addActionListener(e -> tambahBukuDialog());
        ubahItem.addActionListener(e -> ubahBukuDialog());
        hapusItem.addActionListener(e -> hapusBukuDialog());
        
        adminMenu.add(tambahItem);
        adminMenu.add(ubahItem);
        adminMenu.add(hapusItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("Tentang");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, 
            "Aplikasi Manajemen Perpustakaan\nTugas Praktik 2\nPemrograman Berbasis Desktop", 
            "Tentang", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(adminMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * Create book table
     */
    private void createBookTable() {
        String[] columnNames = {"No", "Judul", "Penulis", "Kategori", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableBuku = new JTable(tableModel);
        tableBuku.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableBuku.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        tableBuku.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableBuku.getColumnModel().getColumn(1).setPreferredWidth(300);
        tableBuku.getColumnModel().getColumn(2).setPreferredWidth(200);
        tableBuku.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableBuku.getColumnModel().getColumn(4).setPreferredWidth(100);
    }
    
    /**
     * Create button panel
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnPinjam = new JButton("Pinjam Buku");
        JButton btnKembali = new JButton("Kembalikan Buku");
        JButton btnRefresh = new JButton("Refresh Data");
        JButton btnBukuDipinjam = new JButton("Lihat Buku Dipinjam");
        JButton btnStatistik = new JButton("Update Statistik");
        
        btnPinjam.addActionListener(e -> pinjamBukuDialog());
        btnKembali.addActionListener(e -> kembalikanBukuDialog());
        btnRefresh.addActionListener(e -> {
            updateBookTable();
            updateStatistik();
        });
        btnBukuDipinjam.addActionListener(e -> lihatBukuDipinjamDialog());
        btnStatistik.addActionListener(e -> updateStatistik());
        
        panel.add(btnPinjam);
        panel.add(btnKembali);
        panel.add(btnBukuDipinjam);
        panel.add(btnStatistik);
        panel.add(btnRefresh);
        
        return panel;
    }
    
    /**
     * Update book table
     */
    private void updateBookTable() {
        tableModel.setRowCount(0);
        
        for (int i = 0; i < perpustakaan.size(); i++) {
            Buku buku = perpustakaan.get(i);
            Object[] rowData = {
                i + 1,
                buku.getJudul(),
                buku.getPenulis(),
                buku.getKategori(),
                buku.isTersedia() ? "Tersedia" : "Dipinjam"
            };
            tableModel.addRow(rowData);
        }
    }
    
    /**
     * Update statistik
     */
    private void updateStatistik() {
        StringBuilder sb = new StringBuilder();
        sb.append("STATISTIK PERPUSTAKAAN\n");
        sb.append("=".repeat(50)).append("\n\n");
        
        String[] kategoris = {"Fiksi", "Non-Fiksi", "Teknologi", "Sejarah"};
        
        for (String kategori : kategoris) {
            int total = 0;
            int tersedia = 0;
            
            for (Buku buku : perpustakaan) {
                if (buku.getKategori().equals(kategori)) {
                    total++;
                    if (buku.isTersedia()) {
                        tersedia++;
                    }
                }
            }
            
            sb.append(String.format("%-12s: %d total, %d tersedia, %d dipinjam\n", 
                    kategori, total, tersedia, (total - tersedia)));
        }
        
        sb.append("\nTotal Buku: ").append(perpustakaan.size()).append("\n");
        sb.append("Total Dipinjam: ").append(bukuDipinjam.size()).append("\n");
        sb.append("Total Tersedia: ").append(perpustakaan.size() - bukuDipinjam.size());
        
        textAreaInfo.setText(sb.toString());
    }
    
    /**
     * Dialog peminjaman buku
     */
    private void pinjamBukuDialog() {
        List<Buku> bukuTersedia = new ArrayList<>();
        for (Buku buku : perpustakaan) {
            if (buku.isTersedia()) {
                bukuTersedia.add(buku);
            }
        }
        
        if (bukuTersedia.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tidak ada buku yang tersedia untuk dipinjam!", 
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] bukuOptions = new String[bukuTersedia.size()];
        for (int i = 0; i < bukuTersedia.size(); i++) {
            bukuOptions[i] = bukuTersedia.get(i).getJudul() + " - " + bukuTersedia.get(i).getPenulis();
        }
        
        List<Buku> bukuDipinjamSesi = new ArrayList<>();
        
        while (true) {
            String selectedBook = (String) JOptionPane.showInputDialog(this,
                    "Pilih buku yang ingin dipinjam:",
                    "Peminjaman Buku",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    bukuOptions,
                    bukuOptions[0]);
            
            if (selectedBook == null) break;
            
            // Find selected book
            Buku bukuDipilih = null;
            for (Buku buku : bukuTersedia) {
                if ((buku.getJudul() + " - " + buku.getPenulis()).equals(selectedBook)) {
                    bukuDipilih = buku;
                    break;
                }
            }
            
            if (bukuDipilih != null) {
                bukuDipilih.setTersedia(false);
                bukuDipilih.setTanggalPinjam(LocalDate.now());
                bukuDipinjam.add(bukuDipilih);
                bukuDipinjamSesi.add(bukuDipilih);
                bukuTersedia.remove(bukuDipilih);
                
                // Update options
                bukuOptions = new String[bukuTersedia.size()];
                for (int i = 0; i < bukuTersedia.size(); i++) {
                    bukuOptions[i] = bukuTersedia.get(i).getJudul() + " - " + bukuTersedia.get(i).getPenulis();
                }
                
                int choice = JOptionPane.showConfirmDialog(this,
                        "Buku '" + bukuDipilih.getJudul() + "' berhasil dipinjam!\n" +
                        "Ingin meminjam buku lain?",
                        "Sukses",
                        JOptionPane.YES_NO_OPTION);
                
                if (choice != JOptionPane.YES_OPTION || bukuTersedia.isEmpty()) {
                    break;
                }
            }
        }
        
        if (!bukuDipinjamSesi.isEmpty()) {
            tampilkanStrukPeminjaman(bukuDipinjamSesi);
        }
        
        updateBookTable();
        updateStatistik();
    }
    
    /**
     * Dialog lihat buku dipinjam
     */
    private void lihatBukuDipinjamDialog() {
        if (bukuDipinjam.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tidak ada buku yang sedang dipinjam!", 
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("BUKU YANG SEDANG DIPINJAM\n");
        sb.append("=".repeat(50)).append("\n\n");
        
        for (int i = 0; i < bukuDipinjam.size(); i++) {
            Buku buku = bukuDipinjam.get(i);
            long hariPinjam = ChronoUnit.DAYS.between(buku.getTanggalPinjam(), LocalDate.now());
            
            sb.append(String.format("%d. %s\n", i + 1, buku.getJudul()));
            sb.append(String.format("   Penulis: %s\n", buku.getPenulis()));
            sb.append(String.format("   Tanggal Pinjam: %s\n", buku.getTanggalPinjam()));
            sb.append(String.format("   Durasi: %d hari\n", hariPinjam));
            
            if (hariPinjam > 7) {
                long denda = (hariPinjam - 7) * 5000;
                sb.append(String.format("   ⚠️ TERLAMBAT %d hari - Denda: Rp. %,d\n", 
                        hariPinjam - 7, denda));
            }
            sb.append("\n");
        }
        
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Buku yang Dipinjam", 
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Dialog pengembalian buku
     */
    private void kembalikanBukuDialog() {
        if (bukuDipinjam.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tidak ada buku yang sedang dipinjam!", 
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] bukuOptions = new String[bukuDipinjam.size()];
        for (int i = 0; i < bukuDipinjam.size(); i++) {
            Buku buku = bukuDipinjam.get(i);
            long hariPinjam = ChronoUnit.DAYS.between(buku.getTanggalPinjam(), LocalDate.now());
            bukuOptions[i] = buku.getJudul() + " - " + buku.getPenulis() + " (" + hariPinjam + " hari)";
        }
        
        String selectedBook = (String) JOptionPane.showInputDialog(this,
                "Pilih buku yang ingin dikembalikan:",
                "Pengembalian Buku",
                JOptionPane.QUESTION_MESSAGE,
                null,
                bukuOptions,
                bukuOptions[0]);
        
        if (selectedBook != null) {
            // Find selected book
            Buku bukuKembali = null;
            for (Buku buku : bukuDipinjam) {
                long hariPinjam = ChronoUnit.DAYS.between(buku.getTanggalPinjam(), LocalDate.now());
                if ((buku.getJudul() + " - " + buku.getPenulis() + " (" + hariPinjam + " hari)").equals(selectedBook)) {
                    bukuKembali = buku;
                    break;
                }
            }
            
            if (bukuKembali != null) {
                // Hitung denda
                long hariPinjam = ChronoUnit.DAYS.between(bukuKembali.getTanggalPinjam(), LocalDate.now());
                long hariTerlambat = Math.max(0, hariPinjam - 7);
                long totalDenda = hariTerlambat * 5000;
                
                // Kembalikan buku
                bukuKembali.setTersedia(true);
                bukuKembali.setTanggalPinjam(null);
                bukuDipinjam.remove(bukuKembali);
                
                // Tampilkan struk pengembalian
                tampilkanStrukPengembalian(bukuKembali, hariPinjam, hariTerlambat, totalDenda);
                
                updateBookTable();
                updateStatistik();
            }
        }
    }
    
    /**
     * Tampilkan struk peminjaman
     */
    private void tampilkanStrukPeminjaman(List<Buku> bukuDipinjamSesi) {
        StringBuilder sb = new StringBuilder();
        sb.append("STRUK PEMINJAMAN\n");
        sb.append("=".repeat(40)).append("\n\n");
        sb.append("Tanggal: ").append(LocalDate.now()).append("\n");
        sb.append("Batas Pengembalian: ").append(LocalDate.now().plusDays(7)).append("\n\n");
        sb.append("Buku yang dipinjam:\n");
        sb.append("-".repeat(40)).append("\n");
        
        for (int i = 0; i < bukuDipinjamSesi.size(); i++) {
            Buku buku = bukuDipinjamSesi.get(i);
            sb.append(String.format("%d. %s\n", i + 1, buku.getJudul()));
            sb.append(String.format("   Penulis: %s\n", buku.getPenulis()));
            sb.append("   Status: Dipinjam\n\n");
        }
        
        sb.append("-".repeat(40)).append("\n");
        sb.append("Total buku dipinjam: ").append(bukuDipinjamSesi.size()).append("\n");
        sb.append("Denda keterlambatan: Rp. 5.000,- per hari setelah 7 hari\n");
        sb.append("Terima kasih! Jangan lupa mengembalikan tepat waktu.");
        
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Struk Peminjaman", 
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Tampilkan struk pengembalian
     */
    private void tampilkanStrukPengembalian(Buku buku, long hariPinjam, long hariTerlambat, long totalDenda) {
        StringBuilder sb = new StringBuilder();
        sb.append("STRUK PENGEMBALIAN\n");
        sb.append("=".repeat(40)).append("\n\n");
        sb.append("Tanggal Pengembalian: ").append(LocalDate.now()).append("\n\n");
        sb.append("Buku yang dikembalikan:\n");
        sb.append("-".repeat(40)).append("\n");
        sb.append("Judul: ").append(buku.getJudul()).append("\n");
        sb.append("Penulis: ").append(buku.getPenulis()).append("\n");
        sb.append("Status: Tersedia\n\n");
        sb.append("-".repeat(40)).append("\n");
        sb.append("Durasi peminjaman: ").append(hariPinjam).append(" hari\n");
        
        if (hariTerlambat > 0) {
            sb.append("Hari keterlambatan: ").append(hariTerlambat).append(" hari\n");
            sb.append("Total Denda: Rp. ").append(String.format("%,d", totalDenda)).append("\n");
        } else {
            sb.append("✓ Dikembalikan tepat waktu, tidak ada denda.\n");
        }
        
        sb.append("-".repeat(40)).append("\n");
        sb.append("Terima kasih telah mengembalikan buku!");
        
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 350));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Struk Pengembalian", 
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Dialog tambah buku baru
     */
    private void tambahBukuDialog() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField judulField = new JTextField(20);
        JTextField penulisField = new JTextField(20);
        String[] kategoris = {"Fiksi", "Non-Fiksi", "Teknologi", "Sejarah"};
        JComboBox<String> kategoriCombo = new JComboBox<>(kategoris);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Judul Buku:"), gbc);
        gbc.gridx = 1;
        panel.add(judulField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Penulis:"), gbc);
        gbc.gridx = 1;
        panel.add(penulisField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Kategori:"), gbc);
        gbc.gridx = 1;
        panel.add(kategoriCombo, gbc);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Tambah Buku Baru", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String judul = judulField.getText().trim();
            String penulis = penulisField.getText().trim();
            String kategori = (String) kategoriCombo.getSelectedItem();
            
            if (judul.isEmpty() || penulis.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Judul dan penulis tidak boleh kosong!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Buku bukuBaru = new Buku(judul, penulis, kategori, true);
            perpustakaan.add(bukuBaru);
            
            JOptionPane.showMessageDialog(this, "Buku berhasil ditambahkan!", 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            
            updateBookTable();
            updateStatistik();
        }
    }
    
    /**
     * Dialog ubah buku
     */
    private void ubahBukuDialog() {
        int selectedRow = tableBuku.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih buku yang ingin diubah dari tabel!", 
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Buku bukuDipilih = perpustakaan.get(selectedRow);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField judulField = new JTextField(bukuDipilih.getJudul(), 20);
        JTextField penulisField = new JTextField(bukuDipilih.getPenulis(), 20);
        String[] kategoris = {"Fiksi", "Non-Fiksi", "Teknologi", "Sejarah"};
        JComboBox<String> kategoriCombo = new JComboBox<>(kategoris);
        kategoriCombo.setSelectedItem(bukuDipilih.getKategori());
        
        JCheckBox tersediaCheck = new JCheckBox("Tersedia", bukuDipilih.isTersedia());
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Judul Buku:"), gbc);
        gbc.gridx = 1;
        panel.add(judulField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Penulis:"), gbc);
        gbc.gridx = 1;
        panel.add(penulisField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Kategori:"), gbc);
        gbc.gridx = 1;
        panel.add(kategoriCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        panel.add(tersediaCheck, gbc);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Ubah Informasi Buku", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String judul = judulField.getText().trim();
            String penulis = penulisField.getText().trim();
            String kategori = (String) kategoriCombo.getSelectedItem();
            boolean tersedia = tersediaCheck.isSelected();
            
            if (judul.isEmpty() || penulis.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Judul dan penulis tidak boleh kosong!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Update status
            if (bukuDipilih.isTersedia() && !tersedia) {
                // Dari tersedia ke dipinjam
                bukuDipilih.setTanggalPinjam(LocalDate.now());
                bukuDipinjam.add(bukuDipilih);
            } else if (!bukuDipilih.isTersedia() && tersedia) {
                // Dari dipinjam ke tersedia
                bukuDipilih.setTanggalPinjam(null);
                bukuDipinjam.remove(bukuDipilih);
            }
            
            bukuDipilih.setJudul(judul);
            bukuDipilih.setPenulis(penulis);
            bukuDipilih.setKategori(kategori);
            bukuDipilih.setTersedia(tersedia);
            
            JOptionPane.showMessageDialog(this, "Informasi buku berhasil diubah!", 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            
            updateBookTable();
            updateStatistik();
        }
    }
    
    /**
     * Dialog hapus buku
     */
    private void hapusBukuDialog() {
        int selectedRow = tableBuku.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih buku yang ingin dihapus dari tabel!", 
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Buku bukuDipilih = perpustakaan.get(selectedRow);
        
        String message = "Apakah Anda yakin ingin menghapus buku ini?\n\n" +
                        "Judul: " + bukuDipilih.getJudul() + "\n" +
                        "Penulis: " + bukuDipilih.getPenulis() + "\n" +
                        "Status: " + (bukuDipilih.isTersedia() ? "Tersedia" : "Dipinjam");
        
        if (!bukuDipilih.isTersedia()) {
            message += "\n\n⚠️ PERINGATAN: Buku ini sedang dipinjam!\n" +
                      "Menghapus buku ini akan mempengaruhi data peminjaman.";
        }
        
        int choice = JOptionPane.showConfirmDialog(this, message, "Konfirmasi Hapus", 
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            String input = JOptionPane.showInputDialog(this, 
                    "Ketik 'HAPUS' untuk konfirmasi akhir:", "Konfirmasi Akhir", 
                    JOptionPane.WARNING_MESSAGE);
            
            if ("HAPUS".equals(input)) {
                perpustakaan.remove(bukuDipilih);
                if (bukuDipinjam.contains(bukuDipilih)) {
                    bukuDipinjam.remove(bukuDipilih);
                }
                
                JOptionPane.showMessageDialog(this, "Buku berhasil dihapus!", 
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
                updateBookTable();
                updateStatistik();
            } else {
                JOptionPane.showMessageDialog(this, "Penghapusan dibatalkan karena konfirmasi tidak sesuai.", 
                        "Dibatalkan", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
