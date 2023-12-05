package com.tecsup.ferreteria.boleta;

public interface BoletaService {
    Boleta saveBoleta(Boleta boleta);

    Boleta getBoleta(Long id);
}
