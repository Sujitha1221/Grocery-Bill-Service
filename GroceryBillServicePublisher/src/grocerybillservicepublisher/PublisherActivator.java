package grocerybillservicepublisher;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class PublisherActivator implements BundleActivator {

	ServiceRegistration publishServiceRegistration;

	
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("\n\tStart Grocery Shop Bill Publisher Service");
		BillServicePublish publishService = new BillServicePublishImpl();
		publishServiceRegistration = bundleContext.registerService(BillServicePublish.class.getName(), publishService, null);
	}


	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println("\n\tStop Grocery Shop Bill Publisher Service");
		publishServiceRegistration.unregister();
	}

}
