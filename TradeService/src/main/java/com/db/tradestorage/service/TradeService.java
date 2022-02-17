package com.db.tradestorage.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.tradestorage.dao.TradeRepository;
import com.db.tradestorage.exception.InvalidTradeException;
import com.db.tradestorage.model.Trade;

@Service
public class TradeService {

	private static final Logger log = LoggerFactory.getLogger(TradeService.class);

	@Autowired
	TradeRepository tradeRepository;

	public void validateTrade(Trade trade) {
		if (validMaturityDate(trade)) {
			// Trade exsitingTrade = tradeDao.findTrade(trade.getTradeId());
			Optional<Trade> exsitingTrade = tradeRepository.findById(trade.getTradeId());
			if (exsitingTrade.isPresent()) {
				validateVersion(trade, exsitingTrade.get());
			}
		} else {
			throw new InvalidTradeException(trade.getTradeId() + "  Invalid Maturity Date...");
		}
	}

	private void validateVersion(Trade trade, Trade oldTrade) {
		// validation 1 During transmission if the
		// lower version is being received by the store it will reject the trade and
		// throw an exception.
		if (trade.getVersion() < oldTrade.getVersion()) {
			throw new InvalidTradeException(trade.getTradeId() + "  Trade Id lower version is not Accepted");
		}

	}

	// 2. Store should not allow the trade which has less maturity date then today
	// date
	private boolean validMaturityDate(Trade trade) {
		return trade.getMaturityDate().isBefore(LocalDate.now()) ? false : true;
	}

	public void persist(Trade trade) {
		// tradeDao.save(trade);
		trade.setCreatedDate(LocalDate.now());
		tradeRepository.save(trade);
	}

	public List<Trade> findAll() {
		return tradeRepository.findAll();
		// return tradeDao.findAll();
	}

	public void updateExpiryFlagOfTrade() {
		tradeRepository.findAll().stream().forEach(t -> {
			if (!validMaturityDate(t)) {
				t.setExpiredFlag("Y");
				log.info("Trade which needs to updated {}", t);
				tradeRepository.save(t);
			}
		});
	}

}
