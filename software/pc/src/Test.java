import robot.SerialManager;
/**
 * Classe � modifier pour que chacun fasse ses tests sans faire chier tout le monde!
 * Bien �videment, ce n'est pas � committer
 * @author pierre
 *
 */
public class Test {
	public static void test()
	{
		SerialManager serialManager = new SerialManager();
		
		serialManager.checkSerial();
		serialManager.createSerial();
		System.out.println(serialManager.serieAsservissement.communiquer("?", 1));
		
		serialManager.serieAsservissement.close();
	}
}
