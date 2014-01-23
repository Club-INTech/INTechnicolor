package container;

import java.util.Hashtable;
import java.util.Map;

import hook.HookGenerator;
import pathfinding.Pathfinding;
import exception.ConfigException;
import exception.ContainerException;
import exception.SerialManagerException;
import exception.ThreadException;
import utils.*;
import scripts.ScriptManager;
import strategie.MemoryManager;
import strategie.Strategie;
import table.Table;
import threads.ThreadAnalyseEnnemi;
import threads.ThreadTimer;
import threads.ThreadManager;
import robot.RobotVrai;
import robot.cartes.Actionneurs;
import robot.cartes.Capteurs;
import robot.cartes.Deplacements;
import robot.cartes.FiltrageLaser;
import robot.cartes.Laser;
import robot.serial.SerialManager;
import robot.serial.Serial;

/**
 * Les différents services appelables sont:
 * Log
 * Read_Ini
 * Table
 * serie* (serieAsservissement, serieCapteursActionneurs, serieLaser)
 * Deplacements
 * Capteur
 * Actionneurs
 * HookGenerator
 * RobotVrai
 * ScriptManager
 * Strategie
 * thread* (threadTimer, threadStrategie, threadCapteurs, threadLaser)
 * Pathfinding
 * MemoryManager
 * Laser
 * FiltrageLaser
 * CheckUp
 * 
 * @author pf
 *
 */

public class Container {

	private Map<String,Service> services = new Hashtable<String,Service>();
	private SerialManager serialmanager = null;
	private ThreadManager threadmanager = null;
	private Log log = null;
	private Read_Ini config = null;

	public void destructeur()
	{
		if(serialmanager != null)
		{
			if(serialmanager.serieAsservissement != null)
				serialmanager.serieAsservissement.close();
			if(serialmanager.serieCapteursActionneurs != null)
				serialmanager.serieCapteursActionneurs.close();
			if(serialmanager.serieLaser != null)
				serialmanager.serieLaser.close();
		}
	}
	
	public Container()
	{
		try {
			services.put("Read_Ini", (Service)new Read_Ini("../pc/config/"));
			config = (Read_Ini)services.get("Read_Ini");
			services.put("Log", (Service)new Log(config));
			log = (Log)services.get("Log");
			threadmanager = new ThreadManager(config, log);
			serialmanager = new SerialManager(log);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public Service getService(String nom) throws ContainerException, ThreadException, ConfigException, SerialManagerException
	{
//		log.debug("getService de "+nom, this);
		if(services.containsKey(nom));
		else if(nom == "Table")
		{
			services.put(nom, (Service)new Table(	(Log)getService("Log"),
													(Read_Ini)getService("Read_Ini")));
			((Table) services.get(nom)).initialise();
		}
		else if(nom.length() > 4 && nom.substring(0,5).equals("serie"))
		{
			services.put(nom, (Service)serialmanager.getSerial(nom));
		}
		else if(nom == "Deplacements")
			services.put(nom, (Service)new Deplacements((Log)getService("Log"),
														(Serial)getService("serieAsservissement")));
		else if(nom == "Capteur" || nom == "Capteurs")
			services.put(nom, (Service)new Capteurs(	(Read_Ini)getService("Read_Ini"),
													(Log)getService("Log"),
													(Serial)getService("serieCapteursActionneurs")));
		else if(nom == "Actionneurs")
			services.put(nom, (Service)new Actionneurs(	(Read_Ini)getService("Read_Ini"),
														(Log)getService("Log"),
														(Serial)getService("serieCapteursActionneurs")));
		else if(nom == "HookGenerator")
			services.put(nom, (Service)new HookGenerator(	(Read_Ini)getService("Read_Ini"),
															(Log)getService("Log"),
															(Capteurs)getService("Capteur")));		
		else if(nom == "RobotVrai")
			services.put(nom, (Service)new RobotVrai(	(Capteurs)getService("Capteur"),
														(Actionneurs)getService("Actionneurs"),
														(Deplacements)getService("Deplacements"),
														(HookGenerator)getService("HookGenerator"),
														(Table)getService("Table"),
														(Read_Ini)getService("Read_Ini"),
														(Log)getService("Log")));		
		else if(nom == "ScriptManager")
			services.put(nom, (Service)new ScriptManager(	(Pathfinding)getService("Pathfinding"),
															(HookGenerator)getService("HookGenerator"),
															(Read_Ini)getService("Read_Ini"),
															(Log)getService("Log"),
															(RobotVrai)getService("RobotVrai")));
		else if(nom == "Strategie")
			services.put(nom, (Service)new Strategie(	(MemoryManager)getService("MemoryManager"),
														(ThreadAnalyseEnnemi)getService("threadAnalyseEnnemi"),
														(ThreadTimer)getService("threadTimer"),
														(ScriptManager)getService("ScriptManager"),
														(Table)getService("Table"),
														(RobotVrai)getService("RobotVrai"),
														(Read_Ini)getService("Read_Ini"),
														(Log)getService("Log")));
		else if(nom == "threadTimer")
			services.put(nom, (Service)threadmanager.getThreadTimer(	(Table)getService("Table"),
																		(Capteurs)getService("Capteur"),
																		(Deplacements)getService("Deplacements")));
		else if(nom == "threadPosition")
			services.put(nom, (Service)threadmanager.getThreadPosition(	(RobotVrai)getService("Robotvrai"),
																		(ThreadTimer)getService("threadTimer")));
		else if(nom == "threadCapteurs")
			services.put(nom, (Service)threadmanager.getThreadCapteurs(	(RobotVrai)getService("Robotvrai"),
																		(Pathfinding)getService("Pathfinding"),
																		(ThreadTimer)getService("threadTimer"),
																		(Table)getService("Table"),
																		(Capteurs)getService("Capteur")));

		else if(nom == "threadStrategie")
			services.put(nom, (Service)threadmanager.getThreadStrategie((Strategie)getService("Strategie"),
																		(Table)getService("Table"),
																		(RobotVrai)getService("RobotVrai"),
																		(MemoryManager)getService("MemoryManager")));
		else if(nom == "threadLaser")
			services.put(nom, (Service)threadmanager.getThreadLaser(	(Laser)getService("Laser"),
																		(Table)getService("Table"),
																		(ThreadTimer)getService("threadTimer"),
																		(FiltrageLaser)getService("FiltrageLaser")));
		else if(nom == "threadAnalyseEnnemi")
			services.put(nom, (Service)threadmanager.getThreadAnalyseEnnemi(	(Table)getService("Table"),
																				(ThreadTimer)getService("threadTimer")));
		else if(nom == "Pathfinding")
			services.put(nom, (Service)new Pathfinding(	(Table)getService("Table"),
														(Read_Ini)getService("Read_Ini"),
														(Log)getService("Log")));
		else if(nom == "MemoryManager")
			services.put(nom, (Service)new MemoryManager(	(Read_Ini)getService("Read_Ini"),
															(Log)getService("Log"),
															(Table)getService("Table")));
		else if(nom == "Laser")
			services.put(nom, (Service)new Laser(	(Read_Ini)getService("Read_Ini"),
													(Log)getService("Log"),
													(Serial)getService("serieLaser"),
													(RobotVrai)getService("RobotVrai")));
		else if(nom == "FiltrageLaser")
			services.put(nom, (Service)new FiltrageLaser(	(Read_Ini)getService("Read_Ini"),
															(Log)getService("Log")));

		else if(nom == "CheckUp")
			services.put(nom, (Service)new CheckUp(	(Log)getService("Log"),
													(RobotVrai)getService("RobotVrai")));
		else
		{
			log.critical("Erreur de getService pour le service: "+nom, this);
			throw new ContainerException();
		}
		return services.get(nom);
	}
	
}
