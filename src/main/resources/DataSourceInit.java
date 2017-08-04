package com.hhly.business;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.miemiedev.mybatis.paginator.OffsetLimitInterceptor;
import com.github.miemiedev.mybatis.paginator.dialect.MySQLDialect;
import com.hhly.business.handler.MoneyHandler;
import com.hhly.common.util.*;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 扫描指定目录. MapperScan 的处理类是 MapperScannerRegistrar, 其基于 ClassPathMapperScanner<br>
 *
 * @see org.mybatis.spring.annotation.MapperScannerRegistrar#registerBeanDefinitions
 * @see org.mybatis.spring.mapper.MapperScannerConfigurer#postProcessBeanDefinitionRegistry
 * @see org.mybatis.spring.mapper.ClassPathMapperScanner
 */
@Configuration
@EnableCaching
@MapperScan(basePackages = "com.hhly.business.repository")
public class DataSourceInit {

    /** 要加载的 mybatis 的配置文件目录 */
    private static final String[] RESOURCE_PATH = new String[] { "auto/*.xml", "custom/*.xml" };
    private static final Resource[] RESOURCE_ARRAY;

    /** 要加载的 mybatis 类型处理器的目录 */
    private static final String PACKAGE = MoneyHandler.class.getPackage().getName();
    private static final TypeHandler[] HANDLE_ARRAY;
    static {
        RESOURCE_ARRAY = getResourceArray();
        HANDLE_ARRAY = getHandleArray();
    }

    /** 获取 mybatis 要加载的 xml 文件 */
    private static Resource[] getResourceArray() {
        List<Resource> resourceList = A.lists();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        for (String path : RESOURCE_PATH) {
            try {
                Resource[] resources = resolver.getResources(path);
                if (A.isNotEmpty(resources)) {
                    Collections.addAll(resourceList, resources);
                }
            } catch (IOException e) {
                if (LogUtil.ROOT_LOG.isErrorEnabled())
                    LogUtil.ROOT_LOG.error(String.format("load file(%s) exception", path), e);
            }
        }
        return resourceList.toArray(new Resource[resourceList.size()]);
    }

    /** 获取 mybatis 装载的类型处理 */
    private static TypeHandler[] getHandleArray() {
        List<TypeHandler> handlerList = A.lists();
        String packageName = PACKAGE.replace(".", "/");
        URL url = DataSourceInit.class.getClassLoader().getResource(packageName);
        if (url != null) {
            if (LogUtil.ROOT_LOG.isDebugEnabled())
                LogUtil.ROOT_LOG.debug("current mybatis load TypeHandler protocol: " + url.getProtocol());
            if ("file".equals(url.getProtocol())) {
                File parent = new File(url.getPath());
                if (parent.isDirectory()) {
                    File[] files = parent.listFiles();
                    if (A.isNotEmpty(files)) {
                        for (File file : files) {
                            TypeHandler handler = getTypeHandler(file.getName());
                            if (handler != null) {
                                handlerList.add(handler);
                            }
                        }
                    }
                }
            } else if ("jar".equals(url.getProtocol())) {
                // java 7 之后的新的 try-with-resources 自动关闭资源地语法糖, 只要实现了 lang.AutoCloseable 或 io.Closeable 就行
                try (JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile()) {
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        String name = entries.nextElement().getName();
                        if (name.startsWith(packageName) && name.endsWith(".class")) {
                            TypeHandler handler = getTypeHandler(name.substring(name.lastIndexOf("/") + 1));
                            if (handler != null) {
                                handlerList.add(handler);
                            }
                        }
                    }
                } catch (IOException e) {
                    if (LogUtil.ROOT_LOG.isErrorEnabled())
                        LogUtil.ROOT_LOG.error("can't load jar file", e);
                }
            }
        }
        return handlerList.toArray(new TypeHandler[handlerList.size()]);
    }
    private static TypeHandler getTypeHandler(String name) {
        if (U.isNotBlank(name)) {
            String className = PACKAGE + "." + name.replace(".class", "");
            try {
                Class<?> clazz = Class.forName(className);
                if (clazz != null && TypeHandler.class.isAssignableFrom(clazz)) {
                    return (TypeHandler) clazz.newInstance();
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                if (LogUtil.ROOT_LOG.isErrorEnabled())
                    LogUtil.ROOT_LOG.error(String.format("TypeHandler clazz (%s) exception", className), e);
            }
        }
        return null;
    }

    /**
     * mybatis 的分页插件: http://my.oschina.net/miemiedev/blog/135516
     * @see org.apache.ibatis.executor.Executor#query(org.apache.ibatis.mapping.MappedStatement,Object,org.apache.ibatis.session.RowBounds,org.apache.ibatis.session.ResultHandler)
     */
    private Interceptor mybatisPage() {
        OffsetLimitInterceptor pageInterceptor = new OffsetLimitInterceptor();
        pageInterceptor.setDialectClass(MySQLDialect.class.getName());
        return pageInterceptor;
    }

    /**
     * http://docs.spring.io/spring-boot/docs/1.3.6.RELEASE/reference/htmlsingle/#howto-two-datasources<br/>
     * &#064;EnableTransactionManagement 注解等同于: &lt;tx:annotation-driven /&gt;
     *
     * @see org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
     * @see org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration
     */
    @Bean
    @ConfigurationProperties(prefix = "druid") // 加载以 druid 开头的配置
    public DataSource setupDruid() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(setupDruid());
        // sessionFactory.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        // 下面的代码等同 mybatis-config.xml 配置, mybatis 的 mappers 不支持通配符且标签有先后顺序限制! 因此不使用 my...fig.xml

        // 配置文件
        if (LogUtil.ROOT_LOG.isDebugEnabled()) {
            LogUtil.ROOT_LOG.debug("mybatis load xml:({})", A.toStr(RESOURCE_ARRAY));
        }
        sessionFactory.setMapperLocations(RESOURCE_ARRAY);

        // 插件
        sessionFactory.setPlugins(new Interceptor[]{ mybatisPage() });

        if (LogUtil.ROOT_LOG.isDebugEnabled()) {
            LogUtil.ROOT_LOG.debug("mybatis load type handle:({})", A.toStr(HANDLE_ARRAY));
        }
        // 为什么使用这种复杂的方式, 而不采用下面只定义一个包名让 mybatis 自己扫描
        // 是因为当项目被打成 jar 的形式发布(war 包放进 tomcat 没有这个问题)时, 使用包名扫描的方式无法被加载到
        sessionFactory.setTypeHandlers(HANDLE_ARRAY);
        // 下面的方式只需要设置一个包名就可以了, 但是当打成 jar 包(war 包放入 tomcat 会解压, 没有这个问题)底下的 jar 发布时会无法装载到类
        /** {@link org.apache.ibatis.type.TypeHandlerRegistry#register(String) } */
        /** {@link org.apache.ibatis.io.ResolverUtil#find(org.apache.ibatis.io.ResolverUtil.Test, String) } */
        /** 类装载这里会无法获取到: {@link org.apache.ibatis.io.VFS#getResources(String) } */
        // sessionFactory.setTypeHandlersPackage(MoneyHandler.class.getPackage().getName());

        return sessionFactory.getObject();
    }

    /** 要构建 or 语句, 参考: http://www.mybatis.org/generator/generatedobjects/exampleClassUsage.html */
    @Bean(name = "sqlSessionTemplate", destroyMethod = "clearCache")
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }

    /** 域名相关 */
    @Bean
    @ConfigurationProperties(prefix = "domain")
    public RenderDomain getDomain() {
        return new RenderDomain();
    }


    /**机器人接口域名URL**/
    @Bean
    @ConfigurationProperties(prefix = "lawBot")
    public LawBotDomain getLawBotDomain(){
        return new LawBotDomain();
    }
    /**
     * 扫描 mybatis 的接口目录, 在上面的类处理完之后再扫描<br>
     * 在类上标注 @MapperScan(basePackages = "com.hhly.business.repository") 效果一样
     */
    /*@Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionTemplateBeanName("sqlSessionTemplate");
        mapperScannerConfigurer.setBasePackage(SysLogMapper.class.getPackage().getName());
        return mapperScannerConfigurer;
    }*/
}
