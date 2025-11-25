package com.example.agenda.service;

import com.example.agenda.dto.ContactRequest;
import com.example.agenda.dto.ContactResponse;
import com.example.agenda.exception.ResourceNotFoundException; // Importe a nova exceção
import com.example.agenda.model.Contact;
import com.example.agenda.repository.ContactRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService {

    private final ContactRepository repository;

    // Injeção de dependência via construtor
    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    /**
     * Cria um novo contato.
     * Captura o retorno de repository.save() para garantir que o ID seja mapeado.
     */
    public ContactResponse create(ContactRequest request) {
        Contact contact = new Contact();
        contact.setName(request.getName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setNotes(request.getNotes());
        contact.setCreatedAt(LocalDateTime.now());

        // CORREÇÃO ESSENCIAL: Captura o objeto retornado pelo save, que contém o ID gerado pelo DB.
        Contact savedContact = repository.save(contact);

        return ContactResponse.fromEntity(savedContact);
    }

    /**
     * Retorna todos os contatos.
     */
    public List<ContactResponse> findAll() {
        return repository.findAll().stream()
                .map(ContactResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Busca um contato por ID. Lança 404 se não encontrado.
     */
    public ContactResponse findById(Long id) {
        Contact contact = repository.findById(id)
                // Lança 404 Not Found (via RestExceptionHandler) se não encontrado
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado"));

        return ContactResponse.fromEntity(contact);
    }

    /**
     * Busca contatos por parte do nome. Lança 404 se não encontrado.
     */
    public List<ContactResponse> findByName(String name) {
        List<Contact> contacts = repository.findByNameContainingIgnoreCase(name);

        if (contacts.isEmpty()) {
            throw new ResourceNotFoundException("Contato com nome \"" + name + " \"não encontrado");
        }

        return contacts.stream()
                .map(ContactResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Busca um contato por telefone. Lança 404 se não encontrado.
     */
    public ContactResponse findByPhone(String phone) {
        Contact contact = repository.findByPhone(phone)
                // Lança 404 Not Found se não encontrado
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum contato encontardo emo este telefone"));

        return ContactResponse.fromEntity(contact);
    }

    /**
     * Atualiza um contato existente. Lança 404 se o contato não existir.
     */
    public ContactResponse update(Long id, ContactRequest request) {
        Contact contact = repository.findById(id)
                // Lança 404 Not Found se não encontrado
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado"));

        contact.setName(request.getName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setNotes(request.getNotes());

        Contact updatedContact = repository.save(contact);

        return ContactResponse.fromEntity(updatedContact);
    }

    /**
     * Deleta um contato por ID. Lança 404 se o contato não existir.
     */
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            // Lança 404 Not Found se o ID não for encontrado
            throw new ResourceNotFoundException("Contato não existe");
        }
        repository.deleteById(id);
    }
}