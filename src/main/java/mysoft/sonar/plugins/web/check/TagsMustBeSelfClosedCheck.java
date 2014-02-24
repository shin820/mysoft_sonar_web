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
import org.sonar.plugins.web.node.NodeType;
import org.sonar.plugins.web.node.TagNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Rule(key = "Mysoft_TagsMustBeSelfClosedCheck",
        name = "Mysoft Rule : Tags Must be Self-Closed Check",
        description = "标签必须使用自关闭的形式",
        priority = Priority.MAJOR)
public class TagsMustBeSelfClosedCheck extends AbstractPageCheck {

    private static final String DEFAULT_TAGS = "";

    @RuleProperty(
            key = "tags",
            defaultValue = DEFAULT_TAGS)
    public String tags = DEFAULT_TAGS;

    @Override
    public void startElement(TagNode node) {

        if (node.getNodeType() != NodeType.TAG) return;

        if (isInSelfClosedTags(node) && !node.getCode().endsWith("/>")) {
            createViolation(node.getStartLinePosition(), "标签" + node.getNodeName() + "必须自关闭");
        }
    }

    private boolean isInSelfClosedTags(TagNode node) {
        String[] selfClosedTags = tags.split(",");
        for (String tag : selfClosedTags) {
            if (node.equalsElementName(tag)) {
                return true;
            }
        }
        return false;
    }

}
