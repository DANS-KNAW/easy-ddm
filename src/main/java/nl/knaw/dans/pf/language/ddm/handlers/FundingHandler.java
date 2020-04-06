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

import nl.knaw.dans.pf.language.emd.EasyMetadata;
import nl.knaw.dans.pf.language.emd.types.Author;
import nl.knaw.dans.pf.language.emd.types.EntityId;
import nl.knaw.dans.pf.language.xml.crosswalk.CrosswalkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class FundingHandler  extends CrosswalkHandler<EasyMetadata> {
  private static final Logger logger = LoggerFactory.getLogger(FundingHandler.class);
  protected Author author;

  @Override
  public void initFirstElement(final String uri, final String localName, final Attributes attributes) {
    author = new Author();
    getTarget().getEmdContributor().getEasContributor().add(author);
  }

  @Override
  protected void finishElement(final String uri, final String localName) throws SAXException {
    if ("funding".equals(localName)) {
      author.setRole(new Author.Role("Funder", "EASY"));
    }
    else {
      final String value = getCharsSinceStart().trim();
      if (value.length() == 0)
        return;
      if ("funderName".equals(localName))
        author.setOrganization(value);
      else if ("funderIdentifier".equals(localName))
        setOrganizationId(author, value, uri);
    }
  }

  private void setOrganizationId(Author author, String value, final String uri) {
    String funderIdentifierType = this.getAttribute("", "funderIdentifierType");
    EntityId orgId = new EntityId();
    orgId.setEntityId(value);
    orgId.setScheme(funderIdentifierType);
    author.setOrganizationIdHolder(orgId);
  }
}
