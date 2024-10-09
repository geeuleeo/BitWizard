package com.wizard.templateController;

import com.wizard.entities.Viaggio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller


@RequestMapping("viaggio")
public class ViaggioTemplateController {

   @GetMapping("/crea")
    public String crea(Model model) {

       Viaggio viaggio = new Viaggio();

       model.addAttribute("viaggio", viaggio);

       return "CreaViaggio";
   }



}
