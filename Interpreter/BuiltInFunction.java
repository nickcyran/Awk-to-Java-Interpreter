package assignment01;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Function;

public class BuiltInFunction implements DefineFunction {
	private Function<HashMap<String, InterpreterDataType>, String> doit;
	private LinkedList<String> parameterNames;
	private boolean variadic;
	
	BuiltInFunction(Function<HashMap<String, InterpreterDataType>, String> func, boolean v) {
		this.parameterNames =  new LinkedList<>();
		variadic = v;
		doit = func;
	}
	
	BuiltInFunction(Function<HashMap<String, InterpreterDataType>, String> func, String[] pNames, boolean v) {
		this(func, v);
		parameterNames.addAll(Arrays.asList(pNames));
	}

	public String execute(HashMap<String, InterpreterDataType> params) {
		return doit.apply(params);
	}
	
	public boolean isVariadic(){
		return variadic;
	}

	@Override
	public LinkedList<String> getParameterNames() {
		return parameterNames;
	}
}
