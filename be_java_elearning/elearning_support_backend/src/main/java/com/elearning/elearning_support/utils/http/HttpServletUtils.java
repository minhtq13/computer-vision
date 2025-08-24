package com.elearning.elearning_support.utils.http;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import com.elearning.elearning_support.utils.object.ObjectUtils;

public class HttpServletUtils {

    private static final String[] IP_ADD_HEADERS = {
        "X-Real-IP",
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR"
    };

    /**
     * Get all client's request ip addresses
     */
    public static Set<String> getClientIPAddresses(HttpServletRequest request) {
        Set<String> ips = new LinkedHashSet<>();
        ips.add(request.getRemoteAddr());
        for(String header : IP_ADD_HEADERS){
            String ipAddress = ObjectUtils.getOrDefault(request.getHeader(header), request.getHeader(header.toLowerCase()));
            if(Objects.nonNull(ipAddress) && StringUtils.hasText(ipAddress) && !"unknown".equalsIgnoreCase(ipAddress)){
                ips.add(ipAddress);
            }
        }
        ips.removeIf(Objects::isNull);
        return ips;
    }

    public String getReferer(HttpServletRequest request) {
        return request.getHeader("referer");
    }

    public String getFullUrl(HttpServletRequest request) {
        final StringBuffer requestURL = request.getRequestURL();
        final String queryString = request.getQueryString();

        return queryString == null ? requestURL.toString() : requestURL.append('?')
            .append(queryString)
            .toString();
    }

    /**
     * ref: <a href="http://stackoverflow.com/a/18030465/1845894">...</a>
     */
    public String getClientOS(HttpServletRequest request) {
        final String browserDetails = request.getHeader("User-Agent");

        // Check OS
        final String lowerCaseBrowser = browserDetails.toLowerCase();
        if (lowerCaseBrowser.contains("windows")) {
            return "Windows";
        } else if (lowerCaseBrowser.contains("mac")) {
            return "Mac";
        } else if (lowerCaseBrowser.contains("x11")) {
            return "Unix";
        } else if (lowerCaseBrowser.contains("android")) {
            return "Android";
        } else if (lowerCaseBrowser.contains("iphone")) {
            return "IPhone";
        } else {
            return "UnKnown, More-Info: " + browserDetails;
        }
    }

    /**
     * ref: <a href="http://stackoverflow.com/a/18030465/1845894">...</a>
     */
    public String getClientBrowser(HttpServletRequest request) {
        final String browserDetails = request.getHeader("User-Agent");
        final String user = browserDetails.toLowerCase();
        String browser = "";

        // Check browser
        if (user.contains("msie")) {
            String substring = browserDetails.substring(browserDetails.indexOf("MSIE")).split(";")[0];
            browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
        } else {
            String[] split = browserDetails.substring(
                browserDetails.indexOf("Version")).split(" ");
            if (user.contains("safari") && user.contains("version")) {
                browser = (browserDetails.substring(browserDetails.indexOf("Safari")).split(" ")[0]).split(
                    "/")[0] + "-" + (split[0]).split("/")[1];
            } else if (user.contains("opr") || user.contains("opera")) {
                if (user.contains("opera"))
                    browser = (browserDetails.substring(browserDetails.indexOf("Opera")).split(" ")[0]).split(
                        "/")[0] + "-" + (split[0]).split("/")[1];
                else if (user.contains("opr"))
                    browser = ((browserDetails.substring(browserDetails.indexOf("OPR")).split(" ")[0]).replace("/",
                        "-")).replace("OPR", "Opera");
            } else if (user.contains("chrome")) {
                browser = (browserDetails.substring(browserDetails.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
            } else if ((user.contains("mozilla/7.0")) || (user.contains("netscape6")) || (user.contains("mozilla/4.7")) || (user.contains("mozilla/4.78")) || (user.contains("mozilla/4.08")) || (user.contains("mozilla/3"))) {
                //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
                browser = "Netscape-?";

            } else if (user.contains("firefox")) {
                browser = (browserDetails.substring(browserDetails.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
            } else if (user.contains("rv")) {
                browser = "IE";
            } else {
                browser = "UnKnown, More-Info: " + browserDetails;
            }
        }

        return browser;
    }

    /**
     * get user-agent from http request
     */
    public String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

}
