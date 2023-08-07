package perpustakaanManager;

import perpustakaanManager.controller.TambahBuku;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    @FXML
    private TextField masukanKodeBuku;
    @FXML
    private Text namaBuku;
    @FXML
    private Text penulisBuku;
    @FXML
    private Text penerbitBuku;
    @FXML
    private Text statusBuku;
    @FXML
    private TextField masukanKodeAnggota;
    @FXML
    private Text namaAnggota;
    @FXML
    private Text telpAnggota;
    @FXML
    private TextField masukanKodeBukuPengembalian;
    @FXML
    private ListView<String> listViewPeminjaman;


    private dbConnect connect = dbConnect.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        primaryStage.setTitle("Perpustakaan Manager");
        primaryStage.setScene(new Scene(root, 700.0, 500.0));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    void tampilWindow(String loc, String title) {
        try {
            Parent layout = FXMLLoader.load(getClass().getResource(loc));

            Scene layout_scene = new Scene(layout);
            Stage appStage = new Stage();
            appStage.setTitle(title);
            appStage.setResizable(false);
            appStage.setScene(layout_scene);
            appStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tombolMenuTambahBuku() {
        tampilWindow("layout/tambahBuku.fxml", "Form Tambah Buku");
    }

    public void tombolMenuDaftarBuku() {
        tampilWindow("layout/daftarBuku.fxml", "Form Daftar Buku");
    }

    public void tombolMenuTambahAnggota() {
        tampilWindow("layout/tambahAnggota.fxml", "Form Tambah Anggota");
    }

    public void tombolMenuDaftarAnggota() {
        tampilWindow("layout/daftarAnggota.fxml", "Form Daftar Anggota");
    }

    public void ambilDataBuku(ActionEvent actionEvent) {
        String query = "SELECT * FROM `buku` WHERE kodebuku = '" + masukanKodeBuku.getText() + "'";
        ResultSet result = connect.executeQuery(query);
        String status;
        try {
            while (result.next()) {
                String judul_buku = result.getString("judul");
                String penulis_buku = result.getString("penulis");
                String penerbit_buku = result.getString("penerbit");

                int tersedia_x = result.getInt("tersedia");

                if (tersedia_x == 1) {
                    status = "Buku Sedia";
                } else {
                    status = "Buku Kosong";
                }

                namaBuku.setText(judul_buku);
                penulisBuku.setText(penulis_buku);
                penerbitBuku.setText(penerbit_buku);
                statusBuku.setText(status);
            }

        } catch (SQLException ex) {
            Logger.getLogger(TambahBuku.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ambilDataAnggota(ActionEvent actionEvent) {
        String query = "SELECT * FROM `anggota` WHERE id = '" + masukanKodeAnggota.getText() + "'";

        ResultSet result = connect.executeQuery(query);

        try {
            while (result.next()) {
                String nama_anggota = result.getString("nama");
                String telp_anggota = result.getString("telp");

                namaAnggota.setText(nama_anggota);
                telpAnggota.setText(telp_anggota);
            }

        } catch (SQLException ex) {
            Logger.getLogger(TambahBuku.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void prosesPinjam(ActionEvent actionEvent) {
        String idBuku = masukanKodeBuku.getText();
        String idAnggota = masukanKodeAnggota.getText();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Konfirmasi Peminjaman Buku");
        alert.setContentText("Apakah anda yakin peminjaman buku dengan judul " + namaBuku.getText() +
                " oleh Anggota dengan nama " + namaAnggota.getText());

        ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == buttonTypeOK) {
            String query1 = "INSERT INTO peminjaman(kodebuku, id_anggota) VALUES(" +
                    "'" + idBuku + "'," +
                    "'" + idAnggota + "'" +
                    ")";

            String query2 = "UPDATE buku SET tersedia = 0 WHERE kodebuku = '" + idBuku + "'";

            if (connect.executeAction(query1) && connect.executeAction(query2)) {
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setHeaderText(null);
                alert2.setContentText("Proses Peminjaman Berhasil");
                alert2.showAndWait();

                namaBuku.setText("nama Buku");
                penulisBuku.setText("Penulis");
                penerbitBuku.setText("Penerbit");
                statusBuku.setText("Status Buku");
                namaAnggota.setText("Nama Anggota");
                telpAnggota.setText("Contact");
                masukanKodeBuku.clear();
                masukanKodeAnggota.clear();

            } else {
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setHeaderText(null);
                alert2.setContentText("Data tidak berhasil dipinjam");
                alert2.showAndWait();
            }
        } else {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setHeaderText(null);
            alert2.setContentText("Buku tidak jadi dipinjam");
            alert2.showAndWait();
        }
    }
    public void loadDataPeminjaman(ActionEvent actionEvent) {
        ObservableList<String> listPeminjaman = FXCollections.observableArrayList();

        String query = "SELECT * FROM peminjaman WHERE kodebuku = '"+ masukanKodeBukuPengembalian.getText()+"'";

        ResultSet result = connect.executeQuery(query);

        try {
            while (result.next()) {
                String idBuku = masukanKodeBukuPengembalian.getText();
                String idAnggota = result.getString("id_anggota");
                Timestamp tglPinjam = result.getTimestamp("tglPinjam");
                int pinjamUlang = result.getInt("pinjamUlang");

                listPeminjaman.add("Tanggal Peminjaman : "+ tglPinjam.toGMTString());
                listPeminjaman.add("Peminjaman Ulang : "+ pinjamUlang);

                query = "SELECT * FROM buku WHERE kodebuku ='"+idBuku+"'";
                ResultSet resultBuku = connect.executeQuery(query);

                listPeminjaman.add("Informasi Buku : ");
                while (resultBuku.next()){
                    listPeminjaman.add("    Nama Buku    : "+resultBuku.getString("judul"));
                    listPeminjaman.add("    Penulis      : "+resultBuku.getString("penulis"));
                    listPeminjaman.add("    Penerbit     : "+resultBuku.getString("penerbit"));
                    listPeminjaman.add("    Tahun Terbit : "+resultBuku.getString("tahun_terbit"));
                }

                query = "SELECT * FROM anggota WHERE id ='"+idAnggota+"'";
                ResultSet resultAnggota = connect.executeQuery(query);

                listPeminjaman.add("Informasi Anggota : ");
                while (resultAnggota.next()){
                    listPeminjaman.add("    Nama Anggota : "+resultAnggota.getString("nama"));
                    listPeminjaman.add("    Alamat       : "+resultAnggota.getString("alamat"));
                    listPeminjaman.add("    Telepon      : "+resultAnggota.getString("telp"));
                    listPeminjaman.add("    Email        : "+resultAnggota.getString("email"));
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(TambahBuku.class.getName()).log(Level.SEVERE, null, ex);
        }
        listViewPeminjaman.getItems().setAll(listPeminjaman);

    }

    public void prosesPengembalian(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Konfirmasi Pengembalian Buku");
        alert.setContentText("Apakah anda yakin dengan pengembalian buku ini?");

        ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == buttonTypeOK) {
            String query1 = "Delete From peminjaman WHERE kodebuku ='"+masukanKodeBukuPengembalian.getText()+"'";

            String query2 = "UPDATE buku SET tersedia = 1 WHERE kodebuku = '" +masukanKodeBukuPengembalian.getText()+ "'";

            if (connect.executeAction(query1) && connect.executeAction(query2)) {
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setHeaderText(null);
                alert2.setContentText("Buku telah berhasil dikembalikan");
                alert2.showAndWait();

                listViewPeminjaman.getItems().clear();
                masukanKodeBukuPengembalian.clear();
            } else {
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setHeaderText(null);
                alert2.setContentText("Proses Pengembalian tidak berhasil");
                alert2.showAndWait();
            }
        } else {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setHeaderText(null);
            alert2.setContentText("Buku tidak jadi dikembalikan");
            alert2.showAndWait();
        }

    }

    public void prosesPerpanjangan(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Konfirmasi Perpanjangan Peminjaman Buku");
        alert.setContentText("Apakah anda yakin dengan perpanjangan peminjaman ulang buku ini?");

        ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == buttonTypeOK) {
            String query = "UPDATE peminjaman SET tglPinjam = CURRENT_TIMESTAMP, pinjamUlang = pinjamUlang+1 WHERE kodebuku ='"+masukanKodeBukuPengembalian.getText()+"'";

            if (connect.executeAction(query)) {
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setHeaderText(null);
                alert2.setContentText("Buku telah berhasil dipinjam ulang");
                alert2.showAndWait();

                loadDataPeminjaman(null);
            } else {
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setHeaderText(null);
                alert2.setContentText("Proses perpanjangan tidak berhasil");
                alert2.showAndWait();
            }
        } else {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setHeaderText(null);
            alert2.setContentText("Buku tidak jadi dipinjam ulang");
            alert2.showAndWait();
        }

    }

}
