package com.tttare.springDemo.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ClassName: HttpUtil <br/>
 * Description: http tool <br/>
 * date: 2019/9/1 10:36<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Slf4j
public class HttpUtil {

    public static void writJsonString(HttpServletResponse response, String str) throws Exception{
        if(StringUtils.isEmpty(str)) {
            throw new NullPointerException("response params is empty");
        }
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(str);
        } catch (IOException e) {
            log.error("Exception:",e);
            throw e;
        }
    }
}
