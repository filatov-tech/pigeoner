package tech.filatov.pigeoner.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String root() {
        return "page32636306";
    }

    @GetMapping("/pigeons")
    public String getPigeons() {
        return "page32658356";
    }

    @GetMapping("/dovecote")
    public String getDovecotes() {
        return "page36229100";
    }


}
