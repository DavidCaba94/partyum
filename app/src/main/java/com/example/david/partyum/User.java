package com.example.david.partyum;

import android.media.Image;

import java.sql.Blob;

public class User {
    private String usuario;
    private String password;
    private String nombre;
    private String apellidos;
    private String email;
    private String foto;
    private int conectado;
    private String referido;
    private int premium;

    public User(String usuario, String password, String nombre, String apellidos, String email, String foto, int conectado, String referido, int premium) {
        this.usuario = usuario;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.foto = foto;
        this.conectado = conectado;
        this.referido = referido;
        this.premium = premium;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getConectado() {
        return conectado;
    }

    public void setConectado(int conectado) {
        this.conectado = conectado;
    }

    public String getReferido() {
        return referido;
    }

    public void setReferido(String referido) {
        this.referido = referido;
    }

    public int getPremium() {
        return premium;
    }

    public void setPremium(int premium) {
        this.premium = premium;
    }
}
