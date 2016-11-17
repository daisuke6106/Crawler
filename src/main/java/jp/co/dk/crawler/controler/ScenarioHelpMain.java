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
		description.append("は、ページの巡回方法を指定するシナリオのマニュアルを参照するコマンドです。").append(System.lineSeparator());
		return description.toString();
	}

	@SuppressWarnings("all")
	@Override
	protected void getOptions(Options options) {
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withDescription("使用できるシナリオを一覧で表示します。").withLongOpt("list").create("l"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("info").withDescription("指定のシナリオの詳細を表示します。").withLongOpt("info").create("i"));
	}

	@Override
	public void execute(){
		if (this.cmd.hasOption("list")) {
			Set<Class<?>> scenarioList = AnnotationClassGenerater.getClassesByAnnotation(MoveScenario.MOVE_SCENARIO_PACKAGE, MoveScenarioName.class);
			for (Class<?> scenarioClass : scenarioList) {
				MoveScenarioName moveScenarioName = scenarioClass.getAnnotationsByType(MoveScenarioName.class)[0];
				this.print(moveScenarioName.name()).print(" : ").println(moveScenarioName.manualTitle());
			}
			System.exit(0);
		} else if (this.cmd.hasOption("info")) {
			String[] scenarioNames = this.cmd.getArgs();
			Set<Class<?>> scenarioList = AnnotationClassGenerater.getClassesByAnnotation(MoveScenario.MOVE_SCENARIO_PACKAGE, MoveScenarioName.class);
			
			for (String scenarioName : scenarioNames) {
				for (Class<?> scenarioClass : scenarioList) {
					MoveScenarioName moveScenarioName = scenarioClass.getAnnotationsByType(MoveScenarioName.class)[0];
					
					if (scenarioName.equals(moveScenarioName.name())) {
						this.print("       [name] : ").println(moveScenarioName.name());
						this.print("      [title] : ").println(moveScenarioName.manualTitle());
						this.print("[description] : ").println(moveScenarioName.manualText());
						String[] manualArguments = moveScenarioName.manualArgument();
						if (manualArguments == null || manualArguments.length == 0) {
							this.println("   [argument] : nothing.");
						} else {
							this.print("   [argument] : ");
							for (int i=0; i<manualArguments.length; i++) {
								if (i == 0) {
									this.println(manualArguments[i]);
								} else {
									this.println("              " + manualArguments[i]);
								}
							}
						}
						String[] manualExample = moveScenarioName.manualExample();
						if (manualExample == null || manualExample.length == 0) {
							this.println("    [example] : nothing.");
						} else {
							this.print("    [example] : ");
							for (int i=0; i<manualExample.length; i++) {
								if (i == 0) {
									this.println(manualExample[i]);
								} else {
									this.println("              " + manualExample[i]);
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

