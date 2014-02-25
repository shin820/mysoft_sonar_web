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

@Rule(key = "Mysoft_OnlyOneChildCheck",
        name = "Mysoft Rule : Only One Child Check",
        description = "检测元素是否只存在唯一的子元素,如table下的thead,tfoot等",
        priority = Priority.MAJOR)
public class OnlyOneChildCheck extends AbstractPageCheck {

    private static final String DEFAULT_ELEMENTS = "";
    private final List<ElementRule> elementRules = new ArrayList<ElementRule>();

    @RuleProperty(
            key = "elements",
            defaultValue = DEFAULT_ELEMENTS)
    public String elements = DEFAULT_ELEMENTS;

    private static final class ElementRule {
        private String parent;
        private String child;
    }

    @Override
    public void startDocument(List<Node> nodes) {
        for (String item : trimSplitCommaSeparatedList(elements)) {
            String[] pair = StringUtils.split(item, ".");
            if (pair.length != 2) continue;

            ElementRule a = new ElementRule();
            a.parent = pair[0].trim();
            a.child = pair[1].trim();
            elementRules.add(a);
        }
    }

    @Override
    public void startElement(TagNode node) {
        for (ElementRule elementRule : elementRules) {
            String parent = elementRule.parent;
            String child = elementRule.child;

            if (!node.equalsElementName(parent)) continue;

            List<TagNode> children = node.getChildren();
            if (hasMultipleChildren(children, child)) {
                createViolation(node.getStartLinePosition(), "元素 '" + parent + "' 下只允许存在一个 '" + child + "' 元素");
            }
        }
    }

    private boolean hasMultipleChildren(List<TagNode> children, String childName) {
        int totalChild = 0;
        for (TagNode child : children) {
            if (child.equalsElementName(childName)) {
                totalChild++;
            }
        }
        return totalChild > 1;
    }

}
