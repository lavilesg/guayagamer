package ec.com.guayagamer.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "EVENTO")
public class EventoDTO {
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)	
	private Long id;
	
	private Long sesionId;
	
	private Long salaId;
	
	private Long expositorId;
	
	private String horaInicio;
	
	private String horaFin;	
	
	@Transient
	private ExpositorDTO expositor;	
	
	@Transient
	private SesionDTO sesion;
	
	@Transient
	private SalaDTO sala;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSesionId() {
		return sesionId;
	}

	public void setSesionId(Long sesionId) {
		this.sesionId = sesionId;
	}

	public Long getSalaId() {
		return salaId;
	}

	public void setSalaId(Long salaId) {
		this.salaId = salaId;
	}

	public Long getExpositorId() {
		return expositorId;
	}

	public void setExpositorId(Long expositorId) {
		this.expositorId = expositorId;
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}

	public SesionDTO getSesion() {
		return sesion;
	}

	public void setSesion(SesionDTO sesion) {
		this.sesion = sesion;
	}

	public SalaDTO getSala() {
		return sala;
	}

	public void setSala(SalaDTO sala) {
		this.sala = sala;
	}

	public ExpositorDTO getExpositor() {
		return expositor;
	}

	public void setExpositor(ExpositorDTO expositor) {
		this.expositor = expositor;
	}
	
	

}
