package com.realtimebidding.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import com.realtimebidding.model.Auction;
import com.realtimebidding.model.Product;
import com.realtimebidding.repository.AuctionRepository;
import com.realtimebidding.services.AuctionService;

@Service
public class AuctionServiceImpl implements AuctionService {

	@Autowired
	AuctionRepository AuctionRepository;
	
	@Autowired
	MongoOperations mongoOperations;
 	
	@Override
	public Auction getById(String id) throws Exception {
		return AuctionRepository.findOne(id);
	}

	@Override
	public Auction addNewAuction(Auction auction) throws Exception {
		return AuctionRepository.insert(auction);
	}

	@Override
	public Auction updateAuction(String id, Auction auction) throws Exception {
		if(AuctionRepository.exists(id))
			return AuctionRepository.save(auction);
		throw new RuntimeException("No such Auction exists with Auction id -> "+id);
	}

	@Override
	public Auction deleteAuction(String id) throws Exception {
		if(AuctionRepository.exists(id)) {
			Auction u = AuctionRepository.findOne(id);
			AuctionRepository.delete(id);
			return u;
		}
		throw new RuntimeException("No such Auction exists with Auction id -> "+id);
	}
	
	@Override
	public Auction getByProduct(Product product) throws Exception {
		Auction u = AuctionRepository.findByProduct(product);
		if(u != null)
			return u;
		throw new RuntimeException("No such auction exists for product ->"+product);
	}

	@Override
	public List<Auction> getAllActive() throws Exception {
		Query query = new Query();
		//query.addCriteria(Criteria.where("endTime").gt(System.currentTimeMillis())); 
		List<Auction> list = mongoOperations.find(query,Auction.class);
		return list;
	}
}