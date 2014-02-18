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
package org.sonar.plugins.web.checks.structure;

import org.apache.commons.lang.StringUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.web.checks.AbstractPageCheck;
import org.sonar.plugins.web.node.Node;
import org.sonar.plugins.web.node.TagNode;

import java.util.List;

@Rule(
  key = "IllegalElementCheck",
  priority = Priority.MAJOR)
public class IllegalElementCheck extends AbstractPageCheck {

  private static final String DEFAULT_ELEMENTS = "";

  @RuleProperty(
    key = "elements",
    defaultValue = DEFAULT_ELEMENTS)
  public String elements = DEFAULT_ELEMENTS;

  private String[] elementsArray;

  @Override
  public void startDocument(List<Node> nodes) {
    elementsArray = trimSplitCommaSeparatedList(elements);
  }

  @Override
  public void startElement(TagNode element) {
    for (String elementName : elementsArray) {
      if (StringUtils.equalsIgnoreCase(element.getLocalName(), elementName)
        || StringUtils.equalsIgnoreCase(element.getNodeName(), elementName)) {
        createViolation(element.getStartLinePosition(), "The use of '" + elementName + "' element is forbidden.");
      }
    }
  }

}
