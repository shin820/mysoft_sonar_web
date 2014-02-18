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

import org.sonar.check.Cardinality;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.web.checks.AbstractPageCheck;
import org.sonar.plugins.web.node.TagNode;

/**
 * Checker for required parent element.
 *
 * e.g. title must have parent element head.
 */
@Rule(
  key = "ParentElementRequiredCheck",
  priority = Priority.MAJOR,
  cardinality = Cardinality.MULTIPLE)
public class ParentElementRequiredCheck extends AbstractPageCheck {

  private static final String DEFAULT_CHILD = "";
  private static final String DEFAULT_PARENT = "";

  @RuleProperty(
    key = "child",
    defaultValue = DEFAULT_CHILD)
  public String child = DEFAULT_CHILD;

  @RuleProperty(
    key = "parent",
    defaultValue = DEFAULT_PARENT)
  public String parent = DEFAULT_PARENT;

  @Override
  public void startElement(TagNode element) {
    if (element.equalsElementName(child) && (element.getParent() == null || !element.getParent().equalsElementName(parent))) {
      createViolation(element.getStartLinePosition(), "The element '" + child + "' must have a '" + parent + "' parent.");
    }
  }

}
