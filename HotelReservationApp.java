import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HotelReservationApp extends JFrame {
    // Representasi kamar hotel
    class Kamar {
        int nomor;
        String tipe;
        double hargaPerMalam;
        boolean tersedia;

        public Kamar(int nomor, String tipe, double hargaPerMalam, boolean tersedia) {
            this.nomor = nomor;
            this.tipe = tipe;
            this.hargaPerMalam = hargaPerMalam;
            this.tersedia = tersedia;
        }

        public String toString() {
            return "Kamar " + nomor + " (" + tipe + ") - Rp " + hargaPerMalam + (tersedia ? " [Tersedia]" : " [Tidak Tersedia]");
        }
    }

    Kamar kamar1 = new Kamar(101, "Standar", 150000, true);
    Kamar kamar2 = new Kamar(102, "Superior", 200000, true);
    Kamar kamar3 = new Kamar(201, "Deluxe", 300000, true);
    Kamar kamar4 = new Kamar(301, "Suite", 450000, true);

    Kamar[] daftarKamar = {kamar1, kamar2, kamar3, kamar4};
    Kamar[] kamarDipesan = new Kamar[3];
    int[] lamaInap = new int[3];
    int jumlahDipesan = 0;

    JTextArea outputArea;

    public HotelReservationApp() {
        setTitle("Reservasi Hotel (1 File Version)");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        outputArea = new JTextArea(20, 40);
        outputArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(outputArea);

        JButton pesanBtn = new JButton("Pesan Kamar");
        JButton cetakBtn = new JButton("Cetak Struk");

        pesanBtn.addActionListener(e -> prosesReservasi());
        cetakBtn.addActionListener(e -> cetakStruk());

        JPanel btnPanel = new JPanel();
        btnPanel.add(pesanBtn);
        btnPanel.add(cetakBtn);

        add(scroll, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        tampilkanKamar();
        setVisible(true);
    }

    void tampilkanKamar() {
        outputArea.setText("=== Daftar Kamar ===\n");
        if (kamar1.tersedia) outputArea.append(kamar1.toString() + "\n");
        if (kamar2.tersedia) outputArea.append(kamar2.toString() + "\n");
        if (kamar3.tersedia) outputArea.append(kamar3.toString() + "\n");
        if (kamar4.tersedia) outputArea.append(kamar4.toString() + "\n");
    }

    void prosesReservasi() {
        if (jumlahDipesan == 3) {
            JOptionPane.showMessageDialog(this, "Maksimal 3 kamar per reservasi.");
            return;
        }

        String inputNomor = JOptionPane.showInputDialog(this, "Masukkan Nomor Kamar:");
        if (inputNomor == null) return;

        String inputLama = JOptionPane.showInputDialog(this, "Lama menginap (malam):");
        if (inputLama == null) return;

        try {
            int nomor = Integer.parseInt(inputNomor);
            int lama = Integer.parseInt(inputLama);
            Kamar dipilih = null;

            if (kamar1.nomor == nomor && kamar1.tersedia) dipilih = kamar1;
            else if (kamar2.nomor == nomor && kamar2.tersedia) dipilih = kamar2;
            else if (kamar3.nomor == nomor && kamar3.tersedia) dipilih = kamar3;
            else if (kamar4.nomor == nomor && kamar4.tersedia) dipilih = kamar4;

            if (dipilih != null) {
                kamarDipesan[jumlahDipesan] = dipilih;
                lamaInap[jumlahDipesan] = lama;
                jumlahDipesan++;
                dipilih.tersedia = false;
                tampilkanKamar();
                JOptionPane.showMessageDialog(this, "Kamar berhasil dipesan.");
            } else {
                JOptionPane.showMessageDialog(this, "Kamar tidak tersedia.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input tidak valid.");
        }
    }

    void cetakStruk() {
        outputArea.setText("=== STRUK RESERVASI ===\n");
        double total = 0;
        double pajak, layanan, diskon = 0;

        if (jumlahDipesan >= 1)
            total += tampilkanDetail(kamarDipesan[0], lamaInap[0]);
        if (jumlahDipesan >= 2)
            total += tampilkanDetail(kamarDipesan[1], lamaInap[1]);
        if (jumlahDipesan == 3)
            total += tampilkanDetail(kamarDipesan[2], lamaInap[2]);

        outputArea.append("\nSubtotal: Rp " + total + "\n");

        pajak = total * 0.1;
        layanan = jumlahDipesan * 50000;

        if (total > 500000) {
            diskon = total * 0.15;
        }

        double totalSetelahDiskon = total - diskon + pajak + layanan;

        outputArea.append("Pajak (10%): Rp " + pajak + "\n");
        outputArea.append("Biaya Layanan: Rp " + layanan + "\n");

        if (diskon > 0)
            outputArea.append("Diskon 15%: -Rp " + diskon + "\n");

        if (total > 300000)
            outputArea.append("Penawaran: Gratis Sarapan untuk semua tamu!\n");

        outputArea.append("Total Bayar: Rp " + totalSetelahDiskon + "\n");
    }

    double tampilkanDetail(Kamar k, int lama) {
        double subtotal = k.hargaPerMalam * lama;
        outputArea.append("- Kamar No: " + k.nomor + ", Tipe: " + k.tipe + ", Lama: " + lama + " malam, Harga/Malam: " + k.hargaPerMalam + ", Total: " + subtotal + "\n");
        return subtotal;
    }

    public static void main(String[] args) {
        new HotelReservationApp();
    }
}