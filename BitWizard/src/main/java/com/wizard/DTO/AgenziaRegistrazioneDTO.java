package com.wizard.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AgenziaRegistrazioneDTO

{


    @NotBlank(message = "Partita IVA necessaria. ")
    private String partitaIVA;

    @NotBlank(message = "Nome obbligatorio.")
    private String nome;

    @NotBlank(message = "Password obbligatoria.")
    private String password;


    @Size(min = 2,max = 500,message = "La descrizione deve avere un minimo di 2 ed un massimo di 500 caratteri ")
    private String descrizione;

    public @Size(min = 2, max = 500, message = "La descrizione deve avere un minimo di 2 ed un massimo di 500 caratteri ") String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(@Size(min = 2, max = 500, message = "La descrizione deve avere un minimo di 2 ed un massimo di 500 caratteri ") String descrizione) {
        this.descrizione = descrizione;
    }

    public @NotBlank(message = "Partita IVA necessaria. ") String getPartitaIVA() {
        return partitaIVA;
    }

    public void setPartitaIVA(@NotBlank(message = "Partita IVA necessaria. ") String partitaIVA) {
        this.partitaIVA = partitaIVA;
    }

    public @NotBlank(message = "Nome obbligatorio.") String getNome() {
        return nome;
    }

    public void setNome(@NotBlank(message = "Nome obbligatorio.") String nome) {
        this.nome = nome;
    }

    public @NotBlank(message = "Password obbligatoria.") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password obbligatoria.") String password) {
        this.password = password;
    }
}
