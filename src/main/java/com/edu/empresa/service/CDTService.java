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
        return jsonManager.leerLista(RUTA_ARCHIVO, CDT.class);
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
        for (CDT cdt : historico) {
            if (cdt.getCliente().getDocId().equals(docId)) {
                cdt.setCliente(cliente);
                cdt.setInversion(inversion);
                cdt.setPlazoDias(plazoDias);
                cdt.setTasaInteresesAnual(tasa);
                cdt.calcularCDT();
                jsonManager.guardarLista(RUTA_ARCHIVO, historico);
                return cdt;
            }
        }
        return null;
    }

    public boolean eliminarCDT(String docId) {
        List<CDT> historico = obtenerHistorico();
        boolean eliminado = historico.removeIf(cdt -> cdt.getCliente().getDocId().equals(docId));
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