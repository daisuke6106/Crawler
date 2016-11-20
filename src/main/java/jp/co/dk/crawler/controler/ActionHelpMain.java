package jp.co.dk.crawler.controler;

import java.util.Set;

import jp.co.dk.crawler.action.MoveAction;
import jp.co.dk.crawler.action.MoveActionName;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class ActionHelpMain extends AbtractCrawlerControler {
	
	public static void main(String[] args) {
		new ActionHelpMain().execute(args);
	}

	@Override
	protected String getCommandName() {
		return "action_help";
	}

	@Override
	protected String getDescription() {
		StringBuilder description = new StringBuilder();
		description.append(this.getCommandName());
		description.append("は、ページを訪れた際に行うアクションのマニュアルを参照するコマンドです。").append(System.lineSeparator());
		return description.toString();
	}

	@SuppressWarnings("all")
	@Override
	protected void getOptions(Options options) {
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withDescription("使用できるアクションを一覧で表示します。").withLongOpt("list").create("l"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("info").withDescription("指定のアクションの詳細を表示します。").withLongOpt("info").create("i"));
	}

	@Override
	public void execute(){
		if (this.cmd.hasOption("list")) {
			Set<Class<?>> actionList = AnnotationClassGenerater.getClassesByAnnotation(MoveAction.MOVE_ACTION_PACKAGE, MoveActionName.class);
			for (Class<?> actionClass : actionList) {
				MoveActionName MoveActionName = actionClass.getAnnotationsByType(MoveActionName.class)[0];
				this.print(MoveActionName.name()).print(" : ").println(MoveActionName.manualTitle());
			}
			System.exit(0);
		} else if (this.cmd.hasOption("info")) {
			String[] actionNames = this.cmd.getArgs();
			Set<Class<?>> actionList = AnnotationClassGenerater.getClassesByAnnotation(MoveAction.MOVE_ACTION_PACKAGE, MoveActionName.class);
			
			for (String actionName : actionNames) {
				for (Class<?> actionClass : actionList) {
					MoveActionName MoveActionName = actionClass.getAnnotationsByType(MoveActionName.class)[0];
					
					if (actionName.equals(MoveActionName.name())) {
						this.print("       [name] : ").println(MoveActionName.name());
						this.print("      [title] : ").println(MoveActionName.manualTitle());
						this.print("[description] : ").println(MoveActionName.manualText());
						String[] manualArguments = MoveActionName.manualArgument();
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
						String[] manualExample = MoveActionName.manualExample();
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

