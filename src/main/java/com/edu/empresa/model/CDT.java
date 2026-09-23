package com.edu.empresa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CDT {
    private Cliente cliente;
    private double inversion;
    private int plazoDias;
    private double tasaInteresesAnual;

    @JsonIgnore
    private double gananciaBruta;
    @JsonIgnore
    private double retencionFuente;
    @JsonIgnore
    private double gananciaNeta;
    @JsonIgnore
    private double valorTotal;

    public CDT() {}

    public CDT(Cliente cliente) {
        this.cliente = cliente;
    }

    public void calcularCDT() {
        this.gananciaBruta = calcularGananciaBruta();
        this.retencionFuente = calcularRetencionFuente();
        this.gananciaNeta = calcularGananciaNeta();
        this.valorTotal = calcularValorTotal();
    }

    public double calcularGananciaBruta() {
    	return (inversion * (tasaInteresesAnual / 100.0) * plazoDias) / 360.0;
    }

    public double calcularRetencionFuente() {
        // Asumiendo un estándar, puedes ajustarlo a la lógica de si declara renta o no
        return this.gananciaBruta * 0.04; 
    }

    public double calcularGananciaNeta() {
        return this.gananciaBruta - this.retencionFuente;
    }

    public double calcularValorTotal() {
        return this.inversion + this.gananciaNeta;
    }

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public double getInversion() {
		return inversion;
	}

	public void setInversion(double inversion) {
		this.inversion = inversion;
	}

	public int getPlazoDias() {
		return plazoDias;
	}

	public void setPlazoDias(int plazoDias) {
		this.plazoDias = plazoDias;
	}

	public double getTasaInteresesAnual() {
		return tasaInteresesAnual;
	}

	public void setTasaInteresesAnual(double tasaInteresesAnual) {
		this.tasaInteresesAnual = tasaInteresesAnual;
	}

	public double getGananciaBruta() {
		return gananciaBruta;
	}

	public void setGananciaBruta(double gananciaBruta) {
		this.gananciaBruta = gananciaBruta;
	}

	public double getRetencionFuente() {
		return retencionFuente;
	}

	public void setRetencionFuente(double retencionFuente) {
		this.retencionFuente = retencionFuente;
	}

	public double getGananciaNeta() {
		return gananciaNeta;
	}

	public void setGananciaNeta(double gananciaNeta) {
		this.gananciaNeta = gananciaNeta;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

    
}