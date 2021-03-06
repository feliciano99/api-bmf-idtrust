package com.idrust.bmfapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.idrust.bmfapi.dtos.BaseResponse;
import com.idrust.bmfapi.dtos.CropPriceDTO;
import com.idrust.bmfapi.services.CropService;

@RestController
@RequestMapping(path = "crops")
public class CropController {

	private final CropService cropService;

	@Autowired
	public CropController(CropService cropService) {
		this.cropService = cropService;
	}

	@ResponseBody
	@GetMapping(value = "{cropCode}/price")
	public ResponseEntity<BaseResponse> calculateCropPrice(@PathVariable String cropCode, @RequestParam String date) {
		final CropPriceDTO cropPriceDTO = new CropPriceDTO(cropService.calculateCropPrice(cropCode, date));
		return ResponseEntity.ok(BaseResponse.ok(cropPriceDTO));
	}

}
