package ec.com.guayagamer.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.guayagamer.repository.EventoRepository;
import ec.com.guayagamer.repository.ExpositorRepository;
import ec.com.guayagamer.repository.SalaRepository;
import ec.com.guayagamer.repository.SesionRepository;

@Service
public class Services {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	protected EventoRepository eventoRepository;
	
	@Autowired
	protected SalaRepository salaRepository;
	
	@Autowired
	protected ExpositorRepository expositorRepository;
	
	@Autowired
	protected SesionRepository sesionRepository;

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
	

	public static Date getItemDate(final String date)
	{
	    final Calendar cal = Calendar.getInstance(TimeZone.getDefault());
	    final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	    format.setCalendar(cal);

	    try {
	        return format.parse(date);
	    } catch (ParseException e) {
	        return null;
	    }
	}

	public static Long minutesDiff(Date earlierDate, Date laterDate)
	{
	    if( earlierDate == null || laterDate == null ) return 0L;

	    return (Long)((laterDate.getTime()/60000) - (earlierDate.getTime()/60000));
	}	
	
	public static Date addMinutes(Date date, int amount){
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.add(Calendar.MINUTE, amount);

	    return calendar.getTime();
	}	
	
	public static String dateFormat(Date date,String formato){
		DateFormat dateFormat = new SimpleDateFormat(formato);  
		return dateFormat.format(date);  	
	}
	

	
	
	
}
