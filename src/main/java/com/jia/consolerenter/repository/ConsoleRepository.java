package com.jia.consolerenter.repository;

import com.jia.consolerenter.model.Console;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConsoleRepository extends JpaRepository<Console,Long> {
    @Query("SELECT DISTINCT c.consoleType FROM Console c")
    List<String> findDistinctConsoleTypes();


}
