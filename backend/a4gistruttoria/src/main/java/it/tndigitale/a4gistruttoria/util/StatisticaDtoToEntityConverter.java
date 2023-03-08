package it.tndigitale.a4gistruttoria.util;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.StatisticaDu;
import it.tndigitale.a4gistruttoria.repository.model.A4gtStatisticaDu;

@Component
public class StatisticaDtoToEntityConverter implements Converter<StatisticaDu, A4gtStatisticaDu> {

	@Override
	public A4gtStatisticaDu convert(StatisticaDu source) {
		A4gtStatisticaDu target = new A4gtStatisticaDu();
		BeanUtils.copyProperties(source, target);
		target.setF300b(LocalDateConverter.to(source.getF300b()));
		target.setStato(source.getStato());
		return target;
	}
}
