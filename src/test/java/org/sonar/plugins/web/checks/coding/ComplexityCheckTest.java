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

import org.junit.Rule;
import org.junit.Test;
import org.sonar.plugins.web.checks.CheckMessagesVerifierRule;
import org.sonar.plugins.web.checks.TestHelper;
import org.sonar.plugins.web.visitor.WebSourceCode;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;

public class ComplexityCheckTest {

  @Rule
  public CheckMessagesVerifierRule checkMessagesVerifier = new CheckMessagesVerifierRule();

  @Test
  public void detected() {
    ComplexityCheck check = new ComplexityCheck();
    assertThat(check.max).isEqualTo(10);
  }

  @Test
  public void custom() {
    ComplexityCheck check = new ComplexityCheck();
    check.max = 15;

    WebSourceCode sourceCode = TestHelper.scan(new File("src/test/resources/checks/ComplexityCheck.html"), check);

    checkMessagesVerifier.verify(sourceCode.getViolations())
      .next().atLine(null).withMessage("Complexity is 17 (max allowed is 15)");
  }

}
