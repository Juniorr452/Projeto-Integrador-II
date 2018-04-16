package com.mobile.pid.pid.home.turmas;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by junio on 07/03/2018.
 */

// Informação do usuário (Nome e Foto) nesse tipo: https://qph.fs.quoracdn.net/main-qimg-c4c82ebf15b46df728e39f2a0149b9ad
public class InfoUsuario implements Parcelable {
    private String nome;
    private String fotoUrl;

    public InfoUsuario() {}

    public InfoUsuario(String nome, String fotoUrl)
    {
        this.nome = nome;
        this.fotoUrl = fotoUrl;
    }

    public String getNome() {
        return nome;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nome);
        dest.writeString(this.fotoUrl);
    }

    protected InfoUsuario(Parcel in) {
        this.nome = in.readString();
        this.fotoUrl = in.readString();
    }

    public static final Parcelable.Creator<InfoUsuario> CREATOR = new Parcelable.Creator<InfoUsuario>() {
        @Override
        public InfoUsuario createFromParcel(Parcel source) {
            return new InfoUsuario(source);
        }

        @Override
        public InfoUsuario[] newArray(int size) {
            return new InfoUsuario[size];
        }
    };
}
