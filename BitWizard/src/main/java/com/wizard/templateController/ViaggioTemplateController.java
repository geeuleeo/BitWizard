package com.wizard.templateController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller


@RequestMapping("viaggio")
public class ViaggioTemplateController {

   @GetMapping("/crea")
    public String crea() {


       return "Crea";
   }



}
