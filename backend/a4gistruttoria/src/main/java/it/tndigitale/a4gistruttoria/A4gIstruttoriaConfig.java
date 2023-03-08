package it.tndigitale.a4gistruttoria;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.tndigitale.a4gistruttoria.component.CaricaPremioCalcolatoSostegnoFactory;
import it.tndigitale.a4gistruttoria.component.IoItaliaInvioMessaggioFactory;
import it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno.Bridu50ControlloAntimafiaFactory;
import it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno.InizializzaDatiIntersostegnoFactory;
import it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno.PassoDisciplinaFinanziariaFactory;
import it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione.CaricatoreDatiLiquidazioneIstruttoriaFactory;
import it.tndigitale.a4gistruttoria.service.businesslogic.processo.ElaborazioneProcessoAsyncStrategyFactory;
import it.tndigitale.a4gistruttoria.service.businesslogic.verbale.VerbaleLiquidazioneServiceFactory;
import it.tndigitale.a4gistruttoria.strategy.processi.ProcessoStrategyFactory;

@Configuration
public class A4gIstruttoriaConfig {

	@Bean
	public AutowireHelper createAutowireHelper() {
		return AutowireHelper.getInstance();

	}
	
	@Bean("ProcessoStrategyFactory")
	 public FactoryBean serviceLocatorFactoryBean() {
	    ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
	    factoryBean.setServiceLocatorInterface(ProcessoStrategyFactory.class);
	    return factoryBean;
	 }	
	
	@Bean("processoFactoryIstruttoria")
	 public FactoryBean serviceLocatorFactoryBeanIstruttoria() {
	    ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
	    factoryBean.setServiceLocatorInterface(ElaborazioneProcessoAsyncStrategyFactory.class);
	    return factoryBean;
	 }	

	@Bean("inizializzatoreDatiIntersostegnoFactory")
	 public FactoryBean serviceLocatorFactoryBeanDatiIntersostegno() {
	    ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
	    factoryBean.setServiceLocatorInterface(InizializzaDatiIntersostegnoFactory.class);
	    return factoryBean;
	 }	
	
	@Bean("inizializzaPassoDisciplinaFinanziariaFactory")
	 public FactoryBean serviceLocatorFactoryBeanPassoDisciplinaFinanziaria() {
	    ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
	    factoryBean.setServiceLocatorInterface(PassoDisciplinaFinanziariaFactory.class);
	    return factoryBean;
	 }	
	
	@Bean("inizializzaBridu50ControlloAntimafiaFactory")
	 public FactoryBean serviceLocatorFactoryBeanBridu50ControlloAntimafia() {
	    ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
	    factoryBean.setServiceLocatorInterface(Bridu50ControlloAntimafiaFactory.class);
	    return factoryBean;
	 }	
	
	
	@Bean("caricatoreDatiLiquidazioneIstruttoriaFactory")
	 public FactoryBean serviceLocatorFactoryCaricatoreDatiLiquidazioneIstruttoria() {
	    ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
	    factoryBean.setServiceLocatorInterface(CaricatoreDatiLiquidazioneIstruttoriaFactory.class);
	    return factoryBean;
	 }	
	
	@Bean("verbaleLiquidazioneServiceFactory")
	 public FactoryBean serviceLocatorFactoryVerbaleLiquidazioneService() {
	    ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
	    factoryBean.setServiceLocatorInterface(VerbaleLiquidazioneServiceFactory.class);
	    return factoryBean;
	 }	
	
	@Bean("caricaPremioCalcolatoSostegnoFactory")
	 public FactoryBean serviceLocatorFactoryCaricaPremioCalcolatoSostegno() {
	    ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
	    factoryBean.setServiceLocatorInterface(CaricaPremioCalcolatoSostegnoFactory.class);
	    return factoryBean;
	 }	
	
	@Bean("ioItaliaInvioMessaggioFactory")
	 public FactoryBean serviceLocatorFactoryIoItaliaInvioMessaggioFactory() {
	    ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
	    factoryBean.setServiceLocatorInterface(IoItaliaInvioMessaggioFactory.class);
	    return factoryBean;
	 }	
}
