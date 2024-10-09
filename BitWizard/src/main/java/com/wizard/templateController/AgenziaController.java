package com.wizard.templateController;

import com.wizard.services.AgenziaService;
import com.wizard.services.AgenziaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AgenziaController {

    @Autowired
    AgenziaService agenziaService = new AgenziaServiceImpl();

    @GetMapping("agenzie/show")
    public String showAgenzie(Model model) {

        model.addAttribute("agenzie", agenziaService.getAgenzie());


        return "agenzieList";
    }


}
