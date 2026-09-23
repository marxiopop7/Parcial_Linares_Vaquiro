package com.edu.empresa.service;

import com.edu.empresa.*;
import com.edu.empresa.model.CDT;
import com.edu.empresa.model.Cliente;
import com.edu.empresa.util.JsonManagerCDT;

import java.util.List;

public class CDTService {
    public JsonManagerCDT jsonManager;
    private final String RUTA_ARCHIVO = "historico_cdts.json";

    public CDTService() {
        this.jsonManager = new JsonManagerCDT();
    }

    public List<CDT> obtenerHistorico() {
        List<CDT> historico = jsonManager.leerLista(RUTA_ARCHIVO, CDT.class);
        // RECALCULAR los valores ignorados en el JSON para que no salgan en 0
        if (historico != null) {
            for (CDT cdt : historico) {
                cdt.calcularCDT();
            }
        }
        return historico;
    }

    public CDT guardarNuevoCDT(Cliente cliente, double inversion, int plazoDias, double tasa) {
        List<CDT> historico = obtenerHistorico();
        
        CDT nuevoCdt = new CDT(cliente);
        nuevoCdt.setInversion(inversion);
        nuevoCdt.setPlazoDias(plazoDias);
        nuevoCdt.setTasaInteresesAnual(tasa);
        nuevoCdt.calcularCDT();
        
        historico.add(nuevoCdt);
        jsonManager.guardarLista(RUTA_ARCHIVO, historico);
        return nuevoCdt;
    }

    public CDT actualizarCDT(String docId, Cliente cliente, double inversion, int plazoDias, double tasa) {
        List<CDT> historico = obtenerHistorico();
        for (int i = 0; i < historico.size(); i++) {
            if (historico.get(i).getCliente().getDocId().equals(docId)) {
                // Crear una nueva instancia limpia para reemplazar la anterior
                CDT actualizado = new CDT(cliente);
                actualizado.setInversion(inversion);
                actualizado.setPlazoDias(plazoDias);
                actualizado.setTasaInteresesAnual(tasa);
                actualizado.calcularCDT();
                
                historico.set(i, actualizado);
                jsonManager.guardarLista(RUTA_ARCHIVO, historico);
                return actualizado;
            }
        }
        return null;
    }
    public boolean eliminarCDT(String docId) {
        List<CDT> historico = obtenerHistorico();
        boolean eliminado = false;
        for (int i = 0; i < historico.size(); i++) {
            if (historico.get(i).getCliente().getDocId().equals(docId)) {
                historico.remove(i);
                eliminado = true;
                break; // Rompe el ciclo para eliminar solo UNO
            }
        }
        if (eliminado) {
            jsonManager.guardarLista(RUTA_ARCHIVO, historico);
        }
        return eliminado;
    }

    public void verificarIdentidadUnica(List<Cliente> clientes, Cliente cliente) {
        for (Cliente c : clientes) {
            if (c.getDocId().equals(cliente.getDocId())) {
                throw new RuntimeException("El cliente con este documento ya existe.");
            }
        }
    }
}