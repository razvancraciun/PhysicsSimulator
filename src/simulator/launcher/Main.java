package simulator.launcher;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/*
 * Examples of command-line parameters:
 * 
 *  -h
 *  -i resources/examples/ex4.4body.txt -s 100
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100 -gl ftcg
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100 -gl nlug
 *
 */

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.factories.BasiBodyBuilder;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.FallingToCenterGravityBuilder;
import simulator.factories.MassLosingBodyBuilder;
import simulator.factories.NewtonUniversalGravitationBuilder;
import simulator.factories.NoGravityBuilder;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;
import view.MainWindow;

public class Main {

	// default values for some parameters
	//
	private final static Double _dtimeDefaultValue = 2500.0;
	private final static Integer _stepsDefaultValue=150;

	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double _dtime = null;
	private static String _inFile = null;
	private static JSONObject _gravityLawsInfo = null;
	private static String _outFile =null;
	private static Integer _steps= null;
	private static String _mode=null;

	// factories
	private static Factory<Body> _bodyFactory;
	private static Factory<GravityLaws> _gravityLawsFactory;

	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void init() {
		// initialize the bodies factory
		List<Builder<?>> builders=new ArrayList<Builder<?>>();
		builders.add(new BasiBodyBuilder());
		builders.add(new MassLosingBodyBuilder());
		
		_bodyFactory=new BuilderBasedFactory(builders);
		
		// initialize the gravity laws factory
		List<Builder> gls = new ArrayList<Builder>();
		gls.add(new NoGravityBuilder());
		gls.add(new FallingToCenterGravityBuilder());
		gls.add(new NewtonUniversalGravitationBuilder());
		_gravityLawsFactory=new BuilderBasedFactory(gls);
	}

	private static void parseArgs(String[] args) {

		// define the valid command line options
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseDeltaTimeOption(line);
			parseGravityLawsOption(line);
			parseOutFileOption(line);
			parseStepsOption(line);
			parseModeOption(line);
			

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static void parseModeOption(CommandLine line) {
		if(!line.hasOption("m")) {
			_mode="gui";
			return;
		}
		_mode = line.getOptionValue("m");
		if(!_mode.equals("gui")&&!_mode.equals("batch")) {
			throw new IllegalArgumentException("Mode must be either gui or batch");
		}
		
		
	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());
		
		//output file
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file").build());
		// delta-time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _dtimeDefaultValue + ".")
				.build());
		//steps
		cmdLineOptions.addOption(Option.builder("s").longOpt("steps").hasArg().desc("Number of steps to run").build());
		
		//mode
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Execution Mode. Possible values: ’batch’\n" + 
				"(Batch mode), ’gui’ (Graphical User Interface").build());
		
		String gravityLawsValues = "N/A";
		String defaultGravityLawsValue = "N/A";
		if (_gravityLawsFactory != null) {
			gravityLawsValues = "";
			for (JSONObject fe : _gravityLawsFactory.getInfo()) {
				if (gravityLawsValues.length() > 0) {
					gravityLawsValues = gravityLawsValues + ", ";
				}
				gravityLawsValues = gravityLawsValues + "'" + fe.get("type") + "' (" + fe.get("desc") + ")";
			}
			defaultGravityLawsValue = _gravityLawsFactory.getInfo().get(0).get("type").toString();
		}
		cmdLineOptions.addOption(Option.builder("gl").longOpt("gravity-laws").hasArg()
				.desc("Gravity laws to be used in the simulator. Possible values: " + gravityLawsValues
						+ ". Default value: '" + defaultGravityLawsValue + "'.")
				.build());

		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null) {
			throw new ParseException("An input file of bodies is required");
		}
	}
	
	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}

	private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
		try {
			_dtime = Double.parseDouble(dt);
			assert (_dtime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
	}
	
	private static void parseStepsOption(CommandLine line) throws ParseException {
		String step=line.getOptionValue("s", _stepsDefaultValue.toString());
		try {
			_steps = Integer.parseInt(step);
			assert(_steps>0);
		}
		
		catch(Exception e) {
			throw new ParseException("Invalid step value" + step);
		}
	}

	private static void parseGravityLawsOption(CommandLine line) throws ParseException {

		String gl = line.getOptionValue("gl");
		if (gl != null) {
			for (JSONObject fe : _gravityLawsFactory.getInfo()) {
				String type=fe.get("type").toString();
				type=type.substring(2,type.length()-2);
				if (gl.equals(type)) {
					_gravityLawsInfo = fe;
					break;
				}
			}
			if (_gravityLawsInfo == null) {
				throw new ParseException("Invalid gravity laws: " + gl);
			}
		} else {
			_gravityLawsInfo = _gravityLawsFactory.getInfo().get(0);
		}
	}

	private static void startBatchMode() throws Exception {
		GravityLaws law=_gravityLawsFactory.createInstance(_gravityLawsInfo);
		PhysicsSimulator sim=new PhysicsSimulator(_dtime,law);
		Controller controller=new Controller(sim,_bodyFactory,_gravityLawsFactory);
		controller.loadBodies(new FileInputStream(_inFile));
		OutputStream out= _outFile==null ? System.out : new FileOutputStream(_outFile);
		controller.run(_steps, out);
	}

	private static void start(String[] args) throws Exception {
		parseArgs(args);
		if(_mode.equals("batch")) {
			startBatchMode();
		}
		else startGUIMode();
		
	}

	private static void startGUIMode() throws Exception {
		GravityLaws law=_gravityLawsFactory.createInstance(_gravityLawsInfo);
		PhysicsSimulator sim=new PhysicsSimulator(_dtime,law);
		Controller controller=new Controller(sim,_bodyFactory,_gravityLawsFactory);
		controller.loadBodies(new FileInputStream(_inFile));
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
			MainWindow mw=	new MainWindow(controller);
			mw.setExtendedState(JFrame.MAXIMIZED_BOTH); 
			mw.setUndecorated(true);
			mw.setVisible(true);
			}
			});
		
	}

	public static void main(String[] args) {
		try {
			init();
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
		
		
	} 
}
