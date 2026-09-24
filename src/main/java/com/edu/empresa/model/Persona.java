package com.edu.empresa.model;

public class Persona {
    protected String docId;
    protected String nombre;
    protected String apellido;
    protected String correo;
    protected String telefono;

    public Persona() {}

    public Persona(String docId, String nombre, String apellido, String correo, String telefono) {
        this.docId = docId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
    }

    public String obtenerNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}



    
    
}