package de.awr;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

@Configuration
public class ExposeEntityIdRestConfiguration extends RepositoryRestMvcConfiguration {
	
	public ExposeEntityIdRestConfiguration(ApplicationContext context,
			ObjectFactory<ConversionService> conversionService) {
		super(context, conversionService);
	}

	@Override
	public RepositoryRestConfiguration repositoryRestConfiguration() {
		RepositoryRestConfiguration configuration = super.repositoryRestConfiguration();
		configuration.exposeIdsFor(Station.class);
		return configuration;
	}
}