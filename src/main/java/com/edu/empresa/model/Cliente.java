package com.edu.empresa.model;

public class Cliente extends Personas {
    
    public Cliente() {
        super();
    }

    public Cliente(String docId, String nombre, String apellido, String correo, String telefono) {
        super(docId, nombre, apellido, correo, telefono);
    }

    public boolean validar() {
        return this.docId != null && !this.docId.isEmpty() && this.nombre != null && !this.nombre.isEmpty();
    }
}