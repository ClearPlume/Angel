package top.fallenangel.common;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;

public class Util {
    public static String readBodyJson(HttpServletRequest request) {
        StringBuilder json = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = request.getReader();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (reader != null) {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return json.toString();
    }

    public static void responseJson(HttpServletResponse response, Object data) {
        response.setContentType("application/json; charset=UTF-8");

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
