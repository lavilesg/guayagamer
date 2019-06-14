package ec.com.guayagamer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ec.com.guayagamer.domain.ExpositorDTO;

public interface ExpositorRepository extends CrudRepository<ExpositorDTO, Long> {
	
	@Query(value="SELECT * FROM EXPOSITOR E WHERE 1=1 AND E.TIPO='E'",nativeQuery=true)
	public List<ExpositorDTO> obtenerAllExpositor();		
	
	
	@Query(value="SELECT * FROM EXPOSITOR E WHERE 1=1 AND E.ID= ?1",nativeQuery=true)
	public ExpositorDTO obtenerExpositorPorId(Long id);	
	
	
	@Query(value="SELECT * FROM EXPOSITOR E WHERE 1=1 AND E.TIPO= ?1",nativeQuery=true)
	public ExpositorDTO obtenerExpositorPorTipo(String tipo);		

}
