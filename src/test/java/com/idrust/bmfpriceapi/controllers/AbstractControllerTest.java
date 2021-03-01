package com.idrust.bmfpriceapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableSpringDataWebSupport
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    protected String doGetExpectStatus(final String url, final HttpStatus status) throws Exception {
        return doGetExpectStatus(url, status, null);
    }

    protected String doGetExpectStatus(final String url, final HttpStatus status,
                                       MultiValueMap<String, String> params) throws Exception {
        if (params == null) {
            params = new HttpHeaders();
        }

        return new ObjectMapper().readTree(
                this.mockMvc
                        .perform(get(url).params(params))
                        .andDo(print())
                        .andExpect(status().is(status.value()))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .toString();
    }

}
