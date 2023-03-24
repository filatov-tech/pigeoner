package tech.filatov.pigeoner.web.pigeon;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pigeons")
public class PigeonController {

    @GetMapping
    public String pigeons() {
        return "page32658356";
    }
}
