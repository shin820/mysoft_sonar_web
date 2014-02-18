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
package org.sonar.plugins.web.checks.sonar;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.web.checks.AbstractPageCheck;
import org.sonar.plugins.web.node.Node;
import org.sonar.plugins.web.node.TagNode;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Rule(
  key = "InputWithoutLabelCheck",
  priority = Priority.MAJOR)
public class InputWithoutLabelCheck extends AbstractPageCheck {

  private static final Set<String> EXCLUDED_TYPES = ImmutableSet.of("SUBMIT", "BUTTON", "IMAGE", "HIDDEN");

  private final Set<String> labelFor = Sets.newHashSet();
  private final Map<String, Integer> inputIdToLine = Maps.newHashMap();

  @Override
  public void startDocument(List<Node> nodes) {
    labelFor.clear();
    inputIdToLine.clear();
  }

  @Override
  public void startElement(TagNode node) {
    if (isInputRequiredLabel(node) || isSelect(node) || isTextarea(node)) {
      String id = node.getAttribute("id");

      if (id == null) {
        createViolation(node.getStartLinePosition(), "Add an \"id\" attribute to this input field and associate it with a label.");
      } else {
        inputIdToLine.put(node.getAttribute("id"), node.getStartLinePosition());
      }
    } else if (isLabel(node) && node.getAttribute("for") != null) {
      labelFor.add(node.getAttribute("for"));
    }
  }

  private static boolean isSelect(TagNode node) {
    return isType(node, "SELECT");
  }

  private static boolean isTextarea(TagNode node) {
    return isType(node, "TEXTAREA");
  }

  private static boolean isInputRequiredLabel(TagNode node) {
    return isType(node, "INPUT") &&
      !hasExcludedType(node);
  }

  private static boolean isType(TagNode node, String type) {
    return type.equalsIgnoreCase(node.getNodeName());
  }

  private static boolean hasExcludedType(TagNode node) {
    String type = node.getAttribute("type");

    return type == null ||
      EXCLUDED_TYPES.contains(type.toUpperCase(Locale.ENGLISH));
  }

  private static boolean isLabel(TagNode node) {
    return "LABEL".equalsIgnoreCase(node.getNodeName());
  }

  @Override
  public void endDocument() {
    for (Map.Entry<String, Integer> entry : inputIdToLine.entrySet()) {
      if (!labelFor.contains(entry.getKey())) {
        createViolation(entry.getValue(), "Associate a valid label to this input field.");
      }
    }
  }

}
