package com.exemple.oussama.service.impl;

import com.exemple.oussama.builder.AppBuilder;
import com.exemple.oussama.exception.AppNotFoundException;
import com.exemple.oussama.model.domain.App;
import com.exemple.oussama.model.dto.AppDTO;
import com.exemple.oussama.repository.AppRepository;
import com.exemple.oussama.service.AppService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class AppServiceImpl implements AppService {

    private final AppRepository repository;
    private final AppBuilder builder;

    @Override
    public Long createNewApp(AppDTO dto) {
        return Stream.of(dto)
            .map(builder::build)
            .map(repository::save)
            .map(App::getId)
            .findFirst()
            .get();
    }

    @Override
    public List<Optional<AppDTO>> getAllApps() {
        return repository.findAll()
            .stream()
            .map(builder::build)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<AppDTO> getAppById(Long id) {
        return repository.findById(id)
            .map(builder::build)
            .orElseThrow(() -> new AppNotFoundException(String.format("No such App for id '%s'", id)));
    }

    @Transactional
    @Override
    public Optional<AppDTO> updateApp(Long id, AppDTO dto) {
        return repository.findById(id)
            .map(model -> builder.build(dto, model))
            .map(repository::save)
            .map(builder::build)
            .orElseThrow(() -> new AppNotFoundException(String.format("No such App for id '%s'", id)));
    }

    @Override
    public void deleteAppById(Long id) {
        repository.deleteById(id);
    }
}
