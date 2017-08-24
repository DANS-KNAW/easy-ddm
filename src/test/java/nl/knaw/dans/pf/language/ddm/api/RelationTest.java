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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class RelationTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { {"relation", "dc"}, {"isRequiredBy", "dct"}, {"conformsTo", "dct"}, {"isVersionOf", "dct"},
                {"hasVersion", "dct"}, {"isReplacedBy", "dct"}, {"replaces", "dct"}, {"isRequiredBy", "dct"}, {"requires", "dct"}, {"isPartOf", "dct"},
                {"hasPart", "dct"}, {"isReferencedBy", "dct"}, {"references", "dct"}, {"isFormatOf", "dct"}, {"hasFormat", "dct"}});
    }

    private String name;
    private String prefix;

    public RelationTest(String name, String prefix) {
        this.name = name;
        this.prefix = prefix;
    }

    @Test
    public void normalSubRelation() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM" +
                "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'" +
                "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'" +
                "  xmlns:dc='http://purl.org/dc/elements/1.1/'" +
                "  xmlns:abr='http://www.den.nl/standaard/166/Archeologisch-Basisregister/'" +
                "  xmlns:dcterms=\"http://purl.org/dc/terms/\"" +
                ">" +
                " <ddm:dcmiMetadata>" +
                "  <dcterms:" + this.name + ">some content</dcterms:" + this.name + "> " +
                " </ddm:dcmiMetadata>" +
                "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);
        DefaultElement sub = (DefaultElement) top.elements().get(0);

        assertThat(top.elements().size(), is(1));
        assertThat(top.getQualifiedName(), is("emd:relation"));
        assertThat(sub.elements().size(), is(0));
        assertThat(sub.getQualifiedName(), is(String.format("%s:%s", this.prefix, this.name)));
        assertThat(sub.getText(), is("some content"));
    }

    @Test
    public void linkedSubRelation() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM" +
                "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'" +
                "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'" +
                "  xmlns:dc='http://purl.org/dc/elements/1.1/'" +
                "  xmlns:abr='http://www.den.nl/standaard/166/Archeologisch-Basisregister/'" +
                ">" +
                " <ddm:dcmiMetadata>" +
                "  <ddm:" + this.name + " href=\"http://x\">some content</ddm:" + this.name + "> " +
                " </ddm:dcmiMetadata>" +
                "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);
        DefaultElement sub = (DefaultElement) top.elements().get(0);
        DefaultElement subsub1 = (DefaultElement) sub.elements().get(0);
        DefaultElement subsub2 = (DefaultElement) sub.elements().get(1);

        assertThat(top.elements().size(), is(1));
        assertThat(top.getQualifiedName(), is("emd:relation"));
        assertThat(sub.elements().size(), is(2));
        assertThat(sub.getQualifiedName(), is(String.format("eas:%s", this.name)));
        assertThat(subsub1.elements().size(), is(0));
        assertThat(subsub1.getQualifiedName(), is("eas:subject-title"));
        assertThat(subsub1.getText(), is("some content"));
        assertThat(subsub2.elements().size(), is(0));
        assertThat(subsub2.getQualifiedName(), is("eas:subject-link"));
        assertThat(subsub2.getText(), is("http://x"));
    }

    @Test
    public void linkedSubRelationNoTitle() throws Exception {
        // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM" +
                "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'" +
                "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'" +
                "  xmlns:dc='http://purl.org/dc/elements/1.1/'" +
                "  xmlns:abr='http://www.den.nl/standaard/166/Archeologisch-Basisregister/'" +
                ">" +
                " <ddm:dcmiMetadata>" +
                "  <ddm:" + this.name + " href=\"http://x\"/> "+
                " </ddm:dcmiMetadata>" +
                "</ddm:DDM>";
        // @formatter:on

        DefaultElement top = firstEmdElementFrom(ddm);
        DefaultElement sub = (DefaultElement) top.elements().get(0);
        DefaultElement subsub1 = (DefaultElement) sub.elements().get(0);

        assertThat(top.elements().size(), is(1));
        assertThat(top.getQualifiedName(), is("emd:relation"));
        assertThat(sub.elements().size(), is(1));
        assertThat(sub.getQualifiedName(), is(String.format("eas:%s", this.name)));
        assertThat(subsub1.elements().size(), is(0));
        assertThat(subsub1.getQualifiedName(), is("eas:subject-link"));
        assertThat(subsub1.getText(), is("http://x"));
    }

  @Test
  public void ddmSubRelationWithoutLink() throws Exception {
    // @formatter:off
        String ddm = "<?xml version='1.0' encoding='utf-8'?><ddm:DDM" +
                "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'" +
                "  xmlns:ddm='http://easy.dans.knaw.nl/schemas/md/ddm/'" +
                "  xmlns:dc='http://purl.org/dc/elements/1.1/'" +
                "  xmlns:abr='http://www.den.nl/standaard/166/Archeologisch-Basisregister/'" +
                ">" +
                " <ddm:dcmiMetadata>" +
                "  <ddm:" + this.name + ">some content</ddm:" + this.name + "> " +
                " </ddm:dcmiMetadata>" +
                "</ddm:DDM>";
        // @formatter:on

    DefaultElement top = firstEmdElementFrom(ddm);
    DefaultElement sub = (DefaultElement) top.elements().get(0);

    assertThat(top.elements().size(), is(1));
    assertThat(top.getQualifiedName(), is("emd:relation"));
    assertThat(sub.elements().size(), is(0));
    assertThat(sub.getQualifiedName(), is(String.format("%s:%s", this.prefix, this.name)));
    assertThat(sub.getText(), is("some content"));
  }

    private DefaultElement firstEmdElementFrom(String ddm) throws XMLSerializationException, CrosswalkException {
        EasyMetadata emd = new Ddm2EmdCrosswalk(null).createFrom(ddm);
        return (DefaultElement) new EmdMarshaller(emd).getXmlElement().elementIterator().next();
    }
}
