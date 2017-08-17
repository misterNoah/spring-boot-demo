package com.zs.util;


import org.json.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

/** <span style="color:red;">!!!此工具类请只在 controller 中调用!!!</span> */
public final class RequestUtils {

    public static final String USER_AGENT = "User-Agent";
    public static final String REFERRER = "referer";
    public static final String APP_VER = "app-ver";

    /**
     * 获取真实客户端IP
     * 关于 X-Forwarded-For 参考: http://zh.wikipedia.org/wiki/X-Forwarded-For<br>
     * 这一 HTTP 头一般格式如下:
     * X-Forwarded-For: client1, proxy1, proxy2,<br><br>
     * 其中的值通过一个 逗号 + 空格 把多个 IP 地址区分开, 最左边(client1)是最原始客户端的IP地址,
     * 代理服务器每成功收到一个请求，就把请求来源IP地址添加到右边
     */
    public static String getRealIp() {
        HttpServletRequest request = getRequest();
        String ip = request.getHeader("X-Real-IP");
        if (U.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }

        ip = request.getHeader("X-Forwarded-For");
        if (U.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为 真实 ip
            return ip.split(",")[0].trim();
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (U.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (U.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }

        ip = request.getHeader("HTTP_CLIENT_IP");
        if (U.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }

        ip = request.getHeader("X-Cluster-Client-IP");
        if (U.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }
        return request.getRemoteAddr();
    }

    /** 判断当前请求是否是 ajax 请求, 是 ajax 则返回 true */
    public static boolean isAjaxRequest() {
        HttpServletRequest request = getRequest();
        String requestedWith = request.getHeader("X-Requested-With");
        String contentType = request.getHeader("Content-Type");
        return (requestedWith != null && "XMLHttpRequest".equals(requestedWith))
                || (contentType != null && "application/json".startsWith(contentType))
                || U.isNotBlank(getHeaderOrParam("_ajax"))
                || U.isNotBlank(getHeaderOrParam("_json"));
    }



    /**
     * <pre>
     * 获取完整的 url 地址(此方法与 request.getRequestURL 等价).
     *
     * <a href="http://tomcat.apache.org/tomcat-8.0-doc/api/org/apache/catalina/valves/RemoteIpValve.html">https</a>
     * 前台走的是 https, 运维经过 nginx 后再转到 tomcat 却成了 http 时:
     *
     * 先在 nginx 的 location 中加: proxy_set_header X-Forwarded-Proto $scheme;
     *
     * 再在 tomcat 的 server.xml 中 context 节点添加下面的配置, 代码就不需要任何变动
     * &lt;Valve className="org.apache.catalina.valves.RemoteIpValve" remoteIpHeader="X-Forwarded-For"
     *  protocolHeader="X-Forwarded-Proto" protocolHeaderHttpsValue="https" /&gt;
     * </pre>
     *
     * @see org.apache.catalina.connector.Request#getRequestURL()
     */
    public static String getRequestUrl() {
        HttpServletRequest request = getRequest();
        // return request.getRequestURL().toString();

        String scheme = request.getScheme();
        int port = request.getServerPort();
        if (port < 0) {
            port = 80;
        }

        StringBuilder sbd = new StringBuilder();
        sbd.append(scheme).append("://").append(request.getServerName());
        if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
            sbd.append(':').append(port);
        }
        sbd.append(request.getRequestURI());
        return sbd.toString();
    }


    /** 请求头里的 referer 这个单词拼写是错误的, 应该是 referrer, 然而历史遗留原因导致这个问题永远不会被更正 */
    public static String getReferrer() {
        return getRequest().getHeader(REFERRER);
    }

    /** 先从请求头中查, 为空再从参数中查 */
    public static String getHeaderOrParam(String param) {
        HttpServletRequest request = getRequest();
        String header = request.getHeader(param);
        if (U.isBlank(header)) header = request.getParameter(param);
        return U.isBlank(header) ? U.EMPTY : header.trim();
    }

    /** 格式化头里的参数: 每对值以换行隔开, 键值以冒号分隔 */
    public static String formatHeadParam() {
        HttpServletRequest request = getRequest();

        StringBuilder sbd = new StringBuilder();
        int i = 0;
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            if (i > 0) sbd.append("\n");
            String headName = headerNames.nextElement();
            sbd.append(headName).append(" : ").append(request.getHeader(headName));
            i++;
        }
        return sbd.toString();
    }


    /** 将「字符串」以 json 格式输出 */
    public static void toJson(String result, HttpServletResponse response) throws IOException {
        render("application/json", result, response);
    }
    private static void render(String type, String result, HttpServletResponse response) throws IOException {

        response.setContentType(type + ";charset=UTF-8;");
        response.getWriter().write(result);
    }
    /** 将「字符」以 html 格式输出. 不常见! 这种只会在一些特殊的场景用到 */
    public static void toHtml(String result, HttpServletResponse response) throws IOException {
        render("text/html", result, response);
    }

    private static ServletRequestAttributes getRequestAttributes() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
    }

    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }
}
