package perpustakaanManager.controller;
//Kelas Anggota merupakan kelas model yang digunakan untuk merepresentasikan data anggota dalam aplikasi perpustakaan.
public class Anggota {

    private final String id;
    private final String nama;
    private final String alamat;
    private final String telp;
    private final String email;
    //Variabel
    public Anggota(String id, String nama, String alamat, String telp, String email) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.telp = telp;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getTelp() {
        return telp;
    }

    public String getEmail() {
        return email;
    }
}
