package jp.co.dk.crawler.controler;

import java.util.Set;

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
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("scenario").withDescription("シナリオ名称").withLongOpt("name").create("n"));
	}

	@Override
	public void execute(){
		if (this.cmd.hasOption("all")) {
			Set<Class<?>> scenarioList = ClassGenerater.getClassesByAnnotation("jp.co.dk", MoveScenarioName.class);
			for (Class<?> scenarioClass : scenarioList) System.out.println(scenarioClass.getName());
			System.exit(0);
		}
	}
}

