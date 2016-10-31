package jp.co.dk.crawler.controler;

import java.util.Set;

import jp.co.dk.crawler.scenario.action.MoveAction;
import jp.co.dk.crawler.scenario.action.MoveActionName;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class ActionHelpMain extends AbtractCrawlerControler {
	
	public static void main(String[] args) {
		new ActionHelpMain().execute(args);
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
			Set<Class<?>> actionList = ClassGenerater.getClassesByAnnotation(MoveAction.MOVE_ACTION_PACKAGE, MoveActionName.class);
			for (Class<?> actionClass : actionList) {
				MoveActionName MoveActionName = actionClass.getAnnotationsByType(MoveActionName.class)[0];
				this.print(MoveActionName.name()).print(" : ").println(MoveActionName.manualTitle());
			}
			System.exit(0);
		} else if (this.cmd.hasOption("action")) {
			String[] actionNames = this.cmd.getArgs();
			Set<Class<?>> actionList = ClassGenerater.getClassesByAnnotation(MoveAction.MOVE_ACTION_PACKAGE, MoveActionName.class);
			
			for (String actionName : actionNames) {
				for (Class<?> actionClass : actionList) {
					MoveActionName MoveActionName = actionClass.getAnnotationsByType(MoveActionName.class)[0];
					
					if (actionName.equals(MoveActionName.name())) {
						this.print("       [name] : ").println(MoveActionName.name());
						this.print("      [title] : ").println(MoveActionName.manualTitle());
						this.print("[description] : ").println(MoveActionName.manualText());
						String[] manualArguments = MoveActionName.manualArgument();
						if (manualArguments == null || manualArguments.length == 0) {
							this.print("   [argument] : nothing.");
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
						String[] manualExample = MoveActionName.manualExample();
						if (manualExample == null || manualExample.length == 0) {
							this.print("    [example] : nothing.");
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

