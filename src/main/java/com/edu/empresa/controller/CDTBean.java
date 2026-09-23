package com.edu.empresa.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import com.edu.empresa.model.*;
import com.edu.empresa.service.*;

@Named("cdtBean")
@ViewScoped
public class CDTBean implements Serializable {
    private CDT cdt;
    private List<CDT> listaHistorico;
    private CDTService cdtService;
    private boolean modoEdicion;
    private String idOriginal;
    private boolean mostrarResultados;

    public CDTBean() {
        this.cdtService = new CDTService();
        this.cdt = new CDT(new Cliente());
    }

    @PostConstruct
    public void init() {
        cargarHistorico();
    }

    public void cargarHistorico() {
        this.listaHistorico = cdtService.obtenerHistorico();
    }

    public void calcular() {
        try {
            validarFormatoCampos();
            if (modoEdicion) {
                cdtService.actualizarCDT(idOriginal, cdt.getCliente(), cdt.getInversion(), cdt.getPlazoDias(), cdt.getTasaInteresesAnual());
            } else {
                cdtService.guardarNuevoCDT(cdt.getCliente(), cdt.getInversion(), cdt.getPlazoDias(), cdt.getTasaInteresesAnual());
            }
            cdt.calcularCDT();
            mostrarResultados = true;
            cargarHistorico();
            mostrarMensaje(FacesMessage.SEVERITY_INFO, "Éxito", "CDT calculado y guardado.");
        } catch (Exception e) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
        }
    }

    public void perpararEdicion(CDT cdtSeleccionado) {
        this.cdt = cdtSeleccionado;
        this.idOriginal = cdtSeleccionado.getCliente().getDocId();
        this.modoEdicion = true;
    }

    public void eliminar(String docId) {
        cdtService.eliminarCDT(docId);
        cargarHistorico();
        mostrarMensaje(FacesMessage.SEVERITY_INFO, "Completado", "Registro eliminado del histórico.");
    }

    public void limpiar() {
        this.cdt = new CDT(new Cliente());
        this.modoEdicion = false;
        this.mostrarResultados = false;
        this.idOriginal = null;
    }

    public void validarFormatoCampos() {
        if (!cdt.getCliente().validar()) {
            throw new RuntimeException("Por favor, complete los datos requeridos del cliente.");
        }
    }

    public void mostrarMensaje(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

	public CDT getCdt() {
		return cdt;
	}

	public void setCdt(CDT cdt) {
		this.cdt = cdt;
	}

	public List<CDT> getListaHistorico() {
		return listaHistorico;
	}

	public void setListaHistorico(List<CDT> listaHistorico) {
		this.listaHistorico = listaHistorico;
	}

	public CDTService getCdtService() {
		return cdtService;
	}

	public void setCdtService(CDTService cdtService) {
		this.cdtService = cdtService;
	}

	public boolean isModoEdicion() {
		return modoEdicion;
	}

	public void setModoEdicion(boolean modoEdicion) {
		this.modoEdicion = modoEdicion;
	}

	public String getIdOriginal() {
		return idOriginal;
	}

	public void setIdOriginal(String idOriginal) {
		this.idOriginal = idOriginal;
	}

	public boolean isMostrarResultados() {
		return mostrarResultados;
	}

	public void setMostrarResultados(boolean mostrarResultados) {
		this.mostrarResultados = mostrarResultados;
	}

    
}