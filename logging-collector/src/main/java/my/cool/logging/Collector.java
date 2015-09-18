package my.cool.logging;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;

public class Collector implements BundleActivator {

    LogService logger = null;
    LogReaderService reader = null;

    @Override
    public void start(BundleContext context) throws Exception {

        ServiceReference srfLogReader = context.getServiceReference(LogReaderService.class.getName());
        /*ServiceReference srfLog = context.getServiceReference(LogService.class.getName());

        if (srfLog != null) {
            logger = (LogService) context.getService(srfLog);
            logger.log(3, "! Bundle Started & LogService retrieved !");
        }*/

        if (srfLogReader != null) {
            LogReaderService reader = (LogReaderService) context.getService(srfLogReader);
            reader.addLogListener(new LogWriter());
            //logger.log(3, "! Registered listener with the Log ReaderService !");
        }
    }

    @Override
    public void stop(BundleContext context) throws Exception { }
}
