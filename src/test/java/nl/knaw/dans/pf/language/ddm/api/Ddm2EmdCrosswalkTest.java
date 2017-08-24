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

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/** Without validation, so pure crosswalk tests that execute without web access. */
public class Ddm2EmdCrosswalkTest {

    @Test
    public void identifierWithIdTypeISBN() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM"
            + "  xmlns:dc='http://purl.org/dc/elements/1.1/'"
            + "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'"
            + "  xmlns:dcterms='http://purl.org/dc/terms/'"
            + "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
            + "  <ddm:dcmiMetadata>"
            + "    <dc:identifier xsi:type='id-type:ISBN'>123456</dc:identifier>"
            + "  </ddm:dcmiMetadata>"
            + "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);
        assertThat(top.elements().size(), is(1));
        assertThat(top.getQualifiedName(), is("emd:identifier"));

        DefaultElement sub = (DefaultElement) top.elements().get(0);
        assertThat(sub.getQualifiedName(), is("dc:identifier"));
        assertThat(sub.getText(), is("123456"));
        assertThat(sub.attributeCount(), is(1));
        assertThat(sub.attribute("scheme").getQualifiedName(), is("eas:scheme"));
        assertThat(sub.attribute("scheme").getValue(), is("ISBN"));
    }

    @Test
    public void identifierWithIdTypeISSN() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM"
            + "  xmlns:dc='http://purl.org/dc/elements/1.1/'"
            + "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'"
            + "  xmlns:dcterms='http://purl.org/dc/terms/'"
            + "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
            + "  <ddm:dcmiMetadata>"
            + "    <dc:identifier xsi:type='id-type:ISSN'>123456</dc:identifier>"
            + "  </ddm:dcmiMetadata>"
            + "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);
        assertThat(top.elements().size(), is(1));
        assertThat(top.getQualifiedName(), is("emd:identifier"));

        DefaultElement sub = (DefaultElement) top.elements().get(0);
        assertThat(sub.getQualifiedName(), is("dc:identifier"));
        assertThat(sub.getText(), is("123456"));
        assertThat(sub.attributeCount(), is(1));
        assertThat(sub.attribute("scheme").getQualifiedName(), is("eas:scheme"));
        assertThat(sub.attribute("scheme").getValue(), is("ISSN"));
    }

    @Test
    public void identifierWithIdTypeNwoProjectNummer() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM"
            + "  xmlns:dc='http://purl.org/dc/elements/1.1/'"
            + "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'"
            + "  xmlns:dcterms='http://purl.org/dc/terms/'"
            + "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
            + "  <ddm:dcmiMetadata>"
            + "    <dc:identifier xsi:type='id-type:NWO-PROJECTNR'>123456</dc:identifier>"
            + "  </ddm:dcmiMetadata>"
            + "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);
        assertThat(top.elements().size(), is(1));
        assertThat(top.getQualifiedName(), is("emd:identifier"));

        DefaultElement sub = (DefaultElement) top.elements().get(0);
        assertThat(sub.getQualifiedName(), is("dc:identifier"));
        assertThat(sub.getText(), is("123456"));
        assertThat(sub.attributeCount(), is(1));
        assertThat(sub.attribute("scheme").getQualifiedName(), is("eas:scheme"));
        assertThat(sub.attribute("scheme").getValue(), is("NWO-projectnummer"));
    }

    @Test
    public void identifierWithIdTypeOldArchis() throws Exception {
        // new archis number if it has less than 10 digits
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM"
            + "  xmlns:dc='http://purl.org/dc/elements/1.1/'"
            + "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'"
            + "  xmlns:dcterms='http://purl.org/dc/terms/'"
            + "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
            + "  <ddm:dcmiMetadata>"
            + "    <dc:identifier xsi:type='id-type:ARCHIS-ZAAK-IDENTIFICATIE'>123456789</dc:identifier>"
            + "  </ddm:dcmiMetadata>"
            + "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);
        assertThat(top.elements().size(), is(1));
        assertThat(top.getQualifiedName(), is("emd:identifier"));

        DefaultElement sub = (DefaultElement) top.elements().get(0);
        assertThat(sub.getQualifiedName(), is("dc:identifier"));
        assertThat(sub.getText(), is("123456789"));
        assertThat(sub.attributeCount(), is(3));
        assertThat(sub.attribute("scheme").getQualifiedName(), is("eas:scheme"));
        assertThat(sub.attribute("scheme").getValue(), is("Archis_onderzoek_m_nr"));
        assertThat(sub.attribute("schemeId").getQualifiedName(), is("eas:schemeId"));
        assertThat(sub.attribute("schemeId").getValue(), is("archaeology.dc.identifier"));
        assertThat(sub.attribute("identification-system").getQualifiedName(), is("eas:identification-system"));
        assertThat(sub.attribute("identification-system").getValue(), is("http://archis2.archis.nl"));
    }

    @Test
    public void identifierWithIdTypeNewArchis() throws Exception {
        // new archis number if it has exactly 10 digits
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM"
            + "  xmlns:dc='http://purl.org/dc/elements/1.1/'"
            + "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'"
            + "  xmlns:dcterms='http://purl.org/dc/terms/'"
            + "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
            + "  <ddm:dcmiMetadata>"
            + "    <dc:identifier xsi:type='id-type:ARCHIS-ZAAK-IDENTIFICATIE'>0123456789</dc:identifier>"
            + "  </ddm:dcmiMetadata>"
            + "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);
        assertThat(top.elements().size(), is(1));
        assertThat(top.getQualifiedName(), is("emd:identifier"));

        DefaultElement sub = (DefaultElement) top.elements().get(0);
        assertThat(sub.getQualifiedName(), is("dc:identifier"));
        assertThat(sub.getText(), is("0123456789"));
        assertThat(sub.attributeCount(), is(3));
        assertThat(sub.attribute("scheme").getQualifiedName(), is("eas:scheme"));
        assertThat(sub.attribute("scheme").getValue(), is("Archis_onderzoek_m_nr"));
        assertThat(sub.attribute("schemeId").getQualifiedName(), is("eas:schemeId"));
        assertThat(sub.attribute("schemeId").getValue(), is("archaeology.dc.identifier"));
        assertThat(sub.attribute("identification-system").getQualifiedName(), is("eas:identification-system"));
        assertThat(sub.attribute("identification-system").getValue(), is("https://archis.cultureelerfgoed.nl"));
    }

    @Test
    public void identifierWithoutIdType() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM"
            + "  xmlns:dc='http://purl.org/dc/elements/1.1/'"
            + "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'"
            + "  xmlns:dcterms='http://purl.org/dc/terms/'"
            + "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
            + "  <ddm:dcmiMetadata>"
            + "    <dc:identifier>ds1</dc:identifier>"
            + "  </ddm:dcmiMetadata>"
            + "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);
        assertThat(top.elements().size(), is(1));
        assertThat(top.getQualifiedName(), is("emd:identifier"));

        DefaultElement sub = (DefaultElement) top.elements().get(0);
        assertThat(sub.getQualifiedName(), is("dc:identifier"));
        assertThat(sub.getText(), is("ds1"));
        assertThat(sub.attributeCount(), is(0));
    }

    @Test
    public void formatWithSchemeAndId() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM"
            + "  xmlns:dc='http://purl.org/dc/elements/1.1/'"
            + "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'"
            + "  xmlns:dcterms='http://purl.org/dc/terms/'"
            + "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
            + "  <ddm:dcmiMetadata>"
            + "    <dc:format xsi:type='dcterms:IMT'>text/plain</dc:format>"
            + "    <dc:format>application/vnd.openxmlformats-officedocument.wordprocessingml.document</dc:format>"
            + "  </ddm:dcmiMetadata>"
            + "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);
        assertThat(top.elements().size(), is(2));
        assertThat(top.getQualifiedName(), is("emd:format"));

        DefaultElement sub1 = (DefaultElement) top.elements().get(0);
        assertThat(sub1.getQualifiedName(), is("dc:format"));
        assertThat(sub1.getText(), is("text/plain"));
        assertThat(sub1.attributeCount(), is(2));
        assertThat(sub1.attribute("scheme").getQualifiedName(), is("eas:scheme"));
        assertThat(sub1.attribute("scheme").getValue(), is("IMT"));
        assertThat(sub1.attribute("schemeId").getQualifiedName(), is("eas:schemeId"));
        assertThat(sub1.attribute("schemeId").getValue(), is("common.dc.format"));

        DefaultElement sub = (DefaultElement) top.elements().get(1);
        assertThat(sub.getQualifiedName(), is("dc:format"));
        assertThat(sub.getText(), is("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
        assertThat(sub.attributeCount(), is(0));
    }

    @Test
    public void typeWithSchemeAndId() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM"
            + "  xmlns:dc='http://purl.org/dc/elements/1.1/'"
            + "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'"
            + "  xmlns:dcterms='http://purl.org/dc/terms/'"
            + "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
            + "  <ddm:dcmiMetadata>"
            + "    <dcterms:type xsi:type='dcterms:DCMIType'>Text</dcterms:type>"
            + "  </ddm:dcmiMetadata>"
            + "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);
        assertThat(top.elements().size(), is(1));
        assertThat(top.getQualifiedName(), is("emd:type"));

        DefaultElement sub = (DefaultElement) top.elements().get(0);
        assertThat(sub.getQualifiedName(), is("dc:type"));
        assertThat(sub.getText(), is("Text"));
        assertThat(sub.attributeCount(), is(2));
        assertThat(sub.attribute("scheme").getQualifiedName(), is("eas:scheme"));
        assertThat(sub.attribute("scheme").getValue(), is("DCMI"));
        assertThat(sub.attribute("schemeId").getQualifiedName(), is("eas:schemeId"));
        assertThat(sub.attribute("schemeId").getValue(), is("common.dc.type"));
    }

    @Test
    public void languageWithSchemeAndIdToEmdCode() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM"
            + "  xmlns:dc='http://purl.org/dc/elements/1.1/'"
            + "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'"
            + "  xmlns:dcterms='http://purl.org/dc/terms/'"
            + "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
            + "  <ddm:dcmiMetadata>"
            + "    <dc:language xsi:type='dcterms:ISO639-2'>nld</dc:language>"
            + "  </ddm:dcmiMetadata>"
            + "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);
        assertThat(top.elements().size(), is(1));
        assertThat(top.getQualifiedName(), is("emd:language"));

        DefaultElement sub = (DefaultElement) top.elements().get(0);
        assertThat(sub.getQualifiedName(), is("dc:language"));
        assertThat(sub.getText(), is("dut/nld"));
        assertThat(sub.attributeCount(), is(2));
        assertThat(sub.attribute("scheme").getQualifiedName(), is("eas:scheme"));
        assertThat(sub.attribute("scheme").getValue(), is("ISO 639"));
        assertThat(sub.attribute("schemeId").getQualifiedName(), is("eas:schemeId"));
        assertThat(sub.attribute("schemeId").getValue(), is("common.dc.language"));
    }

    @Test
    public void languageWithSchemeAndIdButNoEmdCode() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM"
            + "  xmlns:dc='http://purl.org/dc/elements/1.1/'"
            + "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'"
            + "  xmlns:dcterms='http://purl.org/dc/terms/'"
            + "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
            + "  <ddm:dcmiMetadata>"
            + "    <dc:language xsi:type='dcterms:ISO639-2'>gre</dc:language>"
            + "  </ddm:dcmiMetadata>"
            + "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);
        assertThat(top.elements().size(), is(1));
        assertThat(top.getQualifiedName(), is("emd:language"));

        DefaultElement sub = (DefaultElement) top.elements().get(0);
        assertThat(sub.getQualifiedName(), is("dc:language"));
        assertThat(sub.getText(), is("gre"));
        assertThat(sub.attributeCount(), is(0));
    }

    @Test
    public void spatialGmlEnvelope() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM" +
                "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'" +
                "  xmlns:gml='http://www.opengis.net/gml'" +
                "  xmlns:dcx-gml='http://easy.dans.knaw.nl/schemas/dcx/gml/'" +
                ">" +
                " <ddm:dcmiMetadata>" +
                "  <dcx-gml:spatial>" +
                "   <gml:boundedBy>" +
                "    <gml:Envelope srsName='http://www.opengis.net/def/crs/EPSG/0/28992'>" +
                "     <gml:lowerCorner>83575.4 455271.2 1.12</gml:lowerCorner>" +
                "     <gml:upperCorner>83575 455271 1</gml:upperCorner>" +
                "    </gml:Envelope>" +
                "   </gml:boundedBy>" +
                "  </dcx-gml:spatial>" +
                " </ddm:dcmiMetadata>" +
                "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);
        DefaultElement sub = (DefaultElement) top.elements().get(0);
        DefaultElement box = (DefaultElement) sub.elements().get(1);
        List<DefaultElement> sides = box.elements();

        String scheme = box.attribute("scheme").getValue();
        assertThat(scheme, is("RD"));

        assertThat(top.elements().size(), is(1));
        assertThat(top.getQualifiedName(), is("emd:coverage"));
        assertThat(sub.getQualifiedName(), is("eas:spatial"));
        assertThat(box.getQualifiedName(), is("eas:box"));
        assertThat(sides.get(0).getQualifiedName(), is("eas:north"));
    }

    @Test
    public void spatialGmlPoints() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM" +
                "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'" +
                "  xmlns:gml='http://www.opengis.net/gml'" +
                "  xmlns:dcx-gml='http://easy.dans.knaw.nl/schemas/dcx/gml/'" +
                ">" +
                " <ddm:dcmiMetadata>" +
                "  <dcx-gml:spatial>" +
                "    <gml:Point srsName='http://www.opengis.net/def/crs/EPSG/0/28992'>" +
                "     <gml:pos>155000 463000</gml:pos>" + // x y
                "    </gml:Point>" +
                "  </dcx-gml:spatial>" +
                "  <dcx-gml:spatial>" +
                "    <gml:Point srsName='http://www.opengis.net/def/crs/EPSG/0/4326'>" +
                "     <gml:pos>52.155172 5.387203</gml:pos>" + // y x
                "    </gml:Point>" +
                "  </dcx-gml:spatial>" +
                "  <dcx-gml:spatial>" +
                "    <gml:Point srsName='http://un.kno.wn/by/easy'>" +
                "     <gml:pos>1 2</gml:pos>" + // EASY unknown coordinate system, make it 'locale'
                "    </gml:Point>" +
                "  </dcx-gml:spatial>" +
                " </ddm:dcmiMetadata>" +
                "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);

        assertThat(top.elements().size(), is(3));
        assertThat(top.getQualifiedName(), is("emd:coverage"));

        DefaultElement sub1 = (DefaultElement) top.elements().get(0);
        DefaultElement point1 = (DefaultElement) sub1.elements().get(1);
        List<DefaultElement> coordinates1 = point1.elements();
        DefaultElement x1 = coordinates1.get(0);
        assertThat(sub1.getQualifiedName(), is("eas:spatial"));
        assertThat(point1.getQualifiedName(), is("eas:point"));
        assertThat(coordinates1.get(0).getQualifiedName(), is("eas:x"));
        assertThat(x1.getStringValue(), is("155000"));
        assertThat(point1.attribute("scheme").getValue(), is("RD"));

        DefaultElement sub2 = (DefaultElement) top.elements().get(1);
        DefaultElement point2 = (DefaultElement) sub2.elements().get(1);
        List<DefaultElement> coordinates2 = point2.elements();
        DefaultElement x2 = coordinates2.get(0);
        assertThat(coordinates2.get(0).getQualifiedName(), is("eas:x"));
        assertThat(x2.getStringValue(), is("5.387203"));
        assertThat(point2.attribute("scheme").getValue(), is("degrees"));

        DefaultElement sub3 = (DefaultElement) top.elements().get(2);
        DefaultElement point3 = (DefaultElement) sub3.elements().get(1);
        assertThat(point3.attribute("scheme").getValue(), is("local"));
    }

    @Test
    public void spatialBox() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM" +
                "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'" +
                "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'" +
                "  xmlns:dcterms='http://purl.org/dc/terms/'" +
                ">" +
                " <ddm:dcmiMetadata>" +
                "  <dcterms:spatial xsi:type='dcterms:Box'>name=Western Australia; northlimit=-13.5; southlimit=-35.5; westlimit=112.5; eastlimit=129</dcterms:spatial>"+
                " </ddm:dcmiMetadata>" +
                "</ddm:DDM>";
        // @formatter:on

        // not copied to EMD (dcterms:Box does not appear in mapping of sword packaging document)
        assertThat(firstEmdElementFrom(ddm).getQualifiedName(), is("emd:other"));
    }

    @Test
    public void spatialPoint() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM" +
                "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'" +
                "  xmlns:dcterms='http://purl.org/dc/terms/'" +
                ">" +
                " <ddm:dcmiMetadata>" +
                "  <dcterms:spatial><Point><pos>1.0 2.0</pos></Point></dcterms:spatial>"+
                " </ddm:dcmiMetadata>" +
                "</ddm:DDM>";
        // @formatter:on

        DefaultElement firstEmdElement = firstEmdElementFrom(ddm);

        assertThat(firstEmdElement.elements().size(), is(1));
        assertThat(firstEmdElement.getQualifiedName(), is("emd:coverage"));
    }

    @Test
    public void temporalPlainText() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM" +
                "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'" +
                "  xmlns:dcterms='http://purl.org/dc/terms/'" +
                ">" +
                " <ddm:dcmiMetadata>" +
                "  <dcterms:temporal>1992-2016</dcterms:temporal>"+
                " </ddm:dcmiMetadata>" +
                "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);

        DefaultElement sub = (DefaultElement) top.elements().get(0);

        assertThat(top.elements().size(), is(1));
        assertThat(top.getQualifiedName(), is("emd:coverage"));
        assertThat(sub.getQualifiedName(), is("dct:temporal"));
        assertThat(sub.getText(), is("1992-2016"));
    }

    @Test
    public void temporalABR() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM" +
                "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'" +
                "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'" +
                "  xmlns:dcterms='http://purl.org/dc/terms/'" +
                "  xmlns:abr='http://www.den.nl/standaard/166/Archeologisch-Basisregister/'" +
                ">" +
                " <ddm:dcmiMetadata>" +
                "  <dcterms:temporal xsi:type='abr:ABRperiode'>PALEOV</dcterms:temporal>" +
                " </ddm:dcmiMetadata>" +
                "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);

        DefaultElement sub = (DefaultElement) top.elements().get(0);

        assertThat(top.elements().size(), is(1));
        assertThat(top.getQualifiedName(), is("emd:coverage"));
        assertThat(sub.getQualifiedName(), is("dct:temporal"));
        assertThat(sub.getText(), is("PALEOV"));
        assertThat(sub.attributeCount(), is(2));
        assertThat(sub.attribute("schemeId").getQualifiedName(), is("eas:schemeId"));
        assertThat(sub.attribute("schemeId").getValue(), is("archaeology.dcterms.temporal"));
        assertThat(sub.attribute("scheme").getQualifiedName(), is("eas:scheme"));
        assertThat(sub.attribute("scheme").getValue(), is("ABR"));
    }

    @Test
    public void subjectPlainText() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM" +
                "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'" +
                "  xmlns:dc='http://purl.org/dc/elements/1.1/'" +
                ">" +
                " <ddm:dcmiMetadata>" +
                "  <dc:subject>hello world</dc:subject>"+
                " </ddm:dcmiMetadata>" +
                "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);

        DefaultElement sub = (DefaultElement) top.elements().get(0);

        assertThat(top.elements().size(), is(1));
        assertThat(top.getQualifiedName(), is("emd:subject"));
        assertThat(sub.getQualifiedName(), is("dc:subject"));
        assertThat(sub.getText(), is("hello world"));
    }

    @Test
    public void subjectABR() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM" +
                "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'" +
                "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'" +
                "  xmlns:dc='http://purl.org/dc/elements/1.1/'" +
                "  xmlns:abr='http://www.den.nl/standaard/166/Archeologisch-Basisregister/'" +
                ">" +
                " <ddm:dcmiMetadata>" +
                "  <dc:subject xsi:type='abr:ABRcomplex'>DEPO</dc:subject>" +
                " </ddm:dcmiMetadata>" +
                "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);

        DefaultElement sub = (DefaultElement) top.elements().get(0);

        assertThat(top.elements().size(), is(1));
        assertThat(top.getQualifiedName(), is("emd:subject"));
        assertThat(sub.getQualifiedName(), is("dc:subject"));
        assertThat(sub.getText(), is("DEPO"));
        assertThat(sub.attributeCount(), is(2));
        assertThat(sub.attribute("schemeId").getQualifiedName(), is("eas:schemeId"));
        assertThat(sub.attribute("schemeId").getValue(), is("archaeology.dc.subject"));
        assertThat(sub.attribute("scheme").getQualifiedName(), is("eas:scheme"));
        assertThat(sub.attribute("scheme").getValue(), is("ABR"));
    }

    @Test
    public void streamingSurrogateRelation() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM" +
                "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'" +
                "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'" +
                "  xmlns:dc='http://purl.org/dc/elements/1.1/'" +
                "  xmlns:abr='http://www.den.nl/standaard/166/Archeologisch-Basisregister/'" +
                ">" +
                " <ddm:dcmiMetadata>" +
                "  <ddm:relation scheme='STREAMING_SURROGATE_RELATION'>presentation</ddm:relation> "+
                " </ddm:dcmiMetadata>" +
                "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);

        DefaultElement sub = (DefaultElement) top.elements().get(0);

        assertThat(top.elements().size(), is(1));
        assertThat(top.getQualifiedName(), is("emd:relation"));
        assertThat(sub.getQualifiedName(), is("dc:relation"));
        assertThat(sub.getText(), is("presentation"));
        assertThat(sub.attributeCount(), is(1));
        assertThat(sub.attribute("scheme").getQualifiedName(), is("eas:scheme"));
        assertThat(sub.attribute("scheme").getValue(), is("STREAMING_SURROGATE_RELATION"));
    }

    @Test
    public void normalRelation() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM" +
                "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'" +
                "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'" +
                "  xmlns:dc='http://purl.org/dc/elements/1.1/'" +
                "  xmlns:abr='http://www.den.nl/standaard/166/Archeologisch-Basisregister/'" +
                ">" +
                " <ddm:dcmiMetadata>" +
                "  <dc:relation>some content</dc:relation> "+
                " </ddm:dcmiMetadata>" +
                "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);
        DefaultElement sub = (DefaultElement) top.elements().get(0);

        assertThat(top.elements().size(), is(1));
        assertThat(top.getQualifiedName(), is("emd:relation"));
        assertThat(sub.elements().size(), is(0));
        assertThat(sub.getQualifiedName(), is("dc:relation"));
        assertThat(sub.getText(), is("some content"));
    }

    private DefaultElement firstEmdElementFrom(String ddm) throws XMLSerializationException, CrosswalkException {
        EasyMetadata emd = new Ddm2EmdCrosswalk(null).createFrom(ddm);
        return (DefaultElement) new EmdMarshaller(emd).getXmlElement().elementIterator().next();
    }
}
