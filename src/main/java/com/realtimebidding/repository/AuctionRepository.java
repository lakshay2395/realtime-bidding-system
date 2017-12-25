package com.realtimebidding.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.realtimebidding.model.Auction;
import com.realtimebidding.model.Product;

@Repository
public interface AuctionRepository extends MongoRepository<Auction,String>{

	Auction findByProduct(Product product);
}
