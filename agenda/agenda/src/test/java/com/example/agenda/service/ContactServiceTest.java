package com.example.agenda.service;

import com.example.agenda.dto.ContactRequest;
import com.example.agenda.dto.ContactResponse;
import com.example.agenda.model.Contact;
import com.example.agenda.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {

    // Injeta os mocks no serviço
    @InjectMocks
    private ContactService contactService;

    // Mock do repositório, dependência externa
    @Mock
    private ContactRepository contactRepository;

    private Contact contact;
    private ContactRequest request;

    // Constante para garantir consistência nas datas
    private final LocalDateTime MOCK_CREATED_AT = LocalDateTime.now().withNano(0);

    @BeforeEach
    void setUp() {
        // Objeto Contact MOCKADO que o repositório DEVE retornar
        contact = new Contact();
        contact.setId(1L); // ESSENCIAL: ID preenchido para a asserção
        contact.setName("João Silva");
        contact.setEmail("joao.silva@example.com");
        contact.setPhone("123456789");
        contact.setNotes("Nota de teste");
        contact.setCreatedAt(MOCK_CREATED_AT);

        // Objeto ContactRequest (Payload de entrada)
        request = new ContactRequest();
        request.setName("João Silva");
        request.setEmail("joao.silva@example.com");
        request.setPhone("123456789");
        request.setNotes("Nota de teste");
    }

    // --------------------------------------------------------------------------------


    @Test
    void create_ShouldSaveContactAndReturnResponse_Success() {
        // --- NOVO: Usamos doAnswer para simular o DB gerando o ID ---
        doAnswer(invocation -> {
            Contact contactArg = invocation.getArgument(0); // Pega o objeto passado ao save
            contactArg.setId(1L); // Simula o banco de dados gerando e setando o ID
            contactArg.setCreatedAt(MOCK_CREATED_AT); // Garante que a data está setada
            return contactArg; // Retorna o objeto (agora com ID)
        }).when(contactRepository).save(any(Contact.class));
        // -----------------------------------------------------------

        ContactResponse response = contactService.create(request);

        // Asserções
        assertNotNull(response);
        // O ID agora deve ser 1, pois foi injetado diretamente no objeto que o serviço está usando.
        assertEquals(1L, response.getId());
        assertEquals(request.getName(), response.getName());

        verify(contactRepository, times(1)).save(any(Contact.class));
    }

    // --------------------------------------------------------------------------------


    @Test
    void findAll_ShouldReturnListOfContactResponses_Success() {
        List<Contact> contactList = Arrays.asList(contact);
        when(contactRepository.findAll()).thenReturn(contactList);

        List<ContactResponse> responseList = contactService.findAll();

        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.size());
        verify(contactRepository, times(1)).findAll();
    }

    // Teste de Busca por ID (Sucesso)
    @Test
    void findById_ShouldReturnContactResponse_Success() {
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        ContactResponse response = contactService.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(contactRepository, times(1)).findById(1L);
    }

    // Teste de Busca por ID (Falha)
    @Test
    void findById_ShouldThrowException_NotFound() {
        when(contactRepository.findById(99L)).thenReturn(Optional.empty());

        // Espera-se que lance a RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            contactService.findById(99L);
        });

        assertEquals("Contato não encontrado", exception.getMessage());
        verify(contactRepository, times(1)).findById(99L);
    }

    // Teste de Busca por Nome (Sucesso)
    @Test
    void findByName_ShouldReturnListOfContactResponses_Success() {
        List<Contact> foundContacts = Arrays.asList(contact);
        when(contactRepository.findByNameContainingIgnoreCase("joão")).thenReturn(foundContacts);

        List<ContactResponse> responseList = contactService.findByName("joão");

        assertFalse(responseList.isEmpty());
    }

    // Teste de Busca por Nome (Falha)
    @Test
    void findByName_ShouldThrowException_NotFound() {
        when(contactRepository.findByNameContainingIgnoreCase("NãoExiste")).thenReturn(List.of());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            contactService.findByName("NãoExiste");
        });

        assertTrue(exception.getMessage().contains("não encontrado"));
    }

    // Teste de Busca por Telefone (Sucesso)
    @Test
    void findByPhone_ShouldReturnContactResponse_Success() {
        when(contactRepository.findByPhone("123456789")).thenReturn(Optional.of(contact));

        ContactResponse response = contactService.findByPhone("123456789");

        assertNotNull(response);
        assertEquals("123456789", response.getPhone());
    }

    // Teste de Busca por Telefone (Falha)
    @Test
    void findByPhone_ShouldThrowException_NotFound() {
        when(contactRepository.findByPhone("000000000")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            contactService.findByPhone("000000000");
        });

        assertEquals("Nenhum contato encontardo emo este telefone", exception.getMessage());
    }

    // --------------------------------------------------------------------------------

    @Test
    void update_ShouldUpdateContactAndReturnResponse_Success() {
        ContactRequest updateRequest = new ContactRequest();
        updateRequest.setName("João Atualizado");
        updateRequest.setEmail("novo@email.com");
        // ... (preenche outros campos)

        // 1. Simula que o contato foi encontrado
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        // 2. Simula o save
        when(contactRepository.save(any(Contact.class))).thenReturn(contact);

        ContactResponse response = contactService.update(1L, updateRequest);

        // Verifica se a entidade *mockada* foi atualizada antes de ser salva
        assertEquals("João Atualizado", contact.getName());

        // Asserções
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("João Atualizado", response.getName());

        verify(contactRepository, times(1)).findById(1L);
        verify(contactRepository, times(1)).save(contact);
    }

    @Test
    void update_ShouldThrowException_NotFound() {
        when(contactRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            contactService.update(99L, request);
        });

        verify(contactRepository, never()).save(any(Contact.class));
    }

    // --------------------------------------------------------------------------------

    @Test
    void delete_ShouldCallDeleteById_Success() {
        // Simula que o contato existe
        when(contactRepository.existsById(1L)).thenReturn(true);
        doNothing().when(contactRepository).deleteById(1L);

        // Asserção: não deve lançar exceção
        assertDoesNotThrow(() -> contactService.delete(1L));

        verify(contactRepository, times(1)).existsById(1L);
        verify(contactRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_NotFound() {
        // Simula que o contato não existe
        when(contactRepository.existsById(99L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            contactService.delete(99L);
        });

        assertEquals("Contato não existe", exception.getMessage());
        verify(contactRepository, times(1)).existsById(99L);
        verify(contactRepository, never()).deleteById(anyLong());
    }
}