package com.lichhao.springspy;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.context.support.XmlWebApplicationContext;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class SpringSpyMonitorServlet extends HttpServlet {

	private static final String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = "org.springframework.web.context.WebApplicationContext.ROOT";

	private static final String RESOURCE_PATH = "com/lichhao/springspy/resources";

	private ServletContext sc;

	private XmlWebApplicationContext webContext;

	private ConfigurableListableBeanFactory beanFactory;

	private Map<String, BeanDefinition> beanDefinitionMap;

	private Configuration cfg;

	public void init() throws ServletException {
		sc = getServletContext();
		webContext = (XmlWebApplicationContext) sc
				.getAttribute(ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		beanFactory = webContext.getBeanFactory();

		int beanDefinitionCount = beanFactory.getBeanDefinitionCount();
		String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();

		beanDefinitionMap = new HashMap<String, BeanDefinition>(
				beanDefinitionCount);

		for (String beanDefinitionName : beanDefinitionNames) {
			beanDefinitionMap.put(beanDefinitionName,
					beanFactory.getBeanDefinition(beanDefinitionName));
		}
		// 初始化Freemarker
		cfg = new Configuration();
		try {
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			cfg.setDefaultEncoding("UTF-8");
			cfg.setWhitespaceStripping(true);

			cfg.setTemplateLoader(new ClassTemplateLoader(getClass(), "/"
					+ RESOURCE_PATH));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pathInfo = req.getPathInfo();

		// 刷新Spring容器
		if (pathInfo.equals("/refresh")) {
			
			/*
			 * 下面的代码演示和说明了classpath开头的引用资源会因为缓存而无法反映xml文件的最新修改
			 * 
			InputStream resourceAsStream = sc.getResourceAsStream("/WEB-INF/classes/dataSource.xml");
			BufferedReader buf = new BufferedReader(new InputStreamReader(resourceAsStream, "UTF-8"));
			StringBuilder builder = new StringBuilder();
			String line = "";
			while((line = buf.readLine())!=null){
				builder.append(line).append("\n");
			}
			System.out.println(builder.toString());
			
			ClassPathResource res = new ClassPathResource("dataSource.xml",webContext.getClassLoader());
			buf = new BufferedReader(new InputStreamReader(res.getInputStream(), "UTF-8"));
			builder = new StringBuilder();
			line = "";
			while((line = buf.readLine())!=null){
				builder.append(line).append("\n");
			}
			System.out.println(builder.toString());
			
			
			ServletContextResource contextRes = new ServletContextResource(getServletContext(), "/WEB-INF/classes/applicationContext.xml");
			buf = new BufferedReader(new InputStreamReader(contextRes.getInputStream(), "UTF-8"));
			builder = new StringBuilder();
			line = "";
			while((line = buf.readLine())!=null){
				builder.append(line).append("\n");
			}
			System.out.println(builder.toString());
			*/
			
			webContext.refresh();
			beanFactory = webContext.getBeanFactory();

			int beanDefinitionCount = beanFactory.getBeanDefinitionCount();
			String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();

			beanDefinitionMap = new HashMap<String, BeanDefinition>(
					beanDefinitionCount);

			for (String beanDefinitionName : beanDefinitionNames) {
				beanDefinitionMap.put(beanDefinitionName,
						beanFactory.getBeanDefinition(beanDefinitionName));
			}
			resp.sendRedirect("index.html");
			return;
		}

		// html后缀转移为ftl文件访问
		if (pathInfo.endsWith(".html")) {
			Template temp;
			String prefix = pathInfo.substring(pathInfo.lastIndexOf('/') + 1,
					pathInfo.length() - 5);
			temp = cfg.getTemplate(prefix + ".ftl");
			temp.setEncoding("UTF-8");

			Map<String, Object> root = new HashMap<String, Object>();

			if (prefix.equals("index")) { // index -> 首页
				int beanDefinitionCount = beanFactory.getBeanDefinitionCount();
				int page = 1;
				if (StringUtils.isNotBlank(req.getParameter("page"))) {
					page = Integer.valueOf(req.getParameter("page"));
				}

				if ((page - 1) * Pagination.PAGE_SIZE + 1 > beanDefinitionCount) { // 输入超出范围的页数，转到第一页
					// page = 1;
					resp.sendRedirect("index.html?page=1");
					return;
				}
				int firstPos = (page - 1) * Pagination.PAGE_SIZE;
				String[] beanDefinitionNames = beanFactory
						.getBeanDefinitionNames();
				Map<String, BeanDefinition> beanMap = new HashMap<String, BeanDefinition>();
				int begin = firstPos + 1;
				int end = begin;
				int i = 0;
				for (i = 0; i < Pagination.PAGE_SIZE; i++) {
					if (firstPos + i > beanDefinitionCount - 1) {
						break;
					} else {
						beanMap.put(beanDefinitionNames[firstPos + i],
								beanDefinitionMap
										.get(beanDefinitionNames[firstPos + i]));
						end = begin + i;
					}
				}

				root.put("total", beanDefinitionCount);
				root.put("begin", begin);
				root.put("end", end);
				root.put("beanDefinitionMap", beanMap);

				int maxPage = (beanDefinitionCount % Pagination.PAGE_SIZE > 0) ? (beanDefinitionCount
						/ Pagination.PAGE_SIZE + 1)
						: (beanDefinitionCount / Pagination.PAGE_SIZE);

				Pagination pagination = new Pagination();
				List<PageItem> items = new ArrayList<PageItem>();

				PageItem item1 = new PageItem();
				item1.setContent("<<");
				items.add(item1);
				// 设置前一页的按钮
				int first = (page - 1) / Pagination.PAGE_RANGE
						* Pagination.PAGE_RANGE;
				if (first + 1 > Pagination.PAGE_RANGE) {
					item1.setHref("index.html?page="
							+ (first + 1 - Pagination.PAGE_RANGE));
				} else {
					item1.setStyle("disabled");
				}
				// 分页
				int j = 0;
				for (j = 1; j <= Pagination.PAGE_RANGE; j++) {
					if (first + j > maxPage) {
						break;
					}
					PageItem item = new PageItem();
					if (first + j == page) {
						item.setStyle("active");
					}
					item.setContent(first + j + "");
					item.setHref("index.html?page=" + (first + j));
					items.add(item);
				}
				// 设置后一页的按钮
				PageItem item2 = new PageItem();
				item2.setContent(">>");
				items.add(item2);
				if (first + j > maxPage) {
					item2.setStyle("disabled");
				} else {
					item2.setHref("index.html?page=" + (first + j));
				}

				pagination.setPageItems(items);
				root.put("pagination", pagination);

			} else if (prefix.equals("detail")) { // detail -> bean详情
				String beanName = req.getParameter("beanName");
				root.put("beanName", beanName);
				root.put("beanDefinition", beanDefinitionMap.get(beanName));
			} else {

			}

			try {
				temp.process(root, resp.getWriter());
			} catch (TemplateException e) {
				super.doGet(req, resp);
			}
		} else {
			// 负责处理资源请求的方法
			returnResourceFile(pathInfo, req, resp);
		}
	}

	/**
	 * 将jpg,css,js,html文件的读取转移到classpath下的资源，其余访问路径跳转到首页
	 * 
	 * @param pathInfo
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void returnResourceFile(String pathInfo, HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("utf-8");
		if (pathInfo.endsWith(".jpg")) {
			byte[] bytes = IOUtils.readByteArrayFromResource(RESOURCE_PATH
					+ pathInfo);
			if (bytes != null) {
				resp.getOutputStream().write(bytes);
			}
			return;
		}

		String text = IOUtils.readFromResource(RESOURCE_PATH + pathInfo);
		if (pathInfo.endsWith(".css")) {
			resp.setContentType("text/css;charset=utf-8");
			resp.getWriter().write(text);
		} else if (pathInfo.endsWith(".js")) {
			resp.setContentType("text/javascript;charset=utf-8");
			resp.getWriter().write(text);
		} else {
			resp.sendRedirect(req.getContextPath() + req.getServletPath()
					+ "/index.html");
		}

	}

	public void destroy() {
	}

}
