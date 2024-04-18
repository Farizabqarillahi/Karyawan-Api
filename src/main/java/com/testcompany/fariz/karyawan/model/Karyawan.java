package com.testcompany.fariz.karyawan.model;

public class Karyawan {

    private String nama;
    private String jabatan;

    public Karyawan(String nama, String jabatan) {
        this.nama = nama;
        this.jabatan = jabatan;
    }

    public String getNama() {
        return nama;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }
}
