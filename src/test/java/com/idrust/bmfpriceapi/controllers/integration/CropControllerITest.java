package com.idrust.bmfpriceapi.controllers.integration;

import com.idrust.bmfpriceapi.controllers.AbstractControllerTest;
import com.idrust.bmfpriceapi.dtos.BaseResponse;
import com.idrust.bmfpriceapi.dtos.CropPriceDTO;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;

import static com.idrust.bmfpriceapi.util.JsonUtil.asJsonString;

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
