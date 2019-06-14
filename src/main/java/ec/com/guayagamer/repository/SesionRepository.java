package ec.com.guayagamer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ec.com.guayagamer.domain.SesionDTO;

public interface SesionRepository extends CrudRepository<SesionDTO, Long> {
	
	@Query(value="SELECT * FROM SESION E WHERE 1=1",nativeQuery=true)
	public List<SesionDTO> obtenerAllSesion();	
	
	@Query(value="SELECT * FROM SESION E WHERE 1=1 AND E.ID= ?1",nativeQuery=true)
	public SesionDTO obtenerSesionPorId(Long id);		

}
