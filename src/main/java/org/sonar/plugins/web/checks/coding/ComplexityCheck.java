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
package org.sonar.plugins.web.checks.coding;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.web.checks.AbstractPageCheck;
import org.sonar.plugins.web.node.Node;

import java.util.List;

@Rule(
  key = "ComplexityCheck",
  priority = Priority.MINOR)
public final class ComplexityCheck extends AbstractPageCheck {

  private static final int DEFAULT_MAX_COMPLEXITY = 10;

  @RuleProperty(
    key = "max",
    defaultValue = "" + DEFAULT_MAX_COMPLEXITY)
  public int max = DEFAULT_MAX_COMPLEXITY;

  @Override
  public void startDocument(List<Node> nodes) {
    int complexity = getWebSourceCode().getMeasure(CoreMetrics.COMPLEXITY).getIntValue();

    if (complexity > max) {
      String msg = String.format("Complexity is %d (max allowed is %d)", complexity, max);
      createViolation(0, msg);
    }
  }

}
