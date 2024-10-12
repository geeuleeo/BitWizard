package com.wizard.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TagDTO {
	
    private Long tagId;
    
    @NotBlank(message = "Il tipo di tag è obbligatorio")
    @Size(max = 50, message = "Il tipo di tag non può superare i 50 caratteri")
    private String tipoTag;

    // Costruttori
    public TagDTO() {}

    public TagDTO(Long tagId, String tipoTag) {
        this.tagId = tagId;
        this.tipoTag = tipoTag;
    }

    // Getters e setters
    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTipoTag() {
        return tipoTag;
    }

    public void setTipoTag(String tipoTag) {
        this.tipoTag = tipoTag;
    }

}
