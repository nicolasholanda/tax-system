package com.taxsystem.service;

import com.taxsystem.entity.State;
import com.taxsystem.repository.StateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateService {

    private final StateRepository stateRepository;

    public StateService(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    public List<State> findAll() {
        return stateRepository.findAll();
    }

    public State findById(Long id) {
        return stateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("State not found with id: " + id));
    }

    public State create(State state) {
        return stateRepository.save(state);
    }

    public State update(Long id, State state) {
        State existing = findById(id);
        existing.setName(state.getName());
        existing.setAbbreviation(state.getAbbreviation());
        return stateRepository.save(existing);
    }

    public void delete(Long id) {
        State existing = findById(id);
        stateRepository.delete(existing);
    }
}
