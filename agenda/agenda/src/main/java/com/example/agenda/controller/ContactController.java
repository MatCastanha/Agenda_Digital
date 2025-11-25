package com.example.agenda.controller;

import com.example.agenda.dto.ContactRequest;
import com.example.agenda.dto.ContactResponse;
import com.example.agenda.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
@Tag(name = "Contatos", description = "Gerenciamento completo da agenda de contatos")
public class ContactController {

    private final ContactService service;

    public ContactController(ContactService contactService) {
        this.service = contactService;
    }

    // CREATE
    @Operation(summary = "Cria um novo contato na agenda")
    @ApiResponse(responseCode = "200", description = "Contato criado com sucesso")
    @PostMapping
    public ResponseEntity<ContactResponse> create(@RequestBody ContactRequest request) {
        ContactResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }

    // READ ALL
    @Operation(summary = "Lista todos os contatos existentes")
    @ApiResponse(responseCode = "200", description = "Lista de contatos retornada")
    @GetMapping
    public ResponseEntity<List<ContactResponse>> findAll() {
        List<ContactResponse> contacts = service.findAll();
        return ResponseEntity.ok(contacts);
    }

    // READ BY ID
    @Operation(summary = "Busca um contato pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Contato encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Contato não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> findById(@PathVariable Long id) {
        ContactResponse contact = service.findById(id);
        return ResponseEntity.ok(contact);
    }

    // READ BY NAME
    @Operation(summary = "Busca contatos que contenham a string no nome")
    @ApiResponse(responseCode = "200", description = "Lista de contatos ou lista vazia")
    @ApiResponse(responseCode = "404", description = "Nenhum contato encontrado com o nome fornecido")
    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<ContactResponse>> findByName(@PathVariable String name) {
        List<ContactResponse> contacts = service.findByName(name);
        return ResponseEntity.ok(contacts);
    }

    // READ BY PHONE
    @Operation(summary = "Busca um contato pelo número de telefone exato")
    @ApiResponse(responseCode = "200", description = "Contato encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum contato encontrado com este telefone")
    @GetMapping("/search/phone/{phone}")
    public ResponseEntity<ContactResponse> findByPhone(@PathVariable String phone) {
        ContactResponse contact = service.findByPhone(phone);
        return ResponseEntity.ok(contact);
    }

    // UPDATE
    @Operation(summary = "Atualiza um contato existente pelo ID")
    @ApiResponse(responseCode = "200", description = "Contato atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "ID de contato não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<ContactResponse> update(@PathVariable Long id, @RequestBody ContactRequest request) {
        ContactResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    // DELETE
    @Operation(summary = "Exclui um contato pelo ID")
    @ApiResponse(responseCode = "200", description = "Contato excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "ID de contato não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}