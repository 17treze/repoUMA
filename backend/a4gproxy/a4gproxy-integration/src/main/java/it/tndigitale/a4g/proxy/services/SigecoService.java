package it.tndigitale.a4g.proxy.services;

import java.util.Optional;

public interface SigecoService {

	public Long recuperaEsitoControlloSigeco(Long anno, String numeroDomanda);

	public Optional<Boolean> recuperaFlagConv(Long annoCampagna, String numeroDomanda, String cuaa);

}
