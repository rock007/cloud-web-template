package org.cloud.webtemplate.controller.front;

import java.util.List;
import java.util.Map;

import org.cloud.core.base.BaseController;
import org.cloud.db.sys.entity.SysRole;
import org.cloud.db.sys.service.UserService;
import org.cloud.webtemplate.db.entity.User;
import org.cloud.webtemplate.db.entity.enums.AgeEnum;
import org.cloud.webtemplate.db.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@Controller
public class HomeController extends BaseController {

	@Autowired
	private IUserService userService;
	
	@Autowired
	private UserService userService2;
	
	@RequestMapping("/")
	public String backend(Map<String, Object> model){

		return "redirect:index";
	}
	
	@RequestMapping("/index")
	public String index(Map<String, Object> model){

		String m="22";
		List<SysRole> roles=userService2.getRolesAll();
		IPage<User> page= userService.page(new Page<User>(0, 12), null);
		
		return "index";
	}
	
	@GetMapping("/test")
	public @ResponseBody  IPage<User> test() {

		User user = new User("张三", AgeEnum.TWO, 1);
		boolean result = userService.save(user);
		// 自动回写的ID
		Long id = user.getId();
		System.err.println("插入一条数据：" + result + ", 插入信息：" + user.toString());
		System.err.println("查询：" + userService.getById(id).toString());
		System.err.println("更新一条数据：" + userService.updateById(new User(1L, "三毛", AgeEnum.ONE, 1)));
		for (int i = 0; i < 5; ++i) {
			userService.save(new User(Long.valueOf(100 + i), "张三" + i, AgeEnum.ONE, 1));
		}

		return userService.page(new Page<User>(0, 12), null);
	}
}
