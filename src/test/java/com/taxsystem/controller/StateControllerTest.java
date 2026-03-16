package com.taxsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxsystem.entity.State;
import com.taxsystem.service.StateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StateController.class)
class StateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StateService stateService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void findAll_returnsStateList() throws Exception {
        State state = new State("São Paulo", "SP");
        state.setId(1L);
        when(stateService.findAll()).thenReturn(List.of(state));

        mockMvc.perform(get("/api/states"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].abbreviation").value("SP"));
    }

    @Test
    void findById_returnsState() throws Exception {
        State state = new State("São Paulo", "SP");
        state.setId(1L);
        when(stateService.findById(1L)).thenReturn(state);

        mockMvc.perform(get("/api/states/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("São Paulo"));
    }

    @Test
    void create_validState_returnsCreated() throws Exception {
        State state = new State("São Paulo", "SP");
        state.setId(1L);
        when(stateService.create(any(State.class))).thenReturn(state);

        mockMvc.perform(post("/api/states")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new State("São Paulo", "SP"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_invalidState_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/states")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"abbreviation\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_validState_returnsUpdated() throws Exception {
        State state = new State("Rio de Janeiro", "RJ");
        state.setId(1L);
        when(stateService.update(eq(1L), any(State.class))).thenReturn(state);

        mockMvc.perform(put("/api/states/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new State("Rio de Janeiro", "RJ"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.abbreviation").value("RJ"));
    }

    @Test
    void delete_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/states/1"))
                .andExpect(status().isNoContent());
    }
}
