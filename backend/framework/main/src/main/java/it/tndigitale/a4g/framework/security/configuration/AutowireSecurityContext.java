package it.tndigitale.a4g.framework.security.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AutowireSecurityContext implements ApplicationContextAware {
		private static final AutowireSecurityContext INSTANCE = new AutowireSecurityContext();
		private ApplicationContext applicationContext;

		private AutowireSecurityContext() {}
		
		/**
		 * @return the singleton instance.
		 */
		public static AutowireSecurityContext build() {
				return INSTANCE;
		}
		
		@Override
		public void setApplicationContext(final ApplicationContext applicationContext) {
				this.applicationContext = applicationContext;
		}

		/**
		 * Tries to autowire the specified instance of the class if one of the specified beans which need to be autowired are null.
		 *
		 * @param classToAutowire
		 *            the instance of the class which holds @Autowire annotations
		 * @param beansToAutowireInClass
		 *            the beans which have the @Autowire annotation in the specified {#classToAutowire}
		 */
		public void autowire(Object classToAutowire, Object... beansToAutowireInClass) {
				for (Object bean : beansToAutowireInClass) {
						if (bean == null) {
								this.applicationContext.getAutowireCapableBeanFactory().autowireBean(classToAutowire);
								return;
						}
				}
		}

		public <T> T newObjectBy(Class<T> classToAutowire) {
				return applicationContext.getBean(classToAutowire);
		}

		@SuppressWarnings("unchecked")
		public <T> T newObjectBy(String beanName) {
				return (T) applicationContext.getBean(beanName);
		}
}
