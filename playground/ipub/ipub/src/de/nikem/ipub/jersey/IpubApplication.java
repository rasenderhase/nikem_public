package de.nikem.ipub.jersey;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import de.nikem.ipub.resource.publication.PublicationListResource;
import de.nikem.ipub.resource.publication.PublicationResource;

public class IpubApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> s = new HashSet<Class<?>>();
		s.add(PublicationListResource.class);
		s.add(PublicationResource.class);
		return s;
	}
}
