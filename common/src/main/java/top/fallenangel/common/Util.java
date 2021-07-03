package top.fallenangel.common;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class Util {
    public static void responseJson(HttpServletResponse response, Object data) {
        response.setContentType("application/json; charset=UTF-8");
        response.addHeader("access-control-allow-origin", "*");

        try {
            response.getWriter().write(JSON.toJSONString(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
