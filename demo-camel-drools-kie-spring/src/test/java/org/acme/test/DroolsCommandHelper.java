package org.acme.test;

import org.apache.camel.Exchange;
import org.drools.core.command.impl.GenericCommand;
import org.drools.core.command.runtime.BatchExecutionCommandImpl;
import org.drools.core.command.runtime.rule.FireAllRulesCommand;
import org.drools.core.command.runtime.rule.InsertObjectCommand;

import java.util.List;

public class DroolsCommandHelper {

    public BatchExecutionCommandImpl insertCommands(Exchange exchange) {
        final Object body = exchange.getIn().getBody();

        BatchExecutionCommandImpl command = new BatchExecutionCommandImpl();
        final List<GenericCommand<?>> commands = command.getCommands();
        commands.add(new InsertObjectCommand(body, "person"));
        commands.add(new FireAllRulesCommand());

        return command;
    }

}