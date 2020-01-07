/**
 * Copyright (C) 2014 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
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
package nl.knaw.dans.pf.language.ddm.handlers;

import nl.knaw.dans.pf.language.ddm.handlertypes.BasicStringHandler;
import nl.knaw.dans.pf.language.emd.types.BasicString;
import org.xml.sax.SAXException;

public class DescriptionHandler extends BasicStringHandler {

  @Override
  protected void finishElement(final String uri, final String localName) throws SAXException {
    final BasicString basicString = createBasicString(uri, localName);

    if (basicString != null) {
      String oldPrefix = "Instructions for Reuse: ";
      String prefix = "Suggestions for data usage: ";
      String value = basicString.getValue();
      String desciptionType = getAttribute("", "descriptionType");
      boolean isTechnicalDescription = desciptionType != null && desciptionType.equals("TechnicalInfo");
      boolean hasOldPrefix = value.toLowerCase().startsWith(oldPrefix.toLowerCase());
      boolean hasPrefix = value.toLowerCase().startsWith(prefix.toLowerCase());

      if (isTechnicalDescription && hasOldPrefix)
        value = value.replaceFirst(oldPrefix, "");

      if (isTechnicalDescription && !hasPrefix)
        basicString.setValue(prefix + value);

      getTarget().getEmdDescription().getDcDescription().add(basicString);
    }
  }
}
