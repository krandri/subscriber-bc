package fr.canal.subscriberbc.subscriber.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.canal.subscriberbc.subscriber.dto.SubscriberDTO;
import fr.canal.subscriberbc.subscriber.exception.ExistingSubscriberException;
import fr.canal.subscriberbc.subscriber.exception.SubscriberNotFoundException;
import fr.canal.subscriberbc.subscriber.service.SubscriberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SubscriberController.class)
@AutoConfigureMockMvc
class SubscriberControllerTest {

    @MockBean
    private SubscriberService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testCreate() throws Exception, ExistingSubscriberException {
        SubscriberDTO dto = SubscriberDTO.builder()
                .subscriberId(1)
                .fname("Kevin")
                .lname("Randri")
                .mail("test@test.com")
                .phone("0606060606")
                .isActiv(true).build();

        when(service.create(any())).thenReturn(dto);

        mockMvc.perform(post("/subscribers")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fname").value("Kevin"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testSearch() throws Exception {

        SubscriberDTO dto = SubscriberDTO.builder()
                .subscriberId(1)
                .fname("Kevin")
                .lname("Randri")
                .mail("test@test.com")
                .phone("0606060606")
                .isActiv(true).build();

        SubscriberDTO dto2 = SubscriberDTO.builder()
                .subscriberId(2)
                .fname("Joe")
                .lname("Randri")
                .mail("test@test.com")
                .phone("0606060606")
                .isActiv(true).build();

        when(service.searchSubscribers(any())).thenReturn(List.of(dto, dto2));

        mockMvc.perform(get("/subscribers")
                        .param("search", "lname:Randri")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lname").value("Randri"))
                .andExpect(jsonPath("$[1].lname").value("Randri"));

    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testUpdateSubscriber() throws Exception, SubscriberNotFoundException {
        String id = "123";
        SubscriberDTO dto = SubscriberDTO.builder()
                .subscriberId(1)
                .fname("Kevin")
                .lname("Randri")
                .mail("test@test.com")
                .phone("0606060606")
                .isActiv(true).build();

        when(service.update(any(), any())).thenReturn(dto);

        mockMvc.perform(put("/subscribers/{id}/update", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fname").value("Kevin"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testCancelSubscriber() throws Exception, SubscriberNotFoundException {
        String id = "123";

        SubscriberDTO dto = SubscriberDTO.builder()
                .subscriberId(1)
                .fname("Kevin")
                .lname("Randri")
                .mail("test@test.com")
                .phone("0606060606")
                .isActiv(false).build();


        when(service.cancel(any())).thenReturn(dto);

        mockMvc.perform(put("/subscribers/{id}/cancel", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)).with(csrf()))
                .andExpect(status().isCreated());
    }
}