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
package org.sonar.plugins.web.core;

import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.AbstractLanguage;
import org.sonar.plugins.web.api.WebConstants;

@Properties({
  @Property(
    key = WebConstants.FILE_EXTENSIONS_PROP_KEY,
    name = "File extensions",
    description = "List of file extensions that will be scanned.",
    defaultValue = WebConstants.FILE_EXTENSIONS_DEF_VALUE,
    global = true,
    project = true)
})
public class Web extends AbstractLanguage {

  private final String[] fileSuffixes;

  public Web(Settings settings) {
    super(WebConstants.LANGUAGE_KEY, WebConstants.LANGUAGE_NAME);
    fileSuffixes = settings.getStringArray(WebConstants.FILE_EXTENSIONS_PROP_KEY);
  }

  @Override
  public String[] getFileSuffixes() {
    return fileSuffixes;
  }

}
