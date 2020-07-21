package com.zx.blog.aspect;


import com.zx.blog.annotation.SystemLog;
import com.zx.blog.entity.SysLog;
import com.zx.blog.service.SysLogService;
import com.zx.blog.service.UserService;
import com.zx.blog.util.IpInfoUtil;
import com.zx.blog.util.ObjectUtil;
import com.zx.blog.util.ThreadPoolUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zouxu
 * @date 2020/3/31 20:59
 */
@Aspect
@Component
public class SysLogAop {

	private static final ThreadLocal<Date> BEGIN_TIME_THREAD_LOCAL = new NamedThreadLocal<>("ThreadLocal beginTime");

	@Autowired
	private SysLogService sysLogService;

	@Autowired
	private UserService userService;

	@Autowired
	private IpInfoUtil ipInfoUtil;

	@Autowired(required = false)
	private HttpServletRequest request;

	/**
	 * Controller层切点,注解方式
	 */
	@Pointcut("@annotation(com.zx.blog.annotation.SystemLog)")
	public void controllerAspect() {

	}

	/**
	 * 前置通知 (在方法执行之前返回)用于拦截Controller层记录用户的操作的开始时间
	 *
	 * @param joinPoint 切点
	 * @throws InterruptedException
	 */
	@Before("controllerAspect()")
	public void doBefore(JoinPoint joinPoint) throws InterruptedException {

		//线程绑定变量（该数据只有当前请求的线程可见）
		Date beginTime = new Date();
		BEGIN_TIME_THREAD_LOCAL.set(beginTime);

	}


	/**
	 * 后置通知(在方法执行之后并返回数据) 用于拦截Controller层无异常的操作
	 *
	 * @param joinPoint 切点
	 */
	@AfterReturning("controllerAspect()")
	public void after(JoinPoint joinPoint) {
		try {
			String username = "";
			String description = getControllerMethodInfo(joinPoint).get("description").toString();
			Map<String, String[]> requestParams = request.getParameterMap();

			SysLog log = new SysLog();

			//请求用户
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			log.setUsername(auth.getName());
			//日志标题
			log.setName(description);
			//日志请求url
			log.setRequestUrl(request.getRequestURI());
			//请求方式
			log.setRequestType(request.getMethod());
			//请求参数
			log.setRequestParam(ObjectUtil.mapToString(requestParams));

			//其他属性
			log.setIp(ipInfoUtil.getIpAddr(request));
			log.setCreateBy("system");
			log.setUpdateBy("system");
			log.setCreateTime(new Date());
			log.setUpdateTime(new Date());

			//请求开始时间
			long beginTime = BEGIN_TIME_THREAD_LOCAL.get().getTime();
			long endTime = System.currentTimeMillis();
			//请求耗时
			Long logElapsedTime = endTime - beginTime;
			log.setCostTime(logElapsedTime.intValue());

			//持久化(存储到数据或者ES，可以考虑用线程池)
			ThreadPoolUtil.getPool().execute(new SaveSystemLogThread(log, sysLogService));

		} catch (Exception e) {
			e.printStackTrace();
			BEGIN_TIME_THREAD_LOCAL.remove();
		}
	}


	/**
	 * 保存日志至数据库
	 */
	private static class SaveSystemLogThread implements Runnable {

		private SysLog sysLog;
		private SysLogService sysLogService;

		public SaveSystemLogThread(SysLog sysLog, SysLogService sysLogService) {
			this.sysLog = sysLog;
			this.sysLogService = sysLogService;
		}

		@Override
		public void run() {
			sysLogService.save(sysLog);
		}
	}

	/**
	 * 获取注解中对方法的描述信息 用于Controller层注解
	 *
	 * @param joinPoint 切点
	 * @return 方法描述
	 * @throws Exception
	 */
	public static Map<String, Object> getControllerMethodInfo(JoinPoint joinPoint) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>(16);
		//获取目标类名
		String targetName = joinPoint.getTarget().getClass().getName();
		//获取方法名
		String methodName = joinPoint.getSignature().getName();
		//获取相关参数
		Object[] arguments = joinPoint.getArgs();
		//生成类对象
		Class targetClass = Class.forName(targetName);
		//获取该类中的方法
		Method[] methods = targetClass.getMethods();

		String description = "";

		for (Method method : methods) {
			if (!method.getName().equals(methodName)) {
				continue;
			}
			Class[] clazz = method.getParameterTypes();
			if (clazz.length != arguments.length) {
				//比较方法中参数个数与从切点中获取的参数个数是否相同，原因是方法可以重载哦
				continue;
			}
			description = method.getAnnotation(SystemLog.class).description();
			map.put("description", description);
		}
		return map;
	}
}
