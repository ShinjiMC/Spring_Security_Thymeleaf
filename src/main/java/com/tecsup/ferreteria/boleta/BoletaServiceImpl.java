package com.tecsup.ferreteria.boleta;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoletaServiceImpl implements BoletaService {
    private final BoletaRepository boletaRepository;

    @Override
    public Boleta saveBoleta(Boleta boleta) {
        return boletaRepository.save(boleta);
    }

    @Override
    public Boleta getBoleta(Long id) {
        return boletaRepository.findById(id).orElseThrow();
    }
}
