package ec.com.guayagamer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ec.com.guayagamer.domain.EventoDTO;
import ec.com.guayagamer.domain.ExpositorDTO;
import ec.com.guayagamer.domain.SalaDTO;
import ec.com.guayagamer.domain.SesionDTO;
import ec.com.guayagamer.service.GuayaGamerService;

@Controller
public class GuayaGamerController {

	@Autowired
	private GuayaGamerService guayaGamerService;	
	
    @RequestMapping(value="/guardarExpositor",method=RequestMethod.POST)
    public Boolean guardarExpositor(@RequestBody ExpositorDTO expositor){
          return guayaGamerService.guardarExpositor(expositor);
    }	
	
    @RequestMapping(value="/guardarSala",method=RequestMethod.POST)
    public Boolean guardarSala(@RequestBody SalaDTO sala){
          return guayaGamerService.guardarSala(sala);
    }	    
    
    @RequestMapping(value="/guardarSesion",method=RequestMethod.POST)
    public Boolean guardarSala(@RequestBody SesionDTO sesion){
          return guayaGamerService.guardarSesion(sesion);
    }	
     
    @RequestMapping(value="/generarDatosTest",method=RequestMethod.GET)    
    public void generarDatosTest(){
    	guayaGamerService.generarDatosTest();
    }
    
    @RequestMapping(value="/generarEventoAutomaticamente",method=RequestMethod.GET)    
    public void generarEventoAutomaticamente(){
    	guayaGamerService.generarDatosTest();
    	guayaGamerService.generarEventoAutomaticamente();
    }    
    
    @RequestMapping("/")
    public String tweets(Model model) {
    	guayaGamerService.generarDatosTest();
    	guayaGamerService.generarEventoAutomaticamente();    	
    	List<EventoDTO>  eventosSala1 = guayaGamerService.obtenerEventosPorSala(1L);
    	System.out.println(eventosSala1.size());
        model.addAttribute("sala1", eventosSala1);
        model.addAttribute("sala2", guayaGamerService.obtenerEventosPorSala(2L));
        //model.addAttribute("profile", profile);
        return "guayagamer";
    }    
    
    
    
}
