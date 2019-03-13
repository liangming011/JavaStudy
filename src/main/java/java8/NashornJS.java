package java8;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class NashornJS {

    //Java 中调用 JavaScript
    //使用 ScriptEngineManager, JavaScript 代码可以在 Java 中执行，实例如下：

    public static void main(String args[]){

        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine nashorn = scriptEngineManager.getEngineByName("nashorn");

        String name = "Runoob";
        Integer result = null;

        try {
            nashorn.eval("print('" + name + "')");
            result = (Integer) nashorn.eval("10 + 2");

        }catch(ScriptException e){
            System.out.println("执行脚本错误: "+ e.getMessage());
        }

        System.out.println(result.toString());
    }
}
