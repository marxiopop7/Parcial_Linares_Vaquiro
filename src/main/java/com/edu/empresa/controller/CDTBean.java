package com.edu.empresa.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;
import com.edu.empresa.model.*;
import com.edu.empresa.service.*;

@Named("cdtBean")
@ViewScoped
public class CDTBean implements Serializable {
    
    private static final long serialVersionUID = 1L;

    // Patrones de validación
    private static final Pattern PATRON_NUMEROS = Pattern.compile("^\\d+$");
    private static final Pattern PATRON_LETRAS = Pattern.compile("^[A-Za-zÁÉÍÓÚÜáéíóúüÑñ\\s]+$");
    private static final Pattern PATRON_CORREO = Pattern.compile("^[\\w.+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private CDT cdt;
    private List<CDT> listaHistorico;
    private CDTService cdtService;
    private boolean modoEdicion;
    private String idOriginal;
    private boolean mostrarResultados;

    @PostConstruct
    public void init() {
        this.cdtService = new CDTService();
        limpiar();
        cargarHistorico();
    }

    public void cargarHistorico() {
        try {
            this.listaHistorico = cdtService.obtenerHistorico();
        } catch (Exception e) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo cargar la información histórica.");
        }
    }

    public void calcular() {
        try {
            // 1. Ejecutar validaciones estrictas
            validarFormatoCampos();

            // 2. Validar que la identificación sea única si es un registro nuevo
            if (!modoEdicion) {
                for (CDT hist : listaHistorico) {
                    if (hist.getCliente().getDocId().equals(cdt.getCliente().getDocId())) {
                        throw new IllegalArgumentException("El número de identificación ya está registrado.");
                    }
                }
                cdtService.guardarNuevoCDT(cdt.getCliente(), cdt.getInversion(), cdt.getPlazoDias(), cdt.getTasaInteresesAnual());
                mostrarMensaje(FacesMessage.SEVERITY_INFO, "Éxito", "CDT registrado correctamente.");
            } else {
                cdtService.actualizarCDT(idOriginal, cdt.getCliente(), cdt.getInversion(), cdt.getPlazoDias(), cdt.getTasaInteresesAnual());
                mostrarMensaje(FacesMessage.SEVERITY_INFO, "Éxito", "CDT actualizado correctamente.");
            }
            
            cargarHistorico();
            limpiar();
            
        } catch (IllegalArgumentException e) {
            // Captura los errores de validación y los muestra en pantalla
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error de Validación", e.getMessage());
        } catch (Exception e) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error del Sistema", "Ocurrió un error inesperado.");
        }
    }

    public void perpararEdicion(CDT cdtSeleccionado) {
        Cliente cli = cdtSeleccionado.getCliente();
        
        // Clonar el cliente para evitar modificar la lista directamente antes de guardar
        Cliente cliEdicion = new Cliente(
            cli.getDocId(),
            cli.getNombre(),
            cli.getApellido(),
            cli.getCorreo(),
            cli.getTelefono()
        );
        
        this.cdt = new CDT(cliEdicion);
        this.cdt.setInversion(cdtSeleccionado.getInversion());
        this.cdt.setTasaInteresesAnual(cdtSeleccionado.getTasaInteresesAnual());
        this.cdt.setPlazoDias(cdtSeleccionado.getPlazoDias());

        this.idOriginal = cli.getDocId();
        this.modoEdicion = true;
    }

    public void eliminar(String docId) {
        try {
            cdtService.eliminarCDT(docId);
            mostrarMensaje(FacesMessage.SEVERITY_INFO, "Completado", "Registro eliminado del histórico.");
            cargarHistorico();
            
            if (docId.equals(idOriginal)) {
                limpiar();
            }
        } catch (Exception e) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el registro.");
        }
    }

    public void limpiar() {
        this.cdt = new CDT(new Cliente());
        this.cdt.setPlazoDias(0);
        this.cdt.setInversion(0.0);
        this.cdt.setTasaInteresesAnual(0.0);
        this.modoEdicion = false;
        this.mostrarResultados = false;
        this.idOriginal = null;
    }

    private void validarFormatoCampos() {
        Cliente cli = cdt.getCliente();
        
        // Validaciones con Regex
        if (cli.getDocId() == null || !PATRON_NUMEROS.matcher(cli.getDocId().trim()).matches()) {
            throw new IllegalArgumentException("La identificación debe contener solo números.");
        }
        if (cli.getNombre() == null || !PATRON_LETRAS.matcher(cli.getNombre().trim()).matches()) {
            throw new IllegalArgumentException("Los nombres solo deben contener letras.");
        }
        if (cli.getApellido() == null || !PATRON_LETRAS.matcher(cli.getApellido().trim()).matches()) {
            throw new IllegalArgumentException("Los apellidos solo deben contener letras.");
        }
        if (cli.getCorreo() == null || !PATRON_CORREO.matcher(cli.getCorreo().trim()).matches()) {
            throw new IllegalArgumentException("El correo no tiene un formato válido.");
        }
        if (cli.getTelefono() == null || !PATRON_NUMEROS.matcher(cli.getTelefono().trim()).matches()) {
            throw new IllegalArgumentException("El teléfono debe contener solo números.");
        }
        
        // Validaciones de negocio exigidas
        if (cdt.getInversion() < 2000000) {
            throw new IllegalArgumentException("El valor a invertir debe ser mínimo $2.000.000.");
        }
        if (cdt.getPlazoDias() < 30) {
            throw new IllegalArgumentException("El plazo mínimo del CDT es de 30 días.");
        }
        if (cdt.getTasaInteresesAnual() <= 0) {
            throw new IllegalArgumentException("La tasa de interés debe ser mayor a 0.");
        }
    }

    private void mostrarMensaje(FacesMessage.Severity severidad, String titulo, String detalle) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severidad, titulo, detalle));
    }

    // Getters y Setters
    public CDT getCdt() { return cdt; }
    public void setCdt(CDT cdt) { this.cdt = cdt; }

    public List<CDT> getListaHistorico() { return listaHistorico; }
    public void setListaHistorico(List<CDT> listaHistorico) { this.listaHistorico = listaHistorico; }

    public boolean isModoEdicion() { return modoEdicion; }
    public void setModoEdicion(boolean modoEdicion) { this.modoEdicion = modoEdicion; }
}