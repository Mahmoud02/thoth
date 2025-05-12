package com.mahmoud.thoth.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mahmoud.thoth.domain.port.out.NameSpaceListViewDto;
import com.mahmoud.thoth.domain.port.out.NameSpaceViewDto;
import com.mahmoud.thoth.domain.port.out.NamespaceQueryRepository;

import lombok.RequiredArgsConstructor;

@Service@RequiredArgsConstructor
public class NamespaceQueryService {

    private final NamespaceQueryRepository namespaceQueryRepository;

    public NameSpaceViewDto findById(Long namespaceId) {
        return this.namespaceQueryRepository.findById(namespaceId);
    }

    public List<NameSpaceListViewDto> findAll() {
        return this.namespaceQueryRepository.findAll();
    }

}
