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
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Rule(key = "Mysoft_StyleAttributeCheck",
        name = "Mysoft Rule : Style Attribute Check",
        description = "通过正则表达式来检查样式属性的格式是否正确",
        priority = Priority.MAJOR)
public class StyleAttributeRegExpCheck extends AbstractPageCheck {

    private static final String DEFAULT_ATTRIBUTES = "";
    private final List<StyleRule> styleRules = new ArrayList<StyleRule>();

    @RuleProperty(
            key = "attributes",
            defaultValue = DEFAULT_ATTRIBUTES)
    public String attributes = DEFAULT_ATTRIBUTES;

    private static final class StyleRule {
        private String name;
        private String rule;
    }

    @Override
    public void startDocument(List<Node> nodes) {
        for (String item : trimSplitCommaSeparatedList(attributes)) {
            String[] pair = StringUtils.split(item, "=");
            if (pair.length <= 0) continue;

            StyleRule a = new StyleRule();
            a.name = pair[0].trim();
            a.rule = pair[1].trim();
            styleRules.add(a);
        }
    }

    @Override
    public void startElement(TagNode node) {
        HashMap<String, String> styles = GetStyles(node);
        if (styles.size() <= 0) return;

        CheckStyleRules(node, styles);
    }

    private void CheckStyleRules(TagNode node, HashMap<String, String> styles) {
        for (StyleRule styleRule : styleRules) {
            String attributeName = styleRule.name;
            if (!styles.containsKey(attributeName)) continue;

            String value = styles.get(attributeName);
            String rule = styleRule.rule;
            Pattern pattern = Pattern.compile(rule);
            Matcher matcher = pattern.matcher(value);
            if (!matcher.find()) {
                createViolation(node.getStartLinePosition(), "样式属性" + attributeName + "不匹配正则表达式" + rule);
            }
        }
    }

    private HashMap<String, String> GetStyles(TagNode node) {

        HashMap<String, String> hash = new HashMap<String, String>();
        String inlineStyle = node.getAttribute("style");
        if (StringUtils.isEmpty(inlineStyle)) return hash;

        String[] styles = StringUtils.split(inlineStyle, ";");
        if (styles.length <= 0) return hash;

        for (String style : styles) {
            if (StringUtils.isEmpty(style)) continue;
            String[] pair = StringUtils.split(style, ":");
            if (pair.length <= 0) continue;
            hash.put(pair[0].trim(), pair[1].trim());
        }

        return hash;
    }
}
