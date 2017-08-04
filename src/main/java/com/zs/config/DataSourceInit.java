package com.zs.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.zs.handler.UserHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zsq-1186
 * Date: 2017-08-04
 * Time: 17:44
 */

@Configuration
@EnableCaching
@MapperScan(basePackages = "com.zs.repository")
public class DataSourceInit {

    static Logger logger= LoggerFactory.getLogger(DataSourceInit.class.getName());

    /** 要加载的 mybatis 的配置文件目录 */
    private static final String[] RESOURCE_PATH = new String[] { "auto/*.xml" };
    private static final Resource[] RESOURCE_ARRAY;

    /** 要加载的 mybatis 类型处理器的目录  随便选择一个用来获取包名*/
    private static final String PACKAGE = UserHandler.class.getPackage().getName();
    private static final TypeHandler[] HANDLE_ARRAY;
    static {
        RESOURCE_ARRAY = getResourceArray();
        HANDLE_ARRAY = getHandleArray();
    }

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
        logger.info("mybatis load xml:({})", toStr(RESOURCE_ARRAY));
        sessionFactory.setMapperLocations(RESOURCE_ARRAY);
        // 分页插件
        //sessionFactory.setPlugins(new Interceptor[]{ mybatisPage() });
        logger.info("mybatis load type handle:({})", toStr(HANDLE_ARRAY));
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



    /** 获取 mybatis 要加载的 xml 文件 */
    private static Resource[] getResourceArray() {
        List<Resource> resourceList = new ArrayList<>();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        for (String path : RESOURCE_PATH) {
            try {
                Resource[] resources = resolver.getResources(path);
                if (resources!=null && resources.length!=0) {
                    Collections.addAll(resourceList, resources);
                }
            } catch (IOException e) {
                logger.error(String.format("load file(%s) exception", path), e);

            }
        }
        return resourceList.toArray(new Resource[resourceList.size()]);
    }


    /** 获取 mybatis 装载的类型处理 */
    private static TypeHandler[] getHandleArray() {
        List<TypeHandler> handlerList = new ArrayList<>();
        String packageName = PACKAGE.replace(".", "/");
        URL url = com.zs.config.DataSourceInit.class.getClassLoader().getResource(packageName);
        if (url != null) {

            logger.debug("current mybatis load TypeHandler protocol: " + url.getProtocol());
            if ("file".equals(url.getProtocol())) {
                File parent = new File(url.getPath());
                if (parent.isDirectory()) {
                    File[] files = parent.listFiles();
                    if (files!=null && files.length!=0) {
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
                    logger.error("can't load jar file", e);
                }
            }
        }
        return handlerList.toArray(new TypeHandler[handlerList.size()]);
    }


    private static TypeHandler getTypeHandler(String name) {
        if (!StringUtils.isEmpty(name)) {
            String className = PACKAGE + "." + name.replace(".class", "");
            try {
                Class<?> clazz = Class.forName(className);
                if (clazz != null && TypeHandler.class.isAssignableFrom(clazz)) {
                    return (TypeHandler) clazz.newInstance();
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {

                logger.error(String.format("TypeHandler clazz (%s) exception", className), e);
            }
        }
        return null;
    }

    /** 以，拼接数组值**/
    public String toStr(Object[] array){
        StringBuilder sb=new StringBuilder();
        for (Object obj:array){
            sb.append(obj);
            sb.append(",");
        }
        return sb.toString();
    }

}
