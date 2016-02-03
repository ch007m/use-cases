package org.fuse.ws.bean;

import org.apache.camel.Exchange;
import org.fuse.ws.model.InputReportIncident;
import org.fuse.ws.model.OutputReportIncident;

/**
 * Bean processing the report incident.
 */
public class ReportIncidentBean {

    public void process(Exchange exchange) throws Exception {
        // get the id of the input
        String id = exchange.getIn().getBody(InputReportIncident.class).getIncidentId();

        // set reply including the id
        OutputReportIncident output = new OutputReportIncident();
        output.setCode("OK;" + id);
        exchange.getOut().setBody(output);
    }

}
