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

import org.junit.Rule;
import org.junit.Test;
import org.sonar.api.rules.Violation;
import org.sonar.plugins.web.checks.AbstractCheckTester;
import org.sonar.plugins.web.checks.CheckMessagesVerifierRule;
import org.sonar.plugins.web.checks.TestHelper;
import org.sonar.plugins.web.visitor.WebSourceCode;

import java.io.File;
import java.util.List;

/**
 * Created by liux09 on 14-2-18.
 */
public class StyleAttributeRegExpCheckTest extends AbstractCheckTester {

    @Rule
    public CheckMessagesVerifierRule checkMessagesVerifier = new CheckMessagesVerifierRule();

    @Test
    public void should_detect_on_html_documents() {

        StyleAttributeRegExpCheck styleAttributeRegExpCheck = new StyleAttributeRegExpCheck();
        styleAttributeRegExpCheck.attributes = "height=^\\d+px$,weight=^\\d+px$";
        WebSourceCode sourceCode = TestHelper.scan(new File("src/test/resources/checks/Mysoft/StyleAttributeRegExpCheck.html"), styleAttributeRegExpCheck);

        List<Violation> violations = sourceCode.getViolations();
        checkMessagesVerifier.verify(violations)
                .next().atLine(1).withMessage("样式属性height不匹配正则表达式^\\d+px$")
                .next().atLine(3).withMessage("样式属性weight不匹配正则表达式^\\d+px$");
    }
}
