package ec.com.guayagamer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ec.com.guayagamer.domain.SalaDTO;

public interface SalaRepository extends CrudRepository<SalaDTO, Long> {
	
	@Query(value="SELECT * FROM SESION E WHERE 1=1",nativeQuery=true)
	public List<SalaDTO> obtenerAllSesion();		
	
	
	@Query(value="SELECT * FROM SALA E WHERE 1=1 AND E.ID= ?1",nativeQuery=true)
	public SalaDTO obtenerSalaPorId(Long id);	
	
	

}
