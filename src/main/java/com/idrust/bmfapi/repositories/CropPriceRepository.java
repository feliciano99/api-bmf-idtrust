package com.idrust.bmfapi.repositories;

import java.util.Optional;

import com.idrust.bmfapi.entities.CropPrice;

public interface CropPriceRepository extends BmfPriceRepository<CropPrice> {

    Optional<CropPrice> findByCodeAndDate(String code, String date);

}
