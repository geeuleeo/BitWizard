package com.wizard.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wizard.DTO.TagDTO;
import com.wizard.entities.Tag;
import com.wizard.entities.Utente;
import com.wizard.entities.UtenteTag;
import com.wizard.repos.ImmagineDAO;
import com.wizard.repos.TagDAO;
import com.wizard.repos.UtenteDAO;
import com.wizard.repos.UtenteDTO;
import com.wizard.repos.UtenteTagDAO;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
public class UtenteServiceImpl implements UtenteService {
	
	@Autowired
    private UtenteDAO dao;
	
	@Autowired
    private UtenteTagDAO utenteTagDAO;
	
	@Autowired
	private TagDAO tagDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private UtenteTagDAO utenteTagRepository;
    
    @Autowired
	private ImmagineDAO immagineDAO;
    
    public Utente salvaUtente(Utente utente, List<TagDTO> tagDTOs) {
        // Salva l'immagine se presente
        if (utente.getImmagine() != null) {
            immagineDAO.save(utente.getImmagine());
        }

        // Salva l'utente
        Utente utenteSalvato = dao.save(utente);

        // Gestisci i tag
        if (tagDTOs != null && !tagDTOs.isEmpty()) {
            for (TagDTO tagDTO : tagDTOs) {
                Tag tag = tagDAO.findById(tagDTO.getTagId())
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setTipoTag(tagDTO.getTipoTag());
                        return tagDAO.save(newTag);
                    });

                UtenteTag utenteTag = new UtenteTag();
                utenteTag.setUtente(utenteSalvato);
                utenteTag.setTag(tag);
                utenteTagRepository.save(utenteTag);

                utenteSalvato.getUtenteTags().add(utenteTag);
            }
        }

        return utenteSalvato;
    }
    
    @Transactional
    public UtenteDTO getUtenteDTO(Long utenteId) {
        Utente utente = dao.findByUtenteId(utenteId)
            .orElseThrow(() -> new EntityNotFoundException("Utente not found with id " + utenteId));

        //collection
        utente.getUtenteTags().size();

        // Map to DTO
        UtenteDTO utenteDTO = new UtenteDTO();
        utenteDTO.setDataNascita(utente.getDataNascita());
        utenteDTO.setNumeroTelefono(utente.getNumeroTelefono());
        utenteDTO.setUtenteId(utente.getUtenteId());
        utenteDTO.setNome(utente.getNome());
        utenteDTO.setCognome(utente.getCognome());
        utenteDTO.setEmail(utente.getEmail());
        utenteDTO.setDescrizione(utente.getDescrizione());
        utenteDTO.setImgProfilo(utente.getImmagine().getImg());

        List<TagDTO> tagDTOs = utente.getUtenteTags().stream()
            .map(utenteTag -> new TagDTO(
                utenteTag.getTag().getTagId(),
                utenteTag.getTag().getTipoTag()
            ))
            .collect(Collectors.toList());
        utenteDTO.setTags(tagDTOs);

        return utenteDTO;
    }
    
    @Override
    @Transactional
    public UtenteDTO modificaUtente(Long utenteId, UtenteDTO utenteDTO, List<TagDTO> tagDTOs) {
        Utente utente = dao.findById(utenteId)
            .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con id " + utenteId));

        // Check if the email is being changed
        if (!utente.getEmail().equals(utenteDTO.getEmail())) {
            // Check if the new email is already in use
            if (dao.existsByEmail(utenteDTO.getEmail())) {
                throw new IllegalArgumentException("Email già in uso");
            }
            utente.setEmail(utenteDTO.getEmail());
        }

        // Update other fields
        utente.setNome(utenteDTO.getNome());
        utente.setCognome(utenteDTO.getCognome());
        utente.setNumeroTelefono(utenteDTO.getNumeroTelefono());
        utente.setDataNascita(utenteDTO.getDataNascita());
        utente.setDescrizione(utenteDTO.getDescrizione());

        // Update the user's tags
        aggiornaUtenteTags(utente, tagDTOs);

        // Save the updated user
        dao.save(utente);

        // Map to DTO and return
        return toDTO(utente);
    }
    
    private void aggiornaUtenteTags(Utente utente, List<TagDTO> nuoviTagDTOs) {
        Set<UtenteTag> utenteTagsEsistenti = utente.getUtenteTags();

        // Identify tags to remove
        Set<UtenteTag> tagsToRemove = utenteTagsEsistenti.stream()
            .filter(vt -> nuoviTagDTOs.stream()
                .noneMatch(tagDTO -> tagDTO.getTagId().equals(vt.getTag().getTagId())))
            .collect(Collectors.toSet());

        utenteTagsEsistenti.removeAll(tagsToRemove);

        // Identify tags to add
        Set<Long> existingTagIds = utenteTagsEsistenti.stream()
            .map(vt -> vt.getTag().getTagId())
            .collect(Collectors.toSet());

        for (TagDTO tagDTO : nuoviTagDTOs) {
            if (!existingTagIds.contains(tagDTO.getTagId())) {
                Tag tag = tagDAO.findById(tagDTO.getTagId())
                    .orElseThrow(() -> new IllegalArgumentException("Tag non trovato"));
                UtenteTag nuovoUtenteTag = new UtenteTag(utente, tag);
                utenteTagsEsistenti.add(nuovoUtenteTag);
            }
        }
    }
    
    private UtenteDTO toDTO(Utente utente) {
        UtenteDTO utenteDTO = new UtenteDTO();

        utenteDTO.setUtenteId(utente.getUtenteId());
        utenteDTO.setNome(utente.getNome());
        utenteDTO.setCognome(utente.getCognome());
        utenteDTO.setEmail(utente.getEmail());
        utenteDTO.setNumeroTelefono(utente.getNumeroTelefono());
        utenteDTO.setDescrizione(utente.getDescrizione());
        utenteDTO.setDataNascita(utente.getDataNascita());

        List<TagDTO> tagDTOs = new ArrayList<>();
        if (utente.getUtenteTags() != null && !utente.getUtenteTags().isEmpty()) {
            for (UtenteTag utenteTag : utente.getUtenteTags()) {
                Tag tag = utenteTag.getTag();
                if (tag != null) {
                    TagDTO tagDTO = new TagDTO();
                    tagDTO.setTagId(tag.getTagId());
                    tagDTO.setTipoTag(tag.getTipoTag());
                    tagDTOs.add(tagDTO);
                }
            }
        }
        utenteDTO.setTags(tagDTOs);

        return utenteDTO;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utente utente = dao.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));
        
        return new User(utente.getEmail(), utente.getPassword(), new ArrayList<>());
    }
	
	@Override
    public boolean existByEmail(String email) {
        return dao.findByEmail(email).isPresent();
    }

    @Override
    public List<Utente> getAllUtenti() {
        return dao.findAll();
    }
    
    @Transactional
    @Override
    public Utente getUtente(HttpSession session) {
        // Recupera l'oggetto Utente dalla sessione
        Utente utente = (Utente) session.getAttribute("utenteLoggato");
        
        if (utente == null) {
            throw new IllegalStateException("Nessun utente loggato.");
        }

        return utente;
    }
    
    public void aggiungiTagAUtente(Utente utente, List<Tag> tags) {
        for (Tag tag : tags) {
            UtenteTag utenteTag = new UtenteTag();
            utenteTag.setUtente(utente);
            utenteTag.setTag(tag);

            utente.getUtenteTags().add(utenteTag);
            utenteTagRepository.save(utenteTag);
        }
    }
    
    public void rimuoviTagDaUtente(Utente utente, Tag tag) {
        UtenteTag utenteTag = utenteTagRepository.findByUtenteAndTag(utente, tag);
        if (utenteTag != null) {
            utente.getUtenteTags().remove(utenteTag);
            utenteTagRepository.delete(utenteTag);
        }
    }

	@Override
	public Utente findById(Long utente_id2) {
		Optional<Utente> utenteOptional = dao.findById(utente_id2);
		
		Utente utente = utenteOptional.get();
		
		return utente;
	}

}