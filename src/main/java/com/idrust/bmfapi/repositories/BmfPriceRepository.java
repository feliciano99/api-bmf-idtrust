package com.idrust.bmfapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.idrust.bmfapi.entities.AbstractEntity;

@NoRepositoryBean
public interface BmfPriceRepository<E extends AbstractEntity> extends JpaRepository<E, Long> {
}
