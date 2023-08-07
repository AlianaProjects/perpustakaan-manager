package perpustakaanManager.controller;
//Kelas Buku merupakan kelas model yang digunakan untuk merepresentasikan data buku dalam aplikasi perpustakaan.
public class Buku {

    private final String kodebuku;
    private final String judul;
    private final String penulis;
    private final String penerbit;
    private final String tahun_terbit;
    private final String tersedia;
    //Variabel
    public Buku(String kodebuku, String judul, String penulis, String penerbit, String tahun_terbit, String tersedia) {
        this.kodebuku = kodebuku;
        this.judul = judul;
        this.penulis = penulis;
        this.penerbit = penerbit;
        this.tahun_terbit = tahun_terbit;
        this.tersedia = tersedia;
    }

    public String getKodebuku() {
        return kodebuku;
    }

    public String getJudul() {
        return judul;
    }

    public String getPenulis() {
        return penulis;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public String getTahun_terbit() {
        return tahun_terbit;
    }

    public String getTersedia() {
        return tersedia;
    }
}
