package app;

import java.util.Arrays;

import expert_iteration.ExpertIteration;
import ludemeplexDetection.LudemeplexDetection;
import main.CommandLineArgParse;
import main.CommandLineArgParse.ArgOption;
import main.CommandLineArgParse.OptionTypes;
import supplementary.experiments.debugging.FindCrashingTrial;
import supplementary.experiments.eval.EvalAgents;
import supplementary.experiments.eval.EvalGames;
import supplementary.experiments.eval.EvalGate;
import supplementary.experiments.scripts.GenerateGatingScripts;
import supplementary.experiments.speed.PlayoutsPerSec;
import utils.concepts.db.ExportDbCsvConcepts;
import utils.features.ExportFeaturesDB;

/**
 * Class with helper method to delegate to various other main methods
 * based on command-line arguments.
 * 
 * @author Dennis Soemers
 */
public class PlayerCLI
{
	/**
	 * @param args The first argument is expected to be a command
	 * to run, with all subsequent arguments being passed onto
	 * the called command.
	 */
	public static void runCommand(final String[] args)
	{
		final String[] commandArg = new String[]{args[0]};
		
		final CommandLineArgParse argParse = 
				new CommandLineArgParse
				(
					true,
					"Run one of Ludii's command-line options, followed by the command's arguments.\n"
					+ "Enter a command's name followed by \"-h\" or \"--help\" for "
					+ "more information on the arguments for that particular command."
				);
		
		argParse.addOption(new ArgOption()
				.help("Command to run. For more help, enter a command followed by \" --help\"")
				.setRequired()
				.withLegalVals
				(
					"--time-playouts",
					"--expert-iteration",
					"--eval-agents",
					"--find-crashing-trial",
					"--eval-gate",
					"--eval-games",
					"--ludeme-detection",
					"--generate-gating-scripts",
					"--export-features-db",
					"--export-moveconcept-db"
				)
				.withNumVals(1)
				.withType(OptionTypes.String));
		
		// parse the args
		if (!argParse.parseArguments(commandArg))
			return;
		
		final String command = argParse.getValueString(0);
		final String[] passArgs = Arrays.copyOfRange(args, 1, args.length);
		
		if (command.equalsIgnoreCase("--time-playouts"))
			PlayoutsPerSec.main(passArgs);
		else if (command.equalsIgnoreCase("--expert-iteration"))
			ExpertIteration.main(passArgs);
		else if (command.equalsIgnoreCase("--eval-agents"))
			EvalAgents.main(passArgs);
		else if (command.equalsIgnoreCase("--find-crashing-trial"))
			FindCrashingTrial.main(passArgs);
		else if (command.equalsIgnoreCase("--eval-gate"))
			EvalGate.main(passArgs);
		else if (command.equalsIgnoreCase("--eval-games"))
			EvalGames.main(passArgs);
		else if (command.equalsIgnoreCase("--ludeme-detection"))
			LudemeplexDetection.main(passArgs);
		else if (command.equalsIgnoreCase("--generate-gating-scripts"))
			GenerateGatingScripts.main(passArgs);
		else if (command.equalsIgnoreCase("--export-features-db"))
			ExportFeaturesDB.main(passArgs);
		else if (command.equalsIgnoreCase("--export-moveconcept-db"))
			ExportDbCsvConcepts.main(passArgs);
		else
			System.err.println("ERROR: command not yet implemented: " + command);

	}

}