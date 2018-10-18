package org.cloud.webtemplate.controller.backend;


import org.cloud.core.base.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@CrossOrigin( maxAge = 3600)
@Controller
@RequestMapping("/backend")
public class DashboardController  extends BaseController{

	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

	@RequestMapping("/crud.html")
	public String crud(Map<String, Object> model){

		return "pages/crud";
	}

}
