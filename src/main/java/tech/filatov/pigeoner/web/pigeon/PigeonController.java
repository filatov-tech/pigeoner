package tech.filatov.pigeoner.web.pigeon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tech.filatov.pigeoner.service.PigeonService;

import java.util.Map;

import static tech.filatov.pigeoner.util.ValidationUtil.*;

@Controller
@RequestMapping("/pigeons")
public class PigeonController {

    private final PigeonService service;

    public PigeonController(PigeonService service) {
        this.service = service;
    }

    @GetMapping
    public String pigeons(@RequestParam(required = false) Map<String, String> filterParameters, ModelMap model) {
        if (!filterParameters.isEmpty()) {
            validateDataFromFilter(filterParameters);
        }
        model.addAttribute("pigeons", service.getAll(filterParameters));
        return "page32658356";
    }
}
