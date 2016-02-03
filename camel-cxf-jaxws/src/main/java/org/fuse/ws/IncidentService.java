package org.fuse.ws;

import org.fuse.ws.model.InputReportIncident;
import org.fuse.ws.model.InputStatusIncident;
import org.fuse.ws.model.OutputReportIncident;
import org.fuse.ws.model.OutputStatusIncident;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Interface with the services we want to expose as web services using code first.
 * <p/>
 * This is a basic example, you can use the JAX-WS annotations to control the contract.
 */
@WebService(serviceName = "incidentService", targetNamespace="http://service.ws.fuse.org/", portName = "incidentServicePort")
public interface IncidentService {

    /**
     * Operation to report an incident
     */
    @WebMethod(action = "reportIncident")
    OutputReportIncident reportIncident(InputReportIncident input);

    /**
     * Operation to get the status of an incident
     */
    OutputStatusIncident statusIncident(InputStatusIncident input);
}
