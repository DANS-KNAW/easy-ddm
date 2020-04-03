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
package nl.knaw.dans.pf.language.ddm.api;
import nl.knaw.dans.pf.language.emd.EasyMetadata;
import nl.knaw.dans.pf.language.emd.binding.EmdMarshaller;
import nl.knaw.dans.pf.language.xml.crosswalk.CrosswalkException;
import nl.knaw.dans.pf.language.xml.exc.XMLSerializationException;
import org.dom4j.tree.DefaultElement;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class FundingTest {


  @Test
  public void fundingMapTest() throws Exception {
    // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM" +
                "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'" +
                "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'" +
                "  xmlns:dc='http://purl.org/dc/elements/1.1/'" +
                "  xmlns:abr='http://www.den.nl/standaard/166/Archeologisch-Basisregister/'" +
                "  xmlns:dcterms=\"http://purl.org/dc/terms/\"" +
                ">" +
                " <ddm:dcmiMetadata>" +
                "  <ddm:funding>"
                  + "<ddm:funderName>NWO</ddm:funderName>"
                  + "<ddm:funderIdentifier funderIdentifierType='ISNI'>123</ddm:funderIdentifier>"
                + "</ddm:funding>" +
                " </ddm:dcmiMetadata>" +
                "</ddm:DDM>";
        // @formatter:on

    /*
    <emd:contributor>
      <eas:contributor>
        <eas:organization>NWO</eas:organization>
        <eas:organizationId scheme="ISNI">123</eas:organizationId>
        <eas:role scheme="EASY">Funder</eas:role>
      <eas:contributor>
    </emd:contributor>
     */

    DefaultElement top = firstEmdElementFrom(ddm);
    DefaultElement sub = (DefaultElement) top.elements().get(0);
    DefaultElement organization = (DefaultElement) sub.elements().get(0);
    DefaultElement organizationId = (DefaultElement) sub.elements().get(1);
    DefaultElement role = (DefaultElement) sub.elements().get(2);

    assertThat(top.elements().size(), is(1));
    assertThat(top.getQualifiedName(), is("emd:contributor"));
    assertThat(sub.elements().size(), is(3));
    assertThat(sub.getQualifiedName(), is("eas:contributor"));
    assertEquals("eas:organization", organization.getQualifiedName());
    assertEquals("NWO", organization.getText());

    assertEquals("eas:organizationId", organizationId.getQualifiedName());
    assertEquals(1, organizationId.attributeCount());
    assertEquals("ISNI", organizationId.attribute("scheme").getValue());
    assertEquals("123", organizationId.getText());

    assertEquals("eas:role", role.getQualifiedName());
    assertEquals("Funder", role.getText());
    assertEquals("EASY", role.attribute("scheme").getValue());
  }


  private DefaultElement firstEmdElementFrom(String ddm) throws XMLSerializationException, CrosswalkException {
    EasyMetadata emd = new Ddm2EmdCrosswalk(null).createFrom(ddm);
    return (DefaultElement) new EmdMarshaller(emd).getXmlElement().elementIterator().next();
  }
}
