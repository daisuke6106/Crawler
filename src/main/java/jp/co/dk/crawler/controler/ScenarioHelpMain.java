package jp.co.dk.crawler.controler;

import java.util.Set;

import jp.co.dk.crawler.scenario.MoveScenario;
import jp.co.dk.crawler.scenario.MoveScenarioName;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class ScenarioHelpMain extends AbtractCrawlerControler {
	
	public static void main(String[] args) {
		new ScenarioHelpMain().execute(args);
	}

	@Override
	protected String getCommandName() {
		return "scenario_help";
	}

	@Override
	protected String getDescription() {
		StringBuilder description = new StringBuilder();
		description.append(this.getCommandName());
		description.append("は、開始URLのページからシナリオに指定された通りにページを巡回し、シナリオに紐付くアクションを行うコマンドです。").append(System.lineSeparator());
		description.append("オプションのURL(--url)に開始地点となるページのURL、オプションのシナリオ(--scenario)に巡回する際のシナリオとページを訪れた際に行うアクションを指定します。").append(System.lineSeparator());
		description.append("シナリオは「シナリオ@アクション」の形式で記述し、複数のシナリオはアロー(->)でつなげます。").append(System.lineSeparator());
		return description.toString();
	}

	@Override
	protected void getOptions(Options options) {
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withDescription("すべて").withLongOpt("all").create("a"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("scenario").withDescription("シナリオ名称").withLongOpt("scenario").create("s"));
	}

	@Override
	public void execute(){
		if (this.cmd.hasOption("all")) {
			Set<Class<?>> scenarioList = ClassGenerater.getClassesByAnnotation(MoveScenario.MOVE_SCENARIO_PACKAGE, MoveScenarioName.class);
			for (Class<?> scenarioClass : scenarioList) {
				MoveScenarioName moveScenarioName = scenarioClass.getAnnotationsByType(MoveScenarioName.class)[0];
				this.print(moveScenarioName.name()).print(" : ").println(moveScenarioName.manualTitle());
			}
			System.exit(0);
		} else if (this.cmd.hasOption("scenario")) {
			String[] scenarioNames = this.cmd.getArgs();
			Set<Class<?>> scenarioList = ClassGenerater.getClassesByAnnotation(MoveScenario.MOVE_SCENARIO_PACKAGE, MoveScenarioName.class);
			
			for (String scenarioName : scenarioNames) {
				for (Class<?> scenarioClass : scenarioList) {
					MoveScenarioName moveScenarioName = scenarioClass.getAnnotationsByType(MoveScenarioName.class)[0];
					
					if (scenarioName.equals(moveScenarioName.name())) {
						this.print("       [name] ").println(moveScenarioName.name());
						this.print("      [title] ").println(moveScenarioName.manualTitle());
						this.print("[description] ").println(moveScenarioName.manualText());
						String[] manualArguments = moveScenarioName.manualArgument();
						if (manualArguments == null || manualArguments.length == 0) {
							this.print("   [argument] nothing.");
						} else {
							this.print("   [argument] ");
							for (int i=0; i<manualArguments.length; i++) {
								if (i == 0) {
									this.println(manualArguments[i]);
								} else {
									this.println("              " + manualArguments[i]);
								}
							}
						}
						
					}
				}
			}
			
			
			System.exit(0);
		} else {
			this.printUsing();
			System.exit(1);
		}
	}
}

