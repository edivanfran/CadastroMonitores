package br.com.monitoria.model;

import br.com.monitoria.excecoes.PesosInvalidosException;
import java.time.LocalDate;

public class EditalBuilder {
    private String numero;
    private LocalDate dataInicio = LocalDate.now();
    private LocalDate dataLimite;
    private double pesoCre = 0.5;
    private double pesoNota = 0.5;

    public EditalBuilder comNumero(String numero) {
        this.numero = numero;
        return this;
    }

    public EditalBuilder comDatas(LocalDate dataInicio, LocalDate dataLimite) {
        this.dataInicio = dataInicio;
        this.dataLimite = dataLimite;
        return this;
    }

    public EditalBuilder comPesoCre(double pesoCre) {
        this.pesoCre = pesoCre;
        return this;
    }

    public EditalBuilder comPesoNota(double pesoNota) {
        this.pesoNota = pesoNota;
        return this;
    }

    public EditalDeMonitoria build() throws PesosInvalidosException {
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("O número do edital é obrigatório.");
        }
        if (dataLimite == null) {
            throw new IllegalArgumentException("A data limite do edital é obrigatória.");
        }
        return new EditalDeMonitoria(numero, dataInicio, dataLimite, pesoCre, pesoNota);
    }
}
