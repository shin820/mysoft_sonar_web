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

import org.sonar.check.Cardinality;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.web.checks.AbstractPageCheck;
import org.sonar.plugins.web.node.TagNode;

import java.util.ArrayList;

@Rule(
  key = "Mysoft_ParentElementRequiredCheck",
 name = "Mysoft Rule : Parent Element Required Check",
  priority = Priority.MAJOR,
  cardinality = Cardinality.MULTIPLE)
public class ParentElementRequiredCheck extends AbstractPageCheck {

  private static final String DEFAULT_CHILD = "";
  private static final String DEFAULT_PARENTS = "";

  @RuleProperty(
    key = "child",
    defaultValue = DEFAULT_CHILD)
  public String child = DEFAULT_CHILD;

  @RuleProperty(
    key = "parents",
    defaultValue = DEFAULT_PARENTS)
  public String parent = DEFAULT_PARENTS;

  @Override
  public void startElement(TagNode element) {
    if(!element.equalsElementName(child))  return;

    if ( (element.getParent() == null || !hasParent(element))) {
      createViolation(element.getStartLinePosition(), "元素 '" + child + "' 必须有一个父元素 '" + parent+"'.");
    }
  }

  private boolean hasParent(TagNode element)
  {
      String[] parents=parent.split(",");
      for(String parent:parents)
      {
          if(parent.trim()=="") continue;
          if(element.getParent().equalsElementName(parent)) return true;
      }
      return false;
  }

}
