/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demoexcel32.repository;

import com.example.demoexcel32.document.Masters;
import com.example.demoexcel32.model.Master;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author Rey Messon
 */

public interface MasterRepository extends MongoRepository<Masters, Integer> {

    
}