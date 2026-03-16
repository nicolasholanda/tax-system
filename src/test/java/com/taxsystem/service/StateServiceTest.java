package com.taxsystem.service;

import com.taxsystem.entity.State;
import com.taxsystem.repository.StateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StateServiceTest {

    @Mock
    private StateRepository stateRepository;

    @InjectMocks
    private StateService stateService;

    private State state;

    @BeforeEach
    void setUp() {
        state = new State("São Paulo", "SP");
        state.setId(1L);
    }

    @Test
    void findAll_returnsAllStates() {
        when(stateRepository.findAll()).thenReturn(List.of(state));

        List<State> result = stateService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getAbbreviation()).isEqualTo("SP");
    }

    @Test
    void findById_existingId_returnsState() {
        when(stateRepository.findById(1L)).thenReturn(Optional.of(state));

        State result = stateService.findById(1L);

        assertThat(result.getName()).isEqualTo("São Paulo");
    }

    @Test
    void findById_nonExistingId_throwsException() {
        when(stateRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> stateService.findById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("State not found");
    }

    @Test
    void create_savesAndReturnsState() {
        when(stateRepository.save(any(State.class))).thenReturn(state);

        State result = stateService.create(new State("São Paulo", "SP"));

        assertThat(result.getId()).isEqualTo(1L);
        verify(stateRepository).save(any(State.class));
    }

    @Test
    void update_existingId_updatesAndReturnsState() {
        when(stateRepository.findById(1L)).thenReturn(Optional.of(state));
        when(stateRepository.save(any(State.class))).thenReturn(state);

        State updated = new State("Rio de Janeiro", "RJ");
        State result = stateService.update(1L, updated);

        assertThat(result).isNotNull();
        verify(stateRepository).save(any(State.class));
    }

    @Test
    void delete_existingId_deletesState() {
        when(stateRepository.findById(1L)).thenReturn(Optional.of(state));

        stateService.delete(1L);

        verify(stateRepository).delete(state);
    }
}
