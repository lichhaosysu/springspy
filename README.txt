将下面这个Servlet声明到web.xml中启动Spring容器的监听器org.springframework.web.context.ContextLoaderListener后面

	<servlet>
		<servlet-name>springspy</servlet-name>
		<servlet-class>com.lichhao.springspy.SpringSpyMonitorServlet</servlet-class>
	</servlet>
	<servlet-mapping>
		<servlet-name>springspy</servlet-name>
		<url-pattern>/springspy/*</url-pattern>
	</servlet-mapping>
	
然后访问http://hostname:port/appName/springspy/index.html即可看到效果

hostname：主机名或ip地址
port：端口号
appName：工程名称	