package com.ilinskii.profileapp.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ExitController {

    @GetMapping("/exit")
    public RedirectView exit() {
        return new RedirectView("exit-success.html");
    }
}
