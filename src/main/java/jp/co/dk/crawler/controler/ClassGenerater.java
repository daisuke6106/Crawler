package jp.co.dk.crawler.controler;

import static jp.co.dk.crawler.message.CrawlerMessage.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import jp.co.dk.browzer.exception.MoveActionFatalException;

public class ClassGenerater {
	
	protected String classesStr;
	
	ClassGenerater(String classesStr) {
		this.classesStr = classesStr;
	}
	
	/**
	 * 構文解析時に使用するフェーズ定数です。
	 * @version 1.0
	 * @author D.Kanno
	 */
	private enum Phase {
		ClassPhase,
		ArgumentPhase,
		ArgumentParamPhase,
		ClosedPhase;
	}
	
	/**
	 * <p>クラスインスタンス一覧を生成し、返却します。</p>
	 * クラス名([引数,])の形式で記述された文字列を元に、MoveActin一覧を生成し、返却します。<br/>
	 * 例<br/>
	 * 単体指定の場合<br/>
	 * jp.co.example.MoveActionXXX()<br/>
	 * jp.co.example.MoveActionXXX('aaa','bbb')<br/>
	 * 複数指定の場合<br/>
	 * jp.co.example.MoveActionXXX();jp.co.example.MoveActionYYY()<br/>
	 * jp.co.example.MoveActionXXX('aaa','bbb');jp.co.example.MoveActionYYY('aaa','bbb')<br/>
	 * @param command クラス名([引数,])の形式で記述された文字列
	 * @return クラスインスタンス一覧
	 * @throws MoveActionFatalException 構文解析に失敗した場合
	 */
	protected List<Object> createObjectList() throws MoveActionFatalException {
		List<Object> classInstanceList = new ArrayList<>();
		
		Deque<Character> formatQue = new LinkedList<>();
		for (char chara : this.classesStr.toCharArray()) formatQue.offer(new Character(chara));
		
		String className = "";
		List<String> argumentList = new ArrayList<>();
		StringBuilder str = new StringBuilder();
		
		Phase nowPhase = Phase.ClassPhase;
		boolean isEscaped = false;
		
		while (formatQue.size() != 0) {
			char commandChar = formatQue.poll().charValue();
			if (isEscaped) {
				str.append(commandChar);
				isEscaped = false;
				continue;
			}
			switch (commandChar) {
				case ' ':
					if (nowPhase == Phase.ClassPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\" \"(スペース)の位置が不正です。", this.classesStr});
					} else if (nowPhase == Phase.ArgumentPhase) {
					} else if (nowPhase == Phase.ArgumentParamPhase) {
						str.append(commandChar);
					} else if (nowPhase == Phase.ClosedPhase) {
					}
					break;	
				case '(':
					if (nowPhase == Phase.ClassPhase) {
						nowPhase = Phase.ArgumentPhase;
						className = str.toString();
						str = new StringBuilder();
					} else if (nowPhase == Phase.ArgumentPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\"(\"の位置が不正です。", this.classesStr});
					} else if (nowPhase == Phase.ArgumentParamPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\"(\"の位置が不正です。", this.classesStr});
					} else if (nowPhase == Phase.ClosedPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\"(\"の位置が不正です。", this.classesStr});
					}
					break;
				case '\'':
					if (nowPhase == Phase.ClassPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\'の位置が不正です。", this.classesStr});
					} else if (nowPhase == Phase.ArgumentPhase) {
						nowPhase = Phase.ArgumentParamPhase;
					} else if (nowPhase == Phase.ArgumentParamPhase) {
						nowPhase = Phase.ArgumentPhase;
						argumentList.add(str.toString());
						str = new StringBuilder();
					} else if (nowPhase == Phase.ClosedPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\'\"の位置が不正です。", this.classesStr});
					}
					break;
				case ',':
					if (nowPhase == Phase.ClassPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\",\"の位置が不正です。", this.classesStr});
					}  else if (nowPhase == Phase.ArgumentPhase) {
					} else if (nowPhase == Phase.ArgumentParamPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\",\"の位置が不正です。", this.classesStr});
					} else if (nowPhase == Phase.ClosedPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\",\"の位置が不正です。", this.classesStr});
					}
					break;
				case ')':
					if (nowPhase == Phase.ClassPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\")\"の位置が不正です。", this.classesStr});
					}  else if (nowPhase == Phase.ArgumentPhase) {
						nowPhase = Phase.ClosedPhase;
					} else if (nowPhase == Phase.ArgumentParamPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\")\"の位置が不正です。", this.classesStr});
					} else if (nowPhase == Phase.ClosedPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\")\"の位置が不正です。", this.classesStr});
					}
					
					break;
				case ';':
					if (nowPhase == Phase.ClassPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\";\"の位置が不正です。", this.classesStr});
					}  else if (nowPhase == Phase.ArgumentPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\";\"の位置が不正です。", this.classesStr});
					} else if (nowPhase == Phase.ArgumentParamPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\";\"の位置が不正です。", this.classesStr});
					} else if (nowPhase == Phase.ClosedPhase) {
						nowPhase = Phase.ClassPhase;
						classInstanceList.add(createByClassName(className, argumentList.toArray(new String[0])));
					}
					break;
				case '\\':
					isEscaped = true;
					break;
				default:
					str.append(commandChar);
			}
		}
		if (nowPhase == Phase.ClassPhase) {
			classInstanceList.add(createByClassName(className, new String[]{}));
		}  else if (nowPhase == Phase.ArgumentPhase) {
			throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"引数が完結していません。", this.classesStr});
		} else if (nowPhase == Phase.ArgumentParamPhase) {
			throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"引数が完結していません。", this.classesStr});
		} else if (nowPhase == Phase.ClosedPhase) {
			nowPhase = Phase.ClassPhase;
			classInstanceList.add(createByClassName(className, argumentList.toArray(new String[0])));
		}
		return classInstanceList;
	}
	
	/**
	 * <p>MoveScenarioクラス生成</p>
	 * 引数に指定されたクラス名と、そのクラスのコンストラクタに引き渡す引数を基にMoveScenarioクラスを生成し、返却します。
	 * 
	 * @param className クラス名
	 * @param arguments コンストラクタに引き渡す引数
	 * @return MoveScenarioインスタンス
	 * @throws MoveActionFatalException クラス生成に失敗した場合
	 */
	@SuppressWarnings("all")
	protected Object createByClassName(String className, String[] arguments) throws MoveActionFatalException {
		Object object;
		try {
			Class<Object> classObject = (Class<Object>) Class.forName(className);
			Constructor<Object> objectConstructor = classObject.getDeclaredConstructor(new Class[]{String[].class});
			object = objectConstructor.newInstance(new Object[]{arguments});
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new MoveActionFatalException(FAILE_TO_CLASS_GENERATION, new String[]{e.getMessage(), className}, e);
		}
		return object;
	}

}
