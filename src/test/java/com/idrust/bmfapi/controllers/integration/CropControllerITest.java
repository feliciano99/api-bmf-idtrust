package com.idrust.bmfapi.controllers.integration;

import com.idrust.bmfapi.controllers.AbstractControllerTest;
import com.idrust.bmfapi.dtos.BaseResponse;
import com.idrust.bmfapi.dtos.CropPriceDTO;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.idrust.bmfapi.util.JsonUtil.asJsonString;

import java.math.BigDecimal;

@AutoConfigureMockMvc
@Sql(value = "/load-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clean-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CropControllerITest extends AbstractControllerTest {

    @Test
    public void shouldReturnPersistedCropPricesCorrectly() throws Exception {
        final String expectedResponse = asJsonString(BaseResponse.ok(new CropPriceDTO(BigDecimal.valueOf(180.12))));
        final String getAuditsResponse = doGetExpectStatus("/crops/SOYBEAN/price", HttpStatus.OK, getParamsFor("2021-02-18"));

        JSONAssert.assertEquals(expectedResponse, getAuditsResponse, false);
    }

    private MultiValueMap<String, String> getParamsFor(String cropDate) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("date", cropDate);

        return params;
    }

}
