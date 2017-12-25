package com.realtimebidding.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realtimebidding.model.Product;
import com.realtimebidding.repository.ProductRepository;
import com.realtimebidding.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository ProductRepository;
	
	@Override
	public Product getById(String id) throws Exception {
		return ProductRepository.findOne(id);
	}

	@Override
	public Product addNewProduct(Product Product) throws Exception {
		return ProductRepository.insert(Product);
	}

	@Override
	public Product updateProduct(String id, Product Product) throws Exception {
		if(ProductRepository.exists(id))
			return ProductRepository.save(Product);
		throw new RuntimeException("No such Product exists with Product id -> "+id);
	}

	@Override
	public Product deleteProduct(String id) throws Exception {
		if(ProductRepository.exists(id)) {
			Product u = ProductRepository.findOne(id);
			ProductRepository.delete(id);
			return u;
		}
		throw new RuntimeException("No such Product exists with Product id -> "+id);
	}

}