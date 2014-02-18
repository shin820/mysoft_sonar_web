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
package org.sonar.plugins.web.checks.comments;

import org.sonar.plugins.web.checks.TestHelper;

import org.junit.Rule;
import org.junit.Test;
import org.sonar.plugins.web.checks.AbstractCheckTester;
import org.sonar.plugins.web.checks.CheckMessagesVerifierRule;
import org.sonar.plugins.web.visitor.WebSourceCode;

import java.io.File;

public class AvoidHtmlCommentCheckTest extends AbstractCheckTester {

  @Rule
  public CheckMessagesVerifierRule checkMessagesVerifier = new CheckMessagesVerifierRule();

  @Test
  public void should_detect_on_jsp_documents() {
    WebSourceCode sourceCode = TestHelper.scan(new File("src/test/resources/checks/AvoidHtmlCommentCheck/document.jsp"), new AvoidHtmlCommentCheck());

    checkMessagesVerifier.verify(sourceCode.getViolations())
        .next().atLine(2).withMessage("Remove this HTML comment.")
        .next().atLine(4);
  }

  @Test
  public void should_not_detect_on_xml_documents() {
    WebSourceCode sourceCode = TestHelper.scan(new File("src/test/resources/checks/AvoidHtmlCommentCheck/document.xml"), new AvoidHtmlCommentCheck());

    checkMessagesVerifier.verify(sourceCode.getViolations());
  }

}
