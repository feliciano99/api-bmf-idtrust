package com.idrust.bmfpriceapi.repositories;

import com.idrust.bmfpriceapi.entities.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BmfPriceRepository<E extends AbstractEntity> extends JpaRepository<E, Long> {
}
