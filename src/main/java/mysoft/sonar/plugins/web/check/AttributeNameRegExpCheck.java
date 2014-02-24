/*
 * SonarQube Web Plugin
 * Copyright (C) 2010 SonarSource and Matthijs Galesloot
 * dev@sonar.codehaus.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mysoft.sonar.plugins.web.check;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.web.checks.AbstractPageCheck;
import org.sonar.plugins.web.node.Attribute;
import org.sonar.plugins.web.node.TagNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Rule(key = "Mysoft_AttributeNameRegExpCheck",
        name = "Mysoft Rule : Attribute Name Check By Reg Expression",
        description = "通过正则表达式来检查属性名的格式是否正确",
        priority = Priority.MAJOR)
public class AttributeNameRegExpCheck extends AbstractPageCheck {

    private static final String DEFAULT_REG_EXPRESSION = "^[^A-Z]+$";

    @RuleProperty(
            key = "regExpression",
            defaultValue = DEFAULT_REG_EXPRESSION)
    public String regExpression = DEFAULT_REG_EXPRESSION;

    @Override
    public void startElement(TagNode node) {
         for(Attribute attribute:node.getAttributes())
         {
             String attributeName=attribute.getName();
             Pattern pattern = Pattern.compile(regExpression);
             Matcher matcher = pattern.matcher(attributeName);
             if(!matcher.find())
             {
                 createViolation(node.getStartLinePosition(), "元素名称" + attributeName + "不匹配正则表达式" + regExpression);
             }
         }
    }

}
