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
import org.sonar.plugins.web.checks.AbstractPageCheck;
import org.sonar.plugins.web.node.CommentNode;
import org.sonar.plugins.web.node.DirectiveNode;
import org.sonar.plugins.web.node.Node;

import java.util.List;

@Rule(key = "Mysoft_AvoidConditionalHtmlCommentCheck",
        name = "Mysoft Rule : Avoid Conditional Html Comment Check",
        description = "不允许使用HTML条件注释",
        priority = Priority.MAJOR)
public class AvoidConditionalHtmlCommentCheck extends AbstractPageCheck {
    private boolean isXml;

    @Override
    public void comment(CommentNode node) {
        String comment = node.getCode();

        boolean isConditionalComment = !isXml && node.isHtml() && comment.toLowerCase().contains("<![endif]");
        if (isConditionalComment) {
            createViolation(node.getStartLinePosition(), "移除HTML条件注释");
        }
    }

    @Override
    public void directive(DirectiveNode node) {
        if (node.isXml()) {
            isXml = true;
        }
    }

    @Override
    public void startDocument(List<Node> nodes) {
        isXml = false;
    }
}
