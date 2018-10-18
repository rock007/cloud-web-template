/**
 * @Title ShiroAuthRealm.java
 * @date 2013-11-2 下午3:52:21
 * @Copyright: 2013
 */
package org.cloud.webtemplate.config.security;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.cloud.core.shiro.jwt.JwtToken;
import org.cloud.core.shiro.jwt.TokenProvider;
import org.cloud.core.utils.EncriptUtil;
import org.cloud.db.sys.entity.SysUser;
import org.cloud.db.sys.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class ShiroAuthRealm extends AuthorizingRealm{

	private static final Logger logger = LoggerFactory.getLogger(ShiroAuthRealm.class);
	
	@Autowired
	private UserService userService;

	@Autowired
	protected RestTemplate restTemplate;

    @Autowired
    private TokenProvider tokenUtil;
    
	@Override
    public boolean supports(AuthenticationToken token) {
        //表示此Realm只支持JwtToken类型
        return token instanceof JwtToken || token instanceof UsernamePasswordToken;
    }
	
	/**
	 * 授权：验证权限时调用
	 * @param principalCollection
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		//String username = (String) principalCollection.getPrimaryPrincipal();
		
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		
		return simpleAuthorizationInfo;
	}
	
	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException{

		String username,password;
		
		//判断是否token
		if (authenticationToken instanceof JwtToken) {

			JwtToken jwtToken = (JwtToken) authenticationToken;

			// 获取token
			String token = jwtToken.getToken();

			if (token == null || "".equals(token)) {

				throw new IncorrectCredentialsException();
			}

			// 从token中获取用户名
			username = tokenUtil.getUsernameFromToken(token);
			password = tokenUtil.getPasswordFromToken(token);

			// 查询用户信息
			SysUser upmsUser = userService.findUserByName(username);

			if (null == upmsUser) {
				throw new UnknownAccountException();
			}

			if (!StringUtils.isEmpty(password)
					&& !upmsUser.getPassword().equals(EncriptUtil.md5(password + upmsUser.getSalt()))) {
				throw new IncorrectCredentialsException();
			}
			// 用户被禁用
			if (upmsUser.getLocked() != null && upmsUser.getLocked() == 1) {
				throw new LockedAccountException();
			}

			try {
				return new SimpleAuthenticationInfo(upmsUser.getUserId(), token, getName());
			} catch (Exception e) {
				throw new AuthenticationException(e);
			}

		}else {
		
			username = (String) authenticationToken.getPrincipal();
			password =authenticationToken.getCredentials()==null?"": new String((char[]) authenticationToken.getCredentials());
			
			// 查询用户信息
			SysUser upmsUser = userService.findUserByName(username);

			if (null == upmsUser) {
				throw new UnknownAccountException();
			}

			//MD5Util.MD5(password + upmsUser.getSalt())
			if (!"".equals(password)&&!upmsUser.getPassword().equals(EncriptUtil.md5(password + upmsUser.getSalt()))) {
			//if (!"".contains(password)&&!upmsUser.getPassword().equals(password )) {
				throw new IncorrectCredentialsException();
			}
			if (upmsUser.getLocked()!=null&&upmsUser.getLocked() == 1) {
				throw new LockedAccountException();
			}

			return new SimpleAuthenticationInfo(upmsUser.getUserId(), password, getName());
		}
		
	}

}
