package org.cloud.webtemplate.receiver;

import org.cloud.core.model.ActLogModel;
import org.cloud.db.sys.entity.ActErrLog;
import org.cloud.db.sys.service.ActLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.fn.Consumer;

@Service
public class LogReceiver implements Consumer<Event<ActLogModel>> {

	static final Logger logger = LoggerFactory.getLogger(LogReceiver.class);
	
	@Autowired
	private ActLogService actLogService;
	
	public void accept(Event<ActLogModel> ev) {
		
		try {
			
			ActLogModel oneLog= ev.getData();
			if(oneLog!=null) {
				
				ActErrLog log=new ActErrLog();
				BeanUtils.copyProperties(oneLog,log);
				
				actLogService.saveLog(log);
			}	
			
		} catch (Exception e) {

			logger.error("accept error:", e);
		}
		
		logger.debug("LogReceiver","收到值:"+ev.getData().getContent()); 

	}

}