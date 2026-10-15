package com.github.pietropn.apiregistro.repository;

import com.github.pietropn.apiregistro.entity.RegistroMeteorologico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroMeteorologicoRepository extends JpaRepository<RegistroMeteorologico, Long> {
}
