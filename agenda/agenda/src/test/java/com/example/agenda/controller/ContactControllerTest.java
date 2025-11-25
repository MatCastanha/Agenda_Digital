package com.example.agenda.controller;

import com.example.agenda.dto.ContactRequest;
import com.example.agenda.dto.ContactResponse;
import com.example.agenda.exception.ResourceNotFoundException;
import com.example.agenda.service.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContactController.class)
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContactService contactService;

    private ContactRequest validRequest;
    private ContactResponse validResponse;
    private final String BASE_URL = "/contacts";

    @BeforeEach
    void setUp() {
        validRequest = new ContactRequest();
        validRequest.setName("João Controller");
        validRequest.setEmail("joao.c@example.com");
        validRequest.setPhone("111222333");
        validRequest.setNotes("Nota do controller");

        validResponse = new ContactResponse();
        validResponse.setId(1L);
        validResponse.setName("João Controller");
        validResponse.setPhone("111222333");
        validResponse.setCreatedAt(LocalDateTime.now().withNano(0));
    }

    // --------------------------------------------------------------------------------
    // --- 1. Testes de SUCESSO (Status 200 OK) ---

    @Test
    void create_ShouldReturnCreatedContactAndStatus200() throws Exception {
        when(contactService.create(any(ContactRequest.class))).thenReturn(validResponse);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(contactService, times(1)).create(any(ContactRequest.class));
    }

    @Test
    void findAll_ShouldReturnListOfContactsAndStatus200() throws Exception {
        List<ContactResponse> listResponse = Arrays.asList(validResponse, validResponse);
        when(contactService.findAll()).thenReturn(listResponse);

        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(contactService, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnContactAndStatus200() throws Exception {
        when(contactService.findById(1L)).thenReturn(validResponse);

        mockMvc.perform(get(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(contactService, times(1)).findById(1L);
    }

    @Test
    void findByName_ShouldReturnListOfContactsAndStatus200() throws Exception {
        List<ContactResponse> listResponse = Arrays.asList(validResponse);
        when(contactService.findByName("João")).thenReturn(listResponse);

        // URL CORRIGIDA
        mockMvc.perform(get(BASE_URL + "/search/name/João")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(contactService, times(1)).findByName("João");
    }

    @Test
    void findByPhone_ShouldReturnContactAndStatus200() throws Exception {
        when(contactService.findByPhone("111222333")).thenReturn(validResponse);

        // URL CORRIGIDA
        mockMvc.perform(get(BASE_URL + "/search/phone/111222333")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("111222333"));

        verify(contactService, times(1)).findByPhone("111222333");
    }

    @Test
    void update_ShouldReturnUpdatedContactAndStatus200() throws Exception {
        ContactResponse updatedResponse = new ContactResponse();
        updatedResponse.setId(1L);
        updatedResponse.setName("Novo Nome");
        updatedResponse.setPhone("111222333");

        ContactRequest updateRequest = new ContactRequest();
        updateRequest.setName("Novo Nome");

        when(contactService.update(eq(1L), any(ContactRequest.class))).thenReturn(updatedResponse);

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Novo Nome"));

        verify(contactService, times(1)).update(eq(1L), any(ContactRequest.class));
    }

    @Test
    void delete_ShouldReturnStatus200() throws Exception {
        doNothing().when(contactService).delete(1L);

        mockMvc.perform(delete(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(contactService, times(1)).delete(1L);
    }

    // --------------------------------------------------------------------------------
    // --- 2. Testes de FALHA (Status 404 Not Found) ---

    @Test
    void findById_ShouldReturnStatus404_WhenServiceThrowsResourceNotFound() throws Exception {
        when(contactService.findById(99L)).thenThrow(new ResourceNotFoundException("Contato não encontrado"));

        mockMvc.perform(get(BASE_URL + "/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(contactService, times(1)).findById(99L);
    }

    @Test
    void findByName_ShouldReturnStatus404_WhenServiceThrowsResourceNotFound() throws Exception {
        final String searchName = "Inexistente";

        // Novo Mock: Focamos apenas no TIPO da exceção, não na mensagem exata
        when(contactService.findByName(searchName)).thenThrow(ResourceNotFoundException.class);

        // URL CORRIGIDA (mantida)
        mockMvc.perform(get(BASE_URL + "/search/name/" + searchName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(contactService, times(1)).findByName(searchName);
    }

    @Test
    void findByPhone_ShouldReturnStatus404_WhenServiceThrowsResourceNotFound() throws Exception {
        final String searchPhone = "000000000";

        // Novo Mock: Focamos apenas no TIPO da exceção
        when(contactService.findByPhone(searchPhone)).thenThrow(ResourceNotFoundException.class);

        // URL CORRIGIDA (mantida)
        mockMvc.perform(get(BASE_URL + "/search/phone/" + searchPhone)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(contactService, times(1)).findByPhone(searchPhone);
    }

    @Test
    void update_ShouldReturnStatus404_WhenServiceThrowsResourceNotFound() throws Exception {
        final Long nonExistentId = 99L;

        when(contactService.update(eq(nonExistentId), any(ContactRequest.class))).thenThrow(
                new ResourceNotFoundException("Contato não encontrado")
        );

        mockMvc.perform(put(BASE_URL + "/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotFound());

        verify(contactService, times(1)).update(eq(nonExistentId), any(ContactRequest.class));
    }


    @Test
    void delete_ShouldReturnStatus404_WhenServiceThrowsResourceNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Contato não existe")).when(contactService).delete(99L);

        mockMvc.perform(delete(BASE_URL + "/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(contactService, times(1)).delete(99L);
    }
}