package com.idrust.bmfpriceapi.repositories;

import com.idrust.bmfpriceapi.entities.CropPrice;
import com.idrust.bmfpriceapi.repositories.CropPriceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(value = "/load-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clean-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@TestPropertySource("classpath:application-test.properties")
public class CropPriceRepositoryITest {

    @Autowired
    private CropPriceRepository cropPriceRepository;

    @Test
    public void shouldHaveThreeCropPrices() {
        assertEquals(3, cropPriceRepository.count(), "Deveriam ter 3 CropPrices cadastrados no banco.");
    }

    @Test
    public void shouldReturnCropPriceByCropCodeAndDate() {
        final Optional<CropPrice> cropPrice = cropPriceRepository.findByCodeAndDate("SOYBEAN", "2021-02-18");

        assertNotNull(cropPrice);
        assertTrue(cropPrice.isPresent());
        assertEquals("SOYBEAN", cropPrice.get().getCode());
        assertEquals(BigDecimal.valueOf(180.12), cropPrice.get().getPrice());
        assertEquals("2021-02-18", cropPrice.get().getDate());
    }

}
