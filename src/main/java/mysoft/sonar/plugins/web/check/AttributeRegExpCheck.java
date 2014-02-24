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

import org.apache.commons.lang.StringUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.web.checks.AbstractPageCheck;
import org.sonar.plugins.web.node.Node;
import org.sonar.plugins.web.node.TagNode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Rule(key = "Mysoft_AttributeRegExpCheck",
        name = "Mysoft Rule : Attribute Check By Reg Expression",
        description = "通过正则表达式来检查属性值的格式是否正确",
        priority = Priority.MAJOR)
public class AttributeRegExpCheck extends AbstractPageCheck {

    private static final String DEFAULT_ATTRIBUTES = "";
    private final List<AttributeRule> attributeRules = new ArrayList<AttributeRule>();

    @RuleProperty(
            key = "attributes",
            defaultValue = DEFAULT_ATTRIBUTES)
    public String attributes = DEFAULT_ATTRIBUTES;

    private static final class AttributeRule {
        private String name;
        private String rule;
    }

    @Override
    public void startDocument(List<Node> nodes) {
        for (String item : trimSplitCommaSeparatedList(attributes)) {
            String[] pair = StringUtils.split(item, "=");
            if (pair.length <= 0) continue;

            AttributeRule a = new AttributeRule();
            a.name = pair[0].trim();
            a.rule = pair[1].trim();
            attributeRules.add(a);
        }
    }

    @Override
    public void startElement(TagNode node) {
        for (AttributeRule attributeRule : attributeRules) {
            String attributeName = attributeRule.name;
            String attributeValue = node.getAttribute(attributeRule.name);
            if (StringUtils.isEmpty(attributeValue)) continue;

            String rule = attributeRule.rule;
            Pattern pattern = Pattern.compile(rule);
            Matcher matcher = pattern.matcher(attributeValue);
            if (!matcher.find()) {
                createViolation(node.getStartLinePosition(), "元素属性" + attributeName + "不匹配正则表达式" + rule);
            }
        }
    }

}
