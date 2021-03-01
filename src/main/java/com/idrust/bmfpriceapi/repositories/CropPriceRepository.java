package com.idrust.bmfpriceapi.repositories;

import com.idrust.bmfpriceapi.entities.CropPrice;

import java.util.Optional;

public interface CropPriceRepository extends BmfPriceRepository<CropPrice> {

    Optional<CropPrice> findByCodeAndDate(String code, String date);

}
