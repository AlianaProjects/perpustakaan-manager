package perpustakaanManager.controller;

import perpustakaanManager.dbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaftarAnggota implements Initializable {
    @ FXML private TableView<Anggota> tableView;
    @ FXML private TableColumn<Anggota, String> col_id;
    @ FXML private TableColumn<Anggota, String> col_nama;
    @ FXML private TableColumn<Anggota, String> col_alamat;
    @ FXML private TableColumn<Anggota, String> col_telp;
    @ FXML private TableColumn<Anggota, String> col_email;

    private dbConnect connect = dbConnect.getInstance();
    private ObservableList<Anggota> daftarAnggota = FXCollections.observableArrayList();
    //Method ini merupakan implementasi dari interface Initializable dan akan dipanggil saat jendela DaftarAnggota diinisialisasi.
    // Method ini menginisialisasi kolom tabel dan memuat data anggota dari database dengan memanggil method initCol() dan loadData().
    @ Override
    public void initialize(URL location, ResourceBundle resources){
        initCol();
        loadData();
    }
    //Method ini digunakan untuk menginisialisasi kolom-kolom pada tabel tableView.
    //Setiap kolom dihubungkan dengan properti yang sesuai pada kelas Anggota menggunakan PropertyValueFactory.
    private void initCol() {
        col_id.setCellValueFactory(new PropertyValueFactory<Anggota, String>("id"));
        col_nama.setCellValueFactory(new PropertyValueFactory<Anggota, String>("nama"));
        col_alamat.setCellValueFactory(new PropertyValueFactory<Anggota, String>("alamat"));
        col_telp.setCellValueFactory(new PropertyValueFactory<Anggota, String>("telp"));
        col_email.setCellValueFactory(new PropertyValueFactory<Anggota, String>("email"));
    }
    //Method ini digunakan untuk memuat data anggota dari database ke dalam ObservableList daftarAnggota.
    //Data anggota diambil dari hasil eksekusi query SQL pada tabel anggota.
    private void loadData() {
        String query = "SELECT * FROM `anggota`";

        ResultSet result = connect.executeQuery(query);

        try{
            while (result.next()){
                String id_x = result.getString( "id");
                String nama_x = result.getString( "nama");
                String alamat_x = result.getString( "alamat");
                String telp_x = result.getString( "telp");
                String email_x = result.getString( "email");

                daftarAnggota.add(new Anggota(id_x, nama_x, alamat_x, telp_x, email_x));

            }

        }catch (SQLException ex){
            Logger.getLogger(DaftarAnggota.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.getItems().setAll(daftarAnggota);
    }

}
