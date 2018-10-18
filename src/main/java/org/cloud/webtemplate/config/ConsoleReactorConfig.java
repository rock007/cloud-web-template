package org.cloud.webtemplate.config;

import org.cloud.webtemplate.receiver.LogReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.Environment;
import reactor.bus.EventBus;

import javax.annotation.PostConstruct;

import static reactor.bus.selector.Selectors.$;

@Component
public class ConsoleReactorConfig {

    @Bean
    Environment env() {
        return Environment.initializeIfEmpty()
                          .assignErrorJournal();
    }
    
    @Bean
    EventBus createEventBus(Environment env) {
	    return EventBus.create(env, Environment.THREAD_POOL);
    }

	@Autowired
	private EventBus eventBus;

	@Autowired
	private LogReceiver receiver;
	
    @PostConstruct
    public void onStartUp() {

    	System.out.println("game is begin");
    	
		eventBus.on($("quotes"), receiver);
    	
    }

}