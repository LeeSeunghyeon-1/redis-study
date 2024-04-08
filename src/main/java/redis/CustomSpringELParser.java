package redis;

import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @DistriSpringEXpression 으로 전달하고 이를 파싱하는 util 클래스
 */

public class CustomSpringELParser {

    public static Object getDynamicValue(String[] parameterNames, Object[] args, String key) {
        SpelExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        return parser.parseExpression(key).getValue(context, Object.class);
    }
}
