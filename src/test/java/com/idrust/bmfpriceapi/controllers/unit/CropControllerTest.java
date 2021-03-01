package com.idrust.bmfpriceapi.controllers.unit;

import com.idrust.bmfpriceapi.controllers.AbstractControllerTest;
import com.idrust.bmfpriceapi.controllers.CropController;
import com.idrust.bmfpriceapi.dtos.BaseResponse;
import com.idrust.bmfpriceapi.dtos.CropPriceDTO;
import com.idrust.bmfpriceapi.entities.CropPrice;
import com.idrust.bmfpriceapi.exceptions.CropPriceCalculationException;
import com.idrust.bmfpriceapi.exceptions.EconomiaAPIException;
import com.idrust.bmfpriceapi.exceptions.QuandlAPIException;
import com.idrust.bmfpriceapi.exceptions.ServiceException;
import com.idrust.bmfpriceapi.services.CropService;
import com.idrust.bmfpriceapi.util.CropPriceBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.stream.Stream;

import static com.idrust.bmfpriceapi.util.JsonUtil.asJsonString;
import static org.mockito.Mockito.*;

@WebMvcTest(CropController.class)
public class CropControllerTest extends AbstractControllerTest {

    @MockBean
    private CropService cropService;

    private CropPrice buildDummyCropPrice() {
        return CropPriceBuilder
                .init()
                .code("SOYBEAN")
                .price(55.33d)
                .date("2021-02-18")
                .build();
    }

    @Test
    public void shouldFindCropPriceByCodeAndDateCorrectly() throws Exception {
        final CropPrice cropPrice = buildDummyCropPrice();
        final CropPriceDTO cropPriceDTO = new CropPriceDTO(cropPrice.getPrice());

        when(cropService.calculateCropPrice(cropPrice.getCode(), cropPrice.getDate())).thenReturn(cropPrice.getPrice());

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("date", cropPrice.getDate());

        final String expectedResponse = asJsonString(BaseResponse.ok(cropPriceDTO));
        final String getResponse = doGetExpectStatus(
                "/crops/" + cropPrice.getCode() + "/price",
                HttpStatus.OK,
                params
        );

        JSONAssert.assertEquals(expectedResponse, getResponse, false);

        verify(cropService, times(1)).calculateCropPrice(cropPrice.getCode(), cropPrice.getDate());
    }

    private static Stream<Arguments> provideCropPriceAPIExceptionArguments() {
        return Stream.of(
                Arguments.of(new RuntimeException("Erro interno, por favor contate o suporte."), HttpStatus.INTERNAL_SERVER_ERROR),
                Arguments.of(new ServiceException("Erro na camada de servicos"), HttpStatus.INTERNAL_SERVER_ERROR),
                Arguments.of(new CropPriceCalculationException("Erro ao calcular preco da cultura"), HttpStatus.INTERNAL_SERVER_ERROR),
                Arguments.of(new EconomiaAPIException("Erro no servico de acesso a API de Economia"), HttpStatus.BAD_GATEWAY),
                Arguments.of(new QuandlAPIException("Erro no servico de acesso a API Quandl"), HttpStatus.BAD_GATEWAY)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCropPriceAPIExceptionArguments")
    public void shouldReturnRrror500WhenThereIsSomeInternalError(Exception exception, HttpStatus httpStatus) throws Exception {
        final CropPrice cropPrice = buildDummyCropPrice();

        when(cropService.calculateCropPrice(cropPrice.getCode(), cropPrice.getDate())).thenThrow(exception);

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("date", cropPrice.getDate());

        final String expectedError = asJsonString(BaseResponse.error(httpStatus, exception.getMessage()));
        final String getResponse = doGetExpectStatus(
                "/crops/" + cropPrice.getCode() + "/price",
                httpStatus,
                params
        );

        JSONAssert.assertEquals(expectedError, getResponse, false);

        verify(cropService, times(1)).calculateCropPrice(cropPrice.getCode(), cropPrice.getDate());
    }

}
