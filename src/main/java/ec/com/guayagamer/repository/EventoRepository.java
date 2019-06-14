package ec.com.guayagamer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ec.com.guayagamer.domain.EventoDTO;

public interface EventoRepository extends CrudRepository<EventoDTO, Long> {
	
	@Query(value="SELECT * FROM EVENTO E WHERE 1=1 AND E.SALA_ID = ?1",nativeQuery=true)
	public List<EventoDTO> obtenerAllEventoPorSala(Long id);		
		

}
