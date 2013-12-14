package scripts;

import hook.HookGenerator;

import java.util.ArrayList;

import pathfinding.Pathfinding;
import robot.Robot;
import smartMath.Vec2;
import table.Table;
import threads.ThreadTimer;
import utils.Log;
import utils.Read_Ini;
import exception.MouvementImpossibleException;

/**
 * Script pour les lances
 * @author pf
 *
 */

// TODO Guy

class ScriptLances extends Script {
	

	public ScriptLances(Pathfinding pathfinding, ThreadTimer threadtimer, HookGenerator hookgenerator, Read_Ini config, Log log)
	{
		super(pathfinding, threadtimer, hookgenerator, config, log);
	}

	/*
	 * Le script lance 4 balles sur le côté droit
	 * Le script donne un point d'entrée
	 */
	

	@Override
	public ArrayList<Integer> version(final Robot robot, final Table table) {
		ArrayList<Integer> versionList = new ArrayList<Integer>();
		// En fait, si j'ai bien compris, les versions repr�sentent en fait
		// le nombre de lances pouvant �tre lanc�es, dans la limite de 4.
		// J'ai bon ?
		int n;
		if (robot.getNbrLances()>=4) {
			n = 4;
		} else {
			n = robot.getNbrLances();
		}
		for (int i = 0; i < n+1; i++) {
			versionList.add(i);
		}
		return versionList;
	}

	@Override
	public Vec2 point_entree(int id, final Robot robot, final Table table) {
		// A modifier, la position devant le mammouth
		// Note � moi-m�me : demander � Ngon� ou Alexandre pour la distance
		return new Vec2(0,0);
	}

	@Override
	public int score(int id_version, final Robot robot, final Table table) {
		// combien on gagne? demander à la table
		return 0;
	}

	@Override
	public int poids(final Robot robot, final Table table) {
		// On s'en fout pour le moment
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void execute(int id_version, Robot robot, Table table) throws MouvementImpossibleException
	{
		// ajuster l'orientation du robot (objet robot)
		robot.tourner(0);
		// tirer (objet robot)
		// à réfléchir (côté droit ou gauche)
		robot.tirerBalles(true);
	}

	@Override
	protected void termine(Robot robot, Table table) {
		// vide
	}
	
}
