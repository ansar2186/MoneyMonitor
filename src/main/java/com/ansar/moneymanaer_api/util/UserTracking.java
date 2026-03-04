package com.ansar.moneymanaer_api.util;

import com.ansar.moneymanaer_api.entity.UserLoginAudit;
import com.ansar.moneymanaer_api.repository.UserLoginAuditRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserTracking {

    private final UserLoginAuditRepository userLoginAuditRepository;

    public static String getClientIp(HttpServletRequest request){
        String[] headers = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP"
        };

        String ip = null;

        for (String header : headers) {
            ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                ip = ip.split(",")[0];
                break;
            }
        }
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    public static String getUserAgent(HttpServletRequest request){
        return request.getHeader("user-agent");
    }

    public  void saveAudit(String userName, String ip, String agent, LocalDateTime localDateTime,String status){
        UserLoginAudit loginAudit = new UserLoginAudit();
        loginAudit.setUserName(userName);
        loginAudit.setIpAddress(ip);
        loginAudit.setUserAgent(agent);
        loginAudit.setLoginTime(localDateTime);
        loginAudit.setCountry("INDIA");
        loginAudit.setCity("NAJIBABAD");
        loginAudit.setLoginStatus(status);

        userLoginAuditRepository.save(loginAudit);

    }
}
