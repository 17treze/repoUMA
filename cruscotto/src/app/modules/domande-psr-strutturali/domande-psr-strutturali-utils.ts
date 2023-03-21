import { InfoGeneraliPSRStrutturale } from './models/info-generali-domanda-psr-strutturale';

export function getTitleForDomandaStatus(domanda: InfoGeneraliPSRStrutturale): string {
  return domanda.statoProgetto.toUpperCase();
}

export function getIconForDatiPagamentoByTipologia(tipologiaDatiPagamento: string): string {
  const tipologia = tipologiaDatiPagamento.toLowerCase();
  const icon = tipologia === 'finanziabilita' ? 'finanziabile' : tipologia;
  return `assets/icons/svg/svg-psr-strutt/tipologie/${icon}.svg`;
}
