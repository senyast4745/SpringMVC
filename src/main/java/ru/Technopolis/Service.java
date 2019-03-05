package ru.Technopolis;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Service {

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
