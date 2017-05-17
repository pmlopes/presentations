package spring;

import java.lang.management.ManagementFactory;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import math.Pi;

@Controller
public class PiController {

    private static final String PID =
        ManagementFactory.getRuntimeMXBean().getName();

    @RequestMapping(value = "/work", method = RequestMethod.GET)
    public @ResponseBody String work(HttpServletResponse response) {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        return "Pi is: " + Pi.calculate(100_000_000);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody String home(HttpServletResponse response) {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        return "Current PID: " + PID;
    }
}