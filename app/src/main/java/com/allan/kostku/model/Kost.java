package com.allan.kostku.model;

import java.io.Serializable;

public class Kost implements Serializable {
    private String nama;
    private String umur;
    private String fasilitas;

    public Kost() {
    }

    public Kost(String nama, String umur, String fasilitas) {
        this.nama = nama;
        this.umur = umur;
        this.fasilitas = fasilitas;
    }

    public String getNama() {
        return nama;
    }

    public String getUmur() {
        return umur;
    }

    public String getFasilitas() {
        return fasilitas;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setUmur(String umur) {
        this.umur = umur;
    }

    public void setFasilitas(String fasilitas) {
        this.fasilitas = fasilitas;
    }
}
