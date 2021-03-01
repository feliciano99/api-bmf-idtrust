package com.idrust.bmfapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuandlCropDTO {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Dataset {
        Object[][] data;

        public void setData(Object[][] data) {
            this.data = data;
        }
    }

    private Dataset dataset;

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public Double getCropPrice() {
        return (Double) this.dataset.data[0][1];
    }

}
