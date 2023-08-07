package perpustakaanManager.controller;

import perpustakaanManager.dbConnect;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TambahBuku {


    @ FXML private AnchorPane FormTambahBuku;
    @ FXML private TextField txtJudulBuku;
    @ FXML private TextField txtKodeBuku;
    @ FXML private TextField txtPenerbit;
    @ FXML private TextField txtPenulis;
    @ FXML private TextField txtTahunTerbit;

    private dbConnect connect = dbConnect.getInstance();
    //Method ini digunakan untuk menutup jendela TambahBuku.
    @ FXML void batal() {
        Stage stage = (Stage) FormTambahBuku.getScene().getWindow();
        stage.close();
    }
    //Method ini digunakan untuk menyimpan data buku baru ke dalam database.
    //Data buku diambil dari TextField yang ada pada antarmuka pengguna.
    @ FXML void simpanBuku() {
        String kodeBuku = txtKodeBuku.getText();
        String judulBuku = txtJudulBuku.getText();
        String penulis = txtPenulis.getText();
        String penerbit = txtPenerbit.getText();
        String tahun_terbit = txtTahunTerbit.getText();
        // Menampilkan pesan error jika data yang dimasukan ada yang kosong
        if (kodeBuku.isEmpty() && judulBuku.isEmpty() && penulis.isEmpty() && penerbit.isEmpty() && tahun_terbit.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Field tidak boleh kosong");
            alert.showAndWait();
            return;
        }

        checkCharacter();
        checkKodeBuku();
        String query = "INSERT INTO buku VALUES("+
                "'"+ kodeBuku +"',"+
                "'"+ judulBuku +"',"+
                "'"+ penulis +"',"+
                "'"+ penerbit +"',"+
                "'"+ tahun_terbit +"',"+
                "'"+ 1 +"'"+
                ")";

        if (connect.executeAction(query)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Data berhasil tersimpan");
            alert.showAndWait();
            clearForm();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Data tidak berhasil tersimpan");
            alert.showAndWait();
        }
    }
    //Method ini digunakan untuk memeriksa apakah kode buku yang dimasukkan sudah ada dalam database.
    // Method ini menjalankan kueri SQL untuk mencari kode buku yang sama.
    // Jika ditemukan, akan ditampilkan pesan error.
    @ FXML void checkKodeBuku() {
        String query = "SELECT kodebuku FROM `buku` WHERE kodebuku = '"+txtKodeBuku.getText()+"'";

        ResultSet result = connect.executeQuery(query);

        try{
            while (result.next()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Kodebuku "+txtKodeBuku.getText()+" sudah ada");
                alert.showAndWait();
                return;
            }

        }catch (SQLException ex){
            Logger.getLogger(TambahBuku.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    //Method ini digunakan untuk membersihkan isi dari semua TextField pada antarmuka pengguna,
    //sehingga formulir menjadi kosong.
    @ FXML void clearForm(){
        txtKodeBuku.clear();
        txtJudulBuku.clear();
        txtPenerbit.clear();
        txtPenulis.clear();
        txtTahunTerbit.clear();
    }
    //Method ini digunakan untuk memeriksa apakah panjang kode buku melebihi batas maksimum karakter yang diizinkan.
    //Jika melebihi, akan ditampilkan pesan error.
    @ FXML void checkCharacter(){
        if(txtKodeBuku.getLength() >= 5){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Kodebuku melebihi batas maksimal karakter");
            alert.showAndWait();
            return;
        }
    }
}
