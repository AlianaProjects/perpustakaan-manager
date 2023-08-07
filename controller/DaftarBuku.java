package perpustakaanManager.controller;

import perpustakaanManager.dbConnect;
import com.mysql.cj.xdevapi.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaftarBuku implements Initializable {
    @ FXML private TableView<Buku> tableView;
    @ FXML private TableColumn<Buku, String> col_kodeBuku;
    @ FXML private TableColumn<Buku, String> col_judul;
    @ FXML private TableColumn<Buku, String> col_penulis;
    @ FXML private TableColumn<Buku, String> col_penerbit;
    @ FXML private TableColumn<Buku, String> col_tahun_terbit;
    @ FXML private TableColumn<Buku, String> col_tersedia;

    private dbConnect connect = dbConnect.getInstance();
    private ObservableList<Buku> daftarBuku = FXCollections.observableArrayList();
    //Method ini merupakan implementasi dari interface Initializable dan akan dipanggil saat jendela DaftarBuku diinisialisasi.
    //Method ini menginisialisasi kolom tabel dan memuat data buku dari database dengan memanggil method initCol() dan loadData().
    @ Override
    public void initialize(URL location, ResourceBundle resources){
        initCol();
        loadData();
    }
    //Method ini digunakan untuk menginisialisasi kolom-kolom pada tabel.
    //Setiap kolom dihubungkan dengan properti yang sesuai pada kelas Buku menggunakan PropertyValueFactory.
    private void initCol() {
        col_kodeBuku.setCellValueFactory(new PropertyValueFactory<Buku, String>("kodebuku"));
        col_judul.setCellValueFactory(new PropertyValueFactory<Buku, String>("judul"));
        col_penulis.setCellValueFactory(new PropertyValueFactory<Buku, String>("penulis"));
        col_penerbit.setCellValueFactory(new PropertyValueFactory<Buku, String>("penerbit"));
        col_tahun_terbit.setCellValueFactory(new PropertyValueFactory<Buku, String>("tahun_terbit"));
        col_tersedia.setCellValueFactory(new PropertyValueFactory<Buku, String>("tersedia"));
    }
    //Method loadData digunakan untuk memuat data buku dari database ke dalam ObservableList daftarBuku.
    //Data buku diambil dari hasil eksekusi query SQL pada tabel buku.
    //Setiap baris data buku ditambahkan sebagai objek Buku ke dalam daftarBuku.
    private void loadData() {
        String query = "SELECT * FROM `buku`";
        String tersedia_y;

        ResultSet result = connect.executeQuery(query);

        try{
            while (result.next()){
                String kodebuku_x = result.getString( "kodeBuku");
                String judul_x = result.getString( "judul");
                String penulis_x = result.getString( "penulis");
                String penerbit_x = result.getString( "penerbit");
                String tahun_terbit_x = result.getString( "tahun_terbit");
                int tersedia_x = result.getInt( "tersedia");

                if (tersedia_x == 1){
                    tersedia_y = "Sedia";
                } else {
                    tersedia_y = "Kosong";
                }
                daftarBuku.add(new Buku(kodebuku_x, judul_x, penulis_x, penerbit_x, tahun_terbit_x, tersedia_y));
            }

        }catch (SQLException ex){
            Logger.getLogger(DaftarBuku.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.getItems().setAll(daftarBuku);
    }

}
