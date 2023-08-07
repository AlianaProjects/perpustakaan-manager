package perpustakaanManager.controller;

import perpustakaanManager.dbConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TambahAnggota {

    @FXML private AnchorPane FormTambahAnggota;
    @FXML private TextField id;
    @FXML private TextField nama;
    @FXML private TextField alamat;
    @FXML private TextField telp;
    @FXML private TextField email;

    private dbConnect connect = dbConnect.getInstance();
    //Method ini digunakan untuk menutup jendela TambahAnggota.
    @FXML void batal( ActionEvent event) {
        Stage stage = (Stage) FormTambahAnggota.getScene().getWindow();
        stage.close();
    }
    //Method ini digunakan untuk menyimpan data anggota baru ke dalam database.
    //Data anggota diambil dari TextField yang ada pada antarmuka pengguna.
    @FXML void simpanAnggota(ActionEvent event) {
        String idText = id.getText();
        String namaText = nama.getText();
        String alamatText = alamat.getText();
        String telpText = telp.getText();
        String emailText = email.getText();

        if (idText.isEmpty() && namaText.isEmpty() && alamatText.isEmpty() && telpText.isEmpty() && emailText.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Field tidak boleh kosong");
            alert.showAndWait();
            return;
        }

        checkCharacter();
        checkIDAnggota();

        String query = "INSERT INTO anggota VALUES("+
                "'"+ idText +"',"+
                "'"+ namaText +"',"+
                "'"+ alamatText +"',"+
                "'"+ telpText +"',"+
                "'"+ emailText +"'"+
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
    //Method ini digunakan untuk memeriksa apakah ID anggota yang dimasukkan sudah ada dalam database.
    //Method ini menjalankan kueri SQL untuk mencari ID anggota yang sama.
    //Jika ditemukan, akan ditampilkan pesan error.
    @ FXML void checkIDAnggota() {
        String query = "SELECT id FROM `anggota` WHERE id = '"+id.getText()+"'";

        ResultSet result = connect.executeQuery(query);

        try{
            while (result.next()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Id "+id.getText()+" sudah ada");
                alert.showAndWait();
                return;
            }

        }catch (SQLException ex){
            Logger.getLogger(TambahBuku.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    //Method ini digunakan untuk membersihkan isi dari semua TextField pada antarmuka pengguna,
    // sehingga formulir menjadi kosong.
    @ FXML void clearForm(){
        id.clear();
        nama.clear();
        alamat.clear();
        telp.clear();
        email.clear();
    }
    //Method ini digunakan untuk memeriksa apakah panjang ID anggota melebihi batas maksimum karakter yang diizinkan.
    // Jika melebihi, akan ditampilkan pesan error.
    @ FXML void checkCharacter(){
        if(id.getLength() >= 5){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("ID Anggota melebihi batas maksimal karakter");
            alert.showAndWait();
            return;
        }
    }
}
