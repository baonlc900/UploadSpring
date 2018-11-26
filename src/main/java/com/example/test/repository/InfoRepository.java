package com.example.test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
//import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Repository;

import com.example.test.model.Info;


@Repository("infoRepository")
public interface InfoRepository extends JpaRepository<Info, Integer> {
	List<Info> findByTitleOrContent(String textTitle, String textContent);
	

}
