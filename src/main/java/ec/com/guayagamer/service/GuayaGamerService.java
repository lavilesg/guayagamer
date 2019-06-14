package ec.com.guayagamer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.com.guayagamer.domain.EventoDTO;
import ec.com.guayagamer.domain.ExpositorDTO;
import ec.com.guayagamer.domain.SalaDTO;
import ec.com.guayagamer.domain.SesionDTO;

@Service
@Transactional
public class GuayaGamerService extends Services {


	public Boolean guardarExpositor(ExpositorDTO expositor) {
		try {
			if (expositor.getId() != null) {
				getEm().merge(expositor);

			} else {
				getEm().persist(expositor);
			}
			
			getEm().flush();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public Boolean guardarSala(SalaDTO sala) {
		try {
			if (sala.getId() != null) {
				getEm().merge(sala);
			} else {
				getEm().persist(sala);

			}
			getEm().flush();			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public Boolean guardarSesion(SesionDTO sesion) {
		try {
			if (sesion.getId() != null) {
				getEm().merge(sesion);
			} else {
				getEm().persist(sesion);				

			}
			getEm().flush();			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	
	
	public List<ExpositorDTO> asignarEvento(List<ExpositorDTO> listaExpositor, Long duracion,Long salaId, Long sesionId, String horaInicio){
		
		List<ExpositorDTO> listaExpoInicio = new ArrayList<ExpositorDTO>();	
		Long duracionSesionInicio = 0L;	
		List<ExpositorDTO> listaExpoTMP = new ArrayList<ExpositorDTO>();		
		Date horaIni = null;	
		Date horaFin = null;
		for(ExpositorDTO expositor : listaExpositor){
			EventoDTO evento = new EventoDTO();
			if(duracionSesionInicio == 0L){
				horaIni = getItemDate("06/14/2019 "+horaInicio);
				horaFin = addMinutes(horaIni, Integer.parseInt(expositor.getDuracion().toString()) );
				duracionSesionInicio = expositor.getDuracion();
				evento.setExpositorId(expositor.getId());
				evento.setSalaId(salaId);
				evento.setSesionId(sesionId);
				evento.setHoraInicio(dateFormat(horaIni,"HH:mm"));
				evento.setHoraFin(dateFormat(horaFin,"HH:mm"));
				eventoRepository.save(evento);
				listaExpoInicio.add(expositor);
				
			}else if((duracionSesionInicio + expositor.getDuracion()) <= duracion){
				duracionSesionInicio = duracionSesionInicio + expositor.getDuracion();
				listaExpoInicio.add(expositor);
				horaIni = horaFin;
				horaFin = addMinutes(horaIni, Integer.parseInt(expositor.getDuracion().toString()) );				
				
				evento.setExpositorId(expositor.getId());
				evento.setSalaId(salaId);
				evento.setSesionId(sesionId);
				evento.setHoraInicio(dateFormat(horaIni,"HH:mm"));
				evento.setHoraFin(dateFormat(horaFin,"HH:mm"));
				eventoRepository.save(evento);				
				
			}else{
				listaExpoTMP.add(expositor);
			}		
		}
		
		for(ExpositorDTO expositor : listaExpoInicio){
			System.out.println("expositor --> "+expositor.getNombre() + " - "+expositor.getDuracion());
		}
		
		System.out.println("----------------------------------------------------");			
		
		
		return listaExpoTMP;
	}
	
	
	public Long obtenerTotalDuracionListaExpositor(List<ExpositorDTO> listaExpositor){
		long duracionRestante= 0L;
		for(ExpositorDTO expositor:listaExpositor){
			duracionRestante = duracionRestante + expositor.getDuracion();
		}
		return duracionRestante;
	}
	
	public void asignarRefrigerio(Long salaId,Long sesionId,String horaInicio){
		ExpositorDTO refrigerio= expositorRepository.obtenerExpositorPorTipo("R");
		EventoDTO evento = new EventoDTO();	
		evento.setExpositorId(refrigerio.getId());
		evento.setSalaId(salaId);
		evento.setSesionId(sesionId);
		evento.setHoraInicio(horaInicio);	
		eventoRepository.save(evento);
		
	}
	
	public void asignarMesaRedonda(Long salaId,Long sesionId,String horaInicio){
		ExpositorDTO mesa= expositorRepository.obtenerExpositorPorTipo("M");
		EventoDTO evento = new EventoDTO();	
		evento.setExpositorId(mesa.getId());
		evento.setSalaId(salaId);
		evento.setSesionId(sesionId);
		evento.setHoraInicio(horaInicio);	
		eventoRepository.save(evento);
		
	}	
	
	
	public void generarEventoAutomaticamente(){
		
		SesionDTO sesionInicio = null;
		SesionDTO sesionFin = null;		
		List<SesionDTO> listaSesionFin = new ArrayList<SesionDTO>();
		
		List<SesionDTO> listaSesion =  sesionRepository.obtenerAllSesion();
		
		for(SesionDTO sesion : listaSesion){
			if("I".equals(sesion.getTipo())){
				sesionInicio = sesion;
			}else if("F".equals(sesion.getTipo())){
				listaSesionFin.add(sesion);
				//sesionFin = sesion;
			}
		}
		
		Long duracionInicio = minutesDiff(getItemDate("06/14/2019 "+sesionInicio.getHoraInicio()), getItemDate("06/14/2019 "+sesionInicio.getHoraFin()));
				
		List<ExpositorDTO> listaExpositorTodos = expositorRepository.obtenerAllExpositor();		
		
		List<ExpositorDTO> listaExpoRestante = asignarEvento(listaExpositorTodos,duracionInicio,1L,sesionInicio.getId(),sesionInicio.getHoraInicio());
		
		asignarRefrigerio(1L, sesionInicio.getId(), dateFormat(addMinutes(getItemDate("06/14/2019 "+sesionInicio.getHoraInicio()),Integer.parseInt(duracionInicio.toString()) ) ,"HH:mm"));
		
		listaExpoRestante = asignarEvento(listaExpoRestante,duracionInicio,2L,sesionInicio.getId(),sesionInicio.getHoraInicio());
		
		asignarRefrigerio(2L, sesionInicio.getId(), dateFormat(addMinutes(getItemDate("06/14/2019 "+sesionInicio.getHoraInicio()),Integer.parseInt(duracionInicio.toString()) ) ,"HH:mm"));
		
		long duracionRestante = obtenerTotalDuracionListaExpositor(listaExpoRestante);
		duracionRestante = duracionRestante / 2;
		
		Long duracionFin = 0L;
	
			
		long duracionAlternativa1 = minutesDiff(getItemDate("06/14/2019 "+listaSesionFin.get(0).getHoraInicio()), getItemDate("06/14/2019 "+listaSesionFin.get(0).getHoraFin()));
		long duracionAlternativa2 = minutesDiff(getItemDate("06/14/2019 "+listaSesionFin.get(1).getHoraInicio()), getItemDate("06/14/2019 "+listaSesionFin.get(1).getHoraFin()));		

		if(duracionAlternativa1 > duracionAlternativa2 && duracionAlternativa2 < duracionRestante  ){
			sesionFin= listaSesionFin.get(0);
			duracionFin = duracionAlternativa1;
		}else{
			sesionFin= listaSesionFin.get(1);
			duracionFin = duracionAlternativa2;
		}
		
		listaExpoRestante = asignarEvento(listaExpoRestante,duracionFin,1L,sesionFin.getId(),sesionFin.getHoraInicio());
		
		asignarMesaRedonda(1L, sesionInicio.getId(), dateFormat(addMinutes(getItemDate("06/14/2019 "+sesionFin.getHoraInicio()),Integer.parseInt(duracionFin.toString()) ) ,"HH:mm"));		
		
		listaExpoRestante = asignarEvento(listaExpoRestante,duracionFin,2L,sesionFin.getId(),sesionFin.getHoraInicio());	
		
		asignarMesaRedonda(2L, sesionInicio.getId(), dateFormat(addMinutes(getItemDate("06/14/2019 "+sesionFin.getHoraInicio()),Integer.parseInt(duracionFin.toString()) ) ,"HH:mm"));		
		
		
	}
	
	public List<EventoDTO> obtenerEventosPorSala(Long salaId){
		List<EventoDTO> eventos = eventoRepository.obtenerAllEventoPorSala(salaId);
		for(EventoDTO evento : eventos){
			evento.setSesion(sesionRepository.obtenerSesionPorId(evento.getId()));
			evento.setSala(salaRepository.obtenerSalaPorId(evento.getSalaId()));
			evento.setExpositor(expositorRepository.obtenerExpositorPorId(evento.getExpositorId()));
		}
		
		return eventos;
	}
	
	
	
	
    public void generarDatosTest(){
    	
    	ExpositorDTO expositor1= new ExpositorDTO();
    	expositor1.setNombre("Electronic Arts");
    	expositor1.setEstado("A");
    	expositor1.setDuracion(60L);
    	expositor1.setTipo("E");
    	guardarExpositor(expositor1);
    	
    	ExpositorDTO expositor2= new ExpositorDTO();
    	expositor2.setNombre("Microsoft");
    	expositor2.setEstado("A");
    	expositor2.setDuracion(45L);
    	expositor2.setTipo("E");
    	guardarExpositor(expositor2);    
    	
    	ExpositorDTO expositor3= new ExpositorDTO();
    	expositor3.setNombre("Ubisoft");
    	expositor3.setEstado("A");
    	expositor3.setDuracion(30L);
    	expositor3.setTipo("E");
    	guardarExpositor(expositor3);
    	
    	ExpositorDTO expositor4= new ExpositorDTO();
    	expositor4.setNombre("Beteshda");
    	expositor4.setEstado("A");
    	expositor4.setDuracion(45L);
    	expositor4.setTipo("E");
    	guardarExpositor(expositor4);
    	
    	ExpositorDTO expositor5= new ExpositorDTO();
    	expositor5.setNombre("Devolver Digital");
    	expositor5.setEstado("A");
    	expositor5.setDuracion(45L);
    	expositor5.setTipo("E");
    	guardarExpositor(expositor5);
    	
    	ExpositorDTO expositor6= new ExpositorDTO();
    	expositor6.setNombre("PC Gaming");
    	expositor6.setEstado("A");
    	expositor6.setDuracion(15L);
    	expositor6.setTipo("E");
    	guardarExpositor(expositor6);

    	ExpositorDTO expositor7= new ExpositorDTO();
    	expositor7.setNombre("Nintendo Spotlight");
    	expositor7.setEstado("A");
    	expositor7.setDuracion(60L);
    	expositor7.setTipo("E");
    	guardarExpositor(expositor7);
    	
    	ExpositorDTO expositor8= new ExpositorDTO();
    	expositor8.setNombre("Game Interface Design");
    	expositor8.setEstado("A");
    	expositor8.setDuracion(30L);
    	expositor8.setTipo("E");
    	guardarExpositor(expositor8);
    	
    	ExpositorDTO expositor9= new ExpositorDTO();
    	expositor9.setNombre("Sega");
    	expositor9.setEstado("A");
    	expositor9.setDuracion(45L);
    	expositor9.setTipo("E");
    	guardarExpositor(expositor9);
    	
    	ExpositorDTO expositor10= new ExpositorDTO();
    	expositor10.setNombre("Atari Perspectives");
    	expositor10.setEstado("A");
    	expositor10.setDuracion(30L);
    	expositor10.setTipo("E");
    	guardarExpositor(expositor10);
    	
    	ExpositorDTO expositor11= new ExpositorDTO();
    	expositor11.setNombre("Sony");
    	expositor11.setEstado("A");
    	expositor11.setDuracion(30L);
    	expositor11.setTipo("E");
    	guardarExpositor(expositor11);
    	
    	ExpositorDTO expositor12= new ExpositorDTO();
    	expositor12.setNombre("Exercise Gaming");
    	expositor12.setEstado("A");
    	expositor12.setDuracion(45L);
    	expositor12.setTipo("E");
    	guardarExpositor(expositor12);
    	
    	ExpositorDTO expositor13= new ExpositorDTO();
    	expositor13.setNombre("Brain Magic Power RPG");
    	expositor13.setEstado("A");
    	expositor13.setDuracion(60L);
    	expositor13.setTipo("E");
    	guardarExpositor(expositor13);
    	
    	ExpositorDTO expositor14= new ExpositorDTO();
    	expositor14.setNombre("Developing Games with Directx");
    	expositor14.setEstado("A");
    	expositor14.setDuracion(60L);
    	expositor14.setTipo("E");
    	guardarExpositor(expositor14);
    	
    	ExpositorDTO expositor15= new ExpositorDTO();
    	expositor15.setNombre("IGDA Game Leadership");
    	expositor15.setEstado("A");
    	expositor15.setDuracion(45L);
    	expositor15.setTipo("E");
    	guardarExpositor(expositor15); 
    	
    	ExpositorDTO expositor16= new ExpositorDTO();
    	expositor16.setNombre("Why Not Jaguar");
    	expositor16.setEstado("A");
    	expositor16.setDuracion(15L);
    	expositor16.setTipo("E");
    	guardarExpositor(expositor16); 
    	
    	ExpositorDTO expositor17= new ExpositorDTO();
    	expositor17.setNombre("Gaming for the masses");
    	expositor17.setEstado("A");
    	expositor17.setDuracion(30L);
    	expositor17.setTipo("E");
    	guardarExpositor(expositor17); 
    	
    	ExpositorDTO expositor18= new ExpositorDTO();
    	expositor18.setNombre("My first Nintendo");
    	expositor18.setEstado("A");
    	expositor18.setDuracion(30L);
    	expositor18.setTipo("E");
    	guardarExpositor(expositor18); 
    	
    	ExpositorDTO expositor19= new ExpositorDTO();
    	expositor19.setNombre("Sonic meets Mario and Luigi");
    	expositor19.setEstado("A");
    	expositor19.setDuracion(60L);
    	expositor19.setTipo("E");
    	guardarExpositor(expositor19); 
    	
    	ExpositorDTO expositor20= new ExpositorDTO();
    	expositor20.setNombre("Game Tester Life");
    	expositor20.setEstado("A");
    	expositor20.setDuracion(30L);
    	expositor20.setTipo("E");
    	guardarExpositor(expositor20); 
    	
    	ExpositorDTO expositor21= new ExpositorDTO();
    	expositor21.setNombre("Club Nintendo");
    	expositor21.setEstado("A");
    	expositor21.setDuracion(15L);
    	expositor21.setTipo("E");
    	guardarExpositor(expositor21);  
    	
    	ExpositorDTO refrigerio= new ExpositorDTO();
    	refrigerio.setNombre("Refrigerio");
    	refrigerio.setEstado("A");
    	refrigerio.setDuracion(60L);
    	refrigerio.setTipo("R");
    	guardarExpositor(refrigerio); 
    	
    	ExpositorDTO mesa= new ExpositorDTO();
    	mesa.setNombre("Mesa Redonda");
    	mesa.setEstado("A");
    	mesa.setDuracion(60L);
    	mesa.setTipo("M");
    	guardarExpositor(mesa);     	
    	
    	SalaDTO sala1 = new SalaDTO();
    	sala1.setDescripcion("Sala 1");
    	sala1.setEstado("A");
    	guardarSala(sala1);    	
    	
    	SalaDTO sala2 = new SalaDTO();
    	sala2.setDescripcion("Sala 2");
    	sala2.setEstado("A");
    	guardarSala(sala2);   
    	
    	SesionDTO session1 = new SesionDTO();
    	session1.setDescripcion("mañana");
    	session1.setHoraInicio("9:00");
    	session1.setHoraFin("12:00"); 
    	session1.setEstado("A");
    	session1.setTipo("I");
    	guardarSesion(session1);    	
    	
    	SesionDTO session2 = new SesionDTO();
    	session2.setDescripcion("tarde");
    	session2.setHoraInicio("13:00");
    	session2.setHoraFin("17:00");  
    	session2.setEstado("A");  
    	session2.setTipo("F");
    	guardarSesion(session2);
    	
    	SesionDTO session3 = new SesionDTO();
    	session3.setDescripcion("tarde");
    	session3.setHoraInicio("13:00");
    	session3.setHoraFin("16:00");  
    	session3.setEstado("A");  
    	session3.setTipo("F");
    	guardarSesion(session3);    	
    	
    }	
	

}
