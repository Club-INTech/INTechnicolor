package strategie;

import container.Service;
import pathfinding.Pathfinding;
import robot.Robot;
import robot.RobotChrono;
import table.Table;
import utils.Log;
import utils.Read_Ini;

public class GameState<R extends Robot> implements Service
{    
    /*
     * Les attributs public sont en "final". Cela signifie que les objets
     * peuvent être modifiés mais pas ces références.
     */
    public final Table table;
    public final R robot;
    public final Pathfinding pathfinding;
    private Log log;
    private Read_Ini config;

    // time contient le temps écoulé depuis le début du match en ms
    // utilisé uniquement dans l'arbre des possibles
    public long time_depuis_debut;
    public long time_depuis_racine;  
    public int pointsObtenus;	// points marqués depus le debut du match

    public GameState(Read_Ini config, Log log, Table table, R robot, Pathfinding pathfinding)
    {
        this.config = config;
        this.log = log;
        this.table = table;
        this.robot = robot;
        this.pathfinding = pathfinding;
        pointsObtenus = 0;
    }
    
    /**
     * Fournit un clone de this. Le clone sera un GameState<RobotChrono>, peu importe si this est un GameState<RobotVrai> ou un GameState<RobotChrono>
     */
    public GameState<RobotChrono> clone()
    {
        Table new_table = table.clone();
        RobotChrono new_rc = new RobotChrono(config, log); 
        robot.copy(new_rc);
        Pathfinding new_pf = pathfinding.clone();	//new Pathfinding(new_table, config, log);
        
        GameState<RobotChrono> out = new GameState<RobotChrono>(config, log, new_table, new_rc, new_pf);
        out.time_depuis_debut = time_depuis_debut;
        out.time_depuis_racine = time_depuis_racine;
        out.pointsObtenus = this.pointsObtenus;
        return out;
    }

    /**
     * Copie this dans other. this reste inchangé.
     * @param other
     */
    public void copy(GameState<RobotChrono> other)
    {
        table.copy(other.table);
        robot.copy(other.robot);
        other.time_depuis_debut = time_depuis_debut;
        other.time_depuis_racine = time_depuis_racine;
        other.pointsObtenus = this.pointsObtenus;
    }

    @Override
    public void maj_config()
    {
        table.maj_config();
        robot.maj_config();
        pathfinding.maj_config();
    }
    
}
