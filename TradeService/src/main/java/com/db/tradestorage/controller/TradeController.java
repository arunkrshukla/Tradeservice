package com.db.tradestorage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.db.tradestorage.model.Trade;
import com.db.tradestorage.service.TradeService;

@RestController
public class TradeController {
	@Autowired
	TradeService tradeService;

	/*
	
	
	 */
	@PostMapping("/trade")
	public ResponseEntity<String> tradeValidateStore(@RequestBody Trade trade) {
		tradeService.validateTrade(trade);
		tradeService.persist(trade);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping("/trade")
	public List<Trade> findAllTrades() {
		return tradeService.findAll();
	}
}
